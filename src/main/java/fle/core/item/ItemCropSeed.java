package fle.core.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.plant.crop.ItemCrop;
import farcore.enums.EnumItem;
import farcore.enums.EnumItem.IInfomationable;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
import farcore.item.ItemFood;
import farcore.lib.crop.CropCard;
import farcore.lib.crop.CropManager;
import farcore.lib.render.item.ItemRenderInfoSimple;
import farcore.util.LanguageManager;
import fle.api.item.ItemResource;
import fle.api.item.behavior.BehaviorCrop;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemCropSeed extends ItemFood implements IInfomationable
{
	public ItemCropSeed()
	{
		super("crop.seed");
		EnumItem.crop_seed.set(new ItemStack(this));
		init();
	}
	
	private void init()
	{
		addSubItem(1, "soybean", "%s", "fle:resource/seed/soybean");
		addSubItem(2, "ramie", "%s Seed", "fle:resource/seed/ramie");
		addSubItem(3, "millet", "%s Seed", "fle:resource/seed/millet");
		addSubItem(4, "reed", "%s", "fle:resource/seed/reed");
		addSubItem(5, "potato", "%s", "fle:resource/seed/potato");
		addSubItem(6, "sweetpotato", "%s", "fle:resource/seed/sweetpotato");
		addSubItem(7, "wheat", "%s Seed", "fle:resource/seed/wheat");
		addSubItem(8, "cotton", "%s Seed", "fle:resource/seed/cotton");
		addSubItem(9, "flax", "%s Seed", "fle:resource/seed/flax");
		addSubItem(10, "cabbage", "%s Seed", "fle:resource/seed/cabbage");
	}
	
	@Override
	protected Object[] getTranslateObject(ItemStack stack)
	{
		try
		{
			return new Object[]{CropManager.getCrop(register.name(getDamage(stack))).getLocalizedName(ItemCrop.getDNA(stack))};
		}
		catch(Exception exception)
		{
			return new Object[]{""};
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		super.getSubItems(item, tab, list);
		for(Object stackRaw : list)
		{
			ItemStack stack = (ItemStack) stackRaw;
			CropCard crop = CropManager.getCrop(register.name(getDamage(stack)));
			if(crop != null)
			{
				ItemCrop.setCrop(stack, crop.makeNativeDNA(), 0);
			}
			else
			{
				ItemCrop.setCrop(stack, "", 0);
			}
		}
	}
	
	public static ItemStack applySeed(int size, CropCard card, int generation, String dna)
	{
		ItemStack stack = EnumItem.crop_seed.instance(size, card.name());
		if(stack != null)
		{
			return ItemCrop.setCrop(stack, dna, generation);
		}
		return null;
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String textureName)
	{
		super.addSubItem(id, name, local, itemInfo, new ItemRenderInfoSimple(textureName));
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String textureName,
			IFoodStat stat)
	{
		super.addSubItem(id, name, local, itemInfo, new ItemRenderInfoSimple(textureName), stat);
	}
	
	public void addSubItem(int id, String name, String local, String textureName)
	{
		super.addSubItem(id, name, local, new BehaviorCrop(name), new ItemRenderInfoSimple(textureName));
	}
	
	public void addSubItem(int id, String name, String local, String textureName, IFoodStat stat)
	{
		super.addSubItem(id, name, local, new BehaviorCrop(name), new ItemRenderInfoSimple(textureName), stat);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		super.addInformation(stack, player, list, flag);
		list.add("Seed Info:");
		String dna = ItemCrop.getDNA(stack);
		list.add("Name : " + EnumChatFormatting.GREEN + CropManager.getCrop(register.name(getDamage(stack))).getName(dna));
		list.add("Generation : "  + EnumChatFormatting.WHITE + (ItemCrop.getGeneration(stack) + 1));
	}

	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String && register.contain((String) objects[0]))
			{
				return new ItemStack(this, size, register.id((String) objects[0]));
			}
			else if(objects[0] instanceof Number)
			{
				return new ItemStack(this, size, ((Number) objects[0]).intValue());
			}
		}
		return null;
	}
}