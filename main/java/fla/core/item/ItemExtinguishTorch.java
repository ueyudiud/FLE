package fla.core.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import fla.core.tileentity.TileEntityOilLamp;

public class ItemExtinguishTorch extends ItemBase
{
	public ItemExtinguishTorch() 
	{
		setCreativeTab(CreativeTabs.tabTools);
		setMaxStackSize(16);
	}
	
	@Override
	public boolean onItemUse(ItemStack item, EntityPlayer player,
			World world, int x, int y, int z,
			int side, float xPos, float yPos,
			float zPos)
	{
		Block block = world.getBlock(x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityOilLamp)
		{
			TileEntityOilLamp te = (TileEntityOilLamp) tile;
			if(te.isBurning() && te.fluid != null)
			{
				--item.stackSize;
				if(item.stackSize == 0)
				{
					player.setCurrentItemOrArmor(0, null);
				}
				player.dropPlayerItemWithRandomChoice(new ItemStack(Blocks.torch), false);
				
				te.fluid.decrStack(5);
			}
		}
		return false;
	}
}