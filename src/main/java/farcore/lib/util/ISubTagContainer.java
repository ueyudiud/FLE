package farcore.lib.util;

public interface ISubTagContainer
{
	void add(SubTag...tags);

	boolean contain(SubTag tag);
}