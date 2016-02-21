package flapi.block;

import farcore.block.BlockFle;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockFleTile<T extends TileEntity> extends BlockFle implements ITileEntityProvider
{
	protected BlockFleTile(Class<? extends ItemBlock> clazz,
			String unlocalized, Material Material)
	{
		super(clazz, unlocalized, Material);
	}
	protected BlockFleTile(String unlocalized, Material Material)
	{
		super(unlocalized, Material);
	}
	
	public abstract T createNewTileEntity(World world, int meta);
}