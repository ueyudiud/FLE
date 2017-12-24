/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.oredict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import nebula.base.Ety;
import nebula.base.Judgable;
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

/**
 * The extended ore dictionary (E-OD).
 * <p>
 * For more compatibility for ore, without instance required, you can use NBT to
 * predicate is item is a ore.
 * <p>
 * All base ore list in {@link net.minecraftforge.oredict.OreDictionary}, will
 * be register into here, so you can also use this dictionary to find vanilla
 * ore item with out find in forge dictionary one more time.
 * <p>
 * Like forge dictionary, this one use name-id-stackPredicator pair to store ore
 * list. The {@link #ID_TO_NAME}, {@link #NAME_TO_ID}, takes uses of id<->name
 * logic, the {@link #STACK_TO_IDS}, {@link #ID_TO_STACK} takes uses to find
 * instance stack of each ore name (Which can be use to display on crafting
 * recipe, etc).
 * <p>
 * To matching ItemStack by E-OD, please use
 * {@link #oreMatchs(ItemStack, String)} to get result directly instead of get
 * list of stack instance to match.
 * 
 * @author ueyudiud
 * @see net.minecraftforge.oredict.OreDictionary
 */
public class OreDictExt
{
	/** The id of 'stack is invalid', only for internal use. */
	private static final long																	INVALID_STACK_ID	= 0x0000000000000001L;
	private static final List<String>															ID_TO_NAME			= new ArrayList<>();
	private static final Map<String, Integer>													NAME_TO_ID			= new HashMap<>(128);
	private static final List<Map<Item, Entry<Judgable<ItemStack>, List<Judgable<ItemStack>>>>>	FUNCTION_MAP		= new ArrayList<>();
	private static final Map<Long, List<Integer>>												STACK_TO_IDS		= new HashMap<>();
	private static final List<Entry<List<ItemStack>, List<ItemStack>>>							ID_TO_STACK			= new ArrayList<>();
	public static final Function<ItemStack, Long>												STACK_SERIALIZER	= stack -> stack == null ? INVALID_STACK_ID : (long) Item.REGISTRY.getIDForObject(stack.getItem()) << 32 | stack.getItemDamage();
	
	private static final OreDictExt INSTANCE = new OreDictExt();
	
	public static final ImmutableList<ItemStack> EMPTY_LIST = ImmutableList.of();
	
	/**
	 * The Far Core use back -1 for general meta value, for some item has extra
	 * meta load from NBT by
	 * {@link nebula.common.item.ItemBase#getStackMetaOffset(ItemStack)}}
	 */
	public static final int		WILDCARD_VALUE		= -1;
	private static final long	WILDCARD_VALUE_LONG	= 0xFFFFFFFFL;
	
	/** Internal method, do not use. */
	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		registerVanillaOres();
	}
	
	/**
	 * OD will called before Event Bus begin to listen ore register event. So
	 * the vanilla ore needed add to EOD again.
	 */
	private static void registerVanillaOres()
	{
		// Dye list.
		final String[] dyes = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
		
		// tree- and wood-related things
		registerOre("logWood", Blocks.LOG);
		registerOre("logWood", Blocks.LOG2);
		registerOre("plankWood", Blocks.PLANKS);
		registerOre("slabWood", Blocks.WOODEN_SLAB);
		registerOre("stairWood", Blocks.OAK_STAIRS);
		registerOre("stairWood", Blocks.SPRUCE_STAIRS);
		registerOre("stairWood", Blocks.BIRCH_STAIRS);
		registerOre("stairWood", Blocks.JUNGLE_STAIRS);
		registerOre("stairWood", Blocks.ACACIA_STAIRS);
		registerOre("stairWood", Blocks.DARK_OAK_STAIRS);
		registerOre("stickWood", Items.STICK);
		registerOre("treeSapling", Blocks.SAPLING);
		registerOre("treeLeaves", Blocks.LEAVES);
		registerOre("treeLeaves", Blocks.LEAVES2);
		registerOre("vine", Blocks.VINE);
		
		// Ores
		registerOre("oreGold", Blocks.GOLD_ORE);
		registerOre("oreIron", Blocks.IRON_ORE);
		registerOre("oreLapis", Blocks.LAPIS_ORE);
		registerOre("oreDiamond", Blocks.DIAMOND_ORE);
		registerOre("oreRedstone", Blocks.REDSTONE_ORE);
		registerOre("oreEmerald", Blocks.EMERALD_ORE);
		registerOre("oreQuartz", Blocks.QUARTZ_ORE);
		registerOre("oreCoal", Blocks.COAL_ORE);
		
		// gems and dusts
		registerOre("gemDiamond", Items.DIAMOND);
		registerOre("gemEmerald", Items.EMERALD);
		registerOre("gemQuartz", Items.QUARTZ);
		registerOre("gemPrismarine", Items.PRISMARINE_SHARD);
		registerOre("dustPrismarine", Items.PRISMARINE_CRYSTALS);
		registerOre("dustRedstone", Items.REDSTONE);
		registerOre("dustGlowstone", Items.GLOWSTONE_DUST);
		registerOre("gemLapis", new ItemStack(Items.DYE, 1, 4));
		registerOre("gemNetherStar", Items.NETHER_STAR);// Far Core added.
		
		// storage blocks
		registerOre("blockGold", Blocks.GOLD_BLOCK);
		registerOre("blockIron", Blocks.IRON_BLOCK);
		registerOre("blockLapis", Blocks.LAPIS_BLOCK);
		registerOre("blockDiamond", Blocks.DIAMOND_BLOCK);
		registerOre("blockRedstone", Blocks.REDSTONE_BLOCK);
		registerOre("blockEmerald", Blocks.EMERALD_BLOCK);
		registerOre("blockQuartz", Blocks.QUARTZ_BLOCK);
		registerOre("blockCoal", Blocks.COAL_BLOCK);
		
		// crops
		registerOre("cropWheat", Items.WHEAT);
		registerOre("cropPotato", Items.POTATO);
		registerOre("cropCarrot", Items.CARROT);
		registerOre("cropNetherWart", Items.NETHER_WART);
		registerOre("sugarcane", Items.REEDS);
		registerOre("blockCactus", Blocks.CACTUS);
		
		registerOre("seedWheat", Items.WHEAT_SEEDS);// Far Core added.
		registerOre("seedPotato", Items.POTATO);// Far Core added.
		registerOre("seedCarrot", Items.CARROT);// Far Core added.
		registerOre("seedMelon", Items.MELON_SEEDS);// Far Core added.
		registerOre("seedPumpkin", Items.PUMPKIN_SEEDS);// Far Core added.
		registerOre("seedNetherWart", Items.NETHER_WART);// Far Core added.
		
		// misc materials
		registerOre("dye", new ItemStack(Items.DYE, 1, WILDCARD_VALUE));
		registerOre("paper", new ItemStack(Items.PAPER));
		registerOre("book", new ItemStack(Items.BOOK));// Far Core added.
		registerOre("book", new ItemStack(Items.ENCHANTED_BOOK));// Far Core
		// added.
		registerOre("book", new ItemStack(Items.WRITTEN_BOOK));// Far Core
		// added.
		
		// mob drops
		registerOre("slimeball", Items.SLIME_BALL);
		registerOre("enderpearl", Items.ENDER_PEARL);
		registerOre("bone", Items.BONE);
		registerOre("gunpowder", Items.GUNPOWDER);
		registerOre("dustGunpowder", Items.GUNPOWDER);// Far core added.
		registerOre("string", Items.STRING);
		registerOre("netherStar", Items.NETHER_STAR);
		registerOre("leather", Items.LEATHER);
		registerOre("feather", Items.FEATHER);
		registerOre("stickBlaze", Items.BLAZE_ROD);// Far core added.
		registerOre("egg", Items.EGG);
		
		// records
		registerOre("record", Items.RECORD_13);
		registerOre("record", Items.RECORD_CAT);
		registerOre("record", Items.RECORD_BLOCKS);
		registerOre("record", Items.RECORD_CHIRP);
		registerOre("record", Items.RECORD_FAR);
		registerOre("record", Items.RECORD_MALL);
		registerOre("record", Items.RECORD_MELLOHI);
		registerOre("record", Items.RECORD_STAL);
		registerOre("record", Items.RECORD_STRAD);
		registerOre("record", Items.RECORD_WARD);
		registerOre("record", Items.RECORD_11);
		registerOre("record", Items.RECORD_WAIT);
		
		// blocks
		registerOre("dirt", Blocks.DIRT);
		registerOre("grass", Blocks.GRASS);
		registerOre("stone", Blocks.STONE);
		registerOre("cobblestone", Blocks.COBBLESTONE);
		registerOre("gravel", Blocks.GRAVEL);
		registerOre("sand", Blocks.SAND);
		registerOre("sandstone", Blocks.SANDSTONE);
		registerOre("sandstone", Blocks.RED_SANDSTONE);
		registerOre("clay", Blocks.CLAY);// Far Core added.
		registerOre("clayHardened", Blocks.HARDENED_CLAY);// Far Core added.
		registerOre("clayHardened", Blocks.STAINED_HARDENED_CLAY);// Far Core
		// added.
		registerOre("netherrack", Blocks.NETHERRACK);
		registerOre("obsidian", Blocks.OBSIDIAN);
		registerOre("glowstone", Blocks.GLOWSTONE);
		registerOre("endstone", Blocks.END_STONE);
		registerOre("torch", Blocks.TORCH);
		registerOre("workbench", Blocks.CRAFTING_TABLE);
		registerOre("blockSlime", Blocks.SLIME_BLOCK);
		registerOre("blockPrismarine", new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.EnumType.ROUGH.getMetadata()));
		registerOre("blockPrismarineBrick", new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.EnumType.BRICKS.getMetadata()));
		registerOre("blockPrismarineDark", new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.EnumType.DARK.getMetadata()));
		registerOre("stoneGranite", new ItemStack(Blocks.STONE, 1, 1));
		registerOre("stoneGranitePolished", new ItemStack(Blocks.STONE, 1, 2));
		registerOre("stoneDiorite", new ItemStack(Blocks.STONE, 1, 3));
		registerOre("stoneDioritePolished", new ItemStack(Blocks.STONE, 1, 4));
		registerOre("stoneAndesite", new ItemStack(Blocks.STONE, 1, 5));
		registerOre("stoneAndesitePolished", new ItemStack(Blocks.STONE, 1, 6));
		registerOre("blockGlassColorless", Blocks.GLASS);
		registerOre("blockGlass", Blocks.GLASS);
		registerOre("blockGlass", Blocks.STAINED_GLASS);
		// blockGlass{Color} is added below with dyes
		registerOre("paneGlassColorless", Blocks.GLASS_PANE);
		registerOre("paneGlass", Blocks.GLASS_PANE);
		registerOre("paneGlass", Blocks.STAINED_GLASS_PANE);
		// paneGlass{Color} is added below with dyes
		
		// chests
		registerOre("chest", Blocks.CHEST);
		registerOre("chest", Blocks.ENDER_CHEST);
		registerOre("chest", Blocks.TRAPPED_CHEST);
		registerOre("chestWood", Blocks.CHEST);
		registerOre("chestEnder", Blocks.ENDER_CHEST);
		registerOre("chestTrapped", Blocks.TRAPPED_CHEST);
		
		// dyes
		for (int i = 0; i < 16; ++i)
		{
			registerOre("dye" + dyes[i], new ItemStack(Items.DYE, 1, i));
			registerOre("blockGlass" + dyes[i], new ItemStack(Blocks.STAINED_GLASS, 1, 15 - i));
			registerOre("paneGlass" + dyes[i], new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 15 - i));
			registerOre("clayHardened" + dyes[i], new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 15 - i));// Far
			// Core
			// added.
		}
	}
	
	private static int getOreID(String name)
	{
		Integer id = NAME_TO_ID.get(name);
		if (id == null)
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
	
	/**
	 * Register block to EOD.
	 * 
	 * @see #registerOre(String, Item)
	 */
	public static void registerOre(String name, Block block)
	{
		registerOre(name, Item.getItemFromBlock(block));
	}
	
	/**
	 * Register block with meta to EOD.
	 * 
	 * @see #registerOre(String, Item, int)
	 */
	public static void registerOre(String name, Block block, int meta)
	{
		registerOre(name, Item.getItemFromBlock(block), meta);
	}
	
	/**
	 * Register item to EOD.
	 * <p>
	 * Added item by EOD will not change OD list.
	 * <p>
	 * Register ore by this method will provider an instance display item stack
	 * to display list and a default any-meta <tt>ItemStack=>boolean</tt>
	 * predicate will be register to EOD. will be applied to ore list.
	 * 
	 * @param name the name of ore.
	 * @param item the register item.
	 */
	public static void registerOre(String name, Item item)
	{
		if (name == null || item == null) return;
		int oreID = getOreID(name);
		int itemID = Item.REGISTRY.getIDForObject(item);
		if (itemID == -1) throw new RuntimeException("An invalid registeration has a raw item id!");
		long stackID = (long) itemID << 32 | WILDCARD_VALUE_LONG;
		registerOreFunction(name, item, Judgable.TRUE);
		registerIDToStackObject(oreID, new ItemStack(item));
		registerStackToIDObject(oreID, stackID);
	}
	
	/**
	 * Register item with specific meta to EOD.
	 * <p>
	 * Added item by EOD will not change OD list.
	 * <p>
	 * Register ore by this method will provider an instance display item stack
	 * to display list and a specific <tt>ItemStack=>boolean</tt> predicate
	 * which is: <code>stack->stack.getItemDamage()==meta</code> will be
	 * register to EOD.
	 * 
	 * @param name the name of ore.
	 * @param item the register item.
	 * @param meta the specific meta.
	 * @see #registerOreFunction(String, Item, Judgable, ItemStack...)
	 */
	public static void registerOre(String name, Item item, int meta)
	{
		registerOre(name, ItemStacks.stack(item, meta));
	}
	
	/**
	 * Register a ore by item and meta from stack.
	 * 
	 * @param name the name of ore.
	 * @param stack the source to get item and meta.
	 * @see #registerOre(String, Item, int)
	 */
	public static void registerOre(String name, ItemStack stack)
	{
		if (name == null || stack == null) return;
		final int damage = stack.getItemDamage();
		registerOreFunction(name, stack.getItem(), s -> s.getItemDamage() == damage, stack);
	}
	
	private static void registerIDToStackObject(int oreID, ItemStack stack)
	{
		ID_TO_STACK.get(oreID).getKey().add(stack.copy());
	}
	
	private static void registerStackToIDObject(int oreID, long stackID)
	{
		List<Integer> list = STACK_TO_IDS.get(stackID);
		if (list == null)
		{
			STACK_TO_IDS.put(stackID, list = new ArrayList<>());
		}
		list.add(oreID);
	}
	
	/** @see #registerOreFunction(String, Item, Judgable, ItemStack[]) */
	public static void registerOreFunction(String name, Item item, Judgable<ItemStack> function, Collection<ItemStack> instances)
	{
		registerOreFunction(name, item, function, L.cast(instances, ItemStack.class));
	}
	
	/**
	 * Registers a ore function into the dictionary.
	 * <p>
	 * Also registers all instances of function provide into instance list.
	 * <p>
	 * The stack will <i>not</i> take any effect to match an ore. It is only can
	 * be display or show what may can be use in this recipe, input a stack can
	 * not let <tt>function.isTrue(stack)</tt> return <tt>true</tt> is allowed,
	 * but taking sure the player can know what's the meaning of your instances
	 * in this ore key (Example: for some recipe may use fluid, you may use
	 * {@link nebula.common.item.ItemFluidDisplay}, it can not get in survival
	 * mode, but you can use it to let crafter know it means you want to use
	 * this fluid in recipe).
	 * 
	 * @param name the name of ore.
	 * @param item the detect item.
	 * @param function the <tt>ItemStack=>boolean</tt> logic to predicate
	 *            whether a ItemStack can be as a ore.
	 * @param instances some instances of ore, use to display in recipe or ore
	 *            transfer list, also can not register any instances, and there
	 *            may no display item stack will be show instead (I'm sure not
	 *            any one will be happy to find this when searching recipe.).
	 * @see #registerOre(String, Item, int)
	 */
	public static void registerOreFunction(String name, Item item, @Nonnull Judgable<ItemStack> function, ItemStack...instances)
	{
		if (name == null || function == null) return;
		int oreID = getOreID(name);
		List<ItemStack> list1 = ID_TO_STACK.get(oreID).getKey();
		Map<Item, Entry<Judgable<ItemStack>, List<Judgable<ItemStack>>>> map = FUNCTION_MAP.get(oreID);
		List<Judgable<ItemStack>> list;
		if (!map.containsKey(item))
		{
			list = new ArrayList<>();
			map.put(item, new Ety<>(Judgable.or(list), list));
		}
		else
			list = map.get(item).getValue();
		list.add(function);
		for (ItemStack stack : instances)
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
	
	/**
	 * Get ores allowance.
	 * <p>
	 * The result list is <i>unmodifiable</i>.
	 * 
	 * @param name the name of ore.
	 * @return the list of ores.
	 */
	public static List<ItemStack> getOres(String name)
	{
		return getOres(getOreID(name));
	}
	
	/**
	 * Get ores allowance.
	 * 
	 * @param name the name of ore.
	 * @param alwaysCreateEntry <tt>true</tt> for E-OD will always create a new
	 *            tag to store list if ore with this name is not exist. The list
	 *            in E-OD will be returned if <tt>true</tt> is inputed.
	 * @return
	 */
	public static List<ItemStack> getOres(String name, boolean alwaysCreateEntry)
	{
		return alwaysCreateEntry || NAME_TO_ID.get(name) != null ? getOres(getOreID(name)) : EMPTY_LIST;
	}
	
	/**
	 * Get all ore names this stack applied, will only check the instance
	 * registered to E-OD and return result.
	 * 
	 * @see #getOreNames(ItemStack, boolean)
	 */
	public static List<String> getOreNames(ItemStack stack)
	{
		return getOreNames(stack, true);
	}
	
	/**
	 * Get all ore names this stack applied.<br>
	 * 
	 * @param stack the stack to find ore.
	 * @param useCache if this stack has special NBT, might not save in cache,
	 *            use <tt>false</tt> to check all ore names again.
	 * @return the ore name list.
	 */
	public static List<String> getOreNames(ItemStack stack, boolean useCache)
	{
		if (stack == null) throw new IllegalArgumentException("The stack can not be null!");
		List<String> list = new ArrayList<>();
		if (useCache)
		{
			long stackID = STACK_SERIALIZER.apply(stack);
			List<Integer> list1 = STACK_TO_IDS.get(stackID);
			if (list1 != null)
			{
				for (Integer i : list1)
				{
					list.add(ID_TO_NAME.get(i));
				}
			}
			stackID |= WILDCARD_VALUE;
			list1 = STACK_TO_IDS.get(stackID);
			if (list1 != null)
			{
				for (Integer i : list1)
				{
					list.add(ID_TO_NAME.get(i));
				}
			}
			return list;
		}
		Item item = stack.getItem();
		for (int i = 0; i < FUNCTION_MAP.size(); ++i)
		{
			Map<Item, Entry<Judgable<ItemStack>, List<Judgable<ItemStack>>>> map = FUNCTION_MAP.get(i);
			if (map.containsKey(item) && map.get(item).getKey().test(stack)) list.add(ID_TO_NAME.get(i));
		}
		return list;
	}
	
	/**
	 * Match stack has this ore name.
	 * 
	 * @param stack
	 * @param oreName
	 * @return
	 */
	public static boolean oreMatchs(ItemStack stack, String oreName)
	{
		Map<Item, Entry<Judgable<ItemStack>, List<Judgable<ItemStack>>>> map;
		return NAME_TO_ID.containsKey(oreName) ? (map = FUNCTION_MAP.get(NAME_TO_ID.get(oreName))).containsKey(stack.getItem()) && map.get(stack.getItem()).getKey().test(stack) : false;
	}
	
	private OreDictExt()
	{
	}
	
	/** Internal method, register ore registered in OD to E-OD. */
	@SubscribeEvent
	public void onOreRegistered(OreDictionary.OreRegisterEvent event)
	{
		if (event.getOre().getItemDamage() == OreDictionary.WILDCARD_VALUE)
		{
			registerOre(event.getName(), event.getOre().getItem());
		}
		else
		{
			registerOre(event.getName(), event.getOre());
		}
	}
}
