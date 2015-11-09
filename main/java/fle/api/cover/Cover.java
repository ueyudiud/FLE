package fle.api.cover;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.te.TEBase;

public class Cover
{
	protected final String uN;
	protected IIcon coverIcon;
	protected String tN;
	
	public Cover(String unlocalized)
	{
		uN = unlocalized;
	}
	
	public final String getUnlocalizedName()
	{
		return uN;
	}
	
	protected String getTextureName()
	{
		return tN == null ? "MISSING_ICON_COVER_" + getUnlocalizedName().toUpperCase() : tN;
	}
	
	public void registerIcon(IIconRegister register)
	{
		coverIcon = register.registerIcon(getTextureName());
	}
	
	public IIcon getCoverIcon()
	{
		return coverIcon;
	}
	
	public IIcon getCoverIcon(World world, int x, int y, int z, ForgeDirection dir)
	{
		return getCoverIcon();
	}
	
	public CoverTile createCoverTile(ForgeDirection dir, TEBase tile)
	{
		return new CoverTile(dir, tile, this);
	}

	public void onBlockBreak(World world, int x, int y, int z)
	{
		
	}
	
	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param player
	 * @param xPos
	 * @param yPos
	 * @param zPos
	 * @return True will skip block act.
	 */
	public boolean onBlockActive(World world, int x, int y, int z, EntityPlayer player, double xPos, double yPos, double zPos)
	{
		return false;
	}
	
	public void onBlockUpdate(World world, int x, int y, int z)
	{
		
	}
	
	public ItemStack getItemDrop()
	{
		return null;
	}
	
	public ArrayList<ItemStack> getItemDrop(World world, int x, int y, int z, Random rand)
	{
		ArrayList list = new ArrayList();
		ItemStack stack = getItemDrop();
		if(stack != null)
			list.add(stack.copy());
		return list;
	}
}