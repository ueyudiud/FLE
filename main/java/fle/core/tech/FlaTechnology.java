package fle.core.tech;

import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import fle.api.recipe.ItemAbstractStack;
import fle.api.tech.ITechTag;
import fle.api.tech.PlayerTechInfo;
import fle.api.tech.Technology;

public class FlaTechnology extends Technology
{
	private final String name;
	protected ITechTag type;
	protected boolean isHide = false;
	
	public FlaTechnology(String aName)
	{
		name = aName;
	}

	@Override
	public final String getName() 
	{
		return name;
	}

	public FlaTechnology setTechClassBelong(ITechTag aType)
	{
		type = aType;
		return this;
	}
	
	public FlaTechnology setHide()
	{
		isHide = true;
		return this;
	}
	
	@Override
	public ITechTag getTechClassBelong() 
	{
		return type;
	}

	@Override
	public boolean canTechSelected() 
	{
		return !isHide;
	}
	
	public FlaTechnology setLearningTypes(LearningType...learningTypes)
	{
		types = learningTypes;
		return this;
	}
	
	public FlaTechnology setLearningCheck(LearningCheck check)
	{
		checker = check;
		return this;
	}
	
	private LearningType[] types;
	private LearningCheck checker;

	@Override
	public LearningType[] getLearningTypes()
	{
		return types == null ? new LearningType[]{} : types;
	}
	
	@Override
	public boolean canBeLearned(PlayerTechInfo info, LearningType type, Event evt)
	{
		return checker != null ? checker.canBeLearned(info, type, evt) : false;
	}

	public static class BreakBlockCheck extends LearningCheck
	{
		private int meta;
		private Block block;

		public BreakBlockCheck(Block aBlock) 
		{
			this(aBlock, OreDictionary.WILDCARD_VALUE);
		}
		public BreakBlockCheck(Block aBlock, int aMetadata) 
		{
			meta = aMetadata;
			block = aBlock;
		}
		
		@Override
		public boolean canBeLearned(PlayerTechInfo info, LearningType type, Event evt) 
		{
			if(!super.canBeLearned(info, type, evt)) return false;
			return type == LearningType.Break ? ((BreakEvent) evt).block == block && (((BreakEvent) evt).blockMetadata == meta || meta == OreDictionary.WILDCARD_VALUE): false;
		}
	}
	
	public static class CraftingCheck extends LearningCheck
	{
		private ItemAbstractStack ic;
		
		public CraftingCheck(ItemAbstractStack aIc) 
		{
			ic = aIc;
		}
		
		@Override
		public boolean canBeLearned(PlayerTechInfo info, LearningType type, Event evt) 
		{
			if(!super.canBeLearned(info, type, evt)) return false;
			return type == LearningType.Crafting ? ic.isStackEqul(((ItemCraftedEvent) evt).crafting) : false;
		}
	}
}