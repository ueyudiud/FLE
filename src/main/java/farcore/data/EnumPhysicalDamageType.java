/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.data;

import farcore.lib.skill.ISkill;
import net.minecraftforge.common.util.EnumHelper;

/**
 * For some physical damage.
 * 
 * @author ueyudiud
 */
public enum EnumPhysicalDamageType
{
	PUNCTURE, SMASH, CUT, HIT;
	
	ISkill skill;
	
	public String getTranslation()
	{
		return "damage.type." + name() + ".name";
	}
	
	public void setSkill(ISkill skill)
	{
		this.skill = skill;
	}
	
	public ISkill getSkill()
	{
		return this.skill;
	}
	
	static
	{
		EnumHelper.testEnum(EnumPhysicalDamageType.class, new Class[0]);
	}
	
	public EnumPhysicalDamageType getType(String key)
	{
		return EnumHelper.addEnum(EnumPhysicalDamageType.class, key, new Class[0]);
	}
}
