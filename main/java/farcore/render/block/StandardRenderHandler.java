package farcore.render.block;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import farcore.block.interfaces.ITextureSwitcher;
import flapi.util.Values;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * Provide a render handler which implements of ITextureSwitcher
 * blocks to get texture.
 * @author ueyudiud
 * @see farcore.block.interfaces.ITextureSwitcher
 */
public class StandardRenderHandler implements ISimpleBlockRenderingHandler
{
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		/**
		 * The block will not use this method to render inventory block.
		 * Use switcher to switch render type to default id, and use classic
		 * renderer to render block.
		 * @see farcore.render.block.ItemBlockRender
		 */
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer)
	{
		ITextureSwitcher switcher = (ITextureSwitcher) block;
		int pass = switcher.getRenderPasses(world, x, y, z);
		for(int i = 0; i < pass; ++i)
		{
			GL11.glPushMatrix();
			switcher.switchRender(i);
			renderer.renderStandardBlock(block, x, y, z);
			GL11.glPopMatrix();
		}
		switcher.setRenderToDefault();
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return Values.FLE_BASIC_RENDER_ID;
	}	
}