package fla.core.util;

import java.util.ArrayList;
import java.util.List;

import fla.api.util.InfoBuilder;
import fla.api.world.BlockPos;
import fla.core.Fla;

public class InfomationHeat implements InfoBuilder<BlockPos>
{
	@Override
	public List<String> getInfo(BlockPos t) 
	{
		List<String> list = new ArrayList();
		list.add("Heat: " + Fla.fla.hm.getHeat(t) + " J.");
		return list;
	}

}
