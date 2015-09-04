package fle.core.block.behaviour;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerColdForging;
import fle.core.gui.GuiColdForging;
import fle.core.item.ItemFleSub;
import fle.core.te.TileEntityColdForgingPlatform;

public class BehaviourColdForgingPlatform extends BehaviourTile
{
	public BehaviourColdForgingPlatform() 
	{
		super("coldForgingPlatform", TileEntityColdForgingPlatform.class);
	}
	
	@Override
	public boolean onBlockActivated(BlockSubTile block, World aWorld, int x,
			int y, int z, EntityPlayer aPlayer, ForgeDirection aSide,
			float xPos, float yPos, float zPos)
	{
		if(aPlayer.isSneaking())
		{
			TileEntityColdForgingPlatform tile = (TileEntityColdForgingPlatform) aWorld.getTileEntity(x, y, z);
			if(tile != null)
			{
				for(int i = 0; i < tile.getSizeInventory() - 1; ++i)
				{
					block.dropBlockAsItem(aWorld, x, y, z, tile.getStackInSlot(i));
				}
			}
			block.breakBlock(aWorld, x, y, z, block, block.getDamageValue(aWorld, x, y, z));
			aWorld.setBlockToAir(x, y, z);
			aWorld.removeTileEntity(x, y, z);
			block.dropBlockAsItem(aWorld, x, y, z, ItemFleSub.a("stone_plate"));
			return true;
		}
		return super.onBlockActivated(block, aWorld, x, y, z, aPlayer, aSide, xPos,
				yPos, zPos);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerColdForging(aPlayer.inventory, (TileEntityColdForgingPlatform) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiColdForging(aPlayer, (TileEntityColdForgingPlatform) aWorld.getTileEntity(x, y, z));
	}
	
	@Override
	public ArrayList<ItemStack> getHarvestDrop(BlockSubTile block,
			World aWorld, int aMeta, TileEntity aTile, int aFotune) 
	{
		ArrayList<ItemStack> drops = new ArrayList();
		drops.add(ItemFleSub.a("stone_plate"));
		return drops;
	}
}