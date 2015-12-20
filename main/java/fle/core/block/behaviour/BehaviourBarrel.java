package fle.core.block.behaviour;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.util.IDebugable;
import fle.core.block.BlockSubTile;
import fle.core.te.TileEntityBarrel;

public class BehaviourBarrel extends BehaviourTile implements IDebugable
{
	public BehaviourBarrel()
	{
		super("fleBarrel", TileEntityBarrel.class);
	}
	
	@Override
	public boolean onBlockActivated(BlockSubTile block, World world, int x,
			int y, int z, EntityPlayer player, ForgeDirection side,
			float xPos, float yPos, float zPos)
	{
		if(world.isRemote) return true;
		else if(world.getTileEntity(x, y, z) instanceof TileEntityBarrel)
		{
			if(side == ForgeDirection.UP)
			{
				((TileEntityBarrel) world.getTileEntity(x, y, z)).onUseItemStack(player, player.getCurrentEquippedItem());
				return true;
			}
			String str = ((TileEntityBarrel) world.getTileEntity(x, y, z)).getOutputInfo();
			if(str != null)
			{
				player.addChatComponentMessage(new ChatComponentText("Like some " + str + " in barrel."));
			}
			else
			{
				player.addChatComponentMessage(new ChatComponentText("Like not finished brewing yet."));
			}
		}
		return false;
	}

	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer){return null;}
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer){return null;}

	@Override
	public void addInfomationToList(World world, int x, int y, int z,
			List<String> list)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityBarrel)
		{
			((TileEntityBarrel) world.getTileEntity(x, y, z)).addDebugInfo(list);
		}
	}
}