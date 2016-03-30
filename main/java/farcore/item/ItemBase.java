package farcore.item;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import farcore.util.U;
import fle.api.FleAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class ItemBase extends Item
{
	protected String unlocalized;
	protected String unlocalizedTooltip;
	protected IIcon icon;
	
	protected ItemBase(String unlocalized)
	{
		this(unlocalized, null);
	}
	protected ItemBase(String unlocalized, String unlocalizedTooltip)
	{
		this(unlocalized, unlocalizedTooltip, U.Mod.getActiveModID());
	}
	protected ItemBase(String modid, String unlocalized, String unlocalizedTooltip)
	{
		this.unlocalized = "fle." + unlocalized;
		this.unlocalizedTooltip = "fle." + unlocalizedTooltip;
		GameRegistry.registerItem(this, unlocalized, modid);
	}
	
	@Override
	public final Item setUnlocalizedName(String name)
	{
		return this;
	}
	
	@Override
	public final String getUnlocalizedName() 
	{
		return unlocalized;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return hasSubtypes ? getUnlocalizedName() + ":" + getMetaUnlocalizedName(getDamage(stack)) : getUnlocalizedName();
	}
	
	public String getMetaUnlocalizedName(int metadata)
	{
		return String.valueOf(metadata);
	}
}