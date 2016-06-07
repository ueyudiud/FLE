package farcore.interfaces.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IToolClickHandler
{	
	ItemStack onToolClick(ItemStack stack, EntityPlayer player, int id);
}