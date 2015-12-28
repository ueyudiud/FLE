package flapi.te;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import flapi.FleAPI;
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
public class TEBase extends TileEntity implements INetEventHandler, IContainerNetworkUpdate, ITEInWorld
{
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
	/**
	 * The nbt cache of tile during the loading.
	 * When tile is loading, it need update tile, with setTileEntity
	 * when it cause a different meta in world data.
	 * Use {@link TEBase.readFromNBT} when reset nbt when initializing.
	 */
	protected NBTTagCompound nbt;
	
	/**
	 * Set direction of block.
	 * @param direction
	 */
	public void setDirction(ForgeDirection aDirection)
	{
		dir = aDirection;
	}
	
	public TEBase() 
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		dir = ForgeDirection.values()[nbt.getByte("BlockFacing")];
		blockMetadata = nbt.getShort("TileMeta");
		if(should(INIT))
		{
			readFromNBT2(nbt);
		}
		else
		{
			this.nbt = nbt;
		}
	}
	
	public void readFromNBT2(NBTTagCompound nbt)
	{
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("BlockFacing", (byte) (dir == null ? ForgeDirection.UNKNOWN.ordinal() : dir.ordinal()));
		nbt.setShort("TileMeta", (short) getMetadata());
	}
	
	@Override
	public final void updateEntity()
	{
		if(!should(INIT))
		{
			if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != blockMetadata && blockMetadata != -1)
			{
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, blockMetadata, 3);
				if(getBlockType().createTileEntity(worldObj, blockMetadata).getClass() != getClass())
				{
					worldObj.removeTileEntity(xCoord, yCoord, zCoord);
					TileEntity tile = blockType.createTileEntity(worldObj, blockMetadata);
					if(tile instanceof TEBase)
					{
						((TEBase) tile).markFinishInit();
						((TEBase) tile).nbt = nbt;
					}
					if(nbt != null)
					{
						tile.readFromNBT(nbt);
					}
					tile.blockMetadata = blockMetadata;
					tile.validate();
					invalidate();
					worldObj.setTileEntity(xCoord, yCoord, zCoord, tile);
				}
			}
			else if(blockMetadata == -1)
			{
				blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			}
			markFinishInit();
			return;
		}
		else if(!should(POSTINIT))
		{
			onPostinit(nbt);
			markNBTUpdate();
			markRenderForUpdate();
			nbt = null;
			enable(POSTINIT);
			return;
		}
		update();
	}
	
	protected void update()
	{
		
	}
	
	protected void onPostinit(NBTTagCompound nbt)
	{
		onPostinit();
	}
	
	protected void onPostinit()
	{
		
	}
	
	private void markFinishInit()
	{
		enable(INIT);
		disable(POSTINIT);
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

	public ForgeDirection getDirction() 
	{
		return dir == null ? ForgeDirection.NORTH : dir;
	}
	
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
	protected static final int INIT = 0;
	protected static final int POSTINIT = 1;
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

	public void dropItem(ItemStack stack)
	{
		if(stack == null) return;
        if (!worldObj.isRemote && worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops") && !worldObj.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            float f = 0.7F;
            double d0 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(worldObj, (double)xCoord + d0, (double)yCoord + d1, (double)zCoord + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
            worldObj.spawnEntityInWorld(entityitem);
        }
	}

	@Override
	public TileEntity getTileEntity()
	{
		return this;
	}
}