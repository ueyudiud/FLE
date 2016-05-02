package farcore.lib.recipe;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

/**
 * The basic crafting inventory, only
 * use to match a recipe.
 * @author ueyudiud
 *
 */
public interface ICraftingInventoryMatching
{
	int getCraftingMatrixSize();
	
	int getToolSlotSize();
	
	int getInventoryMaxWidth();
	
	int getInventoryMaxHeight();
		
	ItemStack getStackByCoord(int x, int y);
	
	ItemStack getStackInMatrix(int id);
	
	ItemStack getTool(int id);
	
	ItemStack getToolMatrial(int id);
	
	@Nullable World getWorld();
	
	@Nullable EntityPlayer getPlayer();
}