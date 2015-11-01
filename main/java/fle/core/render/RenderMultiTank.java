package fle.core.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidTank;
import fle.core.block.tank.ItemTankBlock;
import fle.core.te.tank.TileEntityMultiTank;
import fle.core.util.TankBlockInfo;

public class RenderMultiTank extends RenderBase
{
	@Override
	public void renderBlock() 
	{
		boolean breaking = render.overrideBlockTexture != null;
		if(breaking)
		{
			renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			return;
		}
		IIcon override;
		IIcon base;
		FluidTank tank;

		if(isItem())
		{
			ItemStack stack = new ItemStack(block, 1, meta);
			TankBlockInfo info = ItemTankBlock.c(stack);
			override = block.getIcon(0, meta & 15);
			base = info.getMaterial().getIcon(0, info.getMetadata());
		}
		else
		{
			override = block.getIcon(0, meta);
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile instanceof TileEntityMultiTank)
			{
				base = ((TileEntityMultiTank) tile).getTankMaterial().getIcon(0, ((TileEntityMultiTank) tile).getTankMaterialMeta());
			}
			else
			{
				base = Blocks.stone.getIcon(0, 0);
			}
		}
		setTexture(base);
		renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		render.clearOverrideBlockTexture();
		setTexture(override);
		renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}
}