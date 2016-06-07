package fle.core.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumItem;
import farcore.enums.EnumItem.IInfomationable;
import farcore.interfaces.item.IBehavior;
import farcore.interfaces.item.IItemInfo;
import farcore.interfaces.item.IItemProperty;
import farcore.item.ItemSubBehavior;
import farcore.util.V;
import fle.core.item.behavior.BehaviorBowl;
import fle.core.item.behavior.bowl.BehaviorWaterBowl;
import fle.core.item.behavior.bowl.BehaviorWaterDirtyBowl;
import fle.core.item.behavior.bowl.BehaviorWaterPurifiedBowl;
import fle.core.item.resource.ItemFleDrinkableFood;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBowl extends ItemSubBehavior implements IInfomationable
{
	private Map<String, String> textureNameMap = new HashMap();
	@SideOnly(Side.CLIENT)
	private Map<String, IIcon> iconMap = new HashMap();
	
	public ItemBowl()
	{
		super("fle.bowl");
		EnumItem.bowl.set(new ItemStack(this));
		init();
	}
	
	private void init()
	{
		addSubItem(0, "empty", "Bowl", "empty", new BehaviorBowl());
		addSubItem(101, "water", "Water Bowl", "water", new BehaviorWaterBowl());
		addSubItem(102, "water_dirty", "Dirty Water Bowl", "water_dirty", new BehaviorWaterDirtyBowl());
		addSubItem(103, "water_purified", "Purified Water Bowl", "water_purified", new BehaviorWaterPurifiedBowl());
		ItemFleDrinkableFood.registerFoods(this, register);
	}
	
	public void addSubItem(int id, String name, String local, String iconName, IBehavior behavior, IItemProperty property)
	{
		super.addSubItem(id, name, local, behavior, property);
		textureNameMap.put(name, iconName);
	}
	
	public void addSubItem(int id, String name, String local, String iconName, IItemInfo itemInfo)
	{
		super.addSubItem(id, name, local, itemInfo);
		textureNameMap.put(name, iconName);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		icon = register.registerIcon(getIconString() + "/base");
		iconMap = new HashMap();
		for(Entry<String, String> entry : textureNameMap.entrySet())
		{
			iconMap.put(entry.getKey(), register.registerIcon(getIconString() + "/" + entry.getValue()));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int metadata)
	{
		return 2;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack)
	{
		return getIcon(stack, 0);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
	{
		return pass == 0 ? icon : iconMap.getOrDefault(register.name(meta), V.voidItemIcon);
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