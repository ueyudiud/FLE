package fle.resource.tile;

import net.minecraft.server.gui.IUpdatePlayerListBox;

import farcore.block.EnumRockState;
import farcore.nbt.NBTLoad;
import farcore.nbt.NBTSave;
import farcore.substance.Substance;
import farcore.tileentity.TEBase;
import fle.init.Substance1;

public class TileEntityRock extends TEBase implements IUpdatePlayerListBox
{
	@NBTLoad(name = "rock")
	@NBTSave(name = "rock")
	public Substance rock = Substance1.$void;
	@NBTLoad(name = "state")
	@NBTSave(name = "state")
	public EnumRockState state = EnumRockState.resource;
	
	@Override
	public void update()
	{
		syncNBT();
	}
}