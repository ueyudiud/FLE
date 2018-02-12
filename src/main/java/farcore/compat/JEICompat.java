/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.compat;

import com.google.common.collect.Collections2;

import farcore.lib.compat.jei.SolidStackHelper;
import farcore.lib.compat.jei.SolidStackRender;
import farcore.lib.solid.Solid;
import farcore.lib.solid.SolidStack;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import nebula.common.util.L;

/**
 * @author ueyudiud
 */
@JEIPlugin
public class JEICompat extends BlankModPlugin
{
	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{
		registry.register(
				SolidStack.class,
				Collections2.<Solid, SolidStack> transform(Solid.REGISTRY.targets(), L.toFunction(SolidStack::new, 1000)),
				new SolidStackHelper(), new SolidStackRender());
	}
}
