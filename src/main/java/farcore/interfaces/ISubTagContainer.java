package farcore.interfaces;

import farcore.util.SubTag;

public interface ISubTagContainer
{
	void add(SubTag...tags);

	boolean contain(SubTag tag);
}