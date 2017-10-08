/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import java.util.List;
import java.util.Random;

import farcore.lib.block.IThermalCustomBehaviorBlock;
import nebula.Log;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.world.IModifiableCoord;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@Optional.InterfaceList({@Optional.Interface(iface = "farcore.lib.block.IThermalCustomBehaviorBlock", modid = "farcore")})
public abstract class BlockSubBehavior extends BlockBase
implements IUpdateDelayBlock, ISmartFallableBlock, IToolableBlock, IThermalCustomBehaviorBlock
{
	public BlockSubBehavior(String name, Material materialIn)
	{
		super(name, materialIn);
	}
	
	public BlockSubBehavior(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
	}
	
	public BlockSubBehavior(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
	}
	
	public BlockSubBehavior(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
	}
	
	protected abstract IBlockBehavior getBehavior(IBlockState state);
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		try
		{
			return getBehavior(state).getAmbientOcclusionLightValue(this, state);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getAmbientOcclusionLightValue(state);
		}
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		try
		{
			return getBehavior(blockState).getBlockHardness(this, blockState, worldIn, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getBlockHardness(blockState, worldIn, pos);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		try
		{
			return getBehavior(state).getBoundingBox(this, state, source, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getBoundingBox(state, source, pos);
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		try
		{
			return getBehavior(blockState).getCollisionBoundingBox(this, blockState, worldIn, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getCollisionBoundingBox(blockState, worldIn, pos);
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune,
			boolean silkTouch)
	{
		try
		{
			return getBehavior(state).getDrops(this, state, pos, world, tile, fortune, silkTouch);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getDrops(world, pos, state, tile, fortune, silkTouch);
		}
	}
	
	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		try
		{
			return getBehavior(state).getExpDrop(this, state, world, pos, fortune);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getExpDrop(state, world, pos, fortune);
		}
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).getExplosionResistance(this, state, world, pos, exploder, explosion);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getExplosionResistance(world, pos, exploder, explosion);
		}
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).getFireSpreadSpeed(this, state, world, pos, face);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getFireSpreadSpeed(world, pos, face);
		}
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).getFlammability(this, state, world, pos, face);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getFlammability(world, pos, face);
		}
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		try
		{
			return getBehavior(state).getHarvestLevel(this, state);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getHarvestLevel(state);
		}
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		try
		{
			return getBehavior(state).getHarvestTool(this, state);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getHarvestTool(state);
		}
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		try
		{
			return getBehavior(state).getLightOpacity(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getLightOpacity(state, world, pos);
		}
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		try
		{
			return getBehavior(state).getLightValue(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getLightValue(state, world, pos);
		}
	}
	
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		try
		{
			return getBehavior(state).getMapColor(this, state, this.blockMapColor);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getMapColor(state);
		}
	}
	
	@Override
	public Material getMaterial(IBlockState state)
	{
		try
		{
			return getBehavior(state).getMaterial(this, state, this.blockMaterial);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getMaterial(state);
		}
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		try
		{
			return getBehavior(state).getMobilityFlag(this, state, this.blockMaterial.getMobilityFlag());
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getMobilityFlag(state);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		try
		{
			return getBehavior(state).getPackedLightmapCoords(this, state, source, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getPackedLightmapCoords(state, source, pos);
		}
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player)
	{
		return super.getPickBlock(state, target, world, pos, player);
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		try
		{
			return getBehavior(blockState).getStrongPower(this, blockState, blockAccess, pos, side);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getStrongPower(blockState, blockAccess, pos, side);
		}
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		try
		{
			return getBehavior(blockState).getWeakPower(this, blockState, blockAccess, pos, side);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getWeakPower(blockState, blockAccess, pos, side);
		}
	}
	
	@Override
	public boolean getWeakChanges(IBlockAccess world, BlockPos pos)
	{
		return super.getWeakChanges(world, pos);
	}
	
	@Override
	public float getEnchantPowerBonus(World world, BlockPos pos)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).getEnchantPowerBonus(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.getEnchantPowerBonus(world, pos);
		}
	}
	
	@Override
	public Boolean isAABBInsideMaterial(World world, BlockPos pos, AxisAlignedBB boundingBox, Material materialIn)
	{
		// TODO Auto-generated method stub
		return super.isAABBInsideMaterial(world, pos, boundingBox, materialIn);
	}
	
	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).isFireSource(this, state, world, pos, side);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isFireSource(world, pos, side);
		}
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).isFlammable(this, state, world, pos, face);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isFlammable(world, pos, face);
		}
	}
	
	@Override
	public boolean isBurning(IBlockAccess world, BlockPos pos)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).isBurning(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isBurning(world, pos);
		}
	}
	
	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	{
		try
		{
			return getBehavior(state).isLadder(this, state, world, pos, entity);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isLadder(state, world, pos, entity);
		}
	}
	
	@Override
	public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		try
		{
			return getBehavior(state).isLeaves(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isLeaves(state, world, pos);
		}
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		try
		{
			return getBehavior(base_state).isSideSolid(this, base_state, world, pos, side);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isSideSolid(base_state, world, pos, side);
		}
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		try
		{
			return getBehavior(state).isToolEffective(this, state, type);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isToolEffective(type, state);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isTranslucent(IBlockState state)
	{
		try
		{
			return getBehavior(state).isTranslucent(this, state);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isTranslucent(state);
		}
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		try
		{
			IBlockState state = worldIn.getBlockState(pos);
			return getBehavior(state).isReplaceable(this, state, worldIn, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isReplaceable(worldIn, pos);
		}
	}
	
	@Override
	public boolean isWood(IBlockAccess world, BlockPos pos)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).isWood(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isWood(world, pos);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		try
		{
			return getBehavior(state).onBlockActivated(this, state, worldIn, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
		return false;
	}
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		try
		{
			IBlockState state = worldIn.getBlockState(pos);
			getBehavior(state).onBlockClicked(this, state, worldIn, pos, playerIn);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			getBehavior(state).onBlockExploded(this, state, world, pos, explosion);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
	{
		;
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		try
		{
			getBehavior(state).onBlockDestroyedByPlayer(this, state, worldIn, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	protected boolean onBlockHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player,
			boolean silkHarvest)
	{
		try
		{
			return getBehavior(state).onBlockHarvest(this, state, worldIn, pos, player, silkHarvest);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
		return super.onBlockHarvest(worldIn, pos, state, player, silkHarvest);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		try
		{
			getBehavior(state).onBlockHarvested(this, state, worldIn, pos, player);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			EnumFacing facing, ItemStack stack)
	{
		try
		{
			getBehavior(state).onBlockPlacedBy(this, state, worldIn, pos, placer, facing, stack);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		try
		{
			getBehavior(state).onEntityCollidedWithBlock(this, state, worldIn, pos, entityIn);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		try
		{
			IBlockState state = worldIn.getBlockState(pos);
			getBehavior(state).onEntityWalk(this, state, worldIn, pos, entityIn);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		try
		{
			IBlockState state = worldIn.getBlockState(pos);
			getBehavior(state).onFallenUpon(this, state, worldIn, pos, entityIn, fallDistance);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	//So how can I get block state???
	@Override
	public void onLanded(World worldIn, Entity entityIn)
	{
		entityIn.motionY = 0.0F;
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			getBehavior(state).onNeighborChange(this, state, world, pos, neighbor);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public int getCheckRange(IBlockState state)
	{
		try
		{
			return getBehavior(state).getCheckRange(this, state);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return 2;
		}
	}
	
	@Override
	public void notifyAfterTicking(IBlockState state, World world, BlockPos pos, IBlockState changed)
	{
		try
		{
			getBehavior(state).notifyAfterTicking(this, state, world, pos, changed);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public boolean canBreakBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).canBreakBlock(this, state, world, pos, player);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.canBreakBlock(world, pos, player);
		}
	}
	
	@Override
	public boolean canBreakEffective(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		try
		{
			return getBehavior(state).canBreakEffective(this, state, worldIn, pos, player);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.canBreakEffective(state, player, worldIn, pos);
		}
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		try
		{
			return getBehavior(state).canConnectRedstone(this, state, world, pos, side);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.canConnectRedstone(state, world, pos, side);
		}
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type)
	{
		try
		{
			return getBehavior(state).canCreatureSpawn(this, state, world, pos, type);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.canCreatureSpawn(state, world, pos, type);
		}
	}
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		try
		{
			return getBehavior(state).canEntityDestroy(this, state, world, pos, entity);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.canEntityDestroy(state, world, pos, entity);
		}
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		try
		{
			return getBehavior(state).canPlaceTorchOnTop(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.canPlaceTorchOnTop(state, world, pos);
		}
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		try
		{
			return getBehavior(state).canSilkHarvest(this, state, world, pos, player);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.canSilkHarvest(world, pos, state, player);
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
			IPlantable plantable)
	{
		try
		{
			return getBehavior(state).canSustainPlant(this, state, world, pos, direction, plantable);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.canSustainPlant(state, world, pos, direction, plantable);
		}
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		try
		{
			getBehavior(state).randomTick(this, state, worldIn, pos, random);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		try
		{
			getBehavior(state).updateTick(this, state, worldIn, pos, rand);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		try
		{
			getBehavior(stateIn).randomDisplayTick(this, stateIn, worldIn, pos, rand);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).canHarvestBlock(this, state, world, pos, player);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.canHarvestBlock(world, pos, player);
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		try
		{
			getBehavior(state).breakBlock(this, state, worldIn, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		try
		{
			return getBehavior(state).isOpaqueCube(this, state);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isOpaqueCube(state);
		}
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state)
	{
		try
		{
			return getBehavior(state).isNormalCube(this, state);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return super.isBlockNormalCube(state);
		}
	}
	
	@Override
	public boolean onDropFallenAsItem(World world, BlockPos pos, IBlockState state, NBTTagCompound tileNBT)
	{
		try
		{
			return getBehavior(state).onDropFallenAsItem(this, state, world, pos, tileNBT);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return false;
		}
	}
	
	@Override
	public float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target)
	{
		try
		{
			return getBehavior(block.getBlock()).onFallOnEntity(this, block.getBlock(), world, block, target);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return 0;
		}
	}
	
	@Override
	public boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT)
	{
		try
		{
			return getBehavior(state).onFallOnGround(this, state, world, pos, height, tileNBT);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return false;
		}
	}
	
	@Override
	public void onStartFalling(World world, BlockPos pos)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			getBehavior(state).onStartFalling(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
		}
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, int level, ItemStack stack, World world,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).onToolClick(this, state, world, pos, player, tool, level, stack, side, hitX, hitY, hitZ);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return IToolableTile.DEFAULT_RESULT;
		}
	}
	
	@Override
	public boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state)
	{
		if (!world.canBlockBePlaced(state.getBlock(), pos, true, EnumFacing.UP, (Entity)null, (ItemStack)null))
		{
			return false;
		}
		try
		{
			return getBehavior(state).canFallingBlockStay(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return EntityFallingBlockExtended.canFallAt(world, pos, state);
		}
	}
	
	@Override
	@Optional.Method(modid = "farcore")
	public int getFireEncouragement(World world, BlockPos pos)
	{
		try
		{
			IBlockState state = world.getBlockState(pos);
			return getBehavior(state).getFireEncouragement(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return 0;
		}
	}
	
	@Override
	@Optional.Method(modid = "farcore")
	public double getThermalConduct(World world, BlockPos pos, IBlockState state)
	{
		try
		{
			return getBehavior(state).getThermalConduct(this, state, world, pos);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return 0;
		}
	}
	
	@Override
	@Optional.Method(modid = "farcore")
	public boolean onBurn(IModifiableCoord coord, float burnHardness, Direction direction)
	{
		try
		{
			IBlockState state = coord.getBlockState();
			return getBehavior(state).onBurn(this, state, coord, burnHardness, direction);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return false;
		}
	}
	
	@Override
	@Optional.Method(modid = "farcore")
	public boolean onBurningTick(IModifiableCoord coord, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		try
		{
			IBlockState state = coord.getBlockState();
			return getBehavior(state).onBurningTick(this, state, coord, rand, fireSourceDir, fireState);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return false;
		}
	}
}