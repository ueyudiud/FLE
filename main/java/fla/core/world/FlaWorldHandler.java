package fla.core.world;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FlaWorldHandler 
{
	private static final List<EventType> list = Arrays.asList(EventType.COAL, EventType.IRON, EventType.GOLD, EventType.LAPIS, EventType.DIAMOND, EventType.DIAMOND, EventType.REDSTONE, EventType.QUARTZ);
		
	@SubscribeEvent
	public void onOreGenerate(GenerateMinable evt)
	{
		if(list.contains(evt.type)) evt.setResult(Result.DENY);
	}

}
