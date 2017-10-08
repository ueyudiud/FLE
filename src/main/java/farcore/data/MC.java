/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import farcore.lib.material.MatCondition;
import nebula.base.Judgable;
import nebula.common.util.ISubTagContainer;
import nebula.common.util.SubTag;

/**
 * Material condition set.
 * @author ueyudiud
 */
public class MC
{
	/**
	 * Only for single unit item mark.
	 */
	public static final MatCondition LATTICE = new MatCondition("lattice", "Lattice", "%s Lattice").setSize(1L).setFilter(SubTag.TRUE);
	
	public static final Judgable<ISubTagContainer> ROCKY_TOOL;
	public static final Judgable<ISubTagContainer> FLINTY_TOOL;
	public static final Judgable<ISubTagContainer> ROCK_OR_FLINT_TOOL;
	
	public static final MatCondition stone = new MatCondition("stone", "Rock", "%").setSize(5184L, 5184L).setFilter(SubTags.ROCK);
	public static final MatCondition cobble = new MatCondition("cobble", "Cobble", "% Cobble").setSize(5184L, 576L).setFilter(SubTags.ROCK);
	public static final MatCondition brickBlock = new MatCondition("brick_block", "brickBlock", "Brick", "% Brick").setSize(5184L, 1296L, 1.5F).setFilter(SubTags.ROCK);
	public static final MatCondition brickCraving = new MatCondition("brick.craving", "brickCraving", "Brick", "Craving % Brick").setSize(5184L, 5184L, 1.2F).setFilter(SubTags.ROCK);
	
	public static final MatCondition slabStone = new MatCondition("slab_stone", "slabStone", "Slab Rock", "% Slab").setSize(2592L, 2592L).setFilter(SubTags.ROCK);
	public static final MatCondition slabCobble = new MatCondition("slab_cobble", "slabCobble", "Slab Cobble", "% Cobble Slab").setSize(2592L, 576L).setFilter(SubTags.ROCK);
	public static final MatCondition slabBrick = new MatCondition("slab_brick", "slabBrick", "Slab Brick", "% Brick Slab").setSize(2592L, 1296L, 1.5F).setFilter(SubTags.ROCK);
	
	public static final MatCondition soil = new MatCondition("soil", "Soil", "%").setSize(5184L).setFilter(SubTags.DIRT);
	public static final MatCondition seed = new MatCondition("seed", "Seed", "% Seed").setSize(72L, 24L).setFilter(SubTags.CROP);
	public static final MatCondition chip_rock = new MatCondition("chip", "Chip", "% Chip").setSize(576L, 576L, 4.0F).setFilter(SubTags.ROCK);
	public static final MatCondition chip_ore = new MatCondition("chip_ore", "chip", "Ore Chip", "% Chip").setSize(576L, 576L, 4.0F).setFilter(SubTags.ORE);
	public static final MatCondition fragment = new MatCondition("fragment", "Fragment", "% Fragment").setSize(864L, 864L, 1.5F).setFilter(SubTags.ROCK);
	public static final MatCondition sapling = new MatCondition("sapling", "Sapling", "% Sapling").setSize(640L, 512L, 25.0F).setFilter(SubTags.WOOD);
	
	public static final MatCondition pile = new MatCondition("pile", "Pile", "% Pile").setSize(576L, 16L).setFilter(Judgable.or(SubTags.DIRT, SubTags.PILEABLE, SubTags.SAND));
	
	public static final MatCondition block = new MatCondition("block", "Block", "% Block").setSize(1296L, 1296L, 0.1F).setFilter(SubTags.METAL);
	public static final MatCondition ingot = new MatCondition("ingot", "Ingot", "% Ingot").setSize(144L, 144L, 1.0F).setFilter(SubTags.METAL);
	public static final MatCondition nugget = new MatCondition("nugget", "Nugget", "% Nugget").setSize(16L, 16L, 9.0F).setFilter(SubTags.METAL);
	
	public static final MatCondition log = new MatCondition("log", "Log", "% Wood").setSize(5184L).setFilter(SubTags.TREE);
	public static final MatCondition log_cutted = new MatCondition("log_cutted", "logCutted", "Cutted Log", "% Log").setUnsizable().setStackLimit(1).setFilter(SubTags.TREE);
	public static final MatCondition plankBlock = new MatCondition("plank_block", "plank", "Plank", "% Plank").setSize(1296L).setFilter(SubTags.WOOD);
	public static final MatCondition branch = new MatCondition("branch", "Branch", "% Branch").setSize(864L).setFilter(SubTags.TREE);
	public static final MatCondition firewood = new MatCondition("firewood", "firewood", "Firewood", "% Firewood").setSize(1296L, 324L).setFilter(SubTags.TREE);
	public static final MatCondition bark = new MatCondition("bark", "bark", "Bark", "% Bark").setSize(144L, 16L).setFilter(SubTags.TREE);
	
	public static final MatCondition plank = new MatCondition("plank", "Plank", "% Plank").setSize(324L).setFilter(SubTags.WOOD);
	
	public static final MatCondition thread = new MatCondition("thread", "Thread", "%").setSize(36L, 36L, 2.0F).setFilter(SubTags.ROPE);
	public static final MatCondition bundle = new MatCondition("bundle", "Bundle", "% Bundle").setSize(144L, 36L, 0.5F).setFilter(SubTags.ROPE);
	
	public static final MatCondition crop = new MatCondition("crop", "Crop", "%").setUnsizable().setFilter(SubTags.CROP);
	
	public static final MatCondition brick = new MatCondition("brick", "Brick", "% Brick").setSize(144L, 72L).setFilter(SubTags.BRICK);
	public static final MatCondition roofshingle = new MatCondition("roofshingle", "Roof Shingle", "% Roof Shingle").setSize(144L, 72L).setFilter(SubTags.BRICK);
	public static final MatCondition rooftile = new MatCondition("rooftile", "Roof Tile", "% Roof Tile").setSize(144L, 72L).setFilter(SubTags.BRICK);
	
	public static final MatCondition tie = new MatCondition("tie", "Tie", "% Tie").setSize(36L, 18L, 3.0F).setFilter(SubTags.ROPE);
	public static final MatCondition handle = new MatCondition("handle", "Handle", "% Handle").setSize(72L, 72L, 1.5F).setFilter(SubTags.HANDLE);
	
	public static final MatCondition adz_metal = new MatCondition("adz_metal", "adz", "Adz", "% Adz").setSize(288L, 288L, 1.2F).setFilter(Judgable.and(SubTags.METAL, SubTags.TOOL));
	public static final MatCondition adz_rock = new MatCondition("adz_rock", "adz", "Adz", "% Adz").setSize(288L, 288L, 1.2F).setFilter(Judgable.and(Judgable.or(SubTags.ROCK, SubTags.FLINT), SubTags.TOOL));
	public static final MatCondition axe_rock = new MatCondition("axe_rock", "axe", "Axe", "% Axe");
	public static final MatCondition hard_hammer_flint = new MatCondition("hard_hammer_flint", "hardHammer", "Hard Hammer", "% Hammer").setSize(288L, 288L, 1.0F);
	public static final MatCondition hard_hammer_rock = new MatCondition("hard_hammer_rock", "hardHammer", "Hard Hammer", "% Hammer").setSize(288L, 288L, 1.0F).setFilter(Judgable.and(SubTags.ROCK, SubTags.FLINT.not(), SubTags.TOOL));
	public static final MatCondition shovel_rock = new MatCondition("shovel_rock", "shovel", "Shovel", "% Shovel").setSize(288L, 288L, 1.2F);
	public static final MatCondition shovel_metal = new MatCondition("shovel_metal", "shovel", "Shovel", "% Shovel").setSize(144L, 144L, 1.2F).setFilter(Judgable.and(SubTags.METAL, SubTags.TOOL));
	public static final MatCondition spade_hoe_rock = new MatCondition("spade_hoe_rock", "spadeHoe", "Spade-Hoe", "% Spade-Hoe").setSize(288L, 288L, 1.3F);
	public static final MatCondition spear_rock = new MatCondition("spear_rock", "spear", "Spear", "% Spear").setSize(288L, 288L, 1.2F);
	public static final MatCondition sickle_rock = new MatCondition("sickle_rock", "sickle", "Sickle", "% Sickle").setSize(288L, 288L, 1.0F);
	public static final MatCondition firestarter = new MatCondition("firestarter", "firestarter", "Firestarter", "% Firestarter").setSize(288L, 288L, 1.0F).setFilter(Judgable.and(SubTags.WOOD, SubTags.TOOL));
	public static final MatCondition decorticating_plate = new MatCondition("decorticating_plate", "decorticatingPlate", "Decorticating Plate", "% Decorticating Plate");
	public static final MatCondition decorticating_stick = new MatCondition("decorticating_stick", "decorticatingStick", "Decorticating Stick", "% Decorticating Stick");
	public static final MatCondition awl = new MatCondition("awl", "Awl", "% Awl").setFilter(Judgable.and(SubTags.FLINT, SubTags.TOOL));
	public static final MatCondition whetstone = new MatCondition("whetstone", "Whestone", "% Whetstone");
	public static final MatCondition biface = new MatCondition("biface", "Biface", "% Biface");
	public static final MatCondition bar_grizzly = new MatCondition("bar_grizzly", "barGrizzly", "Bar Grizzly", "% Bar Grizzly").setFilter(Judgable.and(SubTags.WOOD, SubTags.TOOL));
	public static final MatCondition spinning_disk = new MatCondition("spinning_disk", "spinningDisk", "Spinning Disk", "% Spinning Disk");
	public static final MatCondition chisel_rock = new MatCondition("chisel_rock", "chisel", "Chisel", "% Chisel");
	
	static
	{
		ROCKY_TOOL = Judgable.and(SubTags.ROCK, SubTags.TOOL);
		FLINTY_TOOL = Judgable.and(SubTags.FLINT, SubTags.TOOL);
		ROCK_OR_FLINT_TOOL = Judgable.and(Judgable.or(SubTags.FLINT, SubTags.ROCK), SubTags.TOOL);
		shovel_rock.setFilter(ROCK_OR_FLINT_TOOL);
		spade_hoe_rock.setFilter(ROCKY_TOOL);
		spear_rock.setFilter(ROCK_OR_FLINT_TOOL);
		sickle_rock.setFilter(ROCKY_TOOL);
		decorticating_plate.setFilter(ROCKY_TOOL);
		decorticating_stick.setFilter(ROCKY_TOOL);
		whetstone.setFilter(ROCKY_TOOL);
		axe_rock.setFilter(ROCK_OR_FLINT_TOOL);
		hard_hammer_flint.setFilter(FLINTY_TOOL);
		biface.setFilter(FLINTY_TOOL);
		spinning_disk.setFilter(ROCKY_TOOL);
		chisel_rock.setFilter(ROCKY_TOOL);
	}
	
	public static void init() {}
}