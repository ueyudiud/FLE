package farcore.lib.block.instance;

import java.util.Random;

import farcore.data.EnumBlock;
import farcore.data.V;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.block.BlockStandardFluid;
import farcore.lib.fluid.FluidBase;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWater extends BlockStandardFluid
{
	public BlockWater(FluidBase fluid)
	{
		super(fluid, Material.WATER);
		EnumBlock.water.set(this);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		int level = getFluidLevel(worldIn, pos);
		if(level == quantaPerBlock - 1)
		{
			if(worldIn.canSeeSky(pos) &&
					ThermalNet.getTemperature(worldIn, pos, true) < V.waterFreezePoint &&
					rand.nextInt(5) == 0)
			{
				worldIn.setBlockState(pos, EnumBlock.ice.block.getDefaultState(), 3);
				return;
			}
		}
		super.updateTick(worldIn, pos, state, rand);
	}
	
	@Override
	public void fillWithRain(World worldIn, BlockPos pos)
	{
		super.fillWithRain(worldIn, pos);
		if(worldIn.provider.isSurfaceWorld() && worldIn.canSeeSky(pos))
		{
			int level = getFluidLevel(worldIn, pos);
			if(level < quantaPerBlock - 1)
			{
				IWorldPropProvider properties = WorldPropHandler.getWorldProperty(worldIn);
				float rainstrength = properties.getRainstrength(worldIn, pos);
				if(worldIn.rand.nextInt((int) (32F / (rainstrength + 1E-2F))) == 0)
				{
					setFluidLevel(worldIn, pos, level + 1, true);
				}
			}
		}
	}
}