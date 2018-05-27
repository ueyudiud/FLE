/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.fluid;

import java.util.Random;

import farcore.data.Config;
import farcore.data.EnumBlock;
import farcore.data.Materials;
import farcore.data.V;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import nebula.common.block.BlockStreamFluid;
import nebula.common.fluid.FluidBase;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

/**
 * The override water block with limited water amount.
 * 
 * @author ueyudiud
 */
public class BlockWater extends BlockStreamFluid
{
	public BlockWater(FluidBase fluid)
	{
		super(fluid, Materials.WATER);
		EnumBlock.water.set(this);
		EnumBlock.water.stateApplier = createFunctionApplier(this);
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		int level = getFluidLevel(worldIn, pos);
		if (!worldIn.isRemote)
		{
			if (level == this.quantaPerBlock)
			{
				if (Config.enableWaterFreezeAndIceMeltTempCheck)
				{
					float det;
					Block block = worldIn.getBlockState(pos.up()).getBlock();
					if (block != this && block != EnumBlock.ice.block && (det = V.WATER_FREEZE_POINT_F - ThermalNet.getTemperature(worldIn, pos, true)) > 0)
					{
						int chance = 5 / (int) (det / 3F + 1F);
						if (chance < 10 && L.nextInt(chance, random) == 0)
						{
							worldIn.setBlockState(pos, EnumBlock.ice.block.getDefaultState(), 2);
							return;
						}
					}
				}
				else if (worldIn.getPrecipitationHeight(pos).getY() == pos.getY() && random.nextInt(6) == 0)
				{
					if (worldIn.getBiome(pos).isSnowyBiome() && worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
					{
						worldIn.setBlockState(pos, EnumBlock.ice.block.getDefaultState(), 3);
						return;
					}
				}
			}
			else if (Config.enableWaterEvaporation && level <= 3)
			{
				float temp = ThermalNet.getTemperature(worldIn, pos, true);
				if (temp >= V.WATER_FREEZE_POINT_F + 15)
				{
					int i = (int) (20 / (1 + Math.log1p(temp - (V.WATER_FREEZE_POINT_F + 15))));
					if (i < 4 || L.nextInt(i, random) == 0)
					{
						worldIn.setBlockToAir(pos);
						return;
					}
				}
			}
		}
		super.randomTick(worldIn, pos, state, random);
	}
	
	@Override
	public void fillWithRain(World worldIn, BlockPos pos)
	{
		super.fillWithRain(worldIn, pos);
		if (worldIn.provider.isSurfaceWorld() && worldIn.canSeeSky(pos))
		{
			int level = getFluidLevel(worldIn, pos);
			if (level < this.quantaPerBlock - 1)
			{
				IWorldPropProvider properties = WorldPropHandler.getWorldProperty(worldIn);
				float rainstrength = properties.getRainstrength(worldIn, pos);
				if (worldIn.rand.nextInt((int) (32F / (rainstrength + 1E-2F))) == 0)
				{
					setFluidLevel(worldIn, pos, level + 1, true);
				}
			}
		}
	}
}
