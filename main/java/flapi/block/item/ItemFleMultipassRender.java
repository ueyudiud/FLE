package flapi.block.item;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import farcore.block.item.ItemBlockBase;
import farcore.render.block.ItemBlockRender;
import flapi.block.BlockFleMultipassRender;
import net.minecraft.block.Block;
import net.minecraftforge.client.MinecraftForgeClient;

public class ItemFleMultipassRender<B extends BlockFleMultipassRender> 
extends ItemBlockBase<B>
{
	public ItemFleMultipassRender(Block block)
	{
		super(block);
		MinecraftForgeClient.registerItemRenderer(this, ItemBlockRender.instance);
	}
}