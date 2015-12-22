package flapi.block;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IHarvestHandler
{
	boolean canHarvestBlock(World world, int x, int y, int z, int metadata, String toolKey, int level);
	
	ArrayList<ItemStack> getHarvestDrop(World world, int x, int y, int z, int metadata, String toolKey, int level);
}