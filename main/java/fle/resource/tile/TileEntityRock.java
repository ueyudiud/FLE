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
import fle.core.enums.EnumRockState;
import fle.init.Substances;

public class TileEntityRock extends TEUpdatable implements ILoadInitTileEntity
{
	@NBTLoad(name = "rock")
	@NBTSave(name = "rock")
	public Substance rock = Substances.$void;
	@NBTLoad(name = "state")
	@NBTSave(name = "state")
	public EnumRockState state = EnumRockState.resource;

	@Override
	public void sendData(FleDataOutputStream stream) throws IOException
	{
		stream.writeString(rock.getName());
		stream.writeInt(state.ordinal());
	}

	@Override
	public void receiveData(FleDataInputStream stream) throws IOException
	{
		rock = FleResource.rock.get(stream.readString());
		state = EnumRockState.values()[stream.readInt()];
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