/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import java.util.Collection;
import java.util.Map.Entry;

import fle.api.recipes.IRecipeMap;
import nebula.base.Ety;
import nebula.common.util.L;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class VanillaCraftingHandler implements IRecipeMap<IRecipe, IRecipe, Entry<InventoryCrafting, World>>
{
	@Override
	public IRecipe readFromNBT(NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void writeToNBT(IRecipe target, NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getRegisteredName()
	{
		return "minecraft.crafting";
	}
	
	@Override
	public boolean addRecipe(IRecipe recipe)
	{
		CraftingManager.getInstance().addRecipe(recipe);
		return true;
	}
	
	@Override
	public IRecipe findRecipe(Entry<InventoryCrafting, World> handler)
	{
		InventoryCrafting crafting = handler.getKey();
		World world = handler.getValue();
		return L.get(recipes(), recipe -> recipe.matches(crafting, world));
	}
	
	@Override
	public Collection<IRecipe> recipes()
	{
		return CraftingManager.getInstance().getRecipeList();
	}
	
	@Override
	public void removeRecipe(IRecipe recipe)
	{
		CraftingManager.getInstance().getRecipeList().remove(recipe);
	}
	
	public void removeRecipe(InventoryCrafting handler)
	{
		removeRecipeByHandler(new Ety<>(handler, null));
	}
	
	@Override
	public void removeRecipeByHandler(Entry<InventoryCrafting, World> handler)
	{
		CraftingManager.getInstance().getRecipeList().removeIf(r->r.matches(handler.getKey(), handler.getValue()));
	}
}