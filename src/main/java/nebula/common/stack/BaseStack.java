/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.stack;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class BaseStack implements AbstractStack
{
	public static final BaseStack EMPTY = new BaseStack((ItemStack) null);
	
	public static BaseStack sizeOf(BaseStack stack, int size)
	{
		return size <= 0 ? null : new BaseStack(stack.item, size, stack.meta, stack.nbt);
	}
	
	public Item				item;
	public int				size;
	public int				meta;
	public NBTTagCompound	nbt;
	
	public BaseStack(String modid, String name, int size, int meta)
	{
		this(Item.REGISTRY.getObject(new ResourceLocation(modid, name)), size, meta, null);
	}
	
	public BaseStack(Block block)
	{
		this(block, 1);
	}
	
	public BaseStack(Block block, int size)
	{
		this(block, size, -1);
	}
	
	public BaseStack(Block block, int size, int meta)
	{
		this(Item.getItemFromBlock(block), size, meta);
	}
	
	public BaseStack(Item item)
	{
		this(item, 1);
	}
	
	public BaseStack(Item item, int size)
	{
		this(item, size, -1);
	}
	
	public BaseStack(@Nonnull Item item, int size, int meta)
	{
		this(Objects.requireNonNull(item), size, meta, null);
	}
	
	public BaseStack(@Nullable Item item, int size, int meta, @Nullable NBTTagCompound nbt)
	{
		this.item = item;
		this.size = size;
		this.meta = meta;
		this.nbt = nbt == null ? null : nbt.copy();
		if (this.item != null && ((this.item.getHasSubtypes() || this.item instanceof ItemBlock) && meta == OreDictionary.WILDCARD_VALUE))
		{
			this.meta = -1;
		}
	}
	
	public BaseStack(@Nullable ItemStack stack)
	{
		if (stack != null)
		{
			this.item = stack.getItem();
			this.size = stack.stackSize;
			this.meta = stack.getItemDamage();
			this.nbt = stack.getTagCompound();
		}
	}
	
	public BaseStack(ItemStack stack, int size)
	{
		this(stack, size, stack == null ? 0 : stack.getItemDamage());
	}
	
	public BaseStack(ItemStack stack, int size, int meta)
	{
		if (stack != null)
		{
			this.item = stack.getItem();
			this.size = size;
			this.meta = meta;
			this.nbt = stack.getTagCompound();
		}
		if (this.item != null && ((this.item.getHasSubtypes() || this.item instanceof ItemBlock) && meta == OreDictionary.WILDCARD_VALUE))
		{
			this.meta = -1;
		}
	}
	
	@Override
	public boolean similar(ItemStack stack)
	{
		return this.item == null ? stack == null : (stack != null && stack.getItem() == this.item &&
				(this.meta == -1 || stack.getItemDamage() == this.meta) && L.equals(stack.getTagCompound(), this.nbt));
	}
	
	@Override
	public boolean contain(ItemStack stack)
	{
		return similar(stack) && (this.item == null || this.size <= stack.stackSize);
	}
	
	@Override
	public int size(ItemStack stack)
	{
		return this.size;
	}
	
	@Override
	public AbstractStack split(ItemStack stack)
	{
		return sizeOf(this, this.size - stack.stackSize);
	}
	
	@Override
	public AbstractStack copyWithSize(int size)
	{
		return sizeOf(this, size);
	}
	
	@Override
	public ItemStack instance()
	{
		return this.item != null ? new ItemStack(this.item, this.size, this.meta) : null;
	}
	
	@Override
	public List<ItemStack> display()
	{
		return this.item != null ? ImmutableList.of(instance()) : ImmutableList.of();
	}
	
	@Override
	public boolean valid()
	{
		return this.item != null;
	}
	
	@Override
	public String toString()
	{
		return this.item == null ? "NULL" : "[" + this.item.getRegistryName() +
				(this.meta == -1 ? "" : ":" + this.meta) + "]" + "x" + this.size;
	}
	
	@Override
	public int hashCode()
	{
		return this.item == null ? 31 : this.item.hashCode() * 31 + this.size;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if ((obj == this) || (this.item == null && obj == EMPTY))
			return true;
		if (!(obj instanceof BaseStack))
			return false;
		BaseStack stack = (BaseStack) obj;
		return this.item == stack.item && this.size == stack.size &&
				this.meta == stack.meta && L.equals(this.nbt, stack.nbt);
	}
}
