/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.blocks;

import java.util.Random;

import farcore.data.EnumBlock;
import farcore.data.Materials;
import farcore.lib.fluid.BlockGas;
import fle.api.recipes.instance.FlamableRecipes;
import nebula.common.environment.EnviornmentBlockPos;
import nebula.common.fluid.FluidBase;
import nebula.common.util.Maths;
import nebula.common.util.W;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class BlockGasExplosive extends BlockGas
{
	private int		checkRange	= 3;
	private int		minFlameTemp;
	private int		minExplosiveAmount;
	private float	explosiveStrength;
	
	public BlockGasExplosive(FluidBase fluid, int minFlameTemp, int minExplosiveAmount, float explosiveStrength)
	{
		super(fluid, Materials.GAS);
		this.minFlameTemp = minFlameTemp;
		this.minExplosiveAmount = minExplosiveAmount * this.quantaPerBlock / 1000;
		this.explosiveStrength = explosiveStrength;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		{
			label: if (rand.nextInt(4) == 0 && FlamableRecipes.isFlamable(new EnviornmentBlockPos(worldIn, pos), this.minFlameTemp))
			{
				int amount = 0;
				
				final int max = Maths.cb(this.checkRange * 2 + 1);
				int total = 0;
				double mainX = 0, mainY = 0, mainZ = 0;
				MutableBlockPos pos1 = new MutableBlockPos();
				for (int i = -this.checkRange; i < this.checkRange; ++i)
					for (int j = -this.checkRange; j < this.checkRange; ++j)
						for (int k = -this.checkRange; k < this.checkRange; ++k)
						{
							pos1.setPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k);
							int value = getFluidLevel(worldIn, pos1);
							if (value > 0)
							{
								amount += value;
								mainX += i * value;
								mainY += j * value;
								mainZ += k * value;
							}
							if ((max - ++total) * MAX_QUANTA_VALUE + amount < this.minExplosiveAmount)
							{
								break label;
							}
						}
				
				if (amount >= this.minExplosiveAmount)
				{
					for (int i = -this.checkRange; i < this.checkRange; ++i)
						for (int j = -this.checkRange; j < this.checkRange; ++j)
							for (int k = -this.checkRange; k < this.checkRange; ++k)
							{
								pos1.setPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k);
								if (W.isBlock(worldIn, pos1, this, -1, true))
								{
									worldIn.setBlockToAir(pos1);
								}
							}
					worldIn.newExplosion(null, pos.getX() + mainX * this.checkRange / total, pos.getY() + mainY * this.checkRange / total, pos.getZ() + mainZ * this.checkRange / total, this.explosiveStrength * amount / this.quantaPerBlockFloat, true, true);
					return;
				}
			}
		}
		super.updateTick(worldIn, pos, state, rand);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
	{
		if (worldIn.rand.nextInt(12) == 0 && EnumBlock.fire.block.canPlaceBlockAt(worldIn, pos))
		{
			worldIn.setBlockState(pos, EnumBlock.fire.apply(12));
		}
	}
}
