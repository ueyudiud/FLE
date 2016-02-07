package flapi.item;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import farcore.FarCore;
import farcore.util.IUnlocalized;
import flapi.FleAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public abstract class ItemFle extends Item implements IUnlocalized
{
	protected String unlocalized;
	protected IIcon icon;
	
	protected ItemFle(String aUnlocalized)
	{
		unlocalized = "fle." + aUnlocalized;
		GameRegistry.registerItem(this, aUnlocalized, FleAPI.MODID);
	}
	protected ItemFle(String aUnlocalized, String aLocalized)
	{
		this(aUnlocalized);
		FarCore.lang.registerLocal(this, aLocalized);
	}
	
	@Override
	public final String getUnlocalized()
	{
		return getUnlocalizedName();
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

//	public abstract void damageItem(ItemStack stack, EntityLivingBase aUser, EnumDamageResource aReource, float aDamage);
	
	public NBTTagCompound setupNBT(ItemStack stack)
	{
		if(stack.stackTagCompound == null)
			stack.stackTagCompound = new NBTTagCompound();
		return stack.stackTagCompound;
	}
}