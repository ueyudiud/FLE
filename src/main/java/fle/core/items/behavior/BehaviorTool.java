package fle.core.items.behavior;

import farcore.lib.item.behavior.BehaviorBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BehaviorTool extends BehaviorBase
{
	protected EnumActionResult useResult;
	
	public BehaviorTool()
	{
		this(EnumActionResult.PASS);
	}
	public BehaviorTool(EnumActionResult useResult)
	{
		this.useResult = useResult;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos,
			EntityLivingBase entity)
	{
		return true;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return useResult;
	}
}