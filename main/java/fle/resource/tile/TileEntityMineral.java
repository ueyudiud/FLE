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

public class TileEntityMineral extends TEUpdatable implements ILoadInitTileEntity
{
	@NBTLoad(name = "ore")
	@NBTSave(name = "ore")
	public Substance ore = Substances.$void;
	@NBTLoad(name = "rock")
	@NBTSave(name = "rock")
	public Substance rock = Substances.$void;
	@NBTLoad(name = "state")
	@NBTSave(name = "state")
	public EnumRockState state = EnumRockState.resource;
	@NBTLoad(name = "amount")
	@NBTSave(name = "amount")
	public int amount;

	@Override
	public void sendData(FleDataOutputStream stream) throws IOException
	{
		stream.writeString(ore.getName());
		stream.writeString(rock.getName());
		stream.writeInt(state.ordinal());
		stream.writeInt(amount);
	}

	@Override
	public void receiveData(FleDataInputStream stream) throws IOException
	{
		ore = FleResource.mineral.get(stream.readString());
		rock = FleResource.rock.get(stream.readString());
		state = EnumRockState.values()[stream.readInt()];
		amount = stream.readInt();
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