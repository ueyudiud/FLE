/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.asm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * The class transformer.
 * @author ueyudiud
 *
 */
public class ClassTransformerBase implements IClassTransformer
{
	private static final DecimalFormat FORMAT = new DecimalFormat("000");
	private static final boolean codeOutput = true;
	private static File file;
	private static PrintStream keyOutputStream;
	public static final Logger LOG = LogManager.getLogger("FarCore ASM");
	
	private static final Map<String, OpInformation> informations = new HashMap();
	
	private static void outputInit()
	{
		if(file == null)
		{
			File file = FarOverrideLoadingPlugin.location;
			if(file.isFile())
			{
				file = file.getParentFile();
			}
			ClassTransformerBase.file = new File(file, "far_asm");
			if(!ClassTransformerBase.file.exists())
			{
				ClassTransformerBase.file.mkdirs();
			}
		}
		if(keyOutputStream == null)
		{
			try
			{
				File file = new File(ClassTransformerBase.file, "keys.txt");
				if(!file.exists())
				{
					file.createNewFile();
				}
				keyOutputStream = new PrintStream(new FileOutputStream(file))
				{
					@Override
					protected void finalize() throws Throwable
					{
						close();
					}
				};
			}
			catch (Exception exception)
			{
				keyOutputStream = System.out;
			}
		}
	}
	
	private static void logOutput(String name, InsnList list)
	{
		if(codeOutput)
		{
			BufferedWriter writer = null;
			try
			{
				if(name.indexOf('<') != -1)//<init> method has invalid character for file name.
				{
					name = name.replace('<', '_');
					name = name.replace('>', '_');
				}
				File file1 = new File(file, name + ".txt");
				if(!file1.exists())
				{
					file1.createNewFile();
				}
				writer = new BufferedWriter(new FileWriter(file1));
				int off = 0;
				Iterator<AbstractInsnNode> itr = list.iterator();
				while(itr.hasNext())
				{
					AbstractInsnNode node = itr.next();
					writer.write((off ++) + " " + getOutput(node));
					writer.newLine();
				}
			}
			catch(IOException exception)
			{
				LOG.error("Fail to output asm code of type {" + name + "}", exception);
			}
			finally
			{
				if(writer != null)
				{
					try
					{
						writer.close();
					}
					catch(Exception exception){}
				}
			}
		}
	}
	
	private static String getOutput(AbstractInsnNode node)
	{
		String opcode = FORMAT.format(node.getOpcode() == -1 ? 256 : node.getOpcode());
		switch (node.getType())
		{
		case AbstractInsnNode.VAR_INSN :
			return opcode + " var " + ((VarInsnNode) node).var;
		case AbstractInsnNode.TYPE_INSN :
			return opcode + " type " + ((TypeInsnNode) node).desc;
		case AbstractInsnNode.TABLESWITCH_INSN :
			return opcode + " typelabel " + ((TableSwitchInsnNode) node).min + "," + ((TableSwitchInsnNode) node).max;
		case AbstractInsnNode.MULTIANEWARRAY_INSN :
			return opcode + " array " + ((MultiANewArrayInsnNode) node).desc + "[x" + ((MultiANewArrayInsnNode) node).dims;
		case AbstractInsnNode.METHOD_INSN :
			return opcode + " method " + ((MethodInsnNode) node).owner + "." + ((MethodInsnNode) node).name + " " + ((MethodInsnNode) node).desc;
		case AbstractInsnNode.LOOKUPSWITCH_INSN :
			return opcode + " lookup " + ((LookupSwitchInsnNode) node).dflt.getLabel().toString();
		case AbstractInsnNode.LINE :
			return opcode + " line " + ((LineNumberNode) node).line;
		case AbstractInsnNode.LDC_INSN :
			return opcode + " ldc " + ((LdcInsnNode) node).cst.toString();
		case AbstractInsnNode.LABEL :
			return opcode + " label " + ((LabelNode) node).getLabel().toString();
		case AbstractInsnNode.JUMP_INSN :
			return opcode + " jumplabel " + ((JumpInsnNode) node).label.getLabel().toString();
		case AbstractInsnNode.INVOKE_DYNAMIC_INSN :
			return opcode + " invoke_dynamic " + ((InvokeDynamicInsnNode) node).name + " " + ((InvokeDynamicInsnNode) node).desc;
		case AbstractInsnNode.INT_INSN :
			return opcode + " " + ((IntInsnNode) node).operand;
		case AbstractInsnNode.INSN :
			return opcode;
		case AbstractInsnNode.IINC_INSN :
			return opcode + " iinc " + ((IincInsnNode) node).var + ":" + ((IincInsnNode) node).incr;
		case AbstractInsnNode.FRAME :
			return opcode + " frame " + ((FrameNode) node).type;
		case AbstractInsnNode.FIELD_INSN :
			return opcode + " " + ((FieldInsnNode) node).owner + "." + ((FieldInsnNode) node).name + "." + ((FieldInsnNode) node).desc;
		default : return "";
		}
	}
	
	OpInformation create(String name)
	{
		return new OpInformation(name);
	}
	
	private boolean putedReplacements = false;
	private int numInsertions = 0;
	
	protected void initObfModifies()
	{
		
	}
	
	protected void initMcpModifies()
	{
		
	}
	
	private void init()
	{
		if(!FarOverrideLoadingPlugin.loadedData) return;
		if(!this.putedReplacements)
		{
			this.putedReplacements = true;
			if(FarOverrideLoadingPlugin.runtimeDeobf)
			{
				initObfModifies();
			}
			else
			{
				initMcpModifies();
			}
		}
		outputInit();
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		init();
		if(transformedName.startsWith("net.minecraft."))
		{
			keyOutputStream.println(name + "=" + transformedName);
		}
		OpInformation information;
		if((information = informations.get(transformedName)) != null)
			return modifyClass(transformedName, information, basicClass);
		return basicClass;
	}
	
	public byte[] modifyClass(String clazzName, OpInformation information, byte[] basicClass)
	{
		try
		{
			LOG.info("Far Core start to modify class {}.", clazzName);
			LOG.debug("Checking targets are {}", information.modifies);
			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(basicClass);
			reader.accept(node, 0);
			for(MethodNode node2 : node.methods)
			{
				String name = node2.name + "|" + node2.desc;
				if(information.modifies.containsKey(name))
				{
					LOG.debug("Injecting method  {}.", name);
					logOutput(clazzName + "." + node2.name + "~source", node2.instructions);
					boolean success = modifyMethodNode(node2.instructions, information.modifies.remove(name));
					logOutput(clazzName + "." + node2.name + "~modified", node2.instructions);
					if(!success)
					{
						LOG.info("Injected method {} failed.", name);
					}
					else
					{
						LOG.debug("Injected method {} success.", name);
					}
					if(information.modifies.isEmpty())
					{
						break;
					}
				}
			}
			ClassWriter writer = new ClassWriter(1);
			node.accept(writer);
			LOG.info("End modify class {}.", clazzName);
			return writer.toByteArray();
		}
		catch(Exception exception)
		{
			LOG.error("Fail to modify class.", exception);
			return basicClass;
		}
	}
	
	private boolean modifyMethodNode(InsnList instructions, List<OpLabel> list)
	{
		list = new ArrayList(list);
		OpLabel info = null;
		if(!list.isEmpty())
		{
			info = list.get(0);
		}
		else
			return false;
		for(int idx = 0; (idx < instructions.size() && !list.isEmpty()); ++idx)
		{
			this.numInsertions = 0;
			while (info != null)
			{
				if (!isLine(instructions.get(idx), info.line))
				{
					break;
				}
				performAnchorOperation(instructions, idx, info);
				list.remove(0);
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
		return list.isEmpty();
	}
	
	/**
	 * I don't know what happen, this might is a bug, I can not replace some of nodes.
	 * So I use array list instead insnlist to cached insn nodes.
	 * @param methodInsn
	 * @param anchor
	 * @param input
	 */
	private void performAnchorOperation(InsnList methodInsn, int anchor, OpLabel input)
	{
		AbstractInsnNode current = methodInsn.get(anchor + input.off + this.numInsertions);
		AbstractInsnNode current1;
		AbstractInsnNode node;
		if (input.nodes != null && input.nodes.size() > 0 && (input.nodes.get(0) instanceof JumpInsnNode))
		{
			input.nodes.set(0, new JumpInsnNode(input.nodes.get(0).getOpcode(), (LabelNode) current.getPrevious()));
		}
		switch (input.type)
		{
		case INSERT :
			Iterator<AbstractInsnNode> itr = input.nodes.iterator();
			this.numInsertions += input.nodes.size();
			do
			{
				node = itr.next();
				itr.remove();
				methodInsn.insert(current, node);
				current = node;
			}
			while(itr.hasNext());
			break;
		case INSERT_BEFORE :
			itr = input.nodes.iterator();
			this.numInsertions += input.nodes.size();
			node = itr.next();
			methodInsn.insertBefore(current, node);
			current = node;
			while(itr.hasNext())
			{
				node = itr.next();
				methodInsn.insert(current, node);
				current = node;
			}
			break;
		case REPLACE :
			itr = input.nodes.iterator();
			this.numInsertions += input.nodes.size() - 1;
			if ((current instanceof JumpInsnNode) && (input.nodes.get(0) instanceof JumpInsnNode))
			{
				((JumpInsnNode) input.nodes.get(0)).label = ((JumpInsnNode) current).label;
			}
			current1 = current;
			do
			{
				node = itr.next();
				itr.remove();
				methodInsn.insert(current1, node);
				current1 = node;
			}
			while(itr.hasNext());
			methodInsn.remove(current);
			break;
		case REMOVE :
			int i = input.len;
			while(i > 0)
			{
				current = methodInsn.get(anchor + input.off + this.numInsertions);
				methodInsn.remove(current);
				--i;
			}
			this.numInsertions -= input.len;
			break;
		case SWITCH :
			current1 = methodInsn.get(anchor + input.off + this.numInsertions + input.len);
			methodInsn.insert(current, current1);
			current1 = methodInsn.get(anchor + input.off + this.numInsertions + input.len);
			methodInsn.insert(current1, current);
			break;
		}
	}
	
	private int findLine(InsnList methodList, int line)
	{
		for (int index = 0; index < methodList.size(); index++)
		{
			if (isLine(methodList.get(index), line))
				return index;
		}
		return -1;
	}
	
	private boolean isLine(AbstractInsnNode current, int line)
	{
		if (current instanceof LineNumberNode)
		{
			int l = ((LineNumberNode) current).line;
			if (l == line)
				return true;
		}
		return false;
	}
	
	public static enum OpType
	{
		INSERT,
		INSERT_BEFORE,
		REPLACE,
		REMOVE,
		SWITCH;
	}
	
	protected class OpInformation
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
			this.label.add(new OpLabel(this.line, this.off, this.length, type, this.cacheList));
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
			informations.put(this.mcpname, this);
		}
	}
	
	protected class OpLabel
	{
		int line;
		int off;
		int len;
		OpType type;
		List<AbstractInsnNode> nodes;
		
		OpLabel(int line, int off, int len, OpType type, List<AbstractInsnNode> nodes)
		{
			this.line = line;
			this.off = off;
			this.len = len;
			this.type = type;
			this.nodes = nodes;
		}
		
		@Override
		public String toString()
		{
			return "label:" + this.type.name() + ":" + (this.nodes != null ? Arrays.toString(this.nodes.toArray()) : "");
		}
	}
}