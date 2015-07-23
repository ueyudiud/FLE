package fla.core.tech;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import fla.api.FlaAPI;
import fla.api.tech.Technology;

public class GuiTechBook extends GuiScreen
{
	private EntityPlayer player;
	
	public GuiTechBook(EntityPlayer player, ItemStack stack, boolean flag)
    {
        this.player = player;
	}
}