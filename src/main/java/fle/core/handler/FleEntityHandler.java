package fle.core.handler;

import farcore.data.Potions;
import fle.core.entity.monster.EntityFLECreeper;
import fle.core.entity.monster.EntityFLESkeleton;
import fle.core.entity.monster.EntityFLESpider;
import fle.core.entity.monster.EntityFLEZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FleEntityHandler
{
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event)
	{
		Class<? extends Entity> clazz = event.getEntity().getClass();
		if (clazz == EntityZombie.class)
		{
			EntityFLEZombie replace = new EntityFLEZombie(event.getWorld());
			replaceEntitySpawn(event.getWorld(), replace, event.getEntity());
			replace.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0);
			replace.setHealth(200.0F);
			replace.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0);
			replace.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24);
			replace.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
			replace.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0);
			event.setCanceled(true);
		}
		else if(clazz == EntitySkeleton.class)
		{
			EntityFLESkeleton replace = new EntityFLESkeleton(event.getWorld());
			replaceEntitySpawn(event.getWorld(), replace, event.getEntity());
			replace.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0);
			replace.setHealth(200.0F);
			replace.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0);
			replace.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
			replace.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
			replace.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0);
			event.setCanceled(true);
		}
		else if(clazz == EntitySpider.class)
		{
			EntityFLESpider replace = new EntityFLESpider(event.getWorld());
			replaceEntitySpawn(event.getWorld(), replace, event.getEntity());
			replace.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(160.0);
			replace.setHealth(160.0F);
			replace.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0);
			replace.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
			replace.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0);
			event.setCanceled(true);
		}
		else if(clazz == EntityCreeper.class)
		{
			EntityFLECreeper replace = new EntityFLECreeper(event.getWorld());
			replaceEntitySpawn(event.getWorld(), replace, event.getEntity());
			replace.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0);
			replace.setHealth(200.0F);
			replace.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0);
			replace.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
			replace.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0);
			event.setCanceled(true);
		}
	}
	
	private <E extends Entity> E replaceEntitySpawn(World world, E replace, Entity source)
	{
		replace.readFromNBT(source.writeToNBT(new NBTTagCompound()));
		world.spawnEntity(replace);
		source.setDead();
		return replace;
	}
	
	@SubscribeEvent
	public void onEntityAttack(LivingAttackEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		float dif = entity.world.getDifficultyForLocation(event.getEntityLiving().getPosition()).getAdditionalDifficulty();
		if (event.getSource() instanceof EntityDamageSource)
		{
			EntityDamageSource source = (EntityDamageSource) event.getSource();
			if (source.getSourceOfDamage() instanceof EntityZombie)
			{
				float hardness = dif * event.getEntityLiving().getRNG().nextFloat() + 1.5F;
				if(dif != 0.0F)
				{
					entity.addPotionEffect(new PotionEffect(Potions.INFECT_ZV, (int) hardness * 800, (int) (dif - 1F)));
					entity.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, (int) hardness * 800, 1));
				}
			}
			else if (source.getSourceOfDamage() instanceof EntityCreeper)
			{
				entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, (int) ((1F + event.getEntityLiving().getRNG().nextFloat() * dif) * (event.getAmount() + 2) * 500)));
			}
		}
		else if(event.getSource().isProjectile() && event.getAmount() > 2.0F)
		{
			entity.addPotionEffect(new PotionEffect(Potions.BLEED, (int) ((event.getAmount() - 2.0F) * (80 + dif * 20F))));
		}
	}
	
	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		if(entity.isPotionActive(Potions.JUMP_REDUCE))
		{
			if(entity.motionY > 0)
			{
				entity.motionY *= 1F - 0.125F * (entity.getActivePotionEffect(Potions.JUMP_REDUCE).getAmplifier() + 1);
			}
		}
		if(entity.isPotionActive(Potions.FRACTURE))
		{
			if(entity.motionY > 0)
			{
				entity.motionY *= 1F - 0.125F * (entity.getActivePotionEffect(Potions.FRACTURE).getAmplifier() + 1);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		float distance = event.getDistance();
		float damage;
		if(distance < 2F)
		{
			damage = 0F;
		}
		else if(distance < 6F)
		{
			damage = distance / 3F;
		}
		else
		{
			damage = distance / 3F + (distance - 6F) * distance / 8F;
		}
		event.setDamageMultiplier(damage);
		float tick = distance + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat()) * distance - 4;
		if(tick > 0)
		{
			entity.addPotionEffect(new PotionEffect(Potions.JUMP_REDUCE, (int) (tick * 1200), distance < 8 ? 0 : distance < 15 ? 1 : 2));
			entity.addPotionEffect(new PotionEffect(Potions.FRACTURE   , (int) (tick * 1200), distance < 8 ? 0 : distance < 15 ? 1 : 2));
		}
	}
}