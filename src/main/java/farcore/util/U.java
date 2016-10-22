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
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import farcore.FarCore;
import farcore.FarCoreSetup.ClientProxy;
import farcore.data.Capabilities;
import farcore.data.Config;
import farcore.data.EnumToolType;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.block.IToolableBlock;
import farcore.lib.collection.Stack;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.inv.IBasicInventory;
import farcore.lib.item.ITool;
import farcore.lib.model.block.ICustomItemModelSelector;
import farcore.lib.model.block.ModelFluidBlock;
import farcore.lib.model.block.StateMapperExt;
import farcore.lib.nbt.NBTTagCompoundEmpty;
import farcore.lib.net.world.PacketBreakBlock;
import farcore.lib.oredict.OreDictExt;
import farcore.lib.render.FontRenderExtend;
import farcore.lib.render.IProgressBarStyle;
import farcore.lib.render.ParticleDiggingExt;
import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.ArrayStack;
import farcore.lib.stack.BaseStack;
import farcore.lib.stack.OreStack;
import farcore.lib.tile.IItemHandlerIO;
import farcore.lib.tile.IToolableTile;
import farcore.lib.tile.TEBase;
import farcore.lib.util.Direction;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.IRenderRegister;
import farcore.lib.util.LanguageManager;
import farcore.lib.world.IBiomeExtended;
import farcore.lib.world.IBiomeRegetter;
import farcore.lib.world.ICoord;
import farcore.lib.world.IObjectInWorld;
import farcore.util.runnable.ThreadLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
			for(int i = 0; i < ret.length; ++i)
			{
				ret[i] = cast(integers[i]);
			}
			return ret;
		}

		public static float cast(Float f)
		{
			return f == null ? 0 : f.floatValue();
		}

		public static int cast(Integer integer)
		{
			return integer == null ? 0 : integer.intValue();
		}

		public static short cast(Short short1)
		{
			return short1 == null ? 0 : short1.shortValue();
		}

		public static int castPositive(byte val)
		{
			return (val & 0xFF);
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

		public static <K, V> void putAll(Map<? super K, ? super V> map, Collection<? extends K> collection, Function<? super K, ? extends V> function)
		{
			for(K key : collection)
			{
				map.put(key, function.apply(key));
			}
		}

		public static <K, V> void putAll(Map<? super K, ? super V> map, Collection<? extends K> collection, V constant)
		{
			putAll(map, collection, (K key) -> constant);
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
				put(map, key, Arrays.<V>asList(values));
			}
		}

		public static <K, V> void put(Map<K, List<V>> map, K key, Collection<? extends V> values)
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
			List<V> list = map.get(key);
			return list != null ? list.remove(value) : false;
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
		
		public static boolean inRange(double max, double min, double target)
		{
			return target <= max && target >= min;
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
		
		public static String translateByI18n(String unlocal, Object...parameters)
		{
			return handlerGatway.translateToLocalByI18n(unlocal, parameters);
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

		public static double[][] gaussianL(int size, double sigma)
		{
			int size1 = size * 2 + 1;
			double t = 0D;
			double[][] ret = new double[size1][size1];
			double s2 = sigma * sigma;
			for(int i = 0; i < size1; ++i)
			{
				for(int j = 0; j < size1; ++j)
				{
					int i1 = i - size - 1;
					int j1 = j - size - 1;
					t += (ret[i][j] = Math.exp(- (i1 * i1 + j1 * j1) / (2 * s2)));
				}
			}
			for(int i = 0; i < size1; ++i)
			{
				for(int j = 0; j < size1; ++j)
				{
					ret[i][j] /= t;
				}
			}
			return ret;
		}

		public static float[][] gaussianLf(int size, float sigma)
		{
			int size1 = size * 2 + 1;
			float t = 0F;
			float[][] ret = new float[size1][size1];
			double s2 = sigma * sigma;
			for(int i = 0; i < size1; ++i)
			{
				for(int j = 0; j < size1; ++j)
				{
					int i1 = i - size - 1;
					int j1 = j - size - 1;
					t += (ret[i][j] = (float) Math.exp(- (i1 * i1 + j1 * j1) / (2 * s2)));
				}
			}
			for(int i = 0; i < size1; ++i)
			{
				for(int j = 0; j < size1; ++j)
				{
					ret[i][j] /= t;
				}
			}
			return ret;
		}
		
		public static double mod(double a, double b)
		{
			double v;
			return (v = a % b) > 0 ? v : v + b;
		}
		
		public static float mod(float a, float b)
		{
			float v;
			return (v = a % b) > 0 ? v : v + b;
		}
		
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
		
		public static void registerClientRegister(Object object)
		{
			handlerGatway.registerRender(object);
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
			handlerGatway.registerRender(block);
		}

		public static void registerItem(Item item, String name)
		{
			registerItem(item, getActiveModID(), name);
		}
		
		public static void registerItem(Item item, String modid, String name)
		{
			GameRegistry.register(item.setRegistryName(modid, name));
			handlerGatway.registerRender(item);
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
		
		public static void registerItemModel(Item item, int meta, String modid, String locate, String type)
		{
			handlerGatway.setModelLocate(item, meta, modid, locate, type);
		}

		public static void registerBiomeColorMultiplier(Block...block)
		{
			handlerGatway.registerBiomeColorMultiplier(block);
		}

		public static void registerCompactModel(Block block, boolean splitFile, String modid, String path, IProperty property, IProperty...properties)
		{
			handlerGatway.registerCompactModel(splitFile, block, modid, path, property, properties);
		}

		@SideOnly(Side.CLIENT)
		public static void registerColorMultiplier(IBlockColor color, Block...block)
		{
			FarCore.proxy.registerColorMultiplier(color, block);
		}

		@SideOnly(Side.CLIENT)
		public static void registerColorMultiplier(IItemColor color, Item...item)
		{
			FarCore.proxy.registerColorMultiplier(color, item);
		}
		
		@SideOnly(Side.CLIENT)
		public static void registerFluid(BlockFluidBase block)
		{
			registerCustomItemModelSelector(Item.getItemFromBlock(block), ModelFluidBlock.Selector.instance);
			ModelLoader.setCustomStateMapper(block, ModelFluidBlock.Selector.instance);
		}

		@SideOnly(Side.CLIENT)
		public static void registerCustomItemModelSelector(Block item, ICustomItemModelSelector selector)
		{
			registerCustomItemModelSelector(Item.getItemFromBlock(item), selector);
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

		public static void registerValid(String name, Function<ItemStack, Boolean> function, ItemStack...instances)
		{
			OreDictExt.registerOreFunction(name, function, instances);
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
	
	public static class Lights
	{
		public static int get256Light(World world, BlockPos pos, EnumSkyBlock type)
		{
			return world.getLightFor(type, pos) << 4;
		}
		
		public static int blend4Light(int light1, int light2, int light3, int light4)
		{
			return (light1 + light2 + light3 + light4) >> 2 & 0xFF;
		}
		
		public static int blend2Light(int light1, int light2)
		{
			return (light1 + light2) >> 1 & 0xFF;
		}

		public static int blend4MixLight(int light1, int light2, int light3, int light4)
		{
			return (light1 + light2 + light3 + light4) >> 2 & 0xFF00FF;
		}
		
		public static int blend2MixLight(int light1, int light2)
		{
			return (light1 + light2) >> 1 & 0xFF00FF;
		}

		public static int mixSkyBlockLight(int sky, int block)
		{
			return sky << 16 | block;
		}

		@SideOnly(Side.CLIENT)
		public static VertexBuffer lightmap(VertexBuffer buffer, int light)
		{
			return buffer.lightmap(light >> 16 & 0xFF, light & 0xFF);
		}
	}

	public static class Worlds
	{
		private static final int[][] rotateFix = {
				{3, 2, 5, 4},
				{1, 0, 5, 4},
				{1, 0, 3, 2}};

		public static void breakBlockWithoutSource(World world, BlockPos pos, boolean harvestBlock)
		{
			if(!world.isRemote) //This method have not effect in client world, it will send a packet to client.
			{
				if(!world.isAreaLoaded(pos, 64))
				{
					world.setBlockToAir(pos);
				}
				if(world.isAirBlock(pos)) return;
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				block.breakBlock(world, pos, state);
				FarCore.network.sendToNearBy(new PacketBreakBlock(world, pos), world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
				TileEntity tile = world.getTileEntity(pos);
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 5);
				if(harvestBlock)
				{
					block.dropBlockAsItem(world, pos, state, 0);
				}
			}
		}

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
					!((ISmartFallableBlock) state.getBlock()).canFallingBlockStay(world, pos, state.getActualState(world, pos)) :
						EntityFallingBlockExtended.canFallAt(world, pos, state))
				return fallBlock(world, pos, state);
			return false;
		}

		public static boolean fallBlock(World world, BlockPos pos, IBlockState state)
		{
			return fallBlock(world, pos, pos, state);
		}
		
		public static boolean fallBlock(World world, BlockPos pos, BlockPos dropPos, IBlockState state)
		{
			if(!BlockFalling.fallInstantly && world.isAreaLoaded(pos, 32))
			{
				world.setBlockToAir(pos);
				return world.isRemote || world.spawnEntityInWorld(new EntityFallingBlockExtended(world, pos, dropPos, state, world.getTileEntity(pos)));
			}
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
					EntityFallingBlockExtended.replaceFallingBlock(world, pos, state, height);
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
			if(heldItem != null && heldItem.getItem() instanceof ITool &&
					tile instanceof IToolableTile)
			{
				ITool tool = (ITool) heldItem.getItem();
				ActionResult<Float> result;
				for(EnumToolType toolType : tool.getToolTypes(heldItem))
				{
					if((result = ((IToolableTile) tile).onToolClick(playerIn, toolType, heldItem, facing, hitX, hitY, hitZ)).getType() != EnumActionResult.PASS)
					{
						tool.onToolUse(playerIn, heldItem, toolType, result.getResult());
						return true;
					}
				}
			}
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
			return false;
		}

		public static boolean matchOutput(AbstractStack output, ItemStack stackInSlot)
		{
			return matchOutput(output, stackInSlot, 64);
		}

		public static boolean matchOutput(AbstractStack output, ItemStack stackInSlot, int stackLimit)
		{
			return output == null || stackInSlot == null ? true :
				output.similar(stackInSlot) && stackInSlot.stackSize + output.size(stackInSlot) <= Math.min(stackLimit, stackInSlot.getMaxStackSize());
		}
		
		public static void insertStack(AbstractStack stack, IBasicInventory inventory, int idx)
		{
			if(inventory.getStackInSlot(idx) == null)
			{
				inventory.setInventorySlotContents(idx, stack.instance());
			}
			else
			{
				ItemStack stack2 = inventory.getStackInSlot(idx);
				stack2.stackSize += stack.size(stack2);
			}
		}
		
		public static void insertStack(AbstractStack stack, IInventory inventory, int idx)
		{
			if(inventory.getStackInSlot(idx) == null)
			{
				inventory.setInventorySlotContents(idx, stack.instance());
			}
			else
			{
				ItemStack stack2 = inventory.getStackInSlot(idx);
				stack2.stackSize += stack.size(stack2);
			}
		}
	}

	public static class ItemStacks
	{
		/**
		 * Some item may override item meta get method,
		 * this method will give a stack with item select
		 * meta.
		 * @param item
		 * @param meta
		 * @return
		 */
		public static ItemStack stack(Item item, int meta)
		{
			ItemStack stack = new ItemStack(item, 1);
			stack.setItemDamage(meta);
			return stack;
		}

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

		public static AbstractStack sizeOf(AbstractStack stack, int size)
		{
			return size <= 0 ? null : stack instanceof BaseStack ?
					BaseStack.sizeOf((BaseStack) stack, size) :
						stack instanceof ArrayStack ?
								ArrayStack.sizeOf((ArrayStack) stack, size) :
									stack instanceof OreStack ?
											OreStack.sizeOf((OreStack) stack, size) : null;
		}

		public static boolean isItemAndTagEqual(ItemStack stack1, ItemStack stack2)
		{
			return stack1 == null || stack2 == null ?
					stack1 == stack2 :
						stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
		}

		public static boolean areTagEqual(NBTTagCompound nbt1, NBTTagCompound nbt2)
		{
			return nbt1 == null || nbt2 == null ? nbt1 == nbt2 : nbt1.equals(nbt2);
		}
		
		/**
		 * This method should called by item onItemUse.
		 * @param stack
		 * @param player
		 * @param world
		 * @param pos
		 * @param side
		 * @param hitX
		 * @param hitY
		 * @param hitZ
		 * @return
		 */
		public static EnumActionResult onUseOnBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
		{
			List<EnumToolType> toolTypes;
			if(stack == null)
			{
				toolTypes = EnumToolType.HAND_USABLE_TOOL;
			}
			else if(stack.getItem() instanceof ITool)
			{
				toolTypes = ((ITool) stack.getItem()).getToolTypes(stack);
			}
			else return EnumActionResult.PASS;
			Direction direction = Direction.of(side);
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() instanceof IToolableBlock)
			{
				IToolableBlock block = (IToolableBlock) state.getBlock();
				for(EnumToolType tool : toolTypes)
				{
					ActionResult<Float> result = block.onToolClick(player, tool, stack, world, pos, direction, hitX, hitY, hitZ);
					if(result.getType() != EnumActionResult.PASS)
					{
						((ITool) stack.getItem()).onToolUse(player, stack, tool, L.cast(result.getResult()));
						return result.getType();
					}
				}
			}
			return EnumActionResult.PASS;
		}
		
		public static List<EnumToolType> getCurrentToolType(ItemStack stack)
		{
			if(stack == null)
				return EnumToolType.HAND_USABLE_TOOL;
			if(stack.getItem() instanceof ITool)
				return ((ITool) stack.getItem()).getToolTypes(stack);
			List<EnumToolType> list = new ArrayList();
			for(EnumToolType toolType : EnumToolType.values())
			{
				if(toolType.match(stack))
				{
					list.add(toolType);
				}
			}
			return list;
		}
		
		public static int getToolLevel(ItemStack stack, EnumToolType toolType)
		{
			if(stack == null)
				return -1;
			if(stack.getItem() instanceof ITool)
				return ((ITool) stack.getItem()).getToolLevel(stack, toolType);
			return toolType.match(stack) ? 1 : -1;
		}
	}

	public static class Players
	{
		public static EntityPlayer player()
		{
			return handlerGatway.playerInstance();
		}
		
		public static List<EnumToolType> getCurrentToolType(EntityPlayer player)
		{
			ItemStack stack = player.getHeldItemMainhand();
			if(stack == null)
			{
				stack = player.getHeldItemOffhand();
			}
			if(stack == null)
				return EnumToolType.HAND_USABLE_TOOL;
			if(stack.getItem() instanceof ITool)
				return ((ITool) stack.getItem()).getToolTypes(stack);
			List<EnumToolType> list = new ArrayList();
			for(EnumToolType toolType : EnumToolType.values())
			{
				if(toolType.match(stack))
				{
					list.add(toolType);
				}
			}
			return list;
		}
		
		public static boolean matchCurrentToolType(EntityPlayer player, EnumToolType...types)
		{
			ItemStack stack = player.getHeldItemMainhand();
			if(stack == null)
			{
				stack = player.getHeldItemOffhand();
			}
			if(stack == null)
			{
				for(EnumToolType type : types)
					if(type == EnumToolType.hand)
						return true;
				return false;
			}
			List<EnumToolType> list;
			if(stack.getItem() instanceof ITool)
			{
				list = ((ITool) stack.getItem()).getToolTypes(stack);
				for(EnumToolType type : types)
				{
					if(list.contains(type)) return true;
				}
				return false;
			}
			for(EnumToolType type : types)
			{
				if(type.match(stack))
					return true;
			}
			return false;
			
		}

		public static void destoryPlayerCurrentItem(EntityPlayer player)
		{
			if(player == null) return;
			if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().stackSize <= 0)
			{
				player.renderBrokenItemStack(player.getHeldItemMainhand());
				player.setHeldItem(EnumHand.MAIN_HAND, null);
			}
			if(player.getHeldItemOffhand() != null && player.getHeldItemOffhand().stackSize <= 0)
			{
				player.renderBrokenItemStack(player.getHeldItemOffhand());
				player.setHeldItem(EnumHand.OFF_HAND, null);
			}
		}
		
		public static Entity moveEntityToAnotherDim(Entity entity, int dim, double x, double y, double z)
		{
			WorldServer targetWorld = DimensionManager.getWorld(dim);
			if(targetWorld != null)
				return moveEntityToAnotherDim(entity, dim, x, y, z, targetWorld.getDefaultTeleporter());
			return null;
		}
		
		public static Entity moveEntityToAnotherDim(Entity entity, int dim, double x, double y, double z, Teleporter teleporter)
		{
			WorldServer targetWorld = DimensionManager.getWorld(dim);
			WorldServer originalWorld = DimensionManager.getWorld(entity.worldObj.provider.getDimension());
			if(targetWorld != null && originalWorld != null && targetWorld != originalWorld)
			{
				if(entity.isRiding())
				{
					entity.dismountRidingEntity();
				}
				if(entity.isBeingRidden())
				{
					entity.removePassengers();
				}
				if (entity instanceof EntityPlayerMP)
				{
					EntityPlayerMP player = (EntityPlayerMP) entity;
					player.mcServer.getPlayerList().transferPlayerToDimension(player, dim, teleporter);
					player.connection.sendPacket(new SPacketEffect(1032, BlockPos.ORIGIN, 0, false));
					player.setLocationAndAngles(x, y, z, player.rotationYaw, player.rotationPitch);
					return player;
				}
				else
				{
					Entity newEntity = entity.changeDimension(dim);
					if (newEntity != null)
					{
						entity.setPosition(x + 0.5D, y + 0.5D, z + 0.5D);
					}
					return newEntity;
				}
			}
			return null;
		}
	}
	
	public static class Server
	{
		/**
		 * I don't know why this method should use in server.
		 * @param world
		 * @param pos
		 * @param state
		 * @param entity
		 * @param numberOfParticles
		 */
		public static void addBlockLandingEffects(World world, BlockPos pos, IBlockState state,
				EntityLivingBase entity, int numberOfParticles)
		{
			((WorldServer) world).spawnParticle(EnumParticleTypes.BLOCK_DUST, entity.posX, entity.posY, entity.posZ, numberOfParticles, 0.0, 0.0, 0.0, 0.15, new int[] {Block.getStateId(state)});
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
		
		public static void addBlockHitEffect(World world, Random rand, IBlockState state, EnumFacing side, BlockPos pos, ParticleManager manager)
		{
			if (state.getRenderType() != EnumBlockRenderType.INVISIBLE)
			{
				int i = pos.getX();
				int j = pos.getY();
				int k = pos.getZ();
				float f = 0.1F;
				AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
				double d0 = i + rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
				double d1 = j + rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
				double d2 = k + rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;
				if (side == EnumFacing.DOWN)
				{
					d1 = j + axisalignedbb.minY - 0.10000000149011612D;
				}
				if (side == EnumFacing.UP)
				{
					d1 = j + axisalignedbb.maxY + 0.10000000149011612D;
				}
				if (side == EnumFacing.NORTH)
				{
					d2 = k + axisalignedbb.minZ - 0.10000000149011612D;
				}
				if (side == EnumFacing.SOUTH)
				{
					d2 = k + axisalignedbb.maxZ + 0.10000000149011612D;
				}
				if (side == EnumFacing.WEST)
				{
					d0 = i + axisalignedbb.minX - 0.10000000149011612D;
				}
				if (side == EnumFacing.EAST)
				{
					d0 = i + axisalignedbb.maxX + 0.10000000149011612D;
				}
				manager.addEffect((new ParticleDiggingExt(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state)).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
			}
		}
		
		public static void addBlockHitEffect(World world, Random rand, IBlockState state, EnumFacing side, BlockPos pos, ParticleManager manager, Object icon)
		{
			if (state.getRenderType() != EnumBlockRenderType.INVISIBLE)
			{
				int i = pos.getX();
				int j = pos.getY();
				int k = pos.getZ();
				float f = 0.1F;
				AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
				double d0 = i + rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
				double d1 = j + rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
				double d2 = k + rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;
				if (side == EnumFacing.DOWN)
				{
					d1 = j + axisalignedbb.minY - 0.10000000149011612D;
				}
				if (side == EnumFacing.UP)
				{
					d1 = j + axisalignedbb.maxY + 0.10000000149011612D;
				}
				if (side == EnumFacing.NORTH)
				{
					d2 = k + axisalignedbb.minZ - 0.10000000149011612D;
				}
				if (side == EnumFacing.SOUTH)
				{
					d2 = k + axisalignedbb.maxZ + 0.10000000149011612D;
				}
				if (side == EnumFacing.WEST)
				{
					d0 = i + axisalignedbb.minX - 0.10000000149011612D;
				}
				if (side == EnumFacing.EAST)
				{
					d0 = i + axisalignedbb.maxX + 0.10000000149011612D;
				}
				manager.addEffect((new ParticleDiggingExt(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state, icon)).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
			}
		}
		
		public static void addBlockDestroyEffects(World world, BlockPos pos, IBlockState state, ParticleManager manager)
		{
			int i = 4;
			for (int j = 0; j < 4; ++j)
			{
				for (int k = 0; k < 4; ++k)
				{
					for (int l = 0; l < 4; ++l)
					{
						double d0 = pos.getX() + (j + 0.5D) / 4.0D;
						double d1 = pos.getY() + (k + 0.5D) / 4.0D;
						double d2 = pos.getZ() + (l + 0.5D) / 4.0D;
						manager.addEffect((new ParticleDiggingExt(world, d0, d1, d2, d0 - pos.getX() - 0.5D, d1 - pos.getY() - 0.5D, d2 - pos.getZ() - 0.5D, state)).setBlockPos(pos));
					}
				}
			}
		}
		
		public static void addBlockDestroyEffects(World world, BlockPos pos, IBlockState state, ParticleManager manager, Object icon)
		{
			int i = 4;
			for (int j = 0; j < 4; ++j)
			{
				for (int k = 0; k < 4; ++k)
				{
					for (int l = 0; l < 4; ++l)
					{
						double d0 = pos.getX() + (j + 0.5D) / 4.0D;
						double d1 = pos.getY() + (k + 0.5D) / 4.0D;
						double d2 = pos.getZ() + (l + 0.5D) / 4.0D;
						manager.addEffect((new ParticleDiggingExt(world, d0, d1, d2, d0 - pos.getX() - 0.5D, d1 - pos.getY() - 0.5D, d2 - pos.getZ() - 0.5D, state, icon)).setBlockPos(pos));
					}
				}
			}
		}

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

		private static final IProgressBarStyle STANDARD_PROGRESSBAR_STYLE = new IProgressBarStyle()
		{
			@Override
			public double getProgressScale(ItemStack stack)
			{
				double durability = stack.getItem().getDurabilityForDisplay(stack);
				return durability == 0 ? -1 : 1.0 - durability;
			}

			@Override
			public int[] getProgressColor(ItemStack stack, double progress)
			{
				int i = (int) (progress * 255);
				return new int[]{i, 255 - i, 0};
			}
		};
		
		public static void renderItemDurabilityBarInGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z)
		{
			renderItemDurabilityBarInGUI(render, fontRenderer, stack, x, z, 1, STANDARD_PROGRESSBAR_STYLE);
		}
		public static void renderItemDurabilityBarInGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z, int off, IProgressBarStyle barStyle)
		{
			double health = barStyle.getProgressScale(stack);
			if(health < 0)
				return;
			if(health > 1)
			{
				health = 1;
			}
			int j = (int) Math.round(health * 13.0D);
			int[] color = barStyle.getProgressColor(stack, health);
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			draw(vertexbuffer, x + 2, z + 15 - 2 * off, 13, 2, 0, 0, 0, 255);
			draw(vertexbuffer, x + 2, z + 15 - 2 * off, 12, 1, color[0] / 4, color[1] / 4, color[2] / 4, 255);
			draw(vertexbuffer, x + 2, z + 15 - 2 * off,  j, 1, color[0], color[1], color[2], 255);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}

		public static void renderItemCooldownInGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z)
		{
			EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
			float f = entityplayersp == null ? 0.0F : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());

			if (f > 0.0F)
			{
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableTexture2D();
				Tessellator tessellator1 = Tessellator.getInstance();
				VertexBuffer vertexbuffer1 = tessellator1.getBuffer();
				draw(vertexbuffer1, x, z + MathHelper.floor_float(16.0F * (1.0F - f)), 16, MathHelper.ceiling_float_int(16.0F * f), 255, 255, 255, 127);
				GlStateManager.enableTexture2D();
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
		}
		
		public static void renderItemSubscirptInGUI(RenderItem render, FontRenderer fontRenderer, ItemStack stack, int x, int z, String text)
		{
			if (text != null)
			{
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableBlend();
				fontRenderer.drawStringWithShadow(text, x + 19 - 2 - fontRenderer.getStringWidth(text), z + 6 + 3, 16777215);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
				GlStateManager.enableBlend();
			}
		}

		/**
		 * Draw with the WorldRenderer
		 */
		private static void draw(VertexBuffer renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha)
		{
			renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			renderer.pos(x + 0, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
			renderer.pos(x + 0, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
			renderer.pos(x + width, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
			renderer.pos(x + width, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
			Tessellator.getInstance().draw();
		}
	}

	public static class CommonHandler
	{
		public World worldInstance(int id)
		{
			return DimensionManager.getWorld(id);
		}
		
		public void registerRender(Object object)
		{

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
		
		public <T extends Comparable<T>> void registerCompactModel(boolean splitFile, Block block, String modid, String path, IProperty<T> property,
				IProperty...properties)
		{

		}

		public void setModelLocate(Item item, int meta, String modid, String name, String type)
		{

		}

		public void setModelLocate(Item item, int meta, String modid, String name)
		{

		}
		
		public void registerBiomeColorMultiplier(Block...block)
		{
			
		}

		public String translateToLocalByI18n(String unlocal, Object...parameters)
		{
			return null;
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
		public void registerRender(Object object)
		{
			if(object instanceof IRenderRegister)
			{
				((ClientProxy) FarCore.proxy).addRenderRegisterListener((IRenderRegister) object);
			}
		}

		@Override
		public <T extends Comparable<T>> void registerCompactModel(boolean splitFile, Block block, String modid, String path, IProperty<T> property,
				IProperty...properties)
		{
			StateMapperExt mapper = new StateMapperExt(modid, path, splitFile ? property : null, properties);
			((ClientProxy) FarCore.proxy).registerCompactModel(mapper, block, property);
		}

		@Override
		public void setModelLocate(Item item, int meta, String modid, String name)
		{
			setModelLocate(item, meta, modid, name, null);
		}
		
		@Override
		public void setModelLocate(Item item, int meta, String modid, String name, String type)
		{
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modid + ":" + name, type));
		}
		
		@Override
		public void registerBiomeColorMultiplier(Block... block)
		{
			FarCore.proxy.registerColorMultiplier(BIOME_COLOR, block);
			FarCore.proxy.registerColorMultiplier(ITEMBLOCK_COLOR, block);
		}

		@Override
		public String translateToLocalByI18n(String unlocal, Object...parameters)
		{
			return I18n.format(unlocal, parameters);
		}
	}
}