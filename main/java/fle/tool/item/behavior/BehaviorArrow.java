package fle.tool.item.behavior;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import flapi.item.interfaces.IArrowItem;

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