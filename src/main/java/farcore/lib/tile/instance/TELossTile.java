package farcore.lib.tile.instance;

import farcore.lib.nbt.NBTTagCompoundEmpty;
import farcore.lib.net.tile.PacketTEAskType;
import farcore.lib.tile.ISynchronizableTile;
import farcore.lib.tile.TEUpdatable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * For tile losing tile entity instead transiently.<br>
 * Often use in client side when it does not synchronize
 * tile entity with server.
 * @author ueyudiud
 *
 */
public class TELossTile extends TEUpdatable implements ISynchronizableTile
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
	protected void updateEntity1()
	{
		if(isServer())
		{
			TileEntity tile = func_190200_a(worldObj, tileNBT);
			if(tile == null)
				throw new RuntimeException("Error tile.");
			worldObj.setTileEntity(pos, tile);
			invalidate();
		}
		else
		{
			sendToServer(new PacketTEAskType(this));
		}
	}

	@Override
	public void readFromDescription(NBTTagCompound nbt)
	{
		serverPacketData.merge(nbt);
	}
	
	public void refreshTile(NBTTagCompound nbt)
	{
		TileEntity tile = func_190200_a(worldObj, nbt);
		if(tile == null) return;
		worldObj.setTileEntity(pos, tile);
		invalidate();
	}
}