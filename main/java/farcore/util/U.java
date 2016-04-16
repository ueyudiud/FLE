package farcore.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.energy.thermal.ThermalNet;
import farcore.entity.EntityFallingBlockExtended;
import farcore.enums.Direction;
import farcore.enums.EnumDamageResource;
import farcore.enums.UpdateType;
import farcore.interfaces.ICalendar;
import farcore.interfaces.ISmartFallableBlock;
import farcore.interfaces.ISmartHarvestBlock;
import farcore.interfaces.ISmartPlantableBlock;
import farcore.interfaces.ISmartSoildBlock;
import farcore.interfaces.item.IBreakSpeedItem;
import farcore.interfaces.item.ICustomDamageItem;
import farcore.lib.nbt.NBTTagCompoundEmpty;
import farcore.lib.net.PacketSound;
import farcore.lib.recipe.ToolDestoryDropRecipes;
import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.BaseStack;
import farcore.lib.stack.NBTPropertyStack;
import farcore.lib.stack.OreStack;
import farcore.lib.world.IWorldDatas;
import farcore.lib.world.WorldCfg;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class U
{
	public static class Lang
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
		
		public static <T> T[] cast(List<? extends T> list, Class<T> clazz)
		{
			T[] ret = (T[]) Array.newInstance(clazz, list.size());
			return list.toArray(ret);
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
			{
				if(checker.isTrue(target)) return true;
			}
			return false;
		}
		
		public static <T> Set<T> containSet(Collection<? extends T> collection, IDataChecker<T> checker)
		{
			if(collection == null || collection.isEmpty()) return ImmutableSet.of();
			Builder<T> builder = ImmutableSet.builder();
			for(T target : collection)
			{
				if(checker.isTrue(target))
				{
					builder.add(target);
				}
			}
			return builder.build();
		}
		
		public static <T> T randomSelect(T[] list, Random random)
		{
			return list == null || list.length == 0 ? null : 
				list.length == 1 ? list[0] :
				list[random.nextInt(list.length)];
		}

		public static String validate(String string)
		{
			if(string == null) return "";
			return string.trim();
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
		
		public static String validateOre(boolean upperFirst, String name)
		{
			String string = validate(name);
			String ret = "";
			boolean shouldUpperCase = upperFirst;
			for(char chr : string.toCharArray())
			{
				if(chr == '_' || chr == ' ' || 
						chr == '-' || chr == '$' || 
						chr == '@' || chr == ' ')
				{
					shouldUpperCase = true;
					continue;
				}
				else
				{
					if(shouldUpperCase)
					{
						ret += Character.toString(Character.toUpperCase(chr));
						shouldUpperCase = false;
					}
					else
					{
						ret += Character.toString(chr);
					}
				}
			}
			return ret;
		}

		public static String upcaseFirst(String name)
		{
			String s = validate(name);
			if(s.length() == 0) return s;
			char chr = name.charAt(0);
			String sub = name.substring(1);
			return Character.toString(Character.toUpperCase(chr)) + sub;
		}

		public static int min(int...values)
		{
			int ret = Integer.MAX_VALUE;
			for(int i : values)
			{
				if(i < ret) ret = i;
			}
			return ret;
		}
		
		public static int max(int...values)
		{
			int ret = Integer.MIN_VALUE;
			for(int i : values)
			{
				if(i > ret) ret = i;
			}
			return ret;
		}

		public static int range(int m1, int m2, int target)
		{
			int v;
			return target > (v = Math.max(m1, m2)) ? v :
				target < (v = Math.min(m1, m2)) ? v : target;
		}
	}

	public static class Reflect
	{
		private static final Map<String, Field> fieldCache = new HashMap();
		private static Field modifiersField;
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
		
		public static <T, F> void overrideField(Class<? extends T> clazz, List<String> field, F override, boolean isPrivate) throws Exception
		{
			overrideField(clazz, field, null, override, isPrivate);
		}
		public static <T, F> void overrideField(Class<? extends T> clazz, List<String> field, T target, F override, boolean isPrivate) throws Exception
		{
			boolean flag = false;
			List<Throwable> list = new ArrayList();
			for(String str : field)
			{
				try
				{
					if(fieldCache.containsKey(clazz.getName() + "|" + str))
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
			}
			if(!flag)
			{
				for(Throwable e : list) e.printStackTrace();
				throw new RuntimeException();
			}
		}
				
		public static <T, F> void overrideFinalField(Class<? extends T> clazz, List<String> field, F override, boolean isPrivate) throws Exception
		{
			overrideFinalField(clazz, field, null, override, isPrivate);
		}
		public static <T, F> void overrideFinalField(Class<? extends T> clazz, List<String> field, T target, F override, boolean isPrivate) throws Exception
		{
			boolean flag = false;
			List<Throwable> list = new ArrayList();
			for(String str : field)
			{
				try
				{
					initModifierField();
					if(fieldCache.containsKey(clazz.getName() + "|" + str))
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
			}
			if(!flag)
			{
				for(Throwable e : list) e.printStackTrace();
				throw new RuntimeException("FLE: fail to find and override field " + field.get(0));
			}
		}
		
		public static <T> Object getValue(Class<? extends T> clazz, List<String> field, T target)
		{
			for(String str : field)
			{
				try
				{
					if(fieldCache.containsKey(clazz.getName() + "|" + str))
					{
						return fieldCache.get(clazz.getName() + "|" + str).get(target);
					}
					Field tField = clazz.getDeclaredField(str);
					tField.setAccessible(true);
					fieldCache.put(clazz.getName() + "|" + str, tField);
					return tField.get(target);
				}
				catch(Throwable e)
				{
					continue;
				}
			}
			return null;
		}
		
		public static void resetReflectCache()
		{
			int size = fieldCache.size();
			fieldCache.clear();
			FleLog.getCoreLogger().debug("Cleared " + size + " of cached fieldes.");
		}
		
		public static Method getMethod(Class clazz, List<String> field, Class...classes)
		{
			for(String str : field)
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

	public static class Mod
	{
		public static String getActiveModID()
		{
			if(Loader.instance().activeModContainer() == null)
			{
				return "minecraft";
			}
			return Loader.instance().activeModContainer().getModId();
		}
		
		public static boolean isModLoaded(String name)
		{
			return Loader.isModLoaded(name);
		}
	}
	
	public static class Sound
	{		
		public static boolean displaySound(String soundName, int timeUntilNextSound, float soundStrength)
		{
			return displaySound(soundName, timeUntilNextSound, soundStrength, Worlds.player());
		}
		
		public static boolean displaySound(String soundName, int timeUntilNextSound, float soundStrength, EntityPlayer player)
		{
			if(!Sides.isClient() || player == null) return false;
			return displaySound(soundName, timeUntilNextSound, soundStrength, player.posX, player.posY, player.posZ);
		}
		
		public static boolean displaySound(String soundName, int timeUntilNextSound, float soundStrength, double x, double y, double z)
		{
			if(!Sides.isClient()) return false;
			return displaySound(soundName, timeUntilNextSound, soundStrength, 0.9F + (float) Math.random() * 0.2F, x, y, z);
		}

		public static boolean displaySound(String soundName, int timeUntilNextSound, float soundStrength, float soundModulation, double x, double y, double z)
		{
			if(!Sides.isClient()) return false;
			EntityPlayer player = Worlds.player();
			if ((player == null) || (!player.worldObj.isRemote) || (soundName == null))
			{
		        return false;
			}
			new ThreadedSound(player.worldObj, x, y, z, timeUntilNextSound, soundName, soundStrength, soundModulation).run();	
			return true;
		}
		
		public static void callDisplaySound(World world, String soundName, float soundStrength, float soundModulation, double x, double y, double z)
		{
			if(world == null || world.isRemote) return;
			FarCoreSetup.network.sendToNearBy(new PacketSound(x, y, z, soundName, soundStrength, soundModulation), world.provider.dimensionId, (int) x, (int) y, (int) z, soundStrength);
		}
		
		private static class ThreadedSound implements Runnable
		{
			private final World world;
			private final String name;
			private final double x, y, z;
			private final int time;
			private final float strength, modulation;

			public ThreadedSound(World world, double x, double y, double z, int timeUntilNextSound, String soundName,
					float soundStrength, float soundModulation)
			{
				this.world = world;
				this.name = soundName;
				this.x = x;
				this.y = y;
				this.z = z;
				this.time = timeUntilNextSound;
				this.strength = soundStrength;
				this.modulation = soundModulation;
			}

			@Override
			public void run()
			{
				try
				{
					world.playSound(x, y, z, name, strength, modulation, true);
				}
				catch(Throwable throwable){}
			}
		}
	}

	public static class Sides
	{
		public static boolean isClient()
		{
			return FMLCommonHandler.instance().getSide().isClient();
		}
		
		public static boolean isSimulating()
		{
			return FMLCommonHandler.instance().getEffectiveSide().isServer();
		}
	}
	
	public static class Worlds
	{
		@SidedProxy(modId = FarCore.ID, serverSide = "farcore.lib.world.WorldCfg", clientSide = "farcore.lib.world.WorldCfgClient")
		public static WorldCfg cfg;
		@SidedProxy(modId = FarCore.ID, serverSide = "farcore.lib.world.WorldDatasServer", clientSide = "farcore.lib.world.WorldDatasClient")
		public static IWorldDatas datas;
		
		public static World world(int dim)
		{
			return cfg.a(dim);
		}
		
		public static EntityPlayer player()
		{
			return cfg.b();
		}
		
		public static void setSmartMetadata(World world, int x, int y, int z, int meta, UpdateType updateType)
		{
			datas.setSmartMetadataWithNotify(world, x, y, z, meta, updateType.ordinal());
		}
		
		public static int getSmartMetadata(World world, int x, int y, int z)
		{
			return datas.getSmartMetadata(world, x, y, z);
		}
		
		public static boolean setBlock(World world, int x, int y, int z, Block block, UpdateType updateType)
		{
			return setBlock(world, x, y, z, block, 0, updateType);
		}
		
		public static boolean setBlock(World world, int x, int y, int z, Block block, int meta, UpdateType updateType)
		{
			return world.setBlock(x, y, z, block, meta, updateType.ordinal());
		}
		
		public static boolean setBlock(int dim, int x, int y, int z, Block block, int meta, UpdateType updateType)
		{
			World world = world(dim);
			if(world == null) return false;
			return setBlock(world, x, y, z, block, updateType);
		}
		
		public static TileEntity setTileEntity(World world, int x, int y, int z, TileEntity tile)
		{
			return setTileEntity(world, x, y, z, tile, true);
		}

		public static TileEntity setTileEntity(World world, int x, int y, int z, TileEntity tile, boolean update)
		{
			if(update)
			{
				world.setTileEntity(x, y, z, tile);
			}
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
		        {
					entity.mountEntity(null);
		        }
		        if (entity.riddenByEntity != null)
		        {
		        	entity.riddenByEntity.mountEntity(null);
		        }
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
		        {
		        	((EntityLivingBase)entity).setPositionAndUpdate(x, y, z);
		        } 
		        else
		        {
		        	entity.setPosition(x, y, z);
		        }
		        originalWorld.resetUpdateEntityTick();
		        targetWorld.resetUpdateEntityTick();
		        return true;
			}
			return false;
	    }

		public static boolean isCatchingRain(World world, int x, int y, int z)
		{
			if(world.isRaining())
			{
				return world.canBlockSeeTheSky(x, y, z);
			}
			return false;
		}

		public static void removeBlock(World world, int x, int y, int z)
		{
			world.removeTileEntity(x, y, z);
			world.setBlockToAir(x, y, z);
		}

		public static int getAirDensity(World world, int y)
		{
			if(world.provider.isHellWorld) return 30;
			else if(world.provider.dimensionId == 1) return -800;
			else return 0;
		}

		public static void spawnDropsInWorld(World world, int x, int y, int z, List<ItemStack> drop)
		{
			if(world.isRemote || drop == null) return;
			for(ItemStack stack : drop)
			{
				if(stack == null) continue;
	            float f = 0.7F;
	            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, stack.copy());
	            entityitem.delayBeforeCanPickup = 10;
	            world.spawnEntityInWorld(entityitem);
			}
		}

		public static ChunkCoordinates makeCoordinate(TileEntity tile)
		{
			return tile == null ? new ChunkCoordinates() :
				new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
		}
		
		public static float getEnviormentTemp(World world, int x, int y, int z)
		{
			return ThermalNet.getEnviormentTemp(world, x, y, z);
		}
		
		public static float getTemp(World world, int x, int y, int z)
		{
			return ThermalNet.getTemp(world, x, y, z, true);
		}
	
		public static boolean checkAndFallBlock(World world, int x, int y, int z, Block block)
		{
			if(world.isRemote) return false;
			if(block instanceof ISmartFallableBlock ?
					!((ISmartFallableBlock) block).canFallingBlockStay(world, x, y, z, world.getBlockMetadata(x, y, z)) :
						EntityFallingBlockExtended.canFallAt(world, x, y, z, block))
			{
				return fallBlock(world, x, y, z, block);
			}
			return false;
		}

		public static boolean fallBlock(World world, int x, int y, int z, Block block) 
		{
			if(!BlockFalling.fallInstantly && world.checkChunksExist(x - 32, y - 32, z - 32, x + 32, y + 32, z + 32))
			{
				return world.spawnEntityInWorld(new EntityFallingBlockExtended(world, x, y, z, block));
			}
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
				{
					((ISmartFallableBlock) block).onStartFalling(world, x, y, z);
				}
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
	}
	
	public static class Inventorys
	{
		public static ImmutableList<ItemStack> sizeOf(List<ItemStack> stacks, int size)
		{
			if(stacks == null || stacks.isEmpty()) return ImmutableList.of();
			ImmutableList.Builder builder = ImmutableList.builder();
			for(ItemStack stack : stacks)
			{
				if(stack != null)
				{
					ItemStack stack2 = stack.copy();
					stack2.stackSize = size;
					builder.add(valid(stack2.copy()));
				}
			}
			return builder.build();
		}

		public static AbstractStack sizeOf(AbstractStack stack, int size)
		{
			if(stack == null) return null;
			if(stack instanceof OreStack)
			{
				return OreStack.sizeOf((OreStack) stack, size);
			}
			else if(stack instanceof BaseStack)
			{
				return BaseStack.sizeOf((BaseStack) stack, size);
			}
			else if(stack instanceof NBTPropertyStack)
			{
				return NBTPropertyStack.sizeOf((NBTPropertyStack) stack, size);
			}
			return null;
		}

		public static void damage(ItemStack target, EntityLivingBase entity, int damage, EnumDamageResource resource)
		{
			damage(target, entity, damage, resource, entity != null);
		}
		public static void damage(ItemStack target, EntityLivingBase entity, int damage, EnumDamageResource resource, boolean isEntityCurrent)
		{
			if((entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode))
				return;
			if(target != null)
			{
				if(target.getItem() instanceof ICustomDamageItem)
				{
					ICustomDamageItem item = (ICustomDamageItem) target.getItem();
					item.damangeItem(target, damage, entity, resource);
					if(isEntityCurrent && target.stackSize <= 0)
					{
						if(entity instanceof EntityPlayer)
						{
							((EntityPlayer) entity).destroyCurrentEquippedItem();
						}
						else
						{
							entity.setCurrentItemOrArmor(0, null);
						}
					}
					return;
				}
				else
				{
					target.damageItem(damage, entity);
					if(isEntityCurrent && target.stackSize <= 0)
					{
						if(entity instanceof EntityPlayer)
						{
							((EntityPlayer) entity).destroyCurrentEquippedItem();
						}
						else
						{
							entity.setCurrentItemOrArmor(0, null);
						}
					}
					return;
				}
			}
		}
		
		public static int dosePlayerHas(EntityPlayer player, AbstractStack target)
		{
			if(target == null) return -1;
			AbstractStack stack = target;
			for(int i = 0; i < player.inventory.mainInventory.length; ++i)
			{
				if(target.contain(player.inventory.mainInventory[i]))
				{
					return i;
				}
			}
			return -1;
		}

		public static void givePlayer(EntityPlayer player, ItemStack target)
		{
			if(!player.inventory.addItemStackToInventory(target))
			{
				player.dropPlayerItemWithRandomChoice(target, false);
			}
		}

		public static void givePlayerToContainer(EntityPlayer player, ItemStack target)
		{
			if(player.inventory.getItemStack() == null)
			{
				player.inventory.setItemStack(target);
			}
			else if(!player.inventory.addItemStackToInventory(target))
			{
				player.dropPlayerItemWithRandomChoice(target, false);
			}
		}

		public static NBTTagCompound setupNBT(ItemStack stack, boolean create)
		{
			if(!stack.hasTagCompound())
			{
				if(create)
				{
					return stack.stackTagCompound = new NBTTagCompound();
				}
				return NBTTagCompoundEmpty.instance;
			}
			return stack.getTagCompound();
		}

		public static ItemStack valid(ItemStack stack)
		{
			if(stack == null || stack.stackSize <= 0)
			{
				return null;
			}
			if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			{
				stack = stack.copy();
				stack.setItemDamage(0);
			}
			return stack;
		}	
	}
	
	public static class Plants
	{
		public static boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, ISmartPlantableBlock plantable)
		{
			Block block = world.getBlock(x, y, z);
			if(block instanceof ISmartSoildBlock)
			{
				if(((ISmartSoildBlock) block).canSustainPlant(world, x, y, z, Direction.of(direction), plantable))
				{
					return true;
				}
			}
			if(plantable.useDefaultType())
			{
				if(block.canSustainPlant(world, x, y, z, direction, plantable))
				{
					return true;
				}
			}
			return false;
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
			if(U.Inventorys.valid(ore) == null) return;
			ItemStack register = ore.copy();
			register.stackSize = 1;
			OreDictionary.registerOre(name, ore);
		}
	}

	public static class Player
	{
		public static float getBaseDigspeed(EntityPlayer player, World world, int x, int y, int z, Block block, int meta)
		{
	        ItemStack stack = player.getCurrentEquippedItem();
	        float f;
	        if(stack == null)
	        {
	        	f = 1.0F;
	        }
	        else if(stack.getItem() instanceof IBreakSpeedItem)
	        {
	        	f = ((IBreakSpeedItem) stack.getItem()).getSpeed(stack, world, x, y, z, block, meta);
	        }
	        else
	        {
	        	f = stack.getItem().getDigSpeed(stack, block, meta);
	        }

	        if (f > 1.0F)
	        {
	            int i = EnchantmentHelper.getEfficiencyModifier(player);
	            if (i > 0 && stack != null)
	            {
	                float f1 = (float)(i * i + 1);
	                boolean canHarvest = U.Player.isToolEffective(player, world, x, y, z, block, meta, true);
	                if (!canHarvest && f <= 1.0F)
	                {
	                    f += f1 * 0.08F;
	                }
	                else
	                {
	                    f += f1;
	                }
	            }
	        }

	        if (player.isPotionActive(Potion.digSpeed))
	        {
	            f *= Math.pow(1.2, player.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1);
	        }

	        if (player.isPotionActive(Potion.digSlowdown))
	        {
	            f *= Math.pow(0.8, player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1);
	        }

	        if (player.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(player))
	        {
	            f /= 5.0F;
	        }

	        if (!player.onGround)
	        {
	            f /= 5.0F;
	        }
			return f;
		}

		
		public static boolean isToolEffective(EntityPlayer player, World world, int x, int y, int z, Block block, int meta, boolean checkHandler)
		{
			if(checkHandler && player.getCurrentEquippedItem() != null)
			{
				if(ToolDestoryDropRecipes.match(player.getCurrentEquippedItem(), world, x, y, z, block, meta) != null)
				{
					return true;
				}
				if(block instanceof ISmartHarvestBlock)
				{
					return ((ISmartHarvestBlock) block).canHarvestBlock(world, x, y, z, meta, player);
				}
			}
			if(player.getCurrentEquippedItem() != null)
			{
				if(player.getCurrentEquippedItem().getItem().canHarvestBlock(block, player.getCurrentEquippedItem()))
				{
					return true;
				}
			}
			return ForgeHooks.canHarvestBlock(block, player, meta);
		}
	}

	public static class Time
	{
		public static long getTime(World world)
		{
			return world.getWorldInfo().getWorldTime();
		}
		
		public static long getTotalDay(World world, ICalendar calendar)
		{
			return calendar.getTotalDayL(getTime(world));
		}
		
		public static long getTotalMonth(World world, ICalendar calendar)
		{
			return calendar.hasMonth() ?
					calendar.getTotalMonthL(getTime(world)) :
						getYear(world, calendar);
		}
		
		public static long getYear(World world, ICalendar calendar)
		{
			return calendar.getYearL(getTime(world));
		}
		
		public static long getMonth(World world, ICalendar calendar)
		{
			return calendar.hasMonth() ?
					calendar.getMonthInYearL(getTime(world)) :
						1;
		}
		
		public static long getDayInMonth(World world, ICalendar calendar)
		{
			return calendar.hasMonth() ?
					calendar.getDayInMonthL(getTime(world)) :
						getDayInYear(world, calendar);
		}
		
		public static long getDayInYear(World world, ICalendar calendar)
		{
			return calendar.getDayInYearL(getTime(world));
		}

		public static double getDayTimeD(World world, ICalendar calendar)
		{
			return calendar.getDayTimeD(getTime(world));
		}
		
		public static String getDateDescription(World world, ICalendar calendar)
		{
			return calendar.hasMonth() ?
					getYear(world, calendar) + "Y" + (getMonth(world, calendar) + 1) + "M" + (getDayInMonth(world, calendar) + 1) :
						getYear(world, calendar) + "Y" + (getDayInMonth(world, calendar) + 1);
		}
	}
	
	public static class Weather
	{
		
	}

	public static class Client
	{
		public static boolean shouldRenderBetterLeaves()
		{
			return (boolean) Reflect.getValue(BlockLeavesBase.class, Arrays.asList("field_150121_P"), Blocks.leaves);
		}
		
	}
}