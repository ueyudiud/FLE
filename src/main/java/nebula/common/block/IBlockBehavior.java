/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.tool.ToolHooks;
import nebula.common.util.Direction;
import nebula.common.world.IModifiableCoord;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public interface IBlockBehavior<B>
{
	default float getAmbientOcclusionLightValue(B blcok, IBlockState state)
	{
		return isNormalCube(blcok, state) ? .2F : 1.0F;
	}
	
	default float getBlockHardness(B block, IBlockState state, World world, BlockPos pos)
	{
		return 0;
	}
	
	default AxisAlignedBB getBoundingBox(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return Block.FULL_BLOCK_AABB;
	}
	
	default AxisAlignedBB getCollisionBoundingBox(B block, IBlockState state, World world, BlockPos pos)
	{
		return getBoundingBox(block, state, world, pos);
	}
	
	default List<ItemStack> getDrops(B block, IBlockState state, BlockPos pos, IBlockAccess world, TileEntity tile, int fortune,
			boolean silkTouch)
	{
		return Arrays.asList(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
	}
	
	default int getExpDrop(B block, IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		return 0;
	}
	
	default float getExplosionResistance(B block, IBlockState state, World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return 0;
	}
	
	default int getFireSpreadSpeed(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 0;
	}
	
	default int getFlammability(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 0;
	}
	
	default int getHarvestLevel(B block, IBlockState state)
	{
		return 0;
	}
	
	default String getHarvestTool(B block, IBlockState state)
	{
		return null;
	}
	
	default int getLightOpacity(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return isOpaqueCube(block, state) ? 255 : 0;
	}
	
	default int getLightValue(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return 0;
	}
	
	default MapColor getMapColor(B block, IBlockState state, MapColor color)
	{
		return color;
	}
	
	default Material getMaterial(B block, IBlockState state, Material material)
	{
		return material;
	}
	
	default EnumPushReaction getMobilityFlag(B block, IBlockState state, EnumPushReaction reaction)
	{
		return reaction;
	}
	
	@SideOnly(Side.CLIENT)
	default int getPackedLightmapCoords(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		int light = getLightValue(block, state, world, pos);
		int i = world.getCombinedLight(pos, light);
		
		if (i == 0 && state.getBlock() instanceof BlockSlab)
		{
			pos = pos.down();
			state = world.getBlockState(pos);
			return world.getCombinedLight(pos, state.getLightValue(world, pos));
		}
		else
		{
			return i;
		}
	}
	
	default int getStrongPower(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return 0;
	}
	
	default int getWeakPower(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return 0;
	}
	
	default float getEnchantPowerBonus(B block, IBlockState state, World world, BlockPos pos)
	{
		return 0;
	}
	
	default boolean isFireSource(B block, IBlockState state, World world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	default boolean isFlammable(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return getFlammability(block, state, world, pos, face) > 0;
	}
	
	default boolean isBurning(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	default boolean isLadder(B block, IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityLivingBase entity)
	{
		return false;
	}
	
	default boolean isLeaves(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	default boolean isSideSolid(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}
	
	default boolean isToolEffective(B block, IBlockState state, String type)
	{
		String harvestTool = getHarvestTool(block, state);
		return harvestTool != null && harvestTool.equals(type);
	}
	
	default boolean isTranslucent(B block, IBlockState state)
	{
		return !state.getMaterial().blocksLight();
	}
	
	default boolean isReplaceable(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.getMaterial().isReplaceable();
	}
	
	default boolean isWood(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	default boolean isNormalCube(B block, IBlockState state)
	{
		return true;
	}
	
	default boolean isOpaqueCube(B block, IBlockState state)
	{
		return true;
	}
	
	default boolean onBlockActivated(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	
	default void onBlockClicked(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player)
	{
		
	}
	
	default void onBlockExploded(B block, IBlockState state, World world, BlockPos pos, Explosion explosion)
	{
		world.setBlockToAir(pos);
	}
	
	default void onBlockDestroyedByPlayer(B block, IBlockState state, World world, BlockPos pos)
	{
		
	}
	
	default boolean onBlockHarvest(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean silkHarvest)
	{
		return false;
	}
	
	default void onBlockHarvested(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player)
	{
		
	}
	
	default void onBlockPlacedBy(B block, IBlockState state, World world, BlockPos pos, EntityLivingBase placer, EnumFacing facing,
			ItemStack stack)
	{
		
	}
	
	default void onEntityCollidedWithBlock(B block, IBlockState state, World world, BlockPos pos, Entity entity)
	{
		
	}
	
	default void onEntityWalk(B block, IBlockState state, World world, BlockPos pos, Entity entity)
	{
		
	}
	
	default void onFallenUpon(B block, IBlockState state, World world, BlockPos pos, Entity entity, float fallDistance)
	{
		entity.fall(fallDistance, 1.0F);
	}
	
	default int getCheckRange(B block, IBlockState state)
	{
		return 2;
	}
	
	default void notifyAfterTicking(B block, IBlockState state, World world, BlockPos pos, IBlockState changed)
	{
		
	}
	
	default void onNeighborChange(B block, IBlockState state, IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		
	}
	
	default void breakBlock(B block, IBlockState state, World world, BlockPos pos)
	{
		
	}
	
	default boolean canBreakBlock(B block, IBlockState state, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return ToolHooks.isToolBreakable(state, player.getHeldItemMainhand());
	}
	
	default boolean canBreakEffective(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player)
	{
		return canBreakBlock(block, state, world, pos, player);
	}
	
	default boolean canHarvestBlock(B block, IBlockState state, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return ToolHooks.isToolHarvestable((Block) block, world, pos, player);
	}
	
	default boolean canConnectRedstone(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	default boolean canCreatureSpawn(B block, IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type)
	{
		return isSideSolid(block, state, world, pos, EnumFacing.UP);
	}
	
	default boolean canEntityDestroy(B block, IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return true;
	}
	
	default boolean canPlaceTorchOnTop(B block, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return isSideSolid(block, state, world, pos, EnumFacing.UP);
	}
	
	default boolean canSilkHarvest(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player)
	{
		return false;
	}
	
	default boolean canSustainPlant(B block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
			IPlantable plantable)
	{
		return false;
	}
	
	default void randomTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		updateTick(block, state, world, pos, random);
	}
	
	default void updateTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	default void randomDisplayTick(B block, IBlockState state, World world, BlockPos pos, Random random)
	{
		
	}
	
	default boolean onDropFallenAsItem(B block, IBlockState state, World world, BlockPos pos, NBTTagCompound tileNBT)
	{
		return false;
	}
	
	default float onFallOnEntity(B block, IBlockState state, World world, EntityFallingBlockExtended entity, Entity target)
	{
		return 0F;
	}
	
	default boolean onFallOnGround(B block, IBlockState state, World world, BlockPos pos, int height,
			NBTTagCompound tileNBT)
	{
		return false;
	}
	
	default void onStartFalling(B block, IBlockState state, World world, BlockPos pos)
	{
		
	}
	
	default ActionResult<Float> onToolClick(B block, IBlockState state, World world, BlockPos pos, EntityPlayer player,
			EnumToolType tool, int level, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
	
	default boolean canFallingBlockStay(B block, IBlockState state, World world, BlockPos pos)
	{
		return EntityFallingBlockExtended.canFallAt(world, pos, state);
	}
	
	default int getFireEncouragement(B block, IBlockState state, World world, BlockPos pos)
	{
		return 0;
	}
	
	default double getThermalConduct(B block, IBlockState state, World world, BlockPos pos)
	{
		return -1F;
	}
	
	default boolean onBurn(B block, IBlockState state, IModifiableCoord coord, float burnHardness, Direction direction)
	{
		return false;
	}
	
	default boolean onBurningTick(B block, IBlockState state, IModifiableCoord coord, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		return false;
	}
}