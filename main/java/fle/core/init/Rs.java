package fle.core.init;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import cpw.mods.fml.common.registry.GameRegistry;
import fle.FLE;
import fle.api.recipe.ShapedFleRecipe;
import fle.api.recipe.ShapelessFleRecipe;
import fle.core.item.ItemFleSub;
import fle.core.recipe.RecipeHelper.FakeCraftingInventory;
import fle.core.recipe.crafting.StoneToolCraftingRecipe;
import fle.core.recipe.crafting.TreeCuttingRecipe;

public class Rs
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
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 1));
		OreDictionary.registerOre("craftingToolAxe", new ItemStack(IB.tool, 1, 2));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_oak"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_spruce"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_birch"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_jungle"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_acacia"));
		OreDictionary.registerOre("branchWood", ItemFleSub.a("branch_darkoak"));
		OreDictionary.registerOre("logFLE", new ItemStack(IB.treeLog, 1, OreDictionary.WILDCARD_VALUE));

		GameRegistry.addRecipe(new ShapelessFleRecipe(ItemFleSub.a("branch_bush"), new Object[]{"branchWood"}));
		GameRegistry.addRecipe(new StoneToolCraftingRecipe("rough_stone_axe", ItemFleSub.a("branch_bush")));
		GameRegistry.addRecipe(new StoneToolCraftingRecipe("stone_axe", "stickWood"));
		GameRegistry.addRecipe(new StoneToolCraftingRecipe("stone_shovel", "stickWood"));
		GameRegistry.addRecipe(new StoneToolCraftingRecipe("stone_hammer", "stickWood"));
		GameRegistry.addRecipe(new TreeCuttingRecipe());

		RecipeSorter.register(FLE.MODID + ":shaped", ShapedFleRecipe.class, SHAPED, "after:forge:shapedore before:minecraft:shapeless");
		RecipeSorter.register(FLE.MODID + ":stonetoolcrafting", StoneToolCraftingRecipe.class, SHAPED, "after:" + FLE.MODID + ":stonetoolcrafting");
		RecipeSorter.register(FLE.MODID + ":stonetoolcrafting", TreeCuttingRecipe.class, SHAPED, "after:" + FLE.MODID + ":stonetoolcrafting");
		RecipeSorter.register(FLE.MODID + ":shapeless", ShapelessFleRecipe.class, SHAPELESS, "after:forge:shapelessore");
	}
}