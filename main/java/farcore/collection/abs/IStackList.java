package farcore.collection.abs;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Not similar to list. Stack list save target by stack type.<br>
 * The <code>IStackList</code> interface not extends <code>Collection</code>
 * but has similarity method to collection.<br>
 * 
 * @see {@link farcore.collection.ArrayStackList}
 * @author ueyudiud
 *
 * @param <S> Stack type.
 * @param <T> Target type.
 */
public interface IStackList<S, T> extends Iterable<S>
{
	/**
	 * Add target to list, the <code>null</code> element
	 * to list is forbidden. <br>
	 * The element <code>t1</code> which the method <code>t1.equals(t2)</code> 
	 * return true is allowed.<br>
	 * Provider will check added stack first when target
	 * is equals to another target in stack, it will add
	 * in to that stack, and then remainder target will
	 * add into a new stack with size 1.
	 * @param targets
	 */
	void add(T...targets);
	/**
	 * Add a target with a size. (Tip: provider will auto 
	 * create stack type)<br>
	 * The target can not be <code>null</code>, and size 
	 * must be an positive number.<br>
	 * 
	 * @param target
	 * @param size
	 */
	void add(T target, long size);
	/**
	 * Add all stacks to list.
	 * @param stacks
	 */
	void addAll(S...stacks);
	
	/**
	 * Remove all object equals to target.
	 * @param target
	 * @return
	 */
	boolean remove(T target);
	void remove(T...targets);
	S removeAll(S stack);
	List<S> removeAll(S...stacks);
	
	boolean contain(T target);
	boolean contains(S stack);
	
	long species();
	long size();
	long weight(T target);
	
	/**
	 * Get target scale in list.<br>
	 * <code>1</code> is max scale, which means all
	 * elements equals target contain in list.<br>
	 * <code>0</code> is min scale, which means none
	 * elements equals target contain in list,
	 * <code>list.cotain(target)</code> will return false.<br>
	 * @param target
	 * @return A number range from 0 to 1(contain 0 and 1).
	 */
	double scale(T target);
	
	/**
	 * Get an target in list randomly.
	 * @return The result are got randomly.
	 */
	T randomGet();
	T randomGet(Random rand);
	
	/**
	 * Get elements of list by target map.
	 * @return
	 */
	Map<T, Long> toMap();
	/**
	 * Get elements of list by stack list.
	 * @return
	 */
	Stack<T>[] toArray();
}