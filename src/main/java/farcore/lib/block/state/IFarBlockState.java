/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block.state;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IFarBlockState extends IBlockState
{
	@Override
	default IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos)
	{
		return getBlock().getActualState(this, blockAccess, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	default float getAmbientOcclusionLightValue()
	{
		return getBlock().getAmbientOcclusionLightValue(this);
	}
	
	@Override
	default float getBlockHardness(World worldIn, BlockPos pos)
	{
		return getBlock().getBlockHardness(this, worldIn, pos);
	}
	
	@Override
	default AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos)
	{
		return getBlock().getBoundingBox(this, blockAccess, pos);
	}
	
	@Override
	@Nullable
	default AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos)
	{
		return getBlock().getCollisionBoundingBox(this, worldIn, pos);
	}
	
	@Override
	default int getComparatorInputOverride(World worldIn, BlockPos pos)
	{
		return getBlock().getComparatorInputOverride(this, worldIn, pos);
	}
	
	@Override
	default int getLightOpacity()
	{
		return getBlock().getLightOpacity(this);
	}
	
	@Override
	default int getLightOpacity(IBlockAccess world, BlockPos pos)
	{
		return getBlock().getLightOpacity(this, world, pos);
	}
	
	@Override
	default int getLightValue()
	{
		return getBlock().getLightValue(this);
	}
	
	@Override
	default int getLightValue(IBlockAccess world, BlockPos pos)
	{
		return getBlock().getLightValue(this, world, pos);
	}
	
	@Override
	default MapColor getMapColor()
	{
		return getBlock().getMapColor(this);
	}
	
	@Override
	default Material getMaterial()
	{
		return getBlock().getMaterial(this);
	}
	
	@Override
	default EnumPushReaction getMobilityFlag()
	{
		return getBlock().getMobilityFlag(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	default int getPackedLightmapCoords(IBlockAccess source, BlockPos pos)
	{
		return getBlock().getPackedLightmapCoords(this, source, pos);
	}
	
	@Override
	default float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos)
	{
		return getBlock().getPlayerRelativeBlockHardness(this, player, worldIn, pos);
	}
	
	@Override
	default EnumBlockRenderType getRenderType()
	{
		return getBlock().getRenderType(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	default AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
	{
		return getBlock().getSelectedBoundingBox(this, worldIn, pos);
	}
	
	@Override
	default int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return getBlock().getStrongPower(this, blockAccess, pos, side);
	}
	
	@Override
	default int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return getBlock().getWeakPower(this, blockAccess, pos, side);
	}
	
	@Override
	default boolean isBlockNormalCube()
	{
		return getBlock().isBlockNormalCube(this);
	}
	
	@Override
	default boolean isFullBlock()
	{
		return getBlock().isFullBlock(this);
	}
	
	@Override
	default boolean isFullyOpaque()
	{
		return getBlock().isFullyOpaque(this);
	}
	
	@Override
	default boolean isFullCube()
	{
		return getBlock().isFullCube(this);
	}
	
	@Override
	default boolean isNormalCube()
	{
		return getBlock().isNormalCube(this);
	}
	
	@Override
	default boolean isOpaqueCube()
	{
		return getBlock().isOpaqueCube(this);
	}
	
	@Override
	default boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return getBlock().isSideSolid(this, world, pos, side);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	default boolean isTranslucent()
	{
		return getBlock().isTranslucent(this);
	}
	
	@Override
	default boolean useNeighborBrightness()
	{
		return getBlock().getUseNeighborBrightness(this);
	}
	
	@Override
	default void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, Entity entity)
	{
		getBlock().addCollisionBoxToList(this, worldIn, pos, aabb, list, entity);
	}
	
	@Override
	default boolean canProvidePower()
	{
		return getBlock().canProvidePower(this);
	}
	
	@Override
	default RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	{
		return getBlock().collisionRayTrace(this, worldIn, pos, start, end);
	}
	
	@Override
	default boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return getBlock().doesSideBlockRendering(this, world, pos, side);
	}
	
	@Override
	default boolean hasComparatorInputOverride()
	{
		return getBlock().hasComparatorInputOverride(this);
	}
	
	@Override
	default void neighborChanged(World worldIn, BlockPos pos, Block block)
	{
		getBlock().neighborChanged(this, worldIn, pos, block);
	}
	
	@Override
	default boolean canEntitySpawn(Entity entityIn)
	{
		return getBlock().canEntitySpawn(this, entityIn);
	}
	
	@Override
	default boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param)
	{
		return getBlock().eventReceived(this, worldIn, pos, id, param);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	default boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing)
	{
		return getBlock().shouldSideBeRendered(this, blockAccess, pos, facing);
	}
}
