package farcore.recipe.crafting;

import java.util.HashMap;

import farcore.recipe.IItemMatrix;
import farcore.recipe.IMatrixInputRecipe;
import farcore.recipe.MatrixSingleInputRecipe;
import farcore.recipe.stack.IS;
import farcore.recipe.stack.OS;
import farcore.tile.inventory.MatrixWarper;
import gnu.trove.map.hash.THashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import scala.collection.generic.BitOperations.Int;

/**
 * Shaped recipe, use crafting matrix for matching inventory.<br>
 * Implements listener during output check.
 * @author ueyudiud
 * @see farcore.recipe.IMatrixInputRecipe
 * @see farcore.recipe.ISingleInputRecipe
 */
public class ShapedFleRecipe implements IRecipe, farcore.recipe.IRecipe, IRecipeListener
{
	/** 3x3 inventory, may added in the future. */
	private static final int MATRIX_WIDTH = 3;
	private static final int MATRIX_HEIGHT = 3;
	
	/** The flag option weather check mirror with x. */
	public final boolean checkMirror;
	/** The all width of recipe. */
	public final int width;
	/** The all height of recipe. */
	public final int height;
	/**
	 * The matrix inputs.
	 */
	public final IMatrixInputRecipe[] inputs;
	/**
	 * The output (check with a select slot).
	 */
	public final ISingleInputRecipe output;
	/**
	 * The output check position of X.
	 */
	private final int checkX;
	/**
	 * The output check position of Y.
	 */
	private final int checkY;
	
	public ShapedFleRecipe(Object...objects)
	{
		try
		{
			int i = 0;
			ISingleInputRecipe output;
			// Get output.
			if(objects[i] instanceof Item)
			{
				output = new DefaultOutputRecipe(new ItemStack((Item) objects[i++]));
			}
			else if(objects[i] instanceof Block)
			{
				output = new DefaultOutputRecipe(new ItemStack((Block) objects[i++]));
			}
			else if(objects[i] instanceof ItemStack)
			{
				output = new DefaultOutputRecipe((ItemStack) objects[i++]);
			}
			else if(objects[i] instanceof ISingleInputRecipe)
			{
				output = (ISingleInputRecipe) objects[i++];
			}
			else throw new RuntimeException();
			//Give a position of output.
			if(objects[i] instanceof int[])
			{
				int[] is = (int[]) objects[i++];
				checkX = is[0];
				checkY = is[1];
			}
			else if(objects[i] instanceof Integer)
			{
				checkX = (Integer) objects[i++];
				checkY = (Integer) objects[i++];
			}
			else
			{
				checkX = 0;
				checkY = 0;
			}
			this.output = output;
			//Mirror flag.
			if(objects[i] instanceof Boolean)
			{
				checkMirror = (Boolean) objects[i++];
			}
			else checkMirror = false;
			//Classic minecraft use new ShapedRecipe(output, inputs), this is to compact that input.
			if(objects[i] instanceof Object[])
			{
				objects = (Object[]) objects[i];
				i = 0;
			}
			String shape = "";
			//Get recipe map.
			if(objects[i] instanceof String[])
			{
				String[] s = (String[]) objects[i++];
				height = s.length;
				width = s[0].length();
				int j = 0;
				while(j < height)
				{
					if(width != s[j].length()) throw new RuntimeException();
					shape += s[j];
				}
			}
			else
			{
				int w = -1;
				int h = -1;
	            while (objects[i] instanceof String)
	            {
	                String s = (String) objects[i++];
	                shape += s;
	                if(w != -1)
	                {
	                	if(w != s.length()) throw new RuntimeException();
	                }
	                else
	                	w = s.length();
	                h++;
	            }
	            width = w;
	            height = h;
			}
			
	        if (width * height != shape.length())
	        {
	            throw new RuntimeException();
	        }
	        //Replace characters.
	        HashMap<Character, IMatrixInputRecipe> itemMap = new HashMap();

	        for (; i < objects.length; i += 2)
	        {
	            Character chr = (Character)objects[i];
	            Object in = objects[i + 1];

	            if (in instanceof ItemStack)
	            {
	                itemMap.put(chr, 
	                		new MatrixSingleInputRecipe(
	                		new SingleMatcherRecipe(new IS(
	                				((ItemStack) in).getItem(), 1, 
	                				((ItemStack) in).getItemDamage()))));
	            }
	            else if (in instanceof Item)
	            {
	                itemMap.put(chr, 
	                		new MatrixSingleInputRecipe(
	                		new SingleMatcherRecipe(new IS((Item) in))));
	            }
	            else if (in instanceof Block)
	            {
	                itemMap.put(chr, 
	                		new MatrixSingleInputRecipe(
	                		new SingleMatcherRecipe(new IS((Block) in))));
	            }
	            else if (in instanceof String)
	            {
	                itemMap.put(chr, 
	                		new MatrixSingleInputRecipe(
	                		new SingleMatcherRecipe(new OS((String) in))));
	            }
//	            else if (in instanceof FluidStack)
//	            {
//	                itemMap.put(chr, new FluidContainerStack((FluidStack) in));
//	            }
	            else if (in instanceof ISingleInputRecipe)
	            {
	            	itemMap.put(chr, 
	            			new MatrixSingleInputRecipe((ISingleInputRecipe) in));
	            }
	            else if (in instanceof IMatrixInputRecipe)
	            {
	            	itemMap.put(chr, (IMatrixInputRecipe) in);
	            }
	            else throw new NullPointerException();
	        }

	        inputs = new IMatrixInputRecipe[width * height];
	        int x = 0;
	        for (char chr : shape.toCharArray())
	        {
	            inputs[x++] = itemMap.get(chr);
	        }
		}
		catch(Exception exception)
		{
            String ret = "Invalid shaped fle recipe: ";
            for (Object tmp : objects)
            {
                ret += tmp + ", ";
            }
			exception.printStackTrace();
			throw new RuntimeException(ret);
		}
	}

	private ThreadLocal<int[]> thread = new ThreadLocal();
	
	@Override
	public boolean matches(InventoryCrafting crafting, World world)
	{
		IItemMatrix matrix = CraftingMatrixWarper.warp(crafting);
        for (int x = 0; x <= MATRIX_WIDTH - width; x++)
        {
            for (int y = 0; y <= MATRIX_WIDTH - height; ++y)
            {
                if (checkMatch(matrix, x, y, false))
                {
                	thread.set(new int[]{x, y, 0});
                    return true;
                }

                if (checkMirror && checkMatch(matrix, x, y, true))
                {
                	thread.set(new int[]{x, y, 1});
                    return true;
                }
            }
        }
        return false;
	}

	private boolean checkMatch(IItemMatrix matrix, int startX, int startY, boolean mirror) 
	{
        for (int x = 0; x < MATRIX_WIDTH; x++)
        {
            for (int y = 0; y < MATRIX_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                IMatrixInputRecipe target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = inputs[width - subX - 1 + subY * width];
                    }
                    else
                    {
                        target = inputs[subX + subY * width];
                    }
                }

                ItemStack slot = matrix.getStack(x, y);

                if (target == null && slot == null)
                	continue;
                else if((target == null && slot != null) || (target != null && slot == null))
                	return false;
                else if(!target.match(x, y, matrix))
                	return false;
            }
        }

        return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting)
	{
		int[] l;
		if((l = thread.get()) == null)
		{
			IItemMatrix matrix = CraftingMatrixWarper.warp(crafting);
			label:
	        for (int x = 0; x <= MATRIX_WIDTH - width; x++)
	        {
	            for (int y = 0; y <= MATRIX_WIDTH - height; ++y)
	            {
	                if (checkMatch(matrix, x, y, false))
	                {
	                	l = new int[]{x, y, 0};
	                	break label;
	                }

	                if (checkMirror && checkMatch(matrix, x, y, true))
	                {
	                	l = new int[]{x, y, 1};
	                	break label;
	                }
	            }
	        }
			if(l == null)
				return getRecipeOutput();
		}
		ItemStack ret = 
				output.output(
						crafting.getStackInRowAndColumn(
								l[2] == 0 ? 
										checkX + l[0] : checkX + width - l[0] - 1, 
								checkY + l[1]));
		return null;
	}

	@Override
	public int getRecipeSize()
	{
		return width * height;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return output.displayOutput();
	}

	@Override
	public void onCrafting(InventoryCrafting crafting, EntityPlayer player)
	{
		int[] l;
		IItemMatrix matrix = CraftingMatrixWarper.warp(crafting);
		if((l = thread.get()) == null)
		{
			label:
	        for (int x = 0; x <= MATRIX_WIDTH - width; x++)
	        {
	            for (int y = 0; y <= MATRIX_WIDTH - height; ++y)
	            {
	                if (checkMatch(matrix, x, y, false))
	                {
	                	l = new int[]{x, y, 0};
	                	break label;
	                }

	                if (checkMirror && checkMatch(matrix, x, y, true))
	                {
	                	l = new int[]{x, y, 1};
	                	break label;
	                }
	            }
	        }
			if(l == null)
				return;
		}
		thread.set(null);
		for(int i = 0; i < width; ++i)
			for(int j = 0; j < height; ++j)
			{
				IMatrixInputRecipe recipe = inputs[width * j + (l[2] == 0 ? i : width - i - 1)];
				if(recipe != null)
				{
					matrix.getStack(l[0] + i, l[1] + j).stackSize++;
					recipe.output(l[0] + i, l[1] + j, matrix);
				}
			}
	}
}