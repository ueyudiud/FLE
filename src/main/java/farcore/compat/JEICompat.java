/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.compat;

import com.google.common.collect.Collections2;

import farcore.lib.compat.jei.SolidStackHelper;
import farcore.lib.compat.jei.SolidStackRender;
import farcore.lib.compat.jei.ToolDisplayRecipeCategory;
import farcore.lib.compat.jei.ToolDisplayRecipeHandler;
import farcore.lib.compat.jei.ToolDisplayRecipeMap;
import farcore.lib.solid.Solid;
import farcore.lib.solid.SolidStack;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import nebula.Nebula;
import nebula.base.function.F;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ueyudiud
 */
@JEIPlugin
public class JEICompat extends BlankModPlugin
{
	@Override
	public void register(IModRegistry registry)
	{
		IIngredientBlacklist ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();
		if (Nebula.fluid_displayment != null)
		{
			ingredientBlacklist.addIngredientToBlacklist(new ItemStack(Nebula.fluid_displayment, 1, OreDictionary.WILDCARD_VALUE));
		}
		registry.addRecipes(ToolDisplayRecipeMap.getList());
		registry.addRecipeCategories(new ToolDisplayRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeHandlers(new ToolDisplayRecipeHandler());
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{
		registry.register(
				SolidStack.class,
				Collections2.<Solid, SolidStack> transform(Solid.REGISTRY.targets(), F.cast(F.const2f(SolidStack::new, 1000))),
				new SolidStackHelper(), new SolidStackRender());
	}
}
