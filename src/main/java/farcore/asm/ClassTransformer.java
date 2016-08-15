package farcore.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
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

public class ClassTransformer implements IClassTransformer
{
	private static final DecimalFormat FORMAT = new DecimalFormat("000");
	private static final boolean codeOutput = true;
	private static File file;
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
			ClassTransformer.file = new File(file, "far_asm");
			if(!ClassTransformer.file.exists())
			{
				ClassTransformer.file.mkdirs();
			}
		}
	}

	private static void logOutput(String name, InsnList list)
	{
		if(codeOutput)
		{
			outputInit();
			BufferedWriter writer = null;
			try
			{
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

	private OpInformation create(String name)
	{
		return new OpInformation(name);
	}
	
	private int numInsertions;
	
	public ClassTransformer()
	{
		create("net.minecraft.world.chunk.Chunk")
		.lName("getBiome|(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;")
		.lPosition(1278, 3)
		.lNode(new VarInsnNode(ALOAD, 1))
		.lNode(new VarInsnNode(ALOAD, 2))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "regetBiome", "(ILnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;", false))
		.lLabel(OpType.REPLACE)
		.lPut(false)
		.lName("a|(Lcm;Laiu;)Laiq;")
		.lPosition(1202, 3)
		.lNode(new VarInsnNode(ALOAD, 1))
		.lNode(new VarInsnNode(ALOAD, 2))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "regetBiome", "(ILcm;Laiu;)Laiq;", false))
		.lLabel(OpType.REPLACE)
		.lPut(true)
		.put();
		create("net.minecraft.client.renderer.EntityRenderer")
		.lName("updateRenderer|()V")
		.lPosition(325, 2)
		.lNode(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "random", "Ljava/util/Random;"))
		.lNode(new VarInsnNode(ALOAD, 0))
		.lNode(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "rendererUpdateCount", "I"))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderDropOnGround", "(Ljava/util/Random;I)V", false))
		.lLabel(OpType.REPLACE)
		.lPut(false)
		.lName("e|()V")
		.lPosition(416, 2)
		.lNode(new FieldInsnNode(GETFIELD, "bnz", "random", "Ljava/util/Random;"))
		.lNode(new VarInsnNode(ALOAD, 0))
		.lNode(new FieldInsnNode(GETFIELD, "bnz", "rendererUpdateCount", "I"))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderDropOnGround", "(Ljava/util/Random;I)V", false))
		.lLabel(OpType.REPLACE)
		.lPut(true)
		.put();
		create("net.minecraft.client.renderer.RenderItem")
		.lName("renderModel|(Lnet/minecraft/client/renderer/block/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V")
		.lPosition(133, 4)
		.lLength(2)
		.lLabel(OpType.REMOVE)
		.lPosition(133, 8)
		.lNode(new VarInsnNode(ALOAD, 3))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false))
		.lLabel(OpType.REPLACE)
		.lPosition(136, 5)
		.lLength(2)
		.lLabel(OpType.REMOVE)
		.lPosition(136, 10)
		.lNode(new VarInsnNode(ALOAD, 3))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false))
		.lLabel(OpType.REPLACE)
		.lPut(false)
		.put();
		create("net.minecraft.world.World")
		.lName("isRainingAt|(Lnet/minecraft/util/math/BlockPos;)Z")
		.lPosition(3882, 17)
		.lLabel(OpType.REMOVE)
		.lPosition(3882, 18)
		.lNode(new VarInsnNode(ALOAD, 2))
		.lNode(new VarInsnNode(ALOAD, 0))
		.lNode(new VarInsnNode(ALOAD, 1))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "isRainingAtBiome", "(Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPut(false)
		.lName("B|(Lcm;)Z")
		.lPosition(3590, 17)
		.lLabel(OpType.REMOVE)
		.lPosition(3590, 18)
		.lNode(new VarInsnNode(ALOAD, 2))
		.lNode(new VarInsnNode(ALOAD, 0))
		.lNode(new VarInsnNode(ALOAD, 1))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "isRainingAtBiome", "(Laiq;Laid;Lcm;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPut(true)
		.lName("checkLight|(Lnet/minecraft/util/math/BlockPos;)Z")
		.lPosition(2984, 5)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPosition(2987, 6)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPut(false)
		.lName("w|(Lcm;)Z")
		.lPosition(2774, 5)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPosition(2777, 6)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPut(true)
		.lName("markBlocksDirtyVertical|(IIII)V")
		.lPosition(500, 9)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPut(false)
		.lName("a|(IIII)V")
		.lPosition(438, 9)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPut(true)
		.lName("tick|()V")
		.lPosition(2741, 2)
		.lNode(new VarInsnNode(ALOAD, 0))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "tickLightUpdate", "(Lnet/minecraft/world/World;)V", false))
		.lLabel(OpType.INSERT)
		.lPut(false)
		.lName("d|()V")
		.lPosition(2543, 2)
		.lNode(new VarInsnNode(ALOAD, 0))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "tickLightUpdate", "(Laid;)V", false))
		.lLabel(OpType.INSERT)
		.put();
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		OpInformation information;
		if((information = informations.get(transformedName)) != null)
			return modifyClass(transformedName, information, basicClass);
		return basicClass;
	}

	public byte[] modifyClass(String clazzName, OpInformation information, byte[] basicClass)
	{
		try
		{
			LOG.info("Far Core start to modify class {" + clazzName + "}.");
			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(basicClass);
			reader.accept(node, 0);
			for(MethodNode node2 : node.methods)
			{
				String name = node2.name + "|" + node2.desc;
				if(information.modifymap().containsKey(name))
				{
					LOG.debug("Injecting method  {" + name + "}.");
					logOutput(clazzName + "." + node2.name + "~source", node2.instructions);
					boolean success = modifyMethodNode(node2.instructions, information.modifymap().get(name));
					logOutput(clazzName + "." + node2.name + "~modified", node2.instructions);
					if(!success)
					{
						LOG.info("Injected method {" + clazzName + "} failed.");
					}
					else
					{
						LOG.debug("Injected method {" + clazzName + "} success.");
					}
				}
			}
			ClassWriter writer = new ClassWriter(1);
			node.accept(writer);
			LOG.info("End modify class {" + clazzName + "}.");
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
			numInsertions = 0;
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

	private void performAnchorOperation(InsnList methodInsn, int anchor, OpLabel input)
	{
		AbstractInsnNode current = methodInsn.get(anchor + input.off + numInsertions);
		AbstractInsnNode current1;
		if (input.nodes.size() > 0 && (input.nodes.get(0) instanceof JumpInsnNode))
		{
			input.nodes.set(input.nodes.get(0), new JumpInsnNode(input.nodes.get(0).getOpcode(), (LabelNode) current.getPrevious()));
		}
		switch (input.type)
		{
		case INSERT :
			numInsertions += input.nodes.size();
			methodInsn.insert(current, input.nodes);
			break;
		case INSERT_BEFORE :
			numInsertions += input.nodes.size();
			methodInsn.insertBefore(current, input.nodes);
			break;
		case REMOVE :
			int i = input.len;
			while(i > 0)
			{
				current = methodInsn.get(anchor + input.off + numInsertions);
				methodInsn.remove(current);
				--i;
			}
			numInsertions -= input.len;
			break;
		case REPLACE :
			numInsertions += input.nodes.size() - 1;
			if ((current instanceof JumpInsnNode) && (input.nodes.get(0) instanceof JumpInsnNode))
			{
				((JumpInsnNode)input.nodes.get(0)).label = ((JumpInsnNode)current).label;
			}
			methodInsn.insert(current, input.nodes);
			methodInsn.remove(current);
			break;
		case SWITCH :
			current1 = methodInsn.get(anchor + input.off + numInsertions + input.len);
			methodInsn.insert(current, current1);
			current1 = methodInsn.get(anchor + input.off + numInsertions + input.len);
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
		Map<String, List<OpLabel>> modifiesmcp = new HashMap();
		Map<String, List<OpLabel>> modifiesobf = new HashMap();
		String cacheName;
		List<OpLabel> label;
		int line = -1;
		int off = -1;
		int length = 1;
		InsnList cacheList;
		
		OpInformation(String name)
		{
			mcpname = name;
		}
		
		Map<String, List<OpLabel>> modifymap()
		{
			return FarOverrideLoadingPlugin.runtimeDeobf ? modifiesobf : modifiesmcp;
		}

		public OpInformation lName(String name)
		{
			cacheName = name;
			return this;
		}
		
		public OpInformation lPosition(int line, int off)
		{
			this.line = line;
			this.off = off;
			length = 1;
			return this;
		}

		public OpInformation lLength(int len)
		{
			length = len;
			return this;
		}

		public OpInformation lNode(AbstractInsnNode node)
		{
			if(cacheList == null)
			{
				cacheList = new InsnList();
			}
			cacheList.add(node);
			return this;
		}

		public OpInformation lLabel(OpType type)
		{
			if(label == null)
			{
				label = new ArrayList();
			}
			label.add(new OpLabel(line, off, length, type, cacheList == null ? new InsnList() : cacheList));
			line = -1;
			off = -1;
			cacheList = null;
			return this;
		}
		
		public OpInformation lPut(boolean isObf)
		{
			if(!(isObf ? modifiesobf : modifiesmcp).containsKey(cacheName))
			{
				(isObf ? modifiesobf : modifiesmcp).put(cacheName, label);
			}
			cacheName = null;
			label = null;
			return this;
		}
		
		public void put()
		{
			informations.put(mcpname, this);
		}
	}

	protected class OpLabel
	{
		int line;
		int off;
		int len;
		OpType type;
		InsnList nodes;

		OpLabel(int line, int off, int len, OpType type, InsnList nodes)
		{
			this.line = line;
			this.off = off;
			this.len = len;
			this.type = type;
			this.nodes = nodes;
		}
	}
}