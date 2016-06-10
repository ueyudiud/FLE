package fle.load.recipe;

import farcore.enums.EnumItem;
import farcore.lib.stack.OreStack;
import fle.api.recipe.machine.CeramicsRecipe;
import net.minecraft.item.ItemStack;

public class Ceramics
{
	public static void init()
	{
//		CeramicsRecipe.registerRecipe(new CeramicsRecipe(new ItemStack(IB.argil_unsmelted, 1, 0), new float[][]{
//			{0.5F, 0.625F}, {0.4375F, 0.5625F}, {0.875F, 1.0F}, {0.9375F, 1.0F}, {0.5F, 0.625F},
//			{0.5F, 0.625F}, {0.4375F, 0.5625F}, {0.875F, 1.0F}, {0.9375F, 1.0F}, {0.5F, 0.625F}}));
		CeramicsRecipe.registerRecipe(new CeramicsRecipe(new OreStack("argil", 6), EnumItem.stone_production.instance(4, "argil_brick_unsmelted"), new float[][]{
			{0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F},
			{0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}, {0.4375F, 0.5625F}}));
		CeramicsRecipe.registerRecipe(new CeramicsRecipe(new OreStack("argil", 6), EnumItem.stone_production.instance(4, "argil_plate_unsmelted"), new float[][]{
			{0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F},
			{0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}, {0.125F, 0.25F}}));
//		CeramicsRecipe.registerRecipe(new CeramicsRecipe(ItemFleSub.a("jug_argil_unsmelted"), new float[][]{
//			{0.125F, 0.25F}, {0.9F, 0.8F}, {0.8F, 0.7F}, {0.7F, 0.55F}, {0.6F, 0.4F},
//			{0.125F, 0.25F}, {0.9F, 0.8F}, {0.8F, 0.7F}, {0.7F, 0.55F}, {0.6F, 0.4F}}));
	}
}