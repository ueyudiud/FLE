/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import farcore.blocks.flora.BlockLeaves;
import farcore.blocks.flora.BlockLeavesCore;
import farcore.blocks.flora.BlockLogArtificial;
import farcore.blocks.flora.BlockLogNatural;
import farcore.lib.bio.ISpecie;
import farcore.lib.tile.instance.TECoreLeaves;
import nebula.common.block.IBlockStateRegister;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.IRegisteredNameable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Unused now, use PropertyTree instead.
 * 
 * @author ueyudiud
 * @see farcore.lib.material.prop.PropertyTree
 */
@Deprecated
public interface ITree extends ITreeGenerator, ISpecie<ISaplingAccess>, IRegisteredNameable
{
	enum BlockType
	{
		LOG, LOG_ART, LEAVES, LEAVES_CORE;
	}
	
	Tree VOID = new TreeVoid();
	
	// Mat material();
	
	void initInfo(BlockLogNatural logNatural, BlockLogArtificial logArtificial, BlockLeaves leaves, BlockLeavesCore leavesCore);
	
	<T extends Block> T getBlock(BlockType type);
	
	boolean tickLogUpdate();
	
	default int getLogMeta(IBlockState state, boolean isArt)
	{
		return state.getValue(BlockLog.LOG_AXIS).ordinal();
	}
	
	default IBlockState getLogState(Block block, int meta, boolean isArt)
	{
		return block.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.values()[meta & 0x3]);
	}
	
	default IBlockState initLogState(boolean isArt, IBlockState state)
	{
		return state;
	}
	
	default IBlockState initLeavesState(IBlockState state)
	{
		return state;
	}
	
	default BlockStateContainer createLogStateContainer(Block block, boolean isArt)
	{
		return new BlockStateContainer(block, BlockLog.LOG_AXIS);
	}
	
	default int getLeavesMeta(IBlockState state)
	{
		return 0;
	}
	
	default IBlockState getLeavesState(Block block, int meta)
	{
		return block.getDefaultState();
	}
	
	default BlockStateContainer createLeavesStateContainer(Block block)
	{
		return new BlockStateContainer(block, net.minecraft.block.BlockLeaves.CHECK_DECAY);
	}
	
	void updateLog(World world, BlockPos pos, Random rand, boolean isArt);
	
	void updateLeaves(World world, BlockPos pos, Random rand, boolean markTick);
	
	void breakLog(World world, BlockPos pos, IBlockState state, boolean isArt);
	
	void breakLeaves(World world, BlockPos pos, IBlockState state);
	
	void beginLeavesDecay(World world, BlockPos pos);
	
	boolean onLogRightClick(EntityPlayer player, World world, BlockPos pos, Direction side, float xPos, float yPos, float zPos, boolean isArt);
	
	default boolean onLeavesRightClick(EntityPlayer player, World world, BlockPos pos, IBlockState state, Direction side, float xPos, float yPos, float zPos, @Nullable TECoreLeaves tile)
	{
		return false;
	}
	
	ActionResult<Float> onToolClickLog(EntityPlayer player, EnumToolType tool, int level, ItemStack stack, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, boolean isArt);
	
	ActionResult<Float> onToolClickLeaves(EntityPlayer player, EnumToolType tool, int level, ItemStack stack, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ);
	
	List<ItemStack> getLogOtherDrop(World world, BlockPos pos, List<ItemStack> list);
	
	List<ItemStack> getLeavesDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, boolean silkTouching, List<ItemStack> list);
	
	int onSaplingUpdate(ISaplingAccess access);
	
	int getGrowAge(ISaplingAccess access);
	
	default void registerLogExtData(Block block, boolean isArt, IBlockStateRegister register)
	{
		register.registerStates(block, BlockLog.LOG_AXIS);
	}
}
