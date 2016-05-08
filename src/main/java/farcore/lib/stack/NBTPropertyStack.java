package farcore.lib.stack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.nbt.NBTProperty;
import farcore.util.U;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A stack with NBT property.
 * @author ueyudiud
 *
 */
public class NBTPropertyStack implements AbstractStack
{
	public static NBTPropertyStack sizeOf(NBTPropertyStack stack, int size)
	{
		return size <= 0 ? null :
			new NBTPropertyStack(U.Inventorys.sizeOf(stack.parent, size), stack.properties);
	}
	
	AbstractStack parent;
	List<NBTProperty> properties;
	List<ItemStack> displays;
	ItemStack instance;

	public NBTPropertyStack(AbstractStack stack, NBTProperty...properties)
	{
		this(stack, ImmutableList.copyOf(properties));
	}
	public NBTPropertyStack(AbstractStack stack, List<NBTProperty> properties)
	{
		if(stack instanceof NBTPropertyStack)
		{
			NBTPropertyStack stack2 = (NBTPropertyStack) stack;
			List<NBTProperty> list = new ArrayList();
			list.addAll(stack2.properties);
			list.addAll(properties);
			this.parent = stack2.parent;
			this.properties = ImmutableList.copyOf(list);
		}
		else
		{
			this.parent = stack;
			this.properties = properties;
		}
	}
	
	@Override
	public boolean similar(ItemStack stack)
	{
		return parent.similar(stack);
	}

	@Override
	public boolean contain(ItemStack stack)
	{
		if(!contain(stack)) return false;
		NBTTagCompound nbt = stack.hasTagCompound() ?
				stack.getTagCompound() : new NBTTagCompound();
		for(NBTProperty property : properties)
		{
			if(!property.isTrue(nbt))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public int size(ItemStack stack)
	{
		return parent.size(stack);
	}
	
	@Override
	public AbstractStack split(ItemStack stack)
	{
		return new NBTPropertyStack(parent.split(stack), properties);
	}

	@Override
	public ItemStack instance()
	{
		if(instance == null)
		{
			instance = parent.instance();
			if(instance == null) return null;
			NBTTagCompound nbt = instance.stackTagCompound;
			if(!instance.hasTagCompound())
			{
				nbt = instance.stackTagCompound = new NBTTagCompound();
			}
			for(NBTProperty property : properties)
			{
				property.addInstanceTag(nbt);
			}
		}
		return instance.copy();
	}

	@Override
	public List<ItemStack> display()
	{
		if(displays == null)
		{
			List<ItemStack> resource = parent.display();
			if(resource == null) return null;
			List<ItemStack> target = new ArrayList();
			for(ItemStack stack : resource)
			{
				if(stack == null) continue;
				NBTTagCompound nbt = stack.stackTagCompound;
				if(!stack.hasTagCompound())
				{
					nbt = stack.stackTagCompound = new NBTTagCompound();
				}
				for(NBTProperty property : properties)
				{
					property.addInstanceTag(nbt);
				}
				target.add(stack);
			}
			displays = ImmutableList.copyOf(target);
		}
		return displays;
	}

	@Override
	public boolean valid()
	{
		return parent.valid();
	}

	@Override
	public boolean useContainer()
	{
		return parent.useContainer();
	}
}