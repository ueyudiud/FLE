/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import nebula.Nebula;
import nebula.common.CommonOverride;
import nebula.common.block.ISmartFallableBlock;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.environment.EnviornmentBlockPos;
import nebula.common.network.packet.PacketBreakBlock;
import nebula.common.world.ICoord;
import nebula.common.world.IObjectInWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
import net.minecraftforge.fluids.BlockFluidBase;

/**
 * The world method helper.
 * 
 * @author ueyudiud
 */
public final class Worlds
{
	private static final int[][] ROTATE_FIX = { { 3, 2, 5, 4 }, { 1, 0, 5, 4 }, { 1, 0, 3, 2 } };
	
	private Worlds()
	{
	}
	
	/**
	 * I don't know why some modification can be crashed on this method, use
	 * this instead.
	 * 
	 * @param world
	 * @param pos
	 * @param side
	 * @param def
	 * @return
	 */
	public static boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side, boolean def)
	{
		if (pos.getY() >= 256 || pos.getY() < 0) return def;
		IBlockState state = world.getBlockState(pos);
		return state.isSideSolid(world, pos, side);
	}
	
	/**
	 * Match block at position is air or is replaceable.
	 * 
	 * @param world the world.
	 * @param pos the position.
	 * @return return <code>true</code> if block is air or replaceable.
	 */
	public static boolean isAirOrReplacable(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		return state.getBlock().isAir(state, world, pos) || state.getBlock().isReplaceable(world, pos);
	}
	
	/**
	 * Try place block on the ground. This method is simulated to method in
	 * {@link ItemBlock#onItemUse(ItemStack, EntityPlayer, World, BlockPos, EnumHand, EnumFacing, float, float, float)}.
	 * <p>
	 * If player ray trace block position can not is not <tt>replaceable</tt>,
	 * the position will offset by <code>facing</code>. This method include
	 * following actions:
	 * <li>Check player can be edit and can be replaced, if <code>false</code>
	 * is return, the remain action will be canceled.
	 * <li>Set block state into world, if <code>false</code> is return, the
	 * remain action will be canceled.
	 * <li>Reload TileEntity NBT from ItemStack, and display placed block sound.
	 * </li>
	 * 
	 * @param world the world.
	 * @param pos the position try to place first, usually is player ray trace
	 *            position, if block is not <tt>replaceable</tt> will try to
	 *            offset position to check.
	 * @param facing the collide facing
	 * @param player the player.
	 * @param stack the used item stack, not force to ItemBlock.
	 * @param placedState the state try to placing.
	 * @param hasTile <code>true</code> will enable tile NBT replacing after
	 *            block added to world.
	 * @return <code>FAIL</code> if check failed, <code>PASS</code> if action is
	 *         not finished, and <code>SUCCESS</code> if action is finished.
	 */
	public static EnumActionResult checkAndPlaceBlockAt(World world, BlockPos pos, @Nonnull EnumFacing facing, @Nonnull EntityPlayer player, ItemStack stack, @Nonnull IBlockState placedState, boolean hasTile)
	{
		IBlockState state1 = world.getBlockState(pos);
		if (!state1.getBlock().isReplaceable(world, pos))
		{
			state1 = world.getBlockState(pos = pos.offset(facing));
		}
		
		if (player.canPlayerEdit(pos, facing, stack) && placedState.getBlock().canReplace(world, pos, facing, stack))
		{
			if (!world.setBlockState(pos, placedState)) return EnumActionResult.PASS;
			
			IBlockState state2 = world.getBlockState(pos);
			if (state2.getBlock() == placedState.getBlock())
			{
				if (hasTile)
				{
					ItemBlock.setTileEntityNBT(world, player, pos, stack);
				}
				placedState.getBlock().onBlockPlacedBy(world, pos, state2, player, stack);
				SoundType soundtype = state2.getBlock().getSoundType(state2, world, pos, player);
				world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
	
	/**
	 * Try break block without player damage.
	 * <p>
	 * This method include following actions:
	 * <li>Remove block by set block to air (For player damaged block, called
	 * {@link Block#removedByPlayer(IBlockState, World, BlockPos, EntityPlayer, boolean)}
	 * instead), and called <code>breakBlock</code> method.
	 * <li>Send a packet to client side and take broken particle rendering.
	 * <li>If <tt>harestBlock</tt> is enabled, called
	 * <code>dropBlockAsItem</code> method from harvested block.</li>
	 * 
	 * @param world
	 * @param pos
	 * @param harvestBlock
	 * @see Block#breakBlock(World, BlockPos, IBlockState)
	 */
	public static void breakBlockWithoutSource(World world, BlockPos pos, boolean harvestBlock)
	{
		if (!world.isRemote) // This method have not effect in client world, it
			// will send a packet to client.
		{
			if (!world.isAreaLoaded(pos, 64))
			{
				world.setBlockToAir(pos);
			}
			if (isAir(world, pos)) return;
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			block.breakBlock(world, pos, state);
			Nebula.network.sendToNearBy(new PacketBreakBlock(world, pos), world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 5);
			if (harvestBlock)
			{
				block.dropBlockAsItem(world, pos, state, 0);
			}
		}
	}
	
	public static <E> List<E> getListFromWorldDimention(Map<Integer, List<E>> map, World world, boolean createEntry)
	{
		Integer dim = world.provider.getDimension();
		List<E> list = map.get(dim);
		if (list == null)
		{
			if (createEntry)
			{
				map.put(dim, list = new ArrayList<>());
				return list;
			}
			return ImmutableList.of();
		}
		return list;
	}
	
	public static int fixSide(EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return fixSide(side.ordinal(), hitX, hitY, hitZ);
	}
	
	/**
	 * Fixed 6 direction side of facing, for change to another face when that
	 * face is not exposed.
	 * 
	 * @param side the side of hitting.
	 * @param hitX the hit x coord.
	 * @param hitY the hit y coord.
	 * @param hitZ the hit z coord.
	 * @return the refacing side.
	 */
	public static int fixSide(int side, float hitX, float hitY, float hitZ)
	{
		float u, v;
		switch (side)
		{
		case 0:
		case 1:
			u = hitX;
			v = hitZ;
			break;
		case 2:
		case 3:
			u = hitX;
			v = hitY;
			break;
		case 4:
		case 5:
			u = hitZ;
			v = hitY;
			break;
		default:
			u = 0.5F;
			v = 0.5F;
			break;
		}
		int id;
		boolean b1 = u >= 0.25F, b2 = v >= 0.25F, b3 = u <= 0.75F, b4 = v <= 0.75F;
		return b1 && b2 && b3 && b4 ? side : (id = (b1 && b3 ? (!b4 ? 1 : 0) : (b2 && b4) ? (!b3 ? 3 : 2) : -1)) == -1 ? Direction.OPPISITE[side] : ROTATE_FIX[side >> 1][id];
	}
	
	/**
	 * Spawn dropping item in world.
	 * 
	 * @param world the world.
	 * @param pos the position to spawn dropping item, and give a random offset
	 *            to set the dropping item location.
	 * @param drop the dropping item.
	 */
	public static void spawnDropInWorld(World world, BlockPos pos, ItemStack drop)
	{
		if (world.isRemote ||
				// Debug world can drop item will crash the game...
				world.getWorldType() == WorldType.DEBUG_WORLD || drop == null)
			return;
		float f = 0.7F;
		double d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		EntityItem entityitem = new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, drop.copy());
		entityitem.setPickupDelay(10);
		world.spawnEntity(entityitem);
	}
	
	/**
	 * Spawn dropping item in world.
	 * <p>
	 * The item will move by a catapult.
	 * 
	 * @param world the world.
	 * @param pos the position to spawn dropping item, and give a random offset
	 *            to set the dropping item location.
	 * @param direction the direction which will give a catapult.
	 * @param drop the dropping item.
	 */
	public static void spawnDropInWorld(World world, BlockPos pos, Direction direction, ItemStack drop)
	{
		if (world.isRemote ||
				// Debug world can drop item will crash the game...
				world.getWorldType() == WorldType.DEBUG_WORLD || drop == null)
			return;
		float f = 0.7F;
		double d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.1D + (direction.x + 1.0F) / 2.0F;
		double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.1D + (direction.y + 1.0F) / 2.0F;
		double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.1D + (direction.z + 1.0F) / 2.0F;
		EntityItem entityitem = new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, drop.copy());
		entityitem.motionX = entityitem.motionX / 5.0 + direction.x * 0.5;
		entityitem.motionY = entityitem.motionY / 5.0 + direction.y * 0.5;
		entityitem.motionZ = entityitem.motionZ / 5.0 + direction.z * 0.5;
		entityitem.setPickupDelay(10);
		world.spawnEntity(entityitem);
	}
	
	/**
	 * Spawn a list of items in the world.
	 * <p>
	 * It usually called when block is broke.
	 * 
	 * @param world the world.
	 * @param pos the spawn position.
	 * @param drops the drops item stacks.
	 */
	public static void spawnDropsInWorld(World world, BlockPos pos, @Nullable List<ItemStack> drops)
	{
		if (world.isRemote || world.getWorldType() == WorldType.DEBUG_WORLD ||
				// Debug world can drop item will crash the game...
				drops == null)
			return;
		for (ItemStack stack : drops)
		{
			if (stack == null)
			{
				continue;
			}
			float f = 0.7F;
			double d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, stack.copy());
			entityitem.setPickupDelay(10);
			world.spawnEntity(entityitem);
		}
	}
	
	public static void spawnDropInWorld(ICoord coord, ItemStack drop)
	{
		spawnDropsInWorld(coord, Arrays.asList(drop));
	}
	
	/**
	 * @see #spawnDropsInWorld(World, BlockPos, List)
	 */
	public static void spawnDropsInWorld(ICoord coord, List<ItemStack> drop)
	{
		spawnDropsInWorld(coord.world(), coord.pos(), drop);
	}
	
	public static void spawnDropInWorld(EntityPlayer player, @Nullable ItemStack drop)
	{
		if (drop == null || drop.stackSize == 0 || player.world.isRemote) return;
		player.dropItem(drop, false);
	}
	
	public static void spawnDropsInWorldByPlayerOpeningContainer(EntityPlayer player, IInventory inventory)
	{
		if (player.world.isRemote) return;
		for (int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			spawnDropInWorld(player, inventory.removeStackFromSlot(i));
		}
	}
	
	/**
	 * Check can block stay and fall block.
	 * 
	 * @param world the world.
	 * @param pos the checking position.
	 * @return return <tt>true</tt> when falling is matched and happened.
	 */
	public static boolean checkAndFallBlock(World world, BlockPos pos)
	{
		if (world.isRemote) return false;
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof ISmartFallableBlock ? !((ISmartFallableBlock) state.getBlock()).canFallingBlockStay(world, pos, state.getActualState(world, pos)) : EntityFallingBlockExtended.canFallAt(world, pos, state)) return fallBlock(world, pos, state);
		return false;
	}
	
	/**
	 * Fall block with starting at located position.
	 * 
	 * @param world the falling block generated world.
	 * @param pos the located position.
	 * @param state the block state.
	 * @return return <tt>true</tt> when falling action is success happen, or
	 *         the side is client or falling block not generate successfully
	 *         otherwise.
	 * @see Worlds#fallBlock(World, BlockPos, BlockPos, IBlockState)
	 */
	public static boolean fallBlock(World world, BlockPos pos, IBlockState state)
	{
		return fallBlock(world, pos, pos, state);
	}
	
	/**
	 * Generate a falling block at drop position, and the source is from start
	 * position, a helper method to create falling block (such behavior like
	 * vanilla sand).
	 * <p>
	 * The source block will be removed after falling action is started, and a
	 * new {@link nebula.common.entity.EntityFallingBlockExtended} will be spawn
	 * in the world if option of falling block instantly is disabled and the
	 * chunk of handling falling action is loaded, or try to falling instantly.
	 * <p>
	 * If the block is instance of
	 * {@link nebula.common.block.ISmartFallableBlock}. The method will be
	 * called after entity spawning.
	 * <p>
	 * 
	 * @param world the world.
	 * @param pos the block at this position will be remove to air.
	 * @param dropPos the falling entity block starting position, it usually
	 *            same to start position.
	 * @param state the falling block state.
	 * @return return <tt>true</tt> when falling action is success happen, or
	 *         the side is client or falling block not generate successfully
	 *         otherwise.
	 * @see net.minecraft.block.BlockFalling
	 * @see nebula.common.block.ISmartFallableBlock
	 * @see nebula.common.entity.EntityFallingBlockExtended
	 */
	public static boolean fallBlock(World world, BlockPos pos, BlockPos dropPos, IBlockState state)
	{
		if (!BlockFalling.fallInstantly && world.isAreaLoaded(pos, 32))
		{
			world.setBlockToAir(pos);
			return world.isRemote || world.spawnEntity(new EntityFallingBlockExtended(world, pos, dropPos, state, world.getTileEntity(pos)));
		}
		else
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null)
			{
				world.removeTileEntity(pos);
			}
			world.setBlockToAir(pos);
			if (state.getBlock() instanceof ISmartFallableBlock)
			{
				((ISmartFallableBlock) state.getBlock()).onStartFalling(world, dropPos);
			}
			int height = 0;
			while (!EntityFallingBlockExtended.canFallAt(world, pos, state))
			{
				pos = pos.down();
				++height;
			}
			if (pos.getY() > 0)
			{
				EntityFallingBlockExtended.replaceFallingBlock(world, pos, state, height);
				NBTTagCompound nbt = new NBTTagCompound();
				if (tile != null)
				{
					tile.writeToNBT(nbt);
				}
				if (state.getBlock() instanceof ISmartFallableBlock && ((ISmartFallableBlock) state.getBlock()).onFallOnGround(world, pos, state, height, nbt))
				{
					
				}
				else
				{
					world.setBlockState(pos, state, 2);
					TileEntity tile1 = world.getTileEntity(pos);
					if (tile1 != null)
					{
						tile1.writeToNBT(nbt);
						tile1.setPos(pos);
					}
				}
			}
			return true;
		}
	}
	
	/**
	 * Get world from dimension id.
	 * 
	 * @param dimID the dimension id of world.
	 * @return the current world with dimension id, or <tt>null</tt> if world
	 *         not found.
	 */
	public static @Nullable World world(int dimID)
	{
		return Nebula.proxy.worldInstance(dimID);
	}
	
	/**
	 * Calculate the square of distance of a object to position.
	 * 
	 * @param object the object in world.
	 * @param pos the position.
	 * @return the distance square.
	 */
	public static double distanceSqTo(IObjectInWorld object, BlockPos pos)
	{
		double[] cache = object.position();
		return absSq(cache[0] - pos.getX() + .5, cache[1] - pos.getY() + .5, cache[2] - pos.getZ() + .5);
	}
	
	public static double distanceSqTo(IObjectInWorld object1, IObjectInWorld object2)
	{
		double[] cache1 = object1.position();
		double[] cache2 = object2.position();
		return absSq(cache1[0] - cache2[0], cache1[1] - cache2[1], cache1[2] - cache2[2]);
	}
	
	public static double distanceSqTo(BlockPos pos1, BlockPos pos2)
	{
		return absSq(pos1.getX() - pos2.getX(), pos1.getY() - pos2.getY(), pos1.getZ() - pos2.getZ());
	}
	
	public static double distanceSqTo(Entity entity, BlockPos pos)
	{
		return entity.getDistanceSq(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
	}
	
	public static double absSq(double x, double y, double z)
	{
		return x * x + y * y + z * z;
	}
	
	public static <T extends Comparable<T>> boolean switchProp(World world, BlockPos pos, IProperty<T> property, T value, int updateFlag)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getValue(property) == value) return false;
		return world.setBlockState(pos, state.withProperty(property, value), updateFlag);
	}
	
	public static boolean isBlockNearby(World world, BlockPos pos, Block block, boolean ignoreUnloadChunk)
	{
		return isBlockNearby(world, pos, block, -1, ignoreUnloadChunk);
	}
	
	public static boolean isBlockNearby(World world, BlockPos pos, Block block, int meta, boolean ignoreUnloadChunk)
	{
		return isBlock(world, pos.up(), block, meta, ignoreUnloadChunk) || isBlock(world, pos.down(), block, meta, ignoreUnloadChunk) || isBlock(world, pos.east(), block, meta, ignoreUnloadChunk) || isBlock(world, pos.west(), block, meta, ignoreUnloadChunk)
				|| isBlock(world, pos.north(), block, meta, ignoreUnloadChunk) || isBlock(world, pos.south(), block, meta, ignoreUnloadChunk);
	}
	
	public static boolean isBlock(World world, BlockPos pos, Block block, int meta, boolean ignoreUnloadChunk)
	{
		IBlockState state;
		return (!ignoreUnloadChunk || world.isAreaLoaded(pos, 0)) && (state = world.getBlockState(pos)).getBlock() == block && (meta < 0 || state.getBlock().getMetaFromState(state) == meta);
	}
	
	public static boolean isAir(IBlockAccess world, BlockPos pos)
	{
		IBlockState state;
		return (state = world.getBlockState(pos)).getBlock().isAir(state, world, pos);
	}
	
	public static boolean isAirNearby(World world, BlockPos pos, boolean ignoreUnloadChunk)
	{
		return (!ignoreUnloadChunk || world.isAreaLoaded(pos, 1)) && (isAir(world, pos.up()) || isAir(world, pos.down()) || isAir(world, pos.west()) || isAir(world, pos.east()) || isAir(world, pos.north()) || isAir(world, pos.south()));
	}
	
	public static boolean isNotOpaqueNearby(World world, BlockPos pos)
	{
		return !world.isAreaLoaded(pos, 1) || !(world.isSideSolid(pos.up(), EnumFacing.DOWN) && world.isSideSolid(pos.down(), EnumFacing.UP) && world.isSideSolid(pos.west(), EnumFacing.EAST) && world.isSideSolid(pos.east(), EnumFacing.WEST) && world.isSideSolid(pos.north(), EnumFacing.SOUTH)
				&& world.isSideSolid(pos.south(), EnumFacing.NORTH));
	}
	
	public static int getBlockMeta(World world, BlockPos pos)
	{
		IBlockState state;
		return (state = world.getBlockState(pos)).getBlock().getMetaFromState(state);
	}
	
	public static boolean setBlock(World world, BlockPos pos, Block block, int meta, int flag)
	{
		return world.setBlockState(pos, block.getStateFromMeta(meta), flag);
	}
	
	public static boolean isCatchingRain(World world, BlockPos pos)
	{
		return isCatchingRain(world, pos, false);
	}
	
	public static boolean isCatchingRain(World world, BlockPos pos, boolean checkNeayby)
	{
		return world.isRaining() ? (world.canBlockSeeSky(pos.up()) && CommonOverride.isRainingAtBiome(world.getBiome(pos), world, pos)) || (checkNeayby && (world.canBlockSeeSky(pos.north()) || world.canBlockSeeSky(pos.south()) || world.canBlockSeeSky(pos.east()) || world.canBlockSeeSky(pos.west())))
				: false;
	}
	
	public static TileEntity getTileEntity(IBlockAccess world, BlockPos pos, boolean update)
	{
		if (world instanceof World)
			return getTileEntity((World) world, pos, update);
		else if (world instanceof ChunkCache)
		{
			return ((ChunkCache) world).getTileEntity(pos, update ? EnumCreateEntityType.IMMEDIATE : EnumCreateEntityType.CHECK);
		}
		return null;
	}
	
	public static TileEntity getTileEntity(World world, BlockPos pos, boolean update)
	{
		if (update)
		{
			return world.getTileEntity(pos);
		}
		else
		{
			Chunk chunk = world.getChunkFromBlockCoords(pos);
			if (chunk != null)
			{
				return chunk.getTileEntity(pos, EnumCreateEntityType.CHECK);
			}
			return null;
		}
	}
	
	public static TileEntity setTileEntity(World world, BlockPos pos, TileEntity tile, boolean update)
	{
		if (update)
		{
			world.setTileEntity(pos, tile);
		}
		else
		{
			Chunk chunk = world.getChunkFromBlockCoords(pos);
			if (chunk != null)
			{
				world.addTileEntity(tile);
				chunk.addTileEntity(pos, tile);
				chunk.setModified(true);
			}
		}
		return tile;
	}
	
	public static Direction getCollideSide(AxisAlignedBB aabb, double[] pre, double[] post)
	{
		if (aabb.maxX < post[0] || aabb.minX > post[0] || aabb.maxY < post[1] || aabb.minY > post[1] || aabb.maxZ < post[2] || aabb.minZ > post[2])
			return null;
		else
			return aabb.maxY < pre[1] ? Direction.U : aabb.minY > pre[1] ? Direction.D : aabb.maxX < pre[0] ? Direction.E : aabb.minX > pre[0] ? Direction.W : aabb.maxZ < pre[2] ? Direction.S : aabb.minZ > pre[2] ? Direction.N : Direction.Q;
	}
	
	public static RayTraceResult rayTrace(World world, EntityLivingBase entity, boolean useLiquids)
	{
		float f = entity.rotationPitch;
		float f1 = entity.rotationYaw;
		double d0 = entity.posX;
		double d1 = entity.posY + entity.getEyeHeight();
		double d2 = entity.posZ;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = 5.0D;
		if (entity instanceof EntityPlayerMP)
		{
			d3 = ((EntityPlayerMP) entity).interactionManager.getBlockReachDistance();
		}
		Vec3d vec3d1 = vec3d.addVector(f6 * d3, f5 * d3, f7 * d3);
		return world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}
	
	private static Method isChunkLoaded;
	
	/**
	 * Return <code>true</code> if redstone is checking the power for update.
	 * 
	 * @return <code>true</code> if redstone is checking the power for update,
	 *         and <code>false</code> for otherwise.
	 */
	public static boolean isRedstoneChecking()
	{
		return !Blocks.REDSTONE_WIRE.canProvidePower(null);
	}
	
	/**
	 * Check is the specific chunk is loaded.
	 * 
	 * @param world the world.
	 * @param x the chunk x coord.
	 * @param z the chunk z coord.
	 * @param allowEmpty if it is <code>true</code>, the result will be
	 *            <code>true</code> if the world does not synch the specific
	 *            chunk.
	 * @return <code>true</code> if chunk is loaded or <code>false</code> for
	 *         otherwise.
	 */
	public static boolean isChunkLoaded(World world, int x, int z, boolean allowEmpty)
	{
		if (isChunkLoaded == null)
		{
			isChunkLoaded = R.getMethod(World.class, "isChunkLoaded", "func_175680_a", int.class, int.class, boolean.class);
		}
		try
		{
			return (Boolean) isChunkLoaded.invoke(world, x, z, allowEmpty);
		}
		catch (Exception exception)
		{
			return false;
		}
	}
	
	/**
	 * Spawn a particle with position gave a random offset.
	 * 
	 * @param world the world.
	 * @param types the type of particle.
	 * @param xCoord the particle x coordinate.
	 * @param yCoord the particle y coordinate.
	 * @param zCoord the particle z coordinate.
	 * @param motionX the particle x motion.
	 * @param motionY the particle y motion.
	 * @param motionZ the particle z motion.
	 * @param randScale the random coordinate offset scale.
	 * @param datas the particle custom data.
	 */
	public static void spawnParticleWithRandomOffset(World world, EnumParticleTypes types, double xCoord, double yCoord, double zCoord, double motionX, double motionY, double motionZ, double randScale, int...datas)
	{
		Random random = L.random();
		world.spawnParticle(types, xCoord + (random.nextFloat() - random.nextFloat()) * randScale, yCoord + (random.nextFloat() - random.nextFloat()) * randScale, zCoord + (random.nextFloat() - random.nextFloat()) * randScale, motionX, motionY, motionZ, datas);
	}
	
	/**
	 * Check if item can drop at position.
	 * 
	 * @param world
	 * @param pos
	 * @param side
	 * @return
	 */
	public static boolean isItemDropable(World world, BlockPos pos, @Nullable Direction side)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().isAir(state, world, pos)) return true;
		return state.getBlock() instanceof BlockLiquid || state.getBlock() instanceof BlockFluidBase || (!state.isFullCube() && (side == null || !state.isSideSolid(world, pos, side.of())) && state.getCollisionBoundingBox(world, pos) == Block.NULL_AABB);
	}
	
	/**
	 * Check to <tt>match</tt> a specific block state or tile entity from source
	 * block position and check neighbour of matched block position and return
	 * true if enough block states are matched.
	 * 
	 * @param world the world.
	 * @param pos the position to start check.
	 * @param checkItSelf should this position be checked first or start from
	 *            its neighbours first and will ignore main position.
	 * @param count how many block states matches can finished the checking
	 *            task.
	 * @param predicate the predictor to predicate if block state can be
	 *            matched.
	 * @return <code>true</code> if enough block states connect to source block
	 *         position in a line matched.
	 */
	public static boolean checkForMatch(World world, BlockPos pos, boolean checkItSelf, int count, Predicate<ICoord> predicate)
	{
		List<BlockPos> list = new ArrayList<>();
		LinkedList<BlockPos> unchecked = new LinkedList<>();
		if (checkItSelf)
			unchecked.add(pos);
		else
		{
			list.add(pos);
			unchecked.add(pos.up());
			unchecked.add(pos.down());
			unchecked.add(pos.north());
			unchecked.add(pos.south());
			unchecked.add(pos.east());
			unchecked.add(pos.west());
		}
		list.addAll(unchecked);
		int size = 0;
		BlockPos pos2;
		final int maxCheck = MathHelper.log2DeBruijn(count) * 25 + count; /* Ticking required. */
		while (!unchecked.isEmpty() && list.size() <= maxCheck)
		{
			BlockPos pos1 = unchecked.removeFirst();
			if (predicate.test(new EnviornmentBlockPos(world, pos1)))
			{
				if (++size == count) return true;
				for (EnumFacing facing : EnumFacing.VALUES)
				{
					if (!list.contains(pos2 = pos1.offset(facing)))
					{
						list.add(pos2);
						unchecked.add(pos2);
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Checking task, to <tt>match</tt> a specific block state or tile entity
	 * from source block position and check neighbour of matched block position
	 * and return true if minimum first matched block state found.
	 * 
	 * @see #checkForMatch(World, BlockPos, boolean, int, Predicate)
	 */
	public static int checkForMinDistance(World world, BlockPos pos, boolean checkItSelf, int max, Predicate<ICoord> predicate)
	{
		List<BlockPos> list = new ArrayList<>();
		LinkedList<BlockPos> unchecked = new LinkedList<>();
		if (checkItSelf)
			unchecked.add(pos);
		else
		{
			list.add(pos);
			unchecked.add(pos.up());
			unchecked.add(pos.down());
			unchecked.add(pos.north());
			unchecked.add(pos.south());
			unchecked.add(pos.east());
			unchecked.add(pos.west());
		}
		list.addAll(unchecked);
		BlockPos pos2;
		while (!unchecked.isEmpty())
		{
			BlockPos pos1 = unchecked.removeFirst();
			if (!predicate.test(new EnviornmentBlockPos(world, pos1)))
			{
				for (EnumFacing facing : EnumFacing.VALUES)
				{
					if (Maths.lp1Distance(pos, pos2 = pos1.offset(facing)) <= max && !list.contains(pos2))
					{
						unchecked.add(pos2);
						list.add(pos2);
					}
				}
			}
			else
			{
				return Maths.lp1Distance(pos, pos1);
			}
		}
		return Integer.MAX_VALUE;
	}
}
