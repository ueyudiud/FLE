package farcore.lib.block.instance;

import java.util.Random;

import farcore.data.CT;
import farcore.data.MC;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyTree;
import nebula.common.LanguageManager;
import nebula.common.block.IToolableBlock;
import nebula.common.data.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.OreDict;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLogArtificial extends BlockLog implements IToolableBlock
{
	public static BlockLogArtificial create(Mat material, PropertyTree $tree)
	{
		return new BlockLogArtificial(material, $tree)
		{
			@Override
			protected BlockStateContainer createBlockState()
			{
				return $tree.createLogStateContainer(this, true);
			}
			
			@Override
			public int getMetaFromState(IBlockState state)
			{
				return $tree.getLogMeta(state, true);
			}
			
			@Override
			public IBlockState getStateFromMeta(int meta)
			{
				return $tree.getLogState(this, meta, true);
			}

			@Override
			protected IBlockState initDefaultState(IBlockState state)
			{
				return $tree.initLogState(true, super.initDefaultState(state));
			}
		};
	}
	
	protected BlockLogArtificial(Mat material, PropertyTree tree)
	{
		super("log.artifical." + material.name, material, tree);
		setCreativeTab(CT.tabTree);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), material.localName + " Log");
		if(tree.tickLogUpdate())
		{
			setTickRandomly(true);
		}
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		OreDict.registerValid("logWood", this);
		MC.log.registerOre(tree.material(), this);
	}
	
	@Override
	public boolean canBreakBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		tree.breakLog(worldIn, pos, state, true);
	}
	
	@Override
	public int tickRate(World worldIn)
	{
		return 20;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		tree.updateLog(worldIn, pos, rand, true);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		if(world instanceof World)
		{
			if(((World) world).isRemote) return;
			((World) world).scheduleUpdate(pos, this, tickRate((World) world));
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return tree.onLogRightClick(playerIn, worldIn, pos, Direction.of(side), hitX, hitY, hitZ, true);
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, BlockPos pos,
			Direction side, float hitX, float hitY, float hitZ)
	{
		return tree.onToolClickLog(player, tool, stack, world, pos, side, hitX, hitY, hitZ, true);
	}
	
	@Override
	public ActionResult<Float> onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		return tree.onToolUseLog(player, tool, stack, world, useTick, pos, side, hitX, hitY, hitZ, true);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		IBlockState state;
		EnumAxis axis2 = (state = world.getBlockState(pos)).getValue(net.minecraft.block.BlockLog.LOG_AXIS);
		Axis axis3 = axis.getAxis();
		EnumAxis axis4 = null;
		switch (axis2)
		{
		case Y:
		case NONE:
			if(axis3 != Axis.Y)
			{
				axis4 = axis3 == Axis.X ? EnumAxis.Z : EnumAxis.X;
			}
			break;
		case X:
			if(axis3 != Axis.X)
			{
				axis4 = axis3 == Axis.Y ? EnumAxis.Z : EnumAxis.Y;
			}
			break;
		case Z:
			if(axis3 != Axis.Z)
			{
				axis4 = axis3 == Axis.X ? EnumAxis.Y : EnumAxis.X;
			}
			break;
		}
		if(!world.isRemote)
		{
			if(axis2 != axis4)
			{
				world.setBlockState(pos, state.withProperty(net.minecraft.block.BlockLog.LOG_AXIS, axis4), 3);
			}
			return true;
		}
		return axis2 != axis4;
	}
	
	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos pos)
	{
		return EnumFacing.VALUES;
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return true;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 25;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 24;
	}
}