package fle.resource.block;

import flapi.block.BlockFleMultipassRender;
import flapi.block.item.ItemFleMultipassRender;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockResourceBase extends BlockFleMultipassRender
{
	protected BlockResourceBase(Class<? extends ItemFleMultipassRender> clazz, String unlocalized, Material Material)
	{
		super(clazz, unlocalized, Material);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z,
			int side)
	{
		return false;
	}
	
	@Override
	public boolean canProvidePower()
	{
		return false;
	}
}