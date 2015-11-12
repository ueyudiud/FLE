package fle.core.te.argil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.te.TEBase;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Materials;

public class TileEntityArgilItems extends TEBase implements IThermalTileEntity
{
	private ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	private double smeltedTick;
	public ItemStack stack;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
		smeltedTick = nbt.getInteger("SmeltedTick");
		stack = ItemStack.loadItemStackFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
		nbt.setInteger("SmeltedTick", (int) smeltedTick);
		if(stack != null)
		{
			stack.writeToNBT(nbt);
		}
	}
	
	private int tick = 0;
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		bakeClay();
		++tick;
		if(tick > 20)
		{
			markNBTUpdate();
		}
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}
	
	private void bakeClay()
	{
		if(getTemperature(ForgeDirection.UNKNOWN) > 450)
		{
			if(FurnaceRecipes.smelting().getSmeltingResult(stack) != null)
			{
				double progress = (getTemperature(ForgeDirection.UNKNOWN) - 450) * 2D;
				smeltedTick += progress;
				tc.emitHeat(progress);
				
				if(smeltedTick >= 8000000 * stack.stackSize)
				{
					ItemStack tStack = FurnaceRecipes.smelting().getSmeltingResult(stack);
					ItemStack ret = tStack.copy();
					ret.stackSize *= stack.stackSize;
					stack = ret;
					smeltedTick = 0;
				}
			}
			else
			{
				smeltedTick = 0;
			}
		}
	}
	
	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}
	
	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return tc.getThermalConductivity();
	}
	
	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		return tc.getHeat();
	}
	
	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		tc.reseaveHeat(heatValue);
	}
	
	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		tc.emitHeat(heatValue);
	}
	
	@Override
	public double getPreHeatEmit()
	{
		return tc.getPreHeatEmit();
	}

	public double getProgress()
	{
		return (double) smeltedTick / 8000000D;
	}
}