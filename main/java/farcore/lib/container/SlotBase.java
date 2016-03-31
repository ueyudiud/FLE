package farcore.lib.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBase extends Slot
{
	public static SlotBase base(IInventory inventory, int id, int x, int y)
	{
		return new SlotBase(inventory, id, x, y);
	}
	public static SlotBase output(IInventory inventory, int id, int x, int y)
	{
		return new SlotOutput(inventory, id, x, y);
	}
	public static SlotBase holo(IInventory inventory, int id, int x, int y)
	{
		return new SlotHolo(inventory, id, x, y);
	}
	public static SlotBase armor(IInventory inventory, int id, int x, int y, EntityPlayer player)
	{
		return new SlotArmor(inventory, player, id, x, y, id);
	}
	
	private List<String> transferTarget = new ArrayList(4);
	
	public SlotBase(IInventory inventory, int id, int x, int y)
	{
		super(inventory, id, x, y);
	}
	
	public SlotBase addTransferTargets(String...strings)
	{
		for(int i = 0; i < strings.length; ++i)
		{
			if(strings[i] != null)
			{
				this.transferTarget.add(transferTarget.size(), strings[i]);
			}
		}
		return this;
	}
	
	public List<String> getTransferTarget()
	{
		return transferTarget;
	}
}