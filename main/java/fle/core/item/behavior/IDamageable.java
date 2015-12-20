package fle.core.item.behavior;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import flapi.enums.EnumDamageResource;

public interface IDamageable
{
	void damageItem(ItemStack stack, EntityLivingBase user,
			EnumDamageResource reource, float damage);
}