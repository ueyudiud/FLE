/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.blockstate;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockStateWrapper implements IExtendedBlockState
{
	private IBlockState state;
	
	protected BlockStateWrapper(IBlockState state)
	{
		this.state = state;
	}
	
	public IBlockState getRealState()
	{
		return this.state;
	}
	
	protected abstract BlockStateWrapper wrapState(IBlockState state);
	
	@Override
	public Collection<IProperty<?>> getPropertyKeys()
	{
		return this.state.getPropertyKeys();
	}
	
	@Override
	public <T extends Comparable<T>> T getValue(IProperty<T> property)
	{
		return this.state.getValue(property);
	}
	
	@Override
	public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value)
	{
		return this.state.withProperty(property, value);
	}
	
	@Override
	public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property)
	{
		return wrapState(this.state.cycleProperty(property));
	}
	
	@Override
	public ImmutableMap<IProperty<?>, Comparable<?>> getProperties()
	{
		return this.state.getProperties();
	}
	
	@Override
	public Block getBlock()
	{
		return this.state.getBlock();
	}
	
	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param)
	{
		return this.state.onBlockEventReceived(worldIn, pos, id, param);
	}
	
	@Override
	public void neighborChanged(World worldIn, BlockPos pos, Block dest)
	{
		this.state.neighborChanged(worldIn, pos, dest);
	}
	
	@Override
	public Material getMaterial()
	{
		return this.state.getMaterial();
	}
	
	@Override
	public boolean isFullBlock()
	{
		return this.state.isFullBlock();
	}
	
	@Override
	public boolean canEntitySpawn(Entity entityIn)
	{
		return this.state.canEntitySpawn(entityIn);
	}
	
	@Override
	@Deprecated
	public int getLightOpacity()
	{
		return this.state.getLightOpacity();
	}
	
	@Override
	public int getLightOpacity(IBlockAccess world, BlockPos pos)
	{
		return this.state.getLightOpacity(world, pos);
	}
	
	@Override
	@Deprecated
	public int getLightValue()
	{
		return this.state.getLightValue();
	}
	
	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		return this.state.getLightValue(world, pos);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isTranslucent()
	{
		return this.state.isTranslucent();
	}
	
	@Override
	public boolean useNeighborBrightness()
	{
		return this.state.useNeighborBrightness();
	}
	
	@Override
	public MapColor getMapColor()
	{
		return this.state.getMapColor();
	}
	
	@Override
	public IBlockState withRotation(Rotation rot)
	{
		return wrapState(this.state.withRotation(rot));
	}
	
	@Override
	public IBlockState withMirror(Mirror mirrorIn)
	{
		return wrapState(this.state.withMirror(mirrorIn));
	}
	
	@Override
	public boolean isFullCube()
	{
		return this.state.isFullCube();
	}
	
	@Override
	public EnumBlockRenderType getRenderType()
	{
		return this.state.getRenderType();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos)
	{
		return this.state.getPackedLightmapCoords(source, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue()
	{
		return this.state.getAmbientOcclusionLightValue();
	}
	
	@Override
	public boolean isBlockNormalCube()
	{
		return this.state.isBlockNormalCube();
	}
	
	@Override
	public boolean isNormalCube()
	{
		return this.state.isNormalCube();
	}
	
	@Override
	public boolean canProvidePower()
	{
		return this.state.canProvidePower();
	}
	
	@Override
	public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return this.state.getWeakPower(blockAccess, pos, side);
	}
	
	@Override
	public boolean hasComparatorInputOverride()
	{
		return this.state.hasComparatorInputOverride();
	}
	
	@Override
	public int getComparatorInputOverride(World worldIn, BlockPos pos)
	{
		return this.state.getComparatorInputOverride(worldIn, pos);
	}
	
	@Override
	public float getBlockHardness(World worldIn, BlockPos pos)
	{
		return this.state.getBlockHardness(worldIn, pos);
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos)
	{
		return this.state.getPlayerRelativeBlockHardness(player, worldIn, pos);
	}
	
	@Override
	public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return this.state.getStrongPower(blockAccess, pos, side);
	}
	
	@Override
	public EnumPushReaction getMobilityFlag()
	{
		return this.state.getMobilityFlag();
	}
	
	@Override
	public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos)
	{
		return wrapState(this.state.getActualState(blockAccess, pos));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
	{
		return this.state.getSelectedBoundingBox(worldIn, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing)
	{
		return this.state.shouldSideBeRendered(blockAccess, pos, facing);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return this.state.isOpaqueCube();
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos)
	{
		return this.state.getCollisionBoundingBox(worldIn, pos);
	}
	
	@Override
	public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB p_185908_3_, List<AxisAlignedBB> p_185908_4_, Entity entity)
	{
		this.state.addCollisionBoxToList(worldIn, pos, p_185908_3_, p_185908_4_, entity);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos)
	{
		return this.state.getBoundingBox(blockAccess, pos);
	}
	
	@Override
	public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	{
		return this.state.collisionRayTrace(worldIn, pos, start, end);
	}
	
	@Override
	@Deprecated
	public boolean isFullyOpaque()
	{
		return this.state.isFullyOpaque();
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return this.state.doesSideBlockRendering(world, pos, side);
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return this.state.isSideSolid(world, pos, side);
	}
	
	@Override
	public Collection<IUnlistedProperty<?>> getUnlistedNames()
	{
		return this.state instanceof IExtendedBlockState ? ((IExtendedBlockState) this.state).getUnlistedNames() : ImmutableList.of();
	}
	
	@Override
	public <V> V getValue(IUnlistedProperty<V> property)
	{
		return this.state instanceof IExtendedBlockState ? ((IExtendedBlockState) this.state).getValue(property) : null;
	}
	
	@Override
	public <V> IExtendedBlockState withProperty(IUnlistedProperty<V> property, V value)
	{
		return wrapState(this.state instanceof IExtendedBlockState ? ((IExtendedBlockState) this.state).withProperty(property, value) : this.state);
	}
	
	@Override
	public ImmutableMap<IUnlistedProperty<?>, Optional<?>> getUnlistedProperties()
	{
		return this.state instanceof IExtendedBlockState ? ((IExtendedBlockState) this.state).getUnlistedProperties() : ImmutableMap.of();
	}
	
	@Override
	public IBlockState getClean()
	{
		return this.state instanceof IExtendedBlockState ? wrapState(((IExtendedBlockState) this.state).getClean()) : this.state;
	}
	
	@Override
	public int hashCode()
	{
		return this.state.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return this == obj || this.state.equals(obj);
	}
	
	@Override
	public String toString()
	{
		return "wraped : {" + this.state.toString() + "}";
	}
}
