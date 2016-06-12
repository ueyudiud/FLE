package fle.override.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import fle.override.FarOverrideLoadingPlugin;
import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer
  implements IClassTransformer
{
	public static final Logger LOG = LogManager.getLogger("FarLandEra ASM");
	private int numInsertions;
	protected Map<String, List<OperationInfo>> mcpMethods = new HashMap();
	protected Map<String, List<OperationInfo>> obfMethods = new HashMap();
	protected String mcpClassName;
	protected String obfClassName;
  
	public ClassTransformer(String mcpName, String obfName)
	{
		this.mcpClassName = mcpName;
		this.obfClassName = obfName;
	}
  
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (name.equals(this.obfClassName) || name.equals(this.mcpClassName)) 
		{
			return transform(bytes);
		}
		return bytes;
	}
  
	protected byte[] transform(byte[] bytes)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		LOG.info("Attempting to Transform: " + classNode.name + " searching for injection...");
		Map<String, List<OperationInfo>> map = getInfos();
		for(MethodNode m : classNode.methods)
		{
			List<OperationInfo> list = map.get(m.name + "|" + m.desc);
			if(list != null)
			{
				list = new ArrayList(list);
				OperationInfo info = null;
				if(!list.isEmpty())
				{
					info = list.get(0);
				}
				else
				{
					LOG.warn("No instructions in method named " + m.name + "()");
				}
				boolean success = false;
				for(int idx = 0; (idx < m.instructions.size() && !list.isEmpty()); ++idx)
				{
					numInsertions = 0;
					while (info != null)
					{
						if (info.count == -1)
						{
							performDirectOperation(m.instructions, info);
							list.remove(0);
						}
						else
						{
							if (!isLineNumber(m.instructions.get(idx), info.count))
							{
								break;
							}
							performAnchorOperation(m.instructions, info, idx);
							AbstractInsnNode node = m.instructions.get(idx + info.offset);
							list.remove(0);
						}
						if (!list.isEmpty())
						{
							info = list.get(0);
						}
						else
						{
							info = null;
						}
					}
				}
				success = list.isEmpty();
				if(success)
				{
					LOG.info("Injected method {" + m.name + "|" + m.desc + "}.");
				}
				else
				{
					LOG.warn("The method {" + m.name + "|" + m.desc + "} might failed to inject, this might cuase game crash.");
				}
			}
		}
		LOG.info("Attempting to Transform: " + classNode.name + " Complete");
		ClassWriter writer = new ClassWriter(1);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	private Map<String, List<OperationInfo>> getInfos()
	{
		return FarOverrideLoadingPlugin.runtimeDeobf ? obfMethods : mcpMethods;
	}
	
	private int findLine(InsnList methodList, int line)
	{
		for (int index = 0; index < methodList.size(); index++)
		{
			if (isLineNumber(methodList.get(index), line))
			{
				return index;
			}
		}
	    return -1;
	}
	  
	private void performDirectOperation(InsnList methodInsn, OperationInfo input)
	{
		AbstractInsnNode current = methodInsn.get(input.offset + numInsertions);
		switch (input.type)
		{
		case InsertAfter: 
			numInsertions += input.list.size();
			methodInsn.insert(current, input.list);
			break;
		case InsertBefore: 
			numInsertions += input.list.size();
			methodInsn.insertBefore(current, input.list);
			break;
		case Remove: 
			numInsertions -= 1;
			methodInsn.remove(current);
			break;
		case Replace: 
			numInsertions += input.list.size() - 1;
			if (((current instanceof JumpInsnNode)) && ((input.list.get(0) instanceof JumpInsnNode)))
			{
				((JumpInsnNode)input.list.get(0)).label = ((JumpInsnNode)current).label;
			}
			methodInsn.insert(current, input.list);
			methodInsn.remove(current);
			break;
		}
	}
	  
	private void performAnchorOperation(InsnList methodInsn, OperationInfo input, int anchor)
	{
		AbstractInsnNode current = methodInsn.get(anchor + input.offset + numInsertions);
		if (input.list.size() > 0 && (input.list.get(0) instanceof JumpInsnNode))
		{
			input.list.set(input.list.get(0), new JumpInsnNode(input.list.get(0).getOpcode(), (LabelNode) current.getPrevious()));
		}
		switch (input.type)
		{
		case InsertAfter: 
			numInsertions += input.list.size();
			methodInsn.insert(current, input.list);
			break;
		case InsertBefore: 
			numInsertions += input.list.size();
			methodInsn.insertBefore(current, input.list);
			break;
		case Remove: 
			numInsertions -= 1;
			methodInsn.remove(current);
			break;
		case Replace: 
			methodInsn.insert(current, input.list);
			methodInsn.remove(current);
			break;
		}
	}
	
	private boolean isLineNumber(AbstractInsnNode current, int line)
	{
		if (current instanceof LineNumberNode)
		{
			int l = ((LineNumberNode) current).line;
			if (l == line)
			{
				return true;
			}
		}
		return false;
	}
	
	public static class OperationInfo
	{
		int count;
		int offset;
		OperationType type;
		InsnList list;

		public OperationInfo(OperationType type, int off, InsnList list)
		{
			this.type = type;
			this.list = list;
			this.offset = off;
			this.count = 0;
		}
		
		public OperationInfo(OperationType type, int off, int start, InsnList list)
		{
			this.type = type;
			this.list = list;
			this.offset = off;
			this.count = start;
		}
		
		public OperationInfo(OperationType type, int off, AbstractInsnNode...nodes)
		{
			this.type = type;
			this.list = new InsnList();
			this.offset = off;
			this.count = 0;
			for(AbstractInsnNode node : nodes)
			{
				list.add(node);
			}
		}
		
		public OperationInfo(OperationType type, int off, int start, AbstractInsnNode...nodes)
		{
			this.type = type;
			this.list = new InsnList();
			this.offset = off;
			this.count = start;
			for(AbstractInsnNode node : nodes)
			{
				list.add(node);
			}
		}
	}
	
	public static enum OperationType
	{
		InsertAfter,
		InsertBefore,
		//Switch,
		Replace,
		Remove;
	}
}