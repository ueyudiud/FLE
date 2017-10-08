/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TECustomName extends TEBase
{
	public String customName;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if(nbt.hasKey("customName"))
		{
			this.customName = nbt.getString("customName");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		if(this.customName != null)
		{
			nbt.setString("customName", this.customName);
		}
		
		return nbt;
	}
	
	/**
	 * Get the formatted ChatComponent that will be used for the sender's username in chat
	 */
	@Override
	public ITextComponent getDisplayName()
	{
		return hasCustomName() ? new TextComponentString(this.customName) :
			new TextComponentTranslation(getName());
	}
	
	public String getName()
	{
		return "";
	}
	
	public boolean hasCustomName()
	{
		return this.customName != null;
	}
}