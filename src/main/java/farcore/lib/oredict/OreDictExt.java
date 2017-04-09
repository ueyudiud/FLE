package farcore.lib.oredict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import nebula.common.base.Ety;
import nebula.common.base.Judgable;
import nebula.common.util.ItemStacks;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictExt
{
	private static final long											INVALID_STACK_ID = 0x0000000000000001L;
	private static final List<String>									ID_TO_NAME = new ArrayList<>();
	private static final Map<String, Integer>							NAME_TO_ID = new HashMap<>(128);
	private static final List<
	Map<Item, Entry<Judgable<ItemStack>, List<Judgable<ItemStack>>>>>FUNCTION_MAP = new ArrayList<>();
	private static final Map<Long, List<Integer>>						STACK_TO_IDS = new HashMap<>();
	private static final List<Entry<List<ItemStack>, List<ItemStack>>>	ID_TO_STACK = new ArrayList<>();
	private static final Function<ItemStack, Long>						STACK_SERIALIZER = stack ->
	stack == null ? INVALID_STACK_ID : (long) Item.REGISTRY.getIDForObject(stack.getItem()) << 32 | stack.getItemDamage();
	
	private static final OreDictExt INSTANCE = new OreDictExt();
	
	public static final ImmutableList<ItemStack> EMPTY_LIST = ImmutableList.of();
	
	/**
	 * The far core use back -1 for general meta value.
	 */
	public static final int WILDCARD_VALUE = -1;
	private static final long WILDCARD_VALUE_LONG = 0xFFFFFFFFL;
	
	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		registerVanillaOres();
	}
	
	/**
	 * OD will called before Event Bus begin to listen ore register
	 * event. It needed to add in again.
	 */
	private static void registerVanillaOres()
	{
		// Dye list.
		final String[] dyes = {
				"Black",
				"Red",
				"Green",
				"Brown",
				"Blue",
				"Purple",
				"Cyan",
				"LightGray",
				"Gray",
				"Pink",
				"Lime",
				"Yellow",
				"LightBlue",
				"Magenta",
				"Orange",
				"White"
		};
		
		// tree- and wood-related things
		registerOre("logWood",        Blocks.LOG);
		registerOre("logWood",        Blocks.LOG2);
		registerOre("plankWood",      Blocks.PLANKS);
		registerOre("slabWood",       Blocks.WOODEN_SLAB);
		registerOre("stairWood",      Blocks.OAK_STAIRS);
		registerOre("stairWood",      Blocks.SPRUCE_STAIRS);
		registerOre("stairWood",      Blocks.BIRCH_STAIRS);
		registerOre("stairWood",      Blocks.JUNGLE_STAIRS);
		registerOre("stairWood",      Blocks.ACACIA_STAIRS);
		registerOre("stairWood",      Blocks.DARK_OAK_STAIRS);
		registerOre("stickWood",      Items.STICK);
		registerOre("treeSapling",    Blocks.SAPLING);
		registerOre("treeLeaves",     Blocks.LEAVES);
		registerOre("treeLeaves",     Blocks.LEAVES2);
		registerOre("vine",           Blocks.VINE);
		
		// Ores
		registerOre("oreGold",        Blocks.GOLD_ORE);
		registerOre("oreIron",        Blocks.IRON_ORE);
		registerOre("oreLapis",       Blocks.LAPIS_ORE);
		registerOre("oreDiamond",     Blocks.DIAMOND_ORE);
		registerOre("oreRedstone",    Blocks.REDSTONE_ORE);
		registerOre("oreEmerald",     Blocks.EMERALD_ORE);
		registerOre("oreQuartz",      Blocks.QUARTZ_ORE);
		registerOre("oreCoal",        Blocks.COAL_ORE);
		
		// gems and dusts
		registerOre("gemDiamond",     Items.DIAMOND);
		registerOre("gemEmerald",     Items.EMERALD);
		registerOre("gemQuartz",      Items.QUARTZ);
		registerOre("gemPrismarine",  Items.PRISMARINE_SHARD);
		registerOre("dustPrismarine", Items.PRISMARINE_CRYSTALS);
		registerOre("dustRedstone",   Items.REDSTONE);
		registerOre("dustGlowstone",  Items.GLOWSTONE_DUST);
		registerOre("gemLapis",       new ItemStack(Items.DYE, 1, 4));
		registerOre("gemNetherStar",  Items.NETHER_STAR);//Far core added.
		
		// storage blocks
		registerOre("blockGold",      Blocks.GOLD_BLOCK);
		registerOre("blockIron",      Blocks.IRON_BLOCK);
		registerOre("blockLapis",     Blocks.LAPIS_BLOCK);
		registerOre("blockDiamond",   Blocks.DIAMOND_BLOCK);
		registerOre("blockRedstone",  Blocks.REDSTONE_BLOCK);
		registerOre("blockEmerald",   Blocks.EMERALD_BLOCK);
		registerOre("blockQuartz",    Blocks.QUARTZ_BLOCK);
		registerOre("blockCoal",      Blocks.COAL_BLOCK);
		
		// crops
		registerOre("cropWheat",      Items.WHEAT);
		registerOre("cropPotato",     Items.POTATO);
		registerOre("cropCarrot",     Items.CARROT);
		registerOre("cropNetherWart", Items.NETHER_WART);
		registerOre("sugarcane",      Items.REEDS);
		registerOre("blockCactus",    Blocks.CACTUS);
		
		registerOre("seedWheat",      Items.WHEAT_SEEDS);//Far core added.
		registerOre("seedPotato",     Items.POTATO);//Far core added.
		registerOre("seedCarrot",     Items.CARROT);//Far core added.
		registerOre("seedMelon",      Items.MELON_SEEDS);//Far core added.
		registerOre("seedPumpkin",    Items.PUMPKIN_SEEDS);//Far core added.
		registerOre("seedNetherWart", Items.NETHER_WART);//Far core added.
		
		// misc materials
		registerOre("dye",            new ItemStack(Items.DYE, 1, WILDCARD_VALUE));
		registerOre("paper",          new ItemStack(Items.PAPER));
		registerOre("book",           new ItemStack(Items.BOOK));//Far core added.
		registerOre("book",           new ItemStack(Items.ENCHANTED_BOOK));//Far core added.
		registerOre("book",           new ItemStack(Items.WRITTEN_BOOK));//Far core added.
		
		// mob drops
		registerOre("slimeball",      Items.SLIME_BALL);
		registerOre("enderpearl",     Items.ENDER_PEARL);
		registerOre("bone",           Items.BONE);
		registerOre("gunpowder",      Items.GUNPOWDER);
		registerOre("dustGunpowder",  Items.GUNPOWDER);//Far core added.
		registerOre("string",         Items.STRING);
		registerOre("netherStar",     Items.NETHER_STAR);
		registerOre("leather",        Items.LEATHER);
		registerOre("feather",        Items.FEATHER);
		registerOre("stickBlaze",     Items.BLAZE_ROD);//Far core added.
		registerOre("egg",            Items.EGG);
		
		// records
		registerOre("record",         Items.RECORD_13);
		registerOre("record",         Items.RECORD_CAT);
		registerOre("record",         Items.RECORD_BLOCKS);
		registerOre("record",         Items.RECORD_CHIRP);
		registerOre("record",         Items.RECORD_FAR);
		registerOre("record",         Items.RECORD_MALL);
		registerOre("record",         Items.RECORD_MELLOHI);
		registerOre("record",         Items.RECORD_STAL);
		registerOre("record",         Items.RECORD_STRAD);
		registerOre("record",         Items.RECORD_WARD);
		registerOre("record",         Items.RECORD_11);
		registerOre("record",         Items.RECORD_WAIT);
		
		// blocks
		registerOre("dirt",           Blocks.DIRT);
		registerOre("grass",          Blocks.GRASS);
		registerOre("stone",          Blocks.STONE);
		registerOre("cobblestone",    Blocks.COBBLESTONE);
		registerOre("gravel",         Blocks.GRAVEL);
		registerOre("sand",           Blocks.SAND);
		registerOre("sandstone",      Blocks.SANDSTONE);
		registerOre("sandstone",      Blocks.RED_SANDSTONE);
		registerOre("clay",           Blocks.CLAY);//Far core added.
		registerOre("clayHardened",   Blocks.HARDENED_CLAY);//Far core added.
		registerOre("clayHardened",   Blocks.STAINED_HARDENED_CLAY);//Far core added.
		registerOre("netherrack",     Blocks.NETHERRACK);
		registerOre("obsidian",       Blocks.OBSIDIAN);
		registerOre("glowstone",      Blocks.GLOWSTONE);
		registerOre("endstone",       Blocks.END_STONE);
		registerOre("torch",          Blocks.TORCH);
		registerOre("workbench",      Blocks.CRAFTING_TABLE);
		registerOre("blockSlime",     Blocks.SLIME_BLOCK);
		registerOre("blockPrismarine",       new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.EnumType.ROUGH.getMetadata()));
		registerOre("blockPrismarineBrick",  new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.EnumType.BRICKS.getMetadata()));
		registerOre("blockPrismarineDark",   new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.EnumType.DARK.getMetadata()));
		registerOre("stoneGranite",          new ItemStack(Blocks.STONE, 1, 1));
		registerOre("stoneGranitePolished",  new ItemStack(Blocks.STONE, 1, 2));
		registerOre("stoneDiorite",          new ItemStack(Blocks.STONE, 1, 3));
		registerOre("stoneDioritePolished",  new ItemStack(Blocks.STONE, 1, 4));
		registerOre("stoneAndesite",         new ItemStack(Blocks.STONE, 1, 5));
		registerOre("stoneAndesitePolished", new ItemStack(Blocks.STONE, 1, 6));
		registerOre("blockGlassColorless", Blocks.GLASS);
		registerOre("blockGlass",     Blocks.GLASS);
		registerOre("blockGlass",     Blocks.STAINED_GLASS);
		//blockGlass{Color} is added below with dyes
		registerOre("paneGlassColorless", Blocks.GLASS_PANE);
		registerOre("paneGlass",      Blocks.GLASS_PANE);
		registerOre("paneGlass",      Blocks.STAINED_GLASS_PANE);
		//paneGlass{Color} is added below with dyes
		
		// chests
		registerOre("chest",          Blocks.CHEST);
		registerOre("chest",          Blocks.ENDER_CHEST);
		registerOre("chest",          Blocks.TRAPPED_CHEST);
		registerOre("chestWood",      Blocks.CHEST);
		registerOre("chestEnder",     Blocks.ENDER_CHEST);
		registerOre("chestTrapped",   Blocks.TRAPPED_CHEST);
		
		// dyes
		for(int i = 0; i < 16; ++i)
		{
			registerOre("dye"          + dyes[i], new ItemStack(Items.DYE, 1, i));
			registerOre("blockGlass"   + dyes[i], new ItemStack(Blocks.STAINED_GLASS, 1, 15 - i));
			registerOre("paneGlass"    + dyes[i], new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 15 - i));
			registerOre("clayHardened" + dyes[i], new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 15 - i));//Far core added.
		}
	}
	
	public static int getOreID(String name)
	{
		Integer id = NAME_TO_ID.get(name);
		if(id == null)
		{
			id = ID_TO_NAME.size();
			ID_TO_NAME.add(name);
			NAME_TO_ID.put(name, id);
			List<ItemStack> stacks = new ArrayList<>();
			ID_TO_STACK.add(new Ety<>(stacks, Collections.unmodifiableList(stacks)));
			FUNCTION_MAP.add(new HashMap<>());
		}
		return id.intValue();
	}
	
	@Deprecated
	private static int setOreIDWithSuggestedList(String name, Judgable<ItemStack> function, List<ItemStack> stacks)
	{
		Integer id = NAME_TO_ID.get(name);
		if(id == null)
		{
			id = ID_TO_NAME.size();
			ID_TO_NAME.add(name);
			NAME_TO_ID.put(name, id);
			ID_TO_STACK.add(new Ety<>(stacks, Collections.unmodifiableList(stacks)));
		}
		return id.intValue();
	}
	
	public static String getOreName(int id)
	{
		return (id >= 0 && id < ID_TO_NAME.size()) ? ID_TO_NAME.get(id) : null;
	}
	
	public static void registerOre(String name, Block block)           { registerOre(name, Item.getItemFromBlock(block)); }
	public static void registerOre(String name, Block block, int meta) { registerOre(name, Item.getItemFromBlock(block), meta); }
	public static void registerOre(String name, Item item)
	{
		if(name == null || item == null) return;
		int oreID = getOreID(name);
		int itemID = Item.REGISTRY.getIDForObject(item);
		if(itemID == -1) throw new RuntimeException("An invalid registeration has a raw item id!");
		long stackID = (long) itemID << 32 | WILDCARD_VALUE_LONG;
		registerOreFunction(name, item, Judgable.TRUE);
		registerIDToStackObject(oreID, new ItemStack(item));
		registerStackToIDObject(oreID, stackID);
	}
	public static void registerOre(String name, Item item, int meta)   { registerOre(name, ItemStacks.stack(item, meta)); }
	public static void registerOre(String name, ItemStack stack)
	{
		if(name == null || stack == null) return;
		final int damage = stack.getItemDamage();
		registerOreFunction(name, stack.getItem(), s-> s.getItemDamage() == damage, stack);
	}
	
	private static void registerIDToStackObject(int oreID, ItemStack stack)
	{
		ID_TO_STACK.get(oreID).getKey().add(stack.copy());
	}
	
	private static void registerStackToIDObject(int oreID, long stackID)
	{
		List<Integer> list = STACK_TO_IDS.get(stackID);
		if(list == null)
		{
			STACK_TO_IDS.put(stackID, list = new ArrayList<>());
		}
		list.add(oreID);
	}
	
	public static void registerOreFunction(String name, Item item, Judgable<ItemStack> function, Collection<ItemStack> instances) { registerOreFunction(name, item, function, L.cast(instances, ItemStack.class)); }
	
	/**
	 * Registers a ore function into the dictionary.
	 * Also registers all instances of function provide into instance list.
	 * @param name
	 * @param item The detect item.
	 * @param function
	 * @param instances
	 */
	public static void registerOreFunction(String name, Item item, Judgable<ItemStack> function, ItemStack...instances)
	{
		if(name == null || function == null) return;
		int oreID = getOreID(name);
		List<ItemStack> list1 = ID_TO_STACK.get(oreID).getKey();
		Map<Item, Entry<Judgable<ItemStack>, List<Judgable<ItemStack>>>> map = FUNCTION_MAP.get(oreID);
		List<Judgable<ItemStack>> list;
		if(!map.containsKey(item))
		{
			list = new ArrayList<>();
			map.put(item, new Ety<>(Judgable.or(list), list));
		}
		else list = map.get(item).getValue();
		list.add(function);
		for(ItemStack stack : instances)
		{
			long stackID = STACK_SERIALIZER.apply(stack);
			list1.add(stack.copy());
			registerStackToIDObject(oreID, stackID);
		}
	}
	
	private static List<ItemStack> getOres(int id)
	{
		return ID_TO_STACK.size() > id ? ID_TO_STACK.get(id).getValue() : EMPTY_LIST;
	}
	
	public static List<ItemStack> getOres(String name)
	{
		return getOres(getOreID(name));
	}
	
	/**
	 * Get ores allowance.
	 * @param name The name of ore.
	 * @param alwaysCreateEntry Should ore dictionary create a new tag for name not contain.
	 * @return
	 */
	public static List<ItemStack> getOres(String name, boolean alwaysCreateEntry)
	{
		return alwaysCreateEntry || NAME_TO_ID.get(name) != null ? getOres(getOreID(name)) : EMPTY_LIST;
	}
	
	public static List<String> getOreNames(ItemStack stack)
	{
		return getOreNames(stack, true);
	}
	
	/**
	 * Get all ore names this stack applied.<br>
	 * @param stack The ore.
	 * @param useCache If this stack has special nbt, might not save in cache,
	 * use false to check all ore names again.
	 * @return
	 */
	public static List<String> getOreNames(ItemStack stack, boolean useCache)
	{
		if(stack == null) throw new IllegalArgumentException("The stack can not be null!");
		List<String> list = new ArrayList<>();
		if(useCache)
		{
			long stackID = STACK_SERIALIZER.apply(stack);
			List<Integer> list1 = STACK_TO_IDS.get(stackID);
			if(list1 != null)
			{
				for(Integer i : list1)
				{
					list.add(ID_TO_NAME.get(i));
				}
			}
			stackID |= WILDCARD_VALUE;
			list1 = STACK_TO_IDS.get(stackID);
			if(list1 != null)
			{
				for(Integer i : list1)
				{
					list.add(ID_TO_NAME.get(i));
				}
			}
			return list;
		}
		Item item = stack.getItem();
		for(int i = 0; i < FUNCTION_MAP.size(); ++i)
		{
			Map<Item, Entry<Judgable<ItemStack>, List<Judgable<ItemStack>>>> map = FUNCTION_MAP.get(i);
			if(map.containsKey(item) && map.get(item).getKey().isTrue(stack))
				list.add(ID_TO_NAME.get(i));
		}
		return list;
	}
	
	public static boolean oreMatchs(ItemStack stack, String oreName)
	{
		Map<Item, Entry<Judgable<ItemStack>, List<Judgable<ItemStack>>>> map;
		return
				NAME_TO_ID.containsKey(oreName) ?
						(map = FUNCTION_MAP.get(NAME_TO_ID.get(oreName)))
						.containsKey(stack.getItem()) && map.get(stack.getItem()).getKey().isTrue(stack)
						: false;
	}
	
	private OreDictExt(){ }
	
	@SubscribeEvent
	public void onOreRegistered(OreDictionary.OreRegisterEvent event)
	{
		if(event.getOre().getItemDamage() == OreDictionary.WILDCARD_VALUE)
		{
			registerOre(event.getName(), event.getOre().getItem());
		}
		else
		{
			registerOre(event.getName(), event.getOre());
		}
	}
}