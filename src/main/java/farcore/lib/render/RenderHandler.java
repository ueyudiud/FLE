package farcore.lib.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;

@SideOnly(Side.CLIENT)
public class RenderHandler implements ISimpleBlockRenderingHandler
{
	private final Map<Class, RenderBase> renderMap = new HashMap();
	private final Map<String, RenderBase> blockRenders = new HashMap();
	public static RenderHandler instance;
	private final int id;
	private final boolean renderingInInventory;

	public RenderHandler(int id, boolean flag) 
	{
		this.id = id;
		renderingInInventory = flag;
	}
	
	private RenderBase getInstance(Class<? extends RenderBase> clazz) throws InstantiationException, IllegalAccessException
	{
		if(!renderMap.containsKey(clazz))
		{
			RenderBase render;
			renderMap.put(clazz, render = clazz.newInstance());
			return render;
		}
		return renderMap.get(clazz);
	}

	public void register(Block block, int meta, Class<? extends RenderBase> clazz)
	{
		try
		{
			blockRenders.put(Block.blockRegistry.getNameForObject(block) + ":" + meta, getInstance(clazz));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private RenderBase getRenderFromBlockAndMeta(Block block, int meta)
	{
		RenderBase ret = blockRenders.get(Block.blockRegistry.getNameForObject(block) + ":" + meta);
		return ret == null ? blockRenders.get(Block.blockRegistry.getNameForObject(block) + ":" + OreDictionary.WILDCARD_VALUE) : ret;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks render1) 
	{
		RenderBase render = getRenderFromBlockAndMeta(block, metadata);
		if(render != null)
		{
			try
			{
				render.render(render1, block, metadata);
			}
			catch(Throwable e)
			{
				throw new RuntimeException("FLE render block in inventory error, place report this bug to ueyudiud.", e);
			}
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks render1) 
	{
		RenderBase render = getRenderFromBlockAndMeta(block, world.getBlockMetadata(x, y, z));
		if(render != null)
		{
			try
			{
				return render.render(render1, world, x, y, z);
			}
			catch(Throwable e)
			{
				throw new RuntimeException("FLE render block in world error, place report this bug to ueyudiud.", e);
			}
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) 
	{
		return renderingInInventory;
	}

	@Override
	public int getRenderId() 
	{
		return id;
	}
}