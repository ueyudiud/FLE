package fle.load;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.ITextureLoadListener;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class Icons implements ITextureLoadListener
{
	public static IIcon washing;

	@Override
	public void registerIcons(IIconRegister register)
	{
		washing = register.registerIcon("fle:washing");
	}
}
