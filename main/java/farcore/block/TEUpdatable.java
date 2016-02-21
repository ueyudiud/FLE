package farcore.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TEUpdatable extends TEBase
{	
	protected static final int INIT = 0;
	protected static final int POSTINIT = 1;
	/**
	 * The nbt cache of tile during the loading.
	 * When tile is loading, it need update tile, with setTileEntity
	 * when it cause a different meta in world data.
	 * Use {@link TEBase.readFromNBT} when reset nbt when initializing.
	 */
	protected NBTTagCompound nbt;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
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
					if(tile instanceof TEUpdatable)
					{
						((TEUpdatable) tile).markFinishInit();
						((TEUpdatable) tile).nbt = nbt;
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
}