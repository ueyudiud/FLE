package fle.core.render;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumBlock;
import farcore.lib.render.RenderBase;
import fle.core.block.BlockFire;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class RenderFire extends RenderBase
{
	@Override
	public void renderBlock()
	{
		setTexture(block);
		Tessellator tessellator = Tessellator.instance;
//        IIcon iicon = block.getFireIcon(0);
//        IIcon iicon1 = block.getFireIcon(1);
//        IIcon iicon2 = iicon;

        if (render.hasOverrideBlockTexture())
        {
//            iicon2 = render.overrideBlockTexture;
        }

        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        tessellator.setBrightness(block.getMixedBrightnessForBlock(this.world, x, y, z));
        double d0 = 0;
        double d1 = 0;
        double d2 = 16;
        double d3 = 16;
        float f = 1.4F;
        double d4, d5, d6, d7, d8, d9, d10, d11, d12;

        if (!World.doesBlockHaveSolidTopSurface(this.world, x, y - 1, z) && !BlockFire.canCatchFire(this.world, x, y - 1, z, UP))
        {
            float f2 = 0.2F;
            float f1 = 0.0625F;
//            if ((x + y + z & 1) == 1)
//            {
//                d0 = (double)iicon1.getMinU();
//                d1 = (double)iicon1.getMinV();
//                d2 = (double)iicon1.getMaxU();
//                d3 = (double)iicon1.getMaxV();
//            }
            if ((x / 2 + y / 2 + z / 2 & 1) == 1)
            {
                d5 = d2;
                d2 = d0;
                d0 = d5;
            }
            if (BlockFire.canCatchFire(this.world, x - 1, y, z, EAST))
            {
            	renderFace(f2, f + f1, 1F, 0, f1, 1F, 0, f1, 0, f2, f + f1, 0);
            }

            if (BlockFire.canCatchFire(this.world, x + 1, y, z, WEST))
            {
            	renderFace(1F - f2, f + f1, 0, 1F, f1, 0, 1F, f1, 1F, 1F - f2, f + f1, 1F);
            }

            if (BlockFire.canCatchFire(this.world, x, y, z - 1, SOUTH))
            {
            	renderFace(0, f + f1, f2, 0, f1, 0, 1F, f1, 0, 1F, f + f1, f2);
            }

            if (BlockFire.canCatchFire(this.world, x, y, z + 1, NORTH))
            {
            	renderFace(1F, f + f1, 1F - f2, 1F, f1, 1F, 0, f1, 1F, 0, f + f1, 1F - f2);
            }

            if (BlockFire.canCatchFire(this.world, x, y + 1, z, DOWN))
            {
                d5 = (double)0.5D + 0.5D;
                d6 = (double)0.5D - 0.5D;
                d7 = (double)0.5D + 0.5D;
                d8 = (double)0.5D - 0.5D;
                d9 = (double)0.5D - 0.5D;
                d10 = (double)0.5D + 0.5D;
                d11 = (double)0.5D - 0.5D;
                d12 = (double)0.5D + 0.5D;
                ++y;
                f = -0.2F;

                if ((x + y + z & 1) == 0)
                {
                	renderFace(d9, f, 0, d5, 0, 0F, d5, 0, 1F, d9, f, 1F);
                	renderFace(d10, f, 1F, d6, 0, 1F, d6, 0, 0, d10, f, 0);
                }
                else
                {
                	renderFace(0, f, d12, 0, 0, d8, 1F, 0, d8, 1F, 0, d12);
                	renderFace(1F, f, d11, 1F, 0, d7, 0, 0, d7, 0, f, d11);
                }
            }
        }
        else
        {
            d4 = (double)0.5D + 0.2D;
            d5 = (double)0.5D - 0.2D;
            d6 = (double)0.5D + 0.2D;
            d7 = (double)0.5D - 0.2D;
            d8 = (double)0.5D - 0.3D;
            d9 = (double)0.5D + 0.3D;
            d10 = (double)0.5D - 0.3D;
            d11 = (double)0.5D + 0.3D;
            renderFace(d8, f, 1F, d4, 0, 1F, d4, 0, 0, d8, f, 0);
            renderFace(1F, f, d8, 1F, 0, d4, 0, 0, d4, 0, f, d8);
            renderFace(1F, f, d11, 1F, 0, d7, 0, 0, d7, 0, f, d11);
            renderFace(d11, f, 1F, d7, 0, 1F, d7, 0, 0, d11, f, 0);
            d4 = (double)0.5D - 0.5D;
            d5 = (double)0.5D + 0.5D;
            d6 = (double)0.5D - 0.5D;
            d7 = (double)0.5D + 0.5D;
            d8 = (double)0.5D - 0.4D;
            d9 = (double)0.5D + 0.4D;
            d10 = (double)0.5D - 0.4D;
            d11 = (double)0.5D + 0.4D;
            renderFace(d8, f, 0, d4, 0, 0, d4, 0, 1F, d8, f, 1F);
            renderFace(0, f, d8, 0, 0, d4, 1F, 0, d4, 1F, f, d8);
            renderFace(0, f, d11, 0, 0, d7, 1F, 0, d7, 1F, f, d11);
            renderFace(d11, f, 0, d7, 0, 0, d7, 0, 1F, d11, f, 1F);
        }
	}
}