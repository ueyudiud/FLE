package fle.resource.tile;

import net.minecraft.server.gui.IUpdatePlayerListBox;

import farcore.block.EnumRockState;
import farcore.nbt.NBTLoad;
import farcore.nbt.NBTSave;
import farcore.substance.Substance;
import farcore.tileentity.TEBase;
import fle.init.Substance1;

public class TileEntityMineral extends TEBase implements IUpdatePlayerListBox
{
	@NBTLoad(name = "ore")
	@NBTSave(name = "ore")
	public Substance ore = Substance1.$void;
	@NBTLoad(name = "rock")
	@NBTSave(name = "rock")
	public Substance rock = Substance1.$void;
	@NBTLoad(name = "state")
	@NBTSave(name = "state")
	public EnumRockState state = EnumRockState.resource;
	@NBTLoad(name = "amount")
	@NBTSave(name = "amount")
	public int amount;
	
	@Override
	public void update()
	{
		syncNBT();
	}
}