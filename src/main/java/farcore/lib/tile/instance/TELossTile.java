package farcore.lib.tile.instance;

import java.util.Map;

import farcore.lib.nbt.NBTTagCompoundEmpty;
import farcore.lib.tile.ISynchronizableTile;
import farcore.lib.tile.abstracts.TEBase;
import farcore.util.U;
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
			if(tile == null) throw new RuntimeException("Errored tile entity.");
			worldObj.setTileEntity(pos, tile);
			tile.onLoad();
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
		Map<String, Class<? extends TileEntity>> map = (Map<String, Class<? extends TileEntity>>) U.R.getValue(TileEntity.class, "nameToClassMap", "", null, false);
		TileEntity tile;
		try
		{
			tile = map.get(nbt.getString("id")).newInstance();
		}
		catch(Exception exception)
		{
			return;
		}
		if(tile instanceof TELossTile) return;
		if(tile instanceof ISynchronizableTile)
		{
			((ISynchronizableTile) tile).readFromDescription(nbt);
		}
		worldObj.setTileEntity(pos, tile);
		invalidate();
	}
}