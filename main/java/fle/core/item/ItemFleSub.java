package fle.core.item;

import java.util.ArrayList;
import java.util.List;

import flapi.cg.ICG;
import flapi.cg.RecipesTab;
import flapi.enums.EnumCraftingType;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;
import flapi.item.interfaces.IBagable;
import flapi.item.interfaces.ICastingTool;
import flapi.item.interfaces.IItemBehaviour;
import flapi.item.interfaces.IPlacedHandler;
import flapi.item.interfaces.IPolishTool;
import flapi.item.interfaces.ISubPolishTool;
import flapi.plant.IFertilableBlock.FertitleLevel;
import flapi.recipe.CraftingState;
import flapi.solid.ISolidContainerItem;
import flapi.solid.Solid;
import flapi.solid.Solid.SolidState;
import flapi.solid.SolidStack;
import flapi.solid.SolidTankInfo;
import flapi.util.FleLog;
import flapi.util.FleValue;
import flapi.world.ITEInWorld;
import fle.core.init.Config;
import fle.core.init.IB;
import fle.core.item.behavior.BehaviorArrowBag;
import fle.core.item.behavior.BehaviorBarrel;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.behavior.BehaviorBlockable;
import fle.core.item.behavior.BehaviorCeramics;
import fle.core.item.behavior.BehaviorFlintChip;
import fle.core.item.behavior.BehaviorGuideBook;
import fle.core.item.behavior.BehaviorPlaceableItem;
import fle.core.item.behavior.BehaviorSack;
import fle.core.item.behavior.BehavoirSackFertilizer;
import fle.core.item.behavior.IDamageable;
import fle.core.item.behavior.tool.BehaviorCastingTool;
import fle.core.util.ItemTextureHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ItemFleSub extends ItemSub implements IPolishTool, IBagable, ICastingTool, 
ICG, ISolidContainerItem, IPlacedHandler
{
	public ItemFleSub(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	}
	
	public ItemFleSub init()
	{
		//addSubItem(0, "null", "Null", "void"); For lost data item.
		addSubItem(1, "flint_a", "Flint Fragment", "resource/1");
		addSubItem(2, "flint_b", "Small Flint Chip", "resource/2", new BehaviorFlintChip());
		addSubItem(3, "flint_c", "Sharp Flint Chip", "resource/3");
		addSubItem(11, "stone_a", "Stone Chip", "resource/11", new BehaviorBlockable(9, Blocks.cobblestone));//Removed now.
		addSubItem(12, "stone_b", "Stone Fragment", "resource/12");//Removed now.
		addSubItem(21, "pile_gravel", "Gravel Pile", "resource/21");
		addSubItem(22, "pile_dirt", "Dirt Pile", "resource/22");
		addSubItem(23, "pile_sludge", "Sludge Pile", "resource/23");
		addSubItem(24, "pile_humus", "Humus Pile", "resource/24");
		addSubItem(25, "pile_sand", "Sand Pile", "resource/25");
		addSubItem(30, "chip_stone", "Stone Chip", "resource/30", new BehaviorBlockable(9, Blocks.cobblestone));
		addSubItem(31, "chip_limestone", "LimeStone Chip", "resource/31");
		addSubItem(32, "chip_sandstone", "Sandstone Chip", "resource/32");
		addSubItem(33, "chip_netherstone", "Nether Chip", "resource/33");
		addSubItem(34, "chip_obsidian", "Obsidian Chip", "resource/34");
		addSubItem(35, "chip_rhyolite", "Rhyolite Chip", "resource/35");
		addSubItem(36, "chip_andesite", "Andesite Chip", "resource/36");
		addSubItem(37, "chip_basalt", "Basalt Chip", "resource/37");
		addSubItem(38, "chip_peridotite", "Peridotite Chip", "resource/38");
		addSubItem(60, "fragment_stone", "Stone Fragment", "resource/60");
		addSubItem(61, "fragment_limestone", "LimeStone Fragment", "resource/61");
		addSubItem(62, "fragment_sandstone", "Sandstone Fragment", "resource/62");
		addSubItem(63, "fragment_netherstone", "Nether Fragment", "resource/63");
		addSubItem(64, "fragment_obsidian", "Obsidian Fragment", "resource/64");
		addSubItem(65, "fragment_rhyolite", "Rhyolite Fragment", "resource/65");
		addSubItem(66, "fragment_andesite", "Andesite Fragment", "resource/66");
		addSubItem(67, "fragment_basalt", "Basalt Fragment", "resource/67");
		addSubItem(68, "fragment_peridotite", "Peridotite Fragment", "resource/68");
		addSubItem(101, "bark_oak", "Oak Bark", "resource/101");
		addSubItem(102, "bark_spruce", "Spruce Bark", "resource/102");
		addSubItem(103, "bark_birch", "Birch Bark", "resource/103");
		addSubItem(104, "bark_jungle", "Jungle Bark", "resource/104");
		addSubItem(105, "bark_acacia", "Acacia Bark", "resource/105");
		addSubItem(106, "bark_darkoak", "Dark Oak Bark", "resource/106");
		addSubItem(107, "bark_beech", "Beech Bark", "resource/107");
		addSubItem(121, "branch_oak", "Oak Branch", "resource/121");
		addSubItem(122, "branch_spruce", "Spruce Branch", "resource/122");
		addSubItem(123, "branch_birch", "Birch Branch", "resource/123");
		addSubItem(124, "branch_jungle", "Jungle Branch", "resource/124");
		addSubItem(125, "branch_acacia", "Acacia Branch", "resource/125");
		addSubItem(126, "branch_darkoak", "Dark Oak Branch", "resource/126");
		addSubItem(127, "branch_beech", "Beech Branch", "resource/127");
		addSubItem(141, "seed_oak", "Acorn", "resource/141", new BehaviorBlockable(Blocks.sapling, 0));
		addSubItem(142, "seed_spruce", "Spruce Cone", "resource/142", new BehaviorBlockable(Blocks.sapling, 1));
		addSubItem(143, "seed_birch", "Birch Fruit", "resource/143", new BehaviorBlockable(Blocks.sapling, 2));
		addSubItem(144, "seed_jungle", "Jungle Wood Seed", "resource/144", new BehaviorBlockable(Blocks.sapling, 3));
		addSubItem(145, "seed_acacia", "Acacia Seed", "resource/145", new BehaviorBlockable(Blocks.sapling, 4));
		addSubItem(146, "seed_darkoak", "Dark Acorn", "resource/146", new BehaviorBlockable(Blocks.sapling, 5));
		addSubItem(147, "seed_beech", "Beechnut", "resource/147");
		addSubItem(151, "branch_bush", "Bush Branch", "resource/151");
		addSubItem(161, "leaves", "Leaves", "resource/161");
		addSubItem(162, "leaves_b", "Leaves", "resource/162");
		addSubItem(163, "leaves_sisal", "Sisal Leaves", "resource/163");
		addSubItem(401, "ramie_fiber", "Ramie Fiber", "resource/401");
		addSubItem(402, "dandelion", "Dandelion", "resource/402");
		addSubItem(403, "rugosa", "A.Rugosa", "resource/403");
		addSubItem(404, "honeysuckle", "Honeysuckle", "resource/404");
		addSubItem(405, "hemlock", "Hemlock", "resource/405");
		addSubItem(406, "nigrum", "S.Nigrum", "resource/406");
		addSubItem(407, "elegans", "G.Elegans", "resource/407");
		addSubItem(408, "fly_agaric", "Fly Agaric", "resource/408");
		addSubItem(409, "belladonna", "Belladonna", "resource/409");
		addSubItem(410, "mentha", "Mentha", "resource/410");
		addSubItem(411, "dill", "Dill", "resource/411");
		addSubItem(412, "basil", "Basil", "resource/412");
		addSubItem(413, "slime_juvenile", "Slime Juvenile", "resource/413");
		addSubItem(414, "white_snakeroot", "White Snakeroot", "resource/414");
		addSubItem(415, "cotton_rough", "Cotton With Seed", "resource/415");
		addSubItem(416, "cotton", "Cotton", "resource/416");
		addSubItem(417, "sprouted_potato", "Sprouted Potato", "resource/417");
		addSubItem(418, "malt", "Malt", "resource/418");
		addSubItem(419, "aspergillus", "Mould Saccharifier", "resource/419");
		addSubItem(501, "crystal_quartz", "Quartz", "resource/501");
		addSubItem(502, "crystal_opal", "Opal", "resource/502");
		addSubItem(521, "chip_quartz", "Chip Quartz", "resource/521");
		addSubItem(1001, "leaves_dry", "Dry Leaves", "resource/1001");
		addSubItem(1002, "tinder", "Tinder", "resource/1002");
		addSubItem(1003, "ramie_fiber_dry", "Dry Ramie Fiber", "resource/1003");
		addSubItem(1004, "ramie_rope", "Ramie Rope", "resource/1004");
		addSubItem(1005, "ramie_bundle_rope", "Ramie Rope Bundle", "resource/1005");
		addSubItem(1006, "charred_log", "Charred Log", "resource/1006", new BehaviorBlockable(4, IB.charcoal));
		addSubItem(1007, "millet", "Millet", "resource/1007");
		addSubItem(1008, "rattan", "Rattan", "resource/1008");
		addSubItem(1009, "plant_waste", "Plant Waste", "resource/1009");
		addSubItem(1010, "ramie_fiber_debonded", "Debonded Ramie Fiber", "resource/1010");
		addSubItem(1011, "twine", "Twine", "resource/1011");
		addSubItem(1012, "cotton_thread", "Cotton Thread", "resource/1012");
		addSubItem(1013, "bone_meal", "Bone Meal", "resource/1013");
		addSubItem(1014, "plant_ash_soap", "Plant Ash Soap", "resource/1014");
		addSubItem(1015, "straw", "Straw", "resource/1015");
		addSubItem(1016, "straw_dry", "Dry Straw", "resource/1016");
		addSubItem(1017, "straw_rope", "Straw Rope", "resource/1017");
		addSubItem(1018, "sisal_fiber", "Sisal Fiber", "resource/1018");
		addSubItem(1019, "sisal_rope", "Sisal Rope", "resource/1019");
		addSubItem(1020, "tinder_smoldering", "Smoldering Tinder", "resource/1020");
		addSubItem(1021, "yeast_strain", "Yeast Strain", "resource/1021");
		addSubItem(2001, "lipocere", "Lipocere", "resource/2001");
		addSubItem(2002, "spinneret", "Spinneret", "resource/2002");
		addSubItem(2003, "crushed_bone", "Crushed Bone", "resource/2003");
		addSubItem(2004, "defatted_crushed_bone", "Defatted Crushed Bone", "resource/2004");
		addSubItem(2005, "sinew", "Sinew", "resource/2005");
		addSubItem(3001, "dust_limestone", "Limestone Dust", "resource/3001", new BehaviorPlaceableItem());
		addSubItem(3002, "plant_ash", "Plant Ash", "resource/3002", new BehaviorBlockable(IB.ash));
		addSubItem(3003, "argil_ball", "Argil Ball", "resource/3003", new BehaviorCeramics());
		addSubItem(3004, "cemented_grit", "Cemented Grit", "resource/3004", new BehaviorCastingTool());
		addSubItem(3005, "dust_quicklime", "Quick Lime Dust", "resource/3005");
		addSubItem(3006, "dust_sand", "Sand Dust", "resource/3006");
		addSubItem(4001, "wooden_wedge", "Wooden Wedge", "resource/4001");
		addSubItem(4002, "millstone", "Millstone", "resource/4002");
		addSubItem(4003, "linen", "Linen", "resource/4003");
		addSubItem(4004, "linen_b", "Fine Linen", "resource/4004");
		addSubItem(4005, "bran", "Bran", "resource/4005");
		addSubItem(4006, "cotton_gauze", "Cotton Gauze", "resource/4006");
		addSubItem(4007, "rotproof_plank", "Rotproof Plank", "resource/4007");
		addSubItem(4008, "rotproof_stick", "Rotproof Wooden Stick", "resource/4008");
		addSubItem(5202, "argil_unsmelted_brick", "Unsmelted Argil Brick", "resource/5202", new BehaviorPlaceableItem());
		addSubItem(5203, "argil_brick", "Argil Brick", "resource/5203");
		addSubItem(6201, "stone_plate", "Stone Plate", "resource/plate/stone", new BehaviorBlockable(IB.stoneMachine1, 2));
		addSubItem(6202, "argil_unsmelted_plate", "Unsmelted Argil Plate", "resource/6202", new BehaviorPlaceableItem());
		addSubItem(6203, "argil_plate", "Argil Plate", "resource/plate/argil");
		addSubItem(7001, "wood_bucket_0_empty", "Empty Wood Barrel", "tools/tank/wood_0_empty", new BehaviorBarrel(null));
		addSubItem(7002, "wood_bucket_0_water", "Water Wood Barrel", "tools/tank/wood_0_water", new BehaviorBarrel(FluidRegistry.WATER));
		addSubItem(7003, "wood_bucket_0_plant_ash_mortar", "Plant Ash mortar Wood Barrel", "tools/tank/wood_0_plant_ash_mortar", new BehaviorBarrel(IB.plant_ash_mortar));
		addSubItem(7004, "wood_bucket_0_lime_mortar", "Lime Mortar Wood Barrel", "tools/tank/wood_0_lime_mortar", new BehaviorBarrel(IB.lime_mortar));
		addSubItem(BehaviorSack.emptySackIndex = 7101, "sack_empty", "Empty Sack", "tools/sack/empty", new BehaviorSack(SolidState.Dust, 1000));
//		addSubItem(7102, "sack_wheat", "Wheat Sack", "tools/sack/wheat", new BehaviorSack("sack_wheat", IB.wheat_b, 1000));
//		addSubItem(7103, "sack_millet", "Millet Sack", "tools/sack/millet", new BehaviorSack("sack_millet", IB.millet_b, 1000));
//		addSubItem(7104, "sack_wheat_b", "Wholemeal Wheat Sack", "tools/sack/wheat_w", new BehaviorSack("sack_wheat_b", IB.wheat, 1000));
//		addSubItem(7105, "sack_millet_b", "Wholemeal Millet Sack", "tools/sack/millet_W", new BehaviorSack("sack_millet_b", IB.millet, 1000));
//		addSubItem(7106, "sack_fertilizer1", "Ca P Fertilizer Sack", "tools/sack/fertilizer1", new BehavoirSackFertilizer("sack_fertilizer1", IB.Ca_P_fertilizer, 1000, new FertitleLevel(0, 3, 0, 5)));
		addSubItem(7201, "bowl_water", "Bowl Of Water", "tools/tank/bowl_water");
		addSubItem(7241, "jug_argil_unsmelted", "Argil Jug", "clay/3", new BehaviorPlaceableItem());
		addSubItem(7242, "jug_argil", "Argil Jug", "clay/1003/empty");
		//addSubItem(7243, "jug_argil_wheat", "Argil Jug With Wheat Alcohol", "clay/1003/alcohol_wheat");
		addSubItem(10001, "arrow_bag", "Arrow Bag", "tools/arrow_bag", new BehaviorArrowBag());
		addSubItem(10101, "guide_book", "Guide Book", "book/10086", new BehaviorGuideBook(RecipesTab.tabClassic));
		addSubItem(10102, "guide_book_1", "Old Stone Age Book", "book/0", new BehaviorGuideBook(RecipesTab.tabOldStoneAge));
		addSubItem(10103, "guide_book_2", "New Stone Age Book", "book/1", new BehaviorGuideBook(RecipesTab.tabNewStoneAge));
		addSubItem(10104, "guide_book_3", "Copper Age Book", "book/2", new BehaviorGuideBook(RecipesTab.tabCopperAge));
		/**
		int i = 0;
		int j = 0;
		String[][] strss = {
				{"ingot", "Ingot", "ingot"}, 
				{"ingot_double", "Double Ingot", "ingot_double"},
				{"ingot_quadruple", "Quadruple Ingot", "ingot_quadruple"},
				{"plate", "Plate", "plate"},
				{"plate_double", "Plate", "plate"},
				{"plate_quadruple", "Quadruple Plate", "plate_quadruple"}};
		for(; j < strss.length; ++j)
		{
			String[] strs = strss[j];
			for(; i < Atoms.values().length; ++i)
			{
				if(Atoms.values()[i].contain(SubTag.ATOM_metal))
					addSubItem(20001 + j * 1000 + i, strs[0] + "_" + Atoms.values()[i].name().toLowerCase(), Atoms.values()[i].getName() + " " + strs[1], "resource/" + strs[2] + "/" + Atoms.values()[i].name().toLowerCase());
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
		 */
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
			FleLog.getLogger().catching(e);
			return null; //Return null.
		}
	}

	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, String aLocalized, String aLocate)
	{
		return addSubItem(aMetaValue, aTagName, aLocalized, new ItemTextureHandler(aLocate), new BehaviorBase());
	}
	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, String aLocalized, String aLocate, IItemBehaviour<ItemFleMetaBase> aBehavior)
	{
		return addSubItem(aMetaValue, aTagName, aLocalized, new ItemTextureHandler(aLocate), aBehavior);
	}
	public final ItemFleMetaBase addSubItem(int aMetaValue, String aTagName, String aLocalized, ItemTextureHandler aLocate)
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

	@Override
	public ItemStack onBlockRemove(ITEInWorld te, ItemStack target)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(target)));
		try
	    {
			if(tBehavior instanceof IPlacedHandler)
			{
				return ((IPlacedHandler) tBehavior).onBlockRemove(te, target);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return target;
	}

	public final int smeltingEnergy = Config.getInteger("pItemSmeltingTick");
	
	@Override
	public ItemStack updatePlacedItem(ITEInWorld te, ItemStack target)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(target)));
		try
	    {
			if(tBehavior instanceof IPlacedHandler)
			{
				return ((IPlacedHandler) tBehavior).updatePlacedItem(te, target);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
//		if(FurnaceRecipes.smelting().getSmeltingResult(target) != null)
//		{
//			int t = ((TileEntityPlacedItem) te.getTileEntity()).getTemperature(ForgeDirection.UNKNOWN);
//			double h = (t - 500) * 256.0D;
//			if(t > 500)
//			{
//				((TileEntityPlacedItem) te.getTileEntity()).onHeatEmit(ForgeDirection.UNKNOWN, h);
//				if(((TileEntityPlacedItem) te.getTileEntity()).addSmeltedTick(h) > smeltingEnergy * target.stackSize)
//				{
//					ItemStack ret = FurnaceRecipes.smelting().getSmeltingResult(target);
//					ret.stackSize *= target.stackSize;
//					return ret;
//				}
//			}
//		}
		return target;
	}
	
	@Override
	public void addPlaceInfomation(ITEInWorld tile, List<String> list,
			ItemStack stack)
	{
		IItemBehaviour<ItemFleMetaBase> tBehavior = itemBehaviors.get(Short.valueOf((short)getDamage(stack)));
		try
	    {
			if(tBehavior instanceof IPlacedHandler)
			{
				((IPlacedHandler) tBehavior).addPlaceInfomation(tile, list, stack);
			}
	    }
	    catch(Throwable e)
	    {
	    	e.printStackTrace();
	    }
//		if(FurnaceRecipes.smelting().getSmeltingResult(stack) != null)
//		{
//			list.add("Smelting Progress : " + FleValue.format_progress.format(((TileEntityPlacedItem) tile).getSmeltedTick() / (double) (smeltingEnergy * stack.stackSize)));
//		}
	}
	
	@Override
	public void damageItem(ItemStack stack, EntityLivingBase user,
			EnumDamageResource reource, float damage)
	{
		IItemBehaviour<ItemFleMetaBase> behavior = itemBehaviors.get(Short.valueOf((short)getDamage(stack)));
		try
	    {
			if(behavior instanceof IDamageable)
			{
				((IDamageable) behavior).damageItem(stack, user, reource, damage);
			}
	    }
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
}