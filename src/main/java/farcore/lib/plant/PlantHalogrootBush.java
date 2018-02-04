/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.plant;

import farcore.data.M;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

/**
 * @author ueyudiud
 */
public class PlantHalogrootBush extends PlantStatic
{
	public PlantHalogrootBush()
	{
		super(M.halogroot_bush);
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Desert;
	}
}
