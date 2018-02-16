/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.compat.jei;

import static farcore.data.V.jeiXOffset;
import static farcore.data.V.jeiXSlotOff;
import static farcore.data.V.jeiYOffset;
import static farcore.data.V.jeiYSlotOff;

import farcore.FarCore;
import farcore.lib.compat.jei.ToolDisplayRecipeMap.ToolDisplayRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import nebula.base.A;
import nebula.base.Judgable;
import nebula.common.LanguageManager;
import nebula.common.util.Strings;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public class ToolDisplayRecipeCategory extends BlankRecipeCategory<ToolDisplayRecipeWrapper>
{
	private final IDrawable drawable;
	
	public ToolDisplayRecipeCategory(IGuiHelper helper)
	{
		this.drawable = helper.createDrawable(new ResourceLocation(FarCore.ID, "textures/gui/jei/tool.png"),
				jeiXOffset, jeiYOffset, 176 - jeiXOffset * 2, 83 - jeiYOffset * 2);
	}
	
	@Override
	public String getUid()
	{
		return "farcore.tool";
	}
	
	@Override
	public String getTitle()
	{
		return LanguageManager.translateToLocal("category.farcore.tool");
	}
	
	@Override
	public IDrawable getBackground()
	{
		return this.drawable;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ToolDisplayRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		final ToolDisplayRecipe recipe = recipeWrapper.recipe;
		final IGuiItemStackGroup group = recipeLayout.getItemStacks();
		
		if (recipe.chances != null && A.or(recipe.chances, Judgable.NOT_NULL))
		{
			group.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
				if (!input)
				{
					int i = slotIndex - 5;
					if (recipe.chances.length > i && recipe.chances[i] != null)
					{
						for (int chance : recipe.chances[i])
						{
							tooltip.add(LanguageManager.translateToLocal("jei.info.chance", Strings.progress(chance / 10000.0)));
						}
					}
				}
			});
		}
		
		group.init(0, true, 36 - jeiXSlotOff, 35 - jeiYSlotOff);
		group.set(0, recipe.input.display());
		if (recipe.tools.length > 0)
		{
			group.init(1, true, 54 - jeiXSlotOff, 24 - jeiYSlotOff);
			group.set(1, recipe.tools[0].display());
		}
		if (recipe.tools.length > 1)
		{
			group.init(2, true, 71 - jeiXSlotOff, 24 - jeiYSlotOff);
			group.set(2, recipe.tools[1].display());
		}
		if (recipe.tools.length > 2)
		{
			group.init(3, true, 54 - jeiXSlotOff, 46 - jeiYSlotOff);
			group.set(3, recipe.tools[2].display());
		}
		if (recipe.tools.length > 3)
		{
			group.init(4, true, 71 - jeiXSlotOff, 46 - jeiYSlotOff);
			group.set(4, recipe.tools[3].display());
		}
		int idx = 0;
		label: for (int j = 0; j < 3; ++j)
		{
			for (int i = 0; i < 3; ++i)
			{
				if (idx >= recipe.outputs.length)
					break label;
				group.init(idx + 5, false, 89 + 17 * i - jeiXSlotOff, 18 + 17 * j - jeiYSlotOff);
				if (recipe.outputs[idx] != null)
				{
					group.set(idx + 5, recipe.outputs[idx].instance());
				}
				++ idx;
			}
		}
	}
}
