package fle.core.te;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.energy.IThermalTileEntity;
import flapi.item.interfaces.ICastingTool;
import flapi.material.MatterDictionary;
import flapi.material.MatterDictionary.IFreezingRecipe;
import flapi.net.INetEventListener;
import flapi.te.TEIFluidTank;
import flapi.util.FleValue;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Lang;
import fle.core.init.Materials;
import fle.core.net.FleTEPacket;
import fle.core.recipe.RecipeHelper;

public class TileEntityCastingPool extends TEIFluidTank implements IThermalTileEntity, INetEventListener
{
	public int buf = 0;
	public ThermalTileHelper tc = new ThermalTileHelper(Materials.Stone);
	
	public TileEntityCastingPool()
	{
		super(12, 6000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
		buf = nbt.getInteger("Buf");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
		nbt.setInteger("Buf", buf);
	}
	
	int syncTick = 0;

	@Override
	public void update()
	{
		if(syncTick == 20)
		{
			if(RecipeHelper.fillOrDrainInventoryTank(this, tank, 9, 10))
			{
				syncFluidTank();
				markRenderForUpdate();
			}
		}
		else ++syncTick;
		IFreezingRecipe recipe = MatterDictionary.getFreeze(tank.getFluid(), this);
		if(recipe != null && !isClient())
		{
			int require = recipe.getMatterRequire(tank.getFluid(), this);
			if(tank.getFluidAmount() >= require)
			{
				++buf;
				if(buf > (tank.getFluid().getFluid().getTemperature(tank.getFluid()) - FleValue.WATER_FREEZE_POINT))
				{
					ItemStack output = recipe.getOutput(tank.getFluid(), this).copy();
					if(RecipeHelper.matchOutput(this, 11, output))
					{
						buf = 0;
						tank.drain(require, true);
						for(int i = 0; i < 9; ++i)
						{
							RecipeHelper.onInputItemStack(this, i);
						}
						RecipeHelper.onOutputItemStack(this, 11, output);
					}
					else
					{
						
					}
				}
				else
				{
					tc.reseaveHeat((tank.getFluid().getFluid().getTemperature(tank.getFluid()) - FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos())) * 2D);
					sendToNearBy(new FleTEPacket(this, (byte) 4), 16.0F);
				}
			}
		}
		else if(recipe == null && !isClient())
		{
			buf = 0;
		}
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return super.canFill(from, fluid) && from == ForgeDirection.UP;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return super.canDrain(from, fluid) && from != ForgeDirection.UP && from != ForgeDirection.DOWN;
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos()) + tc.getTempreture();
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
	
	@SideOnly(Side.CLIENT)
	public double getProgress()
	{
		if(tank.getFluid() == null) return 0D;
		return (double) buf / (double) ((tank.getFluid().getFluid().getTemperature(tank.getFluid()) - FleValue.WATER_FREEZE_POINT));
	}
	
	@Override
	public Object onEmit(byte aType)
	{
		switch(aType)
		{
		case 3 : return buf;
		case 4 : return tc.getHeat();
		}
		return null;
	}

	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 3)
		{
			buf = (Integer) contain;
		}
		else if(type == 4)
		{
			tc.syncHeat((Double) contain);
		}
	}

	@Override
	public double getPreHeatEmit()
	{
		return tc.getPreHeatEmit();
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_castingPool;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return i < 9 && itemstack != null ? itemstack.getItem() instanceof ICastingTool : true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP ? new int[]{9, 10, 11} : null;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return direction == ForgeDirection.UP && slotID == 9;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return direction == ForgeDirection.UP && (slotID == 10 || slotID == 11);
	}
	
	@Override
	public int getProgressSize()
	{
		return 1;
	}
	
	@Override
	public void setProgress(int id, int value)
	{
		if(id == 0) buf = value;
	}
	
	@Override
	public int getProgress(int id)
	{
		return id == 0 ? buf : 0;
	}
}