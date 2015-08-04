package fle.core.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;

@SideOnly(Side.CLIENT)
public class RenderHandler implements ISimpleBlockRenderingHandler
{
	private static final Map<String, RenderBase> renders = new HashMap();

	public static void register(Block block, int meta, Class clazz)
	{
		try
		{
			renders.put(Block.blockRegistry.getNameForObject(block) + ":" + meta, (RenderBase) clazz.newInstance());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static RenderBase getRenderFromBlockAndMeta(Block block, int meta)
	{
		RenderBase ret = renders.get(Block.blockRegistry.getNameForObject(block) + ":" + meta);
		return ret == null ? renders.get(Block.blockRegistry.getNameForObject(block) + ":" + OreDictionary.WILDCARD_VALUE) : ret;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks render1) 
	{
		RenderBase render = getRenderFromBlockAndMeta(block, metadata);
		if(render != null)
		{
			render.render(render1, block, metadata);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks render1) 
	{
		RenderBase render = getRenderFromBlockAndMeta(block, world.getBlockMetadata(x, y, z));
		if(render != null)
		{
			return render.render(render1, world, x, y, z);
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) 
	{
		return true;
	}

	@Override
	public int getRenderId() 
	{
		return FleValue.FLE_RENDER_ID;
	}
}