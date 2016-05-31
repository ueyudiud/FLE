package farcore.lib.net.world;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import farcore.handler.FarCoreDataHandler;
import farcore.lib.collection.IRegister;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.U;
import farcore.util.io.DataStream;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PacketRegisterSynclize extends PacketAbstract
{
	Map<String, IRegister<?>> map;
	String[] tags;
	byte[][] bytes;

	public PacketRegisterSynclize(Map<String, IRegister<?>> map)
	{
		this.map = map;
	}
	public PacketRegisterSynclize()
	{
		
	}

	@Override
	protected void encode(DataStream output) throws IOException
	{
		bytes = new byte[map.size()][];
		int i = 0;
		output.writeInt(i);
		for(Entry<String, IRegister<?>> entry : map.entrySet())
		{
			output.writeString(U.Lang.validate(entry.getKey()));
			output.writeBytes(FarCoreDataHandler.applySingle(entry.getValue()));
			++i;
		}
	}

	@Override
	protected void decode(DataStream input) throws IOException
	{
		int size = input.readInt();
		tags = new String[size];
		bytes = new byte[size][];
		for(int i = 0; i < size; ++i)
		{
			tags[i] = input.readString();
			bytes[i] = input.readBytes();
		}
	}
	
	@Override
	public IPacket process(NetworkBasic network)
	{
		for(int i = 0; i < tags.length; ++i)
		{
			IRegister<?> register = FarCoreDataHandler.worldSynclizeMap.get(tags[i]);
			if(register != null)
			{
				FarCoreDataHandler.arrangeSingle(register, bytes[i]);
			}
		}
		return null;
	}
}