package farcore.lib.util;

import java.util.ArrayList;
import java.util.List;

import farcore.FarCore;

public class UnlocalizedList
{
	List<String> list;
	
	public UnlocalizedList()
	{
		this(new ArrayList());
	}
	public UnlocalizedList(List<String> list)
	{
		this.list = list;
	}

	public void add(String arg, Object...translation)
	{
		list.add(FarCore.translateToLocal(arg, translation));
	}
	
	public void addLocal(String arg)
	{
		list.add(arg);
	}
	
	public List<String> list()
	{
		return list;
	}
}