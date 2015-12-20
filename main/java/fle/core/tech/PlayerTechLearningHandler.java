package fle.core.tech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import flapi.tech.Technology;
import flapi.tech.Technology.LearningType;
import fle.FLE;

/**
 * This system have not complete yet.
 * @author ueyudiud
 *
 */
public class PlayerTechLearningHandler 
{
	private static Map<LearningType, List<Technology>> techLearningList = new HashMap();
	
	public static void registerLearning(Technology tech)
	{
		if(tech.getLearningTypes() == null) return;
		for(int i = 0; i < tech.getLearningTypes().length; ++i)
		{
			if(!techLearningList.containsKey(tech.getLearningTypes()[i]))
				techLearningList.put(tech.getLearningTypes()[i], new ArrayList());
			techLearningList.get(tech.getLearningTypes()[i]).add(tech);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBreaking(BreakEvent evt)
	{
		checkEvent(evt, evt.getPlayer(), LearningType.Break);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onHarvest(HarvestDropsEvent evt)
	{
		checkEvent(evt, evt.harvester, LearningType.Hearvest);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onHurt(EntityItemPickupEvent evt)
	{
		checkEvent(evt, evt.entityPlayer, LearningType.Pickup);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onCrafting(ItemCraftedEvent evt)
	{
		checkEvent(evt, evt.player, LearningType.Crafting);
	}
	
	private void checkEvent(Event evt, EntityPlayer player, LearningType type)
	{
		if(!techLearningList.containsKey(type)) return;
		for(Technology tech : techLearningList.get(type))
		{
			if(tech.canBeLearned(FLE.fle.getTechManager().getPlayerInfo(player), type, evt))
			{
				FLE.fle.getTechManager().getPlayerInfo(player).setTech(tech);
			}
		}
	}
}