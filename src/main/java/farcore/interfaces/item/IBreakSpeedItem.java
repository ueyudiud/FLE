package farcore.interfaces.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBreakSpeedItem
{
	float getSpeed(ItemStack stack, World world, int x, int y, int z, Block block, int meta);	
}