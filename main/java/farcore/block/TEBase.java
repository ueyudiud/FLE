package farcore.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import flapi.FleAPI;
import flapi.net.FleBaseTEPacket;
import flapi.net.INetEventHandler;
import flapi.net.IPacket;
import flapi.solid.ISolidTanks;
import flapi.te.interfaces.IContainerNetworkUpdate;
import flapi.te.interfaces.IFluidTanks;
import flapi.util.FleLog;
import flapi.world.BlockPos;
import flapi.world.ITEInWorld;

/**
 * The base class of FLE tile entities.
 * @author ueyudiud
 *
 */
public class TEBase extends TileEntity
implements INetEventHandler, IContainerNetworkUpdate, ITEInWorld
{
	public static enum Facing
	{
		BOTTOM,
		SIDE,
		FRONT,
		TOP,
		BACK;
	}
	
	/**
	 * The default random of tile.
	 */
	public final Random rand = new Random();
	/**
	 * The facing of this block.
	 * It is use to rendering icon or check witch side can input fluid,
	 * save in NBT with tag "BlockFacing".
	 */
	protected ForgeDirection dir = ForgeDirection.UNKNOWN;
	
	public TEBase() 
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		dir = ForgeDirection.values()[nbt.getByte("BlockFacing")];
		blockMetadata = nbt.getShort("TileMeta");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("BlockFacing", (byte) (dir == null ? ForgeDirection.UNKNOWN.ordinal() : dir.ordinal()));
		nbt.setShort("TileMeta", (short) getMetadata());
	}
	
	public boolean isClient()
	{
		return worldObj.isRemote;
	}

	@Override
	public World getWorldObj() 
	{
		return worldObj;
	}

	public BlockPos getBlockPos() 
	{
		return new BlockPos(worldObj, xCoord, yCoord, zCoord);
	}
	
	protected void updateStates()
	{
		sendLarge(new FleBaseTEPacket(this, dir, lightOpacity, colourState), 64.0F);
	}
	
	//=============================Block state control=============================
	
	public void onBlockPlaced(ItemStack stack, EntityPlayer player, int side, float hitX, float hitY,
			float hitZ)
	{
		
	}

	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	
	public void onEntityWalking(Entity entity)
	{
		
	}

	public void onDrop(Block block, List<ItemStack> list, int meta, String key, int level)
	{
		list.add(new ItemStack(block, 1, meta));
	}

	public boolean canHarvestBlock(int metadata, String key, int level)
	{
		return true;
	}
	
	protected int lightOpacity;	
	
	public int getLightOpacity()
	{
		return lightOpacity;
	}
	
	//==================================Set block state==============================

	protected int[] colourState = {-1, -1, -1, -1, -1, -1};
	
	protected boolean canBeRecoloured()
	{
		return false;
	}
	
	public boolean recolourBlock(ForgeDirection side, int colour)
	{
		if(canBeRecoloured() && side != ForgeDirection.UNKNOWN && colourState[side.ordinal()] != -1)
		{
			colourState[side.ordinal()] = ItemDye.field_150922_c[colour];
			return true;
		}
		return false;
	}
	
	public void setColorState(int[] states)
	{
		if(worldObj.isRemote)
		{
			colourState = states;
			markRenderForUpdate();
		}		
	}
	
	public void setLightOpacity(int opacity)
	{
		lightOpacity = opacity;
		if(!worldObj.isRemote)
		{
			sendToNearBy(new FleBaseTEPacket(this, 2, opacity), 128.0F);
		}
		else
		{
			markForUpdate();
		}
	}
	
	/**
	 * Set direction of block.
	 * @param direction
	 */
	public void setDirction(ForgeDirection direction)
	{
		dir = direction;
		if(!worldObj.isRemote)
		{
			sendToNearBy(new FleBaseTEPacket(this, direction), 128.0F);
		}
	}

	public ForgeDirection getDirction() 
	{
		return dir == null ? ForgeDirection.NORTH : dir;
	}
	
	public Facing getFacing(ForgeDirection direction)
	{
		return dir == direction ? Facing.FRONT :
			dir.getOpposite() == direction ? Facing.BACK :
				direction == ForgeDirection.UP ? Facing.TOP :
					direction == ForgeDirection.DOWN ? Facing.BOTTOM :
						Facing.SIDE;
	}
	
	//==================================Sync method==========================
	public void syncSolidTank()
	{
		if(this instanceof ISolidTanks && !isClient())
		{
			sendToNearBy(FleAPI.mod.getNetworkHandler().getPacketMaker().makeSolidTankPacket(this), 16.0F);
		}
	}
	
	public void syncFluidTank()
	{
		if(this instanceof IFluidTanks && !isClient())
		{
			sendToNearBy(FleAPI.mod.getNetworkHandler().getPacketMaker().makeFluidTankPacket(this), 16.0F);
		}
	}
	
	public void syncNBT()
	{
		if(!isClient())
		{
			sendLarge(FleAPI.mod.getNetworkHandler().getPacketMaker().makeNBTPacket(this), 256F);
		}
	}
	
	/**
	 * Send a network packet to player nearby by FLE channel.
	 * @see fle.api.net.FleNetworkHandler
	 * @param packet
	 * @param range
	 */
	public void sendToNearBy(IPacket packet, float range)
	{
		if(!isClient())
		{
			FleAPI.mod.getNetworkHandler().sendToNearBy(packet, this, range);
		}
	}
	
	/**
	 * Send a network packet to server by FLE channel.
	 * @see fle.api.net.FleNetworkHandler
	 * @param packet
	 */
	public void sendToServer(IPacket packet)
	{
		if(isClient())
		{
			FleAPI.mod.getNetworkHandler().sendToServer(packet);
		}
	}

	/**
	 * Send a large network packet (example : a nbt with large amount of NBT tag, the world datas)
	 * to player nearby by FLE channel.
	 * @see fle.api.net.FleNetworkHandler
	 * @param packet
	 * @param range
	 */
	public void sendLarge(IPacket pkg, float range)
	{
		if(!isClient())
		{
			FleAPI.mod.getNetworkHandler().sendLargePacket(pkg, new TargetPoint(worldObj.provider.dimensionId, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, range));
		}
	}

	/**
	 * Send all of NBT by nbt packet to player nearby by FLE channel.
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
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
	}

	/**
	 * Mark for render update.
	 * @see net.minecraft.world.World
	 */
	public void markRenderForUpdate()
	{
		worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
	}

	/**
	 * Mark for tile update, this method will cause block updating.
	 * @see net.block.Block
	 * @see net.minecraft.world.World
	 */
	public void markForUpdate()
	{
		markDirty();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markRenderForUpdate();
	}

	//=====================================Meta handler====================================
	/**
	 * Set tile meta of this tile entity.
	 * Info: this meta is similar with world data & 15 (Because 
	 * minecraft can only save 16 meta before 1.8)
	 * If this meta is not similar with world meta, this tile
	 * will cause a update when initialized.
	 * This data is not same return with <code> fle.api.world.IWorldManager.getData </code>.
	 * @see net.block.Block
	 * @see net.minecraft.world.World
	 * @see fle.api.world.IWorldManager
	 */
	public void setMetadata(short meta)
	{
		blockMetadata = meta;
	}

	public short getMetadata()
	{
		return (short) blockMetadata;
	}
	
	//====================================Net work start==============================
	/**
	 * Get packet information from this tile.
	 * Used in NWH to get message.
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
	 * @param type The message type of this.
	 * @param contain The contain of this elements often to be an number.
	 */
	@Override
	public void onReceive(byte type, Object contain)
	{
		if(contain instanceof Number)
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

	//====================================Long flag system start================================
	protected static final int COVER = 8;
	
	private long flags = 0;
	
	protected void disable(int i)
	{
		flags &=~ (1 << i);
	}
	protected void enable(int i)
	{
		flags |= (1 << i);
	}
	protected boolean should(int i)
	{
		return (flags & (1 << i)) != 0;
	}
	
	//===========================World conf start==========================

	public int getLightValue() 
	{
		return (int) (16 * worldObj.getLightBrightness(xCoord, yCoord, zCoord));
	}

	public boolean isRedStoneEmmit() 
	{
		return worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) != 0;
	}

	public boolean isCatchRain() 
	{
		if(worldObj.isRaining())
		{
			for(int i = yCoord + 1; i < 256; ++i)
			{
				if(worldObj.getBlock(xCoord, yCoord, zCoord).isLeaves(worldObj, xCoord, i, zCoord) || worldObj.getBlock(xCoord, i, zCoord).isSideSolid(worldObj, xCoord, i, zCoord, ForgeDirection.UP))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public TileEntity getTileEntity()
	{
		return this;
	}
}