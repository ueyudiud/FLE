/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;

public class DamageSourceEntityAttack extends EntityDamageSource
{
	private ITextComponent message;
	
	public DamageSourceEntityAttack(String damageTypeIn, ITextComponent message, Entity damageSourceEntityIn)
	{
		super(damageTypeIn, damageSourceEntityIn);
		this.message = message;
	}
	
	@Override
	public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
	{
		return this.message != null ? this.message : super.getDeathMessage(entityLivingBaseIn);
	}
}
