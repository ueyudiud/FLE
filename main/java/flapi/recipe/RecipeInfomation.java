package flapi.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import flapi.util.FleValue;

public class RecipeInfomation
{
	public static void setChance(ItemStack aInput, float chance)
	{
		a(aInput).setFloat("Chance", chance);
	}
	
	public static float getChance(ItemStack aInput)
	{
		return a(aInput).getFloat("Chance");
	}
	
	public static String getChanceInfo(ItemStack aInput, boolean flag)
	{
		return flag ? "Chance : " + FleValue.format_progress.format_c(getChance(aInput)) : 
			"Chance : " + FleValue.format_progress.format(getChance(aInput));
	}
	
	public static void setHeat(ItemStack aInput, float heat)
	{
		a(aInput).setFloat("Heat", heat);
	}
	
	public static float getHeat(ItemStack aInput)
	{
		return a(aInput).getFloat("Heat");
	}
	
	public static void addHeat(ItemStack aInput, float heat)
	{
		setHeat(aInput, getHeat(aInput) + heat);
	}
	
	private static NBTTagCompound a(ItemStack aStack)
	{
		if(!aStack.hasTagCompound()) aStack.stackTagCompound = new NBTTagCompound();
		return aStack.getTagCompound();
	}
}