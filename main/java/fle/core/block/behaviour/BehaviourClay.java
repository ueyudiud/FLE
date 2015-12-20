package fle.core.block.behaviour;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.block.interfaces.IDebugableBlock;
import flapi.util.FleValue;
import fle.core.block.BlockSubTile;
import fle.core.te.argil.TileEntityArgilUnsmelted;

public class BehaviourClay extends BehaviourTile implements IDebugableBlock
{
	public BehaviourClay()
	{
		super("ArgilUnsmelted", TileEntityArgilUnsmelted.class);
	}
	
	@Override
	public boolean canBlockStay(BlockSubTile block, World aWorld, int x, int y,
			int z)
	{
		return aWorld.getBlock(x, y - 1, z).isSideSolid(aWorld, x, y - 1, z, ForgeDirection.DOWN);
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