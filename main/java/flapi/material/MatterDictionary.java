package flapi.material;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import flapi.material.Matter.AtomStack;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.util.FleLog;
import flapi.util.SizeUtil;
import flapi.util.SizeUtil.I;
import flapi.util.SizeUtil.Size;

public class MatterDictionary
{
	private static final Map<ItemAbstractStack, I<Matter>> itemMatterList = new HashMap();
	private static final Map<Fluid, I<Matter>> fluidMatterList = new HashMap();
	private static final Map<Matter, EnumMap<Size, ItemStack>> matterToItemMap = new HashMap<Matter, EnumMap<Size,ItemStack>>();
	
	private static final Map<ItemAbstractStack, AtomStack> matterMap = new HashMap();
	private static final Map<Matter, Fluid> fluidMap = new HashMap();
	private static final Map<Fluid, Matter> fluidMatterMap = new HashMap();
	private static final Map<ItemAbstractStack, int[]> meltingRequireMap = new HashMap();
	private static final List<IFreezingRecipe> freezingRecipeMap = new ArrayList();

	public static void regMatter(Object obj, Matter matter)
	{
		regMatter(obj, SizeUtil.info(matter, 1));
	}
	public static void regMatter(Object obj, Matter matter, Size aSize)
	{
		regMatter(obj, SizeUtil.info(matter, 1, aSize));
	}
	public static void regMatter(Object obj, I<Matter> info)
	{
		if(obj instanceof Item)
			regMatter(new BaseStack((Item) obj), info);
		else if(obj instanceof Block)
			regMatter(new BaseStack((Block) obj), info);
		else if(obj instanceof ItemStack)
			regMatter(new BaseStack((ItemStack) obj), info);
		else if(obj instanceof Fluid)
		{
			if(fluidMatterList.containsKey(obj))
			{
				FleLog.getLogger().error("Fle API: Fluid " + ((Fluid) obj).getUnlocalizedName() + 
						" had alreadly register.");
				return;
			}
			fluidMatterList.put((Fluid) obj, info);
		}
		else if(obj instanceof ItemAbstractStack)
		{
			itemMatterList.put((ItemAbstractStack) obj, info);
		}
	}
	
	public static I<Matter> toMatterInfo(ItemStack aStack)
	{
		for(Entry<ItemAbstractStack, I<Matter>> e : itemMatterList.entrySet())
		{
			if(e.getKey().equal(aStack))
				return e.getValue().copy(aStack.stackSize);
		}
		return null;
	}
	
	public static AtomStack toMatter(ItemStack aStack)
	{
		I<Matter> i = toMatterInfo(aStack);
		if(i != null)
			return new AtomStack(i.getTarget(), i.getSize());
		return null;
	}
	public static ItemStack toItem(Matter stacks, Size size)
	{
		return matterToItemMap.containsKey(stacks) ? 
				!matterToItemMap.get(stacks).containsKey(size) ? null : 
					matterToItemMap.get(stacks).get(size).copy() : null;
	}
	
	public static void registerMatter(ItemAbstractStack aStack, Matter matter, int amount)
	{
		matterMap.put(aStack, new AtomStack(matter, amount));
	}
	public static void registerMatter(ItemAbstractStack aStack, Matter matter, int amount, int temNeed, int meltRequire)
	{
		matterMap.put(aStack, new AtomStack(matter, amount));
		meltingRequireMap.put(aStack, new int[]{temNeed, meltRequire});
	}
	
	public static void registerMatter(IFreezingRecipe recipe)
	{
		freezingRecipeMap.add(recipe);
	}
	
	public static void registerFluid(Fluid fluid, Matter matter)
	{
		if(fluidMap.containsKey(matter) || fluidMatterMap.containsKey(fluid))
		{
			FleLog.getLogger().error("Fle API: Fluid " + fluid.getUnlocalizedName() + " had alreadly register.");
			return;
		}
		fluidMap.put(matter, fluid);
		fluidMatterMap.put(fluid, matter);
	}
	
	public static AtomStack getMatter(ItemStack aStack)
	{
		for(ItemAbstractStack tStack : matterMap.keySet())
		{
			if(tStack.equal(aStack))
				return matterMap.get(tStack);
		}
		return null;
	}
	
	public static int[] getMeltRequires(ItemStack aStack)
	{
		for(ItemAbstractStack tStack : meltingRequireMap.keySet())
		{
			if(tStack.equal(aStack))
				return meltingRequireMap.get(tStack);
		}
		return new int[]{Integer.MAX_VALUE, -1};
	}
	
	public static FluidStack getMelting(ItemStack aStack)
	{
		for(ItemAbstractStack tStack : matterMap.keySet())
		{
			if(tStack.equal(aStack))
				return new FluidStack(getFluid((Matter) matterMap.get(tStack).get()), matterMap.get(tStack).size());
		}
		return null;
	}
	
	public static AtomStack getMatter(FluidStack aStack)
	{
		for(Fluid fluid : fluidMatterMap.keySet())
		{
			if(new FluidStack(fluid, 1).isFluidEqual(aStack))
				return new AtomStack(fluidMatterMap.get(fluid), aStack.amount);
		}
		return null;
	}
	
	public static Fluid getFluid(Matter matter)
	{
		return fluidMap.get(matter);
	}
	
	public static FluidStack getFluid(AtomStack matter)
	{
		return new FluidStack(fluidMap.get(matter.get()), matter.size());
	}
	
	public static IFreezingRecipe getFreeze(FluidStack aStack, IInventory aInv)
	{
		for(IFreezingRecipe recipe : freezingRecipeMap)
		{
			if(recipe.match(aStack, aInv)) return recipe;
		}
		return null;
	}
	
	public static interface IFreezingRecipe
	{
		public boolean match(FluidStack aStack, IInventory inv);
		
		public int getMatterRequire(FluidStack aStack, IInventory inv);
		
		public ItemStack getOutput(FluidStack aStack, IInventory inv);
	}

	public static List<IFreezingRecipe> getFreezeRecipes()
	{
		return freezingRecipeMap;
	}
}