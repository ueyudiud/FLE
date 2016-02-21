package flapi.cg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiButton;
import flapi.cg.IGuideType.GuideTransInfo;
import flapi.cg.IGuideType.IGuidePage;
import flapi.gui.GuiIconButton;
import flapi.gui.GuiIconButton.ButtonSize;
import flapi.util.FleLog;

public class GuiBook extends GuiBookBase
{
	protected int selectType;
	protected int selectPage;
	
	protected IGuideType[] types;
	protected IGuidePage[][] pages;
	protected RecipesTab tab;

	public GuiBook(RecipesTab aTab)
	{
		tab = aTab;
		types = CraftGuide.instance.getTabGuides(aTab);
		pages = new IGuidePage[types.length][];
		for(int i = 0; i < types.length; ++i)
		{
			List<IGuidePage> l = types[i].getAllPage();
			pages[i] = l.toArray(new IGuidePage[l.size()]);
		}
		selectPage = 0;
		selectType = 0;
	}
	
	@Override
	public IGuidePage getShowingRecipe()
	{
		return pages[selectType][selectPage];
	}

	@Override
	protected IGuideType getActiveType()
	{
		return types[selectType];
	}

	@Override
	public void initGui()
	{
		super.initGui();
		xoffset = (width - xSize) / 2 - 5;
		yoffset = (height - ySize) / 2 + 5;
		buttonList.add(new GuiIconButton(0, xoffset + 16, yoffset + 4, ButtonSize.Small, GuiIconButton.buttonLocate, 64, 0));
		buttonList.add(new GuiIconButton(1, xoffset + 176 - 16 - 10, yoffset + 4, ButtonSize.Small, GuiIconButton.buttonLocate, 72, 0));
		buttonList.add(new GuiIconButton(2, xoffset + 16, yoffset + 166 - 4 - 10, ButtonSize.Small, GuiIconButton.buttonLocate, 64, 0));
		buttonList.add(new GuiIconButton(3, xoffset + 176 - 16 - 10, yoffset + 166 - 4 - 10, ButtonSize.Small, GuiIconButton.buttonLocate, 72, 0));
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		if(button.id == 0)
		{
			--selectType;
			if(selectType < 0) selectType = types.length - 1;
			selectPage = 0;
		}
		else if(button.id == 1)
		{
			++selectType;
			if(selectType >= types.length) selectType = 0;
			selectPage = 0;
		}
		else if(button.id == 2)
		{
			selectPage -= isShiftKeyDown() ? 5 : 1;
			if(selectPage < 0) selectPage = pages[selectType].length - 1;
		}
		else if(button.id == 3)
		{
			selectPage += isShiftKeyDown() ? 5 : 1;
			if(selectPage >= pages[selectType].length) selectPage = 0;
		}
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		IGuidePage page = getShowingRecipe();
		try
		{
			page.onUpdate(this);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().warn("FLE API : some mod has bug recipe item, please report this bug to modder.", e);
		}
	}
	
	protected void markRecipeForUpdate()
	{
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int type)
	{
		super.mouseClicked(mouseX, mouseY, type);
		if(type == 0)
		{
			xoffset = (width - xSize) / 2;
			yoffset = (height - ySize) / 2;
			IGuidePage page;
			page = (IGuidePage) getShowingRecipe();
			GuideTransInfo info = types[selectType].b(page, mouseX - xoffset, mouseY - yoffset);
			if(info != null)
			{
				cacheTypeList = new HashMap();
				CraftGuide.instance.getTabGuides(this, tab, info);
				if(!cacheTypeList.isEmpty())
				{
					types = new IGuideType[cacheTypeList.size()];
					pages = new IGuidePage[cacheTypeList.size()][];
					int i = 0;
					for(Entry<IGuideType, List<IGuidePage>> e : cacheTypeList.entrySet())
					{
						types[i] = e.getKey();
						pages[i] = e.getValue().toArray(new IGuidePage[e.getValue().size()]);
						++i;
					}
					selectType = 0;
					selectPage = 0;
					cacheTypeList = null;
				}
			}
		}
	}
	
	private Map<IGuideType, List<IGuidePage>> cacheTypeList;
		
	void putRecipeInCache(IGuideType type, List<IGuidePage> pages)
	{
		if(!pages.isEmpty())
		{
			if(cacheTypeList.containsKey(type))
				cacheTypeList.get(type).addAll(pages);
			else cacheTypeList.put(type, pages);
		}
	}
	
	@Override
	protected String getPageString()
	{
		return String.format("Page %d/%d", selectPage + 1, pages[selectType].length);
	}
}