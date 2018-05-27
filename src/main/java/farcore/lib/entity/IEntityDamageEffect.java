/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.entity;

import farcore.data.EnumPhysicalDamageType;

/**
 * @author ueyudiud
 */
public interface IEntityDamageEffect
{
	default float getDamageMultiplier(EnumPhysicalDamageType type)
	{
		return 1.0F;
	}
}
