package fle.resource.block;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.energy.IThermalTileEntity;
import flapi.material.MaterialOre;
import flapi.te.TEBase;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Parts;
import fle.core.net.FleTEPacket;
import fle.core.recipe.OreMeltingRecipe;
import fle.resource.item.ItemOre;

public class TileEntityOreCobble extends TEBase implements IThermalTileEntity
{
	double progress;
	int amount = Parts.chip.resolution;
	MaterialOre ore;
	ThermalTileHelper hc;
	
	public TileEntityOreCobble()
	{
		hc = new ThermalTileHelper(1F, 1F);
	}
	
	public void init(MaterialOre ore, int amount)
	{
		this.ore = ore;
		hc = new ThermalTileHelper(ore);
		this.amount = amount;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		amount = nbt.getInteger("Amount");
		progress = nbt.getDouble("Progress");
		ore = MaterialOre.getOreFromName(nbt.getString("Ore"));
		hc.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("Amount", amount);
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
	public void update()
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
		if(ore != null)
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
		return ore == null ? 0 : hc.getThermalConductivity();
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
		list.add(ItemOre.a(ore, amount, 1));
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
	public Object onEmit(byte type)
	{
		return type == 2 ? new int[]{MaterialOre.getOreID(ore), amount} : null;
	}
	
	@Override
	public void onReceive(byte type, Object contain)
	{		
		if(type == 2)
		{
			int[] is = (int[]) contain;
			init(MaterialOre.getOreFromID(is[0]), is[1]);
		}	
	}
	
}