package flapi.item.interfaces;

import java.util.List;

import net.minecraft.item.ItemStack;
import flapi.world.ITEInWorld;

/**
 * The item handler when placed on the ground.
 * @author ueyudiud
 *
 */
public interface IPlacedHandler
{
	ItemStack onBlockRemove(ITEInWorld te, ItemStack target);
	
	ItemStack updatePlacedItem(ITEInWorld te, ItemStack target);

	void addPlaceInfomation(ITEInWorld tile,
			List<String> list, ItemStack stack);
}