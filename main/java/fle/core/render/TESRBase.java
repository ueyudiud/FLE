package fle.core.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import com.google.common.primitives.SignedBytes;

import fle.FLE;
import fle.core.render.RenderFakeBlock.RenderInfo;
import fle.core.util.FLEMath;

public abstract class TESRBase<T extends TileEntity> extends TileEntitySpecialRenderer
{
	protected static RenderBlocks renderBlocks = new RenderBlocks();
	protected static RenderItem itemRenderer;
	
	public abstract void renderTileEntityAt(T tile, double xPos, double yPos, double zPos);

	public void renderTileEntityAt(TileEntity tile, double xPos, double yPos, double zPos, float aLevel)
	{
		try
		{
			if(tile != null)
				renderTileEntityAt((T) tile, xPos, yPos, zPos);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	protected World getWorldObj()
	{
		return field_147501_a.field_147550_f;
	}

    private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
    private static final Map<Fluid, int[]> flowingRenderCache = new HashMap<Fluid, int[]>();
    private static final Map<Fluid, int[]> stillRenderCache = new HashMap<Fluid, int[]>();
    public static final int DISPLAY_STAGES = 100;
    private static final RenderInfo liquidBlock = new RenderInfo();

    public ResourceLocation getFluidSheet(Fluid fluid)
    {
        return BLOCK_TEXTURE;
    }

    public ResourceLocation setupFlowingLiquidTexture(Fluid fluid, IIcon[] texArray)
    {
        if (fluid == null)
            return null;
        IIcon top = fluid.getStillIcon();
        IIcon side = fluid.getFlowingIcon();
        texArray[0] = top;
        texArray[1] = top;
        texArray[2] = side;
        texArray[3] = side;
        texArray[4] = side;
        texArray[5] = side;
        return getFluidSheet(fluid);
    }

    public static void setColorForTank(FluidStack fluid)
    {
        if (fluid == null)
            return;

        int color = fluid.getFluid().getColor(fluid);
        GL11.glColor3f((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F);
    }
	public int[] getLiquidDisplayLists(Fluid fluid)
	{
        return getLiquidDisplayLists(fluid, false);
    }

    public int[] getLiquidDisplayLists(Fluid fluid, boolean flowing)
    {
        if (fluid == null)
            return null;
        Map<Fluid, int[]> cache = flowing ? flowingRenderCache : stillRenderCache;
        int[] diplayLists = cache.get(fluid);
        if (diplayLists != null)
            return diplayLists;

        diplayLists = new int[DISPLAY_STAGES];

        liquidBlock.texture[0] = null;

        if (fluid.getBlock() != null) 
        {
            liquidBlock.template = fluid.getBlock();
            liquidBlock.texture[0] = getFluidTexture(fluid, flowing);
        } 
        else
        {
            liquidBlock.template = Blocks.water;
            liquidBlock.texture[0] = getFluidTexture(fluid, flowing);
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        for (int s = 0; s < DISPLAY_STAGES; ++s)
        {
            diplayLists[s] = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(diplayLists[s], 4864 /*GL_COMPILE*/);

            liquidBlock.minX = 0.01f;
            liquidBlock.minY = 0;
            liquidBlock.minZ = 0.01f;

            liquidBlock.maxX = 0.99f;
            liquidBlock.maxY = (float) s / (float) DISPLAY_STAGES;
            liquidBlock.maxZ = 0.99f;
            
            RenderFakeBlock.renderBlockForEntity(liquidBlock, null, 0, 0, 0, false, true);

            GL11.glEndList();
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);

        cache.put(fluid, diplayLists);

        return diplayLists;
    }
    
	protected void renderFont(String[] strings, float x, float y, float z, float size, float rotation)
	{
		GL11.glPushMatrix();
		FontRenderer fontrenderer = this.func_147498_b();
		float dis = (float) FLEMath.distance(x, y, z);
		//GL11.glRotatef(rotation, 0F, -1, 0F);
		GL11.glTranslatef(x, y, z);
		GL11.glScalef(size, -size, size);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * size);
		GL11.glDepthMask(false);
		byte b0 = 0;
		
		int i = 0;
		for (String s : strings)
		{
			fontrenderer.drawString(s, - fontrenderer.getStringWidth(s) / 2, i++ * 10 - strings.length * 5, b0);
		}

		GL11.glDepthMask(true);
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glPopMatrix();
	}

	protected float getDefaultRotation()
	{
		return (float) FLEMath.mod(FLE.fle.getPlatform().getPlayerInstance().rotationYaw, 180D);
	}
	
	protected static IIcon getFluidTexture(Fluid fluid, boolean flowing)
	{
        if (fluid == null)
            return Blocks.stone.getIcon(0, 0);
        IIcon icon = flowing ? fluid.getFlowingIcon() : fluid.getStillIcon();
        return icon;
    }
	
	static
	{
        liquidBlock.texture = new IIcon[1];
		itemRenderer = new RenderItem()
		{
			@Override
			public byte getMiniBlockCount(ItemStack stack, byte original)
			{
				return SignedBytes.saturatedCast(Math.min(stack.stackSize / 16, 3) + 1);
			}

			@Override
			public byte getMiniItemCount(ItemStack stack, byte original)
			{
				return SignedBytes.saturatedCast(Math.min(stack.stackSize / 8, 7) + 1);
			}

			@Override
			public boolean shouldBob()
			{
				return true;
			}

			@Override
			public boolean shouldSpreadItems()
			{
				return false;
			}
		};
		itemRenderer.setRenderManager(RenderManager.instance);
	}
}