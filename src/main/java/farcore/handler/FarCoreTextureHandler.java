package farcore.handler;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.render.IIconLoader;
import farcore.lib.render.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FarCoreTextureHandler implements IIconRegister
{
	private static final List<IIconLoader> LIST = new ArrayList();

	public static void addIconLoader(IIconLoader loader)
	{
		LIST.add(loader);
	}

	private TextureMap map;
	
	@SubscribeEvent
	public void onTexturesReoload(TextureStitchEvent.Pre event)
	{
		map = event.getMap();
		for(IIconLoader loader : LIST)
		{
			loader.registerIcon(this);
		}
		map = null;
	}
	
	@Override
	public TextureAtlasSprite registerIcon(String domain, String path)
	{
		return registerIcon(new ResourceLocation(domain, path));
	}
	
	@Override
	public TextureAtlasSprite registerIcon(String key)
	{
		return registerIcon(new ResourceLocation(key));
	}

	@Override
	public TextureAtlasSprite registerIcon(ResourceLocation location)
	{
		return map.registerSprite(location);
	}
}