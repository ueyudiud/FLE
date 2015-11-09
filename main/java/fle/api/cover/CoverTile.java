package fle.api.cover;

import java.util.ArrayList;
import java.util.Random;

import fle.api.te.TEBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CoverTile
{
	protected ForgeDirection dir;
	protected TEBase tile;
	protected Cover cover;
	
	public CoverTile(ForgeDirection aDir, TEBase aTile, Cover aCover)
	{
		dir = aDir;
		tile = aTile;
		cover = aCover;
	}
	
	public Cover getCover()
	{
		return cover;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		
	}

	public void onBlockBreak()
	{
		cover.onBlockBreak(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
		ArrayList<ItemStack> drops = getItemDrop();
		if(drops != null)
			for(ItemStack stack : drops)
			{
				tile.dropItem(stack);
			}
	}
	
	public boolean onBlockActive(EntityPlayer player, double xPos, double yPos, double zPos)
	{
		return cover.onBlockActive(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, player, xPos, yPos, zPos);
	}
	
	public void onBlockUpdate()
	{
		cover.onBlockUpdate(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public ArrayList<ItemStack> getItemDrop()
	{
		return cover.getItemDrop(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.rand);
	}
}