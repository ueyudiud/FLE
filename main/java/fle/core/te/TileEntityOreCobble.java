package fle.core.te;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.material.MaterialOre;
import fle.api.te.TEBase;
import fle.core.block.BlockOreCobble;
import fle.core.energy.ThermalTileHelper;
import fle.core.item.ItemOre;
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
	
	@Override
	public void updateEntity()
	{
		init();
		smeltedOre();
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		hc.update();
	}
	
	private void init()
	{
		if(ore == null)
		{
			double heat = hc.getHeat();
			init(BlockOreCobble.getOre(worldObj, xCoord, yCoord, zCoord));
			hc.reseaveHeat(heat < 0 ? 0 : heat);
		}
		worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
	}
	
	private void smeltedOre()
	{
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
					FLE.fle.getWorldManager().setData(getBlockPos(), 0, MaterialOre.getOreID(ore));
					worldObj.markBlockRangeForRenderUpdate(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 1, yCoord + 1, zCoord + 1);
				}
			}
		}
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		init();
		return FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos()) + hc.getTempreture();
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		init();
		return hc.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		init();
		return hc.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		init();
		hc.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		init();
		hc.emitHeat(heatValue);
	}
	
	public ArrayList<ItemStack> getDrops()
	{
		ArrayList<ItemStack> list = new ArrayList();
		list.add(ItemOre.a(ore, (int) Math.floor(amount / (1000D / 9D))));
		return list;
	}

	@SideOnly(Side.CLIENT)
	public boolean isSmelting()
	{
		init();
		OreMeltingRecipe recipe = OreMeltingRecipe.matchRecipe(ore);
		return recipe != null;
	}

	@SideOnly(Side.CLIENT)
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
}