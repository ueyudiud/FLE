package fle.core.block.behaviour;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerStoneMill;
import fle.core.gui.GuiStoneMill;
import fle.core.recipe.RecipeHelper;
import fle.core.te.TileEntityStoneMill;

public class BehaviourStoneMill extends BehaviourTile
{
	public BehaviourStoneMill()
	{
		super("stoneMill", TileEntityStoneMill.class);
	}
	
	@Override
	public boolean canBlockStay(BlockSubTile block, World aWorld, int x, int y,
			int z)
	{
		return aWorld.getBlock(x, y - 1, z).isSideSolid(aWorld, x, y - 1, z, ForgeDirection.DOWN);
	}
	
	@Override
	public boolean onBlockActivated(BlockSubTile block, World world, int x,
			int y, int z, EntityPlayer aPlayer, ForgeDirection aSide,
			float xPos, float yPos, float zPos)
	{
		if(aSide == ForgeDirection.UP)
		{
			ItemStack stack = aPlayer.getCurrentEquippedItem();
			if(world.getTileEntity(x, y, z) instanceof TileEntityStoneMill)
			{
				TileEntityStoneMill tile = (TileEntityStoneMill) world.getTileEntity(x, y, z);
				if(aPlayer.getCurrentEquippedItem() != null && RecipeHelper.matchOutput(tile, 0, stack))
				{
					if(world.isRemote) return true;
					RecipeHelper.onOutputItemStack(tile, 0, stack.copy());
					aPlayer.destroyCurrentEquippedItem();
					return true;
				}
				else
				{
					((TileEntityStoneMill) world.getTileEntity(x, y, z)).onPower();
					return true;
				}
			}
		}
		return super.onBlockActivated(block, world, x, y, z, aPlayer, aSide, xPos,
				yPos, zPos);
	}
	
	@Override
	public void onBlockBreak(BlockSubTile block, World aWorld, int x, int y,
			int z, Block aBlock, int aMeta)
	{
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile instanceof IInventory)
		{
			for(int i = 0; i < ((IInventory) tile).getSizeInventory(); ++i)
			{
				block.dropBlockAsItem(aWorld, x, y, z, ((IInventory) tile).getStackInSlot(i));
			}
		}
	}
	
	@Override
	public void onFallenUpon(BlockSubTile block, World world, int x, int y,
			int z, Entity entity, float aHeight)
	{
		
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