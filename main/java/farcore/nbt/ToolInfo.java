package farcore.nbt;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class ToolInfo
{
	public static final Map<String, ToolType> map = new HashMap();
	
	public static enum ToolType
	{
		pickaxe,
		axe,
		shovel,
		knife,
		sword,
		hoe;
				
		public final String name = name();
		
		ToolType()
		{
			map.put(name, this);
		}
	}
}