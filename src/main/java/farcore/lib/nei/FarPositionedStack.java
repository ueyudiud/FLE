package farcore.lib.nei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import codechicken.nei.PositionedStack;
import cpw.mods.fml.common.registry.GameData;
import farcore.enums.EnumItem;
import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.ArrayStack;
import farcore.lib.stack.BaseStack;
import farcore.util.U;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FarPositionedStack extends PositionedStack
{
	private static final Comparator<ItemStack> COMPARATOR = (ItemStack stack1, ItemStack stack2) ->
	{
		String s1 = GameData.getItemRegistry().getNameForObject(stack1.getItem());
		String s2 = GameData.getItemRegistry().getNameForObject(stack2.getItem());
		int i = s1.compareTo(s2);
		if(i != 0) return i;
		i = stack1.getItemDamage() - stack2.getItemDamage();
		if(i != 0) return i;
		
		return stack1.stackTagCompound.hashCode() - stack2.stackTagCompound.hashCode();
	};
	private static final ItemStack DEF = new ItemStack(Blocks.stone);
	public AbstractStack stack;
	private static boolean genFlag = false;
	private boolean sortList;

	public FarPositionedStack(FluidStack stack, int x, int y)
	{
		this(EnumItem.display_fluid.instance(1, stack), x, y);
	}
	public FarPositionedStack(ItemStack stack, int x, int y)
	{
		this(new BaseStack(stack), x, y);
	}
	public FarPositionedStack(List<ItemStack> stacks, int x, int y)
	{
		this(new ArrayStack(stacks), x, y);
	}
	public FarPositionedStack(AbstractStack stack, int x, int y)
	{
		this(stack, x, y, false);
	}
	@Deprecated
	public FarPositionedStack(AbstractStack stack, int x, int y, boolean genPerms) 
	{
		super(DEF, x, y, genPerms);
		this.stack = stack;
		this.sortList = genPerms;
		genFlag = true;
		generatePermutations();
		genFlag = false;
	}
	
	@Override
	public void generatePermutations()
	{
		if(!genFlag) return;
		items = U.Lang.cast(stack.display(), ItemStack.class);
		if(sortList)
		{
			genFlag = false;
			super.generatePermutations();
			genFlag = true;
			Arrays.sort(items, COMPARATOR);
		}
		for(int i = 0; i < items.length; 
				items[i] = U.Inventorys.valid(items[i]),
						++i);
		setPermutationToRender(0);
	}
	
	@Override
	public void setPermutationToRender(int index)
	{
		if(!genFlag) return;
		item = U.Inventorys.valid(stack.instance());
		if(item == null)
		{
			item = new ItemStack(Blocks.fire);
		}
	}
	
	@Override
	public boolean contains(ItemStack arg0)
	{
		return stack.similar(arg0);
	}
	
	@Override
	public boolean contains(Item arg0)
	{
		return super.contains(arg0);
	}
	
	@Override
	public void setMaxSize(int size)
	{
		stack = U.Inventorys.sizeOf(stack, size);
		generatePermutations();
	}
}