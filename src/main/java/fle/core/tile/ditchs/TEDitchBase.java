/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.tile.ditchs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import fle.api.ditch.DitchBlockHandler;
import fle.api.ditch.DitchFactory;
import fle.api.tile.IDitchTile;
import nebula.client.util.Client;
import nebula.common.NebulaSynchronizationHandler;
import nebula.common.fluid.FluidTankN;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.IUpdatableTile;
import nebula.common.tile.TE04Synchronization;
import nebula.common.util.Direction;
import nebula.common.util.FluidStacks;
import nebula.common.util.L;
import nebula.common.util.NBTs;
import nebula.common.util.TileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class TEDitchBase extends TE04Synchronization
implements IDitchTile, IUpdatableTile, ITB_BlockPlacedBy, IDebugableTile, INetworkedSyncTile, ITB_AddDestroyEffects, ITB_AddHitEffects, ITP_BoundingBox
{
	private static final AxisAlignedBB AABB_DITCH_RENDER_RANGE = new AxisAlignedBB(-1F, -1F, -1F, 2F, 2F, 2F), AABB_DITCH_BOUNDS[];
	
	private static final int InitState;
	private static final int[] Connect = { 0x0, 0x1, 0x2, 0x3 };
	
	static
	{
		int _init_state = 0;
		for (int i : Connect)
		{
			_init_state |= 1 << i;
		}
		InitState = _init_state;
		
		AABB_DITCH_BOUNDS = new AxisAlignedBB[16];
		float x1, x2, z1, z2, y1 = 0.1875F, y2 = 0.5F;
		for (int i = 0; i < 16; ++i)
		{
			z2 = (i & 0x1) != 0 ? 1.0F : 0.6875F;
			x1 = (i & 0x2) != 0 ? 0.0F : 0.3125F;
			z1 = (i & 0x4) != 0 ? 0.0F : 0.3125F;
			x2 = (i & 0x8) != 0 ? 1.0F : 0.6875F;
			AABB_DITCH_BOUNDS[i] = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
		}
	}
	
	protected int[]	flowBuffer			= new int[4], flowAmount = { 0, 0, 0, 0 }, lastFlowAmount = { 0, 0, 0, 0 };
	private byte[]	lastConnectionState	= new byte[4];
	
	protected DitchFactory	factory		= DitchBlockHandler.getFactory(null);
	protected FluidTankN	tank;
	
	protected TEDitchBase()
	{
		this.tank = new FluidTankN(0);
		this.state = InitState;
	}
	
	protected TEDitchBase(Void unused)
	{
		
	}
	
	protected void setMaterial(Mat material)
	{
		this.factory = DitchBlockHandler.getFactory(material);
		this.tank = this.factory.apply(this);
	}
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		byte i = 0;
		for (Direction direction : Direction.DIRECTIONS_2D)
			if (canLink(direction, getTE(direction)))
				i |= direction.flag1;
		return AABB_DITCH_BOUNDS[i];
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		super.causeUpdate(pos, state, tileUpdate);
		boolean flag = false;
		for (Direction direction : Direction.DIRECTIONS_2D)
		{
			byte s = (byte) getLinkState(direction);
			if (this.lastConnectionState[direction.horizontalOrdinal] != s)
			{
				flag = true;
				this.lastConnectionState[direction.horizontalOrdinal] = s;
			}
		}
		if (flag)
		{
			markBlockUpdate();
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		setMaterial(Mat.getMaterialByNameOrDefault(nbt, "material", Mat.VOID));
		this.tank.readFrom(nbt, "tank");
		this.flowBuffer = NBTs.getIntArrayOrDefault(nbt, "flowBuf", this.flowBuffer);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("material", getMaterial().name);
		this.tank.writeTo(nbt, "tank");
		nbt.setIntArray("flowBuf", this.flowBuffer);
		return nbt;
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		Mat material1 = Mat.getMaterialByIDOrDefault(nbt, "m", getMaterial());
		if (getMaterial() != material1)
		{
			setMaterial(material1);
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("m", getMaterial().id);
	}
	
	@Override
	public void writeToClientInitalization(NBTTagCompound nbt)
	{
		super.writeToClientInitalization(nbt);
		this.tank.writeTo(nbt, "t");
	}
	
	@Override
	protected void initClient(NBTTagCompound nbt)
	{
		super.initClient(nbt);
		this.tank.readFrom(nbt, "t");
		markBlockRenderUpdate();
	}
	
	@Override
	protected void updateServer()
	{
		FluidStack stack = FluidStacks.copy(this.tank.getFluid());
		super.updateServer();
		this.factory.onUpdate(this);
		if (!isInvalid())
		{
			if (this.tank.getFluid() == null) return;
			if (FluidStacks.isGaseous(this.tank.getFluid()))
			{
				this.tank.setFluid(null);
			}
			else
			{
				int val;
				final int viscosity = FluidStacks.getViscosity(this.tank.getFluid());
				final int speedMutiple = this.factory.getSpeedMultiple(this);
				final int limit = this.factory.getMaxTransferLimit(this);
				for (Direction direction : Direction.DIRECTIONS_2D)
				{
					if (is(Connect[direction.horizontalOrdinal]))
					{
						this.flowBuffer[direction.horizontalOrdinal] = Math.min(speedMutiple + this.flowBuffer[direction.horizontalOrdinal], limit << 10);
						val = tryFillFluidInto(this.flowBuffer[direction.horizontalOrdinal], viscosity, limit, direction);
						if (val >= 0)
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
		}
		System.arraycopy(this.flowAmount, 0, this.lastFlowAmount, 0, this.lastFlowAmount.length);
		Arrays.fill(this.flowAmount, 0);
		if (!FluidStacks.areFluidStacksEqual(stack, this.tank.getFluid()))
		{
			NebulaSynchronizationHandler.markTileEntityForUpdate(this, 0);
		}
	}
	
	protected int tryFillFluidInto(int buffer, int viscosity, int limit, Direction direction)
	{
		if (this.tank.getFluid() == null) return 0;
		
		TileEntity tile = getTE(direction);
		if (tile instanceof IDitchTile)
		{
			IDitchTile ditch = (IDitchTile) tile;
			Fluid fluid = getFluidContain();
			Fluid fluid1 = ditch.getFluidContain();
			if (fluid != null && fluid1 != null && fluid != fluid1)
			{
				ditch.setLink(direction.opposite(), false);
				setLink(direction, false);
				return -1;
			}
			if (fluid == null)
			{
				if (fluid1 == null) return -1;
				fluid = fluid1;
			}
			float h1 = getFlowHeight();
			float h2 = ditch.getFlowHeight();
			if (L.similar(h1, h2)) return -1;
			if (h1 > h2)
			{
				int speed = getFlowSpeed(buffer, limit, viscosity);
				if (speed < 1) return 0;
				int amount = ditch.getTank().fill(this.tank.drain(speed, false), true);
				this.tank.drain(amount, true);
				return amount;
			}
			// else if(h1 < h2) ? Let another ditch tile to handle!
			return 0;
		}
		else
		{
			int speed = getFlowSpeed(buffer, limit, viscosity);
			int result;
			if ((result = TileEntities.tryFlowFluidInto(this.tank, tile, direction.opposite(), speed, true)) != -1)
			{
				return result;
			}
			else if (isAirBlock(direction) && this.pos.getY() > 1)
			{
				tile = getTE(direction.x, -1, direction.z);
				speed = getFlowSpeed(buffer, limit, viscosity / 2);
				if (tile instanceof IDitchTile)
				{
					FluidStack stack = this.tank.drain(speed, true);
					// Force to drain fluid.
					return ((IDitchTile) tile).getTank().fill(stack, true);
				}
				else
				{
					result = TileEntities.tryFlowFluidInto(this.tank, tile, direction.opposite(), speed, true);
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
		return !isLinked(direction) ? 0 : canLink(direction, getTE(direction)) ? 1 : isAirBlock(direction) && canLink(Direction.U, getTE(direction.x, direction.y - 1, direction.z)) ? 2 : 0;
	}
	
	public boolean canLink(Direction face, TileEntity tile)
	{
		EnumFacing facing = face.opposite().of();
		return tile == null ? false : tile instanceof IDitchTile || tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
	}
	
	@Override
	public boolean isLinked(Direction direction)
	{
		return direction.horizontal ? is(Connect[direction.horizontalOrdinal]) : direction == Direction.U;
	}
	
	@Override
	public void setLink(Direction direction, boolean state)
	{
		set(Connect[direction.horizontalOrdinal], state);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AABB_DITCH_RENDER_RANGE.offset(this.pos);
	}
	
	@Override
	public FluidTankN getTank()
	{
		return this.tank;
	}
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getMaterialIcon()
	{
		return this.factory.getMaterialIcon(getMaterial());
	}
	
	@Override
	public void addDebugInformation(EntityPlayer player, Direction side, List<String> list)
	{
		list.add(String.format("Connecting State : [%b, %b, %b, %b]", is(Connect[0]), is(Connect[1]), is(Connect[2]), is(Connect[3])));
		list.add("Amount : " + this.tank.getFluidAmount() + "/" + this.tank.getCapacity());
		list.add("Flow : " + Arrays.toString(this.lastFlowAmount));
		list.add("Buffer : " + Arrays.toString(this.flowBuffer));
	}
	
	@Override
	public void writeNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 0:
			buf.writeFluidStack(this.tank.getFluid());
			break;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 0:
			this.tank.setFluid(buf.readFluidStack());
			break;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		Client.addBlockDestroyEffects(this.world, this.pos, getBlockState(), manager, getMaterialIcon());
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		Client.addBlockDestroyEffects(this.world, this.pos, getBlockState(), manager, getMaterialIcon());
		return true;
	}
}
