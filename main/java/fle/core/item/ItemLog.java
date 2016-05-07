package fle.core.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.enums.EnumItem;
import farcore.enums.EnumItem.IInfomationable;
import farcore.interfaces.item.IItemInfo;
import farcore.item.ItemBase;
import farcore.item.ItemSubBehavior;
import farcore.lib.render.item.ItemRenderInfoSimple;
import farcore.lib.substance.SubstanceWood;
import farcore.util.U;
import farcore.util.V;
import fle.api.item.behavior.BehaviorBase;
import fle.load.Langs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemLog extends ItemBase implements IInfomationable
{
	@SideOnly(Side.CLIENT)
	protected Map<Integer, IIcon> icons;
	
	public ItemLog()
	{
		super("log");
		setTextureName("fle:log");
		maxStackSize = 1;
		hasSubtypes = true;
		EnumItem.log.set(new ItemStack(this));
	}
	
	@Override
	public String getMetaUnlocalizedName(int metadata)
	{
		return SubstanceWood.getWoods().name(metadata);
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(SubstanceWood wood : SubstanceWood.getWoods())
		{
			if(wood != SubstanceWood.WOOD_VOID)
			{
				list.add(setLogSize(new ItemStack(item, 1, wood.getID()), 16));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		icons = new HashMap();
		for(SubstanceWood wood : SubstanceWood.getWoods())
		{
			icons.put(Integer.valueOf(wood.getID()), register.registerIcon(getIconString() + "/" + wood.getName()));
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		IIcon icon = icons.get(meta);
		if(icon == null)
		{
			icon = V.voidItemIcon;
		}
		return icon;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		super.addInformation(stack, player, list, flag);
		list.add(FarCoreSetup.lang.translateToLocal(Langs.infoLogLength, getLogSize(stack)));
	}
	
	public ItemStack setLogSize(ItemStack stack, int length)
	{
		U.Inventorys.setupNBT(stack, true).setShort("length", (short) length);
		return stack;
	}
	
	public int getLogSize(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getShort("length");
	}

	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof SubstanceWood)
			{
				return setLogSize(new ItemStack(this, 1, ((SubstanceWood) objects[0]).getID()), 16);
			}
		}
		else if(objects.length == 2)
		{
			if(objects[0] instanceof SubstanceWood &&
					objects[1] instanceof Number)
			{
				return setLogSize(new ItemStack(this, 1, ((SubstanceWood) objects[0]).getID()), ((Number) objects[1]).intValue());
			}
		}
		return null;
	}
}