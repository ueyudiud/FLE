package farcore.energy.thermal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWorldThermalConductivityHandler
{
	float getThermalConductivity(World world, BlockPos pos, IBlockState state);
}