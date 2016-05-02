package farcore.lib.container;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.gui.IHasGui;
import net.minecraft.entity.player.EntityPlayer;
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
		return ID >= 0 ? 
				((ID & 0x256) != 0 ?
						((IHasGui) world.getBlock(x, y, z)).openContainer(ID, player, world, x, y, z) : 
							((IHasGui) world.getTileEntity(x, y, z)).openContainer(ID, player, world, x, y, z)) :
								openServerGui(-ID, player, world, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return ID >= 0 ? 
					((ID & 0x256) != 0 ?
							((IHasGui) world.getBlock(x, y, z)).openGUI(ID, player, world, x, y, z) : 
								((IHasGui) world.getTileEntity(x, y, z)).openGUI(ID, player, world, x, y, z)) :
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