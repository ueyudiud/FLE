package farcore.lib.item;

import java.util.List;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.IRegisteredNameable;
import farcore.lib.util.IconHook;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.UnlocalizedList;
import farcore.util.U;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBase extends Item implements IRegisteredNameable
{
	private boolean hasToolTip;
	protected final String modid;
	protected final String unlocalized;

	protected ItemBase(String unlocalized)
	{
		this(U.Mod.getActiveModID(), unlocalized);
	}
	protected ItemBase(String modid, String unlocalized)
	{
		this(modid, unlocalized, null, null);
	}
	protected ItemBase(String modid, String unlocalized, String localName, String localToolTip)
	{
		this.modid = modid;
		this.unlocalized = unlocalized;
		GameRegistry.registerItem(this, unlocalized, modid);
		if(localName != null)
		{
			LanguageManager.registerLocal(getUnlocalizedName(0) + ".name", localName);
		}
		if(localToolTip != null)
		{
			hasToolTip = true;
			LanguageManager.registerLocal(getUnlocalizedTooltip(0) + ".tooltip", localToolTip);
		}
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return modid + "." + unlocalized;
	}

	public String getUnlocalizedName(int meta)
	{
		return hasSubtypes ? getUnlocalizedName() + "@" + meta : 
			getUnlocalizedName();
	}

	public String getUnlocalizedTooltip(int meta)
	{
		return hasSubtypes ? getUnlocalizedName() + "@" + meta : 
			getUnlocalizedName();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName(getDamage(stack));
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return FarCore.translateToLocal(getUnlocalizedName(stack) + ".name");
	}
	
	public String getFirstDisplayTooltip(int meta)
	{
		return FarCore.translateToLocal(getUnlocalizedTooltip(meta) + ".tooltip");
	}

	@SideOnly(Side.CLIENT)
	public final void registerIcons(IIconRegister register)
	{
		IconHook.set(register);
		IconHook.instance.push(this);
		registerIcons(IconHook.instance);
		IconHook.instance.pop();
		IconHook.unset();
	}

	@SideOnly(Side.CLIENT)
	protected void registerIcons(INamedIconRegister register)
	{
		register.registerIcon(null, getIconString());
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass)
	{
		IconHook.instance.push(this);
		IIcon icon = getIcon(IconHook.instance, stack, pass);
		IconHook.instance.pop();
		return icon == null ? FarCore.voidItemIcon : icon;
	}

	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(INamedIconRegister register, ItemStack stack, int pass)
	{
		return getIconFromDamageForRenderPass(register, getDamage(stack), pass);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		IconHook.instance.push(this);
		IIcon icon = getIcon(IconHook.instance, stack, renderPass, player, usingItem, useRemaining);
		IconHook.instance.pop();
		return icon == null ? FarCore.voidItemIcon : icon;
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(INamedIconRegister register, ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		return getIcon(register, stack, renderPass);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return getIconFromDamageForRenderPass(meta, 0);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
	{
		IconHook.instance.push(this);
		IIcon icon = getIconFromDamageForRenderPass(IconHook.instance, meta, pass);
		IconHook.instance.pop();
		return icon == null ? FarCore.voidItemIcon : icon;
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIconFromDamageForRenderPass(INamedIconRegister register, int meta, int pass)
	{
		return register.getIconFromName(null);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack)
	{
		return getIcon(stack, 0);
	}
	
	@Override
	public String getRegisteredName()
	{
		return GameData.getItemRegistry().getNameForObject(this);
	}
	
	@SideOnly(Side.CLIENT)
	public final void addInformation(ItemStack stack, EntityPlayer player, List list, boolean F3H)
	{
		addUnlocalInfomation(stack, player, new UnlocalizedList(list), F3H);
	}
	
	@SideOnly(Side.CLIENT)
	public void addUnlocalInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean F3H)
	{
		if(hasToolTip)
		{
			list.add(getUnlocalizedTooltip(getDamage(stack)) + ".tooltip");
		}
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		return null;
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return getContainerItem(stack) != null;
	}
	
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack)
	{
		return super.doesContainerItemLeaveCraftingGrid(stack);
	}
}