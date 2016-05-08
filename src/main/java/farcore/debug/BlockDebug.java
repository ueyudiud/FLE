package farcore.debug;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDebug extends BlockContainer
{
	public BlockDebug() 
	{
		super(Material.rock);
		GameRegistry.registerBlock(this, "ydivgwjidydi");
		GameRegistry.registerTileEntity(TileEntityDebug.class, "cyuwiffeudjdnwi");
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return "fle.debug";
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityDebug();
	}
}