/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class EntityFLESlime extends EntitySlime
{
	public EntityFLESlime(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected void dealDamage(EntityLivingBase entityIn)
	{
		int i = getSlimeSize();
		
		if (canEntityBeSeen(entityIn) && getDistanceSqToEntity(entityIn) < 0.6 * 0.6 * i * i && entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 1.5F * getAttackStrength()))
		{
			playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			applyEnchantments(this, entityIn);
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (super.attackEntityFrom(source, amount))
		{
			if (!source.isMagicDamage() && !source.isFireDamage() && !source.isCreativePlayer() && !source.isDamageAbsolute() && !source.isProjectile())
			{
				causeDebufToAttacked(source.getEntity(), amount);
			}
			return true;
		}
		return false;
	}
	
	protected void causeDebufToAttacked(Entity entity, float amount)
	{
		if (entity instanceof EntityLivingBase)
		{
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100));
		}
	}
}
