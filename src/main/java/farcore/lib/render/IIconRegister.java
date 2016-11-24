package farcore.lib.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IIconRegister
{
	TextureAtlasSprite registerIcon(String domain, String path);
	
	TextureAtlasSprite registerIcon(ResourceLocation location);

	TextureAtlasSprite registerIcon(String key);
}