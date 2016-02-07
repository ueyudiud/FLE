package farcore.render.block;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.util.Values;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;

@SideOnly(Side.CLIENT)
public class CustomRenderHandler implements ISimpleBlockRenderingHandler
{
	private static final Map<String, BlockRenderBase> blockRenders = new HashMap();
	private final boolean isNoInvRendering;

	public CustomRenderHandler(boolean a) 
	{
		isNoInvRendering = a;
	}

	public static void register(Block block, int meta, Class<? extends BlockRenderBase> clazz)
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
	
	private static BlockRenderBase getRenderFromBlockAndMeta(Block block, int meta)
	{
		BlockRenderBase ret = blockRenders.get(Block.blockRegistry.getNameForObject(block) + ":" + meta);
		return ret == null ? blockRenders.get(Block.blockRegistry.getNameForObject(block) + ":" + OreDictionary.WILDCARD_VALUE) : ret;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks render1) 
	{
		BlockRenderBase render = getRenderFromBlockAndMeta(block, metadata);
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
		BlockRenderBase render = getRenderFromBlockAndMeta(block, world.getBlockMetadata(x, y, z));
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
		return Values.FLE_CUSTOM_RENDER_ID;
	}
}