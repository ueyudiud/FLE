package fle.core.te;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.net.FlePackets.CoderTankUpdate;
import fle.api.net.FlePackets.CoderTileUpdate;
import fle.api.net.INetEventListener;
import fle.api.te.IFluidTanks;
import fle.api.te.TEBase;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Config;
import fle.core.init.Materials;

public class TileEntityLavaHeatTransfer extends TEBase implements IFluidHandler, IThermalTileEntity, IFluidTanks, INetEventListener
{
	private ThermalTileHelper tc = new ThermalTileHelper(Materials.Stone);
	private final FluidTank tank = new FluidTank(1000);
	private final int lavaHeatPower = Config.getInteger("pLavaTransfer", 1000);

	private int buf = 0;
	public int tick = 0;
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
		tank.writeToNBT(nbt);
		nbt.setInteger("Buf", buf);
		nbt.setInteger("Tick", tick);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
		tank.readFromNBT(nbt);
		buf = nbt.getInteger("Buf");
		tick = nbt.getInteger("Tick");
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(!worldObj.isRemote)
		{	
			if(tank.getFluidAmount() == 0)
			{
				lable:
				for(int i = -2; i <= 2; ++i)
					for(int j = 0; j <= 2; ++j)
						for(int k = -2; k <= 2; ++k)
						{
							if(getBlockPos().toPos(i, j, k).getBlock() == Blocks.lava && getBlockPos().toPos(i, j, k).getBlockMeta() == 0)
							{
								worldObj.setBlockToAir(xCoord + i, yCoord + j, zCoord + k);
								tank.fill(new FluidStack(FluidRegistry.LAVA, 1000), true);
								break lable;
							}
						}
			}
		}
		if(tc.getTempreture() < 1000)
		{
			if(buf == 0)
			{
				if(tank.drain(1, true) != null)
				{
					buf += 50;
					tick += rand.nextInt(5);
					if(tick > 1000) tick = 1000;
					sendToNearBy(new CoderTankUpdate(getBlockPos()), 16.0F);
				}
			}
			if(buf > 0)
			{
				tc.reseaveHeat(lavaHeatPower);
				--buf;
			}
		}
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return from == ForgeDirection.UP ? tank.fill(resource, doFill) : 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain)
	{
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return from == ForgeDirection.UP && fluid == FluidRegistry.LAVA;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{tank.getInfo()};
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
	public int getSizeTank()
	{
		return 1;
	}

	@Override
	public IFluidTank getTank(int index)
	{
		return tank;
	}

	@Override
	public FluidStack getFluidStackInTank(int index)
	{
		return tank.getFluid();
	}

	@Override
	public void setFluidStackInTank(int index, FluidStack aStack)
	{
		tank.setFluid(aStack);
	}

	@Override
	public FluidStack drainTank(int index, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public int fillTank(int index, FluidStack resource, boolean doFill)
	{
		return tank.fill(resource, doFill);
	}
	
	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 1)
		{
			tc.syncHeat((Double) contain);
		}
	}

	@Override
	public double getPreHeatEmit()
	{
		return tc.getPreHeatEmit();
	}
}