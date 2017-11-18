/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import farcore.lib.block.material.MaterialIce;
import farcore.lib.block.material.MaterialLog;
import farcore.lib.block.material.MaterialOre;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;

/**
 * All kinds of material may used in Far Core and it's child mod.
 * 
 * @author ueyudiud
 */
public class Materials
{
	public static final Material	AIR			= Material.AIR;
	public static final Material	GAS			= new MaterialTransparent(MapColor.AIR);
	public static final Material	ROCK		= Material.ROCK;
	public static final Material	SAND		= Material.SAND;
	/**
	 * Metal material, it is just
	 * {@link net.minecraft.block.material.Material#IRON}.
	 */
	public static final Material	METALIC		= Material.IRON;
	/**
	 * Tree log material, different from
	 * {@link net.minecraft.block.material.Material#WOOD}, this one is
	 * tool-required.
	 */
	public static final Material	LOG			= new MaterialLog();
	public static final Material	WOOD		= Material.WOOD;
	public static final Material	PLANT		= Material.PLANTS;
	public static final Material	VINE		= Material.VINE;
	public static final Material	DIRT		= Material.GROUND;
	public static final Material	DIRT_GRASSY	= Material.GRASS;
	public static final Material	CLAY		= Material.CLAY;
	public static final Material	ORE			= new MaterialOre();
	
	public static final Material	GLASS	= Material.GLASS;
	/**
	 * Ice material, different from
	 * {@link net.minecraft.block.material.Material#ICE}, this one is
	 * tool-required.
	 */
	public static final Material	ICE		= new MaterialIce();
	
	public static final Material	WATER	= Material.WATER;
	public static final Material	LAVA	= Material.LAVA;
	
	public static final Material	FIRE	= Material.FIRE;
	/**
	 * Pottery material, use vanilla rock now, will be changed in the feature.
	 */
	public static final Material	POTTERY	= Material.ROCK;
}
