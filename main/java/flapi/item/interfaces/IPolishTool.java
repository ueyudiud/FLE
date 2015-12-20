package flapi.item.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import flapi.enums.EnumCraftingType;
import flapi.recipe.CraftingState;

public interface IPolishTool
{
	ItemStack getOutput(EntityPlayer player, ItemStack input);

	CraftingState getState(ItemStack input, EnumCraftingType aType, CraftingState aState);
}
