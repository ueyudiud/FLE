/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.recipes.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import fle.api.recipes.IRecipeMap;
import fle.api.recipes.instance.PortableWoodworkRecipeMap.PortableWoodworkRecipe;
import nebula.base.function.F;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class PortableWoodworkRecipeMap implements IRecipeMap<PortableWoodworkRecipe, PortableWoodworkRecipe, ItemStack[]>
{
	public static interface PortableWoodworkRecipe
	{
		boolean match(ItemStack[] inventory);
		
		@Nullable
		default int[] getIntScaleRange(ItemStack[] inventory)
		{
			return null;
		}
		
		ItemStack[] getOutputs(ItemStack[] inventory, int value);
		
		void onOutput(ItemStack[] inventory, int value);
		
		@Nullable
		@SideOnly(Side.CLIENT)
		default int[] getDisplayNumbers(ItemStack[] inventory, int value)
		{
			return null;
		}
	}
	
	private final List<PortableWoodworkRecipe> recipes = new ArrayList<>();
	
	@Override
	public PortableWoodworkRecipe readFrom(NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void writeTo(PortableWoodworkRecipe target, NBTTagCompound nbt)
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
	public PortableWoodworkRecipe findRecipe(ItemStack[] handler)
	{
		return L.get(this.recipes, F.const2p(PortableWoodworkRecipe::match, handler));
	}
	
	@Override
	public Collection<PortableWoodworkRecipe> recipes()
	{
		return this.recipes;
	}
}
