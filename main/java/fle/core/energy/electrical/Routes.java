package fle.core.energy.electrical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.util.BlockPos;

public class Routes
{
	private Map<Node, List<Link>> map = new HashMap();
	
	public Routes()
	{
		
	}
	
	public boolean markDirty(BlockPos pos)
	{
		for(Node node : map.keySet())
		{
			if(node.isConnect(pos))
				return true;
		}
		return false;
	}
	
	@Override
	public Routes clone()
	{
		Routes ret = new Routes();
		ret.map.putAll(map);
		return ret;
	}
	
	public Set<Node> nodeSet()
	{
		return map.keySet();
	}
	
	public Set<Entry<Node, List<Link>>> links()
	{
		return map.entrySet();
	}
	
	public List<Link> link(Node node)
	{
		return map.get(node);
	}
	
	public void addNode(Node node)
	{
		if(map.containsKey(node)) return;
		map.put(node, new ArrayList());
	}
	
	public void addLink(Link link)
	{
		assert(link != null);
		addNode(link.tile1);
		addNode(link.tile2);
		if(!link(link.tile1).contains(link)) link(link.tile1).add(link);
		if(!link(link.tile2).contains(link)) link(link.tile2).add(link);
	}
	
	public void clear()
	{
		map.clear();
	}
	
	public List<Link> remove(Node node)
	{
		return map.remove(node);
	}
	
	public void remove(Link link)
	{
		assert(link != null);
		if(map.containsKey(link.tile1) && map.containsKey(link.tile2))
		{
			map.get(link.tile1).remove(link);
			map.get(link.tile2).remove(link);
		}
	}
}