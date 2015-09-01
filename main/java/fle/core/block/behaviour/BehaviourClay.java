package fle.core.block.behaviour;

import java.util.List;

import fle.api.FleValue;
import fle.api.block.IDebugableBlock;
import fle.core.block.BlockSubTile;
import fle.core.te.argil.TileEntityArgilUnsmelted;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BehaviourClay extends BehaviourTile implements IDebugableBlock
{
	public BehaviourClay()
	{
		super("ArgilUnsmelted", TileEntityArgilUnsmelted.class);
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

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile instanceof TileEntityArgilUnsmelted)
		{
			aList.add("Progress : " + FleValue.format_progress.format_c(((TileEntityArgilUnsmelted) tile).getProgress()));
		}
	}
}