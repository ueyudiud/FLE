package fle.core.te;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.item.IPlacedHandler;
import fle.api.te.TEBase;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Config;
import fle.core.init.Materials;
import fle.core.net.FleTEPacket;

public class TileEntityPlacedItem extends TEBase implements IThermalTileEntity
{
	private ThermalTileHelper tc = new ThermalTileHelper(Materials.Void);
	private double smeltedTick;
	public ItemStack stack;
	
	public void init(ItemStack aStack)
	{
		stack = aStack;
	}
	
	public ItemStack getStack()
	{
		return stack;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
		smeltedTick = nbt.getDouble("SmeltedTick");
		stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
		nbt.setDouble("SmeltedTick", smeltedTick);
		nbt.setTag("Item", stack.writeToNBT(new NBTTagCompound()));
	}
	
	public void setSmeltedTick(double tick)
	{
		smeltedTick = tick;
	}
	
	public double addSmeltedTick(double add)
	{
		return (smeltedTick += add);
	}
	
	public double getSmeltedTick()
	{
		return smeltedTick;
	}
	
	int buf = 0;
	
	@Override
	public void updateEntity()
	{
		if(should(INIT))
		{
			disable(INIT);
		}
		else if(should(POSTINIT))
		{
			disable(POSTINIT);
		}
		if(buf++ > 20)
		{
			buf = 0;
			sendToNearBy(new FleTEPacket(this, (byte) 1), 32.0F);
			sendToNearBy(new FleTEPacket(this, (byte) 3), 64.0F);
		}
		tc.update();
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		if(stack != null && !worldObj.isRemote)
			if(stack.getItem() instanceof IPlacedHandler)
			{
				ItemStack a = ((IPlacedHandler) stack.getItem()).updatePlacedItem(this, stack.copy());
				if(!ItemStack.areItemStacksEqual(a, stack))
				{
					stack = a;
					if(stack.stackSize <= 0)
					{
						worldObj.setBlockToAir(xCoord, yCoord, zCoord);
						worldObj.removeTileEntity(xCoord, yCoord, zCoord);
						return;
					}
				}
			}
	}
	
	public ItemStack getDrop()
	{
		if(stack != null)
			if(stack.getItem() instanceof IPlacedHandler)
			{
				return ((IPlacedHandler) stack.getItem()).onBlockRemove(this, stack);
			}
			else
			{
				return stack;
			}
		return null;
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

	@Override
	public double getPreHeatEmit()
	{
		return tc.getPreHeatEmit();
	}
	
	@Override
	public Object onEmit(byte aType)
	{
		if(aType == 1)
		{
			return tc.getHeat();
		}
		return aType == 3 ? stack : super.onEmit(aType);
	}
	
	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 1)
		{
			tc.syncHeat((Double) contain);
			return;
		}
		if(type == 3)
		{
			stack = (ItemStack) contain;
			return;
		}
		super.onReceive(type, contain);
	}
}