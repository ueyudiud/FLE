package farcore.interfaces;

import farcore.enums.Direction;
import farcore.enums.EnumBlock;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import net.minecraftforge.fluids.BlockFluidBase;

public interface ISmartSoildBlock
{
	boolean canSustainPlant(IBlockAccess world, int x, int y, int z, Direction direction, ISmartPlantableBlock block);
}