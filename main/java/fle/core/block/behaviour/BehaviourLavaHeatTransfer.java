package fle.core.block.behaviour;

import java.util.List;
import java.util.Random;

import fle.api.FleValue;
import fle.api.block.IDebugableBlock;
import fle.core.block.BlockSubTile;
import fle.core.item.ItemFleSub;
import fle.core.te.TileEntityLavaHeatTransfer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BehaviourLavaHeatTransfer extends BehaviourTile implements IDebugableBlock
{
	public BehaviourLavaHeatTransfer()
	{
		super("LavaHeatTransfer", TileEntityLavaHeatTransfer.class);
	}
	
	@Override
	public boolean onBlockActivated(BlockSubTile block, World aWorld, int x,
			int y, int z, EntityPlayer aPlayer, ForgeDirection aSide,
			float xPos, float yPos, float zPos)
	{
		TileEntityLavaHeatTransfer tile = (TileEntityLavaHeatTransfer) aWorld.getTileEntity(x, y, z);
		if(tile != null) 
		{
			if(tile.tick == 1000)
			{
				if(!aWorld.isRemote)
				{
					aPlayer.dropPlayerItemWithRandomChoice(ItemFleSub.a("chip_obsidian"), false);
					tile.tick = 0;
				}
				return true;
			}
			return false;
		}
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

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		TileEntityLavaHeatTransfer tile = (TileEntityLavaHeatTransfer) aWorld.getTileEntity(x, y, z);
		if(tile != null) 
		{
			aList.add("Lava amount : " + FleValue.format_L.format_c(tile.getTank(0).getFluidAmount()));
			aList.add("Obsidion Progress : " + FleValue.format_progress.format_c((double) tile.tick / 1000D));
		}
	}
	
	@Override
	public void onRenderUpdate(BlockSubTile block, World aWorld, int x, int y,
			int z, Random rand)
	{
		super.onRenderUpdate(block, aWorld, x, y, z, rand);
		TileEntityLavaHeatTransfer tile = (TileEntityLavaHeatTransfer) aWorld.getTileEntity(x, y, z);
		if(tile != null) 
		{
			if(tile.getFluidStackInTank(0) != null)
			{
				float f = (float)x + rand.nextFloat();
	            float f1 = (float)y + 0.5F + rand.nextFloat() * 6.0F / 16.0F;
	            float f2 = (float)z + rand.nextFloat();

	            aWorld.spawnParticle("smoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}