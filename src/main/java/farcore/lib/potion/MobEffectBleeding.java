package farcore.lib.potion;

import nebula.common.mobeffect.MobEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class MobEffectBleeding extends MobEffect
{
	protected int duration;
	protected float amount;
	
	public MobEffectBleeding(String name, int liquidColorIn, int duration, float amount)
	{
		super(name, true, liquidColorIn);
		this.duration = duration;
		this.amount = amount;
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
		int k = duration >> amplifier;
		return duration % k == 0;
	}
	
	@Override
	public void affectEntity(Entity source, Entity indirectSource,
			EntityLivingBase entityLivingBaseIn, int amplifier, double health)
	{
		if(entityLivingBaseIn.isEntityUndead())
		{
			entityLivingBaseIn.attackEntityFrom(DamageSource.generic, this.amount);
		}
	}
}