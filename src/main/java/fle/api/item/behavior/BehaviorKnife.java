package fle.api.item.behavior;

import com.google.common.collect.Multimap;

import farcore.enums.EnumDamageResource;
import farcore.util.U;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BehaviorKnife extends BehaviorDigableTool
{
	public BehaviorKnife()
	{
		this.craftingDamage = 0.125F;
		this.hitEntityDamage = 0.25F;
	}
	
	@Override
	public void addAttributeModifiers(Multimap map, ItemStack stack)
	{
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemDamageUUID, "Tool modifier", 2.0F, 0));
	}
}