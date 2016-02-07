package fle.resource.tile;

import java.io.IOException;

import farcore.nbt.NBTLoad;
import farcore.nbt.NBTSave;
import farcore.substance.Substance;
import farcore.tile.ILoadInitTileEntity;
import farcore.tile.TEBase;
import farcore.tile.TEUpdatable;
import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;
import flapi.FleResource;
import fle.init.Substances;

public class TileEntitySand extends TEUpdatable implements ILoadInitTileEntity
{
	@NBTLoad(name = "sand")
	@NBTSave(name = "sand")
	public Substance sand = Substances.$void;

	@Override
	public void sendData(FleDataOutputStream stream) throws IOException
	{
		stream.writeString(sand.getName());
	}

	@Override
	public void receiveData(FleDataInputStream stream) throws IOException
	{
		sand = FleResource.sand.get(stream.readString());
	}

	@Override
	public void init()
	{
		syncNBT();	
	}

	@Override
	public void onClientUpdate()
	{
		
	}

	@Override
	public void update(long tick, long tick1)
	{
		syncNBT();
	}
}