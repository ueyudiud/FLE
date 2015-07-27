package fle.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container
{
	protected InventoryPlayer inventoryPlayer;
	
	public ContainerBase(EntityPlayer aPlayer) 
	{
		this(aPlayer.inventory);
	}
	public ContainerBase(InventoryPlayer aPlayer) 
	{
		inventoryPlayer = aPlayer;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer aPlayer)
	{
		return false;
	}
	
	public class TransLocation
	{
		String name;
		int startId;
		int endId;

		public TransLocation(String name, int id)
		{
			this.name = name;
			this.startId = id;
			this.endId = id + 1;
		}
		public TransLocation(String name, int startId, int endId)
		{
			this.name = name;
			this.startId = startId;
			this.endId = endId;
		}
		
		public String getName()
		{
			return name;
		}
		
		public boolean conrrect(int i)
		{
			return i >= startId && i < endId;
		}
		
		public boolean mergeItemStack(ItemStack itemstack, boolean isFront)
		{
			return ContainerBase.this.mergeItemStack(itemstack, startId, endId, isFront);
		}
	}
}