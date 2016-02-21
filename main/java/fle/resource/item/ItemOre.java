package fle.resource.item;

import java.util.List;

import farcore.util.Part;
import flapi.material.MaterialOre;
import flapi.material.MaterialRock;
import fle.core.init.IB;
import fle.core.item.ItemSub;
import fle.core.item.behavior.BehaviorBlockable;
import fle.core.util.ItemTextureHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemOre extends ItemSub
{
	public static ItemStack a(MaterialOre material)
	{
		return a(material, Part.chunk.resolution, 1);
	}
	public static ItemStack a(MaterialOre material, int size)
	{
		return a(material, Part.chunk.resolution, size);
	}
	public static ItemStack a(MaterialOre material, int amount, int size)
	{
		ItemStack ret = new ItemStack(IB.oreChip, size, MaterialOre.getOreID(material));
		((ItemOre) IB.oreChip).setOreAmount(ret, amount);
		return ret;
	}
	
	public ItemOre(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
		maxStackSize = 16;
	}

	public ItemOre init()
	{
		ItemTextureHandler locate = new ItemTextureHandler("iconsets/chip_rock", "void", "iconsets/chip_ore1", "iconsets/chip_ore2", "void");
		for(MaterialOre ore : MaterialOre.getOres())
		{
			addSubItem(MaterialOre.getOreID(ore), ore.getOreName().toLowerCase(), ore.getOreName() + " Chip", locate, new BehaviorBlockable(IB.ore_cobble, MaterialOre.getOreID(ore)));
		}
		return this;
	}
	
	@Override
	protected void addAdditionalToolTips(List list, ItemStack stack)
	{
		super.addAdditionalToolTips(list, stack);
		list.add(EnumChatFormatting.AQUA.toString() + 
				"Name : " + 
				MaterialOre.getOreFromID(getDamage(stack)).getOreName());
	}
	
	@Override
	public int getColorFromItemStack(ItemStack aStack, int aPass)
	{
		if(!aStack.hasTagCompound()) return 0xFFFFFF;
		switch(aPass)
		{
		case 1 : return MaterialRock.getOreFromID(getDamage(aStack) / 4096).getPropertyInfo().getColors()[0];
		case 2 : return MaterialOre.getOreFromID(getDamage(aStack) % 4096).getPropertyInfo().getColors()[0];
		case 3 : return MaterialOre.getOreFromID(getDamage(aStack) % 4096).getPropertyInfo().getColors()[1];
		default : return 0xFFFFFF;
		}
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs aCreativeTab, List list)
	{
		super.getSubItems(item, aCreativeTab, list);
		for(Object rawItem : list)
		{
			setOreAmount((ItemStack) rawItem, 36);
		}
	}
	
	public int getOreAmount(ItemStack stack)
	{
		return setupNBT(stack).getInteger("Amount");
	}
	
	public void setOreAmount(ItemStack stack, int amount)
	{
		setupNBT(stack).setInteger("Amount", amount);
	}
}