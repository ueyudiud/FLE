package fle.resource.tile;

import net.minecraft.server.gui.IUpdatePlayerListBox;

import farcore.nbt.NBTLoad;
import farcore.nbt.NBTSave;
import farcore.substance.Substance;
import farcore.tileentity.TEBase;
import fle.init.Substance1;

public class TileEntitySand extends TEBase implements IUpdatePlayerListBox
{
	@NBTLoad(name = "sand")
	@NBTSave(name = "sand")
	public Substance sand = Substance1.$void;
	
	@Override
	public void update()
	{
		syncNBT();
	}
}