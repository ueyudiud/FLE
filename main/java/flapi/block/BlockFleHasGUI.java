package flapi.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import flapi.FleAPI;

public abstract class BlockFleHasGUI extends BlockFle
{
	protected BlockFleHasGUI(String unlocalized, Material Material)
	{
		super(unlocalized, Material);	
	}

	protected BlockFleHasGUI(Class<? extends ItemBlock> clazz,
			String unlocalized, Material Material)
	{
		super(clazz, unlocalized, Material);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float xPos, float yPos, float zPos)
	{
		if(world.isRemote) return true;
		else if(canOpenGUI(world, x, y, z, player, side, xPos, yPos, zPos))
		{
			player.openGui(getMod(), getGuiId(world, x, y, z, side), world, x, y, z);
			return true;
		}
		return false;
	}
	
	protected boolean canOpenGUI(World world, int x, int y, int z,
			EntityPlayer player, int side, float xPos, float yPos, float zPos)
	{
		return true;
	}
	
	protected Object getMod()
	{
		return FleAPI.MODID;
	}
	
	protected int getGuiId(World world, int x, int y, int z, int side)
	{
		return 0;
	}

	public abstract Container getContainer(int ID, EntityPlayer player, World world, int x, int y, int z);
	public abstract GuiContainer getGUI(int ID, EntityPlayer player, World world, int x, int y, int z);
}