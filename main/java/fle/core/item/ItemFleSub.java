package fle.core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import fle.api.cg.ICG;
import fle.api.cg.RecipesTab;
import fle.api.enums.EnumAtoms;
import fle.api.enums.EnumCraftingType;
import fle.api.item.IBagable;
import fle.api.item.ICastingTool;
import fle.api.item.IItemBehaviour;
import fle.api.item.IPolishTool;
import fle.api.item.ISubPolishTool;
import fle.api.item.ItemFleMetaBase;
import fle.api.recipe.CraftingState;
import fle.api.soild.ISolidContainerItem;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTankInfo;
import fle.api.soild.Solid.SolidState;
import fle.api.util.FleLog;
import fle.api.util.ITextureLocation;
import fle.api.util.SubTag;
import fle.core.init.IB;
import fle.core.item.behavior.BehaviorArgilItem;
import fle.core.item.behavior.BehaviorArrowBag;
import fle.core.item.behavior.BehaviorBarrel;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.behavior.BehaviorBlockable;
import fle.core.item.behavior.BehaviorCastingTool;
import fle.core.item.behavior.BehaviorCeramics;
import fle.core.item.behavior.BehaviorFlintChip;
import fle.core.item.behavior.BehaviorGuideBook;
import fle.core.item.behavior.BehaviorSack;
import fle.core.util.TextureLocation;

public class ItemFleSub extends ItemSub implements IPolishTool, IBagable, ICastingTool, ICG, ISolidContainerItem
{
	public ItemFleSub(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	}
	
	public ItemFleSub init()
	{
		addSubItem(0, "flint_a", "Flint Fragment", "stones/1");
		addSubItem(1, "flint_b", "Small Flint Chip", "stones/2", new BehaviorFlintChip());
		addSubItem(2, "flint_c", "Sharp Flint Chip", "stones/3");
		addSubItem(10, "stone_a", "Stone Chip", "stones/201", new BehaviorBlockable(9, Blocks.cobblestone));
		addSubItem(11, "stone_b", "Stone Fragment", "stones/202");
		addSubItem(31, "limestone", "LimeStone Chip", "stones/1001");
		addSubItem(32, "sandstone", "Sandstone Chip", "stones/1002");
		addSubItem(33, "netherstone", "Nether Chip", "stones/1003");
		addSubItem(34, "chip_obsidian", "Obsidian Chip", "stones/2001");
		addSubItem(101, "bark_oak", "Oak Bark", "tree/1");
		addSubItem(102, "bark_spruce", "Spruce Bark", "tree/2");
		addSubItem(103, "bark_birch", "Birch Bark", "tree/3");
		addSubItem(104, "bark_jungle", "Jungle Bark", "tree/4");
		addSubItem(105, "bark_acacia", "Acacia Bark", "tree/5");
		addSubItem(106, "bark_darkoak", "Dark Oak Bark", "tree/6");
		addSubItem(121, "branch_oak", "Oak Branch", "tree/101");
		addSubItem(122, "branch_spruce", "Spruce Branch", "tree/102");
		addSubItem(123, "branch_birch", "Birch Branch", "tree/103");
		addSubItem(124, "branch_jungle", "Jungle Branch", "tree/104");
		addSubItem(125, "branch_acacia", "Acacia Branch", "tree/105");
		addSubItem(126, "branch_darkoak", "Dark Oak Branch", "tree/106");
		addSubItem(141, "seed_oak", "Acorn", "tree/201", new BehaviorBlockable(Blocks.sapling, 0));
		addSubItem(142, "seed_spruce", "Spruce Cone", "tree/202", new BehaviorBlockable(Blocks.sapling, 1));
		addSubItem(143, "seed_birch", "Birch Fruit", "tree/203", new BehaviorBlockable(Blocks.sapling, 2));
		addSubItem(144, "seed_jungle", "Jungle Wood Seed", "tree/204", new BehaviorBlockable(Blocks.sapling, 3));
		addSubItem(145, "seed_acacia", "Acacia Seed", "tree/205", new BehaviorBlockable(Blocks.sapling, 4));
		addSubItem(146, "seed_darkoak", "Dark Acorn", "tree/206", new BehaviorBlockable(Blocks.sapling, 5));
		addSubItem(151, "branch_bush", "Bush Branch", "tree/121");
		addSubItem(161, "leaves", "Leaves", "tree/231");
		addSubItem(401, "ramie_fiber", "Ramie Fiber", "crop/ramie_fiber");
		addSubItem(402, "dandelion", "Dandelion", "crop/dandelion");
		addSubItem(403, "rugosa", "A.Rugosa", "crop/rugosa");
		addSubItem(404, "honeysuckle", "Honeysuckle", "crop/honeysuckle");
		addSubItem(405, "hemlock", "Hemlock", "crop/hemlock");
		addSubItem(406, "nigrum", "S.Nigrum", "crop/nigrum");
		addSubItem(407, "elegans", "G.Elegans", "crop/elegans");
		addSubItem(408, "fly_agaric", "Fly Agaric", "crop/fly_agaric");
		addSubItem(409, "belladonna", "Belladonna", "crop/belladonna");
		addSubItem(410, "mentha", "Mentha", "crop/mentha");
		addSubItem(411, "dill", "Dill", "crop/dill");
		addSubItem(412, "basil", "Basil", "crop/basil");
		addSubItem(413, "slime_juvenile", "Slime Juvenile", "crop/slime_juvenile");
		addSubItem(414, "white_snakeroot", "White Snakeroot", "crop/white_snakeroot");
		addSubItem(415, "cotton_rough", "Cotton With Seed", "crop/cotton_rough");
		addSubItem(416, "cotton", "Cotton", "crop/cotton");
		addSubItem(1001, "leaves_dry", "Dry Leaves", "tree/1001");
		addSubItem(1002, "tinder", "Tinder", "tree/1002");
		addSubItem(1003, "ramie_fiber_dry", "Dry Ramie Fiber", "crop/ramie_fiber_dry");
		addSubItem(1004, "ramie_rope", "Ramie Rope", "crop/ramie_rope");
		addSubItem(1005, "ramie_bundle_rope", "Ramie Rope Bundle", "crop/ramie_bundle_rope");
		addSubItem(1006, "charred_log", "Charred Log", "tree/1003", new BehaviorBlockable(4, IB.charcoal));
		addSubItem(1007, "millet", "Millet", "crop/millet");
		addSubItem(1008, "rattan", "Rattan", "crop/rattan");
		addSubItem(1009, "plant_waste", "Plant Waste", "crop/plant_waste");
		addSubItem(1010, "ramie_fiber_debonded", "Debonded Ramie Fiber", "crop/ramie_fiber_dry_b");
		addSubItem(1011, "twine", "Twine", "crop/twine");
		addSubItem(1012, "cotton_thread", "Cotton Thread", "crop/cotton_thread");
		addSubItem(2001, "lipocere", "Lipocere", "resource/dust/1");
		addSubItem(2002, "spinneret", "Spinneret", "drop/spinneret");
		addSubItem(3001, "dust_limestone", "Limestone Dust", "stones/11001", new BehaviorArgilItem());
		addSubItem(3002, "plant_ash", "Plant Ash", "resource/dust/3", new BehaviorBlockable(IB.ash));
		addSubItem(3003, "argil_ball", "Argil Ball", "resource/dust/2", new BehaviorCeramics());
		addSubItem(3004, "cemented_grit", "Cemented Grit", "resource/dust/1001", new BehaviorCastingTool());
		addSubItem(3005, "dust_quicklime", "Quick Lime Dust", "resource/dust/4");
		addSubItem(4001, "wooden_wedge", "Wooden Wedge", "tools/wooden_wedge");
		addSubItem(4002, "millstone", "Millstone", "machine/millstone");
		addSubItem(4003, "linen", "Linen", "crop/linen");
		addSubItem(4004, "linen_b", "Fine Linen", "crop/fine_linen");
		addSubItem(4005, "bran", "Bran", "crop/bran");
		addSubItem(4006, "cotton_gauze", "Cotton Gauze", "tools/cotton_gauze");
		addSubItem(5202, "argil_unsmelted_brick", "Unsmelted Argil Brick", "clay/101", new BehaviorArgilItem());
		addSubItem(5203, "argil_brick", "Argil Brick", "clay/1101");
		addSubItem(6201, "stone_plate", "Stone Plate", "resource/plate/stone", new BehaviorBlockable(IB.stoneMachine1, 2));
		addSubItem(6202, "argil_unsmelted_plate", "Unsmelted Argil Plate", "resource/plate/argil_unsmelted", new BehaviorArgilItem());
		addSubItem(6203, "argil_plate", "Argil Plate", "resource/plate/argil");
		addSubItem(7001, "wood_bucket_0_empty", "Empty Wood Barrel", "tank/wood_0_empty", new BehaviorBarrel(null));
		addSubItem(7002, "wood_bucket_0_water", "Water Wood Barrel", "tank/wood_0_water", new BehaviorBarrel(FluidRegistry.WATER));
		addSubItem(7003, "wood_bucket_0_plant_ash_mortar", "Plant Ash mortar Wood Barrel", "tank/wood_0_plant_ash_mortar", new BehaviorBarrel(IB.plant_ash_mortar));
		addSubItem(7004, "wood_bucket_0_lime_mortar", "Lime Mortar Wood Barrel", "tank/wood_0_lime_mortar", new BehaviorBarrel(IB.lime_mortar));
		addSubItem(BehaviorSack.emptySackIndex = 7101, "sack_empty", "Empty Sack", "tools/sack/empty", new BehaviorSack(SolidState.Dust, 1000));
		addSubItem(7102, "sack_wheat", "Wheat Sack", "tools/sack/wheat", new BehaviorSack("sack_wheat", IB.wheat_b, 1000));
		addSubItem(7103, "sack_millet", "Millet Sack", "tools/sack/millet", new BehaviorSack("sack_millet", IB.millet_b, 1000));
		addSubItem(7104, "sack_wheat_b", "Wholemeal Wheat Sack", "tools/sack/wheat_w", new BehaviorSack("sack_wheat", IB.wheat, 1000));
		addSubItem(7105, "sack_millet_b", "Wholemeal Millet Sack", "tools/sack/millet_W", new BehaviorSack("sack_millet", IB.millet, 1000));
		addSubItem(10001, "arrow_bag", "Arrow Bag", "tools/arrow_bag", new BehaviorArrowBag());
		addSubItem(10101, "guide_book", "Guide Book", "book/guide_book", new BehaviorGuideBook(RecipesTab.tabClassic));
		addSubItem(10102, "guide_book_1", "Old Stone Age Book", "book/0", new BehaviorGuideBook(RecipesTab.tabOldStoneAge));
		addSubItem(10103, "guide_book_2", "New Stone Age Book", "book/1", new BehaviorGuideBook(RecipesTab.tabNewStoneAge));
		addSubItem(10104, "guide_book_3", "Copper Age Book", "book/2", new BehaviorGuideBook(RecipesTab.tabCopperAge));
		int i = 0;
		int j = 0;
		String[][] strss = {{"ingot", "Ingot", "ingot"}, {"ingot_double", "Double Ingot", "ingot_double"}, {"plate", "Plate", "plate"}};
		for(; j < strss.length; ++j)
		{
			String[] strs = strss[j];
			for(; i < EnumAtoms.values().length; ++i)
			{
				if(EnumAtoms.values()[i].contain(SubTag.ATOM_metal))
					addSubItem(20001 + j * 1000 + i, strs[0] + "_" + EnumAtoms.values()[i].name().toLowerCase(), EnumAtoms.values()[i].getName() + " " + strs[1], "resource/" + strs[2] + "/" + EnumAtoms.values()[i].name().toLowerCase());
			}
			++i;
			addSubItem(20001 + j * 1000 + i++, strs[0] + "_cu_as_0", "Arsenic Bronze " + strs[1], "resource/" + strs[2] + "/cu-as-1");
			addSubItem(20001 + j * 1000 + i++, strs[0] + "_cu_as_1", "High Arsenic Bronze " + strs[1], "resource/" + strs[2] + "/cu-as-2");
			addSubItem(20001 + j * 1000 + i++, strs[0] + "_cu_pb_0", "Lead Bronze " + strs[1], "resource/" + strs[2] + "/cu-pb-1");
			addSubItem(20001 + j * 1000 + i++, strs[0] + "_cu_pb_1", "High Lead Bronze " + strs[1], "resource/" + strs[2] + "/cu-pb-2");
			addSubItem(20001 + j * 1000 + i++, strs[0] + "_cu_sn_0", "Tin Bronze " + strs[1], "resource/" + strs[2] + "/cu-sn-1");
			addSubItem(20001 + j * 1000 + i++, strs[0] + "_cu_sn_1", "High Tin Bronze " + strs[1], "resource/" + strs[2] + "/cu-sn-2");
			addSubItem(20001 + j * 1000 + i++, strs[0] + "_cu_pb_sn", "Lead Tin Bronze " + strs[1], "resource/" + strs[2] + "/cu-pb-sn");
			i = 0;
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
			FleLog.getLogger().warn("Fle: some mod use empty item id " + name + ", please check your fle-addon "
					+ "had already update, or report this bug to mod editer.");
			return null; //Return null.
		}
	}

	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, String aLocalized, String aLocate)
	{
		return addSubItem(aMetaValue, aTagName, aLocalized, new TextureLocation(aLocate), new BehaviorBase());
	}
	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, String aLocalized, String aLocate, IItemBehaviour<ItemFleMetaBase> aBehavior)
	{
		return addSubItem(aMetaValue, aTagName, aLocalized, new TextureLocation(aLocate), aBehavior);
	}
	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, String aLocalized, ITextureLocation aLocate)
	{
		return addSubItem(aMetaValue, aTagName, aLocalized, aLocate, new BehaviorBase());
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

	@Override
	public SolidStack getSolid(ItemStack aStack)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof ISolidContainerItem)
			{
				return ((ISolidContainerItem) tBehavior).getSolid(aStack);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return null;
	}

	@Override
	public int fill(ItemStack aStack, SolidStack resource, boolean doFill)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof ISolidContainerItem)
			{
				return ((ISolidContainerItem) tBehavior).fill(aStack, resource, doFill);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return 0;
	}

	@Override
	public SolidStack drain(ItemStack aStack, SolidStack resource,
			boolean doDrain)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof ISolidContainerItem)
			{
				return ((ISolidContainerItem) tBehavior).drain(aStack, resource, doDrain);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return null;
	}

	@Override
	public SolidStack drain(ItemStack aStack, int maxDrain, boolean doDrain)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof ISolidContainerItem)
			{
				return ((ISolidContainerItem) tBehavior).drain(aStack, maxDrain, doDrain);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return null;
	}

	@Override
	public boolean canFill(ItemStack aStack, Solid Solid)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof ISolidContainerItem)
			{
				return ((ISolidContainerItem) tBehavior).canFill(aStack, Solid);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}

	@Override
	public boolean canDrain(ItemStack aStack, Solid Solid)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof ISolidContainerItem)
			{
				return ((ISolidContainerItem) tBehavior).canDrain(aStack, Solid);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}

	@Override
	public SolidTankInfo getTankInfo(ItemStack aStack)
	{
		isItemStackUsable(aStack);
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof ISolidContainerItem)
			{
				return ((ISolidContainerItem) tBehavior).getTankInfo(aStack);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return null;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack aStack)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof IFluidContainerItem)
			{
				return ((IFluidContainerItem) tBehavior).getFluid(aStack) != null;
			}
			if(tBehavior instanceof ISolidContainerItem)
			{
				return ((ISolidContainerItem) tBehavior).getSolid(aStack) != null;
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack aStack)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(aStack)));
		try
	    {
			if(tBehavior instanceof IFluidContainerItem && ((IFluidContainerItem) tBehavior).getFluid(aStack) != null)
			{
				return 1D - (double) ((IFluidContainerItem) tBehavior).getFluid(aStack).amount / (double) ((IFluidContainerItem) tBehavior).getCapacity(aStack);
			}
			if(tBehavior instanceof ISolidContainerItem)
			{
				return 1D - ((ISolidContainerItem) tBehavior).getTankInfo(aStack).getProgress();
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return 0.0D;
	}
}