package fle.core.enums;

import farcore.item.enums.EnumItemSize;
import farcore.item.enums.EnumParticleSize;
import farcore.util.IUnlocalized;
import farcore.util.Part;

public enum EnumRockState implements IUnlocalized
{
	resource(EnumParticleSize.block), 
	cobble(EnumParticleSize.ingotic), 
	crush(EnumParticleSize.chunk);
	
	private final EnumParticleSize size;
	
	private EnumRockState(EnumParticleSize size)
	{
		this.size = size;
	}
	
	public String getUnlocalized()
	{
		return "state.rock." + name();
	}

	public Part part()
	{
		return Part.part("rock." + name(), size, Part.cube.resolution);
	}
}