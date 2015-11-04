package fle.api.te;

import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.cover.Cover;

public interface ICoverTE
{
	Cover getCover(ForgeDirection dir);
	
	boolean addCover(ForgeDirection dir, Cover cover);
	
	@SideOnly(Side.CLIENT)
	void setCover(ForgeDirection dir, Cover cover);
	
	void removeCover(ForgeDirection dir);
}