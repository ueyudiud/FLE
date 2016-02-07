package farcore.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import farcore.nbt.NBT;
import farcore.net.FleBaseTEPacket;
import farcore.net.INetEventHandler;
import farcore.net.IPacket;
import farcore.net.PacketFactory;
import farcore.util.Direction;
import farcore.util.Facing;
import farcore.util.FleLog;
import farcore.util.FleRandom;
import farcore.world.BlockData;
import flapi.FleAPI;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

/**
 * The base class of FLE tile entities.
 * 
 * @author ueyudiud
 * 		
 */
public class TEBase extends TileEntity
		implements INetEventHandler, IContainerNetworkUpdate
{
	public NBT getNBTHandler()
	{
		return NBT.getNBT(getClass());
	}
	
	/**
	 * The default random of tile.
	 */
	public final FleRandom rand = new FleRandom();
	/**
	 * The facing of this block. It is use to rendering icon or check witch side
	 * can input fluid, save in NBT with tag "BlockFacing".
	 */
	protected Direction dir = Direction.UNKNOWN;
	/**
	 * The light value, range from 0 to 15.<br>
	 */
	protected int lightValue;
	/**
	 * The color state for 6 facing.
	 */
	protected int[] colorState = {-1, -1, -1, -1, -1, -1};
	
	private BlockData data;
	
	public TEBase()
	{
	
	}
	
	public BlockData data()
	{
		return data == null ? data = new BlockData(this) : data;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		dir = Direction.values()[nbt.getByte("BlockFacing")];
		NBT.readFromNBT(this, nbt);
		// blockMetadata = nbt.getShort("TileMeta");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("BlockFacing", (byte) (dir == null
				? Direction.UNKNOWN.ordinal() : dir.ordinal()));
		NBT.writeToNBT(this, nbt);
		// nbt.setShort("TileMeta", (short) getMetadata());
	}
	
	public boolean isClient()
	{
		return worldObj.isRemote;
	}
	
	@Override
	public World getWorld()
	{
		return super.getWorld();
	}
	
	public BlockPos getBlockPos()
	{
		return pos;
	}
	
	public Facing getFacing(Direction dir)
	{
		return dir.getMachineFacing(this.dir);
	}
	
	protected void updateStates()
	{
		sendLarge(new FleBaseTEPacket(this, dir, lightValue, colorState),
				64.0F);
	}
	
	// ================Block state control=============
	public void onBlockPlaced(ItemStack stack, EntityLivingBase placer,
			Direction side)
	{
		this.dir = side;
	}
	
	public boolean onBlockActivated(EntityPlayer player, Direction side,
			Vec3 vec)
	{
		return false;
	}
	
	public void onEntityWalking(Entity entity)
	{
	
	}
	
	public void onBlockBreak()
	{
	
	}
	
	public void onHarvest(EntityPlayer player, BlockPos pos, int fortune,
			boolean silk)
	{
	
	}
	
	public void onBlockExploded(Explosion explosion)
	{
	
	}
	
	// ==========Set block state & Block properties==========
	public int getLightOpacity()
	{
		return lightValue;
	}
	
	public boolean canHarvestBlock(int metadata, String key, int level)
	{
		return true;
	}
	
	protected boolean canBeRecoloured()
	{
		return false;
	}
	
	public boolean canConnectRedstone(Direction side)
	{
		return false;
	}
	
	public int getRedstonePower(Direction side)
	{
		return 0;
	}
	
	public boolean recolourBlock(Direction side, int colour)
	{
		if (side.isAdvanced())
			return false;
		if (canBeRecoloured() && side != Direction.UNKNOWN
				&& colorState[side.ordinal()] != -1)
		{
			colorState[side.ordinal()] = ItemDye.dyeColors[colour];
			return true;
		}
		return false;
	}
	
	public void setColorState(int[] states)
	{
		if (worldObj.isRemote)
		{
			colorState = states;
			markRenderForUpdate();
		}
	}
	
	public void setLightValue(int v)
	{
		if (lightValue == v)
			return;
		lightValue = v;
		if (!worldObj.isRemote)
		{
			sendToNearBy(new FleBaseTEPacket(this, 2, v), 128.0F);
		}
		else
		{
			markForUpdate();
		}
	}
	
	/**
	 * Set direction of block.
	 * 
	 * @param direction
	 */
	public void setDirction(Direction direction)
	{
		dir = direction;
		if (!worldObj.isRemote)
		{
			sendToNearBy(new FleBaseTEPacket(this, direction), 128.0F);
		}
	}
	
	public int getLightValue()
	{
		return lightValue;
	}
	
	public Direction getDirction()
	{
		return dir == null ? Direction.NORTH : dir;
	}
	
	// ==================================Sync method==========================
	/*
	 * public void syncSolidTank() { if(this instanceof ISolidTanks &&
	 * !isClient()) {
	 * sendToNearBy(FleAPI.mod.getNetworkHandler().getPacketMaker().
	 * makeSolidTankPacket(this), 16.0F); } }
	 */
	
	/*
	 * public void syncFluidTank() { if(this instanceof IFluidTanks &&
	 * !isClient()) {
	 * sendToNearBy(FleAPI.mod.getNetworkHandler().getPacketMaker().
	 * makeFluidTankPacket(this), 16.0F); } }
	 */
	
	public void syncData(byte type, float range)
	{
		if (!isClient())
		{
			sendToNearBy(PacketFactory.makeTEPacket(this, type), range);
		}
	}
	
	public void syncNBT()
	{
		if (!isClient())
		{
			sendLarge(PacketFactory.makeNBTPacket(this), 256F);
		}
	}
	
	/**
	 * Send a network packet to player nearby by FLE channel.
	 * 
	 * @see fle.api.net.FleNetworkHandler
	 * @param packet
	 * @param range
	 */
	public void sendToNearBy(IPacket packet, float range)
	{
		if (!isClient())
		{
			FleAPI.mod.getNetworkHandler().sendToNearBy(packet, this, range);
		}
	}
	
	/**
	 * Send a network packet to server by FLE channel.
	 * 
	 * @see fle.api.net.FleNetworkHandler
	 * @param packet
	 */
	public void sendToServer(IPacket packet)
	{
		if (isClient())
		{
			FleAPI.mod.getNetworkHandler().sendToServer(packet);
		}
	}
	
	/**
	 * Send a large network packet (example : a nbt with large amount of NBT
	 * tag, the world datas) to player nearby by FLE channel.
	 * 
	 * @see fle.api.net.FleNetworkHandler
	 * @param packet
	 * @param range
	 */
	public void sendLarge(IPacket pkg, float range)
	{
		if (!isClient())
		{
			FleAPI.mod.getNetworkHandler().sendLargePacket(pkg,
					new TargetPoint(worldObj.provider.getDimensionId(),
							pos.getX() + 0.5F, pos.getY() + 0.5F,
							pos.getZ() + 0.5F, range));
		}
	}
	
	/**
	 * Send all of NBT by nbt packet to player nearby by FLE channel.
	 * 
	 * @see fle.api.net.FlePackets
	 * @see fle.api.net.FleNetworkHandler
	 * @param packet
	 * @param range
	 */
	public void markNBTUpdate()
	{
		try
		{
			syncNBT();
		}
		catch (Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
	}
	
	/**
	 * Mark for render update.
	 * 
	 * @see net.minecraft.world.World
	 */
	public void markRenderForUpdate()
	{
		worldObj.markBlockRangeForRenderUpdate(pos, pos);
	}
	
	/**
	 * Mark for tile update, this method will cause block updating.
	 * 
	 * @see net.block.Block
	 * @see net.minecraft.world.World
	 */
	public void markForUpdate()
	{
		markDirty();
		worldObj.markBlockForUpdate(pos);
		markRenderForUpdate();
	}
	
	// ==============Meta handler====================
	/**
	 * REMOVE IN 1.8! <br>
	 * Set tile meta of this tile entity. Info: this meta is similar with world
	 * data & 15 (Because minecraft can only save 16 meta before 1.8) If this
	 * meta is not similar with world meta, this tile will cause a update when
	 * initialized. This data is not same return with
	 * <code> fle.api.world.IWorldManager.getData </code>.
	 * 
	 * @see net.block.Block
	 * @see net.minecraft.world.World
	 * @see fle.api.world.IWorldManager
	 */
	/*
	 * public void setMetadata(short meta) { blockMetadata = meta; }
	 * 
	 * public short getMetadata() { return (short) blockMetadata; }
	 */
	
	// ===============Net work start=================
	/**
	 * Get packet information from this tile. Used in NWH to get message.
	 * 
	 * @see fle.api.net.FleNetworkHandler
	 * @param type
	 * @return
	 */
	@Override
	public Object onEmit(byte aType)
	{
		return emmit(aType);
	}
	
	protected short emmit(byte aType)
	{
		return 0;
	}
	
	/**
	 * Receive packet information from network.
	 * 
	 * @param type
	 *            The message type of this.
	 * @param contain
	 *            The contain of this elements often to be an number.
	 */
	@Override
	public void onReceive(byte type, Object contain)
	{
		if (contain instanceof Number)
		{
			receiveNumber(type, (Number) contain);
		}
	}
	
	protected void receiveNumber(byte type, Number number)
	{
		;
	}
	
	@Override
	public int getProgressSize()
	{
		return 0;
	}
	
	@Override
	public int getProgress(int id)
	{
		return 0;
	}
	
	@Override
	public void setProgress(int id, int value)
	{
	
	}
	
	// =========Long flag system start===========
	protected static final int COVER = 8;
	
	/**
	 * Far Land Era flag system. Cached a 64 bit-data for flag.
	 */
	private long flags = 0;
	
	protected void disable(int i)
	{
		flags &= ~(1 << i);
	}
	
	protected void enable(int i)
	{
		flags |= (1 << i);
	}
	
	protected boolean should(int i)
	{
		return (flags & (1 << i)) != 0;
	}
	
	// ============World conf start===============
	public int getEnviormentLight()
	{
		return worldObj.getLight(pos);
	}
	
	public long getEnviormentTemperature()
	{
		return FleAPI.getThermalNet().getEnviormentTemperature(worldObj, pos);
	}
	
	public boolean isRedStoneEmmit(Direction direction)
	{
		return worldObj.getRedstonePower(pos, direction.toFacing()) != 0;
	}
	
	public boolean isCatchRain()
	{
		if (worldObj.isRaining())
		{
			for (int i = pos.getY() + 1; i < 256; ++i)
			{
				BlockPos pos = new BlockPos(this.pos.getX(), i,
						this.pos.getZ());
				if (worldObj.getBlockState(pos).getBlock().isLeaves(worldObj,
						pos)
						|| worldObj.getBlockState(pos).getBlock().isSideSolid(
								worldObj, pos, Direction.UP.toFacing()))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
}