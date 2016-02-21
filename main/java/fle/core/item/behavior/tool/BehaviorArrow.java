package fle.core.item.behavior.tool;

import flapi.item.interfaces.IArrowItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class BehaviorArrow extends BehaviorTool implements IArrowItem
{
	@Override
	public boolean isShootable(ItemStack aTool, ItemStack aStack)
	{
		return true;
	}

	@Override
	public Entity onShoot(EntityLivingBase aEntity)
	{
		return null;
	}
}