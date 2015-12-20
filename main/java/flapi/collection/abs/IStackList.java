package flapi.collection.abs;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Not similar to list. Stack list save target by stack type.<br>
 * The <code>IStackList</code> interface not extends <code>Collection</code>
 * but has similarity method to collection.<br>
 * 
 * @see {@link flapi.collection.ArrayStackList}
 * @author ueyudiud
 *
 * @param <S> Stack type.
 * @param <T> Target type.
 */
public interface IStackList<S, T> extends Iterable<S>
{
	/**
	 * Add target to list, you can't add <code>null</code>
	 * to list. But you can add <code>t1</code> which the 
	 * method <code>t1.equals(t2)</code> return true.
	 * @param targets
	 */
	void add(T...targets);
	/**
	 * Add an target with a size.<br>
	 * The target can not be <code>null</code>.
	 * @param target
	 * @param size
	 */
	void add(T target, int size);
	/**
	 * Add stacks to list.
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
	
	int species();
	int size();
	int weight(T target);
	
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
	Map<T, Integer> toMap();
	/**
	 * Get elements of list by stack list.
	 * @return
	 */
	Stack<T>[] toArray();
}