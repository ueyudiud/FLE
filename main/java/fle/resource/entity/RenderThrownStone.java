package fle.resource.entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.interfaces.IFallable;
import farcore.entity.EntityFleFallingBlock;
import farcore.render.entity.RenderFleFallingBlock;
import flapi.FleResource;
import fle.init.Blocks;
import fle.resource.block.auto.BlockUniversalStoneChip;
import fle.resource.block.auto.ResourceIcons;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class RenderThrownStone extends Render
{
    private final RenderBlocks render = new RenderBlocks();
    
	@Override
	public void doRender(Entity entity, double x, double y, double z, float a,
			float b)
	{
		doRender((EntityThrowStone) entity, x, y, z, a, b);
	}
	
	public void doRender(EntityThrowStone entity, double x, double y, double z, float a,
			float b)
	{
        World world = entity.worldObj;
        ItemStack item = 
        		((BlockUniversalStoneChip) Blocks.stoneChip).setRock(new ItemStack(Blocks.stoneChip), entity.rock, entity.size);
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY);
        int k = MathHelper.floor_double(entity.posZ);

//        if (block != null && block != world.getBlock(i, j, k))
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            this.bindEntityTexture(entity);
            GL11.glDisable(GL11.GL_LIGHTING);
            Tessellator tessellator;

//            render.setRenderBoundsFromBlock(Blocks.stoneChip);
            renderStone(entity, world, i, j, k);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
	}
	
	protected void renderStone(EntityThrowStone stone, World world, int x, int y, int z)
	{
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(Blocks.stoneChip.getMixedBrightnessForBlock(world, x, y, z));
		tessellator.setColorOpaque_F(f, f, f);
		render.renderFaceYNeg(Blocks.stoneChip, -0.5D, -0.5D, -0.5D,
				ResourceIcons.rock[FleResource.rock.serial(stone.rock)]);
		tessellator.setColorOpaque_F(f1, f1, f1);
		render.renderFaceYPos(Blocks.stoneChip, -0.5D, -0.5D, -0.5D,
				ResourceIcons.rock[FleResource.rock.serial(stone.rock)]);
		tessellator.setColorOpaque_F(f2, f2, f2);
		render.renderFaceZNeg(Blocks.stoneChip, -0.5D, -0.5D, -0.5D,
				ResourceIcons.rock[FleResource.rock.serial(stone.rock)]);
		tessellator.setColorOpaque_F(f2, f2, f2);
		render.renderFaceZPos(Blocks.stoneChip, -0.5D, -0.5D, -0.5D,
				ResourceIcons.rock[FleResource.rock.serial(stone.rock)]);
		tessellator.setColorOpaque_F(f3, f3, f3);
		render.renderFaceXNeg(Blocks.stoneChip, -0.5D, -0.5D, -0.5D,
				ResourceIcons.rock[FleResource.rock.serial(stone.rock)]);
		tessellator.setColorOpaque_F(f3, f3, f3);
		render.renderFaceXPos(Blocks.stoneChip, -0.5D, -0.5D, -0.5D,
				ResourceIcons.rock[FleResource.rock.serial(stone.rock)]);
		tessellator.draw();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return TextureMap.locationBlocksTexture;
	}
}