/*
 * copyright© 2016 ueyudiud
 */

package fle.core.tile.ditchs;

import java.util.Arrays;
import java.util.List;

import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.abstracts.TESynchronization;
import farcore.lib.util.Direction;
import farcore.util.FluidStacks;
import farcore.util.L;
import farcore.util.NBTs;
import farcore.util.U;
import fle.api.tile.IDitchTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
public class TEDitch extends TESynchronization implements IDitchTile, IUpdatableTile, ITB_BlockPlacedBy, IDebugableTile
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
			return TEDitch.this.tank.getTankProperties();
		}
		
		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			int amount = TEDitch.this.tank.fill(resource, doFill);
			if(doFill && amount > 0)
			{
				TEDitch.this.flowAmount[this.facing.getHorizontalIndex()] -= amount;
				syncToNearby();
			}
			return amount;
		}
		
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			FluidStack stack = TEDitch.this.tank.drain(resource, doDrain);
			if(doDrain && stack != null)
			{
				TEDitch.this.flowAmount[this.facing.getHorizontalIndex()] += stack.amount;
				syncToNearby();
			}
			return stack;
		}
		
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			FluidStack stack = TEDitch.this.tank.drain(maxDrain, doDrain);
			if(doDrain && stack != null)
			{
				TEDitch.this.flowAmount[this.facing.getHorizontalIndex()] += stack.amount;
				syncToNearby();
			}
			return stack;
		}
	}
	
	private int[] flowBuffer = new int[4];
	private int[] flowAmount = {0, 0, 0, 0};
	private int[] lastFlowAmount = {0, 0, 0, 0};
	
	private Mat material = Mat.VOID;
	private DitchFactory factory = DitchBlockHandler.getFactory(null);
	private FluidTank tank;
	private DitchSidedHandler[] handlers;
	
	public TEDitch()
	{
		this.tank = new FluidTank(0);
		this.handlers = new DitchSidedHandler[4];
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			this.handlers[facing.getHorizontalIndex()] = new DitchSidedHandler(facing);
		}
		for(int i : Connect) enable(i);
	}
	public TEDitch(Mat material)
	{
		this();
		setMaterial(material);
	}
	
	public void setMaterial(Mat material)
	{
		this.material = material;
		this.factory = IDitchTile.DitchBlockHandler.getFactory(material);
		this.tank = this.factory.apply(this);
	}
	
	@Override
	public Mat getMaterial()
	{
		return this.material;
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		setMaterial(Mat.material(stack.getItemDamage()));
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		super.causeUpdate(pos, state, tileUpdate);
		markBlockRenderUpdate();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.material = NBTs.getMaterialByNameOrDefault(nbt, "material", Mat.VOID);
		this.factory = DitchBlockHandler.getFactory(this.material);
		this.tank = this.factory.apply(this);
		this.tank.readFromNBT(NBTs.getCompound(nbt, "tank", false));
		this.flowBuffer = NBTs.getIntArrayOrDefault(nbt, "flowBuf", this.flowBuffer);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("material", this.material.name);
		nbt.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
		nbt.setIntArray("flowBuf", this.flowBuffer);
		return nbt;
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		Mat material1 = NBTs.getMaterialByIDOrDefault(nbt, "m", this.material);
		if(this.material != material1)
		{
			this.material = material1;
			this.factory = DitchBlockHandler.getFactory(material1);
			this.tank = this.factory.apply(this);
			this.tank.setFluid(NBTs.getFluidStackOrDefault(nbt, "t", null));
			markBlockRenderUpdate();
		}
		else
		{
			this.tank.setFluid(NBTs.getFluidStackOrDefault(nbt, "t", this.tank.getFluid()));
		}
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("m", this.material.id);
		NBTs.setFluidStack(nbt, "t", this.tank.getFluid(), true);
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		this.factory.onUpdate(this);
		if(!isInvalid())
		{
			if(this.tank.getFluid() == null) return;
			if(FluidStacks.isGaseous(this.tank.getFluid()))
			{
				this.tank.setFluid(null);
				syncToNearby();
			}
			int val;
			final int viscosity = FluidStacks.getViscosity(this.tank.getFluid());
			final int speedMutiple = this.factory.getSpeedMultiple(this);
			final int limit = this.factory.getMaxTransferLimit(this);
			for(Direction direction : Direction.DIRECTIONS_2D)
			{
				if(is(Connect[direction.horizontalOrdinal]))
				{
					this.flowBuffer[direction.horizontalOrdinal] = Math.min(speedMutiple + this.flowBuffer[direction.horizontalOrdinal], limit << 10);
					val = tryFillFluidInto(this.flowBuffer[direction.horizontalOrdinal], viscosity, limit, direction);
					if(val >= 0)
					{
						this.flowAmount[direction.horizontalOrdinal] += val;
						this.flowBuffer[direction.horizontalOrdinal] = 0;
					}
				}
				else
				{
					this.flowBuffer[direction.horizontalOrdinal] = 0;
				}
			}
		}
		System.arraycopy(this.flowAmount, 0, this.lastFlowAmount, 0, this.lastFlowAmount.length);
		Arrays.fill(this.flowAmount, 0);
	}
	
	protected int tryFillFluidInto(int buffer, int viscosity, int limit, Direction direction)
	{
		if(this.tank.getFluid() == null) return 0;
		
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
				int speed = getFlowSpeed(buffer, limit, viscosity);
				if(speed < 1) return 0;
				int amount = ditch.getTank().fill(this.tank.drain(speed, false), true);
				this.tank.drain(amount, true);
				syncToNearby();
				markDirty();
				return amount;
			}
			//else if(h1 < h2) ? Let another ditch tile to handle!
			return 0;
		}
		else
		{
			int speed = getFlowSpeed(buffer, limit, viscosity);
			int result;
			if((result = U.TileEntities.tryFlowFluidInto(this.tank, tile, direction.getOpposite(), speed, true)) != -1)
			{
				return result;
			}
			else if(isAirBlock(direction) && this.pos.getY() > 1)
			{
				tile = getTE(direction.x, -1, direction.z);
				speed = getFlowSpeed(buffer, limit, viscosity / 2);
				if(tile instanceof IDitchTile)
				{
					FluidStack stack = this.tank.drain(speed, true);//Force to drain fluid.
					return ((IDitchTile) tile).getTank().fill(stack, true);
				}
				else
				{
					result = U.TileEntities.tryFlowFluidInto(this.tank, tile, direction.getOpposite(), speed, true);
					return result;
				}
			}
			return -1;
		}
	}
	
	protected int getFlowSpeed(int buffer, int limit, int viscosity)
	{
		int speed = (int) ((float) buffer / (float) viscosity);
		return speed > limit ? limit : speed;
	}
	
	@Override
	public float getFlowHeight()
	{
		return (float) this.tank.getFluidAmount() / (float) this.tank.getCapacity();
	}
	
	public int getLinkState(Direction direction)
	{
		return !isLinked(direction) ? 0 : canLink(direction, getTE(direction)) ? 1 :
			isAirBlock(direction) && canLink(Direction.U, getTE(direction.x, direction.y - 1, direction.z)) ? 2 : 0;
	}
	
	public boolean canLink(Direction face, TileEntity tile)
	{
		EnumFacing facing = face.getOpposite().of();
		return tile == null ? false : tile instanceof IDitchTile ||
				tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
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
		return AABB_DITCH_RENDER_RANGE.offset(this.pos);
	}
	
	@Override
	public FluidTank getTank()
	{
		return this.tank;
	}
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getMaterialIcon()
	{
		return this.factory.getMaterialIcon(this.material);
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
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.handlers[facing.getHorizontalIndex()]);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void addDebugInformation(EntityPlayer player, Direction side, List<String> list)
	{
		list.add(String.format("Connecting State : [%b, %b, %b, %b]", is(Connect[0]), is(Connect[1]), is(Connect[2]), is(Connect[3])));
		list.add("Amount : " + this.tank.getFluidAmount() + "/" + this.tank.getCapacity());
		list.add("Flow : " + Arrays.toString(this.lastFlowAmount));
		list.add("Buffer : " + Arrays.toString(this.flowBuffer));
	}
}