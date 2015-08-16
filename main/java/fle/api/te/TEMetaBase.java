package fle.api.te;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.block.IFacingBlock;
import fle.api.world.BlockPos;

public class TEMetaBase extends TileEntity implements ITEInWorld, IMetadataTile, IFacingBlock
{
	private ForgeDirection dir = ForgeDirection.NORTH;
	
	public TEMetaBase()
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		blockMetadata = nbt.getShort("BlockMeta");
		dir = ForgeDirection.VALID_DIRECTIONS[nbt.getByte("Facing")];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("BlockMeta", (short) blockMetadata);
		nbt.setByte("Facing", new Integer(FleAPI.getIndexFromDirection(dir)).byteValue());
	}
	
	@Override
	public boolean canUpdate()
	{
		return false;
	}

	public World getWorldObj() 
	{
		return worldObj;
	}

	public BlockPos getBlockPos() 
	{
		return new BlockPos(worldObj, xCoord, yCoord, zCoord);
	}
	
	public int getLightValue() 
	{
		return (int) (16 * worldObj.getLightBrightness(xCoord, yCoord, zCoord));
	}

	public boolean isRedStoneEmmit() 
	{
		return worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) != 0;
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

	@Override
	public void setMetadata(short aMeta)
	{
		blockMetadata = aMeta;
	}

	@Override
	public short getMetadata()
	{
		return (short) blockMetadata;
	}
	
	@Override
	public void markDirty()
	{
        if (this.worldObj != null)
        {
           worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, this);

            if (this.getBlockType() != Blocks.air)
            {
                this.worldObj.func_147453_f(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            }
        }
	}

	@Override
	public TileEntity getTileEntity()
	{
		return this;
	}

	
	@Override
	public ForgeDirection getDirction(BlockPos pos)
	{
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
}