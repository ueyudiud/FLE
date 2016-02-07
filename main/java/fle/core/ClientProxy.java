package fle.core;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.util.ColorMap;
import farcore.util.FleLog;
import farcore.util.IColorMapHandler;
import farcore.util.IColorMapProvider;
import fle.core.enums.EnumDirtState;
import fle.core.init.Renders;
import fle.core.init.Rs;
import fle.core.util.FleColorMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener, IColorMapHandler
{
	private List<IColorMapProvider> colorMapProviderList = new ArrayList();
	
	public ClientProxy() 
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
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
		Renders.init();
	}
	
	@Override
	public void onCompleteLoad() 
	{
		super.onCompleteLoad();
		Renders.postInit();
		Rs.completeInit();
	}

	@Override
	public void onIconRegister()
	{
//		FleTextureMap map = new FleTextureMap("textures/condition");
//		for(GuiCondition c : GuiCondition.register)
//		{
//			c.registerIcon(map);
//		}
//		Minecraft.getMinecraft().renderEngine.loadTexture(FleValue.conditionLocate, map);
	}
	
	private ThreadLocal<IResourceManager> resourceManager = new ThreadLocal();

	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		FleLog.info("Fle start reload color map.");
		int i = 0;
		resourceManager.set(manager);
		for(IColorMapProvider provider : colorMapProviderList)
		{
			try
			{
				provider.registerColorMap(this);
			}
			catch(Exception exception)
			{
				FleLog.addExceptionToCache(exception);
			}
			++i;
		}
		resourceManager.set(null);
		FleLog.info("Loaded " + i + " color maps.");
		FleLog.resetAndCatchException("Catching exception during reload color maps.");
	}
	
	@Override
	public ColorMap registerColorMap(String name)
	{
		if(resourceManager.get() == null)
			throw new RuntimeException("It is not loading resource time, some mod register color map in wrong time!");
		try
		{
			return new FleColorMap(
					TextureUtil.readImageData(
							resourceManager.get(), 
							new ResourceLocation(name + ".png")));
		}
		catch(Throwable e)
		{
			return new FleColorMap();
		}
	}
	
	@Override
	public void addColorMapProvider(IColorMapProvider provider)
	{
		if(!colorMapProviderList.contains(provider))
			colorMapProviderList.add(provider);
		else 
			throw new RuntimeException("Already registered this provider.");
	}
	
//	@SubscribeEvent
//	public void onFluidIconRegister(FluidIconRegisterEvent evt)
//	{
//		for(FluidBase tFluid : FluidBase.register)
//		{
//			tFluid.registerIcon(evt.register);
//		}
//		for(Solid tSoild : SolidRegistry.getSolidList())
//		{
//			tSoild.registerIcon(evt.register);
//		}
//		for(Cover tCover : CoverRegistry.getCoverRegister())
//		{
//			tCover.registerIcon(evt.register);
//		}
//	}
}