package farcore.interfaces;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public interface ISmartPlantableBlock extends IPlantable
{
	@Deprecated
	EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z);
	
	boolean useDefaultType();
	
    String getSmartPlantType(IBlockAccess world, int x, int y, int z);
}
