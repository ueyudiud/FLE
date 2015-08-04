package fle.api.util;

import net.minecraftforge.common.util.ForgeDirection;

public interface IBlockTextureManager extends ITextureLocation
{
	int getIconID(ForgeDirection dir);
}
