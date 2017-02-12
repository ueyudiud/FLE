/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;

import nebula.common.util.L;

/**
 * @author ueyudiud
 */
class OpInformation
{
	final String mcpname;
	Map<String, List<OpLabel>> modifies = new HashMap();
	String cacheName;
	List<OpLabel> label;
	int line = -1;
	int off = -1;
	int length = 1;
	List<AbstractInsnNode> cacheList;
	@Deprecated
	Map<Label, int[]> labelLocate = new HashMap();
	
	OpInformation(String name)
	{
		this.mcpname = name;
	}
	
	public OpInformation lName(String name)
	{
		this.cacheName = name;
		return this;
	}
	
	public OpInformation lPosition(int line, int off)
	{
		this.line = line;
		this.off = off;
		this.length = 1;
		return this;
	}
	
	public OpInformation lLength(int len)
	{
		this.length = len;
		return this;
	}
	
	public OpInformation lNode(AbstractInsnNode...nodes)
	{
		if(this.cacheList == null)
		{
			this.cacheList = new ArrayList();
		}
		for(AbstractInsnNode node : nodes)
		{
			this.cacheList.add(node);
		}
		return this;
	}
	
	public OpInformation lLabel(OpType type)
	{
		if(this.label == null)
		{
			this.label = new ArrayList();
		}
		this.label.add(new OpLabel.OpLabelLineNumber(this.line, this.off, this.length, type, this.cacheList));
		this.line = -1;
		this.off = -1;
		this.cacheList = null;
		return this;
	}
	
	public OpInformation lPut()
	{
		if(!this.modifies.containsKey(this.cacheName))
		{
			this.modifies.put(this.cacheName, this.label);
		}
		this.cacheName = null;
		this.label = null;
		return this;
	}
	
	public OpInformation insert(int line, int off, boolean isBefore, AbstractInsnNode...nodes)
	{
		return lPosition(line, off).lNode(nodes).lLabel(isBefore ? OpType.INSERT_BEFORE : OpType.INSERT);
	}
	
	public OpInformation remove(int line, int off)
	{
		return remove(line, off, 1);
	}
	
	public OpInformation remove(int line, int off, int length)
	{
		return lPosition(line, off).lLength(length).lLabel(OpType.REMOVE);
	}
	
	public OpInformation replace(int line, int off, AbstractInsnNode...nodes)
	{
		return replace(line, off, 1, nodes);
	}
	public OpInformation replace(int line, int off, int len, AbstractInsnNode...nodes)
	{
		return lPosition(line, off).lLength(len).lNode(nodes).lLabel(OpType.REPLACE);
	}
	
	public void put()
	{
		if (ClassTransformerBase.informations.containsKey(this.mcpname))
		{
			ClassTransformerBase.informations.get(this.mcpname).merge(this);
		}
		else
		{
			ClassTransformerBase.informations.put(this.mcpname, this);
		}
	}
	
	private OpInformation merge(OpInformation information)
	{
		ClassTransformerBase.LOG.warn("Same class " + this.mcpname +
				" type modification detected, this may cause modification "
				+ "failed, please change them if necessary.");
		information.modifies.forEach((key, labels) -> L.put(this.modifies, key, labels));
		return this;
	}
}