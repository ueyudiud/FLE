package fle.core.block.behaviour;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import flapi.block.interfaces.IDebugableBlock;
import flapi.material.IMolecular;
import flapi.util.FleValue;
import flapi.world.BlockPos;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerCeramicFurnace;
import fle.core.gui.GuiCeramicFurnace;
import fle.core.te.argil.TileEntityCeramicFurnaceCrucible;

public class BehaviourCeramicFurnaceCrucible extends BehaviourTile implements IDebugableBlock
{
	public BehaviourCeramicFurnaceCrucible()
	{
		super("ceramicFurnaceCrucible", TileEntityCeramicFurnaceCrucible.class);
	}
	
	@Override
	public void onBlockBreak(BlockSubTile block, World aWorld, int x, int y,
			int z, Block aBlock, int aMeta)
	{
		if(aWorld.isRemote) return;
		if (aWorld.getTileEntity(x, y, z) instanceof TileEntityCeramicFurnaceCrucible)
		{
			TileEntityCeramicFurnaceCrucible tile = (TileEntityCeramicFurnaceCrucible) aWorld.getTileEntity(x, y, z);
			for(int i = 0; i < tile.getSizeInventory(); ++i)
			{
				block.dropBlockAsItem(aWorld, x, y, z, tile.getStackInSlot(i));
			}
		}
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerCeramicFurnace(new BlockPos(aWorld, x, y, z), aPlayer.inventory);
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiCeramicFurnace(aWorld, x, y, z, aPlayer);
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		if (aWorld.getTileEntity(x, y, z) instanceof TileEntityCeramicFurnaceCrucible)
		{
			aList.add("=====================Contain=====================");
			TileEntityCeramicFurnaceCrucible tile = (TileEntityCeramicFurnaceCrucible) aWorld.getTileEntity(x, y, z);
			Map<IMolecular, Integer> wh = tile.getContainerMap();
			double size = wh.size();
			for(Entry<IMolecular, Integer> e : wh.entrySet())
			{
				if(e.getKey() == null) continue;
				if((double) e.getValue().intValue() / size < 0.03125F) continue;
				aList.add(String.format("%s : %s", e.getKey().getChemName(), FleValue.format_progress.format_c((double) e.getValue().intValue() / size)));
			}
			for(int a : tile.getProgress())
			{
				aList.add("Progress : " + a);
			}
		}
	}
}