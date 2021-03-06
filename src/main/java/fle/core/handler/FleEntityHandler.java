/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.handler;

import java.lang.reflect.Method;

import farcore.data.Potions;
import fle.core.entity.misc.EntityAttributeTag;
import fle.core.entity.monster.EntityFLECreeper;
import fle.core.entity.monster.EntityFLESkeleton;
import fle.core.entity.monster.EntityFLESlime;
import fle.core.entity.monster.EntityFLESpider;
import fle.core.entity.monster.EntityFLEZombie;
import fle.loader.Configs;
import nebula.base.R;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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
			setAttributes(replace, Configs.zombie);
			event.setCanceled(true);
		}
		else if (clazz == EntitySkeleton.class)
		{
			EntityFLESkeleton replace = new EntityFLESkeleton(event.getWorld());
			replaceEntitySpawn(event.getWorld(), replace, event.getEntity());
			setAttributes(replace, Configs.skeleton);
			event.setCanceled(true);
		}
		else if (clazz == EntitySpider.class)
		{
			EntityFLESpider replace = new EntityFLESpider(event.getWorld());
			replaceEntitySpawn(event.getWorld(), replace, event.getEntity());
			setAttributes(replace, Configs.spider);
			event.setCanceled(true);
		}
		else if (clazz == EntityCreeper.class)
		{
			EntityFLECreeper replace = new EntityFLECreeper(event.getWorld());
			replaceEntitySpawn(event.getWorld(), replace, event.getEntity());
			setAttributes(replace, Configs.creeper);
			event.setCanceled(true);
		}
		else if (clazz == EntitySlime.class)
		{
			EntityFLESlime replace = new EntityFLESlime(event.getWorld());
			replaceEntitySpawn(event.getWorld(), replace, event.getEntity());
			event.setCanceled(true);
		}
	}
	
	private void setAttributes(EntityMob entity, EntityAttributeTag tag)
	{
		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(tag.maxHealth);
		entity.setHealth(tag.maxHealth);
		entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(tag.followRange);
		entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(tag.movementSpeed);
		entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(tag.attackDamage);
		entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(tag.armor);
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
				if (dif != 0.0F)
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
		else if (event.getSource().isProjectile() && event.getAmount() > 2.0F)
		{
			entity.addPotionEffect(new PotionEffect(Potions.BLEED, (int) ((event.getAmount() - 2.0F) * (80 + dif * 20F))));
		}
	}
	
	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		if (entity.isPotionActive(Potions.JUMP_REDUCE))
		{
			if (entity.motionY > 0)
			{
				entity.motionY *= 1F - 0.125F * (entity.getActivePotionEffect(Potions.JUMP_REDUCE).getAmplifier() + 1);
			}
		}
		if (entity.isPotionActive(Potions.FRACTURE))
		{
			if (entity.motionY > 0)
			{
				entity.motionY *= 1F - 0.125F * (entity.getActivePotionEffect(Potions.FRACTURE).getAmplifier() + 1);
			}
		}
	}
	
	private static Method getFallSound;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityFall(LivingFallEvent event)
	{
		if (event.getEntityLiving().world.isRemote) return;
		EntityLivingBase entity = event.getEntityLiving();
		if (!(entity instanceof EntityPlayer) && !(entity instanceof EntityAnimal) || (entity instanceof EntityChicken)) return;
		float a = event.getDistance();// For motion is distance calculation.
		a *= event.getDamageMultiplier();
		PotionEffect effect = entity.getActivePotionEffect(MobEffects.JUMP_BOOST);
		a -= effect == null ? 2.5F : (effect.getAmplifier() + 3.5F);
		float damage;
		if (a > 0)
		{
			if (a < 4F)
			{
				damage = a;
			}
			else
			{
				damage = a + (a - 4F) * a / 4F;
			}
			entity.playSound(R.invokeMethod(getFallSound, entity, (int) damage), 1.0F, 1.0F);
			if (damage > 0)
			{
				entity.attackEntityFrom(DamageSource.fall, damage);
				BlockPos pos = new BlockPos(entity.posX, entity.posY - .2, entity.posZ);
				IBlockState state = entity.world.getBlockState(pos);
				
				if (state.getMaterial() != Material.AIR)
				{
					SoundType soundtype = state.getBlock().getSoundType();
					entity.playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
				}
				float tick = a + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat()) * a - 4;
				if (tick > 0)
				{
					entity.addPotionEffect(new PotionEffect(Potions.JUMP_REDUCE, (int) (tick * 1200), a < 8 ? 0 : a < 15 ? 1 : 2));
					entity.addPotionEffect(new PotionEffect(Potions.FRACTURE, (int) (tick * 1200), a < 8 ? 0 : a < 15 ? 1 : 2));
				}
			}
		}
		event.setCanceled(true);
	}
	
	{
		getFallSound = R.getMethod(EntityLivingBase.class, "getFallSound", "func_184588_d", int.class);
	}
}
