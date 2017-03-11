/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import static fle.api.recipes.ShapedFleRecipe.castAsInputMatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ObjectArrays;

import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

/**
 * @author ueyudiud
 */
public class ShapelessFleRecipe implements IRecipe
{
	protected List<SingleInputMatch> inputs;
	protected AbstractStack output;
	protected boolean forcePlayerContain;
	
	protected ShapelessFleRecipe() {}
	public ShapelessFleRecipe(ItemStack output, Object...inputs) { this(new BaseStack(output), inputs); }
	public ShapelessFleRecipe(AbstractStack output, Object...inputs)
	{
		try
		{
			if (output == null || (output instanceof BaseStack && ((BaseStack) output).instance() == null))
				throw new RuntimeException();
			this.output = output;
			this.inputs = new ArrayList<>();
			for (int i = 0; i < inputs.length; ++i)
			{
				SingleInputMatch match = castAsInputMatch(inputs[i]);
				if (i + 1 < inputs.length && inputs[i + 1] instanceof Integer)
				{
					int a = (Integer) inputs[i + 1];
					
					for (int j = 0; j < a; this.inputs.add(match), ++j);
					++i;
				}
				else
				{
					this.inputs.add(match);
				}
			}
		}
		catch (Exception exception)
		{
			throw new RuntimeException("Invalid recipe table, " + Arrays.toString(ObjectArrays.concat(inputs, output)), exception);
		}
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		EntityPlayer player = ForgeHooks.getCraftingPlayer();
		if (this.forcePlayerContain && player == null)
			return false;
		else if (!matchPlayerCondition(worldIn, player))
			return false;
		return matchInventory(inv);
	}
	
	protected boolean matchInventory(InventoryCrafting inv)
	{
		ArrayList<SingleInputMatch> list = new ArrayList<>(this.inputs);
		label : for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack == null) continue;
			Iterator<SingleInputMatch> iterator = list.iterator();
			while (iterator.hasNext())
			{
				SingleInputMatch match = iterator.next();
				if (match.match(stack))
				{
					iterator.remove();
					continue label;
				}
			}
			return false;
		}
		return list.isEmpty();
	}
	
	protected boolean matchPlayerCondition(World world, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack result = this.output.instance();
		ArrayList<SingleInputMatch> list = new ArrayList<>(this.inputs);
		label : for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack == null) continue;
			Iterator<SingleInputMatch> iterator = list.iterator();
			while (iterator.hasNext())
			{
				SingleInputMatch match = iterator.next();
				if (match.match(stack))
				{
					match.applyOutput(stack, result);
					iterator.remove();
					continue label;
				}
			}
			return null;//Is this recipe invalid?
		}
		return result;
	}
	
	@Override
	public int getRecipeSize()
	{
		return this.inputs.size();
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return this.output.instance();
	}
	
	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv)
	{
		ItemStack[] result = new ItemStack[inv.getSizeInventory()];
		ArrayList<SingleInputMatch> list = new ArrayList<>(this.inputs);
		label : for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack == null) continue;
			Iterator<SingleInputMatch> iterator = list.iterator();
			while (iterator.hasNext())
			{
				SingleInputMatch match = iterator.next();
				if (match.match(stack))
				{
					result[i] = match.getRemain(stack);
					iterator.remove();
					continue label;
				}
			}
		}
		return result;
	}
}