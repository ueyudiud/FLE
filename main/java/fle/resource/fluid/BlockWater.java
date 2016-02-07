package fle.resource.fluid;

import java.util.Random;

import farcore.FarCore;
import farcore.fluid.BlockFluidBase;
import farcore.fluid.FluidBase;
import farcore.util.Vs;
import farcore.world.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockWater extends BlockFluidBase
{
	public BlockWater(FluidBase fluid, Material material)
	{
		super(fluid, material);
		setQuantaPerBlock(16);
	}
	
	private static final float EVAPORATE_CHANCE_MULTIPER = 1.28E-5F;
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		super.updateTick(world, x, y, z, rand);
		int v;
		if((v = getQuantaValue(world, x, y, z)) > 0)
		{
			int temp = FarCore.getEnviormentTemperature(world, new BlockPos(world, x, y, z));
			if(temp > Vs.water_freeze_point)
			{
				if((temp - Vs.water_freeze_point) * EVAPORATE_CHANCE_MULTIPER < rand.nextDouble())
				{
					if(v == 1) world.setBlockToAir(x, y, z);
					else world.setBlockMetadataWithNotify(x, y, z, v - 2, 2);
					return;
				}
			}
		}
	}
}