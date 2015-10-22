package fle.api.te;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.block.IFacingBlock;
import fle.api.enums.EnumWorldNBT;
import fle.api.net.FLENBTPacket;
import fle.api.net.FleAbstractPacket;
import fle.api.world.BlockPos;

public class TEBase extends TileEntity implements ITEInWorld, IFacingBlock, IMetadataTile
{
	protected final Random rand = new Random();
	protected ForgeDirection dir;
	private NBTTagCompound nbt;
	
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
		this.nbt = nbt;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("BlockFacing", (byte) (dir == null ? ForgeDirection.UNKNOWN.ordinal() : dir.ordinal()));
		nbt.setShort("TileMeta", (short) getMetadata());
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
					if(nbt != null) tile.readFromNBT(nbt);
					if(tile instanceof TEBase)
					{
						((TEBase) tile).markFinishInit();
					}
					worldObj.setTileEntity(xCoord, yCoord, zCoord, tile);
				}
			}
			else if(blockMetadata == -1)
			{
				blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			}
			markRenderForUpdate();
			FLE.fle.getWorldManager().setData(getBlockPos(), EnumWorldNBT.Metadata, blockMetadata);
			markFinishInit();
		}
		if(!postinit)
		{
			markNBTUpdate();
			postinit = true;
		}
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
	
	public void sendToNearBy(FleAbstractPacket packet, float range)
	{
		FleAPI.mod.getNetworkHandler().sendToNearBy(packet, new TargetPoint(worldObj.provider.dimensionId, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, range));
	}
	
	public void sendLarge(FleAbstractPacket pkg, float range)
	{
		if(!worldObj.isRemote)
		{
			FleAPI.mod.getNetworkHandler().sendLargePacket(pkg, new TargetPoint(worldObj.provider.dimensionId, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, range));
		}
	}
	
	public void markNBTUpdate()
	{
		sendLarge(new FLENBTPacket(this), 256F);
	}
	
	public void markRenderForUpdate()
	{
		if(worldObj.isRemote)
		{
			worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
		}
	}
	
	public void markForUpdate()
	{
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markRenderForUpdate();
	}

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
}