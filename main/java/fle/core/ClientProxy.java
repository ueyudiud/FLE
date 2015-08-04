package fle.core;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.gui.GuiCondition;
import fle.api.util.ColorMap;
import fle.api.util.IColorMapHandler;
import fle.core.init.IB;
import fle.core.render.RenderHandler;
import fle.core.render.RenderOre;
import fle.core.util.FleColorMap;
import fle.core.util.FleTextureMap;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener, IColorMapHandler
{
	@SideOnly(Side.CLIENT)
	private IResourceManager resourceManager;
	
	@Override
	public void onPreload() 
	{
		super.onPreload();
    	((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}
	
	@Override
	public void onPostload() 
	{
		super.onPostload();
        FleValue.FLE_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
        RenderHandler.register(IB.ore, OreDictionary.WILDCARD_VALUE, RenderOre.class);
    	RenderingRegistry.registerBlockHandler(new RenderHandler());
	}
	
	@Override
	public void onCompleteLoad() 
	{
		super.onCompleteLoad();
		if(flag)
			onIconRegister();
	}
	
	boolean flag = false;

	@Override
	public void onIconRegister()
	{
		FleAPI.conditionIconRegister = new FleTextureMap("textures/condition");
		for(GuiCondition c : GuiCondition.register)
		{
			c.registerIcon(FleAPI.conditionIconRegister);
		}
		Minecraft.getMinecraft().renderEngine.loadTexture(FleAPI.conditionLocate, (FleTextureMap) FleAPI.conditionIconRegister);
	}

	@Override
	public void onResourceManagerReload(IResourceManager aManager)
	{
		resourceManager = aManager;
		flag = true;
	}

	@Override
	public ColorMap registerColorMap(String aResourceName) 
	{
		try
		{
			return new FleColorMap(TextureUtil.readImageData(resourceManager, new ResourceLocation(aResourceName)));
		}
		catch(Throwable e)
		{
			return new FleColorMap();
		}
	}
}