package fle.core;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Use Far core instead this class.
 * @author ueyudiud
 *
 */
@Deprecated
public class PlatformCommon
{
	public boolean isSimulating()
	{
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}

	public boolean isRendering()
	{
		return !isSimulating();
	}
	
	public String getStackTrace(Exception e)
	{
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		e.printStackTrace(printWriter);
		return writer.toString();
	}

	public EntityPlayer getPlayerInstance()
	{
		return null;
	}

	public void registerRenderers()
	{
	}

	public void profilerStartSection(String s)
	{
	}

	public void profilerEndSection()
	{
	}

	public void profilerEndStartSection(String s)
	{
	}

	public File getMinecraftDir()
	{
		return new File(".");
	}

	public void playSoundSp(String s, float f1, float f2)
	{
	}

	public void resetPlayerInAirTime(EntityPlayer aPlayer)
	{
		if (!(aPlayer instanceof EntityPlayerMP))
		{
			return;
		} 
		else
		{
			ObfuscationReflectionHelper.setPrivateValue(NetHandlerPlayServer.class, ((EntityPlayerMP)aPlayer).playerNetServerHandler, Integer.valueOf(0), new String[] {
				"field_147365_f", "floatingTickCount"
			});
			return;
		}
	}

	public void removePotion(EntityLivingBase aEntity, int aPotion)
	{
		aEntity.removePotionEffect(aPotion);
	}

	public World getWorldInstance(int aDimId) 
	{
		return DimensionManager.getWorld(aDimId);
	}
}