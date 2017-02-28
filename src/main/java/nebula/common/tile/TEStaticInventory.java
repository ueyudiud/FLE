/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.tile;

import java.util.Arrays;

import farcore.data.V;
import nebula.common.data.NBTLSs;
import nebula.common.inventory.IBasicInventory;
import nebula.common.inventory.InventoryHelper;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BreakBlock;
import nebula.common.util.A;
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
public abstract class TEStaticInventory extends TEStatic
implements IBasicInventory, IInventory, ITB_BreakBlock
{
	protected final ItemStack[] stacks;
	protected String customName;
	
	protected TEStaticInventory(int invSize)
	{
		this.stacks = new ItemStack[invSize];
	}
	
	protected void onInventoryChanged(int index)
	{
		markDirty();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTs.setList(compound, "items", this.stacks, NBTLSs.ITEMSTACK_WRITER, true);
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
		NBTs.insertToList(compound, "items", this.stacks, NBTLSs.ITEMSTACK_READER, true);
		if (compound.hasKey("customName", NBT.TAG_STRING))
		{
			this.customName = compound.getString("customName");
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
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
		return hasCustomName() ? new TextComponentString(this.customName) :  new TextComponentTranslation(getName());
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
		Arrays.fill(this.stacks, null);
		onInventoryChanged(-1);
	}
	
	@Override
	public ItemStack[] toArray()
	{
		return A.transform(this.stacks, ItemStack.class, stack->ItemStack.copyItemStack(stack));
	}
	
	@Override
	public void fromArray(ItemStack[] stacks)
	{
		System.arraycopy(stacks, 0, this.stacks, 0, this.stacks.length);
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.stacks.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.stacks[index];
	}
	
	@Override
	public int insertStack(int index, ItemStack resource, boolean process)
	{
		int size = InventoryHelper.incrStack(this, index, false, resource, process, false);
		if (size != 0 && process)
		{
			onInventoryChanged(index);
		}
		return size;
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count, boolean process)
	{
		ItemStack result = ItemStacks.copyNomoreThan(this.stacks[index], count);
		if (result != null)
		{
			if (process)
			{
				if (this.stacks[index].stackSize == result.stackSize)
				{
					this.stacks[index] = null;
				}
				else
				{
					this.stacks[index].stackSize -= result.stackSize;
				}
				onInventoryChanged(index);
			}
			return result;
		}
		return null;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		ItemStack stack = this.stacks[index];
		this.stacks[index] = null;
		onInventoryChanged(index);
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.stacks[index] = ItemStack.copyItemStack(stack);
		onInventoryChanged(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return decrStackSize(index, count, true);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return V.GENERAL_MAX_STACK_SIZE;
	}
	
	@Override
	public void onBlockBreak(IBlockState state)
	{
		super.onBlockBreak(state);
		TileEntities.dropItemStacks(this);
	}
}