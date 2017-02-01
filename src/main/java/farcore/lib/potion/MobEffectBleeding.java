package farcore.lib.potion;

import nebula.common.mobeffect.MobEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class MobEffectBleeding extends MobEffect
{
	public MobEffectBleeding(String name, int liquidColorIn)
	{
		super(name, true, liquidColorIn);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
		int k = 30 >> amplifier;
		return duration % k == 0;
	}
	
	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier,
			double health)
	{
		if(entityLivingBaseIn.isEntityUndead())
		{
			entityLivingBaseIn.attackEntityFrom(DamageSource.generic, 2.0F);
		}
	}
}