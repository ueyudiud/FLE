package fle.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import fle.api.util.ITextureLocation;
import fle.api.util.Register;

public class ItemFleFood extends ItemFleMetaBase
{
	private Register<IFoodStat> foodStats = new Register();

	protected ItemFleFood(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	    setHasSubtypes(true);
	    setMaxDamage(0);
	}
	
	public final ItemFleFood addSubItem(int aMetaValue, String name, ITextureLocation aLocate, IItemBehaviour<ItemFleMetaBase> aBehavior, IFoodStat<ItemFleFood> aFoodBehavior)
	{
		if ((aMetaValue < 0) || (aMetaValue >= OreDictionary.WILDCARD_VALUE ))
		{
			return this;
	    }
		this.addSubItem(aMetaValue, name, aLocate, aBehavior);
		if(aFoodBehavior != null)
			foodStats.register(Short.valueOf((short)aMetaValue), aFoodBehavior, name);
	    return this;
	}

	public boolean useStandardMetaItemRenderer()	
	{
		return true;
	}
	
	public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer)
	{
		IFoodStat tStat = (IFoodStat)foodStats.get(Short.valueOf((short)getDamage(aStack)));
		if ((tStat != null) && (aPlayer.canEat(tStat.alwaysEdible(this, aStack, aPlayer)))) 
		{
			aPlayer.setItemInUse(aStack, 32);
	    }
	    return super.onItemRightClick(aStack, aWorld, aPlayer);
	}
	  
	public int getMaxItemUseDuration(ItemStack aStack)
	{
		return foodStats.get(Short.valueOf((short)getDamage(aStack))) == null ? 0 : foodStats.get(Short.valueOf((short)getDamage(aStack))).getEatTick(this, aStack);
	}
	  
	public EnumAction getItemUseAction(ItemStack aStack)
	{
		IFoodStat tStat = (IFoodStat)this.foodStats.get(Short.valueOf((short)getDamage(aStack)));
	    return tStat == null ? EnumAction.none : tStat.getFoodAction(this, aStack);
	}
	  
	public final ItemStack onEaten(ItemStack aStack, World aWorld, EntityPlayer aPlayer)
	{
		IFoodStat tStat = (IFoodStat)this.foodStats.get(Short.valueOf((short)getDamage(aStack)));
	    if (tStat != null)
	    {
	    	aPlayer.getFoodStats().addStats(tStat.getFoodLevel(this, aStack, aPlayer), tStat.getSaturation(this, aStack, aPlayer));
	    }
	    tStat.onEaten(this, aStack, aPlayer);
	    return aStack;
	}
	  
	public int getItemEnchantability()
	{
		return 0;
	}
	  
	public boolean isBookEnchantable(ItemStack aStack, ItemStack aBook)
	{
		return false;
	}
	  
	public boolean getIsRepairable(ItemStack aStack, ItemStack aMaterial)
	{
		return false;
	}
}