package farcore.handler;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import farcore.event.AchievementDetectEvent;
import farcore.event.AchievementDetectEvent.Type;
import farcore.lib.stack.AbstractStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraftforge.common.MinecraftForge;

public class FarCoreAchievementHandler
{
	public static Map<Type, Map<AbstractStack, StatBase>> stackAchievement = new EnumMap(Type.class);

	public static void addAchievementRequirement(StatBase achi, Type type, AbstractStack stack)
	{
		if(!stackAchievement.containsKey(type))
		{
			stackAchievement.put(type, new HashMap());
		}
		stackAchievement.get(type).put(stack, achi);
	}
	
	public static void postCrafted(EntityPlayer player, ItemStack stack)
	{
		if(player != null && stack != null)
		{
			MinecraftForge.EVENT_BUS.post(new AchievementDetectEvent.Item(player, Type.CRAFTED, stack));
		}
	}
	
	@SubscribeEvent
	public void onItemPickup(ItemPickupEvent event)
	{
		detectItemAndMark(event.player, stackAchievement.get(Type.PICKUP), event.pickedUp.getEntityItem());
	}
		
	@SubscribeEvent
	public void onAchievementDetect(AchievementDetectEvent.Item event)
	{
		switch (event.type)
		{
		case CRAFTED:
			detectItemAndMark(event.player, stackAchievement.get(event.type), ((AchievementDetectEvent.Item) event).stack);
			break;
		case PICKUP:
			detectItemAndMark(event.player, stackAchievement.get(event.type), ((AchievementDetectEvent.Item) event).stack);
			break;
		default:
			break;
		}
	}
	
	private void detectItemAndMark(EntityPlayer player, Map<AbstractStack, StatBase> map, ItemStack target)
	{
		if(map == null) return;
		for(Entry<AbstractStack, StatBase> entry : map.entrySet())
		{
			if(entry.getKey().similar(target))
			{
				player.addStat(entry.getValue(), 1);
			}
		}
	}
}