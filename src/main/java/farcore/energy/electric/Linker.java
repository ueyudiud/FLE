/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.electric;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import nebula.base.collection.A;

/**
 * @author ueyudiud
 */
class Linker
{
	int N;
	LongList list = new LongArrayList();
	IntSet[] links;
	Set<IntSet> graphs = new HashSet<>();
	Int2ObjectMap<IntSet> nodeToGraphMap = new Int2ObjectLinkedOpenHashMap<>();
	
	Linker(int count)
	{
		this.N = count;
		A.fill(this.links = new IntSet[this.N], (Supplier<IntArraySet>) IntArraySet::new);
	}
	
	void bind(int l, int r)
	{
		assert l != r;
		this.links[l].add(r);
		this.links[r].add(l);
		linkGraph(l, r);
		this.list.add(l > r ? (long) l << 32 | r : (long) r << 32 | l);
	}
	
	private void linkGraph(int l, int r)
	{
		if (this.nodeToGraphMap.containsKey(r))
		{
			if (this.nodeToGraphMap.containsKey(l))
			{
				IntSet g1 = this.nodeToGraphMap.get(l);
				IntSet g2 = this.nodeToGraphMap.get(r);
				g1.addAll(g2);
				this.nodeToGraphMap.putAll(Maps.asMap(g2, __ -> g1));
				this.graphs.remove(g2);
			}
			else
			{
				IntSet graph = this.nodeToGraphMap.get(r);
				graph.add(l);
				this.nodeToGraphMap.put(l, graph);
			}
		}
		else
		{
			IntSet graph = getGraph(l);
			graph.add(r);
			this.nodeToGraphMap.put(r, graph);
		}
	}
	
	private IntSet getGraph(int i)
	{
		if (this.nodeToGraphMap.containsKey(i))
		{
			return this.nodeToGraphMap.get(i);
		}
		else
		{
			IntSet r = new IntArraySet();
			r.add(i);
			this.graphs.add(r);
			this.nodeToGraphMap.put(i, r);
			return r;
		}
	}
}
