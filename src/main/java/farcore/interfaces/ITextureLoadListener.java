package farcore.interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public interface ITextureLoadListener
{
	@SideOnly(Side.CLIENT)
	void registerIcons(IIconRegister register);
}