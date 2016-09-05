package farcore.lib.util;

import java.util.ArrayList;
import java.util.List;

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
		list.add(LanguageManager.translateToLocal(arg, translation));
	}
	
	public void addNotNull(String arg, Object...translation)
	{
		String val = LanguageManager.translateToLocalWithIgnoreUnmapping(arg, translation);
		if(val != null)
		{
			list.add(val);
		}
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