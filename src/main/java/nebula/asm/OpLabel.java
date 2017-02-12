/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.asm;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author ueyudiud
 */
abstract class OpLabel
{
	int off;
	int len;
	OpType type;
	List<AbstractInsnNode> nodes;
	
	OpLabel(int off, int len, OpType type, List<AbstractInsnNode> nodes)
	{
		this.off = off;
		this.len = len;
		this.type = type;
		this.nodes = nodes;
	}
	
	abstract boolean matchNode(AbstractInsnNode node);
	
	int performAnchorOperation(InsnList list, int anchor, int insertions)
	{
		AbstractInsnNode current = list.get(anchor + this.off + insertions);
		AbstractInsnNode current1;
		AbstractInsnNode node;
		if (this.nodes != null && this.nodes.size() > 0 && (this.nodes.get(0) instanceof JumpInsnNode))
		{
			this.nodes.set(0, new JumpInsnNode(this.nodes.get(0).getOpcode(), (LabelNode) current.getPrevious()));
		}
		int size = this.nodes == null ? 0 : this.nodes.size();
		switch (this.type)
		{
		case INSERT :
			Iterator<AbstractInsnNode> itr = this.nodes.iterator();
			do
			{
				node = itr.next();
				itr.remove();
				list.insert(current, node);
				current = node;
			}
			while(itr.hasNext());
			return size;
		case INSERT_BEFORE :
			itr = this.nodes.iterator();
			node = itr.next();
			list.insertBefore(current, node);
			current = node;
			while(itr.hasNext())
			{
				node = itr.next();
				list.insert(current, node);
				current = node;
			}
			return size;
		case REPLACE :
			itr = this.nodes.iterator();
			if ((current instanceof JumpInsnNode) && (this.nodes.get(0) instanceof JumpInsnNode))
			{
				((JumpInsnNode) this.nodes.get(0)).label = ((JumpInsnNode) current).label;
			}
			for (int i = 1; i < this.len; ++i)
			{
				list.remove(current.getNext());//Remove length - 1 size of nodes.
			}
			current1 = current;
			do
			{
				node = itr.next();
				itr.remove();
				list.insert(current1, node);
				current1 = node;
			}
			while(itr.hasNext());
			list.remove(current);//Remove last node for mark.
			return size - this.len;
		case REMOVE :
			int i = this.len - 1;
			while(i > 0)
			{
				list.remove(current.getNext());
				--i;
			}
			list.remove(current);
			return - this.len;
		case SWITCH :
			current1 = list.get(anchor + this.off + insertions + this.len);
			list.insert(current, current1);
			current1 = list.get(anchor + this.off + insertions + this.len);
			list.insert(current1, current);
		default :
			return 0;
		}
	}
	
	static class OpLabelLineNumber extends OpLabel
	{
		int line;
		
		OpLabelLineNumber(int line, int off, int len, OpType type, List<AbstractInsnNode> nodes)
		{
			super(off, len, type, nodes);
			this.line = line;
		}
		
		@Override
		boolean matchNode(AbstractInsnNode node)
		{
			return (node instanceof LineNumberNode) && ((LineNumberNode) node).line == this.line;
		}
	}
	
	static class OpLabelMethodAsTag extends OpLabel
	{
		String owner;
		String name;
		String desc;
		int count;
		int i;
		
		OpLabelMethodAsTag(int count, String owner, String name, String desc, int off, int len, OpType type, List<AbstractInsnNode> nodes)
		{
			super(off, len, type, nodes);
			this.count = count;
			this.owner = owner;
			this.name = name;
			this.desc = desc;
		}
		
		@Override
		boolean matchNode(AbstractInsnNode node)
		{
			if (node instanceof MethodInsnNode)
			{
				if (this.name.equals(((MethodInsnNode) node).name) &&
						this.desc.equals(((MethodInsnNode) node).desc) &&
						this.owner.equals(((MethodInsnNode) node).owner))
				{
					return ++this.i == this.count;
				}
			}
			return false;
		}
	}
}