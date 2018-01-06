/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.recipes.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import fle.api.recipes.IRecipeMap;
import fle.api.recipes.instance.PortableWoodworkRecipeMap.PortableWoodworkRecipe;
import nebula.common.inventory.IBasicInventory;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class PortableWoodworkRecipeMap implements IRecipeMap<PortableWoodworkRecipe, PortableWoodworkRecipe, IBasicInventory>
{
	public static interface PortableWoodworkRecipe
	{
		boolean match(IBasicInventory inventory);
		
		@Nullable
		default int[] getIntScaleRange(IBasicInventory inventory)
		{
			return null;
		}
		
		ItemStack[] getOutputs(IBasicInventory inventory, int value);
		
		void onOutput(IBasicInventory inventory, int value);
		
		@SideOnly(Side.CLIENT)
		@Nullable
		default int[] getDisplayNumbers(IBasicInventory inventory, int value)
		{
			return null;
		}
	}
	
	private final List<PortableWoodworkRecipe> recipes = new ArrayList<>();
	
	@Override
	public PortableWoodworkRecipe readFromNBT(NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void writeToNBT(PortableWoodworkRecipe target, NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final String getRegisteredName()
	{
		return "fle.portable.woodwork";
	}
	
	@Override
	public boolean addRecipe(PortableWoodworkRecipe recipe)
	{
		return this.recipes.add(recipe);
	}
	
	@Override
	public PortableWoodworkRecipe findRecipe(IBasicInventory handler)
	{
		return L.get(this.recipes, r -> r.match(handler));
	}
	
	@Override
	public Collection<PortableWoodworkRecipe> recipes()
	{
		return this.recipes;
	}
}
