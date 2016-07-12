package farcore.lib.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.entity.EntityFallingBlockExtended;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class RenderFallingBlockExtended extends Render
{
	private RenderBlocks render = new RenderBlocks();
	
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
	public void doRender(EntityFallingBlockExtended entity, double x, double y, double z, float a, float b)
	{
		World world = entity.worldObj;
		Block block = entity.getBlock();
		if(block == null) return;
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY);
        int k = MathHelper.floor_double(entity.posZ);

        if (block != null && block != world.getBlock(i, j, k))
        {
        	GL11.glPushMatrix();
            GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);
            this.bindEntityTexture(entity);
            GL11.glDisable(GL11.GL_LIGHTING);
            
            Tessellator tessellator;
            this.render.blockAccess = world;
//            tessellator = Tessellator.instance;
//            tessellator.startDrawingQuads();
//            tessellator.setTranslation((double)((float)(-i) - 0.5F), (double)((float)(-j) - 0.5F), (double)((float)(-k) - 0.5F));
            this.render.setRenderBoundsFromBlock(block);
            this.render.renderBlockSandFalling(block, world, i, j, k, entity.getMeta());
//            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
//            tessellator.draw();
            
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityFallingBlock entity)
    {
        return TextureMap.locationBlocksTexture;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return getEntityTexture((EntityFallingBlock) entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double x, double y, double z, float a, float b)
    {
        this.doRender((EntityFallingBlockExtended)entity, x, y, z, a, b);
    }
}