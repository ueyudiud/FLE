package fla.api.tech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class Page
{
	protected ResourceLocation locate;
	
	public Page() 
	{
		
	}
	
	public void setTextureName(String str1, String str2)
	{
		locate = new ResourceLocation(str1, str2);
	}
	
	public ResourceLocation getTextureLocation()
	{
		return locate;
	}
	
	public abstract IInventory getStacks();
	
	public abstract int getXPosFromId(int id);
	
	public abstract int getYPosFromId(int id);
	
	public abstract void drawOther(IPageGui gui);
}