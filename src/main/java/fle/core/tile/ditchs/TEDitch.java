/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.tile.ditchs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import farcore.handler.FarCoreSynchronizationHandler;
import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import fle.api.ditch.DitchBlockHandler;
import fle.api.ditch.DitchFactory;
import fle.api.tile.IDitchTile;
import nebula.client.util.Client;
import nebula.common.fluid.FluidTankN;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.IUpdatableTile;
import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;
import nebula.common.util.FluidStacks;
import nebula.common.util.L;
import nebula.common.util.NBTs;
import nebula.common.util.TileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
public class TEDitch extends TESynchronization
implements IDitchTile, IUpdatableTile, ITB_BlockPlacedBy, ITP_BlockHardness,
IDebugableTile, INetworkedSyncTile, ITB_AddDestroyEffects, ITB_AddHitEffects
{
	private static final AxisAlignedBB AABB_DITCH_RENDER_RANGE = new AxisAlignedBB(-1F, -1F, -1F, 2F, 2F, 2F);
	
	private static final int[] Connect = {0x0, 0x1, 0x2, 0x3};
	
	private int[]
			flowBuffer = new int[4],
			flowAmount = {0, 0, 0, 0},
			lastFlowAmount = {0, 0, 0, 0};
	private byte[] lastConnectionState = new byte[4];
	
	private Mat material = Mat.VOID;
	private DitchFactory factory = DitchBlockHandler.getFactory(null);
	private FluidTankN tank;
	
	public TEDitch()
	{
		this.tank = new FluidTankN(0);
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
		this.factory = DitchBlockHandler.getFactory(material);
		this.tank = this.factory.apply(this);
	}
	
	@Override
	public Mat getMaterial()
	{
		return this.material;
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return 0.5F;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player)
	{
		return player.getDigSpeed(state, this.pos) / getBlockHardness(state) / 30F;
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		setMaterial(Mat.material(stack.getItemDamage()));
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		super.causeUpdate(pos, state, tileUpdate);
		boolean flag = false;
		for(Direction direction : Direction.DIRECTIONS_2D)
		{
			byte s = (byte) getLinkState(direction);
			if(this.lastConnectionState[direction.horizontalOrdinal] != s)
			{
				flag = true;
				this.lastConnectionState[direction.horizontalOrdinal] = s;
			}
		}
		if(flag)
		{
			markBlockUpdate();
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.material = Mat.getMaterialByNameOrDefault(nbt, "material", Mat.VOID);
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
		Mat material1 = Mat.getMaterialByIDOrDefault(nbt, "m", this.material);
		if(this.material != material1)
		{
			this.material = material1;
			this.factory = DitchBlockHandler.getFactory(material1);
			this.tank = this.factory.apply(this);
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("m", this.material.id);
	}
	
	@Override
	protected void initClient(NBTTagCompound nbt)
	{
		super.initClient(nbt);
		markBlockRenderUpdate();
	}
	
	@Override
	protected void updateServer()
	{
		FluidStack stack = FluidStacks.copy(this.tank.getFluid());
		super.updateServer();
		this.factory.onUpdate(this);
		if(!isInvalid())
		{
			if(this.tank.getFluid() == null) return;
			if(FluidStacks.isGaseous(this.tank.getFluid()))
			{
				this.tank.setFluid(null);
			}
			else
			{
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
		}
		System.arraycopy(this.flowAmount, 0, this.lastFlowAmount, 0, this.lastFlowAmount.length);
		Arrays.fill(this.flowAmount, 0);
		if(!FluidStacks.areFluidStacksEqual(stack, this.tank.getFluid()))
		{
			FarCoreSynchronizationHandler.markTileEntityForUpdate(this, 0);
		}
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
				return amount;
			}
			//else if(h1 < h2) ? Let another ditch tile to handle!
			return 0;
		}
		else
		{
			int speed = getFlowSpeed(buffer, limit, viscosity);
			int result;
			if((result = TileEntities.tryFlowFluidInto(this.tank, tile, direction.getOpposite(), speed, true)) != -1)
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
					result = TileEntities.tryFlowFluidInto(this.tank, tile, direction.getOpposite(), speed, true);
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
		return this.factory.getMaterialIcon(this.material);
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
		case 0 :
			buf.writeFluidStack(this.tank.getFluid());
			break;
		default:
			break;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 0 :
			this.tank.setFluid(buf.readFluidStack());
			break;
		default:
			break;
		}
	}
	
	@Override
	public int fill(Direction direction, FluidStack resource, boolean process)
	{
		int amount = IDitchTile.super.fill(direction, resource, process);
		if(process && direction.horizontal)
		{
			this.flowAmount[direction.horizontalOrdinal] -= amount;
		}
		return amount;
	}
	
	@Override
	public FluidStack drain(Direction direction, FluidStack required, boolean process)
	{
		FluidStack stack = IDitchTile.super.drain(direction, required, process);
		if(process && direction.horizontal)
		{
			this.flowAmount[direction.horizontalOrdinal] += FluidStacks.getAmount(stack);
		}
		return stack;
	}
	
	@Override
	public FluidStack drain(Direction direction, int maxAmount, boolean process)
	{
		FluidStack stack = IDitchTile.super.drain(direction, maxAmount, process);
		if(process && direction.horizontal)
		{
			this.flowAmount[direction.horizontalOrdinal] += FluidStacks.getAmount(stack);
		}
		return stack;
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