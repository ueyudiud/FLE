package fle.api.util;

import java.io.File;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IPlatform 
{
	public boolean isSimulating();

	public boolean isRendering();

	public String getStackTrace(Exception aException);

	public EntityPlayer getPlayerInstance();

	public World getWorldInstance(int aDimId);
	
	public File getMinecraftDir();
	
	public void playSoundSp(String aSoundName, float f1, float f2);

	public void resetPlayerInAirTime(EntityPlayer aPlayer);

	public void removePotion(EntityLivingBase aEntity, int potion);
}