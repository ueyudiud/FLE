package farcore.lib.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotArmor extends SlotBase
{
	private EntityPlayer player;
	private int armorID;
	
	public SlotArmor(IInventory inventory, EntityPlayer player, int id, int x, int y, int armorID)
	{
		super(inventory, id, x, y);
		this.armorID = armorID;
		this.player = player;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack != null && stack.getItem().isValidArmor(stack, armorID, player);
	}
}