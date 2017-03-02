package nebula.common.util;

public interface ISubTagContainer
{
	/**
	 * Add sub tags.
	 * @param tags
	 */
	void add(SubTag...tags);
	
	/**
	 * Matched is container contain sub tag.
	 * @param tag
	 * @return
	 */
	boolean contain(SubTag tag);
}