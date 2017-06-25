package farcore.instances;

import farcore.energy.thermal.IWorldThermalHandler;
import nebula.common.block.BlockStandardFluid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidRegistry;

public class TemperatureHandler implements IWorldThermalHandler
{
	@Override
	public float getThermalConductivity(World world, BlockPos pos, IBlockState state)
	{
		if(state.getBlock() instanceof BlockStandardFluid)
			return ((BlockStandardFluid) state.getBlock()).fluid.thermalConductivity;
		Material material = state.getMaterial();
		if(material == Material.IRON) return 4.5E-1F;
		if(material == Material.ANVIL) return 3.8E-1F;
		if(material == Material.WATER) return 2.2E-2F;
		if(material == Material.LAVA) return 8.4E-2F;
		if(material == Material.GLASS) return 7.5E-2F;
		if(material == Material.ROCK) return 3.2E-3F;
		if(material == Material.SAND) return 5.8E-3F;
		if(material == Material.GROUND) return 5.4E-3F;
		if(material == Material.WOOD) return 2.2E-3F;
		return -1;
	}
	
	@Override
	public float getTemperature(World world, BlockPos pos, float baseTemp)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockFluidBase)
			return Math.max(((BlockFluidBase) state.getBlock()).getFluid().getTemperature(world, pos), baseTemp);
		if (state.getBlock() instanceof BlockLiquid)
		{
			return state.getMaterial() == Material.WATER ? baseTemp * 0.95F : FluidRegistry.LAVA.getTemperature();
		}
		return -1;
	}
}