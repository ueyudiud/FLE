package farcore.block.interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import scala.inline;

@SideOnly(Side.CLIENT)
public interface ITextureSwitcher
{
	int getRenderPasses(IBlockAccess world, int x, int y, int z);
	
	int getRenderPasses(ItemStack stack);	
	
	void switchRender(int render);
	
	void setRenderToDefault();
}