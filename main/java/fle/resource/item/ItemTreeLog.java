package fle.resource.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import flapi.item.ItemFleMetaBase;
import flapi.item.interfaces.IItemBehaviour;
import flapi.item.interfaces.ITreeLog;
import flapi.recipe.stack.AbstractStack;
import fle.core.init.Lang;
import fle.core.init.Plants;
import fle.core.item.behavior.BehaviorBase;
import fle.core.tool.AxeHandler;
import fle.core.tool.AxeHandler.TreeChecker;
import fle.core.util.ItemTextureHandler;

public class ItemTreeLog extends ItemFleMetaBase implements ITreeLog
{
	public ItemTreeLog init() 
	{
		addSubItem(0, "log_void", "Void Log", new TreeChecker(Blocks.log, -1), new ItemStack(Blocks.log), "resource/logs/void");
		addSubItem(1, "log_oak", "Oak Log", new TreeChecker(Blocks.log, 0, 4, 8), new ItemStack(Blocks.log, 1, 0), "resource/logs/oak");
		addSubItem(2, "log_spruce", "Spruce Log", new TreeChecker(Blocks.log, 1, 5, 9), new ItemStack(Blocks.log, 1, 1), "resource/logs/spruce");
		addSubItem(3, "log_birch", "Birch Log", new TreeChecker(Blocks.log, 2, 6, 10), new ItemStack(Blocks.log, 1, 2), "resource/logs/birch");
		addSubItem(4, "log_jungle", "Jungle Log", new TreeChecker(Blocks.log, 3, 7, 11), new ItemStack(Blocks.log, 1, 3), "resource/logs/jungle");
		addSubItem(5, "log_acacia", "Acacia Log", new TreeChecker(Blocks.log2, 0, 4, 8), new ItemStack(Blocks.log2, 1, 0), "resource/logs/acacia");
		addSubItem(6, "log_darkoak", "Dark Oak Log", new TreeChecker(Blocks.log2, 1, 5, 9), new ItemStack(Blocks.log2, 1, 1), "resource/logs/darkoak");
		try
		{
			addSubItem(7, "log_rub", "Rubber Log", new TreeChecker(Block.getBlockFromItem(AxeHandler.IC2RubWood.getItem())), AxeHandler.IC2RubWood, "resource/logs/rub");
		}
		catch(Throwable e)
		{
			;
		}
		addSubItem(8, "log_beech", "Beech Log", new TreeChecker(Plants.beech.log()), new ItemStack(Plants.beech.log()), "resource/logs/beech");
		return this;
	}
	
	public ItemTreeLog(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		setMaxStackSize(1);
	}
	
	private Map<AbstractStack, String> map = new HashMap();
	private Map<String, ItemStack> outputMap = new HashMap();

	public final ItemTreeLog addSubItem(int aMetaValue, String aTagName, String aLocalized, AbstractStack aStack, ItemStack aOutput, String aLocate)
	{
		addSubItem(aMetaValue, aTagName, aLocalized, new ItemTextureHandler(aLocate), new BehaviorBase());
		if(aStack != null)
		{
			map.put(aStack, aTagName);
			outputMap.put(aTagName, aOutput);
		}
		return this;
	}
	
	@Override
	protected void addAdditionalToolTips(List aList, ItemStack aStack)
	{
		super.addAdditionalToolTips(aList, aStack);
		aList.add(new StringBuilder().append(Lang.info_length).append(":").append(setupNBT(aStack).getShort("Length")).toString());
	}
	
	@Override
	public boolean isLog(ItemStack aStack)
	{
		return true;
	}
	
	@Override
	public ItemStack createStandardLog(Block block, int blockMetadata, int length)
	{
		try
		{
			if(AxeHandler.IC2RubWood != null)
			{
				if(Block.getBlockFromItem(AxeHandler.IC2RubWood.getItem()) == block)
				{
					return createStandardLog(7, length);
				}
			}
		}
		catch(Throwable e)
		{
			;
		}
		for(Entry<AbstractStack, String> checker : map.entrySet())
		{
			if(checker.getKey().similar(new ItemStack(block, 1, blockMetadata)))
			{
				return createStandardLog(itemBehaviors.serial(checker.getValue()), length);
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
	
	@Override
	public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List aList)
	{
		for(IItemBehaviour tBehavior : itemBehaviors)
		{
			aList.add(createStandardLog(itemBehaviors.serial(tBehavior), 16));
		}
	}
}