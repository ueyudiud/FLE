package fle.core.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.util.FleValue;

@SideOnly(Side.CLIENT)
public class RenderHandler implements ISimpleBlockRenderingHandler
{
	private static final Map<String, RenderBase> blockRenders = new HashMap();
	public static RenderHandler instance;
	private final boolean isNoInvRendering;

	public RenderHandler(boolean a) 
	{
		isNoInvRendering = a;
	}

	public static void register(Block block, int meta, Class<? extends RenderBase> clazz)
	{
		try
		{
			blockRenders.put(Block.blockRegistry.getNameForObject(block) + ":" + meta, clazz.newInstance());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void register(Item item, int meta, Class<? extends RIBase> clazz)
	{
		try
		{
			MinecraftForgeClient.registerItemRenderer(item, clazz.newInstance());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static RenderBase getRenderFromBlockAndMeta(Block block, int meta)
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
		return !isNoInvRendering;
	}

	@Override
	public int getRenderId() 
	{
		return isNoInvRendering ? FleValue.FLE_NOINV_RENDER_ID : FleValue.FLE_RENDER_ID;
	}
}