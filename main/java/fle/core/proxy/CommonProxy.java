package fle.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import fle.core.FleEventListener;
import fle.core.init.Entities;
import fle.core.init.IBF;
import fle.core.init.Lang;
import fle.core.init.Substances;
import fle.core.net.Network;

public class CommonProxy extends Proxy
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z)
	{
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z)
	{
		return null;
	}

	@Override
	public void onPreload()
	{
		Network.init();
		Substances.init();
		IBF.preinit();
	}

	@Override
	public void onLoad()
	{
		Substances.postInit();
		IBF.init();
		Entities.init();
	}

	@Override
	public void onPostload()
	{
		IBF.postinit();
	}

	@Override
	public void onCompleteLoad()
	{
		Lang.init();
		MinecraftForge.EVENT_BUS.register(new FleEventListener());
		FMLCommonHandler.instance().bus().register(new FleEventListener());
	}
	
	@Override
	public void onServerLoad()
	{
		
	}
}