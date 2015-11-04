package fle.api.te;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.block.IFacingBlock;
import fle.api.cover.Cover;
import fle.api.cover.CoverRegistry;
import fle.api.cover.CoverTile;
import fle.api.enums.EnumWorldNBT;
import fle.api.net.INetEventHandler;
import fle.api.net.IPacket;
import fle.api.soild.ISolidTanks;
import fle.api.util.FleLog;
import fle.api.world.BlockPos;

/**
 * The base class of FLE tile entities.
 * @author ueyudiud
 *
 */
public class TEBase extends TileEntity implements ITEInWorld, IFacingBlock, IMetadataTile, INetEventHandler, ICoverTE
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
	protected ForgeDirection dir;
	/**
	 * The nbt cache of tile during the loading.
	 * When tile is loading, it need update tile, with setTileEntity
	 * when it cause a different meta in world data.
	 * Use {@link TEBase.readFromNBT} when reset nbt when initializing.
	 */
	private NBTTagCompound nbt;
	
	protected final CoverTile[] covers = new CoverTile[6];
	
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
		if(init)
		{
			if(nbt.hasKey("CoverList"))
			{
				NBTTagList list = nbt.getTagList("CoverList", 10);
				for(int i = 0; i < list.tagCount(); ++i)
				{
					NBTTagCompound nbt1 = list.getCompoundTagAt(i);
					int f = nbt1.getByte("Facing");
					if(f > 0 && f < 7)
					{
						--f;
						Cover cover = CoverRegistry.getCoverRegister().get(nbt1.getString("Cover"));
						if(cover != null)
							covers[f] = cover.createCoverTile(this);
						if(covers[f] != null)
							covers[f].readFromNBT(nbt1);
					}
				}
			}
		}
		else
		{
			this.nbt = nbt;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("BlockFacing", (byte) (dir == null ? ForgeDirection.UNKNOWN.ordinal() : dir.ordinal()));
		nbt.setShort("TileMeta", (short) getMetadata());
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < covers.length; ++i)
		{
			if(covers[i] != null)
			{
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("Facing", (byte) (i + 1));
				nbt1.setString("Cover", covers[i].getCover().getUnlocalizedName());
				covers[i].writeToNBT(nbt1);
				list.appendTag(nbt1);
			}
		}
		nbt.setTag("CoverList", list);
	}

	private boolean init = false;
	private boolean postinit = true;
	
	@Override
	public void updateEntity()
	{
		if(!init)
		{
			if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != blockMetadata && blockMetadata != -1)
			{
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, blockMetadata, 3);
				if(getBlockType().createTileEntity(worldObj, blockMetadata).getClass() != getClass())
				{
					worldObj.removeTileEntity(xCoord, yCoord, zCoord);
					TileEntity tile = blockType.createTileEntity(worldObj, blockMetadata);
					tile.blockMetadata = blockMetadata;
					if(tile instanceof TEBase)
					{
						((TEBase) tile).markFinishInit();
					}
					if(nbt != null) tile.readFromNBT(nbt);
					worldObj.setTileEntity(xCoord, yCoord, zCoord, tile);
				}
			}
			else if(blockMetadata == -1)
			{
				blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			}
			FLE.fle.getWorldManager().setData(getBlockPos(), EnumWorldNBT.Metadata, blockMetadata);
			markFinishInit();
		}
		else if(!postinit)
		{
			onPostinit();
			markNBTUpdate();
			markRenderForUpdate();
			postinit = true;
		}
	}
	
	protected void updateCovers()
	{
		for(int i = 0; i < covers.length; ++i)
		{
			if(covers[i] != null)
				covers[i].onBlockUpdate();
		}
	}
	
	protected void onPostinit()
	{
		
	}
	
	private void markFinishInit()
	{
		init = true;
		postinit = false;
	}
	
	@Override
	public TileEntity getTileEntity() 
	{
		return this;
	}

	@Override
	public int getLightValue() 
	{
		return (int) (16 * worldObj.getLightBrightness(xCoord, yCoord, zCoord));
	}

	@Override
	public boolean isRedStoneEmmit() 
	{
		return worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) != 0;
	}

	@Override
	public World getWorldObj() 
	{
		return worldObj;
	}

	@Override
	public BlockPos getBlockPos() 
	{
		return new BlockPos(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public ForgeDirection getDirction(BlockPos pos) 
	{
		if(dir == ForgeDirection.UNKNOWN || dir == null)
		{
			if(worldObj.isRemote)
			{
				FLE.fle.getWorldManager().markPosForUpdate(pos);
				dir = FLE.fle.getWorldManager().getData(pos, 1) != -1 ? ForgeDirection.VALID_DIRECTIONS[FLE.fle.getWorldManager().getData(pos, EnumWorldNBT.Facing)] : ForgeDirection.NORTH;
			}
			else
				dir = ForgeDirection.VALID_DIRECTIONS[FLE.fle.getWorldManager().getData(pos, EnumWorldNBT.Facing)];
		}
		return dir;
	}

	@Override
	public boolean canSetDirection(BlockPos pos, ItemStack tool, float xPos,
			float yPos, float zPos) 
	{
		return false;
	}

	@Override
	public void setDirection(World world, BlockPos pos, ItemStack tool,
			float xPos, float yPos, float zPos) 
	{
		
	}

	@Override
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
	
	public void syncSolidTank()
	{
		if(this instanceof ISolidTanks && !worldObj.isRemote)
			sendToNearBy(FleAPI.mod.getNetworkHandler().getPacketMaker().makeSolidTankPacket(this), 16.0F);
	}
	
	public void syncFluidTank()
	{
		if(this instanceof IFluidTanks && !worldObj.isRemote)
			sendToNearBy(FleAPI.mod.getNetworkHandler().getPacketMaker().makeFluidTankPacket(this), 16.0F);
	}
	
	public void syncNBT()
	{
		if(!worldObj.isRemote)
			sendLarge(FleAPI.mod.getNetworkHandler().getPacketMaker().makeNBTPacket(this), 256F);
	}
	
	/**
	 * Send a network packet to player nearby by FLE channel.
	 * @see fle.api.net.FleNetworkHandler
	 * @param packet
	 * @param range
	 */
	public void sendToNearBy(IPacket packet, float range)
	{
		FleAPI.mod.getNetworkHandler().sendToNearBy(packet, this, range);
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
		if(!worldObj.isRemote)
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
		if(worldObj.isRemote)
		{
			worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
		}
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
	@Override
	public void setMetadata(short meta)
	{
		blockMetadata = meta;
	}

	@Override
	public short getMetadata()
	{
		return (short) blockMetadata;
	}

	/**
	 * Get packet information from this tile.
	 * Used in NWH to get message.
	 * @see fle.api.net.FleNetworkHandler
	 * @param type
	 * @return
	 */
	@Override
	public Object onEmmit(byte aType)
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
	public void onReseave(byte type, Object contain)
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
	public Cover getCover(ForgeDirection dir)
	{
		return covers[FleAPI.getIndexFromDirection(dir)].getCover();
	}

	@Override
	public boolean addCover(ForgeDirection dir, Cover cover)
	{
		if(getCover(dir) != null) return false;
		covers[FleAPI.getIndexFromDirection(dir)] = cover.createCoverTile(this);
		return true;
	}

	@Override
	public void setCover(ForgeDirection dir, Cover cover)
	{
		covers[FleAPI.getIndexFromDirection(dir)] = cover.createCoverTile(this);
	}

	@Override
	public void removeCover(ForgeDirection dir)
	{
		covers[FleAPI.getIndexFromDirection(dir)] = null;
	}
}