/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import nebula.Nebula;
import nebula.common.CommonOverride;
import nebula.common.block.ISmartFallableBlock;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.network.packet.PacketBreakBlock;
import nebula.common.world.ICoord;
import nebula.common.world.IObjectInWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.BlockFluidBase;

/**
 * @author ueyudiud
 */
public final class Worlds
{
	private static final int[][] rotateFix = {
			{3, 2, 5, 4},
			{1, 0, 5, 4},
			{1, 0, 3, 2}};
	
	private Worlds() {}
	
	/**
	 * I don't know why some mod can be crashed on this method, use this instead.
	 * @param world
	 * @param pos
	 * @param side
	 * @param def
	 * @return
	 */
	public static boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side, boolean def)
	{
		if (pos.getY() >= 256 || pos.getY() < 0)
			return def;
		IBlockState state = world.getBlockState(pos);
		return state.isSideSolid(world, pos, side);
	}
	
	public static boolean isAirOrReplacable(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		return state.getBlock().isAir(state, world, pos) || state.getBlock().isReplaceable(world, pos);
	}
	
	public static void breakBlockWithoutSource(World world, BlockPos pos, boolean harvestBlock)
	{
		if(!world.isRemote) //This method have not effect in client world, it will send a packet to client.
		{
			if(!world.isAreaLoaded(pos, 64))
			{
				world.setBlockToAir(pos);
			}
			if(isAir(world, pos)) return;
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			block.breakBlock(world, pos, state);
			Nebula.network.sendToNearBy(new PacketBreakBlock(world, pos), world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 5);
			if(harvestBlock)
			{
				block.dropBlockAsItem(world, pos, state, 0);
			}
		}
	}
	
	public static <E> List<E> getListFromWorldDimention(Map<Integer, List<E>> map, World world, boolean createEntry)
	{
		Integer dim = world.provider.getDimension();
		List<E> list = map.get(dim);
		if(list == null)
		{
			if(createEntry)
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
	 * Fixed 6 direction side of facing, for change to another face when that face is not exposed.
	 * @param side
	 * @param hitX
	 * @param hitY
	 * @param hitZ
	 * @return
	 */
	public static int fixSide(int side, float hitX, float hitY, float hitZ)
	{
		float u, v;
		if(side == 0 || side == 1)
		{
			u = hitX;
			v = hitZ;
		}
		else if(side == 2 || side == 3)
		{
			u = hitX;
			v = hitY;
		}
		else if(side == 4 || side == 5)
		{
			u = hitZ;
			v = hitY;
		}
		else
		{
			u = 0.5F;
			v = 0.5F;
		}
		int id;
		boolean b1 = u >= 0.25F, b2 = v >= 0.25F, b3 = u <= 0.75F, b4 = v <= 0.75F;
		return b1 && b2 && b3 && b4 ?
				side : (id = (b1 && b3 ? (!b4 ? 1 : 0) :
					(b2 && b4) ? (!b3 ? 3 : 2) : -1)) == -1 ?
							Direction.OPPISITE[side] :
								rotateFix[side / 2][id];
	}
	
	public static void spawnDropInWorld(World world, BlockPos pos, ItemStack drop)
	{
		if(world.isRemote ||
				//Debug world can drop item will crash the game...
				world.getWorldType() == WorldType.DEBUG_WORLD ||
				drop == null)
			return;
		float f = 0.7F;
		double d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		EntityItem entityitem = new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, drop.copy());
		entityitem.setPickupDelay(10);
		world.spawnEntity(entityitem);
	}
	
	public static void spawnDropInWorld(World world, BlockPos pos, Direction direction, ItemStack drop)
	{
		if(world.isRemote ||
				//Debug world can drop item will crash the game...
				world.getWorldType() == WorldType.DEBUG_WORLD ||
				drop == null)
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
	 * Spawn a list of items in the world.<p>
	 * It usually called when block is broke.
	 * @param world the world.
	 * @param pos the spawn position.
	 * @param drops the drops item stacks.
	 */
	public static void spawnDropsInWorld(World world, BlockPos pos, @Nullable List<ItemStack> drops)
	{
		if(world.isRemote ||
				world.getWorldType() == WorldType.DEBUG_WORLD ||
				//Debug world can drop item will crash the game...
				drops == null) return;
		for(ItemStack stack : drops)
		{
			if(stack == null)
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
		if(drop == null || drop.stackSize == 0 || player.world.isRemote) return;
		player.dropItem(drop, false);
	}
	
	public static void spawnDropsInWorldByPlayerOpeningContainer(EntityPlayer player, IInventory inventory)
	{
		if(player.world.isRemote) return;
		for(int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			spawnDropInWorld(player, inventory.removeStackFromSlot(i));
		}
	}
	
	/**
	 * Check can block stay and fall block.
	 * @param world the world.
	 * @param pos the checking position.
	 * @return return <tt>true</tt> when falling is matched and happened.
	 */
	public static boolean checkAndFallBlock(World world, BlockPos pos)
	{
		if(world.isRemote) return false;
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof ISmartFallableBlock ?
				!((ISmartFallableBlock) state.getBlock()).canFallingBlockStay(world, pos, state.getActualState(world, pos)) :
					EntityFallingBlockExtended.canFallAt(world, pos, state))
			return fallBlock(world, pos, state);
		return false;
	}
	
	/**
	 * Fall block with starting at located position.
	 * @param world the falling block generated world.
	 * @param pos the located position.
	 * @param state the block state.
	 * @return return <tt>true</tt> when falling action is success happen,
	 * or the side is client or falling block not generate successfully otherwise.
	 * @see Worlds#fallBlock(World, BlockPos, BlockPos, IBlockState)
	 */
	public static boolean fallBlock(World world, BlockPos pos, IBlockState state)
	{
		return fallBlock(world, pos, pos, state);
	}
	
	/**
	 * Generate a falling block at drop position, and the source is from start position,
	 * a helper method to create falling block (such behavior like vanilla sand).<p>
	 * The source block will be removed after falling action is started, and a new
	 * {@link nebula.common.entity.EntityFallingBlockExtended} will be spawn in the world if
	 * option of falling block instantly is disabled and the chunk of handling falling action is loaded,
	 * or try to falling instantly.<p>
	 * If the block is instance of {@link nebula.common.block.ISmartFallableBlock}.
	 * The method will be called after entity spawning.<p>
	 * 
	 * @param world the world.
	 * @param pos the block at this position will be remove to air.
	 * @param dropPos the falling entity block starting position, it usually same to start position.
	 * @param state the falling block state.
	 * @return return <tt>true</tt> when falling action is success happen,
	 * or the side is client or falling block not generate successfully otherwise.
	 * @see net.minecraft.block.BlockFalling
	 * @see nebula.common.block.ISmartFallableBlock
	 * @see nebula.common.entity.EntityFallingBlockExtended
	 */
	public static boolean fallBlock(World world, BlockPos pos, BlockPos dropPos, IBlockState state)
	{
		if(!BlockFalling.fallInstantly && world.isAreaLoaded(pos, 32))
		{
			world.setBlockToAir(pos);
			return world.isRemote || world.spawnEntity(new EntityFallingBlockExtended(world, pos, dropPos, state, world.getTileEntity(pos)));
		}
		else
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null)
			{
				world.removeTileEntity(pos);
			}
			world.setBlockToAir(pos);
			if(state.getBlock() instanceof ISmartFallableBlock)
			{
				((ISmartFallableBlock) state.getBlock()).onStartFalling(world, pos);
			}
			int height = 0;
			while(!EntityFallingBlockExtended.canFallAt(world, pos, state))
			{
				pos = pos.down();
				++height;
			}
			if(pos.getY() > 0)
			{
				EntityFallingBlockExtended.replaceFallingBlock(world, pos, state, height);
				NBTTagCompound nbt = new NBTTagCompound();
				if (tile != null)
				{
					tile.writeToNBT(nbt);
				}
				if(state.getBlock() instanceof ISmartFallableBlock && ((ISmartFallableBlock) state.getBlock()).onFallOnGround(world, pos, state, height, nbt))
				{
					
				}
				else
				{
					world.setBlockState(pos, state, 2);
					TileEntity tile1 = world.getTileEntity(pos);
					if(tile1 != null)
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
	 * @param dimID the dimension id of world.
	 * @return the current world with dimension id, or <tt>null</tt> if world not found.
	 */
	public static @Nullable World world(int dimID)
	{
		return Nebula.proxy.worldInstance(dimID);
	}
	
	public static double distanceSqTo(IObjectInWorld object, BlockPos pos)
	{
		double[] cache = object.position();
		return distanceSqTo(cache[0] - pos.getX() + .5, cache[1] - pos.getY() + .5, cache[2] - pos.getZ() + .5);
	}
	
	public static double distanceSqTo(IObjectInWorld object1, IObjectInWorld object2)
	{
		double[] cache1 = object1.position();
		double[] cache2 = object2.position();
		return distanceSqTo(cache1[0] - cache2[0], cache1[1] - cache2[1], cache1[2] - cache2[2]);
	}
	
	public static double distanceSqTo(BlockPos pos1, BlockPos pos2)
	{
		return distanceSqTo(pos1.getX() - pos2.getX(), pos1.getY() - pos2.getY(), pos1.getZ() - pos2.getZ());
	}
	
	public static double distanceSqTo(Entity entity, BlockPos pos)
	{
		return entity.getDistanceSq(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
	}
	
	public static double distanceSqTo(double x, double y, double z)
	{
		return x * x + y * y + z * z;
	}
	
	public static <T extends Comparable<T>> boolean switchProp(World world, BlockPos pos, IProperty<T> property, T value, int updateFlag)
	{
		IBlockState state = world.getBlockState(pos);
		if(state.getValue(property) == value) return false;
		return world.setBlockState(pos, state.withProperty(property, value), updateFlag);
	}
	
	public static boolean isBlockNearby(World world, BlockPos pos, Block block, boolean ignoreUnloadChunk)
	{
		return isBlockNearby(world, pos, block, -1, ignoreUnloadChunk);
	}
	
	public static boolean isBlockNearby(World world, BlockPos pos, Block block, int meta, boolean ignoreUnloadChunk)
	{
		return
				isBlock(world, pos.up(), block, meta, ignoreUnloadChunk)   ||
				isBlock(world, pos.down(), block, meta, ignoreUnloadChunk) ||
				isBlock(world, pos.east(), block, meta, ignoreUnloadChunk) ||
				isBlock(world, pos.west(), block, meta, ignoreUnloadChunk) ||
				isBlock(world, pos.north(), block, meta, ignoreUnloadChunk)||
				isBlock(world, pos.south(), block, meta, ignoreUnloadChunk);
	}
	
	public static boolean isBlock(World world, BlockPos pos, Block block, int meta, boolean ignoreUnloadChunk)
	{
		IBlockState state;
		return (!ignoreUnloadChunk || world.isAreaLoaded(pos, 0)) &&
				(state = world.getBlockState(pos)).getBlock() == block &&
				(meta < 0 || state.getBlock().getMetaFromState(state) == meta);
	}
	
	public static boolean isAir(IBlockAccess world, BlockPos pos)
	{
		IBlockState state;
		return (state = world.getBlockState(pos)).getBlock().isAir(state, world, pos);
	}
	
	public static boolean isAirNearby(World world, BlockPos pos, boolean ignoreUnloadChunk)
	{
		return (!ignoreUnloadChunk || world.isAreaLoaded(pos, 1)) && (
				isAir(world, pos.up())   ||
				isAir(world, pos.down()) ||
				isAir(world, pos.west()) ||
				isAir(world, pos.east()) ||
				isAir(world, pos.north())||
				isAir(world, pos.south()));
	}
	
	public static boolean isNotOpaqueNearby(World world, BlockPos pos)
	{
		return !world.isAreaLoaded(pos, 1) || !(
				world.isSideSolid(pos.up(),    EnumFacing.DOWN) &&
				world.isSideSolid(pos.down(),  EnumFacing.UP)   &&
				world.isSideSolid(pos.west(),  EnumFacing.EAST) &&
				world.isSideSolid(pos.east(),  EnumFacing.WEST) &&
				world.isSideSolid(pos.north(), EnumFacing.SOUTH)&&
				world.isSideSolid(pos.south(), EnumFacing.NORTH));
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
		return world.isRaining() ?
				(world.canBlockSeeSky(pos) && CommonOverride.isRainingAtBiome(world.getBiome(pos), world, pos)) ||
				(checkNeayby && (
						world.canBlockSeeSky(pos.north()) ||
						world.canBlockSeeSky(pos.south()) ||
						world.canBlockSeeSky(pos.east()) ||
						world.canBlockSeeSky(pos.west()))) :
							false;
	}
	
	public static TileEntity setTileEntity(World world, BlockPos pos, TileEntity tile, boolean update)
	{
		if(update)
		{
			world.setTileEntity(pos, tile);
		}
		else
		{
			Chunk chunk = world.getChunkFromBlockCoords(pos);
			if(chunk != null)
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
		if(aabb.maxX < post[0] || aabb.minX > post[0] ||
				aabb.maxY < post[1] || aabb.minY > post[1] ||
				aabb.maxZ < post[2] || aabb.minZ > post[2])
			return null;
		else
			return aabb.maxY < pre[1] ? Direction.U :
				aabb.minY > pre[1] ? Direction.D :
					aabb.maxX < pre[0] ? Direction.E :
						aabb.minX > pre[0] ? Direction.W :
							aabb.maxZ < pre[2] ? Direction.S :
								aabb.minZ > pre[2] ? Direction.N :
									Direction.Q;
	}
	
	public static RayTraceResult rayTrace(World world, EntityLivingBase entity, boolean useLiquids)
	{
		float f = entity.rotationPitch;
		float f1 = entity.rotationYaw;
		double d0 = entity.posX;
		double d1 = entity.posY + entity.getEyeHeight();
		double d2 = entity.posZ;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = 5.0D;
		if (entity instanceof net.minecraft.entity.player.EntityPlayerMP)
		{
			d3 = ((net.minecraft.entity.player.EntityPlayerMP)entity).interactionManager.getBlockReachDistance();
		}
		Vec3d vec3d1 = vec3d.addVector(f6 * d3, f5 * d3, f7 * d3);
		return world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}
	
	private static Method isChunkLoaded;
	
	public static boolean isRedstoneChecking()
	{
		return !Blocks.REDSTONE_WIRE.canProvidePower(null);
	}
	
	public static boolean isChunkLoaded(World world, int x, int z, boolean allowEmpty)
	{
		if(isChunkLoaded == null)
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
	
	public static void spawnParticleWithRandomOffset(World world, EnumParticleTypes types, double xCoord,
			double yCoord, double zCoord, double motionX, double motionY, double motionZ, double randScale, int...datas)
	{
		Random random = L.random();
		world.spawnParticle(types,
				xCoord + (random.nextFloat() - random.nextFloat()) * randScale,
				yCoord + (random.nextFloat() - random.nextFloat()) * randScale,
				zCoord + (random.nextFloat() - random.nextFloat()) * randScale,
				motionX, motionY, motionZ, datas);
	}
	
	/**
	 * Check if item can drop at position.
	 * @param world
	 * @param pos
	 * @param side
	 * @return
	 */
	public static boolean isItemDropable(World world, BlockPos pos, @Nullable Direction side)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().isAir(state, world, pos))
			return true;
		return state.getBlock() instanceof BlockLiquid || state.getBlock() instanceof BlockFluidBase ||
				(!state.isFullCube() &&
						(side == null || !state.isSideSolid(world, pos, side.of())) &&
						state.getCollisionBoundingBox(world, pos) == Block.NULL_AABB);
	}
}