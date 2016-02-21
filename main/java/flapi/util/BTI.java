package flapi.util;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class BTI extends AbstractTextureInfo
{
	public boolean isItem;
	public Block block;
	public IBlockAccess world;
	public int x, y, z;
	public int side;
	public int meta;
}