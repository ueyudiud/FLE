package nebula.common.block;

import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IToolableBlock
{
	ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, BlockPos pos,
			Direction side, float hitX, float hitY, float hitZ);
	
	@Deprecated
	default ActionResult<Float> onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, BlockPos pos,
			Direction side, float hitX, float hitY, float hitZ)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
}