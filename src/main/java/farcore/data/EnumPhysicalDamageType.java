/*
 * copyright© 2016 ueyudiud
 */

package farcore.data;

import net.minecraftforge.common.util.EnumHelper;

/**
 * For some physical damage.
 * @author ueyudiud
 */
public enum EnumPhysicalDamageType
{
	PUNCTURE,
	SMASH,
	CUT;

	public String getTranslation()
	{
		return "damage.type." + name() + ".name";
	}

	static { EnumHelper.testEnum(EnumPhysicalDamageType.class, new Class[0]); }

	public EnumPhysicalDamageType getType(String key)
	{
		return EnumHelper.addEnum(EnumPhysicalDamageType.class, key, new Class[0]);
	}
}