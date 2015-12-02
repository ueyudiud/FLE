package fle.core.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import fle.api.block.BlockWithStaticTile;
import fle.api.block.ItemFleBlock;
import fle.api.te.TEStatic;

public class BlockFleOre extends BlockWithStaticTile
{
	protected BlockFleOre(Class<? extends ItemFleBlock> aItemClass,
			String aName, String aLocalized, Material aMaterial)
	{
		super(aItemClass, aName, aLocalized, aMaterial);	
	}

	@Override
	public TEStatic createNewTileEntity(World aWorld, int aMeta)
	{
		return null;
	}
}