package flapi.chem;

import java.util.ArrayList;
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
import net.minecraftforge.oredict.OreDictionary;
import flapi.chem.base.IChemCondition;
import flapi.chem.base.IMatterInputHatch;
import flapi.chem.base.Matter;
import flapi.chem.base.MatterStack;
import flapi.chem.base.Part;
import flapi.collection.Register;
import flapi.recipe.SingleMatterOutputRecipe;
import flapi.recipe.StandardSingleMatterOutputRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.util.FleLog;

public class MatterDictionary
{
	private static final List<SingleMatterOutputRecipe> itemMatterList = new ArrayList();
	private static final Map<Fluid, MatterStack> fluidMatterList = new HashMap();
	private static final Map<MatterStack, ItemAbstractStack> matterToItemMap = new HashMap();
	
	private static final Map<Matter, Fluid> fluidMap = new HashMap();
	private static final Register<MeltingRecipe> meltingRequireMap = new Register();
	private static final List<IFreezingRecipe> freezingRecipeMap = new ArrayList();

	public static void registerMatter(Fluid fluid, Matter matter)
	{
		registerMatter(fluid, matter, Part.part("fluidy", 1));
	}
	public static void registerMatter(Fluid fluid, Matter matter, Part part)
	{
		if(fluidMatterList.containsKey(fluid) || fluidMap.containsKey(matter))
		{
			FleLog.getLogger().error("Fle API: Fluid " + fluid.getUnlocalizedName() + 
					" had alreadly register.");
			return;
		}
		fluidMatterList.put(fluid, new MatterStack(matter, part));
		fluidMap.put(matter, fluid);
	}
	public static void registerMatter(Item item, Matter matter, Part part)
	{
		registerMatter(new BaseStack(item), new MatterStack(matter, part));
	}
	public static void registerMatter(ItemStack item, Matter matter, Part part)
	{
		registerMatter(new BaseStack(item), new MatterStack(matter, part));
	}
	public static void registerMatter(Block block, Matter matter, Part part)
	{
		registerMatter(new BaseStack(block), new MatterStack(matter, part));
	}
	public static void registerMatter(ItemAbstractStack stack, Matter matter, Part part)
	{
		registerMatter(stack, new MatterStack(matter, part));
	}
	public static void registerMatter(ItemAbstractStack stack, MatterStack matter)
	{
		if(itemMatterList.contains(stack) || matterToItemMap.containsKey(matter))
		{
			FleLog.getLogger().error("Fle API: Item " + stack.toString() + 
					" had alreadly register.");
			return;
		}
		itemMatterList.add(new StandardSingleMatterOutputRecipe(stack, matter));
		matterToItemMap.put(matter, stack);
	}
	public static void registerMatter(SingleMatterOutputRecipe recipe)
	{
		if(itemMatterList.contains(recipe))
		{
			FleLog.getLogger().error("Fle API: Item " + recipe.toString() + 
					" had alreadly register.");
			return;
		}
		itemMatterList.add(recipe);
	}
	
	public static void registerMatter(MeltingRecipe recipe)
	{
		meltingRequireMap.register(recipe, recipe.getName());
	}
	
	public static void registerMatter(IFreezingRecipe recipe)
	{
		freezingRecipeMap.add(recipe);
	}
	
	private static ItemStack a(ItemAbstractStack stack)
	{
		if(stack == null) return null;
		ItemStack[] list = stack.toList();
		if(list == null || list.length == 0 || list[0] == null) return null;
		ItemStack i = list[0].copy();
		if(i.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			i.setItemDamage(0);
		return i;
	}
	
	public static ItemStack toItem(Matter matter, Part part)
	{
		if(matter == null) return null;
		MatterStack stack1 = new MatterStack(matter, part);
		return a(matterToItemMap.get(stack1));
	}
	
	public static ItemStack toItem(MatterStack stack)
	{
		if(stack == null) return null;
		MatterStack stack1 = stack.copy();
		stack1.size = 1;
		if(!matterToItemMap.containsKey(stack1)) return null; 
		ItemStack item = a(matterToItemMap.get(stack1));
		item.stackSize *= stack.size;
		return item;
	}
	
	public static MatterStack toMatter(ItemStack stack)
	{
		if(stack == null) return null;
		for(SingleMatterOutputRecipe recipe : itemMatterList)
		{
			if(recipe.matchInput(stack))
			{
				return recipe.getOutput(stack);
			}
		}
		return null;
	}
	
	public static MatterStack toMatter(FluidStack stack)
	{
		if(stack == null) return null;
		for(Entry<Fluid, MatterStack> entry : fluidMatterList.entrySet())
		{
			if(new FluidStack(entry.getKey(), 1).isFluidEqual(stack))
			{
				MatterStack ret = entry.getValue().copy();
				ret.size *= stack.amount;
				return ret;
			}
		}
		return null;
	}

	public static Fluid toFluid(Matter matter)
	{
		return matter == null ? null : fluidMap.get(matter);
	}

	public static MeltingRecipe getMeltingRecipe(IChemCondition condition, IMatterInputHatch hatch)
	{
		for(MeltingRecipe recipe : meltingRequireMap)
		{
			if(ChemReaction.match(condition, hatch, recipe.input) != -1)
				return recipe;
		}
		return null;
	}
	
	public static Object[] getAndInputMeltingRecipe(IChemCondition condition, IMatterInputHatch hatch)
	{
		for(MeltingRecipe recipe : meltingRequireMap)
		{
			int i;
			if((i = ChemReaction.matchAndInputRecipe(condition, hatch, recipe.input)) != -1)
			{
				return new Object[]{recipe, new Integer(i)};
			}
		}
		return null;
	}
	
	public static MeltingRecipe getMeltingRecipe(String name)
	{
		return meltingRequireMap.get(name);
	}
	
	public static IFreezingRecipe getFreeze(FluidStack stack, IInventory inv)
	{
		for(IFreezingRecipe recipe : freezingRecipeMap)
		{
			if(recipe.match(stack, inv)) return recipe;
		}
		return null;
	}
	
	public static interface IFreezingRecipe
	{
		boolean match(FluidStack stack, IInventory inv);
		
		int getMatterRequire(FluidStack stack, IInventory inv);
		
		ItemStack getOutput(FluidStack stack, IInventory inv);
	}

	public static List<MeltingRecipe> getMeltingRecipes()
	{
		return meltingRequireMap.toList();
	}
	
	public static List<IFreezingRecipe> getFreezeRecipes()
	{
		return freezingRecipeMap;
	}
	
	public static void addRecipe(Object recipe)
	{
		if(recipe instanceof MeltingRecipe)
		{
			meltingRequireMap.register((MeltingRecipe) recipe, ((MeltingRecipe) recipe).getName());
		}
		else if(recipe instanceof IFreezingRecipe)
		{
			freezingRecipeMap.add((IFreezingRecipe) recipe);
		}
		else throw new RuntimeException();
	}
}