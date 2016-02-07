package farcore.render.entity;

import org.lwjgl.opengl.GL11;

import farcore.block.interfaces.IFallable;
import farcore.entity.EntityFleFallingBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Not use minecraft render.<br>
 * To load custom icon for render block.
 * @author ueyudiud
 *
 */
public class RenderFleFallingBlock extends Render
{
	protected RenderBlocks blockRender = new RenderBlocks();
	
	public RenderFleFallingBlock()
	{
		this.shadowSize = 0.5F;
	}
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float a,
			float b)
	{
		doRender((EntityFleFallingBlock) entity, x, y, z, a, b);
	}

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityFleFallingBlock entity)
    {
        return TextureMap.locationBlocksTexture;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return getEntityTexture((EntityFleFallingBlock)entity);
    }
	
	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(EntityFleFallingBlock entity, double x, double y, double z, float a, float b)
	{
		World world = entity.getWorld();
		Block block = entity.getBlock();
		int i = MathHelper.floor_double(entity.posX);
		int j = MathHelper.floor_double(entity.posY);
		int k = MathHelper.floor_double(entity.posZ);
		
		if (block != null && block != world.getBlock(i, j, k))
		{
			GL11.glPushMatrix();
			GL11.glTranslatef((float)x, (float)y, (float)z);
			bindEntityTexture(entity);
			GL11.glDisable(GL11.GL_LIGHTING);
			Tessellator tessellator;
			
			blockRender.setRenderBoundsFromBlock(block);
			renderFalling(entity, world, i, j, k);
			
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}
	
	protected void renderFalling(EntityFleFallingBlock block, World world, int x, int y, int z)
	{
		IFallable fallable = (IFallable) block.getBlock();
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(block.getBlock().getMixedBrightnessForBlock(world, x, y, z));
		tessellator.setColorOpaque_F(f, f, f);
		blockRender.renderFaceYNeg(block.getBlock(), -0.5D, -0.5D, -0.5D, fallable.getFallingIcon(block, 0));
		tessellator.setColorOpaque_F(f1, f1, f1);
		blockRender.renderFaceYPos(block.getBlock(), -0.5D, -0.5D, -0.5D, fallable.getFallingIcon(block, 1));
		tessellator.setColorOpaque_F(f2, f2, f2);
		blockRender.renderFaceZNeg(block.getBlock(), -0.5D, -0.5D, -0.5D, fallable.getFallingIcon(block, 2));
		tessellator.setColorOpaque_F(f2, f2, f2);
		blockRender.renderFaceZPos(block.getBlock(), -0.5D, -0.5D, -0.5D, fallable.getFallingIcon(block, 3));
		tessellator.setColorOpaque_F(f3, f3, f3);
		blockRender.renderFaceXNeg(block.getBlock(), -0.5D, -0.5D, -0.5D, fallable.getFallingIcon(block, 4));
		tessellator.setColorOpaque_F(f3, f3, f3);
		blockRender.renderFaceXPos(block.getBlock(), -0.5D, -0.5D, -0.5D, fallable.getFallingIcon(block, 5));
		tessellator.draw();
	}
}