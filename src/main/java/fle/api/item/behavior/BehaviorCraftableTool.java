package fle.api.item.behavior;

import farcore.enums.EnumDamageResource;
import farcore.interfaces.item.ICustomDamageBehavior;
import farcore.lib.recipe.ICraftingInventory;
import farcore.util.U;
import net.minecraft.item.ItemStack;

public class BehaviorCraftableTool extends BehaviorBase implements ICustomDamageBehavior
{
	private float craftingDamage;
	
	public BehaviorCraftableTool(float craftingDamage)
	{
		this.craftingDamage = craftingDamage;
	}

	@Override
	public ItemStack getCraftedItem(ItemStack stack, ICraftingInventory crafting)
	{
		U.Inventorys.damage(stack, null, craftingDamage, EnumDamageResource.USE, false);
		return U.Inventorys.valid(stack);
	}
}