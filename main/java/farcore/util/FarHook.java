package farcore.util;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import farcore.block.IHarvestCheck;

public class FarHook
{
	/**
	 * Not use yet.
	 */
	@Deprecated
	public static float getBlockStrength(IBlockState state, EntityPlayer player, World world, BlockPos pos)
    {
        float hardness = state.getBlock().getBlockHardness(world, pos);
        if (hardness < 0.0F)
        {
            return 0.0F;
        }

        if (!canHarvestBlock(state.getBlock(), player, world, pos))
        {
            return player.getBreakSpeed(state, pos) / hardness / 100F;
        }
        else
        {
            return player.getBreakSpeed(state, pos) / hardness / 30F;
        }
	}
	
	public static boolean canHarvestBlock(Block block, EntityPlayer player, IBlockAccess world, BlockPos pos)
    {
        if (block.getMaterial().isToolNotRequired())
        {
            return true;
        }
        if(block instanceof IHarvestCheck)
        {
        	ItemStack stack = player.inventory.getCurrentItem();
        	
        	IBlockState state = world.getBlockState(pos);
        	state = state.getBlock().getActualState(state, world, pos);
        	Set<String> set = stack.getItem().getToolClasses(stack);
        	IHarvestCheck check = (IHarvestCheck) block;
        	for(String tool : set)
        	{
            	int toolLevel = stack.getItem().getHarvestLevel(stack, tool);
            	if (toolLevel < 0)
            	{
            		return player.canHarvestBlock(block);
            	}
            	else if(check.canBeHarvested(state, tool, toolLevel))
            	{
            		return true;
            	}
        	}

        	return false;
        }

        return ForgeHooks.canHarvestBlock(block, player, world, pos);
    }
}