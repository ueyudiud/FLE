/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.block.delegated;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import nebula.client.util.UnlocalizedList;
import nebula.common.block.BlockBase;
import nebula.common.block.delegated.ITileDelegate.ITD_Box;
import nebula.common.block.delegated.ITileDelegate.ITD_Flammable;
import nebula.common.block.delegated.ITileDelegate.ITD_Harvestibility;
import nebula.common.block.delegated.ITileDelegate.ITD_Light;
import nebula.common.block.delegated.ITileDelegate.ITD_PackedLightmap;
import nebula.common.block.delegated.ITileDelegate.ITD_PlayerRelativeBlockHardness;
import nebula.common.world.ICoord;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class BlockDelegated<B extends BlockDelegated<B, T>, T extends TileEntity> extends BlockBase
{
	public BlockDelegated(String name, Material materialIn)
	{
		super(name, materialIn);
	}
	
	public BlockDelegated(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
	}
	
	public BlockDelegated(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
	}
	
	public BlockDelegated(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		for (ITileDelegate delegate : delegates())
		{
			delegate.renderload(this);
		}
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		for (ITileDelegate delegate : delegates())
		{
			delegate.postload(this);
		}
	}
	
	protected abstract Iterable<ITileDelegate<? super B, ? super T>> delegates();
	
	protected abstract ITileDelegate<? super B, ? super T> delegateOf(IBlockState state);
	
	protected abstract ITileDelegate<? super B, ? super T> delegateOf(ItemStack stack);
	
	protected final T getTileOrLazy(ITileDelegate<? super B, ? super T> delegate, IBlockState state, World world, BlockPos pos)
	{
		TileEntity tile = world.getChunkFromBlockCoords(pos).getTileEntity(pos, EnumCreateEntityType.CHECK);
		if (tile == null && delegate.hasTileEntity((B) this, state))
		{
			tile = delegate.createTileEntity((B) this, state, world);
			tile.setWorld(world);
			tile.setPos(pos);
		}
		return (T) tile;
	}
	
	protected final T getTileOrLazy(ITileDelegate<? super B, ? super T> delegate, IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if (world instanceof World)
		{
			return getTileOrLazy(delegate, state, (World) world, pos);
		}
		else
		{
			return (T) world.getTileEntity(pos);
		}
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_AddCollisionBox)
		{
			((ITileDelegate.ITD_AddCollisionBox) delegate).addCollisionBoxToList(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), entityBox, collidingBoxes, entityIn);
		}
		else
		{
			super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_RenderingEffect)
		{
			return ((ITileDelegate.ITD_RenderingEffect) delegate).addDestroyEffects(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, manager);
		}
		else
		{
			return super.addDestroyEffects(world, pos, manager);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_RenderingEffect)
		{
			return ((ITileDelegate.ITD_RenderingEffect) delegate).addHitEffects(this, getTileOrLazy(delegate, state, worldObj, target.getBlockPos()), state, worldObj, target, manager);
		}
		else
		{
			return super.addHitEffects(state, worldObj, target, manager);
		}
	}
	
	@Override
	public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_RenderingEffect)
		{
			return ((ITileDelegate.ITD_RenderingEffect) delegate).addLandingEffects(this, getTileOrLazy(delegate, state, worldObj, blockPosition), state, worldObj, ICoord.create(worldObj, blockPosition), entity, numberOfParticles);
		}
		else
		{
			return super.addLandingEffects(state, worldObj, blockPosition, iblockstate, entity, numberOfParticles);
		}
	}
	
	@Override
	public void beginLeavesDecay(IBlockState state, World world, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Tree)
		{
			((ITileDelegate.ITD_Tree) delegate).beginLeavesDecay(this, getTileOrLazy(delegate, state, world, pos), state, world, ICoord.create(world, pos));
		}
		else
		{
			super.beginLeavesDecay(state, world, pos);
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_BreakBlock)
		{
			((ITileDelegate.ITD_BreakBlock) delegate).breakBlock(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos));
		}
		else
		{
			super.breakBlock(worldIn, pos, state);
		}
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Replacable)
		{
			return ((ITileDelegate.ITD_Replacable) delegate).canBeReplacedByLeaves(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.canBeReplacedByLeaves(state, world, pos);
		}
	}
	
	@Override
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
	{
		return delegateOf(state).canCollideCheck((B) this, state, hitIfLiquid);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_RedstonePower)
		{
			return ((ITileDelegate.ITD_RedstonePower) delegate).canConnectRedstone(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, side);
		}
		else
		{
			return super.canConnectRedstone(state, world, pos, side);
		}
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_CreatureSpawn)
		{
			return ((ITileDelegate.ITD_CreatureSpawn) delegate).canCreatureSpawn(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, type);
		}
		else
		{
			return super.canCreatureSpawn(state, world, pos, type);
		}
	}
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_BreakAbility)
		{
			return ((ITileDelegate.ITD_BreakAbility) delegate).canEntityDestroy(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, entity);
		}
		else
		{
			return super.canEntityDestroy(state, world, pos, entity);
		}
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Harvestibility)
		{
			return ((ITileDelegate.ITD_Harvestibility) delegate).canHarvestBlock(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, player);
		}
		else
		{
			return super.canHarvestBlock(world, pos, player);
		}
	}
	
	@Override
	public boolean canProvidePower(IBlockState state)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_RedstonePower)
		{
			return ((ITileDelegate.ITD_RedstonePower) delegate).canProvidePower(this, state);
		}
		else
		{
			return super.canProvidePower(state);
		}
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Solid)
		{
			return ((ITileDelegate.ITD_Solid) delegate).canPlaceTorchOnTop(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.canPlaceTorchOnTop(state, world, pos);
		}
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return delegateOf(state).canRenderInLayer(state, layer);
	}
	
	@Override
	public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack)
	{
		IBlockState state = worldIn.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Replacable)
		{
			return ((ITileDelegate.ITD_Replacable) delegate).canReplace(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), side, stack);
		}
		else
		{
			return super.canReplace(worldIn, pos, side, stack);
		}
	}
	
	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Tree)
		{
			return ((ITileDelegate.ITD_Tree) delegate).canSustainLeaves(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.canSustainLeaves(state, world, pos);
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Soil)
		{
			return ((ITileDelegate.ITD_Soil) delegate).canSustainPlant(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, direction, plantable);
		}
		else
		{
			return super.canSustainPlant(state, world, pos, direction, plantable);
		}
	}
	
	@Override
	public void fillWithRain(World worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_UpdateTick)
		{
			((ITileDelegate.ITD_UpdateTick) delegate).rainTick(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos));
		}
		else
		{
			super.fillWithRain(worldIn, pos);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		ITileDelegate delegate = delegateOf(state);
		return delegate instanceof ITileDelegate.ITD_AmbientOcclusionLightValue ?
				((ITileDelegate.ITD_AmbientOcclusionLightValue) delegate).getAmbientOcclusionLightValue(this, state) :
					super.getAmbientOcclusionLightValue(state);
	}
	
	@Override
	public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos)
	{
		ITileDelegate delegate = delegateOf(state);
		return delegate instanceof ITileDelegate.ITD_Colored ?
				((ITileDelegate.ITD_Colored) delegate).getBeaconColorMultiplier(this, getTileOrLazy(delegate, state, world, pos), state, world, ICoord.create(world, pos), beaconPos) :
					super.getBeaconColorMultiplier(state, world, pos, beaconPos);
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(blockState);
		return delegate.getBlockHardness(this, getTileOrLazy(delegate, blockState, worldIn, pos), blockState, worldIn, ICoord.create(worldIn, pos));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Box)
		{
			return ((ITileDelegate.ITD_Box) delegate).getBoundBox(this, getTileOrLazy(delegate, state, source, pos), state, source, pos);
		}
		else
		{
			return super.getBoundingBox(state, source, pos);
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(blockState);
		if (delegate instanceof ITileDelegate.ITD_Box)
		{
			return ((ITileDelegate.ITD_Box) delegate).getCollisionBoundingBox(this, getTileOrLazy(delegate, blockState, worldIn, pos), blockState, worldIn, ICoord.create(worldIn, pos));
		}
		else
		{
			return super.getCollisionBoundingBox(blockState, worldIn, pos);
		}
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(blockState);
		if (delegate instanceof ITileDelegate.ITD_ComparactorInputOverride)
		{
			return ((ITileDelegate.ITD_ComparactorInputOverride) delegate).getComparatorInputOverride(this, getTileOrLazy(delegate, blockState, worldIn, pos), blockState, worldIn, ICoord.create(worldIn, pos));
		}
		else
		{
			return super.getComparatorInputOverride(blockState, worldIn, pos);
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Harvest)
		{
			return ((ITileDelegate.ITD_Harvest) delegate).getDrops(this, tile, state, world, pos, fortune, silkTouch);
		}
		else
		{
			return super.getDrops(world, pos, state, tile, fortune, silkTouch);
		}
	}
	
	@Override
	public float getEnchantPowerBonus(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_EnchantPowerBonus)
		{
			return ((ITileDelegate.ITD_EnchantPowerBonus) delegate).getEnchantPowerBonus(this, getTileOrLazy(delegate, state, world, pos), world, ICoord.create(world, pos));
		}
		else
		{
			return super.getEnchantPowerBonus(world, pos);
		}
	}
	
	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Harvest)
		{
			return ((ITileDelegate.ITD_Harvest) delegate).getExpDrop(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, fortune);
		}
		else
		{
			return super.getExpDrop(state, world, pos, fortune);
		}
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		return delegate.getExplosionResistance(this, getTileOrLazy(delegate, state, world, pos), world, ICoord.create(world, pos), exploder, explosion);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITD_Flammable)
		{
			return ((ITD_Flammable) delegate).getFireSpreadSpeed(this, getTileOrLazy(delegate, state, world, pos), world, pos, face);
		}
		else
		{
			return super.getFireSpreadSpeed(world, pos, face);
		}
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITD_Flammable)
		{
			return ((ITD_Flammable) delegate).getFlammability(this, getTileOrLazy(delegate, state, world, pos), world, pos, face);
		}
		else
		{
			return super.getFlammability(world, pos, face);
		}
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITD_Harvestibility)
		{
			return ((ITD_Harvestibility) delegate).getHarvestLevel(this, state);
		}
		else
		{
			return super.getHarvestLevel(state);
		}
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITD_Harvestibility)
		{
			return ((ITD_Harvestibility) delegate).getHarvestTool(this, state);
		}
		else
		{
			return super.getHarvestTool(state);
		}
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITD_PlayerRelativeBlockHardness)
		{
			return ((ITD_PlayerRelativeBlockHardness) delegate).getPlayerRelativeBlockHardness(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), player);
		}
		else
		{
			return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
		}
	}
	
	@Override
	public int getLightOpacity(IBlockState state)
	{
		return delegateOf(state).isOpaqueCube((B) this, state) ? 255 : 1;
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITD_Light)
		{
			return ((ITD_Light) delegate).getLightOpacity(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.getLightOpacity(state, world, pos);
		}
	}
	
	@Override
	public int getLightValue(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITD_Light)
		{
			return ((ITD_Light) delegate).getLightValue(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.getLightValue(state, world, pos);
		}
	}
	
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		return delegateOf(state).getMapColor((B) this, state);
	}
	
	@Override
	public Material getMaterial(IBlockState state)
	{
		return delegateOf(state).getMaterial((B) this, state);
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return delegateOf(state).getMobilityFlag((B) this, state);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITD_PackedLightmap)
		{
			return ((ITD_PackedLightmap) delegate).getPackedLightmapCoords(this, getTileOrLazy(delegate, state, source, pos), state, source, pos);
		}
		else
		{
			return super.getPackedLightmapCoords(state, source, pos);
		}
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return delegateOf(state).getRenderType((B) this, state);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITD_Box)
		{
			return ((ITD_Box) delegate).getSelectedBoundingBox(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos));
		}
		else
		{
			return super.getSelectedBoundingBox(state, worldIn, pos);
		}
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity)
	{
		ITileDelegate delegate = delegateOf(state);
		return delegate.getSoundType(this, getTileOrLazy(delegate, state, world, pos), state, world, ICoord.create(world, pos), entity);
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		ITileDelegate delegate = delegateOf(blockState);
		if (delegate instanceof ITileDelegate.ITD_RedstonePower)
		{
			return ((ITileDelegate.ITD_RedstonePower) delegate).getStrongPower(this, getTileOrLazy(delegate, blockState, blockAccess, pos), blockState, blockAccess, pos, side);
		}
		else
		{
			return super.getStrongPower(blockState, blockAccess, pos, side);
		}
	}
	
	@Override
	public String getTranslateNameForItemStack(ItemStack stack)
	{
		return delegateOf(stack).getUnlocalizedName((B) this, stack) + ".name";
	}
	
	@Override
	public boolean getUseNeighborBrightness(IBlockState state)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Light)
		{
			return ((ITileDelegate.ITD_Light) delegate).getUseNeighborBrightness(this, state);
		}
		else
		{
			return super.getUseNeighborBrightness(state);
		}
	}
	
	@Override
	public boolean getWeakChanges(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_RedstonePower)
		{
			return ((ITileDelegate.ITD_RedstonePower) delegate).getWeakChanges(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.getWeakChanges(world, pos);
		}
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		ITileDelegate delegate = delegateOf(blockState);
		if (delegate instanceof ITileDelegate.ITD_RedstonePower)
		{
			return ((ITileDelegate.ITD_RedstonePower) delegate).getWeakPower(this, getTileOrLazy(delegate, blockState, blockAccess, pos), blockState, blockAccess, pos, side);
		}
		else
		{
			return super.getWeakPower(blockState, blockAccess, pos, side);
		}
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Harvest)
		{
			((ITileDelegate.ITD_Harvest) delegate).harvestBlock(this, te, worldIn, state, ICoord.create(worldIn, pos), player, stack);
		}
		else
		{
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return delegateOf(state) instanceof ITileDelegate.ITD_ComparactorInputOverride;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return delegateOf(state).hasTileEntity((B) this, state);
	}
	
	@Override
	public Boolean isAABBInsideMaterial(World world, BlockPos pos, AxisAlignedBB boundingBox, Material materialIn)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_MaterialIn)
		{
			return ((ITileDelegate.ITD_MaterialIn) delegate).isAABBInsideMaterial(this, getTileOrLazy(delegate, state, world, pos), state, world, ICoord.create(world, pos), boundingBox, materialIn);
		}
		else
		{
			return super.isAABBInsideMaterial(world, pos, boundingBox, materialIn);
		}
	}
	
	@Override
	public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_MetalBlock)
		{
			return ((ITileDelegate.ITD_MetalBlock) delegate).isBeaconBase(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, beacon);
		}
		else
		{
			return super.isBeaconBase(world, pos, beacon);
		}
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState state = worldIn.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Solid)
		{
			return ((ITileDelegate.ITD_Solid) delegate).isBlockSolid(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, pos, side);
		}
		else
		{
			return super.isBlockSolid(worldIn, pos, side);
		}
	}
	
	@Override
	public boolean isBurning(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Burning)
		{
			return ((ITileDelegate.ITD_Burning) delegate).isBurning(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.isBurning(world, pos);
		}
	}
	
	@Override
	public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead)
	{
		ITileDelegate delegate = delegateOf(iblockstate);
		if (delegate instanceof ITileDelegate.ITD_MaterialIn)
		{
			return ((ITileDelegate.ITD_MaterialIn) delegate).isEntityInsideMaterial(this, getTileOrLazy(delegate, iblockstate, world, blockpos), iblockstate, world, blockpos, entity, yToTest, materialIn, testingHead);
		}
		else
		{
			return super.isEntityInsideMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
		}
	}
	
	@Override
	public boolean isFertile(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Soil)
		{
			return ((ITileDelegate.ITD_Soil) delegate).isFertile(this, getTileOrLazy(delegate, state, world, pos), state, world, ICoord.create(world, pos));
		}
		else
		{
			return super.isFertile(world, pos);
		}
	}
	
	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Flammable)
		{
			return ((ITileDelegate.ITD_Flammable) delegate).isFireSource(this, getTileOrLazy(delegate, state, world, pos), state, world, ICoord.create(world, pos), side);
		}
		else
		{
			return super.isFireSource(world, pos, side);
		}
	}
	
	@Override
	public boolean isFoliage(IBlockAccess world, BlockPos pos)
	{
		// TODO Auto-generated method stub
		return super.isFoliage(world, pos);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return delegateOf(state).isFullCube((B) this, state);
	}
	
	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Ladder)
		{
			return ((ITileDelegate.ITD_Ladder) delegate).isLadder(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, entity);
		}
		else
		{
			return super.isLadder(state, world, pos, entity);
		}
	}
	
	@Override
	public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Tree)
		{
			return ((ITileDelegate.ITD_Tree) delegate).isLeaves(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.isLeaves(state, world, pos);
		}
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Solid)
		{
			return ((ITileDelegate.ITD_Solid) delegate).isNormalCube(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.isNormalCube(state, world, pos);
		}
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Replacable)
		{
			return ((ITileDelegate.ITD_Replacable) delegate).isReplaceable(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, pos);
		}
		else
		{
			return super.isReplaceable(worldIn, pos);
		}
	}
	
	@Override
	public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Replacable)
		{
			return ((ITileDelegate.ITD_Replacable) delegate).isReplaceableOreGen(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, target);
		}
		else
		{
			return super.isReplaceableOreGen(state, world, pos, target);
		}
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		ITileDelegate delegate = delegateOf(base_state);
		if (delegate instanceof ITileDelegate.ITD_Solid)
		{
			return ((ITileDelegate.ITD_Solid) delegate).isSideSolid(this, getTileOrLazy(delegate, base_state, world, pos), base_state, world, pos, side);
		}
		else
		{
			return super.isSideSolid(base_state, world, pos, side);
		}
	}
	
	@Override
	public boolean isTranslucent(IBlockState state)
	{
		return delegateOf(state).isTranslucent((B) this, state);
	}
	
	@Override
	public boolean isWood(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Tree)
		{
			return ((ITileDelegate.ITD_Tree) delegate).isWood(this, getTileOrLazy(delegate, state, world, pos), state, world, pos);
		}
		else
		{
			return super.isWood(world, pos);
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_NeighbourChanged)
		{
			((ITileDelegate.ITD_NeighbourChanged) delegate).neighborChanged(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), blockIn);
		}
		else
		{
			super.neighborChanged(state, worldIn, pos, blockIn);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Activated)
		{
			return ((ITileDelegate.ITD_Activated) delegate).onBlockActivated(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		}
		else
		{
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_BlockAdded)
		{
			((ITileDelegate.ITD_BlockAdded) delegate).onBlockAdded(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos));
		}
		else
		{
			super.onBlockAdded(worldIn, pos, state);
		}
	}
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		IBlockState state = worldIn.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Click)
		{
			((ITileDelegate.ITD_Click) delegate).onBlockClicked(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), playerIn);
		}
		else
		{
			super.onBlockClicked(worldIn, pos, playerIn);
		}
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
	{
		IBlockState state = worldIn.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Destoryed)
		{
			((ITileDelegate.ITD_Destoryed) delegate).onBlockDestroyedByExplosion(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), explosionIn);
		}
		else
		{
			super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Destoryed)
		{
			((ITileDelegate.ITD_Destoryed) delegate).onBlockDestroyedByPlayer(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos));
		}
		else
		{
			super.onBlockDestroyedByPlayer(worldIn, pos, state);
		}
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Exploded)
		{
			((ITileDelegate.ITD_Exploded) delegate).onBlockExploded(this, getTileOrLazy(delegate, state, world, pos), state, world, ICoord.create(world, pos), explosion);
		}
		else
		{
			super.onBlockExploded(world, pos, explosion);
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_EntityNearby)
		{
			((ITileDelegate.ITD_EntityNearby) delegate).onEntityCollidedWithBlock(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), entityIn);
		}
		else
		{
			super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		}
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		IBlockState state = worldIn.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_EntityNearby)
		{
			((ITileDelegate.ITD_EntityNearby) delegate).onEntityWalk(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), entityIn);
		}
		else
		{
			super.onEntityWalk(worldIn, pos, entityIn);
		}
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		IBlockState state = worldIn.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_EntityNearby)
		{
			((ITileDelegate.ITD_EntityNearby) delegate).onFallenUpon(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), entityIn, fallDistance);
		}
		else
		{
			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		}
	}
	
	@Override
	public void onLanded(World worldIn, Entity entityIn)
	{
		BlockPos pos = new BlockPos(entityIn.posX, entityIn.posY - .2, entityIn.posZ);
		IBlockState state = worldIn.getBlockState(pos);
		if (state.getBlock() != this)
		{
			state.getBlock().onLanded(worldIn, entityIn);
			return;
		}
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_EntityNearby)
		{
			((ITileDelegate.ITD_EntityNearby) delegate).onLanded(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), entityIn);
		}
		else
		{
			super.onLanded(worldIn, entityIn);
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		IBlockState state = world.getBlockState(pos);
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_NeighbourChanged)
		{
			((ITileDelegate.ITD_NeighbourChanged) delegate).onNeighborChange(this, getTileOrLazy(delegate, state, world, pos), state, world, pos, neighbor);
		}
		else
		{
			super.onNeighborChange(world, pos, neighbor);
		}
	}
	
	@Override
	public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_Soil)
		{
			((ITileDelegate.ITD_Soil) delegate).onPlantGrow(this, getTileOrLazy(delegate, state, world, pos), state, world, ICoord.create(world, pos), source);
		}
		else
		{
			super.onPlantGrow(state, world, pos, source);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		ITileDelegate delegate = delegateOf(stateIn);
		if (delegate instanceof ITileDelegate.ITD_RenderingEffect)
		{
			((ITileDelegate.ITD_RenderingEffect) delegate).randomDisplayTick(this, getTileOrLazy(delegate, stateIn, worldIn, pos), stateIn, worldIn, ICoord.create(worldIn, pos), rand);
		}
		else
		{
			super.randomDisplayTick(stateIn, worldIn, pos, rand);
		}
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_UpdateTick)
		{
			((ITileDelegate.ITD_UpdateTick) delegate).randomTick(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), random);
		}
		else
		{
			super.randomTick(worldIn, pos, state, random);
		}
	}
	
	@Override
	public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color)
	{
		// TODO Auto-generated method stub
		return super.recolorBlock(world, pos, side, color);
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_RemovedByPlayer)
		{
			return ((ITileDelegate.ITD_RemovedByPlayer) delegate).removedByPlayer(this, getTileOrLazy(delegate, state, world, pos), state, world, ICoord.create(world, pos), player, willHarvest);
		}
		else
		{
			return super.removedByPlayer(state, world, pos, player, willHarvest);
		}
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		// TODO Auto-generated method stub
		return super.rotateBlock(world, pos, axis);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		ITileDelegate delegate = delegateOf(state);
		if (delegate instanceof ITileDelegate.ITD_UpdateTick)
		{
			((ITileDelegate.ITD_UpdateTick) delegate).updateTick(this, getTileOrLazy(delegate, state, worldIn, pos), state, worldIn, ICoord.create(worldIn, pos), rand);
		}
		else
		{
			super.updateTick(worldIn, pos, state, rand);
		}
	}
	
	@Override
	protected void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList tooltip, boolean advanced)
	{
		delegateOf(stack).addInformation((B) this, stack, player, tooltip, advanced);
	}
}
