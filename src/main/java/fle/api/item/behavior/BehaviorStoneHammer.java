package fle.api.item.behavior;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import farcore.enums.EnumToolType;
import farcore.interfaces.item.IItemInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class BehaviorStoneHammer extends BehaviorDigableTool
{
	private static final Set<String> toolClasses = ImmutableSet.of(EnumToolType.hammer_digable.name());
	
	public BehaviorStoneHammer()
	{
		destroyBlockDamageBase = 0.8F;
		destroyBlockDamageHardnessMul = 0.1F;
		hitEntityDamage = 3;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(entity instanceof EntityLivingBase)
		{
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 1));
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 40));
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
	
	@Override
	public void addAttributeModifiers(Multimap map, ItemStack stack)
	{
		map.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(itemDamageUUID, "Tool modifier", 1.0F, 0));
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return toolClasses;
	}
}