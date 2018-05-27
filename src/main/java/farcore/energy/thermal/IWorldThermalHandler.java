/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.thermal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Handler to handle default block thermal properties.
 * 
 * @author ueyudiud
 *
 */
public interface IWorldThermalHandler
{
	float getThermalConductivity(World world, BlockPos pos, IBlockState state);
	
	float getHeatCapacity(World world, BlockPos pos, IBlockState state);
	
	float getTemperature(World world, BlockPos pos, float baseTemp);
}
