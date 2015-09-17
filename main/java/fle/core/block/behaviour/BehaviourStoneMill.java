package fle.core.block.behaviour;

import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerStoneMill;
import fle.core.gui.GuiStoneMill;
import fle.core.te.TileEntityStoneMill;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BehaviourStoneMill extends BehaviourTile
{
	public BehaviourStoneMill()
	{
		super("stoneMill", TileEntityStoneMill.class);
	}
	
	@Override
	public boolean onBlockActivated(BlockSubTile block, World aWorld, int x,
			int y, int z, EntityPlayer aPlayer, ForgeDirection aSide,
			float xPos, float yPos, float zPos)
	{
		if(aSide == ForgeDirection.UP)
		{
			if(aWorld.getTileEntity(x, y, z) instanceof TileEntityStoneMill)
			{
				((TileEntityStoneMill) aWorld.getTileEntity(x, y, z)).onPower();
				return true;
			}
		}
		return super.onBlockActivated(block, aWorld, x, y, z, aPlayer, aSide, xPos,
				yPos, zPos);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerStoneMill(aPlayer.inventory, (TileEntityStoneMill) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiStoneMill(aPlayer, (TileEntityStoneMill) aWorld.getTileEntity(x, y, z));
	}
}