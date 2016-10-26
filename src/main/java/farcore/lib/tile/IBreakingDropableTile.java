package farcore.lib.tile;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

@Deprecated
public interface IBreakingDropableTile
{
	List<ItemStack> getDropsOnTileRemoved(IBlockState state);
}