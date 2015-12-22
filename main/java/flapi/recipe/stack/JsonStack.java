package flapi.recipe.stack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cpw.mods.fml.common.registry.GameData;
import flapi.collection.abs.AbstractStack;

public class JsonStack extends ItemAbstractStack
{
	@Expose(serialize = false, deserialize = false)
	public ItemAbstractStack parent;
	StackInfomation infomation;
	
	public JsonStack(StackInfomation infomation)
	{
		this.infomation = infomation;
		if(infomation.oreName != null && infomation.itemName != null)
			throw new RuntimeException();
		if(infomation.size == 0) infomation.size = 1;
		if(infomation.oreName != null)
		{
			parent = new OreStack(infomation.oreName, infomation.size);
		}
		else if(infomation.itemName != null)
		{
			parent = new BaseStack(
					GameData.getItemRegistry().getObject(infomation.itemName), infomation.size, 
					infomation.damage == -1 ? OreDictionary.WILDCARD_VALUE : infomation.damage);
		}
		else if(infomation.id != 0)
		{
			parent = new BaseStack(
					GameData.getItemRegistry().getObjectById(infomation.id), infomation.size, 
					infomation.damage == -1 ? OreDictionary.WILDCARD_VALUE : infomation.damage);
		}
		else
		{
			parent = null;
		}
	}
	public JsonStack(ItemStack stack)
	{
		this(new BaseStack(stack));
	}
	public JsonStack(ItemAbstractStack stack)
	{
		parent = stack;
		infomation = new StackInfomation();
		if(stack instanceof BaseStack)
		{
			ItemStack i = ((BaseStack) stack).stack;
			infomation.itemName = GameData.getItemRegistry().getNameForObject(i.getItem());
			infomation.damage = i.getItemDamage() == OreDictionary.WILDCARD_VALUE ? -1 : i.getItemDamage();
			infomation.size = i.stackSize;
		}
		else if(stack instanceof OreStack)
		{
			infomation.oreName = ((OreStack) stack).name;
			infomation.size = ((OreStack) stack).size;
		}
		else if(stack == null)
		{
			;
		}
		else throw new RuntimeException();
	}
	
	public StackInfomation getInfomation()
	{
		return infomation;
	}
	
	public static class StackInfomation
	{
		@Expose
		@SerializedName("ore")
		public String oreName;
		@Expose(serialize = false)
		@SerializedName("itemID")
		public int id;
		@Expose
		@SerializedName("itemName")
		public String itemName;
		@Expose
		@SerializedName("itemDamage")
		public int damage;
		@Expose
		@SerializedName("itemSize")
		public int size;
		
		public ItemAbstractStack toStack()
		{
			return oreName == null && id == 0 && itemName == null ? null : new JsonStack(this).parent;
		}
		
		public ItemStack getStack()
		{
			ItemAbstractStack stack =toStack();
			if(stack == null || stack.toList().length == 0) return null;
			ItemStack ret = stack.toList()[0].copy();
			if(ret.getItemDamage() == OreDictionary.WILDCARD_VALUE)
				ret.setItemDamage(0);
			return ret;
		}

		public boolean hasStack()
		{
			return oreName != null || id == 0 || itemName != null;
		}
	}
	
	@Override
	public boolean equal(ItemStack arg)
	{
		return parent == null ? arg == null :  parent.equal(arg);
	}

	@Override
	public boolean equal(AbstractStack<ItemStack> arg)
	{
		return parent == null ? arg == null || (arg instanceof JsonStack && ((JsonStack) arg).parent == null) : parent.equal(arg);
	}

	@Override
	public boolean contain(AbstractStack<ItemStack> arg)
	{
		return parent == null ? false : parent.contain(arg);
	}

	@Override
	public ItemStack[] toList()
	{
		return parent == null ? new ItemStack[0] : parent.toList();
	}
	
	public boolean hasStack()
	{
		return parent == null ? false : parent.toList().length != 0;
	}
}