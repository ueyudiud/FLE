/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.event;

import farcore.data.EnumPhysicalDamageType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * Called before use Far Core weapon attack entity. Posted in
 * {@link MinecraftForge#EVENT_BUS}
 * 
 * @author ueyudiud
 */
@Cancelable
public class AttackEvent extends PlayerEvent
{
	public final Entity					target;
	public final ItemStack				weapon;
	public ItemStack					newWeapon;
	public final EnumPhysicalDamageType	type;
	
	public AttackEvent(EntityPlayer player, Entity target, ItemStack weapon, EnumPhysicalDamageType type)
	{
		super(player);
		this.target = target;
		this.newWeapon = this.weapon = weapon;
		this.type = type;
	}
	
	public static AttackEvent post(EntityPlayer player, Entity target, ItemStack weapon, EnumPhysicalDamageType type)
	{
		AttackEvent event = new AttackEvent(player, target, weapon, type);
		MinecraftForge.EVENT_BUS.post(event);
		return event;
	}
}
