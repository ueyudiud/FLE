package fle.resource.tile;

import net.minecraft.server.gui.IUpdatePlayerListBox;

import farcore.nbt.NBTLoad;
import farcore.nbt.NBTSave;
import farcore.substance.Substance;
import farcore.tileentity.TEBase;
import flapi.enums.EnumRockSize;
import fle.init.Substance1;

public class TileEntityStoneChip extends TEBase implements IUpdatePlayerListBox
{
	@NBTLoad(name = "rock")
	@NBTSave(name = "rock")
	public Substance rock = Substance1.$void;
	@NBTLoad(name = "size")
	@NBTSave(name = "size")
	public EnumRockSize size = EnumRockSize.small;
	
	@Override
	public void update()
	{
		syncNBT();
	}
}