package fle.core.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import flapi.util.IPlatform;

public class Platform implements IPlatform
{
	@Override
	public boolean isSimulating()
	{
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}

	@Override
	public boolean isRendering()
	{
		return !isSimulating();
	}

	@Override
	public String getStackTrace(Exception e)
	{
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		e.printStackTrace(printWriter);
		return writer.toString();
	}

	@Override
	public EntityPlayer getPlayerInstance()
	{
		return null;
	}

	@Override
	public File getMinecraftDir()
	{
		return new File(".");
	}

	@Override
	public void playSoundSp(String s, float f1, float f2)
	{
	}

	@Override
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

	@Override
	public void removePotion(EntityLivingBase aEntity, int aPotion)
	{
		aEntity.removePotionEffect(aPotion);
	}

	@Override
	public World getWorldInstance(int dim) 
	{
		return DimensionManager.getWorld(dim);
	}
}