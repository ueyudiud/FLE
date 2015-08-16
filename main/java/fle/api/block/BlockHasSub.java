package fle.api.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import fle.api.FleAPI;
import fle.api.world.BlockPos;

public abstract class BlockHasSub extends BlockFle implements IWorldNBT
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