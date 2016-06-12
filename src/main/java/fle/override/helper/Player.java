package fle.override.helper;

import java.util.Arrays;

import farcore.util.FarFoodStats;
import farcore.util.FleLog;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;

public class Player
{
	public static void initStat(EntityPlayer player)
	{
		try
		{
			U.Reflect.overrideField(EntityPlayer.class, Arrays.asList("foodStats", "field_71100_bB"), player, 
					new FarFoodStats(), true);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
}