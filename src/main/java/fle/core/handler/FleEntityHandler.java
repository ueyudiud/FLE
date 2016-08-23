package fle.core.handler;

import farcore.data.Potions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FleEntityHandler
{
	public static final byte i = 3;
	
	@SubscribeEvent
	public void onEntityAttack(LivingAttackEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		if(event.getSource() instanceof EntityDamageSource)
		{
			EntityDamageSource source = (EntityDamageSource) event.getSource();
			if(source.getSourceOfDamage() instanceof EntityZombie)
			{
				float dif = entity.worldObj.getDifficultyForLocation(event.getEntityLiving().getPosition()).getAdditionalDifficulty();
				float hardness = dif * event.getEntityLiving().getRNG().nextFloat() + 1.5F;
				if(dif != 0.0F)
				{
					entity.addPotionEffect(new PotionEffect(Potions.INFECT_ZV, (int) hardness * 800, (int) (dif - 1F)));
					entity.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, (int) hardness * 800, 1));
				}
			}
		}
		else if(event.getSource().isProjectile())
		{
			entity.addPotionEffect(new PotionEffect(Potions.BLEED, (int) (event.getAmount() * 100)));
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
		if(distance < 3F)
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
		int tick = (int) (distance + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat()) * distance - 4);
		if(tick > 0)
		{
			entity.addPotionEffect(new PotionEffect(Potions.JUMP_REDUCE, tick * 1200, distance < 8 ? 0 : distance < 15 ? 1 : 2));
			entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, tick * 1200, distance < 8 ? 0 : distance < 15 ? 1 : 2));
		}
	}
}