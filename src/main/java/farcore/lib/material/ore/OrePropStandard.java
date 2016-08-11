package farcore.lib.material.ore;

import java.util.Random;

import farcore.data.EnumToolType;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OrePropStandard implements IOreProperty
{
	@Override
	public void updateTick(TEOre ore, Random rand)
	{
	}
	
	@Override
	public boolean onBlockClicked(TEOre ore, EntityPlayer playerIn, Direction side)
	{
		return false;
	}
	
	@Override
	public void onEntityWalk(TEOre ore, Entity entityIn)
	{
	}
	
	@Override
	public EnumActionResult onBlockActivated(TEOre ore, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
			Direction side, float hitX, float hitY, float hitZ)
	{
		return EnumActionResult.PASS;
	}
	
	@Override
	public boolean onBurn(TEOre ore, float burnHardness, Direction direction)
	{
		return false;
	}
	
	@Override
	public boolean onBurningTick(TEOre ore, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		return false;
	}
	
	@Override
	public float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, TEOre ore, Direction side,
			float hitX, float hitY, float hitZ)
	{
		return 0;
	}

	@Override
	public float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, TEOre ore, Direction side,
			float hitX, float hitY, float hitZ, long tick)
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, TEOre ore, Random rand)
	{

	}
}