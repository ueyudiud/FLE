package farcore.lib.container;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.gui.IHasGui;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiFactory implements IGuiHandler
{
	public final String modid;
	
	protected GuiFactory(String modid)
	{
		this.modid = modid;
		NetworkRegistry.INSTANCE.registerGuiHandler(modid, this);
	}
	
	public void openGui(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		player.openGui(modid, id, world, x, y, z);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		Block block;
		TileEntity tile;
		return ID >= 0 ? 
				((block = world.getBlock(x, y, z)) instanceof IHasGui ?
						((IHasGui) block).openContainer(ID, player, world, x, y, z) : 
							((tile = world.getTileEntity(x, y, z)) instanceof IHasGui) ? 
							((IHasGui) tile).openContainer(ID, player, world, x, y, z) : null) :
								openServerGui(-ID, player, world, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		Block block;
		TileEntity tile;
		return ID >= 0 ? 
				((block = world.getBlock(x, y, z)) instanceof IHasGui ?
						((IHasGui) block).openGUI(ID, player, world, x, y, z) : 
							((tile = world.getTileEntity(x, y, z)) instanceof IHasGui) ? 
							((IHasGui) tile).openGUI(ID, player, world, x, y, z) : null) :
								openClientGui(-ID, player, world, x, y, z);
		
	}
	
	protected Object openServerGui(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	protected Object openClientGui(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}