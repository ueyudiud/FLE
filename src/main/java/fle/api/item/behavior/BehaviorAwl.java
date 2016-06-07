package fle.api.item.behavior;

import farcore.enums.EnumDamageResource;
import farcore.util.U;
import fle.load.Potions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class BehaviorAwl extends BehaviorBase
{
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		U.Inventorys.damage(stack, player, .75F, EnumDamageResource.HIT);
		if(entity instanceof EntityLivingBase)
		{
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potions.bleeding.id, 10, 2));
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
}