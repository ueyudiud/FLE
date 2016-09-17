package farcore.data;

import farcore.lib.material.MatCondition;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.SubTag;

public class MC
{
	public static final MatCondition LATTICE = new MatCondition("lattice", "Lattice", "%s Lattice").setSize(1L).setFilter(SubTag.TRUE);

	public static final MatCondition stone = new MatCondition("stone", "Rock", "%s").setSize(5184L, 5184L).setFilter(SubTag.ROCK);
	public static final MatCondition cobble = new MatCondition("cobble", "Cobble", "%s Cobble").setSize(5184L, 576L).setFilter(SubTag.ROCK);
	public static final MatCondition brickBlock = new MatCondition("brick.block", "brickBlock", "Brick", "%s Brick").setSize(5184L, 1296L, 1.5F).setFilter(SubTag.ROCK);
	public static final MatCondition brickCraving = new MatCondition("brick.craving", "brickCraving", "Brick", "Craving %s Brick").setSize(5184L, 5184L, 1.2F).setFilter(SubTag.ROCK);
	public static final MatCondition seed = new MatCondition("seed", "Seed", "%s Seed").setSize(72L, 24L).setFilter(SubTag.CROP);
	public static final MatCondition chip_rock = new MatCondition("chip", "Chip", "%s Chip").setSize(576L, 576L, 4.0F).setFilter(SubTag.ROCK);
	public static final MatCondition chip_ore = new MatCondition("chip.ore", "chip", "Ore Chip", "%s Chip").setSize(576L, 576L, 4.0F).setFilter(SubTag.ORE);
	public static final MatCondition fragment = new MatCondition("fragment", "Fragment", "%s Fragment").setSize(864L, 864L, 1.5F).setFilter(SubTag.ROCK);
	public static final MatCondition sapling = new MatCondition("sapling", "Sapling", "%s Sapling").setSize(640L, 512L, 25.0F).setFilter(SubTag.WOOD);
	public static final MatCondition nugget = new MatCondition("nugget", "Nugget", "%s Nugget").setSize(16L, 16L, 9.0F).setFilter(new IDataChecker.Or(SubTag.METAL, SubTag.ORE_NOBLE));

	public static final MatCondition thread = new MatCondition("thread", "Thread", "%s").setSize(36L, 36L, 2.0F).setFilter(SubTag.ROPE);
	public static final MatCondition bundle = new MatCondition("bundle", "Bundle", "%s Bundle").setSize(144L, 36L, 0.5F).setFilter(SubTag.ROPE);

	public static final MatCondition tie = new MatCondition("tie", "Tie", "%s Tie").setSize(36L, 18L, 3.0F).setFilter(SubTag.ROPE);
	public static final MatCondition handle = new MatCondition("handle", "Handle", "%s Handle").setSize(72L, 72L, 1.5F).setFilter(SubTag.HANDLE);

	public static final MatCondition adz_metal = new MatCondition("adz.metal", "adz", "Adz", "%s Adz").setSize(288L, 288L, 1.2F).setFilter(new IDataChecker.And(SubTag.METAL, SubTag.TOOL));
	public static final MatCondition adz_rock = new MatCondition("adz.rock", "adz", "Adz", "%s Adz").setSize(288L, 288L, 1.2F).setFilter(new IDataChecker.And(new IDataChecker.Or(SubTag.ROCK, SubTag.FLINT), SubTag.TOOL));

	public static final MatCondition hard_hammer_flint = new MatCondition("hard.hammer.flint", "hardHammer", "Hard Hammer", "%s Hammer").setSize(288L, 288L, 1.0F).setFilter(new IDataChecker.And(SubTag.FLINT, SubTag.TOOL));
	
	public static final MatCondition shovel_rock = new MatCondition("shovel.rock", "shovel", "Shovel", "%s Shovel").setSize(288L, 288L, 1.2F).setFilter(new IDataChecker.And(new IDataChecker.Or(SubTag.ROCK, SubTag.FLINT), SubTag.TOOL));
	public static final MatCondition shovel_metal = new MatCondition("shovel.metal", "shovel", "Shovel", "%s Shovel").setSize(144L, 144L, 1.2F).setFilter(new IDataChecker.And(SubTag.METAL, SubTag.TOOL));
	
	public static final MatCondition spade_hoe_rock = new MatCondition("spade.hoe.rock", "spadeHoe", "Spade-Hoe", "%s Spade-Hoe").setSize(288L, 288L, 1.3F).setFilter(new IDataChecker.And(SubTag.ROCK, SubTag.TOOL));

	public static final MatCondition spear_rock = new MatCondition("spear.rock", "spear", "Spear", "%s Spear").setSize(288L, 288L, 1.2F).setFilter(new IDataChecker.And(SubTag.ROCK, SubTag.TOOL));

	public static final MatCondition sickle_rock = new MatCondition("sickle.rock", "sickle", "Sickle", "%s Sickle").setSize(288L, 288L, 1.0F).setFilter(new IDataChecker.And(SubTag.ROCK, SubTag.TOOL));
	
	public static void init() {}
}