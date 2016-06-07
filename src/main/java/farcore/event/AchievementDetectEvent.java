package farcore.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Achievement detect event, suggested use when item crafted,
 * hit entity, or achieve some goal, etc.<br>
 * Not only use in work bench, furnace, pickup item.<br>
 * You can post this event in custom crafting, some tool using
 * on entity.
 * @author ueyudiud
 *
 */
public abstract class AchievementDetectEvent extends Event
{
	public enum Type
	{
		OPEN,
		FIND,
		CRAFTED,
		PICKUP,
		HIT,
		ATTACK,
		KILL,
		EXCHANGE,//Acted when right click entity (EX: Villager to buy something).
		INTO;
	}
	
	public final EntityPlayer player;
	public final Type type;
	
	public AchievementDetectEvent(EntityPlayer player, Type type)
	{
		this.player = player;
		this.type = type;
	}
	
	public static class Item extends AchievementDetectEvent
	{
		public ItemStack stack;
		
		public Item(EntityPlayer player, Type type, ItemStack stack)
		{
			super(player, type);
			this.stack = stack;
		}
	}
	
	public static class Exchange extends AchievementDetectEvent
	{
		public Entity entity;
		
		public Exchange(EntityPlayer player, Type type, Entity entity)
		{
			super(player, type);
			this.entity = entity;
		}
	}
}