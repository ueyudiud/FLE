/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.util;

import java.util.Collection;
import java.util.HashSet;

import nebula.base.Judgable;
import nebula.base.register.IRegister;
import nebula.base.register.Register;

/**
 * The sub tag, use to mark a item for tags.
 * 
 * @author ueyudiud
 *
 */
public final class SubTag implements Judgable<ISubTagContainer>
{
	public static final IRegister<SubTag> TAGS = new Register<>();
	
	public static void addTagsTo(SubTag[] tags, ISubTagContainer...containers)
	{
		for (ISubTagContainer container : containers)
			if (container != null)
			{
				container.add(tags);
			}
	}
	
	public static SubTag getNewSubTag(String name)
	{
		SubTag tag = TAGS.get(name);
		if (tag == null)
		{
			tag = new SubTag(name);
		}
		return tag;
	}
	
	private final String						name;
	public final Collection<ISubTagContainer>	relevantTaggedItems	= new HashSet(1);
	
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
	
	public SubTag addContainerToList(ISubTagContainer...containers)
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
	
	public SubTag addTo(ISubTagContainer...containers)
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
	public boolean test(ISubTagContainer container)
	{
		return container.contain(this);
	}
}
