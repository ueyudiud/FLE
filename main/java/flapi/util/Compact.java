package flapi.util;

import java.lang.reflect.Method;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * The mod compact (For other mods) invoke method and get field method.
 * @author ueyudiud
 *
 */
public class Compact
{
	//IC
	private static Method IC2Items_getItem;
	//GT
	private static Class<?> MultiItemTool;
	private static Class<?> ItemTool;
	//RC
	private static Class<?> RailcraftCraftingManager;
	public static Object RCcocm;
	private static Method ICokeOvenCraftingManager_addRecipe;
	public static Object RCbfcm;
	private static Method IBlastFurnaceCraftingManager_addRecipe;
	public static Object RCrmcm;
	private static Method IRollingMachineCraftingManager_addRecipe;
	
	public static boolean isGTTool(Item item)
	{
		try
		{
			if(MultiItemTool == null)
			{
				MultiItemTool = Class.forName("gregapi.item.MultiItemTool");
			}
			return MultiItemTool.isAssignableFrom(item.getClass()) || item.getClass() == MultiItemTool;
		}
		catch(Throwable e)
		{
			return false;
		}
	}
	
	public static boolean isFLETool(Item item)
	{
		try
		{
			if(ItemTool == null)
			{
				ItemTool = Class.forName("fle.tool.item.ItemTool");
			}
			return ItemTool.isAssignableFrom(item.getClass()) || item.getClass() == ItemTool;
		}
		catch(Throwable e)
		{
			return false;
		}
	}
	
	public static void damageGTTool(ItemStack aStack, float damage)
	{
		try
		{
			if(MultiItemTool == null)
			{
				MultiItemTool = Class.forName("gregapi.item.MultiItemTool");
			}
			Method method = MultiItemTool.getMethod("doDamage", ItemStack.class, Long.class);
			method.invoke(null, aStack, (long) (damage * 100));
			return;
		}
		catch(Throwable e)
		{
			FleLog.getLogger().info("Fle fail to get item damage of gregtech.");
			return;
		}
	}

	public static Method getRCRecipeRegister(byte type)
	{
		try
		{
			if(RailcraftCraftingManager == null)
			{
				RailcraftCraftingManager = Class.forName("mods.railcraft.api.crafting.RailcraftCraftingManager");
			}
			switch(type)
			{
			case 0 :;
			if(ICokeOvenCraftingManager_addRecipe == null)
			{
				RCcocm = RailcraftCraftingManager.getField("cokeOven").get(null);
				if(RCcocm != null)
					ICokeOvenCraftingManager_addRecipe = Class.forName("mods.railcraft.api.crafting.ICokeOvenCraftingManager")
					.getMethod("addRecipe", ItemStack.class, Boolean.class, Boolean.class, ItemStack.class, FluidStack.class, Integer.class);
				else return null;
			}
			return ICokeOvenCraftingManager_addRecipe;
			case 1 :;
			if(IBlastFurnaceCraftingManager_addRecipe == null)
			{
				RCbfcm = RailcraftCraftingManager.getField("blastFurnace").get(null);
				if(RCbfcm != null)
					IBlastFurnaceCraftingManager_addRecipe = Class.forName("mods.railcraft.api.crafting.BlastFurnaceCraftingManager")
					.getMethod("addRecipe", ItemStack.class, Boolean.class, Boolean.class, Integer.class, ItemStack.class);
				else return null;
			}
			return IBlastFurnaceCraftingManager_addRecipe;
			case 2 :;
			if(IRollingMachineCraftingManager_addRecipe == null)
			{
				RCrmcm = RailcraftCraftingManager.getField("rollingMachine").get(null);
				if(RCrmcm != null)
					IRollingMachineCraftingManager_addRecipe = Class.forName("mods.railcraft.api.crafting.IRollingMachineCraftingManager")
					.getMethod("addRecipe", ItemStack.class, Object[].class);
				else return null;
			}
			return IRollingMachineCraftingManager_addRecipe;
			}
			return null;
		}
		catch(Throwable e)
		{
			FleLog.getLogger().info("Fle fail to get RC recipe registry.");
			return null;
		}
	}

	public static ItemStack getItem(SubTag tag, String aName, int size)
	{
		if(tag == SubTag.IC2Item)
		{
			try
			{
				if(IC2Items_getItem == null)
				{
					Class<?> clazz = Class.forName("ic2.api.item.IC2Items");
					IC2Items_getItem = clazz.getMethod("getItem", String.class);
				}
				Object aRawStack = IC2Items_getItem.invoke(null, aName);
				if(aRawStack instanceof ItemStack)
				{
					ItemStack aStack = ((ItemStack) aRawStack).copy();
					aStack.stackSize = size;
					return aStack;
				}
			}
			catch(Throwable e)
			{
				FleLog.getLogger().warn("FLE API: Fail to get rubber wood of IC2.", e);
				return null;
			}
		}
		FleLog.getLogger().warn("FLE API: Fail to find wood.");
		return null;
	}
}