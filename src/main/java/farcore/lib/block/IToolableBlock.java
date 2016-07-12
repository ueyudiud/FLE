package farcore.lib.block;

import farcore.data.EnumToolType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IToolableBlock
{
	float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ);

	float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, int x, int y,
			int z, int side, float hitX, float hitY, float hitZ);
}