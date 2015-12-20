package flapi.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemTextureHandler extends TextureHandler<ITI>
{
	private final ITI itemTextureInfo = new ITI();
	
	public ItemTextureHandler(ITextureHandler<ITI> handler)
	{
		super(handler);
	}

	public boolean requireMultipleRenderPasses()
	{
		return h.getLocateSize() > 1;
	}

	public int getRenderPasses(int metadata)
	{
		return h.getLocateSize();
	}

	public IIcon getIcon(ItemStack stack)
	{
		itemTextureInfo.stack = stack;
		itemTextureInfo.pass = 0;
		return getIcon(itemTextureInfo);
	}

	public IIcon getIcon(ItemStack stack, int pass)
	{
		itemTextureInfo.stack = stack;
		itemTextureInfo.pass = pass;
		return getIcon(itemTextureInfo);
	}
	
	public int getColor(ItemStack stack, int pass)
	{
		itemTextureInfo.stack = stack;
		itemTextureInfo.pass = pass;
		return getColor(itemTextureInfo);
	}
}