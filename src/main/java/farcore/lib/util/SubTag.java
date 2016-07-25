package farcore.lib.util;

import java.util.Collection;
import java.util.HashSet;

import farcore.lib.collection.Register;

public class SubTag implements IDataChecker<ISubTagContainer>
{
	private static final Register<SubTag> subTags = new Register();

	public static final SubTag WOOD = getNewSubTag("WOOD");
	public static final SubTag DIRT = getNewSubTag("DIRT");
	public static final SubTag SAND = getNewSubTag("SAND");
	public static final SubTag ROCK = getNewSubTag("ROCK");
	public static final SubTag TOOL = getNewSubTag("TOOL");
	public static final SubTag HANDLE = getNewSubTag("HANDLE");
	public static final SubTag CROP = getNewSubTag("SEED");
	public static final SubTag PLANT = getNewSubTag("PLANT");
	
	public static void addTagsTo(SubTag[] tags, ISubTagContainer...containers)
	{
		for(ISubTagContainer container : containers)
			if(container != null)
			{
				container.add(tags);
			}
	}
	
	public static SubTag getNewSubTag(String name)
	{
		for (SubTag tSubTag : subTags)
			if (tSubTag.name.equals(name))
				return tSubTag;
		return new SubTag(name);
	}
	
	private final String name;
	public final Collection<ISubTagContainer> relevantTaggedItems = new HashSet(1);
	
	private SubTag(String name)
	{
		this.name = name;
		subTags.register(name, this);
	}
	
	public String name()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	public SubTag addContainerToList(ISubTagContainer... containers)
	{
		if (containers != null)
		{
			for (ISubTagContainer container : containers)
				if ((container != null) && (!relevantTaggedItems.contains(container)))
				{
					relevantTaggedItems.add(container);
				}
		}
		return this;
	}
	
	public SubTag addTo(ISubTagContainer... containers)
	{
		if (containers != null)
		{
			for (ISubTagContainer container : containers)
				if (container != null)
				{
					container.add(new SubTag[] { this });
				}
		}
		return this;
	}

	@Override
	public boolean isTrue(ISubTagContainer container)
	{
		return container.contain(this);
	}
}