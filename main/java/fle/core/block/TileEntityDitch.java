package fle.core.block;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.TEBase;
import farcore.util.U;
import flapi.energy.IThermalTileEntity;
import flapi.fluid.FluidDictionary;
import flapi.net.INetEventListener;
import flapi.plant.IIrrigationHandler;
import flapi.te.interfaces.IDitchTile;
import flapi.te.interfaces.IFluidTanks;
import flapi.util.FleLog;
import flapi.world.BlockPos;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.net.FleTEPacket;
import fle.tool.DitchInfo;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public class TileEntityDitch extends TEBase implements IDitchTile, IFluidTanks, IThermalTileEntity, INetEventListener, IIrrigationHandler
{
	public static final ForgeDirection dirs[] = {ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.WEST, ForgeDirection.SOUTH};
	protected ThermalTileHelper tc = new ThermalTileHelper(1.0F, 1000.0F);
	protected FluidTank tank = new FluidTank(1);
	protected DitchInfo info;
	
	public TileEntityDitch()
	{
		
	}
	public void setup(DitchInfo aInfo, int cap)
	{
		info = aInfo;
		tank = new FluidTank(cap);
		tc = new ThermalTileHelper(aInfo.getMaterial());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tank = new FluidTank(nbt.getInteger("Capacity"));
		tank.readFromNBT(nbt);
		info = DitchInfo.register.get(nbt.getString("Type"));
		tc = new ThermalTileHelper(info.getMaterial());
		tc.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("Capacity", tank.getCapacity());
		tank.writeToNBT(nbt);
		nbt.setString("Type", info.getName());
		tc.writeToNBT(nbt);
	}
	
	int buf = 0;
	
	@Override
	public void updateEntity()
	{
		if(info == null) return;
		if(!info.canStay(tank))
		{
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			return;
		}
		else if(!worldObj.isRemote)
		{
			if(tc != null)
			{
				FLE.fle.getThermalNet().emmitHeat(getBlockPos());
			}
			label:
				if(tank.getFluid() != null)
				{
					if(tank.getFluid().getFluid().isGaseous(tank.getFluid()))
					{
						tank.drain(tank.getCapacity(), true);
						break label;
					}
					int temp = tank.getFluid().getFluid().getTemperature(tank.getFluid());
					int temp1 = getTemperature(ForgeDirection.UNKNOWN);
					if (temp > temp1 + 20)
					{
						tc.reseaveHeat((temp - temp1) * 1.5F);
						tank.drain(1, true);
					}
					else if(temp < temp1 - 20)
					{
						tc.emitHeat((temp1 - temp) * 1.5F);
						tank.drain(1, true);
					}
				}
			int sideMaxSpeed = (int) Math.ceil(1000F * tank.getCapacity() / (float) getDitchViscosity());
			for(ForgeDirection dir : dirs)
			{
				TileEntity tile = getBlockPos().toPos(dir).getBlockTile();
				if(tile instanceof IDitchTile)
				{
					IDitchTile tile1 = ((IDitchTile) tile);
					int level = getWaterLevel();
					int level1 = tile1.getWaterLevel();
					if(level > level1)
					{
						if(tile1.canFill(dir.getOpposite(), tank.getFluid().getFluid()))
						{
							int speed = (int) ((float) ((level - level1) * sideMaxSpeed) / 256.0F);
							FluidStack stack = drain(dir, speed, false);
							if(stack != null)
							{
								int in = tile1.fill(dir.getOpposite(), stack, true);
								if(in > 0)
								{
									drain(dir, in, true);
								}
							}
						}
					}
//					else if(level < level1)
//					{
//						int speed = (int) ((double) ((level1 - level) * sideMaxSpeed) / 256.0D);
//						FluidStack stack = tile1.drain(dir.getOpposite(), speed, false);
//						if(stack != null)
//						{
//							int in = fill(dir, stack, true);
//							if(in > 0)
//							{
//								tile1.drain(dir.getOpposite(), in, true);
//							}
//						}
//					}
					continue;
				}
				else if(tile instanceof IFluidHandler)
				{
					IFluidHandler tile1 = ((IFluidHandler) tile);
					int level = getWaterLevel();
					int level1 = 0;
					try
					{
						FluidStack stack = tile1.drain(dir.getOpposite(), tank.getCapacity(), false);
						if(stack != null)
						{
							if(stack.getFluid().isGaseous(stack)) continue;
							level1 = (int) (256D * (double) stack.amount / (double) tank.getCapacity());
						}
					}
					catch(Throwable e)
					{
						e.printStackTrace();
					}
					if(level > level1)
					{
						if(tile1.canFill(dir.getOpposite(), tank.getFluid().getFluid()))
						{
							int speed = (int) ((double) ((level - level1) * sideMaxSpeed) / 256.0D);
							FluidStack stack = drain(dir, speed, false);
							if(stack != null)
							{
								int in = tile1.fill(dir.getOpposite(), stack, true);
								if(in > 0)
								{
									drain(dir, in, true);
								}
							}
						}
					}
					else if(level < level1)
					{
						int speed = (int) ((double) ((level1 - level) * sideMaxSpeed) / 256.0D);
						FluidStack stack = tile1.drain(dir.getOpposite(), speed, false);
						if(stack != null)
						{
							int in = fill(dir, stack, true);
							if(in > 0)
							{
								tile1.drain(dir.getOpposite(), in, true);
							}
						}
					}
					continue;
				}
				if(getBlockPos().toPos(dir).getBlock().getMaterial() == Material.air && tank.getFluid() != null)
				{
					tile = getBlockPos().toPos(dir).toPos(ForgeDirection.DOWN).getBlockTile();
					if(tile instanceof IDitchTile)
					{
						IDitchTile tile1 = ((IDitchTile) tile);
						int level = getWaterLevel();
						if(tile1.canFill(ForgeDirection.UP, tank.getFluid().getFluid()))
						{
							int speed = (int) ((double) ((level + 1) * sideMaxSpeed) / 256.0D);
							FluidStack stack = drain(dir, speed, false);
							if(stack != null)
							{
								int in = tile1.fill(ForgeDirection.UP, stack, true);
								if(in > 0)
								{
									drain(dir, in, true);
								}
							}
						}
						continue;
					}
					else if(tile instanceof IFluidHandler)
					{
						IFluidHandler tile1 = ((IFluidHandler) tile);
						int level = getWaterLevel();
						if(tile1.canFill(ForgeDirection.UP, tank.getFluid().getFluid()))
						{
							int speed = (int) ((double) ((level + 1) * sideMaxSpeed) / 256.0D);
							FluidStack stack = drain(dir, speed, false);
							if(stack != null)
							{
								int in = tile1.fill(ForgeDirection.UP, stack, true);
								if(in > 0)
								{
									drain(dir, in, true);
								}
							}
						}
						continue;
					}
				}
			}
			if(rand.nextDouble() < info.getDripSpeed())
			{
				tank.drain(1, true);
			}
		}
		if(buf++ == 20)
		{
			syncFluidTank();
			markRenderForUpdate();
			sendToNearBy(new FleTEPacket(this, (byte) -1, this), 24.0F);
			buf = 0;
		}
		tc.update();
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) 
	{
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain)
	{
		return tank.getFluid() != null ? tank.getFluid().isFluidEqual(resource) ? drain(from, resource.amount, doDrain) : null : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return from != ForgeDirection.DOWN;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return from != ForgeDirection.UP && from != ForgeDirection.DOWN;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{tank.getInfo()};
	}

	@Override
	public boolean canConnectWith(ForgeDirection dir)
	{
		return getType(dir) != 0;
	}
	
	public byte getType(ForgeDirection dir)
	{
		BlockPos pos = getBlockPos().toPos(dir);
		if(pos.getBlockTile() instanceof IFluidHandler)
		{
			return 1;
		}
		else if(pos.toPos(ForgeDirection.DOWN).getBlockTile() instanceof IFluidHandler)
		{
			return 2;
		}
		return 0;
	}

	@Override
	public int getWaterLevel()
	{
		return (int) (256 * ((double) tank.getFluidAmount() / (double) tank.getCapacity()));
	}

	@Override
	public int getDitchViscosity()
	{
		return info.getViscosity() + (tank.getFluid() != null ? tank.getFluid().getFluid().getViscosity(tank.getFluid()) : 0);
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
	public int getTemperature(ForgeDirection dir)
	{
		return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return tc.getThermalConductivity();
	}

//	@Override
//	public double getThermalEnergyCurrect(ForgeDirection dir)
//	{
//		return tc.getHeat();
//	}

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

	@SideOnly(Side.CLIENT)
	public ItemStack getRenderItem()
	{
		return info == null ? new ItemStack(Blocks.stone) : info.getBlock();
	}
	
	public ArrayList<ItemStack> getDrop()
	{
		ArrayList<ItemStack> ret = new ArrayList();
		ret.add(ItemDitch.a(info, tank.getCapacity()));
		return ret;
	}
	
	@Override
	public Object onEmit(byte aType)
	{
		return aType == -1 ? new int[]{tank.getCapacity(), DitchInfo.register.serial(info)} : null;
	}
	
	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == -1)
		{
			boolean flag = false;
			int[] i = (int[]) contain;
			if(i[0] != tank.getCapacity())
			{
				FluidStack fluid = tank.getFluid();
				tank = new FluidTank(fluid, i[0]);
				flag = true;
			}
			DitchInfo di = DitchInfo.register.get(i[1]);
			if(di != info)
			{
				info = di;
				flag = true;
			}
			if(flag)
				markRenderForUpdate();
		}
	}
	
	@Override
	public boolean canIrrigate(ForgeDirection dir)
	{
		return dir != ForgeDirection.UP && dir != ForgeDirection.DOWN;
	}
	
	@Override
	public int doIrrigate(ForgeDirection dir, int amount)
	{
		if(U.F.isWater(tank.getFluid()))
		{
			FluidStack stack = drain(dir, amount, true);
			return stack == null ? 0 : stack.amount;
		}
		return 0;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2);
	}
}