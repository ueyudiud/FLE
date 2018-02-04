/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.items.tool;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumPhysicalDamageType;
import farcore.data.EnumToolTypes;
import nebula.common.tool.EnumToolType;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ToolChisel extends Tool
{
	public ToolChisel()
	{
		super(EnumToolTypes.CHISEL);
		this.damagePerAttack = 4.0F;
		this.damagePerBreak = 2.5F;
		this.durabilityMultiplier = 2.0F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) chiseled by (S)";
	}
	
	@Override
	public EnumPhysicalDamageType getPhysicalDamageType()
	{
		return EnumPhysicalDamageType.PUNCTURE;
	}
	
	@Override
	public List<EnumToolType> getAllowedToolTypes()
	{
		return ImmutableList.of(EnumToolTypes.CHISEL, EnumToolTypes.CHISEL_CARVE, EnumToolTypes.CHISEL_POLISH);
	}
	
	@Override
	public List<EnumToolType> getToolTypes(ItemStack stack)
	{
		EnumToolType type1;
		switch (NBTs.getIntOrDefault(ItemStacks.getOrSetupNBT(stack, false), "mode", 0))
		{
		case 0:
			type1 = EnumToolTypes.CHISEL_CARVE;
			break;
		default:
			type1 = EnumToolTypes.CHISEL_POLISH;
			break;
		}
		return ImmutableList.of(EnumToolTypes.CHISEL, type1);
	}
}
