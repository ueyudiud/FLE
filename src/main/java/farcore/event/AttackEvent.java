/*
 * copyright© 2016 ueyudiud
 */

package farcore.event;

import farcore.data.EnumPhysicalDamageType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * Posted in MinecraftForge.EVENT_BUS
 * @author ueyudiud
 */
@Cancelable
public class AttackEvent extends net.minecraftforge.event.entity.player.PlayerEvent
{
	public final Entity target;
	public final ItemStack weapon;
	public ItemStack newWeapon;
	public final EnumPhysicalDamageType type;

	public AttackEvent(EntityPlayer player, Entity target, ItemStack weapon, EnumPhysicalDamageType type)
	{
		super(player);
		this.target = target;
		newWeapon = this.weapon = weapon;
		this.type = type;
	}

	public static AttackEvent post(EntityPlayer player, Entity target, ItemStack weapon, EnumPhysicalDamageType type)
	{
		AttackEvent event = new AttackEvent(player, target, weapon, type);
		MinecraftForge.EVENT_BUS.post(event);
		return event;
	}
}