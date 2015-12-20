package fle.core.block.behaviour;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.core.block.BlockSubTile;
import fle.core.te.argil.TileEntityCeramicBricks;

public class BehaviourCeramicBricks extends BehaviourTile
{
	public BehaviourCeramicBricks()
	{
		super("ceramicBricks", TileEntityCeramicBricks.class);
	}

	@Override
	public boolean onBlockActivated(BlockSubTile block, World aWorld, int x,
			int y, int z, EntityPlayer aPlayer, ForgeDirection aSide,
			float xPos, float yPos, float zPos)
	{
		return false;
	}
	
	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return null;
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return null;
	}
}