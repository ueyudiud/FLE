package fle.core.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fle.api.material.IAtoms;
import fle.api.material.Matter;
import fle.api.net.FleCoordinatesPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.te.IMatterContainer;
import fle.api.te.IObjectInWorld;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;

public class FleMatterUpdatePacket extends FleCoordinatesPacket
{
	Map<IAtoms, Integer> map;
	
	public FleMatterUpdatePacket()
	{
		super(true);
		map = new HashMap<IAtoms, Integer>();
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
		Map<IAtoms, Integer> map = new HashMap();
		for(Entry<IAtoms, Integer> entry : this.map.entrySet())
		{
			if(entry.getKey() == null || entry.getValue() <= 0) continue;
			map.put(entry.getKey(), entry.getValue());
		}
		os.writeInt(map.size());
		for(Entry<IAtoms, Integer> entry : map.entrySet())
		{
			os.writeString(entry.getKey().getChemicalFormulaName());
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