/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items.tool;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumPhysicalDamageType;
import farcore.data.EnumToolTypes;
import nebula.common.tool.EnumToolType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author ueyudiud
 */
public class ToolBiface extends Tool
{
	public ToolBiface()
	{
		super(EnumToolTypes.BIFACE);
		this.damagePerAttack = 3.0F;
		this.damagePerBreak = 1.5F;
		this.damageVsEntity = 2.0F;
		this.durabilityMultiplier = 2.0F;
		this.speedMultiplier = 0.4F;
	}
	
	@Override
	public boolean isWeapon()
	{
		return true;
	}
	
	@Override
	public EnumPhysicalDamageType getPhysicalDamageType()
	{
		return EnumPhysicalDamageType.PUNCTURE;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(S) has two face because of (M).";
	}
	
	@Override
	public List<EnumToolType> getAllowedToolTypes()
	{
		return ImmutableList.of(EnumToolTypes.BIFACE, EnumToolTypes.ADZ, EnumToolTypes.SICKLE);
	}
}
