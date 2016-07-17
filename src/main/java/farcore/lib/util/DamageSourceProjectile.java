package farcore.lib.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class DamageSourceProjectile extends DamageSource
{
	public static final DamageSource instance = new DamageSourceProjectile();

	public DamageSourceProjectile()
	{
		super("farcore.projectile");
	}

	@Override
	public IChatComponent func_151519_b(EntityLivingBase entity)
	{
		return new ChatComponentTranslation("damagesource.projectile", entity.func_145748_c_());
	}
}