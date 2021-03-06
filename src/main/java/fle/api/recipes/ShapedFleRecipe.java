/*
 * copyright 2016-2018 ueyudiud
 */
package fle.api.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ObjectArrays;

import farcore.lib.oredict.OreStackExt;
import nebula.base.collection.A;
import nebula.base.collection.ObjArrayParseHelper;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

/**
 * The shaped recipe type. A more usable recipe template for crafting table.
 * <p>
 * 
 * @author ueyudiud
 * @see net.minecraftforge.oredict.ShapedOreRecipe
 */
public class ShapedFleRecipe implements IRecipe
{
	public static SingleInputMatch castAsInputMatch(Object object)
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
			return new SingleInputMatch(new OreStackExt((String) object));
		}
		else if (object instanceof AbstractStack)
		{
			if (object instanceof OreStack) object = new OreStackExt((OreStack) object);
			// Extended wrapper.
			return new SingleInputMatch((AbstractStack) object);
		}
		else if (object instanceof SingleInputMatch)
		{
			return (SingleInputMatch) object;
		}
		else
			throw new RuntimeException("Unknown element: " + object);
	}
	
	public SingleInputMatch[][]	inputs;
	public int					width;
	public int					height;
	public AbstractStack		output;
	public boolean				forcePlayerContain;
	public boolean				enableMirror;
	
	public ShapedFleRecipe(ItemStack output, Object...inputs)
	{
		this(new BaseStack(output), inputs);
	}
	
	public ShapedFleRecipe(AbstractStack output, Object...inputs)
	{
		try
		{
			if (output == null || (output instanceof BaseStack && ((BaseStack) output).instance() == null)) throw new RuntimeException();
			this.output = output;
			int i = 0;
			ObjArrayParseHelper helper = A.create(inputs);
			this.enableMirror = helper.readOrSkip(false);
			
			char[][] map;
			String[] table = helper.readArrayOrCompact(String.class);
			
			if (table.length == 0) throw new RuntimeException();
			
			this.height = table.length;
			this.width = table[0].length();
			map = new char[this.height][];
			for (int j = 0; j < table.length; ++j)
			{
				map[j] = table[j].toCharArray();
				if (map[j].length != this.width) throw new RuntimeException();
			}
			
			Map<Character, SingleInputMatch> matchs = new HashMap<>();
			helper.<Character, Object> readToEnd((chr, obj) -> matchs.put(chr, castAsInputMatch(obj)));
			
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
	protected ShapedFleRecipe()
	{
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		// The player argument get.
		EntityPlayer player = ForgeHooks.getCraftingPlayer();
		if ((this.forcePlayerContain && player == null) || !matchPlayerCondition(worldIn, player)) return false;
		if (inv.getHeight() < this.height || inv.getWidth() < this.width) return false;
		return getOffset(inv) != null;
	}
	
	/**
	 * Checking could player crafting this recipe.
	 * 
	 * @param world the crafting world.
	 * @param player the crafting player.
	 */
	protected boolean matchPlayerCondition(World world, EntityPlayer player)
	{
		return true;
	}
	
	protected int[] getOffset(InventoryCrafting inv)
	{
		int maxXOff = inv.getWidth() - this.width;
		int maxYOff = inv.getHeight() - this.height;
		for (int j = 0; j <= maxYOff; ++j)
		{
			for (int i = 0; i <= maxXOff; ++i)
			{
				if (matchWithCoordOffset(inv, i, j, false)) return new int[] { i, j, 0 };
				if (this.enableMirror && matchWithCoordOffset(inv, i, j, true)) return new int[] { i, j, 1 };
			}
		}
		return null;
	}
	
	protected boolean matchWithCoordOffset(InventoryCrafting inv, int xOff, int yOff, boolean mirror)
	{
		for (int j = 0; j < this.height; ++j)
		{
			for (int i = 0; i < this.width; ++i)
			{
				SingleInputMatch match = this.inputs[j][mirror ? this.width - i - 1 : i];
				ItemStack stack = inv.getStackInRowAndColumn(i + xOff, j + yOff);
				if (!match.match(stack)) return false;
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		int[] offset = getOffset(inv);
		ItemStack result = getRecipeOutput();
		if (offset != null)
		{
			boolean mirror = offset[2] == 1;
			for (int j = 0; j < this.height; ++j)
			{
				for (int i = 0; i < this.width; ++i)
				{
					this.inputs[j][mirror ? this.width - i - 1 : i]
							.applyOutput(ItemStack.copyItemStack(inv.getStackInRowAndColumn(i + offset[0], j + offset[1])), result);
				}
			}
		}
		return result;
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
					stacks[(j + offset[1]) * inv.getWidth() + i + offset[0]] = match.getRemain(ItemStack.copyItemStack(stack));
				}
			return stacks;
		}
		return new ItemStack[inv.getSizeInventory()];
	}
}
