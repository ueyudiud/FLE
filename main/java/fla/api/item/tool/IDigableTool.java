package fla.api.item.tool;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IDigableTool extends ITool
{	
	/**
	 * Get weather tool can harvest block.
	 * @param stack the stack player are using.
	 * @param block the target witch player is harvesting.
	 * @param meta the meta of block.
	 * @return can harvest block.
	 */
	public boolean canToolHarvestBlock(ItemStack stack, Block block, int meta);
	
	/**
	 * Get tool can dig block in a fast speed.
	 * @param stack the stack player are using.
	 * @param block the target witch player is harvesting.
	 * @param meta the meta of block.
	 * @return is tool effective
	 */
	public boolean isToolEffective(ItemStack stack, Block block, int meta);
	
	public float getToolDigSpeed(ItemStack stack, Block block, int meta);
}
