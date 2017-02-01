package nebula.common.util;

import java.util.Collection;
import java.util.HashSet;

import nebula.common.base.Register;

public final class SubTag implements IDataChecker<ISubTagContainer>
{
	public static final Register<SubTag> TAGS = new Register();
	
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
		for (SubTag tSubTag : TAGS)
			if (tSubTag.name.equals(name))
				return tSubTag;
		return new SubTag(name);
	}
	
	private final String name;
	public final Collection<ISubTagContainer> relevantTaggedItems = new HashSet(1);
	
	private SubTag(String name)
	{
		this.name = name;
		TAGS.register(name, this);
	}
	
	public String name()
	{
		return this.name;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	public SubTag addContainerToList(ISubTagContainer... containers)
	{
		if (containers != null)
		{
			for (ISubTagContainer container : containers)
				if ((container != null) && (!this.relevantTaggedItems.contains(container)))
				{
					this.relevantTaggedItems.add(container);
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
					container.add(this);
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