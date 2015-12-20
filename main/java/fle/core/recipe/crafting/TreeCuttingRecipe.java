package fle.core.recipe.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import flapi.FleAPI;
import flapi.enums.EnumDamageResource;
import flapi.item.interfaces.ITreeLog;
import flapi.recipe.IPlayerToolCraftingRecipe;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.OreStack;

public class TreeCuttingRecipe implements IPlayerToolCraftingRecipe
{	
	private ItemAbstractStack stack = new OreStack("craftingToolAxe");
	
	public TreeCuttingRecipe()
	{
		
	}

	@Override
	public boolean match(ItemStack input1, ItemStack input2, ItemStack tool)
	{
		return input1 != null || input2 == null ? false : input2.getItem() instanceof ITreeLog ?
				stack.equal(tool) : false;
	}

	@Override
	public ItemStack useTool(EntityPlayer player, ItemStack tool)
	{
		FleAPI.damageItem(player, tool, EnumDamageResource.UseTool, 1F);
		return tool;
	}

	@Override
	public ItemStack getOutput(ItemStack input1, ItemStack input2,
			ItemStack tool)
	{
		return ((ITreeLog) input2.getItem()).getLogDrop(input2);
	}
}