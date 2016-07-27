package farcore.lib.tile;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface IBreakingDropableTile
{
	List<ItemStack> getDropsOnTileRemoved(IBlockState state);
}