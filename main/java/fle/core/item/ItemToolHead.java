package fle.core.item;

import static fle.core.item.ItemTool.materials;
import static fle.core.item.ItemTool.tagMap;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
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
		addSubItem(1, "rough_stone_axe", 
				new TextureLocation(new String[]{"tools/head/rough_axe_head", "tools/head/rough_axe_rust"}));
		addSubItem(2, "stone_axe", 
				new TextureLocation(new String[]{"tools/head/stone_axe_head", "tools/head/stone_axe_rust"}));
		addSubItem(3, "stone_shovel", 
				new TextureLocation(new String[]{"tools/head/stone_shovel_head", "tools/head/stone_shovel_rust"}));
		addSubItem(4, "flint_hammer", 
				new TextureLocation(new String[]{"tools/head/flint_hammer_head", "tools/head/flint_hammer_rust"}));
		addSubItem(5, "stone_hammer", 
				new TextureLocation(new String[]{"tools/head/stone_hammer_head", "tools/head/stone_hammer_rust"}));
		addSubItem(10, "flint_arrow", 
				new TextureLocation(new String[]{"tools/head/flint_arrow_head", FleValue.VOID_ICON_FILE}));
		return this;
	}
	
	public ItemToolHead(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		setNoRepair();
		setMaxStackSize(4);
	}

	public final ItemToolHead addSubItem(int aMetaValue, String aTagName, ITextureLocation aLocate)
	{
		addSubItem(aMetaValue, aTagName, aLocate, new BehaviorBase());
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
	
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack aStack, int pass)
    {
    	ToolMaterialInfo tInfo = new ToolMaterialInfo(setupNBT(aStack));
    	switch(pass)
    	{
    	case 0 :
    		return tInfo.getMaterialBase().getPropertyInfo().getColors()[0];
    	case 1 :
    		if(tInfo.getCoverLevel() < 0.3F)
    		{
    			return tInfo.getMaterialBase().getPropertyInfo().getColors()[1];
    		}
    		else if(tInfo.getMaterialSurface() != null)
    		{
    			return tInfo.getMaterialSurface().getPropertyInfo().getColors()[0];
    		}
    		else return 0xFFFFFF;
    	case 2 : 
    		return tInfo.getMaterialMosaic() == null ? 0xFFFFFF : tInfo.getMaterialMosaic().getPropertyInfo().getColors()[0];
    	default : return 0xFFFFFF;
    	}
    }
}