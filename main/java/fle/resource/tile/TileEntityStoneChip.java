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
import fle.core.enums.EnumRockSize;
import fle.init.Substances;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public class TileEntityStoneChip extends TEUpdatable implements ILoadInitTileEntity
{
	@NBTLoad(name = "rock")
	@NBTSave(name = "rock")
	public Substance rock = Substances.$void;
	@NBTLoad(name = "size")
	@NBTSave(name = "size")
	public EnumRockSize size = EnumRockSize.small;

	@Override
	public void sendData(FleDataOutputStream stream) throws IOException
	{
		stream.writeString(rock.getName());
		stream.writeInt(size.ordinal());
	}

	@Override
	public void receiveData(FleDataInputStream stream) throws IOException
	{
		rock = FleResource.rock.get(stream.readString());
		size = EnumRockSize.values()[stream.readInt()];
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