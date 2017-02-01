/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.util;

import java.util.Random;

import nebula.client.render.FontRenderExtend;
import nebula.client.render.IProgressBarStyle;
import nebula.client.render.ParticleDiggingExt;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class Client
{
	public static final ModelResourceLocation MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
	
	public static int mulColor(int ARGB1, int ARGB2)
	{
		int a = ((ARGB1 >> 24) & 0xFF) * ((ARGB2 >> 24) & 0xFF) >> 8;
		int r = ((ARGB1 >> 16) & 0xFF) * ((ARGB2 >> 16) & 0xFF) >> 8;
		int g = ((ARGB1 >> 8 ) & 0xFF) * ((ARGB2 >> 8 ) & 0xFF) >> 8;
		int b = ((ARGB1      ) & 0xFF) * ((ARGB2      ) & 0xFF) >> 8;
		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public static void addBlockHitEffect(World world, Random rand, IBlockState state, EnumFacing side, BlockPos pos, ParticleManager manager)
	{
		if (state.getRenderType() != EnumBlockRenderType.INVISIBLE)
		{
			int i = pos.getX();
			int j = pos.getY();
			int k = pos.getZ();
			float f = 0.1F;
			AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
			double d0 = i + rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
			double d1 = j + rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
			double d2 = k + rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;
			if (side == EnumFacing.DOWN)
			{
				d1 = j + axisalignedbb.minY - 0.10000000149011612D;
			}
			if (side == EnumFacing.UP)
			{
				d1 = j + axisalignedbb.maxY + 0.10000000149011612D;
			}
			if (side == EnumFacing.NORTH)
			{
				d2 = k + axisalignedbb.minZ - 0.10000000149011612D;
			}
			if (side == EnumFacing.SOUTH)
			{
				d2 = k + axisalignedbb.maxZ + 0.10000000149011612D;
			}
			if (side == EnumFacing.WEST)
			{
				d0 = i + axisalignedbb.minX - 0.10000000149011612D;
			}
			if (side == EnumFacing.EAST)
			{
				d0 = i + axisalignedbb.maxX + 0.10000000149011612D;
			}
			manager.addEffect((new ParticleDiggingExt(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state)).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
	}
	
	public static void addBlockHitEffect(World world, Random rand, IBlockState state, EnumFacing side, BlockPos pos, ParticleManager manager, Object icon)
	{
		if (state.getRenderType() != EnumBlockRenderType.INVISIBLE)
		{
			int i = pos.getX();
			int j = pos.getY();
			int k = pos.getZ();
			float f = 0.1F;
			AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
			double d0 = i + rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
			double d1 = j + rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
			double d2 = k + rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;
			if (side == EnumFacing.DOWN)
			{
				d1 = j + axisalignedbb.minY - 0.10000000149011612D;
			}
			if (side == EnumFacing.UP)
			{
				d1 = j + axisalignedbb.maxY + 0.10000000149011612D;
			}
			if (side == EnumFacing.NORTH)
			{
				d2 = k + axisalignedbb.minZ - 0.10000000149011612D;
			}
			if (side == EnumFacing.SOUTH)
			{
				d2 = k + axisalignedbb.maxZ + 0.10000000149011612D;
			}
			if (side == EnumFacing.WEST)
			{
				d0 = i + axisalignedbb.minX - 0.10000000149011612D;
			}
			if (side == EnumFacing.EAST)
			{
				d0 = i + axisalignedbb.maxX + 0.10000000149011612D;
			}
			manager.addEffect((new ParticleDiggingExt(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state, icon)).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
	}
	
	public static void addBlockDestroyEffects(World world, BlockPos pos, IBlockState state, ParticleManager manager)
	{
		int i = 4;
		for (int j = 0; j < 4; ++j)
		{
			for (int k = 0; k < 4; ++k)
			{
				for (int l = 0; l < 4; ++l)
				{
					double d0 = pos.getX() + (j + 0.5D) / 4.0D;
					double d1 = pos.getY() + (k + 0.5D) / 4.0D;
					double d2 = pos.getZ() + (l + 0.5D) / 4.0D;
					manager.addEffect((new ParticleDiggingExt(world, d0, d1, d2, d0 - pos.getX() - 0.5D, d1 - pos.getY() - 0.5D, d2 - pos.getZ() - 0.5D, state)).setBlockPos(pos));
				}
			}
		}
	}
	
	public static void addBlockDestroyEffects(World world, BlockPos pos, IBlockState state, ParticleManager manager, Object icon)
	{
		int i = 4;
		for (int j = 0; j < 4; ++j)
		{
			for (int k = 0; k < 4; ++k)
			{
				for (int l = 0; l < 4; ++l)
				{
					double d0 = pos.getX() + (j + 0.5D) / 4.0D;
					double d1 = pos.getY() + (k + 0.5D) / 4.0D;
					double d2 = pos.getZ() + (l + 0.5D) / 4.0D;
					manager.addEffect((new ParticleDiggingExt(world, d0, d1, d2, d0 - pos.getX() - 0.5D, d1 - pos.getY() - 0.5D, d2 - pos.getZ() - 0.5D, state, icon)).setBlockPos(pos));
				}
			}
		}
	}
	
	public static boolean shouldRenderBetterLeaves()
	{
		return Blocks.LEAVES.getBlockLayer() == BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	public static void registerModel(Block block, int meta, String modid, String path)
	{
		registerModel(Item.getItemFromBlock(block), meta, modid, path);
	}
	
	public static void registerModel(Item item, int meta, String modid, String path)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modid, path));
	}
	
	private static FontRenderExtend render;
	
	public static FontRenderer getFontRender()
	{
		if(render == null)
		{
			render = new FontRenderExtend();
		}
		return render;
	}
	
	private static final IProgressBarStyle STANDARD_PROGRESSBAR_STYLE = new IProgressBarStyle()
	{
		@Override
		public double getProgressScale(ItemStack stack)
		{
			double durability = stack.getItem().getDurabilityForDisplay(stack);
			return durability == 0 ? -1 : 1.0 - durability;
		}
		
		@Override
		public int[] getProgressColor(ItemStack stack, double progress)
		{
			int i = (int) (progress * 255);
			return new int[]{i, 255 - i, 0};
		}
	};
	
	public static void renderItemDurabilityBarInGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z)
	{
		renderItemDurabilityBarInGUI(render, fontRenderer, stack, x, z, 1, STANDARD_PROGRESSBAR_STYLE);
	}
	public static void renderItemDurabilityBarInGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z, int off, IProgressBarStyle barStyle)
	{
		double health = barStyle.getProgressScale(stack);
		if(health < 0)
			return;
		if(health > 1)
		{
			health = 1;
		}
		int j = (int) Math.round(health * 13.0D);
		int[] color = barStyle.getProgressColor(stack, health);
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		draw(vertexbuffer, x + 2, z + 15 - 2 * off, 13, 2, 0, 0, 0, 255);
		draw(vertexbuffer, x + 2, z + 15 - 2 * off, 12, 1, color[0] / 4, color[1] / 4, color[2] / 4, 255);
		draw(vertexbuffer, x + 2, z + 15 - 2 * off,  j, 1, color[0], color[1], color[2], 255);
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}
	
	public static void renderItemCooldownInGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z)
	{
		EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
		float f = entityplayersp == null ? 0.0F : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());
		
		if (f > 0.0F)
		{
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			Tessellator tessellator1 = Tessellator.getInstance();
			VertexBuffer vertexbuffer1 = tessellator1.getBuffer();
			draw(vertexbuffer1, x, z + MathHelper.floor(16.0F * (1.0F - f)), 16, MathHelper.ceil(16.0F * f), 255, 255, 255, 127);
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}
	}
	
	public static void renderItemSubscirptInGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z, String text)
	{
		if (text != null)
		{
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableBlend();
			fontRenderer.drawStringWithShadow(text, x + 19 - 2 - fontRenderer.getStringWidth(text), z + 6 + 3, 16777215);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			GlStateManager.enableBlend();
		}
	}
	
	/**
	 * Draw with the WorldRenderer
	 */
	private static void draw(VertexBuffer renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha)
	{
		renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x + 0, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + 0, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + width, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + width, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
		Tessellator.getInstance().draw();
	}
}