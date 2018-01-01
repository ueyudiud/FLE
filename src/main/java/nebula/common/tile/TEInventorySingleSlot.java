/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tile;

import farcore.data.V;
import nebula.common.inventory.IBasicInventory;
import nebula.common.inventory.InventoryHelper;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BreakBlock;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import nebula.common.util.TileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author ueyudiud
 */
public class TEInventorySingleSlot extends TESynchronization implements IBasicInventory, IInventory, ITB_BreakBlock
{
	protected String	customName;
	protected ItemStack	stack;
	
	protected void onInventoryChanged()
	{
		markDirty();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTs.setItemStack(compound, "item", this.stack, false);
		if (this.customName != null)
		{
			compound.setString("customName", this.customName);
		}
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.stack = NBTs.getItemStackOrDefault(compound, "item", null);
		if (compound.hasKey("customName", NBT.TAG_STRING))
		{
			this.customName = compound.getString("customName");
		}
	}
	
	@Override
	public boolean isValidForSlot(int index, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean hasCustomName()
	{
		return this.customName != null;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation(getName());
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return player.getDistanceSq(this.pos) < 64.0;
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
		
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
		
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
		
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		this.stack = null;
		onInventoryChanged();
	}
	
	@Override
	public ItemStack[] toArray()
	{
		return new ItemStack[] { this.stack.copy() };
	}
	
	@Override
	public void fromArray(ItemStack[] stacks)
	{
		if (stacks.length > 0)
		{
			this.stack = stacks[0];
		}
		else
		{
			this.stack = null;
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return 1;
	}
	
	public ItemStack getStack()
	{
		return this.stack;
	}
	
	@Override
	public final ItemStack getStackInSlot(int index)
	{
		return getStack();
	}
	
	@Override
	public ItemStack getStack(int index)
	{
		return this.stack;
	}
	
	@Override
	public int incrItem(int index, ItemStack resource, boolean process)
	{
		int size = InventoryHelper.incrStack(this, index, false, resource, process, false);
		if (size != 0 && process)
		{
			onInventoryChanged();
		}
		return size;
	}
	
	@Override
	public ItemStack decrItem(int index, int count, boolean process)
	{
		ItemStack result = ItemStacks.copyNomoreThan(this.stack, count);
		if (result != null)
		{
			if (process)
			{
				if (this.stack.stackSize == result.stackSize)
				{
					this.stack = null;
				}
				else
				{
					this.stack.stackSize -= result.stackSize;
				}
				onInventoryChanged();
			}
			return result;
		}
		return null;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		ItemStack stack = this.stack;
		this.stack = null;
		onInventoryChanged();
		return stack;
	}
	
	@Override
	public void setSlotContents(int index, ItemStack stack)
	{
		this.stack = ItemStack.copyItemStack(stack);
		onInventoryChanged();
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return decrItem(index, count, true);
	}
	
	@Override
	public int getStackLimit()
	{
		return V.GENERAL_MAX_STACK_SIZE;
	}
	
	@Override
	public void onBlockBreak(IBlockState state)
	{
		super.onBlockBreak(state);
		TileEntities.dropItemStacks(this);
	}
	
	@Override
	public final boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return isValidForSlot(index, stack);
	}
	
	@Override
	public final int getInventoryStackLimit()
	{
		return getStackLimit();
	}
	
	@Override
	public final void setInventorySlotContents(int index, ItemStack stack)
	{
		setSlotContents(index, stack);
	}
}
