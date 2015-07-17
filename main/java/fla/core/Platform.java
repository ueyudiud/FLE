package fla.core;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class Platform
{

	public Platform()
	{
	}

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

	public World getWorld(int dimId)
	{
		return DimensionManager.getWorld(dimId);
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

	public void resetPlayerInAirTime(EntityPlayer player)
	{
		if (!(player instanceof EntityPlayerMP))
		{
			return;
		} else
		{
			ObfuscationReflectionHelper.setPrivateValue(NetHandlerPlayServer.class, ((EntityPlayerMP)player).playerNetServerHandler, Integer.valueOf(0), new String[] {
				"field_147365_f", "floatingTickCount"
			});
			return;
		}
	}

	public int getBlockTexture(Block block, IBlockAccess world, int x, int i, int j, int k)
	{
		return 0;
	}

	public int addArmor(String name)
	{
		return 0;
	}

	public void removePotion(EntityLivingBase entity, int potion)
	{
		entity.removePotionEffect(potion);
	}
}
