package fle.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.api.util.DamageResources;
import fle.api.util.WeightHelper;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemTool;
import fle.core.util.FlePotionEffect;

public class EntityHandler
{
	private final WeightHelper<ItemStack> zombieHandle = new WeightHelper<ItemStack>(
			ItemTool.a("metal_axe", Materials.CuSn, 47),
			ItemTool.a("metal_axe", Materials.CuAs, 44),
			ItemTool.a("metal_axe", Materials.Copper, 43));
	
	@SubscribeEvent
	public void onEntitySpawn(LivingSpawnEvent evt)
	{
		if(evt.entityLiving instanceof EntityZombie)
		{
			if(evt.entityLiving.getRNG().nextInt(4) == 0)
				evt.entityLiving.setCurrentItemOrArmor(0, zombieHandle.randomGet());
		}
	}
	
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent evt)
	{
		if(evt.entityLiving instanceof EntitySheep)
		{
			
		}
		else if(evt.entityLiving instanceof EntityZombie)
		{
			List<EntityItem> d1 = new ArrayList(evt.drops);
			for(EntityItem drop : d1)
			{
				if(drop.getEntityItem().getItem() == Items.iron_ingot)
				{
					evt.drops.remove(drop);
				}
			}
		}
		else if(evt.entityLiving instanceof EntityCow)
		{
			List<EntityItem> d1 = new ArrayList(evt.drops);
			for(EntityItem drop : d1)
			{
				if(drop.getEntityItem().getItem() == Items.leather)
				{
					evt.drops.remove(drop);
				}
			}
			evt.drops.add(new EntityItem(evt.entityLiving.worldObj, evt.entityLiving.posX, evt.entityLiving.posY, evt.entityLiving.posZ, new ItemStack(Items.bone, evt.entityLiving.getRNG().nextInt(2) + 1)));
		}
		else if(evt.entityLiving instanceof EntitySheep)
		{
			evt.drops.add(new EntityItem(evt.entityLiving.worldObj, evt.entityLiving.posX, evt.entityLiving.posY, evt.entityLiving.posZ, new ItemStack(Items.bone, evt.entityLiving.getRNG().nextInt(2) + 1)));
		}
		else if(evt.entityLiving instanceof EntityPig)
		{
			evt.drops.add(new EntityItem(evt.entityLiving.worldObj, evt.entityLiving.posX, evt.entityLiving.posY, evt.entityLiving.posZ, new ItemStack(Items.bone, evt.entityLiving.getRNG().nextInt(2) + 1)));
		}
		else if(evt.entityLiving instanceof EntitySpider)
		{
			evt.drops.clear();
			evt.drops.add(new EntityItem(evt.entityLiving.worldObj, evt.entityLiving.posX, evt.entityLiving.posY, evt.entityLiving.posZ, ItemFleSub.a("spinneret", evt.entityLiving.getRNG().nextInt(1) + 1)));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onAttack(LivingAttackEvent evt)
	{
		if(evt.source instanceof EntityDamageSource)
		{
			if(evt.entityLiving instanceof EntityPlayer || evt.entityLiving instanceof IAnimals)
			{
				int level;
				if(evt.ammount > 8.0F) level = 2;
				else if(evt.ammount > 3.0F) level = 1;
				else level = 0;
				evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.bleeding.id, 800, level));
			}
		}
		else if(evt.source == DamageSource.fall)
		{
			if(evt.ammount > 6.0F) evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.fracture.id, 5000, 2));
			else if(evt.ammount > 2.0F) evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.fracture.id, 5000, 1));
			else evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.fracture.id, 5000, 0));
		}
		else if(evt.source == DamageSource.onFire || evt.source instanceof DamageResources.FleHeatDamageSource)
		{
			if(evt.ammount > 4.0F) evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.burn.id, 1000, 2));
			else if(evt.ammount > 2.0F) evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.burn.id, 1000, 1));
			else evt.entityLiving.addPotionEffect(new PotionEffect(FlePotionEffect.burn.id, 1000, 0));
		}
	}
}