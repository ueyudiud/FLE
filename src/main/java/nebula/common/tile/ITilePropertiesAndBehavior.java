/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tile;

import java.util.List;
import java.util.Random;

import nebula.common.util.Direction;
import nebula.common.world.ICoord;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITilePropertiesAndBehavior
{
	public static interface ITP_BlockHardness
	{
		default float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player)
		{
			TileEntity tile = (TileEntity) this;
			float hardness = state.getBlockHardness(tile.getWorld(), tile.getPos());
			if (hardness < 0.0F) return 0.0F;
			
			if (!state.getBlock().canHarvestBlock(tile.getWorld(), tile.getPos(), player))
				return 0.0F;// No usable tool will not provide any effect.
			else
				return player.getDigSpeed(state, tile.getPos()) / hardness / 30F;
		}
		
		float getBlockHardness(IBlockState state);
	}
	
	public static interface ITP_ExplosionResistance
	{
		float getExplosionResistance(Entity exploder, Explosion explosion);
	}
	
	public static interface ITP_BoundingBox extends ICoord
	{
		AxisAlignedBB getBoundBox(IBlockState state);
		
		default AxisAlignedBB getCollisionBoundingBox(IBlockState state)
		{
			return getBoundBox(state);
		}
		
		default void addCollisionBoxToList(IBlockState state, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity)
		{
			AxisAlignedBB axisAlignedBB = getCollisionBoundingBox(state);
			if (axisAlignedBB != Block.NULL_AABB)
			{
				axisAlignedBB = axisAlignedBB.offset(pos());
				if (axisAlignedBB.intersectsWith(entityBox)) collidingBoxes.add(axisAlignedBB);
			}
		}
		
		@SideOnly(Side.CLIENT)
		default AxisAlignedBB getSelectedBoundingBox(IBlockState state)
		{
			return getBoundBox(state);// .offset(pos());
		}
	}
	
	public static interface ITP_ComparatorInputOverride
	{
		int getComparatorInputOverride(IBlockState state);
	}
	
	public static interface ITP_RedstonePower
	{
		int getStrongPower(IBlockState blockState, Direction side);
		
		int getWeakPower(IBlockState blockState, Direction side);
		
		default boolean listenWeakChanges()
		{
			return false;
		}
	}
	
	public static interface ITP_ConnectRedstone
	{
		boolean canConnectRedstone(IBlockState state, Direction side);
	}
	
	public static interface ITP_CreatureSpawn
	{
		boolean canCreatureSpawn(IBlockState state, SpawnPlacementType type);
	}
	
	public static interface ITP_BeaconBase
	{
		boolean isBeaconBase(BlockPos beaconPos);
	}
	
	public static interface ITP_SideSolid
	{
		boolean isSideSolid(Direction side);
		
		default boolean canPlaceTorchOnTop()
		{
			return isSideSolid(Direction.U);
		}
	}
	
	public static interface ITP_EntityDestroy
	{
		boolean canEntityDestroy(IBlockState state, Entity entity);
	}
	
	public static interface ITP_HarvestCheck
	{
		boolean canHarvestBlock(EntityPlayer player);
	}
	
	public static interface ITP_EnchantPowerBonus
	{
		float getEnchantPowerBonus();
	}
	
	public static interface ITP_Burn extends ITP_FireSource, ITP_FireSpreadSpeed, ITP_Flammability, ITP_Flammable
	{
		boolean canBeBurned();
		
		int getFireEncouragement();
		
		boolean canFireBurnOn(Direction side, boolean isCatchRain);
	}
	
	public static interface ITP_FireSource
	{
		boolean isFireSource(Direction side);
	}
	
	public static interface ITP_Flammable
	{
		boolean isFlammable(Direction side);
	}
	
	public static interface ITP_FireSpreadSpeed
	{
		int getFireSpreadSpeed(Direction side);
	}
	
	public static interface ITP_Flammability
	{
		int getFlammability(Direction side);
	}
	
	public static interface ITP_Drops
	{
		List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch);
	}
	
	public static interface ITP_Light
	{
		@Deprecated
		default int getLightOpacity(IBlockState state)
		{
			return 0;
		}
		
		int getLightValue(IBlockState state);
	}
	
	public static interface ITB_BlockAdded
	{
		void onBlockAdded(IBlockState state);
	}
	
	public static interface ITB_BlockPlacedBy
	{
		void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack);
	}
	
	public static interface ITB_BlockExploded
	{
		void onBlockExploded(Explosion explosion);
	}
	
	public static interface ITB_BlockDestroyedByPlayer
	{
		void onBlockDestroyedByPlayer(IBlockState state);
	}
	
	public static interface ITB_BlockClicked
	{
		boolean onBlockClicked(EntityPlayer player, Direction side, float hitX, float hitY, float hitZ);
	}
	
	public static interface ITB_BlockActived
	{
		EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ);
	}
	
	public static interface ITB_BlockHarvest
	{
		boolean onBlockHarvest(IBlockState state, EntityPlayer player, boolean silkHarvest);
	}
	
	public static interface ITB_BlockHarvested
	{
		boolean onBlockHarvested(IBlockState state, EntityPlayer player);
	}
	
	public static interface ITB_FillWithRain
	{
		void fillWithRain();
	}
	
	public static interface ITB_BreakBlock
	{
		void onBlockBreak(IBlockState state);
	}
	
	public static interface ITB_EntityCollidedWithBlock
	{
		void onEntityCollidedWithBlock(IBlockState state, Entity entity);
	}
	
	public static interface ITB_EntityWalk
	{
		void onEntityWalk(Entity entity);
	}
	
	public static interface ITB_EntityFallenUpon
	{
		void onEntityFallenUpon(Entity entityIn, float fallDistance);
	}
	
	public static interface ITB_EntityLanded
	{
		void onEntityLanded(Entity entity);
	}
	
	public static interface ITB_PlantGrow
	{
		void onPlantGrow(IBlockState state, BlockPos source);
	}
	
	public static interface ITB_Update
	{
		void onUpdateTick(IBlockState state, Random random, boolean isTickRandomly);
	}
	
	public static interface ITB_DisplayUpdate
	{
		@SideOnly(Side.CLIENT)
		void randomDisplayTick(IBlockState stateIn, Random rand);
	}
	
	public static interface ITB_Burn
	{
		boolean onBurn(float burnHardness, Direction direction);
		
		boolean onBurningTick(Random rand, Direction fireSourceDir, IBlockState fireState);
		
		void onHeatChange(Direction direction, long amount);
	}
	
	public static interface ITB_AddDestroyEffects
	{
		@SideOnly(Side.CLIENT)
		boolean addDestroyEffects(ParticleManager manager);
	}
	
	public static interface ITB_AddHitEffects
	{
		@SideOnly(Side.CLIENT)
		boolean addHitEffects(RayTraceResult target, ParticleManager manager);
	}
	
	public static interface ITB_AddLandingEffects
	{
		boolean addLandingEffects(IBlockState state, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles);
	}
	
	public static interface ITB_Containerable extends IGuiTile
	{
	}
}
