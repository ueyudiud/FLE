package fle.tool.item.behavior;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import flapi.enums.EnumCraftingType;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;
import flapi.item.interfaces.ISubPolishTool;
import flapi.recipe.CraftingState;

public class BehaviorWoodHammer extends BehaviorTool implements ISubPolishTool<ItemFleMetaBase>
{
	@Override
	public boolean onLeftClickEntity(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, Entity entity) 
	{
		item.damageItem(itemstack, player, EnumDamageResource.HitEntity, 1.25F);
		return super.onLeftClickEntity(item, itemstack, player, entity);
	}
	
	@Override
	public ItemStack getOutput(ItemFleMetaBase item, ItemStack aStack, EntityPlayer aPlayer) 
	{
		ItemStack ret = aStack.copy();
		item.damageItem(ret, aPlayer, EnumDamageResource.Crafting, 1);
		return ret;
	}

	@Override
	public CraftingState getState(ItemFleMetaBase item, ItemStack aStack,
			EnumCraftingType aType, CraftingState aState) 
	{
		return aState == CraftingState.DEFAULT ? CraftingState.CRUSH : aState;
	}
}