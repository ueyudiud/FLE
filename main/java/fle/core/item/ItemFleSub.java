package fle.core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import fle.api.enums.EnumAtoms;
import fle.api.enums.EnumCraftingType;
import fle.api.item.IBagable;
import fle.api.item.ICastingTool;
import fle.api.item.IItemBehaviour;
import fle.api.item.IPolishTool;
import fle.api.item.ISubPolishTool;
import fle.api.item.ItemFleMetaBase;
import fle.api.recipe.CraftingState;
import fle.api.util.FleLog;
import fle.api.util.ITextureLocation;
import fle.api.util.SubTag;
import fle.cg.ICG;
import fle.cg.RecipesTab;
import fle.core.init.IB;
import fle.core.item.behavior.BehaviorArrowBag;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.behavior.BehaviorBlockable;
import fle.core.item.behavior.BehaviorCastingTool;
import fle.core.item.behavior.BehaviorCeramics;
import fle.core.item.behavior.BehaviorFlintChip;
import fle.core.item.behavior.BehaviorGuideBook;
import fle.core.util.TextureLocation;

public class ItemFleSub extends ItemSub implements IPolishTool, IBagable, ICastingTool, ICG
{
	public ItemFleSub(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	}
	
	public ItemFleSub init()
	{
		addSubItem(0, "flint_a", "stones/1");
		addSubItem(1, "flint_b", "stones/2", new BehaviorFlintChip());
		addSubItem(2, "flint_c", "stones/3");
		addSubItem(10, "stone_a", "stones/201", new BehaviorBlockable(9, Blocks.cobblestone));
		addSubItem(11, "stone_b", "stones/202");
		addSubItem(31, "limestone", "stones/1001");
		addSubItem(32, "sandstone", "stones/1002");
		addSubItem(33, "netherstone", "stones/1003");
		addSubItem(34, "chip_obsidian", "stones/2001");
		addSubItem(101, "bark_oak", "tree/1");
		addSubItem(102, "bark_spruce", "tree/2");
		addSubItem(103, "bark_birch", "tree/3");
		addSubItem(104, "bark_jungle", "tree/4");
		addSubItem(105, "bark_acacia", "tree/5");
		addSubItem(106, "bark_darkoak", "tree/6");
		addSubItem(121, "branch_oak", "tree/101");
		addSubItem(122, "branch_spruce", "tree/102");
		addSubItem(123, "branch_birch", "tree/103");
		addSubItem(124, "branch_jungle", "tree/104");
		addSubItem(125, "branch_acacia", "tree/105");
		addSubItem(126, "branch_darkoak", "tree/106");
		addSubItem(141, "seed_oak", "tree/201", new BehaviorBlockable(Blocks.sapling, 0));
		addSubItem(142, "seed_spruce", "tree/202", new BehaviorBlockable(Blocks.sapling, 1));
		addSubItem(143, "seed_birch", "tree/203", new BehaviorBlockable(Blocks.sapling, 2));
		addSubItem(144, "seed_jungle", "tree/204", new BehaviorBlockable(Blocks.sapling, 3));
		addSubItem(145, "seed_acacia", "tree/205", new BehaviorBlockable(Blocks.sapling, 4));
		addSubItem(146, "seed_darkoak", "tree/206", new BehaviorBlockable(Blocks.sapling, 5));
		addSubItem(151, "branch_bush", "tree/121");
		addSubItem(161, "leaves", "tree/231");
		addSubItem(401, "ramie_fiber", "crop/ramie_fiber");
		addSubItem(1001, "leaves_dry", "tree/1001");
		addSubItem(1002, "tinder", "tree/1002");
		addSubItem(1003, "ramie_fiber_dry", "crop/ramie_fiber_dry");
		addSubItem(1004, "ramie_rope", "crop/ramie_rope");
		addSubItem(1005, "ramie_bundle_rope", "crop/ramie_bundle_rope");
		addSubItem(1006, "charred_log", "tree/1003", new BehaviorBlockable(4, IB.charcoal));
		addSubItem(2001, "lipocere", "resource/dust/1");
		addSubItem(3001, "dust_limestone", "stones/11001");
		addSubItem(3002, "plant_ash", "resource/dust/3", new BehaviorBlockable(IB.ash));
		addSubItem(3003, "argil_ball", "resource/dust/2", new BehaviorCeramics());
		addSubItem(3004, "cemented_grit", "resource/dust/1001", new BehaviorCastingTool());
		addSubItem(5202, "argil_brick", "clay/101");
		addSubItem(6201, "stone_plate", "resource/plate/stone");
		addSubItem(6202, "argil_plate", "resource/plate/argil");
		addSubItem(10001, "arrow_bag", "tools/arrow_bag", new BehaviorArrowBag());
		addSubItem(10101, "guide_book", "book/guide_book", new BehaviorGuideBook(RecipesTab.tabClassic));
		addSubItem(10102, "guide_book_1", "book/0", new BehaviorGuideBook(RecipesTab.tabOldStoneAge));
		addSubItem(10103, "guide_book_2", "book/1", new BehaviorGuideBook(RecipesTab.tabNewStoneAge));
		addSubItem(10104, "guide_book_3", "book/2", new BehaviorGuideBook(RecipesTab.tabCopperAge));
		for(int i = 0; i < EnumAtoms.values().length; ++i)
		{
			if(EnumAtoms.values()[i].contain(SubTag.ATOM_metal))
				addSubItem(20001 + i, "ingot_" + EnumAtoms.values()[i].name().toLowerCase(), "resource/ingot/" + EnumAtoms.values()[i].name().toLowerCase());
		}
		stackLimitList.add(10001);
		return this;
	}
	
	private List<Integer> stackLimitList = new ArrayList();

	public static ItemStack a(String name)
	{
		return a(name, 1);
	}
	public static ItemStack a(String name, int size)
	{
		if("ingot_fe".equals(name)) return new ItemStack(Items.iron_ingot, size);
		if("ingot_au".equals(name)) return new ItemStack(Items.gold_ingot, size);
		try
		{
			int meta = ((ItemFleSub) IB.subItem).itemBehaviors.serial(name);
			ItemStack ret = new ItemStack(IB.subItem, size, meta);
			IB.subItem.setDamage(ret, meta);
			return ret;
		}
		catch(Throwable e)
		{
			//Use a null item.
			FleLog.getLogger().catching(new RuntimeException("Fle: some mod use empty item id " + name + ", please check your fle-addon "
					+ "had already update, or report this bug to mod editer."));
			return null; //Return null.
		}
	}

	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, String aLocate)
	{
		return addSubItem(aMetaValue, aTagName, new TextureLocation(aLocate), new BehaviorBase());
	}
	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, String aLocate, IItemBehaviour<ItemFleMetaBase> aBehavior)
	{
		return addSubItem(aMetaValue, aTagName, new TextureLocation(aLocate), aBehavior);
	}
	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, ITextureLocation aLocate)
	{
		return addSubItem(aMetaValue, aTagName, aLocate, new BehaviorBase());
	}
	
	@Override
	public int getItemStackLimit(ItemStack aStack)
	{
		return stackLimitList.contains(getDamage(aStack)) ? 1 : super.getItemStackLimit(aStack);
	}

	@Override
	public ItemStack getOutput(EntityPlayer player, ItemStack aStack) 
	{
	    isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior instanceof ISubPolishTool)
	    	{
	            return ((ISubPolishTool) tBehavior).getOutput(this, aStack, player);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return aStack;
	}

	@Override
	public CraftingState getState(ItemStack aStack, EnumCraftingType aType, CraftingState aState) 
	{
	    isItemStackUsable(aStack);
	    IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
	    try
	    {
	    	if (tBehavior instanceof ISubPolishTool)
	    	{
	            return ((ISubPolishTool) tBehavior).getState(this, aStack, aType, aState);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return aState;
	}

	@Override
	public int getSize(ItemStack aStack)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
	    	if (tBehavior instanceof IBagable)
	    	{
	            return ((IBagable) tBehavior).getSize(aStack);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return 0;
	}

	@Override
	public ItemStack getItemContain(ItemStack aStack, int i)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
	    	if (tBehavior instanceof IBagable)
	    	{
	            return ((IBagable) tBehavior).getItemContain(aStack, i);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return null;
	}

	@Override
	public void setItemContain(ItemStack aStack, int i, ItemStack aInput)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
	    	if (tBehavior instanceof IBagable)
	    	{
	            ((IBagable) tBehavior).setItemContain(aStack, i, aInput);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}

	@Override
	public boolean isItemValid(ItemStack aStack, ItemStack aInput)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
	    	if (tBehavior instanceof IBagable)
	    	{
	            return ((IBagable) tBehavior).isItemValid(aStack, aInput);
	        }
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}
	
	@Override
	public boolean isValidArmor(ItemStack aStack, int armorType, Entity entity)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
	    	return tBehavior.isValidArmor(this, aStack, armorType, entity);
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return super.isValidArmor(aStack, armorType, entity);
	}

	@Override
	public boolean isCastingTool(ItemStack aStack)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof ICastingTool)
			{
				return ((ICastingTool) tBehavior).isCastingTool(aStack);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}

	@Override
	public RecipesTab getBookTab(ItemStack aStack) 
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof ICG)
			{
				return ((ICG) tBehavior).getBookTab(aStack);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return RecipesTab.tabClassic;
	}
}