package fle.matter.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farcore.util.Part;
import flapi.material.MaterialAbstract;
import flapi.util.FleValue;
import fle.core.item.ItemSub;
import fle.core.item.behavior.BehaviorBase;
import fle.core.util.ItemTextureHandler;
import fle.matter.FleMatter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMatter extends ItemSub
{
	private static Part[] parts = 
		{
				//Part.dust,
				//Part.dustSmall,
				//Part.dustTiny,
				Part.ingot,
				Part.ingotDouble,
				Part.ingotQuadruple,
				Part.plate,
				Part.plateDouble,
				Part.plateQuadruple,
				//Part.powder,
				//Part.nugget,
				Part.cube,
				Part.stick,
				Part.stickLong
		};
	private static Map<String, ItemMatter> map = new HashMap();

	public static ItemStack a(MaterialAbstract matter, Part part)
	{
		if(!map.containsKey(part.name)) return null;
		ItemMatter item = map.get(part.name);
		return item.a(matter);
	}
	public ItemStack a(MaterialAbstract matter)
	{
		ItemStack ret = new ItemStack(this, 1);
		setDamage(ret, FleMatter.register.serial(matter));
		return ret;
	}
	
	public static void init()
	{
		for(Part part : parts)
		{
			ItemMatter item;
			map.put(part.name, item = new ItemMatter(part, "resource." + part.name, "tooltip." + part.name));
			for(MaterialAbstract material : FleMatter.register)
			{
				if(material.contain(part.partTag))
					OreDictionary.registerOre(part.name + material.getName(), item.a(material));
			}
		}
	}
	
	Part part;
	
	public ItemMatter(Part part, String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		this.part = part;
		setCreativeTab(FleValue.tabFLE);
		for(MaterialAbstract material : FleMatter.register)
			if(material.contain(part.partTag))
				addSubItem(FleMatter.register.serial(material),
						material.getName().toLowerCase(), new ItemTextureHandler("resource/" + part.name + "/" + material.getName().toLowerCase()), new BehaviorBase());
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack aStack)
	{
		return FleMatter.register.get(aStack.getItemDamage()).getDisplayName();
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTab, List list)
	{
		for(MaterialAbstract material : FleMatter.register)
		{
			if(material.contain(part.partTag))
				list.add(a(material));
		}
	}
}