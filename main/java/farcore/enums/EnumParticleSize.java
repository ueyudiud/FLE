package farcore.enums;

import net.minecraft.world.chunk.Chunk;

public enum EnumParticleSize
{
	block(true),//Such as log.
	/**
	 * This type means this item can sort size.
	 * Use in tools(hammer, axe, etc), machine part(wire, pipe, etc).
	 */
	unused(true),
	ingotic(true),//Such as metal ingot.
	chunk(true),
	nuggetic(true),//Such as gravel, metal nugget.
	dust(false),//Such as sand.
	colloid(false),//Such as flour, dirt.
	thick(false),//Such as oil.
	fluid(false);//Such as water.
	
	public final boolean canCurrent;
	
	EnumParticleSize(boolean flag)
	{
		this.canCurrent = flag;
	}
}