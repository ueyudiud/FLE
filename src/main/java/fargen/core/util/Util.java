package fargen.core.util;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.EnumHelper;

public class Util
{
	private static Class[] dimensionType = new Class[]
			{int.class, String.class, String.class, Class.class};
	
	public static DimensionType newDimensionType(String enumName, int id, String name, String suffix, Class <? extends WorldProvider> clazz)
	{
		return EnumHelper.addEnum(DimensionType.class, enumName, dimensionType, id, name, suffix, clazz);
	}
}
