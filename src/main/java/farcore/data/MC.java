/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import farcore.lib.material.MatCondition;
import nebula.common.base.Judgable;
import nebula.common.util.SubTag;

/**
 * Material condition set.
 * @author ueyudiud
 *
 */
public class MC
{
	/**
	 * Only for single unit item mark.
	 */
	public static final MatCondition LATTICE = new MatCondition("lattice", "Lattice", "%s Lattice").setSize(1L).setFilter(SubTag.TRUE);
	
	public static final MatCondition stone = new MatCondition("stone", "Rock", "%s").setSize(5184L, 5184L).setFilter(SubTags.ROCK);
	public static final MatCondition cobble = new MatCondition("cobble", "Cobble", "%s Cobble").setSize(5184L, 576L).setFilter(SubTags.ROCK);
	public static final MatCondition brickBlock = new MatCondition("brick_block", "brickBlock", "Brick", "%s Brick").setSize(5184L, 1296L, 1.5F).setFilter(SubTags.ROCK);
	public static final MatCondition brickCraving = new MatCondition("brick.craving", "brickCraving", "Brick", "Craving %s Brick").setSize(5184L, 5184L, 1.2F).setFilter(SubTags.ROCK);
	
	public static final MatCondition slabStone = new MatCondition("slab_stone", "slabStone", "Slab Rock", "%s Slab").setSize(2592L, 2592L).setFilter(SubTags.ROCK);
	public static final MatCondition slabCobble = new MatCondition("slab_cobble", "slabCobble", "Slab Cobble", "%s Cobble Slab").setSize(2592L, 576L).setFilter(SubTags.ROCK);
	public static final MatCondition slabBrick = new MatCondition("slab_brick", "slabBrick", "Slab Brick", "%s Brick Slab").setSize(2592L, 1296L, 1.5F).setFilter(SubTags.ROCK);
	
	public static final MatCondition soil = new MatCondition("soil", "Soil", "%s").setSize(5184L).setFilter(SubTags.DIRT);
	public static final MatCondition seed = new MatCondition("seed", "Seed", "%s Seed").setSize(72L, 24L).setFilter(SubTags.CROP);
	public static final MatCondition chip_rock = new MatCondition("chip", "Chip", "%s Chip").setSize(576L, 576L, 4.0F).setFilter(SubTags.ROCK);
	public static final MatCondition chip_ore = new MatCondition("chip_ore", "chip", "Ore Chip", "%s Chip").setSize(576L, 576L, 4.0F).setFilter(SubTags.ORE);
	public static final MatCondition fragment = new MatCondition("fragment", "Fragment", "%s Fragment").setSize(864L, 864L, 1.5F).setFilter(SubTags.ROCK);
	public static final MatCondition sapling = new MatCondition("sapling", "Sapling", "%s Sapling").setSize(640L, 512L, 25.0F).setFilter(SubTags.WOOD);
	public static final MatCondition ingot = new MatCondition("ingot", "Ingot", "%s Ingot").setSize(144L, 144L, 1.0F).setFilter(SubTags.METAL);
	public static final MatCondition nugget = new MatCondition("nugget", "Nugget", "%s Nugget").setSize(16L, 16L, 9.0F).setFilter(Judgable.or(SubTags.METAL, SubTags.ORE_NOBLE));
	
	public static final MatCondition log = new MatCondition("log", "Log", "%s Wood").setSize(5184L).setFilter(SubTags.WOOD);
	public static final MatCondition log_cutted = new MatCondition("log_cutted", "logCutted", "Cutted Log", "%s Log").setUnsizable().setStackLimit(1).setFilter(SubTags.WOOD);
	public static final MatCondition plankBlock = new MatCondition("plank_block", "plank", "Plank", "%s Plank").setSize(1296L).setFilter(SubTags.WOOD);
	public static final MatCondition branch = new MatCondition("branch", "Branch", "%s Branch").setSize(864L).setFilter(SubTags.WOOD);
	
	public static final MatCondition thread = new MatCondition("thread", "Thread", "%s").setSize(36L, 36L, 2.0F).setFilter(SubTags.ROPE);
	public static final MatCondition bundle = new MatCondition("bundle", "Bundle", "%s Bundle").setSize(144L, 36L, 0.5F).setFilter(SubTags.ROPE);
	
	public static final MatCondition crop = new MatCondition("crop", "Crop", "%s").setUnsizable().setFilter(SubTags.CROP);
	
	public static final MatCondition tie = new MatCondition("tie", "Tie", "%s Tie").setSize(36L, 18L, 3.0F).setFilter(SubTags.ROPE);
	public static final MatCondition handle = new MatCondition("handle", "Handle", "%s Handle").setSize(72L, 72L, 1.5F).setFilter(SubTags.HANDLE);
	
	public static final MatCondition adz_metal = new MatCondition("adz_metal", "adz", "Adz", "%s Adz").setSize(288L, 288L, 1.2F).setFilter(Judgable.and(SubTags.METAL, SubTags.TOOL));
	public static final MatCondition adz_rock = new MatCondition("adz_rock", "adz", "Adz", "%s Adz").setSize(288L, 288L, 1.2F).setFilter(Judgable.and(Judgable.or(SubTags.ROCK, SubTags.FLINT), SubTags.TOOL));
	public static final MatCondition hard_hammer_flint = new MatCondition("hard.hammer.flint", "hardHammer", "Hard Hammer", "%s Hammer").setSize(288L, 288L, 1.0F).setFilter(Judgable.and(SubTags.FLINT, SubTags.TOOL));
	public static final MatCondition shovel_rock = new MatCondition("shovel_rock", "shovel", "Shovel", "%s Shovel").setSize(288L, 288L, 1.2F).setFilter(Judgable.and(Judgable.or(SubTags.ROCK, SubTags.FLINT), SubTags.TOOL));
	public static final MatCondition shovel_metal = new MatCondition("shovel_metal", "shovel", "Shovel", "%s Shovel").setSize(144L, 144L, 1.2F).setFilter(Judgable.and(SubTags.METAL, SubTags.TOOL));
	public static final MatCondition spade_hoe_rock = new MatCondition("spade_hoe.rock", "spadeHoe", "Spade-Hoe", "%s Spade-Hoe").setSize(288L, 288L, 1.3F).setFilter(Judgable.and(SubTags.ROCK, SubTags.TOOL));
	public static final MatCondition spear_rock = new MatCondition("spear_rock", "spear", "Spear", "%s Spear").setSize(288L, 288L, 1.2F).setFilter(Judgable.and(SubTags.ROCK, SubTags.TOOL));
	public static final MatCondition sickle_rock = new MatCondition("sickle_rock", "sickle", "Sickle", "%s Sickle").setSize(288L, 288L, 1.0F).setFilter(Judgable.and(SubTags.ROCK, SubTags.TOOL));
	public static final MatCondition firestarter = new MatCondition("firestarter", "firestarter", "Firestarter", "%s Firestarter").setSize(288L, 288L, 1.0F).setFilter(Judgable.and(SubTags.WOOD, SubTags.TOOL));
	public static final MatCondition decorticating_plate = new MatCondition("decorticating.plate", "decorticatingPlate", "Decorticating Plate", "%s Decorticating Plate").setFilter(Judgable.and(SubTags.ROCK, SubTags.TOOL));
	public static final MatCondition decorticating_stick = new MatCondition("decorticating.stick", "decorticatingStick", "Decorticating Stick", "%s Decorticating Stick").setFilter(Judgable.and(SubTags.ROCK, SubTags.TOOL));
	
	public static void init() {}
}