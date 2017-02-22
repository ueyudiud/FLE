/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ObjectArrays;

import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

/**
 * @author ueyudiud
 */
public class ShapedFleRecipe implements IRecipe
{
	static SingleInputMatch castAsInputMatch(Object object)
	{
		if (object instanceof Item)
		{
			return new SingleInputMatch(new BaseStack((Item) object));
		}
		else if (object instanceof Block)
		{
			return new SingleInputMatch(new BaseStack((Block) object));
		}
		else if (object instanceof ItemStack)
		{
			return new SingleInputMatch(new BaseStack((ItemStack) object));
		}
		else if (object instanceof String)
		{
			return new SingleInputMatch(new OreStack((String) object));
		}
		else if (object instanceof AbstractStack)
		{
			return new SingleInputMatch((AbstractStack) object);
		}
		else if (object instanceof SingleInputMatch)
		{
			return (SingleInputMatch) object;
		}
		else throw new RuntimeException();
	}
	
	protected SingleInputMatch[][] inputs;
	protected int width;
	protected int height;
	protected AbstractStack output;
	protected boolean forcePlayerContain;
	protected boolean enableMirror;
	
	public ShapedFleRecipe(ItemStack output, Object...inputs) { this(new BaseStack(output), inputs); }
	public ShapedFleRecipe(AbstractStack output, Object...inputs)
	{
		try
		{
			if (output == null || (output instanceof BaseStack && ((BaseStack) output).instance() == null))
				throw new RuntimeException();
			this.output = output;
			int i = 0;
			if (inputs[i] instanceof Boolean)
			{
				this.enableMirror = (Boolean) inputs[i ++];
			}
			char[][] map;
			if (inputs[i] instanceof String[])
			{
				String[] table = (String[]) inputs[i ++];
				this.height = table.length;
				this.width = table[0].length();
				map = new char[this.height][];
				for (int j = 0; j < table.length; ++j)
				{
					map[j] = table[j].toCharArray();
					if (map[j].length != this.width)
						throw new RuntimeException();
				}
			}
			else if (inputs[i] instanceof String)
			{
				this.width = ((String) inputs[i]).length();
				List<char[]> list = new ArrayList<>();
				do
				{
					char[] array = ((String) inputs[i]).toCharArray();
					if (array.length != this.width)
						throw new RuntimeException();
					list.add(array);
				}
				while(inputs[++i] instanceof String);
				map = L.cast(list, char[].class);
			}
			else throw new RuntimeException();
			Map<Character, SingleInputMatch> matchs = new HashMap<>();
			for (; i < inputs.length; i += 2)
			{
				char chr = (Character) inputs[i];
				Object object = inputs[i + 1];
				matchs.put(chr, castAsInputMatch(object));
			}
			this.inputs = new SingleInputMatch[this.height][this.width];
			for (i = 0; i < this.height; ++i)
				for (int j = 0; j < this.width; ++j)
				{
					this.inputs[i][j] = matchs.getOrDefault(map[i][j], SingleInputMatch.EMPTY);
				}
		}
		catch (Exception exception)
		{
			throw new RuntimeException("Invalid recipe table, " + Arrays.toString(ObjectArrays.concat(inputs, output)));
		}
	}
	
	/**
	 * For extends, argument will rewrite in extended type.
	 */
	protected ShapedFleRecipe() { }
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		EntityPlayer player = ForgeHooks.getCraftingPlayer();
		if (this.forcePlayerContain && player == null)
			return false;
		else if (!matchPlayerCondition(worldIn, player))
			return false;
		if (inv.getHeight() < this.height || inv.getWidth() < this.width) return false;
		return getOffset(inv) != null;
	}
	
	protected boolean matchPlayerCondition(World world, EntityPlayer player)
	{
		return true;
	}
	
	protected int[] getOffset(InventoryCrafting inv)
	{
		int maxXOff = inv.getWidth() - this.width;
		int maxYOff = inv.getHeight() - this.height;
		for (int j = 0; j <= maxYOff; ++j)
			for (int i = 0; i <= maxXOff; ++i)
			{
				if (matchWithCoordOffset(inv, i, j, false))
					return new int[]{i, j, 0};
				if (this.enableMirror && matchWithCoordOffset(inv, i, j, true))
					return new int[]{i, j, 1};
			}
		return null;
	}
	
	protected boolean matchWithCoordOffset(InventoryCrafting inv, int xOff, int yOff, boolean mirror)
	{
		for (int j = 0; j < this.height; ++j)
			for (int i = 0; i < this.width; ++i)
			{
				SingleInputMatch match = this.inputs[j][mirror ? this.width - i - 1 : i];
				ItemStack stack = inv.getStackInRowAndColumn(i + xOff, j + yOff);
				if (!match.match(stack))
					return false;
			}
		return true;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		int[] offset = getOffset(inv);
		ItemStack result = this.output.instance();
		if (offset != null)
		{
			boolean mirror = offset[2] == 1;
			for (int j = 0; j < this.height; ++j)
				for (int i = 0; i < this.width; ++i)
				{
					SingleInputMatch match = this.inputs[j][mirror ? this.width - i - 1 : i];
					ItemStack stack = inv.getStackInRowAndColumn(i + offset[0], j + offset[1]);
					match.applyOutput(stack, result);
				}
		}
		return null;
	}
	
	@Override
	public int getRecipeSize()
	{
		return this.width * this.height;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return this.output.instance();
	}
	
	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv)
	{
		int[] offset = getOffset(inv);
		if (offset != null)
		{
			boolean mirror = offset[2] == 1;
			ItemStack[] stacks = new ItemStack[inv.getSizeInventory()];
			for (int j = 0; j < this.height; ++j)
				for (int i = 0; i < this.width; ++i)
				{
					SingleInputMatch match = this.inputs[j][mirror ? this.width - i - 1 : i];
					ItemStack stack = inv.getStackInRowAndColumn(i + offset[0], j + offset[1]);
					stacks[(j + offset[1]) * this.width + i + offset[0]] = match.getRemain(stack);
				}
			return stacks;
		}
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}