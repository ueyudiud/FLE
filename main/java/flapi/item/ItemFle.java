package flapi.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.registry.GameRegistry;
import flapi.FleAPI;
import flapi.enums.EnumDamageResource;

public abstract class ItemFle extends Item
{
	protected String unlocalized;
	protected String unlocalizedTooltip;
	protected IIcon icon;
	
	protected ItemFle(String aUnlocalized)
	{
		this(aUnlocalized, null);
	}
	protected ItemFle(String aUnlocalized, String aUnlocalizedTooltip)
	{
		unlocalized = "fle." + aUnlocalized;
		unlocalizedTooltip = aUnlocalizedTooltip;
		GameRegistry.registerItem(this, aUnlocalized, FleAPI.MODID);
	}
	protected ItemFle(String aUnlocalized, String aUnlocalizedTooltip, String aLocalized)
	{
		this(aUnlocalized, aUnlocalizedTooltip);
		FleAPI.langManager.registerLocal(getUnlocalizedName(), aLocalized);
	}
	
	@Override
	public final Item setUnlocalizedName(String aName)
	{
		return this;
	}
	
	@Override
	public final String getUnlocalizedName() 
	{
		return unlocalized;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack aStack)
	{
		return hasSubtypes ? getUnlocalizedName() + ":" + getMetaUnlocalizedName(getDamage(aStack)) : getUnlocalizedName();
	}
	
	public String getMetaUnlocalizedName(int aMetadata)
	{
		return String.valueOf(aMetadata);
	}

	public abstract void damageItem(ItemStack stack, EntityLivingBase aUser, EnumDamageResource aReource, float aDamage);
	
	public NBTTagCompound setupNBT(ItemStack stack)
	{
		if(stack.stackTagCompound == null)
			stack.stackTagCompound = new NBTTagCompound();
		return stack.stackTagCompound;
	}
}