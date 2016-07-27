package farcore.data;

import farcore.lib.material.MatCondition;
import farcore.lib.util.SubTag;

public class MC
{
	public static final MatCondition LATTICE = new MatCondition("lattice", "Lattice", "%s Lattice").setSize(1L).setFilter(SubTag.TRUE);
	
	public static final MatCondition stone = new MatCondition("stone", "Rock", "%s").setSize(5184L, 5184L).setFilter(SubTag.ROCK);
	public static final MatCondition cobble = new MatCondition("cobble", "Cobble", "%s Cobble").setSize(5184L, 576L).setFilter(SubTag.ROCK);
	public static final MatCondition brick = new MatCondition("brick", "Brick", "%s Brick").setSize(5184L, 1296L, 1.5F).setFilter(SubTag.ROCK);
	public static final MatCondition brickCraving = new MatCondition("brickCraving", "Brick", "Craving %s Brick").setSize(5184L, 5184L, 1.2F).setFilter(SubTag.ROCK);
	public static final MatCondition seed = new MatCondition("seed", "Seed", "%s Seed").setSize(72L, 24L).setFilter(SubTag.CROP);
	public static final MatCondition chip = new MatCondition("chip", "Chip", "%s Chip").setSize(576L, 576L, 4.0F).setFilter(SubTag.ROCK);
	public static final MatCondition sapling = new MatCondition("sapling", "Sapling", "%s Sapling").setSize(640L, 512L, 25.0F).setFilter(SubTag.WOOD);

	public static void init(){	}
}