package farcore.enums;

import cpw.mods.fml.common.registry.GameData;
import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.BaseStack;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Far land era item enum type.<br>
 * Current needed item register by far land era and other child mod.
 * You can get fle item here with some required NBT.<br>
 * A example:<br>
 * Get a pickaxe with material stone.<br>
 * <code>
 * EnumItem.tool.instance(1, "pickaxe", SubstanceTool.getSubstance("stone"), SubstanceHandle.VOID_TOOL); 
 * </code><br>
 * You can see develop version to see what instance can be provide.
 * Invalid provide will return a null.<br>
 * @author ueyudiud
 * 
 */
public enum EnumItem
{
	/**
	 * Far core debugger.<br>
	 * Use to get world&block information.
	 */
	debug, 
	/**
	 * Far core item to show fluid by item stack.<br>
	 * The item implements IInfomationable,
	 * and has four accessed input.<br>
	 * <code>provide(Fluid fluid);</code><br>
	 * Input with only a fluid with unknown amount.<br>
	 * <code>provide(FluidStack stack);</code><br>
	 * Provide a stack can display similar amount with stack amount.<br>
	 * <code>provide(String fluid);</code><br> 
	 * Get fluid by fluid name, if this name is not registered,
	 * return a default stack (Current water, no amount).<br>
	 * <code>provide(Fluid fluid, int amount);</code><br>
	 * Provide a stack.<br>
	 * This item is just use in NEI or somewhere should use fluid icon 
	 * to display a recipe, etc.
	 * @param fluid The display fluid.
	 * @param amount The amount of fluid stack, or not display it.
	 */
	display_fluid,
	/**
	 * Far land era torch, registered by fle.<br>
	 * This item implements IInfomationable,
	 * and has three accessed input.<br>
	 * <code>provide(int size);</code><br>
	 * This method provide a torch stack only, with void for material.<br>
	 * <code>provide(int size, SubstanceWood wood);</code><br>
	 * This method provide a torch stack with select wood for material,
	 * and full burn time.<br>
	 * <code>provide(int size, SubstanceWood wood, int burntime);</code><br>
	 * This method provide a torch stack with select wood for material,
	 * and select burn time.<br>
	 * @param wood The material of torch.
	 * @param burntime The burn time of torch.
	 */
	torch, 
	fire, 
	rock_block, 
	cobble_block, 
	sand_block,
	/**
	 * The stone chips.<br>
	 */
	stone_chip,
	/**
	 * The stone fragments.<br>
	 */
	stone_fragment,
	pile,
	/**
	 * Far land era tools, registered by fle.<br>
	 * There are all kinds of tools generated in fle.
	 */
	tool, 
	tool_head,
	log, 
	plant, 
	plant_production, 
	ore_block,
	/**
	 * The cut tree log (The natural generate please use
	 * SubstanceWood field log).
	 */
	log_block,
	brick_block, 
	food_smeltable, 
	food_divide_smeltable,
	stick;
	
	boolean init = false;
	AbstractStack stack;
	ItemStack instance;
	Item item;
	
	public void set(AbstractStack stack)
	{
		set(stack, stack.instance());
	}
	public void set(ItemStack stack)
	{
		set(new BaseStack(stack), stack);
	}
	public void set(AbstractStack stack, ItemStack instance)
	{
		if(init)
		{
			throw new RuntimeException("The item " + name() + " has already init!");
		}
		if(instance != null)
		{
			this.item = instance.getItem();
		}
		this.stack = stack;
		this.instance = instance.copy();
		init = true;
	}
	
	public ItemStack instance()
	{
		return ItemStack.copyItemStack(instance);
	}
	
	public ItemStack instance(int size)
	{
		if(init)
		{
			ItemStack stack = instance.copy();
			stack.stackSize = size;
			return stack;
		}
		return null;
	}
	
	public ItemStack instance(int size, Object...objects)
	{
		if(init)
		{
			if(item instanceof IInfomationable)
				return ((IInfomationable) item).provide(size, objects);
			ItemStack stack = instance.copy();
			stack.stackSize = size;
			return stack;
		}
		return null;
	}
	
	public AbstractStack get()
	{
		return stack;
	}
	
	public AbstractStack get(int size)
	{
		return U.Inventorys.sizeOf(stack, size);
	}
	
	public Item item()
	{
		return item;
	}
	
	public Block block()
	{
		return Block.getBlockFromItem(item);
	}
	
	public boolean initialised()
	{
		return init;
	}
	
	public static interface IInfomationable
	{
		ItemStack provide(int size, Object...objects);
	}
}