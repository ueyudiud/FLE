package fle.core.tool;

import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.api.item.IArrowItem;
import fle.api.item.IBagable;
import fle.core.entity.EntityFleArrow;
import fle.core.item.tool.ToolMaterialInfo;

public class BowHandler
{
	@SubscribeEvent
	public void onArrowCheck(ArrowNockEvent evt)
	{
		if(evt.entityPlayer != null)
		{
			if(evt.entityPlayer.capabilities.isCreativeMode) return;
			if(evt.entityPlayer.getCurrentArmor(2) != null)
				if(evt.entityPlayer.getCurrentArmor(2).getItem() instanceof IBagable)
				{
					IBagable bag = (IBagable) evt.entityPlayer.getCurrentArmor(2).getItem();
					int maxSize = bag.getSize(evt.entityPlayer.getCurrentArmor(2));
					for(int i = 0; i < maxSize; ++i)
					{
						ItemStack aStack = bag.getItemContain(evt.entityPlayer.getCurrentArmor(2), i);
						if(aStack != null)
						{
							if(aStack.getItem() instanceof IArrowItem)
							{
								if(((IArrowItem) aStack.getItem()).isShootable(evt.result, aStack))
								{
									evt.entityPlayer.setItemInUse(evt.result, Items.bow.getMaxItemUseDuration(aStack));
									evt.setCanceled(true);
									return;
								}
							}
							else if(aStack.getItem() == Items.arrow)
							{
								evt.entityPlayer.setItemInUse(evt.result, Items.bow.getMaxItemUseDuration(aStack));
								evt.setCanceled(true);
								return;
							}
						}
					}
				}
		}
	}
	
	@SubscribeEvent
	public void onShoot(ArrowLooseEvent evt)
	{
		if(evt.entityPlayer != null)
		{
			if(evt.entityPlayer.capabilities.isCreativeMode) return;
			if(evt.entityPlayer.getCurrentArmor(2) != null)
				if(evt.entityPlayer.getCurrentArmor(2).getItem() instanceof IBagable)
				{
					IBagable bag = (IBagable) evt.entityPlayer.getCurrentArmor(2).getItem();
					int maxSize = bag.getSize(evt.entityPlayer.getCurrentArmor(2));
					for(int i = 0; i < maxSize; ++i)
					{
						ItemStack aStack = bag.getItemContain(evt.entityPlayer.getCurrentArmor(2), i);
						if(aStack != null)
						{
							if(aStack.getItem() instanceof IArrowItem)
							{
								if(((IArrowItem) aStack.getItem()).isShootable(evt.bow, aStack))
								{
									if(!evt.entity.worldObj.isRemote)
									{
										ToolMaterialInfo info = new ToolMaterialInfo(aStack.stackTagCompound);
										EntityArrow entity = new EntityArrow(evt.entityPlayer.worldObj, evt.entityPlayer, Math.min((float) evt.charge / 12F * info.getMaterialBase().getPropertyInfo().getDenseness(), 6F));
										entity.setKnockbackStrength((int) info.getMaterialBase().getPropertyInfo().getDenseness());
										entity.canBePickedUp = 0;
										entity.setDamage(Math.log(info.getHardness() * 30D));
										evt.entityPlayer.worldObj.spawnEntityInWorld(entity);
									}
									--aStack.stackSize;
									if(aStack.stackSize == 0) aStack = null;
									bag.setItemContain(evt.entityPlayer.getCurrentArmor(2), i, aStack);
									evt.setCanceled(true);
									break;
								}
							}
							else if(aStack.getItem() == Items.arrow)
							{
								if(!evt.entity.worldObj.isRemote)
								{
									EntityArrow entity = new EntityArrow(evt.entityPlayer.worldObj, evt.entityPlayer, Math.min((float) evt.charge / 12F, 6F));
									evt.entityPlayer.worldObj.spawnEntityInWorld(entity);
								}
								--aStack.stackSize;
								if(aStack.stackSize == 0) aStack = null;
								bag.setItemContain(evt.entityPlayer.getCurrentArmor(2), i, aStack);
								evt.setCanceled(true);
								break;
							}
						}
					}
				}
			evt.setCanceled(true);
		}
	}
}
