package fle.core.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import flapi.material.IMolecular;
import flapi.material.Matter;
import flapi.net.FleCoordinatesPacket;
import flapi.net.FleNetworkHandler;
import flapi.te.interfaces.IMatterContainer;
import flapi.te.interfaces.IObjectInWorld;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;

public class FleMatterUpdatePacket extends FleCoordinatesPacket
{
	Map<IMolecular, Integer> map;
	
	public FleMatterUpdatePacket()
	{
		super(true);
		map = new HashMap<IMolecular, Integer>();
	}
	public FleMatterUpdatePacket(IObjectInWorld oiw, IMatterContainer mc)
	{
		super(true, oiw.getBlockPos());
		map = mc.getMatterContain();
	}

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		Map<IMolecular, Integer> map = new HashMap();
		for(Entry<IMolecular, Integer> entry : this.map.entrySet())
		{
			if(entry.getKey() == null || entry.getValue() <= 0) continue;
			map.put(entry.getKey(), entry.getValue());
		}
		os.writeInt(map.size());
		for(Entry<IMolecular, Integer> entry : map.entrySet())
		{
			os.writeString(entry.getKey().getChemName());
			os.writeInt(entry.getValue());
		}
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		super.read(is);
		int size = is.readInt();
		for(int i = 0; i < size; ++i)
		{
			Matter matter = Matter.getMatterFromName(is.readString());
			int l = is.readInt();
			map.put(matter, l);
		}
	}

	@Override
	public Object process(FleNetworkHandler nwh)
	{
		if(pos().getBlockTile() instanceof IMatterContainer)
		{
			((IMatterContainer) pos().getBlockTile()).setMatterContain(map);
		}
		return null;
	}
}