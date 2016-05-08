package farcore.lib.recipe;

import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The standard recipe handler.
 * To save recipes and load recipes.
 * To add recipe or remove recipe.
 * @author ueyudiud
 *
 * @param <R> The recipe type.
 */
public interface IRecipeHandler<R>
{
	void addRecipe(String group, String name, R recipe);

	void removeRecipes(String group);
	
	void removeRecipe(String group, String name);
	
	List<R> getRecipes();
	
	Map<String, List<R>> getSortedRecipes();
	
	List<R> getRecipes(String group);
	
	R readFromNBT(NBTTagCompound nbt);
	
	void writeToNBT(NBTTagCompound nbt, R recipe);
}