package fle.core.item;

import static fle.core.item.ItemTool.materials;
import static fle.core.item.ItemTool.tagMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.item.IItemBehaviour;
import fle.api.material.MaterialAbstract;
import fle.api.util.IDataChecker;
import fle.api.util.ISubTagContainer;
import fle.api.util.ITextureLocation;
import fle.core.init.IB;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.tool.ToolMaterialInfo;
import fle.core.util.TextureLocation;

public class ItemToolHead extends ItemSub
{
	public static ItemStack a(String toolTip, int size, MaterialAbstract base)
	{
		ItemStack ret = a(toolTip, base);
		ret.stackSize = size;
		return ret;
	}
	public static ItemStack a(String toolTip, MaterialAbstract base)
	{
		return a(toolTip, base, 0);
	}
	public static ItemStack a(String toolTip, MaterialAbstract base, int damage)
	{
		return a(toolTip, base, null, 0.0F, null, damage);
	}
	public static ItemStack a(String toolTip, MaterialAbstract base, MaterialAbstract cover, float area, MaterialAbstract mosaic, int damage)
	{
		ItemStack aStack = new ItemStack(IB.toolHead, 1, ((ItemToolHead) IB.toolHead).itemBehaviors.serial(toolTip));
		aStack.stackTagCompound = new NBTTagCompound();
		new ToolMaterialInfo(base, cover, area, mosaic).writeToNBT(aStack.getTagCompound());
		((ItemToolHead) IB.toolHead).setDisplayDamage(aStack, damage);
		return aStack;
	}
	
	public ItemToolHead init()
	{
		addSubItem(1, "rough_stone_axe", "Rough Stone Axe Head",
				new TextureLocation(new String[]{"tools/head/rough_axe_head", "tools/head/rough_axe_rust"}));
		addSubItem(2, "stone_axe", "Stone Axe Head",
				new TextureLocation(new String[]{"tools/head/stone_axe_head", "tools/head/stone_axe_rust"}));
		addSubItem(3, "stone_shovel", "Stone Shovel Head",
				new TextureLocation(new String[]{"tools/head/stone_shovel_head", "tools/head/stone_shovel_rust"}));
		addSubItem(4, "flint_hammer", "Flint Hammer Head",
				new TextureLocation(new String[]{"tools/head/flint_hammer_head", "tools/head/flint_hammer_rust"}));
		addSubItem(5, "stone_hammer", "Stone Hammer Head",
				new TextureLocation(new String[]{"tools/head/stone_hammer_head", "tools/head/stone_hammer_rust"}));
		addSubItem(10, "flint_arrow", "Arrow Point", 
				new TextureLocation("tools/head/flint_arrow_head"));
		addSubItem(11, "stone_sickle", "Stone Sickle Head", 
				new TextureLocation(new String[]{"tools/head/stone_sickle_head", "tools/head/stone_sickle_rust"}));
		addSubItem(12, "stone_spade_hoe", "Stone Spaed-Hoe Head", 
				new TextureLocation(new String[]{"tools/head/spade_hoe_head", "tools/head/spade_hoe_rust"}));
		addSubItem(16, "stone_spinning_disk", "Stone Spinning Disk", 
				new TextureLocation("tools/head/stone_spinning_disk_head"));
		addSubItem(18, "stone_knife", "Stone Knife Head", 
				new TextureLocation(new String[]{"tools/head/stone_knife_head", "tools/head/stone_knife_rust"}));
		addSubItem(101, "metal_axe", "Metal Axe Head",
				new TextureLocation(new String[]{"tools/head/metal_axe_head", "tools/head/metal_axe_rust", "tools/head/metal_axe_mosaic"}));
		addSubItem(102, "metal_pickaxe", "Metal Pickaxe Head",
				new TextureLocation(new String[]{"tools/head/metal_pickaxe_head", "tools/head/metal_pickaxe_rust", "tools/head/metal_pickaxe_mosaic"}));
		addSubItem(103, "metal_hammer", "Metal Hammer Head",
				new TextureLocation(new String[]{"tools/head/metal_hammer_head", "tools/head/metal_hammer_rust", "tools/head/metal_hammer_mosaic"}));
		addSubItem(104, "metal_shovel", "Metal Shovel Head",
				new TextureLocation(new String[]{"tools/head/metal_shovel_head", "tools/head/metal_shovel_rust", "tools/head/metal_shovel_mosaic"}));
		addSubItem(105, "metal_chisel", "Metal Chisel Head",
				new TextureLocation(new String[]{"tools/head/metal_chisel_head", "tools/head/metal_chisel_rust", "tools/head/metal_chisel_mosaic"}));
		addSubItem(106, "metal_bowsaw", "Metal Bowsaw",
				new TextureLocation(new String[]{"tools/head/bowsaw_head", "tools/head/bowsaw_rust", "tools/head/bowsaw_mosaic"}));
		addSubItem(107, "metal_adz", "Metal Adz",
				new TextureLocation(new String[]{"tools/head/metal_adz_head", "tools/head/metal_adz_rust", "tools/head/metal_adz_mosaic"}));
		heightLightSet.add(16);
		return this;
	}
	
	public ItemToolHead(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		setNoRepair();
		setMaxStackSize(4);
	}

	public final ItemToolHead addSubItem(int aMetaValue, String aTagName, String aLocalized, ITextureLocation aLocate)
	{
		addSubItem(aMetaValue, aTagName, aLocalized, aLocate, new BehaviorBase());
		return this;
	}
	
	@Override
	public int getMaxDamage(ItemStack aStack) 
	{
		return new ToolMaterialInfo(setupNBT(aStack)).getMaxUse();
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack aStack) 
	{
		return getDisplayDamage(aStack) != 0;
	}
	
	@Override
	public int getDisplayDamage(ItemStack aStack) 
	{
		return setupNBT(aStack).getInteger("ToolDamage");
	}
	
	public void setDisplayDamage(ItemStack aStack, int aDamage) 
	{
		setupNBT(aStack).setInteger("ToolDamage", aDamage);
	}
	
	@Override
	public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List aList) 
	{
		for(IItemBehaviour tBehavior : itemBehaviors)
		{
			IDataChecker<ISubTagContainer> tTag = tagMap.get(itemBehaviors.name(tBehavior));
			if(tTag != null)
			{
				for(MaterialAbstract material : materials)
					if(tTag.isTrue(material))
					{
						ItemStack tStack = new ItemStack(aItem, 1, itemBehaviors.serial(tBehavior));
						setupNBT(tStack);
						new ToolMaterialInfo(material, null, 0.0F, null).writeToNBT(tStack.stackTagCompound);
						aList.add(tStack);
					}
			}
			else
			{
				for(MaterialAbstract material : materials)
				{
					ItemStack tStack = new ItemStack(aItem, 1, itemBehaviors.serial(tBehavior));
					setupNBT(tStack);
					new ToolMaterialInfo(material, null, 0.0F, null).writeToNBT(tStack.stackTagCompound);
					aList.add(tStack);
				}
			}
		}
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		if(pass == 1)
		{
	    	ToolMaterialInfo tInfo = new ToolMaterialInfo(setupNBT(stack));
	    	if(tInfo.getMaterialSurface() == null)
	    		return itemIcon;
		}
		if(pass == 2)
		{
	    	ToolMaterialInfo tInfo = new ToolMaterialInfo(setupNBT(stack));
	    	if(tInfo.getMaterialMosaic() == null)
	    		return itemIcon;
		}
		return super.getIcon(stack, pass);
	}
	
	Set<Integer> heightLightSet = new HashSet();
	
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack aStack, int pass)
    {
    	ToolMaterialInfo tInfo = new ToolMaterialInfo(setupNBT(aStack));
    	int colorIndex = 0xFFFFFF;
    	if(tInfo.getMaterialBase() == null) return colorIndex;
    	switch(pass)
    	{
    	case 0 :
    		colorIndex = tInfo.getMaterialBase().getPropertyInfo().getColors()[0];
    		break;
    	case 1 :
    		if(tInfo.getCoverLevel() < 0.3F)
    		{
    			colorIndex = tInfo.getMaterialBase().getPropertyInfo().getColors()[1];
    		}
    		else if(tInfo.getMaterialSurface() != null)
    		{
    			colorIndex = tInfo.getMaterialSurface().getPropertyInfo().getColors()[0];
    		}
    		break;
    	case 2 : 
    		colorIndex = tInfo.getMaterialMosaic() == null ? 0xFFFFFF : tInfo.getMaterialMosaic().getPropertyInfo().getColors()[0];
    		break;
    	default : return 0xFFFFFF;
    	}
    	if(heightLightSet.contains(getDamage(aStack)))
    	{
            int r = ((colorIndex >> 16 & 255) + 0xFF) / 2;
            int g = ((colorIndex >> 8 & 255) + 0xFF) / 2;
            int b = ((colorIndex & 255) + 0xFF) / 2;
            return (r << 16) + (g << 8) + b;
    	}
    	return colorIndex;
    }
}