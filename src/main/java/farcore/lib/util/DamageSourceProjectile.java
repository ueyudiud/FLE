package farcore.lib.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class DamageSourceProjectile extends DamageSource
{
	public static final DamageSource instance = new DamageSourceProjectile();

	public DamageSourceProjectile()
	{
		super("farcore.projectile");
	}

	@Override
	public ITextComponent getDeathMessage(EntityLivingBase entity)
	{
		return new TextComponentTranslation("damagesource.projectile", entity.getDisplayName());
	}
}