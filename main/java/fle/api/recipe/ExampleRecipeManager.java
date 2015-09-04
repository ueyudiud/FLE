package fle.api.recipe;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import fle.api.recipe.AbstractRecipe.GetRecipeMap;
import fle.api.recipe.AbstractRecipe.GetRecipeName;
import fle.api.recipe.AbstractRecipe.OnInput;
import fle.api.recipe.AbstractRecipe.OnOutput;

@AbstractRecipe(recipeName = "crafting")
public class ExampleRecipeManager
{
	@GetRecipeName
	public static ItemStack match(InventoryCrafting inv, World world)
	{
		return CraftingManager.getInstance().findMatchingRecipe(inv, world);
	}
	
	@OnInput
	@OnOutput
	public static void onOutput(EntityPlayer player, ItemStack output, InventoryCrafting inv)
	{
		ItemCraftedEvent evt = new ItemCraftedEvent(player, output, inv);
		MinecraftForge.EVENT_BUS.post(evt);
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			inv.decrStackSize(i, 1);
		}
	}
	
	@GetRecipeMap
	public static List getRecipeList()
	{
		return CraftingManager.getInstance().getRecipeList();
	}
}