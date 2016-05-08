package farcore.lib.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.BaseStack;
import farcore.util.U;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedFleRecipe extends AbstractFleRecipe
{
	public int width;
	public int height;
	public boolean allowMirror;
	public AbstractStack[][] input;
	
	public ShapedFleRecipe(ItemStack output, int tick, Object...objects)
	{
		try
		{
			int i = 0;
			Entry[] entries = new Entry[0];
			if(objects[i] instanceof Entry[])
			{
				entries = (Entry[]) objects[i++];
			}
			else
			{
				List<Entry> list = new ArrayList();
				while(objects[i] instanceof Entry)
				{
					list.add((Entry) objects[i++]);
				}
				entries = list.toArray(entries);
			}
			if(objects[i] instanceof Boolean)
			{
				allowMirror = ((Boolean) objects[i++]).booleanValue();
			}
			wrap(this, output, tick, entries);
			
			String inputString = "";
			
			if(objects[i] instanceof String[])
			{
				String[] map = (String[]) objects[i++];
				width = map[0].length();
				height = map.length;
				for(String string : map)
				{
					if(width != string.length()) throw new RuntimeException("The crafting input matrix do not has same width!");
					inputString += string;
				}
			}
			else
			{
				int h = 0;
				while(objects[i] instanceof String)
				{
					String string = (String) objects[i++];
					if(width == 0) width = string.length();
					else if(width != string.length()) throw new RuntimeException("The crafting input matrix do not has same width!");
					inputString += string;
					++h;
				}
				height = h;
			}
			Map<Character, AbstractStack> inputRepleaces = new HashMap();
			for(; i < objects.length; i += 2)
			{
				Character character = (Character) objects[i];
				AbstractStack stack = U.Inventorys.asStack(objects[i + 1], true);
				if(inputRepleaces.containsKey(character)) throw new RuntimeException("The character " + character + " may use twice!");
				inputRepleaces.put(character, stack);
			}
			input = new AbstractStack[height][width];
			for(i = 0; i < inputString.length(); ++i)
			{
				input[i / width][i % width] = inputRepleaces.get(inputString.charAt(i));
			}
		}
		catch(Throwable throwable)
		{
            String ret = "Invalid shaped recipe: ";
            for (Object tmp : objects)
            {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret, throwable);
		}
	}

	@Override
	public boolean matchRecipe(ICraftingInventoryMatching inventory)
	{
		if(!super.matchRecipe(inventory)) return false;
		return matchMatrix(inventory);
	}
	
	protected boolean matchMatrix(ICraftingInventoryMatching inventory)
	{
		int h = inventory.getInventoryMaxHeight();
		int w = inventory.getInventoryMaxWidth();
		if(h < height || w < width)
			return false;
		for(int i = 0; i < h - height + 1; ++i)
			for(int j = 0; j < w - width + 1; ++j)
			{
				if(matchMatrixOffset(inventory, j, i, false))
					return true;
				if(allowMirror && matchMatrixOffset(inventory, j, i, true))
					return true;
			}
		return false;
	}
	
	protected boolean matchMatrixOffset(ICraftingInventoryMatching inventory, int u, int v, boolean mirror)
	{
		int w = inventory.getInventoryMaxWidth();
		int h = inventory.getInventoryMaxHeight();
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				AbstractStack check;
				if(i < u || j < v || i >= u + width || j >= v + height)
				{
					check = null;
				}
				else
				{
					check = input[j - v][mirror ? (width - 1 - (i - u)) : i - u];
				}
				ItemStack stack = inventory.getStackByCoord(i, j);
				if((check == null || check == BaseStack.EMPTY) ^ stack == null)
				{
					return false;
				}
				if(check != null && !check.contain(stack))
				{
					return false;
				}
			}
		return true;
	}

	@Override
	public void onOutputStack(ICraftingInventory inventory)
	{
		super.onOutputStack(inventory);
		int h = inventory.getInventoryMaxHeight();
		int w = inventory.getInventoryMaxWidth();
		for(int i = 0; i < h - height + 1; ++i)
			for(int j = 0; j < w - width + 1; ++j)
			{
				if(matchMatrixOffset(inventory, j, i, false))
				{
					onOutputStackOffset(inventory, j, i, false);
					return;
				}
				if(allowMirror && matchMatrixOffset(inventory, j, i, true))
				{
					onOutputStackOffset(inventory, j, i, true);
					return;
				}
			}
	}
	
	protected void onOutputStackOffset(ICraftingInventory inventory, int u, int v, boolean mirror)
	{
		for(int i = 0; i < width; ++i)
			for(int j = 0; j < height; ++j)
			{
				AbstractStack check = input[j][mirror ? (width - 1 - i) : i];
				ItemStack stack = inventory.getStackByCoord(u + i, v + j);
				if((check == null || check == BaseStack.EMPTY) || stack == null)
				{
					continue;
				}
				if(check.useContainer() || !stack.getItem().hasContainerItem(stack))
				{
					inventory.decrStackInMatrix(u + i, v + j, check.size(stack));
				}
				else
				{
					inventory.setStackInMatrix(u + i, v + j, stack.getItem().getContainerItem(stack));
				}
			}
	}
}