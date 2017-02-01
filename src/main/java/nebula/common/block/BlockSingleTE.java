package nebula.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddLandingEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockAdded;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockClicked;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockDestroyedByPlayer;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockExploded;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockHarvest;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockHarvested;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BreakBlock;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_DisplayUpdate;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_EntityCollidedWithBlock;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_EntityFallenUpon;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_EntityLanded;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_EntityWalk;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_FillWithRain;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_PlantGrow;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_Update;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BeaconBase;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ComparatorInputOverride;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ConnectRedstone;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_CreatureSpawn;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_EnchantPowerBonus;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_EntityDestroy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_FireSource;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_FireSpreadSpeed;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Flammability;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Flammable;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Light;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_RedstonePower;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_SideSolid;
import nebula.common.tile.IUpdatableTile;
import nebula.common.util.Direction;
import nebula.common.util.TileEntities;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@InterfaceList({@Interface(iface = "farcore.lib.block.IThermalCustomBehaviorBlock", modid = "farcore")})
public abstract class BlockSingleTE extends BlockBase
implements ITileEntityProvider
{
	public BlockSingleTE(String name, Material materialIn)
	{
		super(name, materialIn);
	}
	public BlockSingleTE(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
	}
	public BlockSingleTE(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
	}
	public BlockSingleTE(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tile = createTileEntity(worldIn, state);
		worldIn.setTileEntity(pos, tile);
		if(tile instanceof ITB_BlockAdded)
		{
			((ITB_BlockAdded) tile).onBlockAdded(state);
		}
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest)
	{
		TileEntity tile = world.getTileEntity(pos);
		boolean flag = super.removedByPlayer(state, world, pos, player, willHarvest);
		if(flag && (tile instanceof ITB_BlockDestroyedByPlayer))
		{
			((ITB_BlockDestroyedByPlayer) tile).onBlockDestroyedByPlayer(state);
		}
		return flag;
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
	}
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		RayTraceResult result = Worlds.rayTrace(worldIn, playerIn, false);
		if(result == null) return;
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_BlockClicked)
		{
			((ITB_BlockClicked) tile).onBlockClicked(playerIn, Direction.of(result.sideHit), (float) result.hitVec.xCoord, (float) result.hitVec.yCoord, (float) result.hitVec.zCoord);
		}
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITB_BlockExploded)
		{
			((ITB_BlockExploded) tile).onBlockExploded(explosion);
		}
	}
	
	@Override
	protected boolean onBlockHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player,
			boolean silkHarvest)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_BlockHarvest)
			return ((ITB_BlockHarvest) tile).onBlockHarvest(state, player, silkHarvest);
		return false;
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_BlockHarvested)
		{
			((ITB_BlockHarvested) tile).onBlockHarvested(state, player);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_BlockPlacedBy)
		{
			((ITB_BlockPlacedBy) tile).onBlockPlacedBy(state, placer, stack);
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_EntityCollidedWithBlock)
		{
			((ITB_EntityCollidedWithBlock) tile).onEntityCollidedWithBlock(state, entityIn);
		}
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_EntityWalk)
		{
			((ITB_EntityWalk) tile).onEntityWalk(entityIn);
		}
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_EntityFallenUpon)
		{
			((ITB_EntityFallenUpon) tile).onEntityFallenUpon(entityIn, fallDistance);
		}
		else
		{
			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		}
	}
	
	@Override
	public void onLanded(World worldIn, Entity entityIn)
	{
		int x = MathHelper.floor(entityIn.posX);
		int y = MathHelper.floor(entityIn.posY - 0.2);
		int z = MathHelper.floor(entityIn.posZ);
		TileEntity tile = worldIn.getTileEntity(new BlockPos(x, y, z));
		if(tile instanceof ITB_EntityLanded)
		{
			((ITB_EntityLanded) tile).onEntityLanded(entityIn);
		}
		else
		{
			super.onLanded(worldIn, entityIn);
		}
	}
	
	@Override
	public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITB_PlantGrow)
		{
			((ITB_PlantGrow) tile).onPlantGrow(state, source);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITB_AddDestroyEffects)
			return ((ITB_AddDestroyEffects) tile).addDestroyEffects(manager);
		return super.addDestroyEffects(world, pos, manager);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager)
	{
		TileEntity tile = world.getTileEntity(target.getBlockPos());
		if(tile instanceof ITB_AddHitEffects)
			return ((ITB_AddHitEffects) tile).addHitEffects(target, manager);
		return super.addHitEffects(state, world, target, manager);
	}
	
	@Override
	public boolean addLandingEffects(IBlockState state, WorldServer world, BlockPos blockPosition,
			IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles)
	{
		TileEntity tile = world.getTileEntity(blockPosition);
		if(tile instanceof ITB_AddLandingEffects)
			return ((ITB_AddLandingEffects) tile).addLandingEffects(state, iblockstate, entity, numberOfParticles);
		return super.addLandingEffects(state, world, blockPosition, iblockstate, entity, numberOfParticles);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_ConnectRedstone)
			return ((ITP_ConnectRedstone) tile).canConnectRedstone(state, Direction.of(side));
		return super.canConnectRedstone(state, world, pos, side);
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_CreatureSpawn)
			return ((ITP_CreatureSpawn) tile).canCreatureSpawn(state, type);
		return super.canCreatureSpawn(state, world, pos, type);
	}
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_EntityDestroy)
			return ((ITP_EntityDestroy) tile).canEntityDestroy(state, entity);
		return super.canEntityDestroy(state, world, pos, entity);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_HarvestCheck)
			return ((ITP_HarvestCheck) tile).canHarvestBlock(player);
		return super.canHarvestBlock(world, pos, player);
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_SideSolid)
			return ((ITP_SideSolid) tile).canPlaceTorchOnTop();
		return super.canPlaceTorchOnTop(state, world, pos);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		TileEntity tile = source.getTileEntity(pos);
		if(tile instanceof ITP_BoundingBox)
			return ((ITP_BoundingBox) tile).getBoundBox(state);
		return super.getBoundingBox(state, source, pos);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITP_BoundingBox)
			return ((ITP_BoundingBox) tile).getCollisionBoundingBox(blockState);
		return super.getCollisionBoundingBox(blockState, worldIn, pos);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITP_BoundingBox)
		{
			List<AxisAlignedBB> list = new ArrayList();
			((ITP_BoundingBox) tile).addCollisionBoxToList(state, entityBox, list, entityIn);
			for(AxisAlignedBB aabb : list)
			{
				addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb);
			}
		}
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn);
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITP_ComparatorInputOverride)
			return ((ITP_ComparatorInputOverride) tile).getComparatorInputOverride(blockState);
		return super.getComparatorInputOverride(blockState, worldIn, pos);
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITP_BlockHardness)
			return ((ITP_BlockHardness) tile).getBlockHardness(blockState);
		return this.blockHardness;
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_ExplosionResistance)
			return ((ITP_ExplosionResistance) tile).getExplosionResistance(exploder, explosion);
		return this.blockResistance;
	}
	
	@Override
	public float getEnchantPowerBonus(World world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_EnchantPowerBonus)
			return ((ITP_EnchantPowerBonus) tile).getEnchantPowerBonus();
		return super.getEnchantPowerBonus(world, pos);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_FireSpreadSpeed)
			return ((ITP_FireSpreadSpeed) tile).getFireSpreadSpeed(Direction.of(face));
		return super.getFireSpreadSpeed(world, pos, face);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_Flammability)
			return ((ITP_Flammability) tile).getFlammability(Direction.of(face));
		return super.getFlammability(world, pos, face);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune,
			boolean silkTouch)
	{
		if(tile instanceof ITP_Drops)
			return ((ITP_Drops) tile).getDrops(state, fortune, silkTouch);
		List<ItemStack> list = new ArrayList();
		list.add(new ItemStack(this, 1, getMetaFromState(state)));
		return list;
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		//		TileEntity tile = world.getTileEntity(pos);
		//		if(tile instanceof ITP_Light)
		//			return ((ITP_Light) tile).getLightOpacity(state);
		return super.getLightOpacity(state, world, pos);
		//The Minecraft will create a Tile Entity BEFORE add Tile Entity and create without data, to prevent this happen, I will disable this property.
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_Light)
			return ((ITP_Light) tile).getLightValue(state);
		return super.getLightValue(state, world, pos);
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITP_BlockHardness)
			return ((ITP_BlockHardness) tile).getPlayerRelativeBlockHardness(state, player);
		return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITP_BoundingBox)
			return ((ITP_BoundingBox) tile).getSelectedBoundingBox(state).offset(pos);
		return super.getSelectedBoundingBox(state, worldIn, pos);
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_RedstonePower)
			return ((ITP_RedstonePower) tile).getStrongPower(blockState, Direction.of(side));
		return super.getStrongPower(blockState, world, pos, side);
	}
	
	@Override
	public boolean getWeakChanges(IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_RedstonePower)
			return ((ITP_RedstonePower) tile).listenWeakChanges();
		return super.getWeakChanges(world, pos);
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_RedstonePower)
			return ((ITP_RedstonePower) tile).getWeakPower(blockState, Direction.of(side));
		return super.getWeakPower(blockState, world, pos, side);
	}
	
	@Override
	public void fillWithRain(World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_FillWithRain)
		{
			((ITB_FillWithRain) tile).fillWithRain();
		}
	}
	
	@Override
	public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_BeaconBase)
			return ((ITP_BeaconBase) tile).isBeaconBase(beacon);
		return super.isBeaconBase(world, pos, beacon);
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITP_SideSolid)
			return ((ITP_SideSolid) tile).isSideSolid(Direction.of(side));
		return super.isBlockSolid(worldIn, pos, side);
	}
	
	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_FireSource)
			return ((ITP_FireSource) tile).isFireSource(Direction.of(side));
		return super.isFireSource(world, pos, side);
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_Flammable)
			return ((ITP_Flammable) tile).isFlammable(Direction.of(face));
		return super.isFlammable(world, pos, face);
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITP_SideSolid)
			return ((ITP_SideSolid) tile).isSideSolid(Direction.of(side));
		return super.isSideSolid(base_state, world, pos, side);
	}
	
	@Override
	public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity,
			double yToTest, Material materialIn, boolean testingHead)
	{
		return super.isEntityInsideMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_BlockActived)
		{
			EnumActionResult result = ((ITB_BlockActived) tile).onBlockActivated(playerIn, hand, heldItem, Direction.of(side), hitX, hitY, hitZ);
			if(result != EnumActionResult.PASS) return result == EnumActionResult.SUCCESS ? true : false;
		}
		return TileEntities.onTileActivatedGeneral(playerIn, hand, heldItem, Direction.of(side), hitX, hitY, hitZ, tile);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_BreakBlock)
		{
			((ITB_BreakBlock) tile).onBlockBreak(state);
		}
		worldIn.removeTileEntity(pos);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IUpdatableTile)
		{
			((IUpdatableTile) tile).causeUpdate(pos, world.getBlockState(pos), true);
		}
		super.onNeighborChange(world, pos, neighbor);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof IUpdatableTile)
		{
			((IUpdatableTile) tile).causeUpdate(pos, worldIn.getBlockState(pos), false);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_DisplayUpdate)
		{
			((ITB_DisplayUpdate) tile).randomDisplayTick(stateIn, rand);
		}
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_Update)
		{
			((ITB_Update) tile).onUpdateTick(state, random, true);
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof ITB_Update)
		{
			((ITB_Update) tile).onUpdateTick(state, rand, false);
		}
	}
}