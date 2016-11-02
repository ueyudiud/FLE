package farcore.lib.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IIconRegister
{
	default TextureAtlasSprite registerIcon(String domain, String path)
	{
		return registerIcon(domain + ":" + path);
	}

	TextureAtlasSprite registerIcon(String key);
}