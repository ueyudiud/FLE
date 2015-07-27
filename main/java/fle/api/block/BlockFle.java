package fle.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockFle extends Block
{
	protected final String unlocalizedName;
	  
	protected BlockFle(Class<? extends ItemBlock> aItemClass, String aName, Material aMaterial)
	{
		super(aMaterial);
		setBlockName(unlocalizedName = aName);
	    GameRegistry.registerBlock(this, aItemClass, getUnlocalizedName());
	}

}
