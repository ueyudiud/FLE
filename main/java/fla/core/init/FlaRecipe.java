package fla.core.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import fla.api.recipe.FuelStack;
import fla.core.FlaBlocks;
import fla.core.FlaItems;
import fla.core.gui.base.RecipeHelper.FakeCraftingInventory;
import fla.core.item.ItemSub;
import fla.core.recipe.OilLampRecipe;
import fla.core.recipe.TreeCuttingRecipe;

public class FlaRecipe
{
	private static List<FakeCraftingInventory> removeList = new ArrayList();
	
	public static void registerRemovedCrafting(FakeCraftingInventory inv)
	{
		removeList.add(inv);
	}
	
	public static void reloadRecipe()
	{
		registerRemovedCrafting(FakeCraftingInventory.init("xx", "xx", 'x', "plankWood"));
		registerRemovedCrafting(FakeCraftingInventory.init("x", "x", 'x', "plankWood"));
		registerRemovedCrafting(FakeCraftingInventory.init("o", "x", 'o', new ItemStack(Items.coal, 1, 0), 'x', "stickWood"));
		registerRemovedCrafting(FakeCraftingInventory.init("o", "x", 'o', new ItemStack(Items.coal, 1, 1), 'x', "stickWood"));
		
		List list = new ArrayList(CraftingManager.getInstance().getRecipeList());
		for(Object rawTarget : list)
		{
			IRecipe recipe = (IRecipe) rawTarget;
			for(FakeCraftingInventory inv : removeList)
			{
				if(recipe.matches(inv, null))
				{
					CraftingManager.getInstance().getRecipeList().remove(rawTarget);
				}
			}
		}
	}
	
	public static void init()
	{
		OreDictionary.registerOre("branch", ItemSub.a("branch_oak"));
		OreDictionary.registerOre("branch", ItemSub.a("branch_spruce"));
		OreDictionary.registerOre("branch", ItemSub.a("branch_birch"));
		OreDictionary.registerOre("branch", ItemSub.a("branch_jungle"));
		OreDictionary.registerOre("branch", ItemSub.a("branch_acacia"));
		OreDictionary.registerOre("branch", ItemSub.a("branch_darkoak"));
		OreDictionary.registerOre("craftingToolFireStarter", new ItemStack(FlaItems.wooden_firestarter, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolFireStarter", new ItemStack(Items.flint_and_steel, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(Items.wooden_axe, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(Items.stone_axe, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(Items.iron_axe, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(Items.golden_axe, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(Items.diamond_axe, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(FlaItems.rough_flint_axe, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(FlaItems.flint_axe, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(FlaItems.stone_axe, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("wick", new ItemStack(Items.string));
		OreDictionary.registerOre("dustLimestone", ItemSub.a("dust_limestone"));
		OreDictionary.registerOre("oreCuprite", new ItemStack(FlaBlocks.ore1, 1, 0));
		OreDictionary.registerOre("oreCopper", new ItemStack(FlaBlocks.ore1, 1, 1));
		OreDictionary.registerOre("oreMalachite", new ItemStack(FlaBlocks.ore1, 1, 2));
		OreDictionary.registerOre("oreAzurite", new ItemStack(FlaBlocks.ore1, 1, 3));
		OreDictionary.registerOre("oreChalococite", new ItemStack(FlaBlocks.ore1, 1, 4));
		OreDictionary.registerOre("oreTenorite", new ItemStack(FlaBlocks.ore1, 1, 5));
		OreDictionary.registerOre("oreChalcopyrite", new ItemStack(FlaBlocks.ore1, 1, 6));
		OreDictionary.registerOre("oreBornite", new ItemStack(FlaBlocks.ore1, 1, 7));
		OreDictionary.registerOre("oreTetrahedrite", new ItemStack(FlaBlocks.ore1, 1, 8));
		OreDictionary.registerOre("oreCovellite", new ItemStack(FlaBlocks.ore1, 1, 9));
		OreDictionary.registerOre("oreEnargite", new ItemStack(FlaBlocks.ore1, 1, 10));
		GameRegistry.addRecipe(new TreeCuttingRecipe(new ItemStack(Blocks.log, 1, 0), new ItemStack(FlaItems.log, 1, 0)));
		GameRegistry.addRecipe(new TreeCuttingRecipe(new ItemStack(Blocks.log, 1, 1), new ItemStack(FlaItems.log, 1, 1)));
		GameRegistry.addRecipe(new TreeCuttingRecipe(new ItemStack(Blocks.log, 1, 2), new ItemStack(FlaItems.log, 1, 2)));
		GameRegistry.addRecipe(new TreeCuttingRecipe(new ItemStack(Blocks.log, 1, 3), new ItemStack(FlaItems.log, 1, 3)));
		GameRegistry.addRecipe(new TreeCuttingRecipe(new ItemStack(Blocks.log2, 1, 0), new ItemStack(FlaItems.log, 1, 4)));
		GameRegistry.addRecipe(new TreeCuttingRecipe(new ItemStack(Blocks.log2, 1, 1), new ItemStack(FlaItems.log, 1, 5)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaBlocks.firewood), new Object[]{"x", "o", 'x', "craftingToolAxe", 'o', "logWood"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(FlaItems.stone_oil_lamp, 1), new ItemStack(FlaItems.stone_oil_lamp, 1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new OilLampRecipe(new FuelStack(Fuels.lipocere, 400), Items.rotten_flesh));
		GameRegistry.addRecipe(new OilLampRecipe(new FuelStack(Fuels.fat, 400), Items.beef));
		GameRegistry.addRecipe(new OilLampRecipe(new FuelStack(Fuels.fat, 500), Items.porkchop));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.rough_flint_axe), new Object[]{"x", "o", 'x', ItemSub.a("flint_c"), 'o', ItemSub.a("branch_bush")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.flint_axe), new Object[]{"x", "o", 'x', ItemSub.a("flint_axe"), 'o', ItemSub.a("branch_bush")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.flint_shovel), new Object[]{"x", "o", 'x', ItemSub.a("flint_shovel"), 'o', ItemSub.a("branch_bush")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.flint_hammer), new Object[]{"x", "o", 'x', ItemSub.a("flint_hammer"), 'o', ItemSub.a("branch_bush")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.stone_axe), new Object[]{"x", "o", 'x', ItemSub.a("stone_axe"), 'o', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.stone_shovel), new Object[]{"x", "o", 'x', ItemSub.a("stone_shovel"), 'o', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.stone_hammer), new Object[]{"x", "o", 'x', ItemSub.a("stone_hammer"), 'o', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.wooden_hammer), new Object[]{"x", "o", 'x', "logWood", 'o', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaBlocks.polishTable), new Object[]{"x", "o", 'x', "logWood", 'o', Blocks.gravel}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.whetstone), new Object[]{"xx", "xx", 'x', ItemSub.a("stone_b")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FlaItems.wooden_firestarter), new Object[]{" s", "wl", 's', "stickWood", 'w', "logWood", 'l', ItemSub.a("tinder")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemSub.a("ramie_rope", 3), new Object[]{"xx", "xx", 'x', ItemSub.a("ramie_fiber_dry")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemSub.a("ramie_bundle_rope"), new Object[]{"xx", "xx", 'x', ItemSub.a("ramie_rope")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemSub.a("ramie_rope", 4), new Object[]{"x", 'x', ItemSub.a("ramie_bundle_rope")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(FlaBlocks.charcoal, new Object[]{"xx", "xx", 'x', ItemSub.a("charred_log")}));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemSub.a("tinder"), new Object[]{"xx", "xx", 'x', ItemSub.a("leaves_dry")}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemSub.a("argil_ball", 3), new Object[]{Items.clay_ball, Items.clay_ball, Blocks.sand, "dustLimestone"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemSub.a("branch_bush"), "branch"));
	}
}