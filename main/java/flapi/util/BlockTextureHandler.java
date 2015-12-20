package flapi.util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockTextureHandler extends TextureHandler<BTI>
{
	private final BTI blockTextureInfo = new BTI();
	
	public BlockTextureHandler(ITextureHandler<BTI> handler)
	{
		super(handler);
	}

	public IIcon getIcon(Block block, ItemStack stack)
	{
		blockTextureInfo.isItem = true;
		blockTextureInfo.block = block;
		blockTextureInfo.meta = stack.getItemDamage();
		blockTextureInfo.side = 3;
		return getIcon(blockTextureInfo);
	}

	public IIcon getIcon(Block block, int meta, int facing)
	{
		blockTextureInfo.isItem = true;
		blockTextureInfo.block = block;
		blockTextureInfo.meta = meta;
		blockTextureInfo.side = facing;
		return getIcon(blockTextureInfo);
	}

	public IIcon getIcon(Block block, IBlockAccess world, int x, int y, int z, int side)
	{
		blockTextureInfo.isItem = false;
		blockTextureInfo.block = block;
		blockTextureInfo.world = world;
		blockTextureInfo.x = x;
		blockTextureInfo.y = y;
		blockTextureInfo.z = z;
		blockTextureInfo.side = side;
		blockTextureInfo.meta = world.getBlockMetadata(x, y, z);
		return getIcon(blockTextureInfo);
	}

	public String getIconName(int pass)
	{
		return h.getTextureFileName(pass) + ":" + h.getTextureName(pass);
	}

	public int getRenderPasses()
	{
		return h.getLocateSize();
	}
}