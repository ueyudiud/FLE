package fle.core.block.behaviour;

import fle.core.block.BlockSubTile;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BehaviourClay extends BehaviourTile
{
	public BehaviourClay(Class<? extends TileEntity> clazz)
	{
		super(clazz);
	}
	
	@Override
	public boolean onBlockActivated(BlockSubTile block, World aWorld, int x,
			int y, int z, EntityPlayer aPlayer, ForgeDirection aSide,
			float xPos, float yPos, float zPos)	{return false;}
	
	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer) {return null;}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer) {return null;}
}
