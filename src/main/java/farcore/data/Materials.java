/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import farcore.lib.block.material.MaterialIce;
import farcore.lib.block.material.MaterialLog;
import farcore.lib.block.material.MaterialOre;
import net.minecraft.block.material.Material;

/**
 * @author ueyudiud
 */
public class Materials
{
	public static final Material AIR = Material.AIR;
	public static final Material ROCK = Material.ROCK;
	public static final Material METALIC = Material.IRON;
	public static final Material LOG = new MaterialLog();
	public static final Material WOOD = Material.WOOD;
	public static final Material PLANT = Material.PLANTS;
	public static final Material DIRT = Material.GROUND;
	public static final Material DIRT_GRASSY = Material.GRASS;
	public static final Material ORE = new MaterialOre();
	
	public static final Material ICE = new MaterialIce();
	
	public static final Material WATER = Material.WATER;
	public static final Material LAVA = Material.LAVA;
	
	public static final Material FIRE = Material.FIRE;
	
	public static final Material POTTERY = Material.ROCK;
}