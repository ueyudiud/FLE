package fle.api.cg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fle.api.util.FleLog;

public class RecipesTab
{
	private static final Map<String, RecipesTab> map = new HashMap();
	
	public static RecipesTab tabOldStoneAge = registerNewTab("oldStoneAge");
	public static RecipesTab tabNewStoneAge = registerNewTab("newStoneAge");
	public static RecipesTab tabCopperAge = registerNewTab("copperAge");
	public static RecipesTab tabBronzeAge = registerNewTab("bronzeAge");
	public static RecipesTab tabClassic = registerNewTab("classic");

	public static RecipesTab registerNewTab(String tab)
	{
		if(map.containsKey(tab))
		{
			FleLog.getLogger().warn("Some mod has registied tab name " + tab);
			return map.get(tab);
		}
		else
		{
			RecipesTab ret = new RecipesTab(tab);
			map.put(tab, ret);
			CraftGuide.instance.recipeList.put(ret, new ArrayList());
			return ret;
		}
	}
	public static RecipesTab registerNewTab(RecipesTab tab)
	{
		if(map.containsKey(tab.tabName))
		{
			FleLog.getLogger().warn("Some mod has registied tab name " + tab.tabName);
			return map.get(tab.tabName);
		}
		else
		{
			map.put(tab.tabName, tab);
			CraftGuide.instance.recipeList.put(tab, new ArrayList());
			return tab;
		}
	}
	
	public String tabName;
	
	public RecipesTab(String aName)
	{
		tabName = aName;
	}
	
	public final String getRecipe()
	{
		return tabName;		
	}
}