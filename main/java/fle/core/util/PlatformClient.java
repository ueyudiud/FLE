package fle.core.util;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlatformClient extends Platform
{
	public PlatformClient()
	{
		
	}
	
	@Override
	public EntityPlayer getPlayerInstance()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public World getWorldInstance(int aDimId)
	{
		if (isSimulating())
		{
			return super.getWorldInstance(aDimId);
		} 
		else
		{
			World world = Minecraft.getMinecraft().theWorld;
			return world == null ? null : world.provider.getDimensionId() != aDimId ? null : world;
		}
	}

	@Override
	public File getMinecraftDir()
	{
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public void playSoundSp(String sound, float f, float g)
	{
		//Or not...
	}
}