package farcore.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.FarCoreSetup.ClientProxy;
import farcore.data.ColorMultiplier;
import farcore.data.Config;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.model.block.ICustomItemModelSelector;
import farcore.lib.model.block.ModelFluidBlock;
import farcore.lib.model.block.statemap.StateMapperExt;
import farcore.lib.net.world.PacketBreakBlock;
import farcore.lib.oredict.OreDictExt;
import farcore.lib.render.FontRenderExtend;
import farcore.lib.render.IProgressBarStyle;
import farcore.lib.render.ParticleDiggingExt;
import farcore.lib.util.Direction;
import farcore.lib.util.IRenderRegister;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
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
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class U
{
	private static final Random RNG = new Random();
	@SidedProxy(serverSide = "farcore.util.U$CommonHandler", clientSide = "farcore.util.U$ClientHandler")
	static CommonHandler handlerGatway;
	
	public static class Mod
	{
		public static String getActiveModID()
		{
			try
			{
				if(Loader.instance().activeModContainer() == null)
					return "minecraft";
				return Loader.instance().activeModContainer().getModId();
			}
			catch (Exception exception)
			{
				return "unknown";
			}
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
			((ClientHandler) handlerGatway).registerColorMultiplier(color, block);
		}
		
		@SideOnly(Side.CLIENT)
		public static void registerColorMultiplier(IItemColor color, Item...item)
		{
			((ClientHandler) handlerGatway).registerColorMultiplier(color, item);
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
		
		public static void registerBuildInModel(Block block)
		{
			if(Loader.instance().hasReachedState(LoaderState.INITIALIZATION))
			{
				Log.warn("The block '" + block.getRegistryName() + "' register buildin model after initialzation, tried register it early.");
			}
			else
			{
				ClientProxy.buildInRender.add(block);
			}
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
			if(ItemStacks.valid(ore) == null) return;
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
				name = farcore.util.Strings.validateOre(false, name);
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
				if(isAir(world, pos)) return;
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
		
		public static <E> List<E> getListFromWorldDimention(Map<Integer, List<E>> map, World world, boolean createEntry)
		{
			Integer dim = world.provider.getDimension();
			List<E> list = map.get(dim);
			if(list == null)
			{
				if(createEntry)
				{
					map.put(dim, list = new ArrayList());
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
		
		public static void spawnDropInWorld(World world, BlockPos pos, ItemStack drop)
		{
			if(world.isRemote ||
					world.getWorldType() == WorldType.DEBUG_WORLD ||
					//Debug world can drop item will crash the game...
					drop == null)
				return;
			float f = 0.7F;
			double d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, drop.copy());
			entityitem.setPickupDelay(10);
			world.spawnEntityInWorld(entityitem);
		}
		
		public static void spawnDropsInWorld(World world, BlockPos pos, List<ItemStack> drops)
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
				world.setBlockToAir(pos);//Because the fallen block use delaying update, remove block directly.
				return world.isRemote || world.spawnEntityInWorld(new EntityFallingBlockExtended(world, pos, dropPos, state, world.getTileEntity(pos)));
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
		
		public static boolean isAir(World world, BlockPos pos)
		{
			IBlockState state = world.getBlockState(pos);
			return state.getBlock().isAir(state, world, pos);
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
		public static final ModelResourceLocation MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
		
		public static int mulColor(int ARGB1, int ARGB2)
		{
			int a = ((ARGB1 >> 24) & 0xFF) * ((ARGB2 >> 24) & 0xFF) >> 8;
			int r = ((ARGB1 >> 16) & 0xFF) * ((ARGB2 >> 16) & 0xFF) >> 8;
			int g = ((ARGB1 >> 8 ) & 0xFF) * ((ARGB2 >> 8 ) & 0xFF) >> 8;
			int b = ((ARGB1      ) & 0xFF) * ((ARGB2      ) & 0xFF) >> 8;
			return a << 24 | r << 16 | g << 8 | b;
		}
		
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
				((ClientProxy) FarCoreSetup.proxy).addRenderRegisterListener((IRenderRegister) object);
			}
		}
		
		@Override
		public <T extends Comparable<T>> void registerCompactModel(boolean splitFile, Block block, String modid, String path, IProperty<T> property,
				IProperty...properties)
		{
			StateMapperExt mapper = new StateMapperExt(modid, path, splitFile ? property : null, properties);
			((ClientProxy) FarCoreSetup.proxy).registerCompactModel(mapper, block, property);
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
			registerColorMultiplier(ColorMultiplier.BIOME_COLOR, block);
			registerColorMultiplier(ColorMultiplier.ITEMBLOCK_COLOR, block);
		}
		
		public void registerColorMultiplier(IBlockColor color, Block[] block)
		{
			farcore.util.L.put(blockColorMap, color, block);
		}
		
		private void registerColorMultiplier(IItemColor itemblockColor, Block[] blocks)
		{
			farcore.util.L.put(itemColorMap, itemblockColor, Lists.transform(Arrays.asList(blocks), (Block block) -> Item.getItemFromBlock(block)));
		}
		
		private void registerColorMultiplier(IItemColor itemColor, Item[] item)
		{
			farcore.util.L.put(itemColorMap, itemColor, item);
		}
		
		@Override
		public String translateToLocalByI18n(String unlocal, Object...parameters)
		{
			return I18n.format(unlocal, parameters);
		}
		
		public static Map<IBlockColor, List<Block>> blockColorMap = new HashMap();
		public static Map<IItemColor, List<Item>> itemColorMap = new HashMap();
	}
}