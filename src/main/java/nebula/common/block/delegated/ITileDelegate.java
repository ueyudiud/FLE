/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.block.delegated;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import mezz.jei.api.IModRegistry;
import nebula.client.util.UnlocalizedList;
import nebula.common.util.Game;
import nebula.common.world.ICoord;
import nebula.common.world.IModifiableCoord;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public interface ITileDelegate<B extends Block, T extends TileEntity>
{
	String getUnlocalizedName(B block, ItemStack stack);
	
	String getUnlocalizedName(B block, T tile, IBlockState stack, World world, ICoord coord);
	
	void load(B block);
	
	void postload(B block);
	
	@SideOnly(Side.CLIENT)
	void renderload(B block);
	
	@Optional.Method(modid = Game.MOD_JEI)
	void loadJEI(B block, Item item, IModRegistry registry);
	
	boolean isOpaqueCube(B block, IBlockState state);
	
	boolean isTranslucent(B block, IBlockState state);
	
	boolean canCollideCheck(B block, IBlockState state, boolean hitIfLiquid);
	
	void addInformation(B block, ItemStack stack, EntityPlayer player, UnlocalizedList tooltip, boolean advanced);
	
	@SideOnly(Side.CLIENT)
	float getAmbientOcclusionLightValue(B block, IBlockState state);
	
	EnumPushReaction getMobilityFlag(B block, IBlockState state);
	
	boolean hasTileEntity(B block, IBlockState state);
	
	T createTileEntity(B block, IBlockState state, World world);
	
	boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer);
	
	EnumBlockRenderType getRenderType(B block, IBlockState state);
	
	float getBlockHardness(B block, T tile, IBlockState state, World world, ICoord coord);
	
	float getExplosionResistance(B block, T tile, World world, ICoord coord, @Nullable Entity exploder, Explosion explosion);
	
	@SideOnly(Side.CLIENT)
	void addSubBlock(B block, int meta, Item item, List<ItemStack> list, CreativeTabs tab);
	
	boolean isFullCube(B block, IBlockState state);
	
	Material getMaterial(B block, IBlockState state);
	
	MapColor getMapColor(B block, IBlockState state);
	
	SoundType getSoundType(B block, T tile, IBlockState state, World world, ICoord coord, @Nullable Entity entity);
	
	interface ITD_AddCollisionBox<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void addCollisionBoxToList(B block, T tile, IBlockState state, World world, ICoord coord, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn);
	}
	
	interface ITD_Box<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		AxisAlignedBB getBoundBox(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
		
		AxisAlignedBB getCollisionBoundingBox(B block, T tile, IBlockState state, World world, ICoord coord);
		
		@SideOnly(Side.CLIENT)
		AxisAlignedBB getSelectedBoundingBox(B block, T tile, IBlockState state, World world, ICoord coord);
	}
	
	interface ITD_PackedLightmap<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		@SideOnly(Side.CLIENT)
		int getPackedLightmapCoords(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
	}
	
	interface ITD_AmbientOcclusionLightValue<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		@SideOnly(Side.CLIENT)
		float getAmbientOcclusionLightValue(B block, IBlockState state);
	}
	
	interface ITD_SideRendered<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		@SideOnly(Side.CLIENT)
		boolean shouldSideBeRendered(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EnumFacing side);
	}
	
	interface ITD_UpdateTick<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void randomTick(B block, T tile, IBlockState state, World world, IModifiableCoord coord, Random random);
		
		void updateTick(B block, T tile, IBlockState state, World world, IModifiableCoord coord, Random random);
		
		void rainTick(B block, T tile, IBlockState state, World world, IModifiableCoord coord);
	}
	
	interface ITD_RenderingEffect<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		@SideOnly(Side.CLIENT)
		void randomDisplayTick(B block, T tile, IBlockState state, World world, IModifiableCoord coord, Random random);
		
		boolean addLandingEffects(B block, T tile, IBlockState state, WorldServer world, ICoord coord, EntityLivingBase entity, int numberOfParticles);
		
		@SideOnly(Side.CLIENT)
		public boolean addHitEffects(B block, T tile, IBlockState state, World world, RayTraceResult target, ParticleManager manager);
		
		@SideOnly(Side.CLIENT)
		boolean addDestroyEffects(B block, T tile, IBlockState state, World world, BlockPos pos, ParticleManager manager);
	}
	
	interface ITD_BreakAbility<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean canEntityDestroy(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, Entity entity);
	}
	
	interface ITD_Exploded<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void onBlockExploded(B block, T tile, IBlockState state, World world, IModifiableCoord coord, Explosion explosion);
	}
	
	interface ITD_Destoryed<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void onBlockDestroyedByPlayer(B block, T tile, IBlockState state, World world, IModifiableCoord coord);
		
		void onBlockDestroyedByExplosion(B block, T tile, IBlockState state, World world, IModifiableCoord coord, Explosion explosion);
	}
	
	interface ITD_NeighbourChanged<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void neighborChanged(B block, T tile, IBlockState state, World world, IModifiableCoord coord, Block mark);
		
		void onNeighborChange(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, BlockPos neighbor);
	}
	
	interface ITD_BlockAdded<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void onBlockAdded(B block, T tile, IBlockState state, World world, IModifiableCoord coord);
	}
	
	interface ITD_BreakBlock<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void breakBlock(B block, T tile, IBlockState state, World world, IModifiableCoord coord);
	}
	
	interface ITD_PlayerRelativeBlockHardness<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		float getPlayerRelativeBlockHardness(B block, T tile, IBlockState state, World world, ICoord coord, EntityPlayer player);
	}
	
	interface ITD_RayTrace<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		RayTraceResult collisionRayTrace(B block, T tile, IBlockState state, World world, ICoord coord, Vec3d start, Vec3d end);
	}
	
	interface ITD_ItemUse<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		EnumActionResult onItemUse(B block, World world, IModifiableCoord coord, ItemStack stack, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ);
	}
	
	interface ITD_Activated<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean onBlockActivated(B block, T tile, IBlockState state, World world, IModifiableCoord pos, EntityPlayer player, EnumHand hand, @Nullable ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ);
	}
	
	interface ITD_EntityNearby<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void onEntityWalk(B block, T tile, IBlockState state, World world, IModifiableCoord pos, Entity entity);
		
		void onEntityCollidedWithBlock(B block, T tile, IBlockState state, World world, IModifiableCoord pos, Entity entity);
		
		Vec3d modifyAcceleration(B block, T tile, IBlockState state, World world, ICoord coord, Entity entity, Vec3d motion);
		
		void onFallenUpon(B block, T tile, IBlockState state, World world, IModifiableCoord pos, Entity entity, float fallDistance);
		
		void onLanded(B block, T tile, IBlockState state, World world, ICoord coord, Entity entity);
	}
	
	interface ITD_Click<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void onBlockClicked(B block, T tile, IBlockState state, World world, IModifiableCoord coord, EntityPlayer player);
	}
	
	interface ITD_RedstonePower<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		int getWeakPower(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EnumFacing side);
		
		int getStrongPower(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EnumFacing side);
		
		boolean canProvidePower(B block, IBlockState state);
		
		boolean canConnectRedstone(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EnumFacing side);
		
		boolean getWeakChanges(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
	}
	
	interface ITD_ComparactorInputOverride<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		int getComparatorInputOverride(B block, T tile, IBlockState state, World world, IModifiableCoord pos);
	}
	
	interface ITD_Harvestibility<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean canHarvestBlock(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EntityPlayer player);
		
		int getHarvestLevel(B block, IBlockState state);
		
		String getHarvestTool(B block, IBlockState state);
	}
	
	interface ITD_Harvest<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void harvestBlock(B block, T tile, World world, IBlockState state, IModifiableCoord coord, EntityPlayer player, @Nullable ItemStack stack);
		
		int getExpDrop(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, int fortune);
		
		List<ItemStack> getDrops(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, int fortune, boolean silkTouch);
	}
	
	interface ITD_RemovedByPlayer<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean removedByPlayer(B block, T tile, IBlockState state, World world, IModifiableCoord coord, EntityPlayer player, boolean willHarvest);
	}
	
	interface ITD_Light<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		int getLightValue(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
		
		int getLightOpacity(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
		
		boolean getUseNeighborBrightness(B block, IBlockState state);
	}
	
	interface ITD_Ladder<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean isLadder(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EntityLivingBase entity);
	}
	
	interface ITD_Solid<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean isNormalCube(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
		
		boolean isBlockSolid(B block, T tile, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side);
		
		boolean isSideSolid(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EnumFacing side);
		
		boolean canPlaceTorchOnTop(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
		
		boolean shouldCheckWeakPower(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EnumFacing side);
	}
	
	interface ITD_Burning<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean isBurning(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
	}
	
	interface ITD_Flammable<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		int getFlammability(B block, T tile, IBlockAccess world, BlockPos coord, EnumFacing side);
		
		int getFireSpreadSpeed(B block, T tile, IBlockAccess world, BlockPos coord, EnumFacing side);
		
		boolean isFireSource(B block, T tile, IBlockState state, World world, ICoord coord, EnumFacing side);
	}
	
	interface ITD_CreatureSpawn<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean canCreatureSpawn(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EntityLiving.SpawnPlacementType type);
	}
	
	interface ITD_Tree<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		void beginLeavesDecay(B block, T tile, IBlockState state, World world, IModifiableCoord coord);
		
		boolean canSustainLeaves(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
		
		boolean isLeaves(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
		
		boolean isWood(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
	}
	
	interface ITD_Replacable<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean canReplace(B block, T tile, IBlockState state, World world, ICoord coord, EnumFacing side, ItemStack stack);
		
		boolean canBeReplacedByLeaves(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord);
		
		boolean isReplaceableOreGen(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, com.google.common.base.Predicate<IBlockState> target);
		
		boolean isReplaceable(B block, T tile, IBlockState state, IBlockAccess world, BlockPos pos);
	}
	
	interface ITD_Soil<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean canSustainPlant(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, EnumFacing side, IPlantable plantable);
		
		void onPlantGrow(B block, T tile, IBlockState state, World world, IModifiableCoord coord, BlockPos source);
		
		boolean isFertile(B block, T tile, IBlockState state, World world, ICoord coord);
	}
	
	interface ITD_MetalBlock<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		boolean isBeaconBase(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, BlockPos beacon);
	}
	
	interface ITD_EnchantPowerBonus<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		float getEnchantPowerBonus(B block, T tile, World world, ICoord coord);
	}
	
	interface ITD_MaterialIn<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		Boolean isEntityInsideMaterial(B block, T tile, IBlockState state, IBlockAccess world, BlockPos coord, Entity entity, double yToTest, Material materialIn, boolean testingHead);
		
		Boolean isAABBInsideMaterial(B block, T tile, IBlockState state, World world, ICoord coord, AxisAlignedBB boundingBox, Material materialIn);
	}
	
	interface ITD_Colored<B extends Block, T extends TileEntity> extends ITileDelegate<B, T>
	{
		float[] getBeaconColorMultiplier(B block, T tile, IBlockState state, World world, ICoord coord, BlockPos beaconPos);
	}
}
