/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.blocks.terria;

import nebula.common.util.Properties.EnumStateName;
import net.minecraft.util.IStringSerializable;

/**
 * @author ueyudiud
 */
@EnumStateName("cover")
public enum EnumCoverType implements IStringSerializable
{
	NONE("none"), FROZEN("frozen"), GRASS("grass"), TUNDRA("tundra"), MYCELIUM("mycelium"), TUNDRA_FROZEN("tundra_frozen"), SNOW("snow"), FROZEN_SNOW("frozen_snow"), GRASS_SNOW("grass_snow"), TUNDRA_SNOW("tundra_snow"), TUNDRA_FROZEN_SNOW("tundra_frozen_snow"), MYCELIUM_SNOW("mycelium_snow");
	
	public static final EnumCoverType[] VALUES = values();
	
	static
	{
		setCover(NONE, SNOW);
		setCover(GRASS, GRASS_SNOW);
		setCover(TUNDRA, TUNDRA_SNOW);
		setCover(MYCELIUM, MYCELIUM_SNOW);
		setCover(FROZEN, FROZEN_SNOW);
		setCover(TUNDRA_FROZEN, TUNDRA_FROZEN_SNOW);
		FROZEN.noFrozen = FROZEN_SNOW.noFrozen = NONE;
		TUNDRA_FROZEN.noFrozen = TUNDRA_FROZEN_SNOW.noFrozen = TUNDRA;
		FROZEN.isFrozen = FROZEN_SNOW.isFrozen = TUNDRA_FROZEN.isFrozen = TUNDRA_FROZEN_SNOW.isFrozen = true;
	}
	
	static void setCover(EnumCoverType no, EnumCoverType snow)
	{
		no.noCover = snow.noCover = no;
		no.snowCover = snow.snowCover = snow;
		snow.isSnow = true;
	}
	
	String			name;
	EnumCoverType	noFrozen	= this;
	EnumCoverType	noCover		= this;
	EnumCoverType	snowCover	= this;
	boolean			isSnow;
	boolean			isFrozen;
	
	EnumCoverType(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
	
	public EnumCoverType getNoCover()
	{
		return this.noCover;
	}
	
	public EnumCoverType getSnowCover()
	{
		return this.snowCover;
	}
}
