package farcore.instances;

import farcore.data.V;
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
		if(material == Material.IRON) return 45F;
		if(material == Material.ANVIL) return 38F;
		if(material == Material.WATER) return 2.2F;
		if(material == Material.LAVA) return 8.4F;
		if(material == Material.GLASS) return 7.5F;
		if(material == Material.ROCK) return 3.2F;
		if(material == Material.SAND) return 5.8F;
		if(material == Material.GROUND) return 5.4F;
		if(material == Material.WOOD) return 2.2F;
		if(material == Material.AIR) return V.airHeatConductivity;
		return -1;
	}
	
	@Override
	public float getHeatCapacity(World world, BlockPos pos, IBlockState state)
	{
		Material material = state.getMaterial();
		if(material == Material.IRON) return 4820F;
		if(material == Material.ANVIL) return 5740F;
		if(material == Material.WATER) return 4730F;
		if(material == Material.LAVA) return 3720F;
		if(material == Material.GLASS) return 2730F;
		if(material == Material.ROCK) return 5820F;
		if(material == Material.SAND) return 2710F;
		if(material == Material.GROUND) return 2980F;
		if(material == Material.WOOD) return 3710F;
		if(material == Material.AIR) return V.airHeatCapacity;
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