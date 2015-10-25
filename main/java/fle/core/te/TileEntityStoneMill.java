package fle.core.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.energy.IRotationTileEntity;
import fle.api.energy.RotationNet.RotationPacket;
import fle.api.gui.GuiCondition;
import fle.api.gui.GuiError;
import fle.api.soild.ISolidHandler;
import fle.api.soild.ISolidTanks;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTank;
import fle.api.soild.SolidTankInfo;
import fle.api.te.TEIT;
import fle.api.util.FleLog;
import fle.api.world.BlockPos;
import fle.core.energy.RotationTileHelper;
import fle.core.inventory.InventoryStoneMill;

public class TileEntityStoneMill extends TEIT<InventoryStoneMill> implements ISolidHandler, ISolidTanks, IRotationTileEntity
{
	private RotationTileHelper rh = new RotationTileHelper(1024D, -1D);
	public GuiCondition type;

	public TileEntityStoneMill()
	{
		super(new InventoryStoneMill());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		rh.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		rh.writeToNBT(nbt);
	}
	
	private static ForgeDirection[] dirs = {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};

	@Override
	public void updateInventory() 
	{
		rh.update();
		if(rh.getRotationEnergy() > 0)
			++tick;
		getTileInventory().updateEntity(this);
		BlockPos tPos = getBlockPos();
		for(ForgeDirection dir : dirs)
		{
			if(getTileInventory().sTank.getStack() != null)
			{
				if(tPos.toPos(dir).getBlockTile() instanceof ISolidHandler)
				{
					ISolidHandler sh = (ISolidHandler) tPos.toPos(dir).getBlockTile();
					if(sh.canFillS(dir.getOpposite(), getTileInventory().sTank.get()))
					{
						int i = sh.fillS(dir.getOpposite(), getTileInventory().sTank.getStack(), false);
						SolidStack aStack = getTileInventory().sTank.drain(i, true);
						sh.fillS(dir.getOpposite(), aStack, true);
					}
				}
				else if(tPos.toPos(dir).toPos(ForgeDirection.DOWN).getBlockTile() instanceof ISolidHandler)
				{
					ISolidHandler sh = (ISolidHandler) tPos.toPos(dir).toPos(ForgeDirection.DOWN).getBlockTile();
					if(sh.canFillS(ForgeDirection.UP, getTileInventory().sTank.get()))
					{
						int i = sh.fillS(ForgeDirection.UP, getTileInventory().sTank.getStack(), false);
						SolidStack aStack = getTileInventory().sTank.drain(i, true);
						sh.fillS(ForgeDirection.UP, aStack, true);
					}
				}
			}
			else break;
		}
		syncSolidTank();
	}
	
	public void onPower()
	{
		rh.reseaveRotation(new RotationPacket(256, 0.25));
	}
	
	int tick;
	
	@SideOnly(Side.CLIENT)
	public double getRotation()
	{
		return (tick % 100) * Math.PI / 100D;
	}
	
	@SideOnly(Side.CLIENT)
	public double getEnergyContain()
	{
		return Math.min(rh.getRotationEnergy(), 1024D) / 1024D;
	}

	@SideOnly(Side.CLIENT)
	public double getProgress(int i)
	{
		return (int) (getTileInventory().getProgress() * i);
	}

	public SolidTank getSolidTank()
	{
		return getTileInventory().sTank;
	}

	@Override
	public int fillS(ForgeDirection from, SolidStack resource, boolean doFill)
	{
		return getTileInventory().sTank.fill(resource, doFill);
	}

	@Override
	public SolidStack drainS(ForgeDirection from, SolidStack resource,
			boolean doDrain)
	{
		return getTileInventory().sTank.has(resource) ? getTileInventory().sTank.drain(resource.getSize(), doDrain) : null;
	}

	@Override
	public SolidStack drainS(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return getTileInventory().sTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFillS(ForgeDirection from, Solid Solid)
	{
		return from == ForgeDirection.UP;
	}

	@Override
	public boolean canDrainS(ForgeDirection from, Solid Solid)
	{
		for(ForgeDirection dir : dirs) if(dir == from) return true;
		return false;
	}

	@Override
	public SolidTankInfo[] getSolidTankInfo(ForgeDirection from)
	{
		return new SolidTankInfo[]{getTileInventory().sTank.getInfo()};
	}

	@Override
	public int getSizeSolidTank() 
	{
		return 1;
	}

	@Override
	public SolidTank getSolidTank(int index)
	{
		return getTileInventory().sTank;
	}

	@Override
	public SolidStack getSolidStackInTank(int index)
	{
		return getTileInventory().sTank.getStack();
	}

	@Override
	public void setSolidStackInTank(int index, SolidStack aStack) 
	{
		getTileInventory().sTank.setStack(aStack);
	}

	@Override
	public SolidStack drainSolidTank(int index, int maxDrain, boolean doDrain)
	{
		return getTileInventory().sTank.drain(maxDrain, doDrain);
	}

	@Override
	public int fillSolidTank(int index, SolidStack resource, boolean doFill)
	{
		return getTileInventory().sTank.fill(resource, doFill);
	}

	@Override
	public double getEnergyCurrect()
	{
		return rh.getRotationEnergy();
	}

	@Override
	public int getPreEnergyEmit()
	{
		return (int) (rh.getShowSpeedConduct() * rh.getShowTouqueConduct());
	}

	@Override
	public boolean canReciveEnergy(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP;
	}

	@Override
	public boolean canEmitEnergy(ForgeDirection dir)
	{
		return false;
	}

	@Override
	public int getStuck(RotationPacket packet, ForgeDirection dir)
	{
		return rh.getStuck(packet);
	}

	@Override
	public void onRotationEmit(RotationPacket packet, ForgeDirection dir)
	{
		rh.emitRotation(packet);
	}

	@Override
	public void onRotationReceive(RotationPacket packet, ForgeDirection dir)
	{
		rh.reseaveRotation(packet);
	}

	@Override
	public void onRotationStuck(int stuck)
	{
		type = GuiError.ROTATION_STUCK;
	}
	
	public RotationTileHelper getRotationHelper()
	{
		return rh;
	}
}