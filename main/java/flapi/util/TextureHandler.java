package flapi.util;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class TextureHandler<I extends AbstractTextureInfo>
{
	final ITextureHandler<I> h;
	final IIcon[] icons;

	public TextureHandler(ITextureHandler<I> handler)
	{
		this.h = handler;
		icons = new IIcon[handler.getLocateSize()];
	}
	
	public void registerIcon(IIconRegister register)
	{
		for(int i = 0; i < icons.length; ++i)
			icons[i] = register.registerIcon(h.getTextureFileName(i) + ":" + h.getTextureName(i));
	}
	
	public IIcon getIcon(I infomation)
	{
		return icons[h.getIconIndex(infomation)];
	}
	
	public int getColor(I infomation)
	{
		return 0xFFFFFF;
	}
}