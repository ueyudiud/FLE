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
	public static IIcon polish_delete;
	public static IIcon next;
	public static IIcon last;
	public static IIcon green;
	public static IIcon blue;

	public static IIcon block_drying_table;
	public static IIcon block_polish_top;
	public static IIcon block_polish_side;
	public static IIcon block_polish_bottom;
	public static IIcon firewood_top;
	public static IIcon firewood_side;
	public static IIcon block_manual_operating_platform_top;
	public static IIcon block_manual_operating_platform_side;
	public static IIcon block_manual_operating_platform_bottom;
	public static IIcon block_ceramics;
	public static IIcon block_ceramics_unsmelted;

	@SideOnly(Side.CLIENT)
	public static class Bottoms implements ITextureLoadListener
	{
		@Override
		public void registerIcons(IIconRegister register)
		{
			washing = register.registerIcon("fle:washing");
			polish_delete = register.registerIcon("fle:polish_delete");
			next = register.registerIcon("fle:next");
			last = register.registerIcon("fle:last");
			green = register.registerIcon("fle:green");
			blue = register.registerIcon("fle:blue");
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