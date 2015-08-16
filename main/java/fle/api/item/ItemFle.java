package fle.api.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.registry.GameRegistry;
import fle.api.FleAPI;
import fle.api.enums.EnumDamageResource;

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
		return hasSubtypes ? getUnlocalizedName() + ":" + getMetaUnlocalizedName(aStack.getItemDamage()) : getUnlocalizedName();
	}

	public ItemStack onDispense(IBlockSource aSource, ItemStack aStack)
	{
		EnumFacing enumfacing = BlockDispenser.func_149937_b(aSource.getBlockMetadata());
		IPosition iposition = BlockDispenser.func_149939_a(aSource);
		ItemStack itemstack1 = aStack.splitStack(1);
		BehaviorDefaultDispenseItem.doDispense(aSource.getWorld(), itemstack1, 6, enumfacing, iposition);
		return aStack;
	}
	
	public boolean hasSubs()
	{
		return hasSubtypes;
	}
	
	public String getMetaUnlocalizedName(int aMetadata)
	{
		return String.valueOf(aMetadata);
	}
	
	protected boolean isItemStackUsable(ItemStack aStack)
	{
		return true;
	}

	public abstract void damageItem(ItemStack stack, EntityLivingBase aUser, EnumDamageResource aReource, float aDamage);
	
	@Override
	public int getDamage(ItemStack aStack) 
	{
		return super.getDamage(aStack);
	}
	
	@Override
	public int getDisplayDamage(ItemStack aStack)
    {
        return getDamage(aStack);
    }
	
	@Override
	public int getMaxDamage(ItemStack aStack)
	{
		return super.getMaxDamage(aStack);
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack aStack) 
	{
		return super.getDurabilityForDisplay(aStack);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack aStack)
	{
		return hasSubtypes ? false : getDisplayDamage(aStack) != 0;
	}
	
	protected boolean isMetaDamagable(int aShowMeta)
	{
		return false;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack aItemStack)
	{
		return EnumAction.none;
	}
	  
	public short[] getRGBa(ItemStack aStack)
	{
		return new short[]{255, 255, 255, 255};
	}
	
	public NBTTagCompound setupNBT(ItemStack aStack)
	{
		if(aStack == null) return new NBTTagCompound();
		if(!aStack.hasTagCompound())
			aStack.setTagCompound(new NBTTagCompound());
		return aStack.getTagCompound();
	}
}