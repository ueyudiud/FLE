/*
 * copyright© 2016 ueyudiud
 */

package fle.core.tile.ditchs;

import farcore.lib.material.Mat;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.abstracts.TESynchronization;
import farcore.lib.util.Direction;
import farcore.util.L;
import farcore.util.U;
import farcore.util.U.NBTs;
import fle.api.tile.IDitchTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TEDitch extends TESynchronization implements IDitchTile, IUpdatableTile, ITB_BlockPlacedBy
{
	private static final AxisAlignedBB AABB_DITCH_RENDER_RANGE = new AxisAlignedBB(-1F, -1F, -1F, 2F, 2F, 2F);
	
	private static final int[] Connect = {0x0, 0x1, 0x2, 0x3};

	private class DitchSidedHandler implements IFluidHandler
	{
		final EnumFacing facing;
		
		DitchSidedHandler(EnumFacing facing)
		{
			this.facing = facing;
		}
		
		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return tank.getTankProperties();
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			return tank.fill(resource, doFill);
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			return tank.drain(resource, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			return tank.drain(maxDrain, doDrain);
		}
	}

	private long[] flowBuffer = new long[4];
	private int[] lastFlow = {0, 0, 0, 0};

	private Mat material = Mat.VOID;
	private DitchFactory factory = DitchBlockHandler.getFactory(null);
	private FluidTank tank;
	private DitchSidedHandler[] handlers;

	public TEDitch()
	{
		tank = new FluidTank(0);
		handlers = new DitchSidedHandler[4];
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			handlers[facing.getHorizontalIndex()] = new DitchSidedHandler(facing);
		}
	}
	public TEDitch(Mat material)
	{
		this();
		setMaterial(material);
	}

	public void setMaterial(Mat material)
	{
		this.material = material;
		factory = IDitchTile.DitchBlockHandler.getFactory(material);
		tank = factory.apply(this);
	}

	@Override
	public Mat getMaterial()
	{
		return material;
	}

	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		setMaterial(Mat.material(stack.getItemDamage()));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		material = NBTs.getMaterialByNameOrDefault(nbt, "material", Mat.VOID);
		factory = DitchBlockHandler.getFactory(material);
		tank = factory.apply(this);
		tank.readFromNBT(NBTs.getCompound(nbt, "tank", false));
		NBTs.getLongArrayOrDefault(nbt, "flowBuf", flowBuffer);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("material", material.name);
		nbt.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		NBTs.setLongArray(nbt, "flowBuf", flowBuffer);
		return nbt;
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		Mat material1 = NBTs.getMaterialByIDOrDefault(nbt, "m", material);
		if(material != material1)
		{
			material = material1;
			factory = DitchBlockHandler.getFactory(material1);
			tank = factory.apply(this);
			tank.setFluid(NBTs.getFluidStackOrDefault(nbt, "s", null));
			markBlockRenderUpdate();
		}
		else
		{
			tank.setFluid(NBTs.getFluidStackOrDefault(nbt, "s", tank.getFluid()));
		}
	}

	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("m", material.id);
		NBTs.setFluidStack(nbt, "s", tank.getFluid(), true);
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		super.causeUpdate(pos, state, tileUpdate);
		if(tileUpdate)
		{
			if(!is(Connect[0]) && !is(Connect[1]) && !is(Connect[2]) && !is(Connect[3]))
			{
				for(Direction direction : Direction.DIRECTIONS_2D)
				{
					if(canLink(direction, getTE(direction)))
					{
						set(Connect[direction.horizontalOrdinal], true);
						break;
					}
				}
			}
		}
	}

	@Override
	protected void updateServer()
	{
		super.updateServer();
		if(tank.getFluid() != null && tank.getFluid().getFluid().isGaseous(tank.getFluid()))
		{
			tank.setFluid(null);
			syncToNearby();
		}
		factory.onUpdate(this);
		if(!isInvalid())
		{
			int val;
			final int speedMutiple = factory.getSpeedMultiple(this);
			final int limit = factory.getMaxTransferLimit(this);
			for(int i = 0; i < Direction.DIRECTIONS_2D.length; ++i)
			{
				Direction direction = Direction.DIRECTIONS_2D[i];
				if(is(Connect[i]))
				{
					val = tryFillFluidInto(limit, direction);
					if(val > 0)
					{
						syncToNearby();
						lastFlow[direction.horizontalOrdinal] = Math.max(0, val - limit);
					}
					else if(val == 0)
					{
						flowBuffer[direction.horizontalOrdinal] += speedMutiple;
					}
					else
					{
						val = 0;
					}
				}
				else
				{
					flowBuffer[direction.horizontalOrdinal] = 0;
				}
			}
		}
	}

	protected int tryFillFluidInto(int limit, Direction direction)
	{
		int value = lastFlow[direction.horizontalOrdinal];
		int viscosity;
		TileEntity tile = getTE(direction);
		if(tile instanceof IDitchTile)
		{
			IDitchTile ditch = (IDitchTile) tile;
			Fluid fluid = getFluidContain();
			Fluid fluid1 = ditch.getFluidContain();
			if(fluid != null && fluid1 != null && fluid != fluid1)
			{
				ditch.setLink(direction.getOpposite(), false);
				setLink(direction, false);
				return -1;
			}
			if(fluid == null)
			{
				if(fluid1 == null) return -1;
				fluid = fluid1;
			}
			float h1 = getFlowHeight();
			float h2 = ditch.getFlowHeight();
			if(L.similar(h1, h2)) return -1;
			if(h1 > h2)
			{
				viscosity = fluid.getViscosity(tank.getFluid());
				int speed = getFlowSpeed(direction, limit, viscosity);
				if(speed < 1) return 0;
				flowBuffer[direction.horizontalOrdinal] -= speed * viscosity;
				int amount = ditch.getTank().fill(tank.drain(speed, false), true);
				tank.drain(amount, true);
				syncToNearby();
				markDirty();
				return amount;
			}
			//else if(h1 < h2) ? Let another ditch tile to handle!
			return 0;
		}
		else
		{
			viscosity = tank.getFluid().getFluid().getViscosity(tank.getFluid());
			int speed = getFlowSpeed(direction, limit, viscosity);
			int result;
			if((result = U.TileEntities.tryFlowFluidInto(tank, tile, direction.getOpposite(), speed, true)) != -1)
			{
				if(result > 0)
				{
					flowBuffer[direction.horizontalOrdinal] -= result * viscosity;
				}
				return result;
			}
			else if(isAirBlock(direction) && pos.getY() > 1)
			{
				tile = getTE(direction.x, -1, direction.z);
				speed = getFlowSpeed(direction, Integer.MAX_VALUE, viscosity / 2);
				if(tile instanceof IDitchTile)
				{
					FluidStack stack = tank.drain(speed, true);//Force to drain fluid.
					flowBuffer[direction.horizontalOrdinal] = 0;
					return ((IDitchTile) tile).getTank().fill(stack, true);
				}
				else
				{
					result = U.TileEntities.tryFlowFluidInto(tank, tile, direction.getOpposite(), speed, true);
					if(result != -1)
					{
						flowBuffer[direction.horizontalOrdinal] -= result * viscosity;
						return result;
					}
				}
			}
			return 0;
		}
	}
	
	protected int getFlowSpeed(Direction direction, int limit, int viscosity)
	{
		int speed = (int) ((float) (flowBuffer[direction.horizontalOrdinal] + 1) / (float) viscosity);
		return speed > limit ? limit : speed;
	}

	@Override
	public float getFlowHeight()
	{
		return (float) tank.getFluidAmount() / (float) tank.getCapacity();
	}

	protected boolean canLink(Direction face, TileEntity tile)
	{
		EnumFacing facing = face.getOpposite().of();
		return tile == null ? false : tile instanceof IDitchTile ||
				tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing) ||
				(tile instanceof net.minecraftforge.fluids.IFluidHandler && ((net.minecraftforge.fluids.IFluidHandler) tile).getTankInfo(facing).length > 0);
	}
	
	@Override
	public boolean isLinked(Direction direction)
	{
		return is(Connect[direction.horizontalOrdinal]);
	}

	@Override
	public void setLink(Direction direction, boolean state)
	{
		set(Connect[direction.horizontalOrdinal], state);
		syncToNearby();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AABB_DITCH_RENDER_RANGE;
	}

	@Override
	public FluidTank getTank()
	{
		return tank;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing.getHorizontalIndex() != -1)
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(handlers[facing.getHorizontalIndex()]);
		return super.getCapability(capability, facing);
	}
}