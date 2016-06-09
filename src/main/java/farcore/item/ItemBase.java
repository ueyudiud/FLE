package farcore.item;

import java.util.List;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.interfaces.item.ILocalizedRegisterListener;
import farcore.util.LanguageManager;
import farcore.util.U;
import fle.api.FleAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemBase extends Item implements ILocalizedRegisterListener
{
	protected String localized;
	protected String unlocalized;
	protected String unlocalizedTooltip;
	@SideOnly(Side.CLIENT)
	protected IIcon icon;
	
	protected ItemBase(String unlocalized)
	{
		this(unlocalized, null);
	}
	protected ItemBase(String unlocalized, String unlocalizedTooltip)
	{
		this.unlocalized = "fle." + unlocalized;
		this.unlocalizedTooltip = "fle." + unlocalizedTooltip;
		GameRegistry.registerItem(this, unlocalized);
	}
	protected ItemBase(String modid, String unlocalized, String unlocalizedTooltip)
	{
		this.unlocalized = "fle." + unlocalized;
		this.unlocalizedTooltip = "fle." + unlocalizedTooltip;
		GameRegistry.registerItem(this, unlocalized, modid);
	}
	
	public void registerLocalizedName(LanguageManager manager)
	{
		if(localized != null)
		{
			manager.registerLocal(unlocalized, localized);
		}
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
	
	protected Object[] getTranslateObject(ItemStack stack)
	{
		return new Object[0];
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return FarCore.translateToLocal(getUnlocalizedName(stack), getTranslateObject(stack));
	}
	
	public String getMetaUnlocalizedName(int metadata)
	{
		return String.valueOf(metadata);
	}
	
	@Override
	public MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer player,
			boolean checkFluid)
	{
		return super.getMovingObjectPositionFromPlayer(world, player, checkFluid);
	}
}