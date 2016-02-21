package farcore.block.interfaces;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Far Land Era check whether the block can be harvest in mod.
 * @author ueyudiud
 *
 */
public interface IHarvestHandler
{
	boolean canHarvestBlock(World world, int x, int y, int z, int metadata, String toolKey, int level);
	
	ArrayList<ItemStack> getHarvestDrop(World world, int x, int y, int z, int metadata, String toolKey, int level);
}