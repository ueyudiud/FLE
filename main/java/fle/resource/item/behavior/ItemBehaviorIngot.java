package fle.resource.item.behavior;

import flapi.item.ItemFleMetaBase;
import flapi.item.behavior.ItemBehaviorStandard;
import fle.resource.lib.infomation.IngotInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBehaviorIngot extends ItemBehaviorStandard<ItemFleMetaBase>
{
	final IngotInfo info;
	
	public ItemBehaviorIngot(IngotInfo info)
	{
		this.info = info;
	}

	@Override
	public boolean onEntityItemUpdate(ItemFleMetaBase item, EntityItem entity)
	{
		info.onEntityUpdate(entity);
		return false;
	}
	
	@Override
	public void onUpdate(ItemFleMetaBase item, ItemStack stack, World world, Entity entity, int tick, boolean flag)
	{
		info.onItemUpdate(stack, world, (int) entity.posX, (int) entity.posY, (int) entity.posZ);
	}
}