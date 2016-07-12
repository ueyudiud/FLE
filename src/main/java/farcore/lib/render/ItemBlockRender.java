package farcore.lib.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class ItemBlockRender implements IItemRenderer
{
	public static final ItemBlockRender instance = new ItemBlockRender();
	public static ItemStack stack;

	private RenderBlocks render = new RenderBlocks();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return type == ItemRenderType.ENTITY;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		stack = item;
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		if (type.equals(ItemRenderType.ENTITY))
	    {
			GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
			GL11.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
			GL11.glTranslated(-0.5D, -0.6D, 0.0D);
	    }
	    else if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON))
	    {
	    	GL11.glTranslated(1.0D, 1.0D, 0.0D);
	    	GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
	    }
	    else if (type.equals(ItemRenderType.EQUIPPED))
	    {
	    	GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
	    	GL11.glTranslated(-1.0D, -1.0D, 0.0D);
	    }
		render.renderBlockAsItem(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage(), 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}
}