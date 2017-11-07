package fargen.core.render;

import java.util.Random;

import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fargen.core.biome.BiomeBase;
import nebula.client.render.IWorldRender;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSurface implements IWorldRender
{
	private int rainSoundCounter;
	
	@Override
	public boolean renderDropOnGround(World world, Entity entity, Random random, int rendererUpdateCount)
	{
		if (world.provider.getDimension() != 0) return false;
		Minecraft minecraft = Minecraft.getMinecraft();
		float f = world.getRainStrength(1.0F);
		IWorldPropProvider properties = WorldPropHandler.getWorldProperty(world);
		if (!minecraft.gameSettings.fancyGraphics)
		{
			f /= 2.0F;
		}
		
		if (f != 0.0F)
		{
			random.setSeed(rendererUpdateCount * 312987231L);
			BlockPos blockpos = new BlockPos(entity);
			int i = 10;
			double d0 = 0.0D;
			double d1 = 0.0D;
			double d2 = 0.0D;
			int j = 0;
			int k = (int) (100.0F * f * f);
			
			if (minecraft.gameSettings.particleSetting == 1)
			{
				k >>= 1;
			}
			else if (minecraft.gameSettings.particleSetting == 2)
			{
				k = 0;
			}
			
			for (int l = 0; l < k; ++l)
			{
				BlockPos blockpos1 = world.getPrecipitationHeight(blockpos.add(random.nextInt(10) - random.nextInt(10), 0, random.nextInt(10) - random.nextInt(10)));
				Biome biome = world.getBiome(blockpos1);
				BlockPos blockpos2 = blockpos1.down();
				IBlockState state = world.getBlockState(blockpos2);
				
				if (blockpos1.getY() <= blockpos.getY() + 10 && blockpos1.getY() >= blockpos.getY() - 10 && properties.getRainstrength(world, blockpos1) >= 1E-4F && properties.getTemperature(world, blockpos1) >= BiomeBase.minSnowTemperature)
				{
					double d3 = random.nextDouble();
					double d4 = random.nextDouble();
					AxisAlignedBB axisalignedbb = state.getBoundingBox(world, blockpos2);
					
					if (state.getMaterial() != Material.LAVA && state.getBlock() != Blocks.MAGMA)
					{
						if (state.getMaterial() != Material.AIR)
						{
							++j;
							
							if (random.nextInt(j) == 0)
							{
								d0 = blockpos2.getX() + d3;
								d1 = blockpos2.getY() + 0.1F + axisalignedbb.maxY - 1.0D;
								d2 = blockpos2.getZ() + d4;
							}
							
							world.spawnParticle(EnumParticleTypes.WATER_DROP, blockpos2.getX() + d3, blockpos2.getY() + 0.1F + axisalignedbb.maxY, blockpos2.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
						}
					}
					else
					{
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, blockpos1.getX() + d3, blockpos1.getY() + 0.1F - axisalignedbb.minY, blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					}
				}
			}
			
			if (j > 0 && random.nextInt(3) < this.rainSoundCounter++)
			{
				this.rainSoundCounter = 0;
				
				if (d1 > blockpos.getY() + 1 && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor(blockpos.getY()))
				{
					world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F, 0.5F, false);
				}
				else
				{
					world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
				}
			}
		}
		return true;
	}
}
