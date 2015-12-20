package fle.core.block.behaviour;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.block.interfaces.IBlockWithTileBehaviour;
import flapi.block.interfaces.IGuiBlock;
import fle.FLE;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerWorkbenchTire0;
import fle.core.gui.GuiWorkbenchTire0;

public class BehaviourWorkbenchTire0 extends BehaviourBase<BlockSubTile> implements IGuiBlock, IBlockWithTileBehaviour<BlockSubTile>
{
	@Override
	public boolean onBlockActivated(BlockSubTile block, World aWorld, int x,
			int y, int z, EntityPlayer aPlayer, ForgeDirection aSide,
			float xPos, float yPos, float zPos)
	{
		if(aWorld.isRemote) return aSide == ForgeDirection.UP;
		if(aSide == ForgeDirection.UP)
		{
			aPlayer.openGui(FLE.MODID, 0, aWorld, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer) 
	{
		return new ContainerWorkbenchTire0(aWorld, x, y, z, aPlayer.inventory);
	}
	
	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiWorkbenchTire0(aWorld, x, y, z, aPlayer);
	}

	@Override
	public TileEntity createNewTileEntity(BlockSubTile block, World aWorld,
			int aMeta)
	{
		return null;
	}	
}