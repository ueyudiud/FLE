package farcore.debug;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDebug1 extends BlockContainer
{
	public BlockDebug1() 
	{
		super(Material.rock);
		GameRegistry.registerBlock(this, "dneivuaoqudo");
		GameRegistry.registerTileEntity(TileEntityDebug1.class, "dueofufuwjx");
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return "fle.debug1";
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityDebug1();
	}
}