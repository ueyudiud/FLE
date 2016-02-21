package flapi.item.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IFoodStat<E extends Item>
{
	public abstract int getFoodLevel(E item, ItemStack itemstack, EntityPlayer player);
	  
	public abstract float getSaturation(E item, ItemStack itemstack, EntityPlayer player);
	
	public abstract int getEatTick(E item, ItemStack itemstack);
	
	public abstract boolean alwaysEdible(E item, ItemStack itemstack, EntityPlayer player);
	
	public abstract boolean isRotten(E item, ItemStack itemstack, EntityPlayer player);
	  
	public abstract EnumAction getFoodAction(E item, ItemStack itemstack);
	  
	public abstract void onEaten(E item, ItemStack itemstack, EntityPlayer player);
}

