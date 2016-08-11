package farcore.util;

import java.io.File;
import java.io.FileNotFoundException;
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

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import farcore.FarCoreSetup;
import farcore.data.Capabilities;
import farcore.data.Config;
import farcore.data.EnumToolType;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.collection.Stack;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.item.ITool;
import farcore.lib.model.block.ICustomItemModelSelector;
import farcore.lib.model.block.ModelFluidBlock;
import farcore.lib.nbt.NBTTagCompoundEmpty;
import farcore.lib.render.FontRenderExtend;
import farcore.lib.tile.IItemHandlerIO;
import farcore.lib.tile.IToolableTile;
import farcore.lib.tile.TEBase;
import farcore.lib.util.Direction;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.ThreadLight;
import farcore.lib.world.IBiomeExtended;
import farcore.lib.world.IBiomeRegetter;
import farcore.lib.world.ICoord;
import farcore.lib.world.IObjectInWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
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
			for(int i = 0; i < ret.length; ret[i++] = cast(integers[i]))
			{
				;
			}
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

		public static <K, V> void put(Map<K, List<V>> map, K key, V value)
		{
			if(map.containsKey(key))
			{
				map.get(key).add(value);
			}
			else
			{
				map.put(key, new ArrayList(ImmutableList.of(value)));
			}
		}

		public static <K, V> void put(Map<K, List<V>> map, K key, V...values)
		{
			if(values.length == 0) return;
			if(values.length == 1)
			{
				put(map, key, values[0]);
			}
			else
			{
				put(map, key, Arrays.asList(values));
			}
		}

		public static <K, V> void put(Map<K, List<V>> map, K key, Collection<V> values)
		{
			if(map.containsKey(key))
			{
				map.get(key).addAll(values);
			}
			else
			{
				map.put(key, new ArrayList(values));
			}
		}
		
		public static <K, V> boolean remove(Map<K, List<V>> map, K key, V value)
		{
			if(map.containsKey(key))
				return map.get(key).remove(value);
			return false;
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
				{
					builder.add(target);
				}
			return builder.build();
		}

		public static <T> T randomInStack(Stack<T>[] stacks, Random random)
		{
			long weight = 0;
			for(Stack<T> stack : stacks)
			{
				weight += stack.size;
			}
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
				if(i < ret)
				{
					ret = i;
				}
			return ret;
		}

		public static float min(float...values)
		{
			float ret = Float.MAX_VALUE;
			for(float i : values)
				if(i < ret)
				{
					ret = i;
				}
			return ret;
		}

		public static int max(int...values)
		{
			int ret = Integer.MIN_VALUE;
			for(int i : values)
				if(i > ret)
				{
					ret = i;
				}
			return ret;
		}

		public static float max(float...values)
		{
			float ret = Float.MIN_VALUE;
			for(float i : values)
				if(i > ret)
				{
					ret = i;
				}
			return ret;
		}

		public static int range(int m1, int m2, int target)
		{
			int v;
			return target > (v = Math.max(m1, m2)) ? v :
				target < (v = Math.min(m1, m2)) ? v : target;
		}

		public static float range(float m1, float m2, float target)
		{
			float v;
			return target > (v = Math.max(m1, m2)) ? v :
				target < (v = Math.min(m1, m2)) ? v : target;
		}

		public static double range(double m1, double m2, double target)
		{
			double v;
			return target > (v = Math.max(m1, m2)) ? v :
				target < (v = Math.min(m1, m2)) ? v : target;
		}

		public static int nextInt(int bound)
		{
			return nextInt(bound, RNG);
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

		public static String validateProperty(String string)
		{
			if(string == null) return "";
			String newString = "";
			for(char chr : string.toCharArray())
			{
				if(chr == '-' || chr == '\\' || chr == '/' || chr == '.' || chr == ' ')
				{
					newString += '_';
				}
				else
				{
					newString += chr;
				}
			}
			return newString.trim();
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
				{
					ret += Character.toString(chr);
				}
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

		private static Field getField(Class<?> clazz, String mcpName, String obfName, boolean isPrivate, boolean isFinal, boolean alwaysInit) throws Exception
		{
			if(isFinal)
			{
				initModifierField();
			}
			for(String str : new String[]{mcpName, obfName})
			{
				try
				{
					if(!alwaysInit && fieldCache.containsKey(clazz.getName() + "|" + str))
						return fieldCache.get(clazz.getName() + "|" + str);
					Field tField;
					if(isPrivate)
					{
						tField = clazz.getDeclaredField(str);
					}
					else
					{
						tField = clazz.getField(str);
					}
					if(isFinal)
					{
						modifiersField.setInt(tField, tField.getModifiers() & 0xFFFFFFEF);
					}
					if(tField != null)
					{
						tField.setAccessible(true);
						fieldCache.put(clazz.getName() + "|" + str, tField);
						return tField;
					}
				}
				catch(Exception exception)
				{
					throw exception;
				}
			}
			throw new FileNotFoundException();
		}

		public static <T, F> void overrideField(Class<? extends T> clazz, String mcpName, String obfName, F override, boolean isPrivate, boolean alwaysInit) throws Exception
		{
			overrideField(clazz, mcpName, obfName, null, override, isPrivate, alwaysInit);
		}
		public static <T, F> void overrideField(Class<? extends T> clazz, String mcpName, String obfName, T target, F override, boolean isPrivate, boolean alwaysInit) throws Exception
		{
			try
			{
				getField(clazz, mcpName, obfName, isPrivate, false, alwaysInit).set(target, override);
			}
			catch(Throwable e)
			{
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		
		public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, F override, boolean isPrivate, boolean alwaysInit) throws Exception
		{
			overrideFinalField(clazz, mcpName, obfName, null, override, isPrivate, alwaysInit);
		}
		public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, T target, F override, boolean isPrivate, boolean alwaysInit) throws Exception
		{
			try
			{
				getField(clazz, mcpName, obfName, isPrivate, true, alwaysInit).set(target, override);
			}
			catch(Throwable e)
			{
				e.printStackTrace();
				throw new RuntimeException("FLE: fail to find and override field " + mcpName);
			}
		}
		public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, T target, int override, boolean isPrivate, boolean alwaysInit) throws Exception
		{
			try
			{
				getField(clazz, mcpName, obfName, isPrivate, true, alwaysInit).setInt(target, override);
			}
			catch(Throwable e)
			{
				e.printStackTrace();
				throw new RuntimeException("FLE: fail to find and override field " + mcpName);
			}
		}

		public static <T> Object getValue(Class<? extends T> clazz, String mcpName, String obfName, T target, boolean alwaysInit)
		{
			try
			{
				return getField(clazz, mcpName, obfName, true, false, alwaysInit).get(target);
			}
			catch(Exception exception)
			{
				return null;
			}
		}

		public static <T> int getInt(Class<? extends T> clazz, String mcpName, String obfName, T target, boolean alwaysInit)
		{
			try
			{
				return getField(clazz, mcpName, obfName, true, false, alwaysInit).getInt(target);
			}
			catch(Exception exception)
			{
				return 0;
			}
		}

		public static Method getMethod(Class clazz, String mcpName, String obfName, Class...classes)
		{
			for(String str : new String[]{mcpName, obfName})
			{
				try
				{
					Method tMethod = clazz.getDeclaredMethod(str, classes);
					return tMethod;
				}
				catch(Throwable e)
				{
					continue;
				}
			}
			return null;
		}
	}

	public static class Maths
	{
		public static final double sq2 = 1.4142135623730950488016887242097;

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

		public static float average(float...floats)
		{
			float j = 0;
			for(float i : floats)
			{
				j += i;
			}
			return j / floats.length;
		}

		public static double average(double...doubles)
		{
			double j = 0;
			for(double i : doubles)
			{
				j += i;
			}
			return j / doubles.length;
		}
		
		public static float lerp(float a, float b, float x)
		{
			return a + (b - a) * x;
		}
		
		public static double lerp(double a, double b, double x)
		{
			return a + (b - a) * x;
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

		public static void registerBlock(Block block, String name)
		{
			registerBlock(block, getActiveModID(), name);
		}

		public static void registerBlock(Block block, String modid, String name)
		{
			registerBlock(block, modid, name, new ItemBlock(block));
		}

		public static void registerBlock(Block block, String modid, String name, Item itemBlock)
		{
			GameRegistry.register(block.setRegistryName(modid, name));
			GameRegistry.register(itemBlock.setRegistryName(modid, name));
		}
		
		public static void registerItem(Item item, String name)
		{
			registerItem(item, getActiveModID(), name);
		}
		
		public static void registerItem(Item item, String modid, String name)
		{
			GameRegistry.register(item.setRegistryName(modid, name));
		}
		
		@SideOnly(Side.CLIENT)
		public static void registerItemBlockModel(Block block, int meta, String modid, String locate)
		{
			registerItemBlockModel(Item.getItemFromBlock(block), meta, modid, locate);
		}
		
		@SideOnly(Side.CLIENT)
		public static void registerItemBlockModel(Item item, int meta, String modid, String locate)
		{
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modid + ":" + locate, "inventory"));
		}

		/**
		 * Because this method is often use in item initialization, to
		 * check the side is client or server is too inconvenient, so
		 * this method used handler gateway.
		 * @param item
		 * @param meta
		 * @param modid
		 * @param locate
		 */
		public static void registerItemModel(Item item, int meta, String modid, String locate)
		{
			handlerGatway.setModelLocate(item, meta, modid, locate);
		}

		public static void registerBiomeColorMultiplier(Block...block)
		{
			handlerGatway.registerBiomeColorMultiplier(block);
		}

		@SideOnly(Side.CLIENT)
		public static void registerColorMultiplier(IBlockColor color, Block...block)
		{
			FarCoreSetup.proxy.registerColorMultiplier(color, block);
		}
		
		@SideOnly(Side.CLIENT)
		public static void registerFluid(BlockFluidBase block)
		{
			registerCustomItemModelSelector(Item.getItemFromBlock(block), ModelFluidBlock.Selector.instance);
			ModelLoader.setCustomStateMapper(block, ModelFluidBlock.Selector.instance);
		}

		@SideOnly(Side.CLIENT)
		public static void registerCustomItemModelSelector(Item item, ICustomItemModelSelector selector)
		{
			ModelLoader.setCustomMeshDefinition(item, selector);
			ModelBakery.registerItemVariants(item, selector.getAllowedResourceLocations(item).toArray(new ResourceLocation[0]));
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
			{
				name = U.Strings.validateOre(false, name);
			}
			registerValid(name, ore);
		}
	}

	public static class Worlds
	{
		private static final int[][] rotateFix = {
				{3, 2, 5, 4},
				{1, 0, 5, 4},
				{1, 0, 3, 2}};

		public static int fixSide(EnumFacing side, float hitX, float hitY, float hitZ)
		{
			return fixSide(side.ordinal(), hitX, hitY, hitZ);
		}

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
		
		public static void checkLight(World world, BlockPos pos)
		{
			if(Config.multiThreadLight)
			{
				new Thread(new ThreadLight(world, pos)).run();
			}
			else
			{
				world.theProfiler.startSection("checkLight");
				world.checkLight(pos);
				world.theProfiler.endSection();
			}
		}

		public static void spawnDropsInWorld(World world, BlockPos pos, List<ItemStack> drops)
		{
			if(world.isRemote || drops == null) return;
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
				world.spawnEntityInWorld(entityitem);
			}
		}

		public static void spawnDropInWorld(ICoord coord, ItemStack drop)
		{
			spawnDropsInWorld(coord, Arrays.asList(drop));
		}

		public static void spawnDropsInWorld(ICoord coord, List<ItemStack> drop)
		{
			spawnDropsInWorld(coord.world(), coord.pos(), drop);
		}

		public static void spawnDropsInWorld(EntityPlayer player, ItemStack drop)
		{
			if(drop == null || drop.stackSize == 0 || player.worldObj.isRemote) return;
			player.dropItem(drop, false);
		}

		public static void spawnDropsInWorldByPlayerOpeningContainer(EntityPlayer player, IInventory inventory)
		{
			if(player.worldObj.isRemote) return;
			for(int i = 0; i < inventory.getSizeInventory(); ++i)
			{
				spawnDropsInWorld(player, inventory.removeStackFromSlot(i));
			}
		}

		public static boolean checkAndFallBlock(World world, BlockPos pos)
		{
			if(world.isRemote) return false;
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() instanceof ISmartFallableBlock ?
					!((ISmartFallableBlock) state.getBlock()).canFallingBlockStay(world, pos, state) :
						EntityFallingBlockExtended.canFallAt(world, pos, state))
				return fallBlock(world, pos, state);
			return false;
		}

		public static boolean fallBlock(World world, BlockPos pos, IBlockState state)
		{
			if(!BlockFalling.fallInstantly && world.isAreaLoaded(pos, 32))
				return world.isRemote ||
						world.spawnEntityInWorld(new EntityFallingBlockExtended(world, pos, state));
			else
			{
				TileEntity tile = world.getTileEntity(pos);
				if(tile != null)
				{
					tile.invalidate();
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
					EntityFallingBlockExtended.replaceFallingBlock(world, pos, state);
					NBTTagCompound nbt;
					tile.writeToNBT(nbt = new NBTTagCompound());
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

		public static World world(int dimID)
		{
			return handlerGatway.worldInstance(dimID);
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
			return world.setBlockState(pos, world.getBlockState(pos).withProperty(property, value), updateFlag);
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

		public static boolean isAirNearby(World world, BlockPos pos, boolean ignoreUnloadChunk)
		{
			return (!ignoreUnloadChunk || world.isAreaLoaded(pos, 1)) && (
					world.isAirBlock(pos.up())   ||
					world.isAirBlock(pos.down()) ||
					world.isAirBlock(pos.west()) ||
					world.isAirBlock(pos.east()) ||
					world.isAirBlock(pos.north())||
					world.isAirBlock(pos.south()));
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
					(world.canBlockSeeSky(pos) && isRainingAtBiome(world.getBiomeGenForCoords(pos), world, pos)) ||
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

		/**
		 * Used by ASM.
		 * @param world
		 * @param pos
		 * @return
		 */
		public static boolean isRainingAtBiome(Biome biome, World world, BlockPos pos)
		{
			if(biome instanceof IBiomeExtended)
				return ((IBiomeExtended) biome).canRainingAt(world, pos);
			return biome.canRain();
		}
		
		/**
		 * Used by ASM.
		 * @param oldBiome
		 * @param pos
		 * @param provider
		 * @return
		 */
		public static Biome regetBiome(int saveID, BlockPos pos, BiomeProvider provider)
		{
			if(provider instanceof IBiomeRegetter)
				return ((IBiomeRegetter) provider).getBiome(saveID, pos);
			return Biome.getBiome(saveID);
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

		public static RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids)
		{
			float f = playerIn.rotationPitch;
			float f1 = playerIn.rotationYaw;
			double d0 = playerIn.posX;
			double d1 = playerIn.posY + playerIn.getEyeHeight();
			double d2 = playerIn.posZ;
			Vec3d vec3d = new Vec3d(d0, d1, d2);
			float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
			float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
			float f4 = -MathHelper.cos(-f * 0.017453292F);
			float f5 = MathHelper.sin(-f * 0.017453292F);
			float f6 = f3 * f4;
			float f7 = f2 * f4;
			double d3 = 5.0D;
			if (playerIn instanceof net.minecraft.entity.player.EntityPlayerMP)
			{
				d3 = ((net.minecraft.entity.player.EntityPlayerMP)playerIn).interactionManager.getBlockReachDistance();
			}
			Vec3d vec3d1 = vec3d.addVector(f6 * d3, f5 * d3, f7 * d3);
			return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
		}
	}
	
	public static class TileEntities
	{
		public static boolean onTileActivatedGeneral(EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
				Direction facing, float hitX, float hitY, float hitZ, TileEntity tile)
		{
			if(tile == null) return false;
			if(tile instanceof TEBase && !((TEBase) tile).isInitialized())
				return false;
			EnumFacing facing2 = facing.of();
			if(tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing2))
			{
				if(heldItem != null && heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
				{
					IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing2);
					IFluidHandler handler2 = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
					FluidStack input;
					FluidStack output;
					int amt;
					if((output = handler2.drain(Integer.MAX_VALUE, false)) != null)
					{
						if((amt = handler.fill(output, true)) != 0)
						{
							input = output.copy();
							input.amount = amt;
							handler2.drain(input, true);
							return true;
						}
					}
					else if((output = handler.drain(Integer.MAX_VALUE, false)) != null)
					{
						if((amt = handler2.fill(output, true)) != 0)
						{
							input = output.copy();
							input.amount = amt;
							handler.drain(input, true);
							return true;
						}
					}
				}
			}
			if(tile.hasCapability(Capabilities.ITEM_HANDLER_IO, facing2))
			{
				IItemHandlerIO handler = tile.getCapability(Capabilities.ITEM_HANDLER_IO, facing2);
				if(heldItem != null && heldItem.hasCapability(Capabilities.ITEM_HANDLER_IO, null))
				{
					IItemHandlerIO handler2 = heldItem.getCapability(Capabilities.ITEM_HANDLER_IO, null);
					if(handler2.canExtractItem() && handler.canInsertItem())
					{
						ItemStack stack = handler2.extractItem(Integer.MAX_VALUE, facing, true);
						if(stack != null)
						{
							int amt = handler.tryInsertItem(stack, facing, false);
							if(amt > 0)
							{
								handler2.extractItem(amt, facing, false);
							}
						}
					}
					if(handler2.canInsertItem() && handler.canExtractItem())
					{
						ItemStack stack = handler.extractItem(Integer.MAX_VALUE, facing, true);
						if(stack != null)
						{
							int amt = handler2.tryInsertItem(stack, facing, false);
							if(amt > 0)
							{
								handler.extractItem(amt, facing, false);
							}
						}
					}
				}
				else if(heldItem == null)
				{
					if(handler.canExtractItem())
					{
						ItemStack stack = handler.extractItem(Integer.MAX_VALUE, facing, false);
						if(stack != null)
						{
							playerIn.setHeldItem(hand, stack);
							return true;
						}
					}
				}
				else
				{
					if(handler.canExtractItem())
					{
						if(heldItem.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
						{
							IItemHandler handler2 = heldItem.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
							ItemStack stack = handler.extractItem(Integer.MAX_VALUE, facing, true);
							if(stack != null)
							{
								ItemStack stack2 = stack;
								int[] puted = new int[handler2.getSlots()];
								int point = 0;
								for(int i = 0; i < handler2.getSlots(); ++i)
								{
									if(handler2.getStackInSlot(i) == null)
									{
										if(handler2.insertItem(i, stack2, true) == null)
										{
											stack2 = handler.extractItem(stack.stackSize, facing, false);
											handler2.insertItem(i, stack, false);
											return true;
										}
										else
										{
											stack2 = stack;
											puted[point ++] = i + 1;
										}
									}
								}
								for(int i = 0; i < handler2.getSlots(); ++i)
								{
									if(!stack.isItemEqual(handler2.getStackInSlot(i)))
									{
										continue;
									}
									if((stack2 = handler2.insertItem(i, stack2, true)) == null)
									{
										break;
									}
									puted[point ++] = i + 1;
								}
								if(stack2 != null)
								{
									stack = handler.extractItem(stack.stackSize - stack2.stackSize, facing, false);
								}
								stack2 = stack;
								for(int i : puted)
								{
									if(i == 0)
									{
										break;
									}
									stack2 = handler2.insertItem(i, stack2, false);
								}
								return true;
							}
						}
						ItemStack stack = handler.extractItem(heldItem.getMaxStackSize() - heldItem.stackSize, facing, true);
						if(stack != null && stack.isItemEqual(heldItem))
						{
							heldItem.stackSize += stack.stackSize;
							handler.extractItem(stack.stackSize, facing, false);
							return true;
						}
					}
					if(handler.canInsertItem())
					{
						int size = handler.tryInsertItem(heldItem.copy(), facing, false);
						if(size > 0)
						{
							heldItem.stackSize -= size;
							return true;
						}
					}
				}
			}
			if(heldItem != null && heldItem.getItem() instanceof ITool &&
					tile instanceof IToolableTile)
			{
				ITool tool = (ITool) heldItem.getItem();
				ActionResult<Float> result;
				for(EnumToolType toolType : tool.getToolTypes(heldItem))
				{
					if((result = ((IToolableTile) tile).onToolClick(playerIn, toolType, heldItem, facing, hitX, hitY, hitZ)).getType() != EnumActionResult.PASS)
					{
						tool.onToolUse(heldItem, toolType, result.getResult());
						return true;
					}
				}
			}
			return false;
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
				{
					stack.setTagCompound(new NBTTagCompound());
					return stack.getTagCompound();
				}
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

	public static class Players
	{
		public static EntityPlayer player()
		{
			return handlerGatway.playerInstance();
		}
	}

	@SideOnly(Side.CLIENT)
	public static class Client
	{
		private static final IBlockColor BIOME_COLOR =
				(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) ->
		{
			if(worldIn == null || pos == null) return -1;
			Biome biome = worldIn.getBiomeGenForCoords(pos);
			switch (tintIndex)
			{
			case 0 : return biome.getGrassColorAtPos(pos);
			case 1 : return biome.getFoliageColorAtPos(pos);
			case 2 : return biome.getWaterColor();
			default: return -1;
			}
		};

		public static boolean shouldRenderBetterLeaves()
		{
			return Blocks.LEAVES.getBlockLayer() == BlockRenderLayer.CUTOUT_MIPPED;
		}

		public static void registerModel(Block block, int meta, String modid, String path)
		{
			registerModel(Item.getItemFromBlock(block), meta, modid, path);
		}

		public static void registerModel(Item item, int meta, String modid, String path)
		{
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modid, path));
		}
		
		private static FontRenderExtend render;

		public static FontRenderer getFontRender()
		{
			if(render == null)
			{
				render = new FontRenderExtend();
			}
			return render;
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

		public void setModelLocate(Item item, int meta, String modid, String name)
		{

		}
		
		public void registerBiomeColorMultiplier(Block...block)
		{
			
		}
	}

	@SideOnly(Side.CLIENT)
	public static class ClientHandler extends CommonHandler
	{
		static final IItemColor ITEMBLOCK_COLOR =
				(ItemStack stack, int tintIndex) ->
		{
			Block block = Block.getBlockFromItem(stack.getItem());
			return Minecraft.getMinecraft().getBlockColors().colorMultiplier(block.getStateFromMeta(stack.getMetadata()), null, null, tintIndex);
		};
		static final IBlockColor BIOME_COLOR =
				(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
		{
			boolean flag = worldIn == null || pos == null;
			Biome biome = flag ? null : worldIn.getBiomeGenForCoords(pos);
			switch(tintIndex)
			{
			case 0 : return flag ? ColorizerGrass.getGrassColor(0.7F, 0.7F) :
				BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
			case 1 : return flag ? ColorizerFoliage.getFoliageColorBasic() :
				BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
			case 2 : return flag ? -1 : BiomeColorHelper.getWaterColorAtPos(worldIn, pos);
			default: return -1;
			}
		};

		@Override
		public World worldInstance(int id)
		{
			if (U.Sides.isSimulating())
				return super.worldInstance(id);
			else
			{
				World world = Minecraft.getMinecraft().theWorld;
				return world == null ? null : world.provider.getDimension() != id ? null : world;
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
		
		@Override
		public void setModelLocate(Item item, int meta, String modid, String name)
		{
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modid + ":" + name, null));
		}
		
		@Override
		public void registerBiomeColorMultiplier(Block... block)
		{
			FarCoreSetup.proxy.registerColorMultiplier(BIOME_COLOR, block);
			FarCoreSetup.proxy.registerColorMultiplier(ITEMBLOCK_COLOR, block);
		}
	}
}