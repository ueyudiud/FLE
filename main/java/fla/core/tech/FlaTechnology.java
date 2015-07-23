package fla.core.tech;

import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import fla.api.recipe.IItemChecker;
import fla.api.tech.Page;
import fla.api.tech.PlayerTechInfo;
import fla.api.tech.TechClass;
import fla.api.tech.Technology;

public class FlaTechnology extends Technology
{
	private final String name;
	protected TechClass type;
	protected Page[] pages;
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

	public FlaTechnology setTechClassBelong(TechClass aType)
	{
		type = aType;
		return this;
	}
	
	public FlaTechnology setHide()
	{
		isHide = true;
		return this;
	}
	
	public FlaTechnology setPages(Page...aPages)
	{
		pages = aPages;
		return this;
	}
	
	@Override
	public TechClass getTechClassBelong() 
	{
		return type;
	}

	@Override
	public boolean canTechSelected() 
	{
		return !isHide;
	}

	@Override
	public Page[] getPages()
	{
		return pages;
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
		private IItemChecker ic;
		
		public CraftingCheck(IItemChecker aIc) 
		{
			ic = aIc;
		}
		
		@Override
		public boolean canBeLearned(PlayerTechInfo info, LearningType type, Event evt) 
		{
			if(!super.canBeLearned(info, type, evt)) return false;
			return type == LearningType.Crafting ? ic.match(((ItemCraftedEvent) evt).crafting) : false;
		}
	}
}