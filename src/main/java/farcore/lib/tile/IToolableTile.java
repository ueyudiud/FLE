package farcore.lib.tile;

import farcore.data.EnumToolType;
import farcore.lib.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

public interface IToolableTile
{
	ActionResult<Float> DEFAULT_RESULT = new ActionResult<Float>(EnumActionResult.PASS, 0.0F);

	ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack,
			Direction side, float hitX, float hitY, float hitZ);
}