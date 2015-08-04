package fle.core.item;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.item.IItemBehaviour;
import fle.api.item.ITreeLog;
import fle.api.item.ItemFleMetaBase;
import fle.api.util.ITextureLocation;
import fle.core.init.IB;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.behavior.BehaviorBlockable;
import fle.core.util.TextureLocation;

public class ItemFleSub extends ItemSub
{
	public ItemFleSub(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	}
	
	public ItemFleSub init()
	{
		addSubItem(0, "flint_a", "stones/1");
		addSubItem(1, "flint_b", "stones/2");
		addSubItem(2, "flint_c", "stones/3");
		addSubItem(10, "stone_a", "stones/201");
		addSubItem(11, "stone_b", "stones/202");
		addSubItem(31, "limestone_a", "stones/1001");
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
		addSubItem(1006, "charred_log", "tree/1003");
		addSubItem(2001, "lipocere", "resource/1");
		addSubItem(3001, "dust_limestone", "stones/11001");
		addSubItem(3002, "plant_ash", "resource/3");
		addSubItem(5001, "argil_brick", "clay/101");
		addSubItem(6002, "argil_plate", "clay/102");
		return this;
	}


	public static ItemStack a(String name)
	{
		return a(name, 1);
	}
	public static ItemStack a(String name, int size)
	{
		int meta = ((ItemFleSub) IB.subItem).itemBehaviors.serial(name);
		ItemStack ret = new ItemStack(IB.subItem, size, meta);
		IB.subItem.setDamage(ret, meta);
		return ret;
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
	public boolean showDurabilityBar(ItemStack aStack) 
	{
		return false;
	}
}