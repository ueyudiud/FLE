package fle.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.enums.EnumItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class EntityHandler
{
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event)
	{
		World world = event.entityLiving.worldObj;
		double x = event.entityLiving.posX;
		double y = event.entityLiving.posY;
		double z = event.entityLiving.posZ;
		if(event.entityLiving instanceof EntitySheep)
		{
			event.drops.clear();
			event.drops.add(new EntityItem(world, x, y, z, EnumItem.food_divide_smeltable.instance(1, "lamb_raw", 12F + event.entityLiving.getRNG().nextFloat() * 8F)));
		}
		else if(event.entityLiving instanceof EntityCow)
		{
			event.drops.clear();
			event.drops.add(new EntityItem(world, x, y, z, EnumItem.food_divide_smeltable.instance(1, "beef_raw", 12F + event.entityLiving.getRNG().nextFloat() * 8F)));
		}
		else if(event.entityLiving instanceof EntityChicken)
		{
			event.drops.clear();
			event.drops.add(new EntityItem(world, x, y, z, EnumItem.food_divide_smeltable.instance(1, "chicken_raw", 5F + event.entityLiving.getRNG().nextFloat() * 4F)));
			int i = event.entityLiving.getRNG().nextInt(3);
			if(i > 0)
			{
				event.drops.add(new EntityItem(world, x, y, z, new ItemStack(Items.feather, i)));
			}
		}
		else if(event.entityLiving instanceof EntityPig)
		{
			event.drops.clear();
			event.drops.add(new EntityItem(world, x, y, z, EnumItem.food_divide_smeltable.instance(1, "pork_raw", 12F + event.entityLiving.getRNG().nextFloat() * 8F)));
		}
		else if(event.entityLiving instanceof EntitySquid)
		{
			event.drops.clear();
			event.drops.add(new EntityItem(world, x, y, z, EnumItem.food_divide_smeltable.instance(1, "squid_raw", 7F + event.entityLiving.getRNG().nextFloat() * 5F)));
		}
	}
}