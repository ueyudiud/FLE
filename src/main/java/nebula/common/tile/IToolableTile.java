/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tile;

import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.world.ICoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

public interface IToolableTile extends ICoord
{
	ActionResult<Float> DEFAULT_RESULT = new ActionResult<>(EnumActionResult.PASS, 0.0F);
	
	ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack,
			Direction side, float hitX, float hitY, float hitZ);
}