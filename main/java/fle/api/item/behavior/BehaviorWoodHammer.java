package fle.api.item.behavior;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import farcore.enums.EnumToolType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BehaviorWoodHammer extends BehaviorDigableTool
{
	public BehaviorWoodHammer()
	{
		destroyBlockDamageBase = 2F;
		hitEntityDamage = 4;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(entity instanceof EntityLivingBase)
		{
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 50));
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
	
	@Override
	public void addAttributeModifiers(Multimap map, ItemStack stack)
	{
		map.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(itemDamageUUID, "Tool modifier", 1.0F, 0));
	}
}