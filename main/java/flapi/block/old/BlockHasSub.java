package flapi.block.old;

import net.minecraft.block.material.Material;

public abstract class BlockHasSub extends BlockFle
{
	protected BlockHasSub(Class<? extends ItemSubBlock> aItemClass, String aName,
			Material aMaterial) 
	{
		super(aItemClass, aName, aMaterial);
	}
	
	@Override
	public boolean hasSubs()
	{
		return true;
	}
}