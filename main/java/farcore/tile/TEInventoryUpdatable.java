package farcore.tile;

import java.util.Arrays;

import farcore.tile.inventory.ISmartInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TEInventoryUpdatable
extends TEUpdatable implements ISmartInventory
{
	ItemStack[] stacks;
	
	public TEInventoryUpdatable(int size)
	{
		this.stacks = new ItemStack[size];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < stacks.length; ++i)
		{
			if(stacks[i] == null) continue;
			NBTTagCompound nbt1 = stacks[i].writeToNBT(new NBTTagCompound());
			nbt1.setByte("SlotId", (byte) i);
			list.appendTag(nbt1);
		}
		nbt.setTag("Slots", list);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Slots", 10);
		int i;
		Arrays.fill(stacks, null);
		for(i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			int slotId = nbt1.getByte("SlotId");
			stacks[slotId] = ItemStack.loadItemStackFromNBT(nbt1);
		}
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 16D;
	}
	
	@Override
	public ItemStack[] getStacks()
	{
		return stacks;
	}
}