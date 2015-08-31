package fle.core.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import fle.api.item.ITreeLog;
import fle.api.item.ItemFleMetaBase;
import fle.api.recipe.ItemAbstractStack;
import fle.core.item.behavior.BehaviorBase;
import fle.core.tool.AxeHandler.TreeChecker;
import fle.core.util.TextureLocation;

public class ItemTreeLog extends ItemFleMetaBase implements ITreeLog
{
	public ItemTreeLog init() 
	{
		addSubItem(1, "log_oak", "Oak Log", new TreeChecker(Blocks.log, 0, 4, 8), new ItemStack(Blocks.log, 1, 0), "logs/oak");
		addSubItem(2, "log_spruce", "Spruce Log", new TreeChecker(Blocks.log, 1, 5, 9), new ItemStack(Blocks.log, 1, 1), "logs/spruce");
		addSubItem(3, "log_birch", "Birch Log", new TreeChecker(Blocks.log, 2, 6, 10), new ItemStack(Blocks.log, 1, 2), "logs/birch");
		addSubItem(4, "log_jungle", "Jungle Log", new TreeChecker(Blocks.log, 3, 7, 11), new ItemStack(Blocks.log, 1, 3), "logs/jungle");
		addSubItem(5, "log_acacia", "Acacia Log", new TreeChecker(Blocks.log2, 0, 4, 8), new ItemStack(Blocks.log2, 1, 0), "logs/acacia");
		addSubItem(6, "log_darkoak", "Dark Oak Log", new TreeChecker(Blocks.log2, 1, 5, 9), new ItemStack(Blocks.log2, 1, 1), "logs/darkoak");
		return this;
	}
	
	public ItemTreeLog(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		setMaxStackSize(1);
	}
	
	private Map<ItemAbstractStack, String> map = new HashMap();
	private Map<String, ItemStack> outputMap = new HashMap();

	public final ItemTreeLog addSubItem(int aMetaValue, String aTagName, String aLocalized, ItemAbstractStack aStack, ItemStack aOutput, String aLocate)
	{
		addSubItem(aMetaValue, aTagName, aLocalized, new TextureLocation(aLocate), new BehaviorBase());
		map.put(aStack, aTagName);
		outputMap.put(aTagName, aOutput);
		return this;
	}
	
	@Override
	protected void addAdditionalToolTips(List aList, ItemStack aStack)
	{
		super.addAdditionalToolTips(aList, aStack);
		aList.add(new StringBuilder().append(StatCollector.translateToLocal("info.length")).append(":").append(setupNBT(aStack).getShort("Length")).toString());
	}
	
	@Override
	public boolean isLog(ItemStack aStack)
	{
		return true;
	}

	@Override
	public ItemStack createStandardLog(Block block, int blockMetadata, int length)
	{
		for(ItemAbstractStack checker : map.keySet())
		{
			if(checker.isStackEqul(new ItemStack(block, 1, blockMetadata)))
			{
				return createStandardLog(itemBehaviors.serial(map.get(checker)), length);
			}
		}
		return null;
	}

	public int getLogLength(ItemStack itemstack) 
	{
		return setupNBT(itemstack).getShort("Length");
	}

	@Override
	public ItemStack getLogDrop(ItemStack stack) 
	{
		int size = getLogLength(stack);
		if(size > 64)
		{
			ItemStack ret = createStandardLog(stack.getItemDamage(), size / 2);
			ret.stackSize = 2;
			return ret;
		}
		ItemStack tStack = outputMap.get(itemBehaviors.name((short) stack.getItemDamage()));
		if(tStack != null)
		{
			ItemStack ret = tStack.copy();
			ret.stackSize = size;
			return ret;
		}
		return null;
	}

	private ItemStack createStandardLog(int itemDamage, int length)
	{
		ItemStack ret = new ItemStack(this, 1, itemDamage);
		setupNBT(ret).setShort("Length", (short) length);
		return ret;
	}
}