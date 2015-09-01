package fle.core.block.behaviour;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.world.BlockPos;
import fle.core.block.BlockSubTile;
import fle.core.te.argil.TileEntityArgilItems;

public class BehaviourArgilItem extends BehaviourTile
{
	public BehaviourArgilItem()
	{
		super("ArgilItem", TileEntityArgilItems.class);
	}
	
	@Override
	public void onBlockPlacedBy(BlockSubTile block, World aWorld, int x, int y,
			int z, ItemStack aStack, EntityLivingBase aEntity)
	{
		;
	}
	
	@Override
	public boolean onBlockActivated(BlockSubTile block, World aWorld, int x,
			int y, int z, EntityPlayer aPlayer, ForgeDirection aSide,
			float xPos, float yPos, float zPos)
	{
		if(aWorld.getTileEntity(x, y, z) instanceof TileEntityArgilItems)
		{
			block.onEntityWalking(aWorld, x, y, y, aPlayer);
			block.dropBlockAsItem(aWorld, x, y, z, ((TileEntityArgilItems) aWorld.getTileEntity(x, y, z)).stack);
		}
		aWorld.setBlockToAir(x, y, z);
		block.breakBlock(aWorld, x, y, z, block, block.getDamageValue(aWorld, x, y, z));
		aWorld.removeTileEntity(x, y, z);
		return true;
	}
	
	@Override
	public void onBlockBreak(BlockSubTile block, World aWorld, int x, int y,
			int z, Block aBlock, int aMeta)
	{
		if(aWorld.getTileEntity(x, y, z) instanceof TileEntityArgilItems)
		{
			block.dropBlockAsItem(aWorld, x, y, z, ((TileEntityArgilItems) aWorld.getTileEntity(x, y, z)).stack);
		}
		super.onBlockBreak(block, aWorld, x, y, z, aBlock, aMeta);
	}
	
	@Override
	public ArrayList<ItemStack> getHarvestDrop(BlockSubTile block,
			World aWorld, int aMeta, TileEntity aTile, int aFotune)
	{
		return new ArrayList();
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