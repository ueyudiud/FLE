package flapi.recipe.stack;

import java.util.Arrays;
import java.util.List;

import farcore.collection.abs.AStack;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class BaseStack extends TemplateStack
{
	public static final BaseStack NULL = new BaseStack();
	
	public boolean checkNBT;
	public ItemStack stack;
	
	private BaseStack(){}
	public BaseStack(Item item                      ){this(item, OreDictionary.WILDCARD_VALUE);}
	public BaseStack(Item item  ,           int meta){this(new ItemStack(item, 1, meta));}
	public BaseStack(Item item  , int size, int meta){this(new ItemStack(item, size, meta));}
	public BaseStack(Block block                    ){this(block, OreDictionary.WILDCARD_VALUE);}
	public BaseStack(Block block,           int meta){this(new ItemStack(block, 1, meta));}
	public BaseStack(Block block, int size, int meta){this(new ItemStack(block, size, meta));}
	public BaseStack(ItemStack item){this(item, false);}
	public BaseStack(ItemStack item, boolean checkNBT)
	{
		if(item != null)
			stack = item.copy();
		this.checkNBT = checkNBT;
	}
	
	@Override
	public boolean similar(ItemStack stack)
	{
		return U.I.equal(this.stack, stack, checkNBT, true);
	}
	
	@Override
	public int size(ItemStack stack)
	{
		return this.stack == null ? 0 : this.stack.stackSize;
	}
	
	@Override
	protected List<ItemStack> create()
	{
		return Arrays.asList(U.I.copy(stack));
	}
	
	@Override
	public ItemStack instance()
	{
		return U.I.copy(stack);
	}
	
	@Override
	public String toString()
	{
		return "{stack:" + stack + "}";
	}
}