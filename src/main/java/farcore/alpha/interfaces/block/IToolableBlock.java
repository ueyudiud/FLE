package farcore.alpha.interfaces.block;

import farcore.enums.EnumToolType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IToolableBlock
{
	/**
	 * Called when tool click on block.
	 * @param player
	 * @param tool
	 * @param stack
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 * @param hitX
	 * @param hitY
	 * @param hitZ
	 * @return The damage multiply.
	 */
	float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, 
			int x, int y, int z, int side, float hitX, float hitY, float hitZ);
	
	/**
	 * Called when tool using on block.
	 * @param player
	 * @param tool
	 * @param stack
	 * @param world
	 * @param useTick
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 * @param hitX
	 * @param hitY
	 * @param hitZ
	 * @return The damage multiply.
	 */
	float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, 
			long useTick, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
}