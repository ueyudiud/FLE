/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

/**
 * The sub tag container, use to be predicated by
 * sub tag.
 * @author ueyudiud
 * @see SubTag
 */
public interface ISubTagContainer
{
	/**
	 * Add sub tags to container.<p>
	 * The same tag register twice will not take effect.
	 * @param tags
	 */
	void add(SubTag...tags);
	
	/**
	 * Removes the specified element from container.
	 * @param tag
	 * @return Return true when tag is present.
	 */
	boolean remove(SubTag tag);
	
	/**
	 * Matched is container contain sub tag.
	 * @param tag
	 * @return
	 */
	boolean contain(SubTag tag);
}