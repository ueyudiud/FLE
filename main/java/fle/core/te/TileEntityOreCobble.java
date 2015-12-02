package fle.core.te;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.enums.EnumWorldNBT;
import fle.api.material.MaterialOre;
import fle.api.te.TEBase;
import fle.core.block.BlockOreCobble;
import fle.core.energy.ThermalTileHelper;
import fle.core.item.ItemOre;
import fle.core.net.FleTEPacket;
import fle.core.recipe.OreMeltingRecipe;

public class TileEntityOreCobble extends TEBase implements IThermalTileEntity
{
	double progress;
	double amount = 1000D;
	MaterialOre ore;
	ThermalTileHelper hc;
	
	public TileEntityOreCobble()
	{
		hc = new ThermalTileHelper(1000F, 1.0F);
	}
	
	public void init(MaterialOre aOre)
	{
		ore = aOre;
		hc = new ThermalTileHelper(aOre);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		amount = nbt.getDouble("Amount");
		progress = nbt.getDouble("Progress");
		ore = MaterialOre.getOreFromName(nbt.getString("Ore"));
		hc.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setDouble("Amount", amount);
		nbt.setDouble("Progress", progress);
		nbt.setString("Ore", ore.getOreName());
		hc.writeToNBT(nbt);
	}
	
	public MaterialOre getOre()
	{
		return ore;
	}
	
	short buf = 0;
	
	@Override
	public void updateEntity()
	{
		if(buf++ == 20)
		{
			sendToNearBy(new FleTEPacket(this, (byte) 2), 64.0F);
			buf = 0;
			if(ore == null)
			{
				double heat = hc.getHeat();
				hc.reseaveHeat(heat < 0 ? 0 : heat);
			}
			markRenderForUpdate();
		}
		smeltedOre();
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		hc.update();
	}
	
	private void smeltedOre()
	{
		if(worldObj.isRemote) return;
		OreMeltingRecipe recipe = OreMeltingRecipe.matchRecipe(ore);
		if(recipe != null)
		{
			if(recipe.minTempreture < getTemperature(ForgeDirection.UNKNOWN))
			{
				double progress = (getTemperature(ForgeDirection.UNKNOWN) - recipe.minTempreture) * 10F;
				hc.emitHeat(progress);
				this.progress += progress;
				if(this.progress >= recipe.energyRequire)
				{
					ore = recipe.output;
					double h = hc.getHeat();
					hc = new ThermalTileHelper(ore);
					hc.reseaveHeat(h);
					amount *= recipe.productivity;
					this.progress = 0D;
					markRenderForUpdate();
					sendToNearBy(new FleTEPacket(this, (byte) 2), 64.0F);
				}
			}
		}
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos()) + hc.getTempreture();
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return hc.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{		
		return hc.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		hc.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		hc.emitHeat(heatValue);
	}
	
	public ArrayList<ItemStack> getDrops()
	{
		ArrayList<ItemStack> list = new ArrayList();
		list.add(ItemOre.a(ore, (int) Math.floor(amount / (1000D / 9D))));
		return list;
	}

	public boolean isSmelting()
	{
		OreMeltingRecipe recipe = OreMeltingRecipe.matchRecipe(ore);
		return recipe != null;
	}

	public double getProgress()
	{
		OreMeltingRecipe recipe = OreMeltingRecipe.matchRecipe(ore);
		return progress / (double) recipe.energyRequire;
	}

	@Override
	public double getPreHeatEmit()
	{
		return hc.getPreHeatEmit();
	}
	
	@Override
	protected short emmit(byte aType)
	{
		return aType == 2 ? (short) MaterialOre.getOreID(ore) : 0;
	}
	
	@Override
	protected void receiveNumber(byte type, Number number)
	{
		if(type == 2)
		{
			init(MaterialOre.getOreFromID(number.shortValue()));
		}
	}
}