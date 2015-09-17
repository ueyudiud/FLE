package fle.core.render;

import org.lwjgl.opengl.GL11;

import fle.core.te.TileEntityPolish;
import fle.core.te.argil.TileEntityArgilItems;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TESRPolishTable extends TESRBase<TileEntityPolish>
{
	@Override
	public void renderTileEntityAt(TileEntityPolish tile, double xPos,
			double yPos, double zPos)
	{
		EntityItem customitem = new EntityItem(getWorldObj());
		customitem.hoverStart = 0F;
		float blockScale = 1.0F;
		if(!tile.getBlockPos().toPos(ForgeDirection.UP).isAir()) return;
		if(tile.getStackInSlot(0) != null)
		{
			GL11.glPushMatrix(); //start
			GL11.glTranslatef((float)xPos + 0.25F, (float)yPos + 1.0F, (float)zPos + 0.325F);
			if (RenderManager.instance.options.fancyGraphics)
			{
				GL11.glRotatef(180F, 0.0F, 0.5F, 0.5F);
			}
			GL11.glScalef(blockScale, blockScale, blockScale);
			ItemStack target = tile.getStackInSlot(0).copy();
			customitem.setEntityItemStack(target);
			try
			{
				itemRenderer.doRender(customitem, 0, 0, 0, 0, 0.0F);
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
			GL11.glPopMatrix(); //end
		}
		if(tile.getStackInSlot(1) != null)
		{
			GL11.glPushMatrix(); //start
			GL11.glTranslatef((float)xPos + 0.75F, (float)yPos + 1.0F, (float)zPos + 0.125F);
			if (RenderManager.instance.options.fancyGraphics)
			{
				GL11.glRotatef(180F, 0.0F, 0.5F, 0.5F);
			}
			GL11.glScalef(blockScale, blockScale, blockScale);
			ItemStack target = tile.getStackInSlot(1).copy();
			customitem.setEntityItemStack(target);
			try
			{
				itemRenderer.doRender(customitem, 0, 0, 0, 0, 0.0F);
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
			GL11.glPopMatrix(); //end
		}	
	}
}