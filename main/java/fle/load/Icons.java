package fle.load;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.ITextureLoadListener;
import fle.api.recipe.machine.PolishRecipe;
import fle.api.recipe.machine.PolishRecipe.PolishCondition;
import fle.api.recipe.machine.PolishRecipe.PolishResource;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class Icons
{
	public static IIcon washing;

	@SideOnly(Side.CLIENT)
	public static class Bottoms implements ITextureLoadListener
	{
		@Override
		public void registerIcons(IIconRegister register)
		{
			washing = register.registerIcon("fle:washing");
		}
	}

	@SideOnly(Side.CLIENT)
	public static class Conditions implements ITextureLoadListener
	{
		@Override
		public void registerIcons(IIconRegister register)
		{
			for(PolishCondition condition : PolishRecipe.conditionRegister)
			{
				condition.registerIcons(register);
			}
		}		
	}
}