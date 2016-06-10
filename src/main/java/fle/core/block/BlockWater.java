package fle.core.block;

import farcore.block.BlockStandardFluid;
import farcore.energy.thermal.ThermalNet;
import farcore.enums.EnumBlock;
import farcore.interfaces.energy.thermal.IThermalProviderBlock;
import farcore.util.U;
import farcore.util.Values;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockWater extends BlockStandardFluid implements IThermalProviderBlock
{
	public BlockWater()
	{
		super(FluidRegistry.WATER, Material.water);
		lightOpacity = 3;
		setQuantaPerBlock(16);
		EnumBlock.water.setBlock(this);
	}

	@Override
	public float getBlockTemperature(World world, int x, int y, int z)
	{
		return Math.max(ThermalNet.getEnviormentTemp(world, x, y, z) - 3F, 0F);
	}

	@Override
	public float getThermalConductivity(World world, int x, int y, int z)
	{
		return Values.waterThermalConductivity;
	}

	@Override
	public void onHeatChanged(World world, int x, int y, int z, float input)
	{
		
	}
}