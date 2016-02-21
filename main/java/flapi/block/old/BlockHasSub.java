package flapi.block.old;

import farcore.block.BlockFle;
import net.minecraft.block.material.Material;

public abstract class BlockHasSub extends BlockFle
{
	protected BlockHasSub(Class<? extends ItemSubBlock> aItemClass, String aName,
			Material aMaterial) 
	{
		super(aItemClass, aName, aMaterial);
	}
	
	@Override
	protected boolean hasSub()
	{
		return true;
	}
}