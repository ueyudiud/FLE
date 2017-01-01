package farcore.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import farcore.lib.item.IItemBehaviorsAndProperties.IIP_CustomOverlayInGui;
import farcore.lib.model.item.ICustomItemRenderModel;
import farcore.lib.render.IWorldRender;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The overridden method in Minecraft.
 * @author ueyudiud
 *
 */
@SideOnly(Side.CLIENT)
public class ClientOverride
{
	public static final List<IWorldRender> RENDERS = new ArrayList();
	private static int rainSoundCounter;
	
	public static void renderDropOnGround(Random random, int rendererUpdateCount)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		World world = minecraft.theWorld;
		Entity entity = minecraft.getRenderViewEntity();
		for(IWorldRender render : RENDERS)
		{
			if(render.renderDropOnGround(world, entity, random, rendererUpdateCount))
				return;
		}
		
		float f = world.getRainStrength(1.0F);
		
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
			int k = (int)(100.0F * f * f);
			
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
				Biome biome = world.getBiomeGenForCoords(blockpos1);
				BlockPos blockpos2 = blockpos1.down();
				IBlockState iblockstate = world.getBlockState(blockpos2);
				
				if (blockpos1.getY() <= blockpos.getY() + 10 && blockpos1.getY() >= blockpos.getY() - 10 && biome.canRain() && biome.getFloatTemperature(blockpos1) >= 0.15F)
				{
					double d3 = random.nextDouble();
					double d4 = random.nextDouble();
					AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, blockpos2);
					
					if (iblockstate.getMaterial() != Material.LAVA && iblockstate.getBlock() != Blocks.field_189877_df)
					{
						if (iblockstate.getMaterial() != Material.AIR)
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
			
			if (j > 0 && random.nextInt(3) < rainSoundCounter++)
			{
				rainSoundCounter = 0;
				
				if (d1 > blockpos.getY() + 1 && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor_float(blockpos.getY()))
				{
					world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F, 0.5F, false);
				}
				else
				{
					world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
				}
			}
		}
	}
	
	public static List<BakedQuad> renderItemModel(IBakedModel model, EnumFacing facing, long rand, ItemStack stack)
	{
		if(model instanceof ICustomItemRenderModel)
			return ((ICustomItemRenderModel) model).getQuads(stack, facing, rand);
		return model.getQuads(null, facing, rand);
	}
	
	public static void renderCustomItemOverlayIntoGUI(RenderItem render, FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text)
	{
		if(stack == null) return;
		Item item = stack.getItem();
		if(!(item instanceof IIP_CustomOverlayInGui) || !((IIP_CustomOverlayInGui) item).renderCustomItemOverlayIntoGUI(render, fr, stack, xPosition, yPosition, text))
		{
			render.renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, text);
		}
	}
}