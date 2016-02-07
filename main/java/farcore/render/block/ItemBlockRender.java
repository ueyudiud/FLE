package farcore.render.block;

import org.lwjgl.opengl.GL11;

import farcore.block.interfaces.ITextureSwitcher;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemBlockRender implements IItemRenderer
{
	public static final ItemBlockRender instance = new ItemBlockRender();
	
	private static ThreadLocal<ItemStack> thread = new ThreadLocal();
	
	private ItemBlockRender(){}
	
	public static ItemStack getStack()
	{
		return thread.get();
	}
	
	public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
	{
		return item.getItem() instanceof ItemBlock;
	}
	
	public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
	{
		return true;
	}
  
	public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
	{
		Block block = Block.getBlockFromItem(item.getItem());
		RenderBlocks renderer = (RenderBlocks) data[0];
		renderer.renderAllFaces = false;
		if ((type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON))
		{
			translateToHand();
		}
		if(block instanceof ITextureSwitcher)
		{
			ITextureSwitcher switcher = (ITextureSwitcher) block;
			renderer.renderAllFaces = false;
			thread.set(item);
			int render = switcher.getRenderPasses(item);
			for(int i = 0; i < render; ++i)
			{
				GL11.glPushMatrix();
				float scale = 1.00F + 0.00390625F * i;
				switcher.switchRender(i);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glScalef(scale, scale, scale);				
				renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glPopMatrix();
				GL11.glDisable(GL11.GL_ALPHA_TEST);
			}
			thread.set(null);
			switcher.setRenderToDefault();
			renderer.renderAllFaces = true;
		}
		else
		{
			renderer.renderAllFaces = false;
			thread.set(item);
			renderer.renderBlockAsItem(block, item.getItemDamage(), 1.0F);
			thread.set(null);
			renderer.renderAllFaces = true;
		}
	}
	
	private void translateToHand()
	{
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}