package fle.api.potion;

import farcore.lib.potion.PotionBase;
import fle.api.damage.DamageSources;
import net.minecraft.entity.EntityLivingBase;

public class PotionBleeding extends PotionBase
{
	public PotionBleeding(String name, int color)
	{
		super(name, true, color);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int level)
	{
		entity.attackEntityFrom(DamageSources.getBleedingSource(), 1F);
	}
	
	@Override
	public boolean isReady(int tick, int level)
	{
		return tick % (96 / (level + 1)) == 0;
	}
}