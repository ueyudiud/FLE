package farcore.util;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.block.IBurnCustomBehaviorBlock;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.collection.Stack;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.nbt.NBTTagCompoundEmpty;
import farcore.lib.util.Direction;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.LanguageManager;
import farcore.lib.world.ICoord;
import farcore.util.runnable.Burning;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.oredict.OreDictionary;

public class U
{
	private static final Random RNG = new Random();
	@SidedProxy(serverSide = "farcore.util.U$CommonHandler", clientSide = "farcore.util.U$ClientHandler")
	static CommonHandler handlerGatway;

	public static class L
	{
		public static int[] cast(Integer[] integers)
		{
			int[] ret = new int[integers.length];
			for(int i = 0; i < ret.length; ret[i++] = cast(integers[i]));
			return ret;
		}

		public static int cast(Integer integer)
		{
			return integer == null ? 0 : integer.intValue();
		}

		public static short cast(Short short1)
		{
			return short1 == null ? 0 : short1.shortValue();
		}

		public static void exit()
		{
			exit(0, false);
		}

		public static void exit(int code, boolean hardExit)
		{
			FMLCommonHandler.instance().exitJava(code, hardExit);
		}

		public static <T> T[] cast(Collection<? extends T> collection, Class<T> clazz)
		{
			T[] ret = (T[]) Array.newInstance(clazz, collection.size());
			return collection.toArray(ret);
		}

		public static <T> ImmutableList<T> castImmutable(T...list)
		{
			return ImmutableList.copyOf(list);
		}

		public static <T> ArrayList<T> castArray(T...list)
		{
			if(list == null || list.length == 0) return new ArrayList();
			return new ArrayList(Arrays.asList(list));
		}

		public static <T> boolean contain(Collection<? extends T> collection, IDataChecker<T> checker)
		{
			if(collection == null || collection.isEmpty()) return false;
			for(T target : collection)
				if(checker.isTrue(target)) return true;
			return false;
		}

		public static <T> Set<T> containSet(Collection<? extends T> collection, IDataChecker<T> checker)
		{
			if(collection == null || collection.isEmpty()) return ImmutableSet.of();
			Builder<T> builder = ImmutableSet.builder();
			for(T target : collection)
				if(checker.isTrue(target))
					builder.add(target);
			return builder.build();
		}

		public static <T> T randomInStack(Stack<T>[] stacks, Random random)
		{
			long weight = 0;
			for(Stack<T> stack : stacks)
				weight += stack.size;
			return randomInStack(stacks, weight, random);
		}

		public static <T> T randomInStack(Stack<T>[] stacks, long weight)
		{
			return randomInStack(stacks, weight, RNG);
		}

		public static <T> T randomInStack(Stack<T>[] stacks, long weight, Random random)
		{
			long rng = Maths.mod(random.nextLong(), weight);
			int i = 0;
			T target;
			do
			{
				target = stacks[i].element;
				rng -= stacks[i].size;
			}
			while(rng >= 0);
			return target;
		}

		public static <T> T random(T...list)
		{
			return random(list, RNG);
		}

		public static <T> T random(Random random, T...list)
		{
			return random(list, random);
		}

		public static <T> T random(T[] list, Random random)
		{
			return list == null || list.length == 0 ? null :
				list.length == 1 ? list[0] :
					list[random.nextInt(list.length)];
		}

		public static <T> T random(Collection<T> collection, Random random)
		{
			if(collection instanceof List)
				return (T) ((List) collection).get(random.nextInt(((List) collection).size()));
			else
				return (T) random(collection.toArray(), random);
		}

		public static int[] fillIntArray(int length, int value)
		{
			if(length == 0) return new int[0];
			if(length == 1) return new int[]{value};
			int[] ret = new int[length];
			Arrays.fill(ret, value);
			return ret;
		}

		public static boolean equal(Object arg1, Object arg2)
		{
			return arg1 == arg2 ? true :
				(arg1 == null ^ arg2 == null) ? false :
					arg1.equals(arg2);
		}

		public static int min(int...values)
		{
			int ret = Integer.MAX_VALUE;
			for(int i : values)
				if(i < ret) ret = i;
			return ret;
		}

		public static int max(int...values)
		{
			int ret = Integer.MIN_VALUE;
			for(int i : values)
				if(i > ret) ret = i;
			return ret;
		}

		public static int range(int m1, int m2, int target)
		{
			int v;
			return target > (v = Math.max(m1, m2)) ? v :
				target < (v = Math.min(m1, m2)) ? v : target;
		}

		public static double range(double m1, double m2, double target)
		{
			double v;
			return target > (v = Math.max(m1, m2)) ? v :
				target < (v = Math.min(m1, m2)) ? v : target;
		}

		public static int nextInt(int bound, Random rand)
		{
			return bound <= 0 ? bound : rand.nextInt(bound);
		}
	}

	public static class Strings
	{
		static final DecimalFormat format1 = new DecimalFormat("##0.0%");

		public static String locale()
		{
			return handlerGatway.getLocale();
		}

		public static String validate(String string)
		{
			if(string == null) return "";
			return string.trim();
		}

		public static String upcaseFirst(String name)
		{
			String s = validate(name);
			if(s.length() == 0) return s;
			char chr = name.charAt(0);
			String sub = name.substring(1);
			return Character.toString(Character.toUpperCase(chr)) + sub;
		}

		public static String validateOre(boolean upperFirst, String name)
		{
			String string = validate(name);
			String ret = "";
			boolean shouldUpperCase = upperFirst;
			for(char chr : string.toCharArray())
				if(chr == '_' || chr == ' ' ||
				chr == '-' || chr == '$' ||
				chr == '@' || chr == ' ')
				{
					shouldUpperCase = true;
					continue;
				}
				else if(shouldUpperCase)
				{
					ret += Character.toString(Character.toUpperCase(chr));
					shouldUpperCase = false;
				}
				else
					ret += Character.toString(chr);
			return ret;
		}

		public static String[] split(String str, char split)
		{
			if(str == null) return new String[0];
			else if(str.indexOf(split) != -1)
				return str.split(Character.toString(split));
			else
				return new String[]{str};
		}

		public static String progress(double value)
		{
			return format1.format(value);
		}

		public static String toOrdinalNumber(int value)
		{
			if(value < 0)
				return Integer.toString(value);
			int i1 = Maths.mod(value, 100);
			if(i1 <= 20 && i1 > 3)
				return value + "th";
			int i2 = Maths.mod(i1, 10);
			switch(i2)
			{
			case 1 : return value + "st";
			case 2 : return value + "nd";
			case 3 : return value + "rd";
			default: return value + "th";
			}
		}

		public static String toOrdinalNumber(long value)
		{
			if(value < 0)
				return Long.toString(value);
			int i1 = (int) Maths.mod(value, 100L);
			if(i1 <= 20 && i1 > 3)
				return value + "th";
			int i2 = Maths.mod(i1, 10);
			switch(i2)
			{
			case 1 : return value + "st";
			case 2 : return value + "nd";
			case 3 : return value + "rd";
			default: return value + "th";
			}
		}
	}

	public static class R
	{
		private static final Map<String, Field> fieldCache = new HashMap();
		private static Field modifiersField;

		public static void resetFieldCache()
		{
			fieldCache.clear();
		}

		private static void initModifierField()
		{
			try
			{
				if(modifiersField == null)
				{
					modifiersField = Field.class.getDeclaredField("modifiers");
					modifiersField.setAccessible(true);
				}
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}

		public static <T, F> void overrideField(Class<? extends T> clazz, String mcpName, String obfName, F override, boolean isPrivate, boolean alwaysInit) throws Exception
		{
			overrideField(clazz, mcpName, obfName, null, override, isPrivate, alwaysInit);
		}
		public static <T, F> void overrideField(Class<? extends T> clazz, String mcpName, String obfName, T target, F override, boolean isPrivate, boolean alwaysInit) throws Exception
		{
			boolean flag = false;
			List<Throwable> list = new ArrayList();
			for(String str : new String[]{mcpName, obfName})
				try
			{
					if(!alwaysInit && fieldCache.containsKey(clazz.getName() + "|" + str))
					{
						fieldCache.get(clazz.getName() + "|" + str).set(target, override);
						return;
					}
					Field tField;
					if(isPrivate) tField = clazz.getDeclaredField(str);
					else tField = clazz.getField(str);
					if(tField != null)
					{
						tField.setAccessible(true);
						fieldCache.put(clazz.getName() + "|" + str, tField);
						tField.set(target, override);
						flag = true;
						return;
					}
			}
			catch(Throwable e)
			{
				list.add(e);
				continue;
			}
			if(!flag)
			{
				for(Throwable e : list) e.printStackTrace();
				throw new RuntimeException();
			}
		}

		public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, F override, boolean isPrivate, boolean alwaysInit) throws Exception
		{
			overrideFinalField(clazz, mcpName, obfName, null, override, isPrivate, alwaysInit);
		}
		public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, T target, F override, boolean isPrivate, boolean alwaysInit) throws Exception
		{
			boolean flag = false;
			List<Throwable> list = new ArrayList();
			for(String str : new String[]{mcpName, obfName})
				try
			{
					initModifierField();
					if(!alwaysInit && fieldCache.containsKey(clazz.getName() + "|" + str))
					{
						fieldCache.get(clazz.getName() + "|" + str).set(target, override);
						return;
					}
					Field tField;
					if(isPrivate) tField = clazz.getDeclaredField(str);
					else tField = clazz.getField(str);
					modifiersField.setInt(tField, tField.getModifiers() & 0xFFFFFFEF);
					if(tField != null)
					{
						tField.setAccessible(true);
						fieldCache.put(clazz.getName() + "|" + str, tField);
						tField.set(target, override);
						flag = true;
						break;
					}
			}
			catch(Throwable e)
			{
				list.add(e);
				continue;
			}
			if(!flag)
			{
				for(Throwable e : list) e.printStackTrace();
				throw new RuntimeException("FLE: fail to find and override field " + mcpName);
			}
		}

		public static <T> Object getValue(Class<? extends T> clazz, String mcpName, String obfName, T target, boolean alwaysInit)
		{
			for(String str : new String[]{mcpName, obfName})
				try
			{
					if(!alwaysInit && fieldCache.containsKey(clazz.getName() + "|" + str))
						return fieldCache.get(clazz.getName() + "|" + str).get(target);
					Field tField = clazz.getDeclaredField(str);
					tField.setAccessible(true);
					fieldCache.put(clazz.getName() + "|" + str, tField);
					return tField.get(target);
			}
			catch(Throwable e)
			{
				continue;
			}
			return null;
		}

		public static Method getMethod(Class clazz, String mcpName, String obfName, Class...classes)
		{
			for(String str : new String[]{mcpName, obfName})
				try
			{
					Method tMethod = clazz.getDeclaredMethod(str, classes);
					return tMethod;
			}
			catch(Throwable e)
			{
				continue;
			}
			return null;
		}
	}

	public static class Maths
	{
		public static int mod(int a, int b)
		{
			int v;
			return (v = a % b) > 0 ? v : v + b;
		}

		public static long mod(long a, long b)
		{
			long v;
			return (v = a % b) > 0 ? v : v + b;
		}
	}

	public static class Mod
	{
		public static String getActiveModID()
		{
			if(Loader.instance().activeModContainer() == null)
				return "minecraft";
			return Loader.instance().activeModContainer().getModId();
		}

		public static boolean isModLoaded(String name)
		{
			return Loader.isModLoaded(name);
		}

		public static File getMCFile()
		{
			return handlerGatway.fileDir();
		}
	}

	public static class Sides
	{
		public static boolean isClient()
		{
			return FMLCommonHandler.instance().getSide().isClient();
		}

		public static boolean isServer()
		{
			return FMLCommonHandler.instance().getSide().isServer();
		}

		public static boolean isSimulating()
		{
			return FMLCommonHandler.instance().getEffectiveSide().isServer();
		}
	}

	public static class Worlds
	{
		public static World world(int dim)
		{
			return handlerGatway.worldInstance(dim);
		}

		public static Block getBlock(ICoord coord)
		{
			int[] is;
			return coord.world().getBlock((is = coord.coordI())[0], is[1], is[2]);
		}

		public static byte getMetadata(ICoord coord)
		{
			int[] is;
			return (byte) coord.world().getBlockMetadata((is = coord.coordI())[0], is[1], is[2]);
		}

		private static final int[][] rotateFix = {
				{3, 2, 5, 4},
				{1, 0, 5, 4},
				{1, 0, 3, 2}};

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
								Direction.oppsite[side] :
									rotateFix[side / 2][id];
		}

		public static boolean setBlock(int dim, int x, int y, int z, Block block, int meta, int updateType)
		{
			World world = world(dim);
			if(world == null) return false;
			return world.setBlock(x, y, z, block, meta, updateType);
		}

		public static TileEntity setTileEntity(World world, int x, int y, int z, TileEntity tile)
		{
			return setTileEntity(world, x, y, z, tile, true);
		}

		public static TileEntity setTileEntity(World world, int x, int y, int z, TileEntity tile, boolean update)
		{
			if(update)
				world.setTileEntity(x, y, z, tile);
			else
			{
				Chunk chunk = world.getChunkFromBlockCoords(x, z);
				if(chunk != null)
				{
					world.addTileEntity(tile);
					chunk.func_150812_a(x & 0xF, y, z & 0xF, tile);
					chunk.isModified = true;
				}
			}
			return tile;
		}

		public static boolean moveEntityToDimensionAtCoords(Entity entity, int dim, double x, double y, double z)
		{
			WorldServer targetWorld = DimensionManager.getWorld(dim);
			WorldServer originalWorld = DimensionManager.getWorld(entity.worldObj.provider.dimensionId);
			if ((targetWorld != null) && (originalWorld != null) && (targetWorld != originalWorld))
			{
				if (entity.ridingEntity != null)
					entity.mountEntity(null);
				if (entity.riddenByEntity != null)
					entity.riddenByEntity.mountEntity(null);
				if ((entity instanceof EntityPlayerMP))
				{
					EntityPlayerMP player = (EntityPlayerMP)entity;
					player.dimension = dim;
					player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
					originalWorld.removePlayerEntityDangerously(player);
					player.isDead = false;
					player.setWorld(targetWorld);
					MinecraftServer.getServer().getConfigurationManager().func_72375_a(player, originalWorld);
					player.playerNetServerHandler.setPlayerLocation(x + 0.5D, y + 0.5D, z + 0.5D, player.rotationYaw, player.rotationPitch);
					player.theItemInWorldManager.setWorld(targetWorld);
					MinecraftServer.getServer().getConfigurationManager().updateTimeAndWeatherForPlayer(player, targetWorld);
					MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory(player);
					for (Object object : player.getActivePotionEffects())
					{
						PotionEffect potioneffect = (PotionEffect) object;
						player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
					}
					player.playerNetServerHandler.setPlayerLocation(x + 0.5D, y + 0.5D, z + 0.5D, player.rotationYaw, player.rotationPitch);
					FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, originalWorld.provider.dimensionId, dim);
				}
				else
				{
					entity.setPosition(x + 0.5D, y + 0.5D, z + 0.5D);
					entity.worldObj.removeEntity(entity);
					entity.dimension = dim;
					entity.isDead = false;
					Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), targetWorld);
					if (newEntity != null)
					{
						newEntity.copyDataFrom(entity, true);
						entity.setDead();
						newEntity.isDead = false;
						boolean temp = newEntity.forceSpawn;
						newEntity.forceSpawn = true;
						targetWorld.spawnEntityInWorld(newEntity);
						newEntity.forceSpawn = temp;
						newEntity.isDead = false;
						entity = newEntity;
					}
				}
				if ((entity instanceof EntityLivingBase))
					((EntityLivingBase)entity).setPositionAndUpdate(x, y, z);
				else
					entity.setPosition(x, y, z);
				originalWorld.resetUpdateEntityTick();
				targetWorld.resetUpdateEntityTick();
				return true;
			}
			return false;
		}

		public static boolean isCatchingRain(World world, int x, int y, int z)
		{
			return isCatchingRain(world, x, y, z, false);
		}

		public static boolean isCatchingRain(World world, int x, int y, int z, boolean checkNeayby)
		{
			if(world.isRaining())
				return world.canBlockSeeTheSky(x, y, z) ||
						(checkNeayby && (
								world.canBlockSeeTheSky(x - 1, y, z) ||
								world.canBlockSeeTheSky(x + 1, y, z) ||
								world.canBlockSeeTheSky(x, y, z + 1) ||
								world.canBlockSeeTheSky(x, y, z - 1)));
			return false;
		}

		public static void removeBlock(World world, int x, int y, int z)
		{
			world.removeTileEntity(x, y, z);
			world.setBlockToAir(x, y, z);
		}

		public static int getAirDensity(World world, float y)
		{
			float v;
			if(world.provider.isHellWorld) return 30;
			else if(world.provider.dimensionId == 1) return -800;
			else return y < (v = (float) world.getWorldInfo().getTerrainType().getHorizon(world)) ||
					world.provider.hasNoSky ? 0 :
						(int) ((v - y) * 0.04F);
		}

		public static void spawnDropsInWorld(ICoord coord, List<ItemStack> drop)
		{
			if(coord.world().isRemote || drop == null) return;
			World world = coord.world();
			double[] ds = coord.coordD();
			float f = 0.2F;
			for(ItemStack stack : drop)
			{
				if(stack == null) continue;
				double d0 = world.rand.nextFloat() * f;
				double d1 = world.rand.nextFloat() * f;
				double d2 = world.rand.nextFloat() * f;
				EntityItem entityitem = new EntityItem(world, ds[0] + d0, ds[1] + d1, ds[1] + d2, stack.copy());
				entityitem.delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityitem);
			}
		}

		public static void spawnDropsInWorld(World world, int x, int y, int z, List<ItemStack> drop)
		{
			if(world.isRemote || drop == null) return;
			for(ItemStack stack : drop)
			{
				if(stack == null) continue;
				float f = 0.7F;
				double d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
				double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
				double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
				EntityItem entityitem = new EntityItem(world, x + d0, y + d1, z + d2, stack.copy());
				entityitem.delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityitem);
			}
		}

		public static void spawnDropsInWorldByPlayer(EntityPlayer player, ItemStack drop)
		{
			if(drop == null || drop.stackSize == 0 || player.worldObj.isRemote) return;
			player.dropPlayerItemWithRandomChoice(drop, false);
		}

		public static void spawnDropsInWorldByPlayerOpeningContainer(EntityPlayer player, IInventory inventory)
		{
			if(player.worldObj.isRemote) return;
			for(int i = 0; i < inventory.getSizeInventory(); ++i)
				spawnDropsInWorldByPlayer(player, inventory.getStackInSlotOnClosing(i));
		}

		public static ChunkCoordinates makeCoordinate(TileEntity tile)
		{
			return tile == null ? new ChunkCoordinates() :
				new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
		}

		//		/**
		//		 * With custom temperature use ASM to override method in world.
		//		 * @param world
		//		 * @param x
		//		 * @param y
		//		 * @param z
		//		 * @return
		//		 */
		//		public static float getBiomeBaseTemperature(World world, int x, int y, int z)
		//		{
		//			BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		//			if(biome instanceof BiomeBase)
		//			{
		//				return ((BiomeBase) biome).getTemperature(world, x, y, z);
		//			}
		//			return biome.getFloatTemperature(x, y, z);
		//		}
		//
		//		public static float getEnviormentTemp(World world, int x, int y, int z)
		//		{
		//			return ThermalNet.getEnviormentTemp(world, x, y, z);
		//		}
		//
		//		public static float getTemp(World world, int x, int y, int z)
		//		{
		//			return ThermalNet.getTemp(world, x, y, z, true);
		//		}

		public static boolean checkAndFallBlock(World world, int x, int y, int z, Block block)
		{
			if(world.isRemote) return false;
			if(block instanceof ISmartFallableBlock ?
					!((ISmartFallableBlock) block).canFallingBlockStay(world, x, y, z, world.getBlockMetadata(x, y, z)) :
						EntityFallingBlockExtended.canFallAt(world, x, y, z, block))
				return fallBlock(world, x, y, z, block);
			return false;
		}

		public static boolean fallBlock(World world, int x, int y, int z, Block block)
		{
			if(!BlockFalling.fallInstantly && world.checkChunksExist(x - 32, y - 32, z - 32, x + 32, y + 32, z + 32))
				return world.spawnEntityInWorld(new EntityFallingBlockExtended(world, x, y, z, block));
			else
			{
				int meta = world.getBlockMetadata(x, y, z);
				TileEntity tile = world.getTileEntity(x, y, z);
				if(tile != null)
				{
					tile.invalidate();
					world.removeTileEntity(x, y, z);
				}
				world.setBlockToAir(x, y, z);
				if(block instanceof ISmartFallableBlock)
					((ISmartFallableBlock) block).onStartFalling(world, x, y, z);
				int height = 0;
				while(!EntityFallingBlockExtended.canFallAt(world, x, y, z, block))
				{
					--y;
					++height;
				}
				if(y > 0)
				{
					EntityFallingBlockExtended.replaceFallingBlock(world, x, y, z, block, meta);
					NBTTagCompound nbt;
					tile.writeToNBT(nbt = new NBTTagCompound());
					if(block instanceof ISmartFallableBlock && ((ISmartFallableBlock) block).onFallOnGround(world, x, y, z, meta, height, nbt))
					{

					}
					else
					{
						world.setBlock(x, y, z, block, meta, 3);
						TileEntity tile1 = world.getTileEntity(x, y, z);
						if(tile1 != null)
						{
							tile1.writeToNBT(nbt);
							tile1.xCoord = x;
							tile1.yCoord = y;
							tile1.zCoord = z;
						}
					}
				}
				return true;
			}
		}

		public static boolean isBlockNearby(World world, int x, int y, int z, Block block, int meta, boolean ignoreUnloadChunk)
		{
			return isBlock(world, x + 1, y, z, block, meta, ignoreUnloadChunk) ||
					isBlock(world, x - 1, y, z, block, meta, ignoreUnloadChunk) ||
					isBlock(world, x, y + 1, z, block, meta, ignoreUnloadChunk) ||
					isBlock(world, x, y - 1, z, block, meta, ignoreUnloadChunk) ||
					isBlock(world, x, y, z + 1, block, meta, ignoreUnloadChunk) ||
					isBlock(world, x, y, z - 1, block, meta, ignoreUnloadChunk);
		}

		public static boolean isBlock(World world, int x, int y, int z, Block block, int meta, boolean ignoreUnloadChunk)
		{
			return (!ignoreUnloadChunk || world.blockExists(x, y, z)) &&
					world.getBlock(x, y, z) == block &&
					(meta < 0 || world.getBlockMetadata(x, y, z) == meta);
		}

		public static boolean isAirNearby(World world, int x, int y, int z, boolean ignoreUnloadChunk)
		{
			return (!ignoreUnloadChunk ||
					world.checkChunksExist(x - 1, 1, z - 1, x + 1, 2, z + 1)) &&
					(world.isAirBlock(x + 1, y, z) ||
					world.isAirBlock(x - 1, y, z) ||
					world.isAirBlock(x, y, z - 1) ||
					world.isAirBlock(x, y, z + 1) ||
					world.isAirBlock(x, y - 1, z) ||
					world.isAirBlock(x, y + 1, z));
		}

		public static MovingObjectPosition getMovingObjectPosition(World world, EntityPlayer player, boolean checkFluid)
		{
			float f = 1.0F;
			float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
			float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * f + (world.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
			Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
			float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
			float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
			float f5 = -MathHelper.cos(-f1 * 0.017453292F);
			float f6 = MathHelper.sin(-f1 * 0.017453292F);
			float f7 = f4 * f5;
			float f8 = f3 * f5;
			double d3 = 5.0D;
			if (player instanceof EntityPlayerMP)
				d3 = ((EntityPlayerMP)player).theItemInWorldManager.getBlockReachDistance();
			Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
			return world.func_147447_a(vec3, vec31, checkFluid, !checkFluid, false);
		}

		public static void createBurnAt(World world, int x, int y, int z, float hardness)
		{
			if(!world.isRemote)
				new Thread(new Burning(world, x, y, z, hardness)).start();
		}

		public static void tryBurnBlock(World world, int x, int y, int z, float hardness, Direction direction)
		{
			Block block = world.getBlock(x, y, z);
			if(block instanceof IBurnCustomBehaviorBlock &&
					((IBurnCustomBehaviorBlock) block).onBurn(world, x, y, z, hardness, direction))
				return;
			if(block.isFlammable(world, x, y, z, direction.of()) &&
					world.isAirBlock(x, y + 1, z) &&
					block.getFlammability(world, x, y, z, direction.of()) / 300F > world.rand.nextFloat() * Math.log(1 + hardness))
			{
				world.setBlock(x, y, z, Blocks.fire, 0, 3);
				world.scheduleBlockUpdate(x, y, z, Blocks.fire, 8);
			}
		}
	}

	public static class Players
	{
		public static EntityPlayer player()
		{
			return handlerGatway.playerInstance();
		}
	}

	public static class ItemStacks
	{
		public static ItemStack valid(ItemStack stack)
		{
			if(stack == null || stack.stackSize <= 0)
				return null;
			if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			{
				stack = stack.copy();
				stack.setItemDamage(0);
			}
			return stack;
		}

		public static NBTTagCompound setupNBT(ItemStack stack, boolean createTag)
		{
			if(!stack.hasTagCompound())
			{
				if(createTag)
					return stack.stackTagCompound = new NBTTagCompound();
				return NBTTagCompoundEmpty.instance;
			}
			return stack.getTagCompound();
		}

		public static ImmutableList<ItemStack> sizeOf(List<ItemStack> stacks, int size)
		{
			if(stacks == null || stacks.isEmpty()) return ImmutableList.of();
			ImmutableList.Builder builder = ImmutableList.builder();
			for(ItemStack stack : stacks)
				if(stack != null)
				{
					ItemStack stack2 = stack.copy();
					stack2.stackSize = size;
					builder.add(valid(stack2.copy()));
				}
			return builder.build();
		}

		public static ItemStack sizeOf(ItemStack stack, int size)
		{
			ItemStack ret;
			(ret = stack.copy()).stackSize = size;
			return ret;
		}
	}

	public static class OreDict
	{
		public static void registerValid(String name, Block ore)
		{
			registerValid(name, new ItemStack(ore, 1, OreDictionary.WILDCARD_VALUE));
		}

		public static void registerValid(String name, Item ore)
		{
			registerValid(name, new ItemStack(ore, 1, OreDictionary.WILDCARD_VALUE));
		}

		public static void registerValid(String name, ItemStack ore)
		{
			if(U.ItemStacks.valid(ore) == null) return;
			ItemStack register = ore.copy();
			register.stackSize = 1;
			OreDictionary.registerOre(name, ore);
		}

		public static void registerValid(String name, ItemStack ore, boolean autoValid)
		{
			if(autoValid)
				name = U.Strings.validateOre(false, name);
			registerValid(name, ore);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class Client
	{
		public static boolean shouldRenderBetterLeaves()
		{
			return (boolean) R.getValue(BlockLeavesBase.class, "field_150121_P", "field_150121_P", Blocks.leaves, false);
		}
	}

	public static class CommonHandler
	{
		public World worldInstance(int id)
		{
			return DimensionManager.getWorld(id);
		}

		public String getLocale()
		{
			return LanguageManager.ENGLISH;
		}

		public File fileDir()
		{
			return new File(".");
		}

		public EntityPlayer playerInstance()
		{
			return null;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class ClientHandler extends CommonHandler
	{
		@Override
		public World worldInstance(int id)
		{
			if (U.Sides.isSimulating())
				return super.worldInstance(id);
			else
			{
				World world = Minecraft.getMinecraft().theWorld;
				return world == null ? null : world.provider.dimensionId != id ? null : world;
			}
		}

		@Override
		public String getLocale()
		{
			return Minecraft.getMinecraft().getLanguageManager()
					.getCurrentLanguage().getLanguageCode();
		}

		@Override
		public EntityPlayer playerInstance()
		{
			return Minecraft.getMinecraft().thePlayer;
		}

		@Override
		public File fileDir()
		{
			return Minecraft.getMinecraft().mcDataDir;
		}
	}
}