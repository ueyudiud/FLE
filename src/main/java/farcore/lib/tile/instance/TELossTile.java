package farcore.lib.tile.instance;

import farcore.lib.nbt.NBTTagCompoundEmpty;
import farcore.lib.tile.TEBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * For tile losing tile entity instead transiently.<br>
 * Often use in client side when it does not synchronize
 * tile entity with server.
 * @author ueyudiud
 *
 */
public class TELossTile extends TEBase
{
	private NBTTagCompound tileNBT = NBTTagCompoundEmpty.instance;
	private NBTTagCompound serverPacketData = new NBTTagCompound();
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if(nbt.hasKey("tile"))
		{
			tileNBT = nbt.getCompoundTag("tile");
		}
		else
		{
			tileNBT = nbt;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(!isInvalid())
		{
			nbt.setTag("tile", tileNBT);
		}
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void onLoad()
	{
		if(isServer())
		{
			if(isDebugWorld()) return;
			TileEntity tile = func_190200_a(worldObj, tileNBT);
			if(tile == null)
				throw new RuntimeException("Error tile.");
			worldObj.setTileEntity(pos, tile);
			tile.onLoad();
			invalidate();
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return tileNBT == NBTTagCompoundEmpty.instance ? super.getUpdateTag() : tileNBT;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		refreshTile(tag);
	}
	
	public void refreshTile(NBTTagCompound nbt)
	{
		TileEntity tile = func_190200_a(worldObj, nbt);
		if(tile instanceof TELossTile) return;
		worldObj.setTileEntity(pos, tile);
		invalidate();
	}
}