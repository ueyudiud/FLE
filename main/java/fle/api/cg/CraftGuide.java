package fle.api.cg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.cg.IGuideType.GuideTransInfo;
import fle.api.cg.IGuideType.IGuidePage;
import fle.api.recipe.ItemAbstractStack;

public final class CraftGuide
{
	public static final CraftGuide instance = new CraftGuide();
		
	Map<RecipesTab, List<IGuideType>> typeList = new HashMap();
	
	private CraftGuide()
	{
		
	}

	public void registerGuideType(RecipesTab tab, IGuideType aType)
	{
		if(!typeList.containsKey(tab)) RecipesTab.registerNewTab(tab);
		typeList.get(tab).add(aType);
	}
	
	public IGuideType[] getTabGuides(RecipesTab tab)
	{
		List<IGuideType> ret = new ArrayList<IGuideType>();
		if(tab == null)
		{
			for(Entry<RecipesTab, List<IGuideType>> e : typeList.entrySet())
			{
				ret.addAll(e.getValue());
			}
		}
		else
		{
			if(typeList.containsKey(tab))
			{
				ret.addAll(typeList.get(tab));
			}
		}
		return ret.toArray(new IGuideType[ret.size()]);
	}
	
	public boolean getTabGuides(GuiBook gui, RecipesTab tab, GuideTransInfo aInfo)
	{
		IGuideType[] types = getTabGuides(tab);
		if(types.length == 0) return false;
		boolean flag = false;
		for(IGuideType type : types)
		{
			List<IGuidePage> pages = type.a(aInfo);
			if(pages != null && !pages.isEmpty())
			{
				gui.putRecipeInCache(type, pages);
				flag = true;
			}
		}
		return flag;
	}
}