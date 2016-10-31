package farcore.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

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
import org.objectweb.asm.tree.InsnNode;
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

public class ClassTransformerModifyMethod implements IClassTransformer
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
			ClassTransformerModifyMethod.file = new File(file, "far_asm");
			if(!ClassTransformerModifyMethod.file.exists())
			{
				ClassTransformerModifyMethod.file.mkdirs();
			}
		}
		if(keyOutputStream == null)
		{
			try
			{
				File file = new File(ClassTransformerModifyMethod.file, "keys.txt");
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

	private boolean putedReplacements = false;
	private int numInsertions = 0;

	private void init()
	{
		if(!FarOverrideLoadingPlugin.loadedData) return;
		if(!putedReplacements)
		{
			putedReplacements = true;
			if(FarOverrideLoadingPlugin.runtimeDeobf)
			{
				create("net.minecraft.util.text.TextComponentTranslation")
				.lName("g|()V")
				.lPosition(51, 5)
				.lNode(new InsnNode(ICONST_0),
						new TypeInsnNode(ANEWARRAY, "java/lang/Object"),
						new MethodInsnNode(INVOKESTATIC, "farcore/lib/util/LanguageManager", "translateToLocal", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraftforge.client.model.ModelLoader")
				.lName("onRegisterAllBlocks|(Lbou;)V")
				.lPosition(1079, 0)
				.lNode(new VarInsnNode(ALOAD, 0),
						new MethodInsnNode(INVOKESTATIC, "farcore/FarCoreSetup$ClientProxy", "onRegisterAllBlocks", "(Lbou;)V", false))
				.lLabel(OpType.INSERT)
				.lPut()
				.put();
				create("net.minecraft.world.chunk.Chunk")
				.lName("a|(Lcm;Laiu;)Laiq;")
				.lPosition(1202, 3)
				.lNode(new VarInsnNode(ALOAD, 1),
						new VarInsnNode(ALOAD, 2),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "regetBiome", "(ILcm;Laiu;)Laiq;", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraft.client.renderer.EntityRenderer")
				.lName("e|()V")
				.lPosition(416, 2)
				.lNode(new FieldInsnNode(GETFIELD, "bnz", "j", "Ljava/util/Random;"),
						new VarInsnNode(ALOAD, 0),
						new FieldInsnNode(GETFIELD, "bnz", "N", "I"),
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderDropOnGround", "(Ljava/util/Random;I)V", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraft.client.renderer.RenderItem")
				.lName("a|(Lbyl;ILadz;)V")
				.lPosition(160, 4)
				.lLength(2)
				.lLabel(OpType.REMOVE)
				.lPosition(160, 8)
				.lNode(new VarInsnNode(ALOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lbyl;Lct;JLadz;)Ljava/util/List;", false))
				.lLabel(OpType.REPLACE)
				.lPosition(163, 5)
				.lLength(2)
				.lLabel(OpType.REMOVE)
				.lPosition(163, 10)
				.lNode(new VarInsnNode(ALOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lbyl;Lct;JLadz;)Ljava/util/List;", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraft.client.gui.inventory.GuiContainer")
				.lName("a|(Ladz;IILjava/lang/String;)V")
				.lPosition(206, 19)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lbsu;Lbdl;Ladz;IILjava/lang/String;)V", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.lName("a|(Lacc;)V")
				.lPosition(299, 9)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lbsu;Lbdl;Ladz;IILjava/lang/String;)V", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraft.world.World")
				.lName("B|(Lcm;)Z")
				.lPosition(3590, 18)
				.lNode(new VarInsnNode(ALOAD, 0),
						new VarInsnNode(ALOAD, 1),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "isRainingAtBiome", "(Laiq;Laid;Lcm;)Z", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.lName("w|(Lcm;)Z")
				.lPosition(2774, 5)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
				.lLabel(OpType.REPLACE)
				.lPosition(2777, 6)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.lName("a|(IIII)V")
				.lPosition(438, 9)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.lName("d|()V")
				.lPosition(2543, 2)
				.lNode(new VarInsnNode(ALOAD, 0))
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "tickLightUpdate", "(Laid;)V", false))
				.lLabel(OpType.INSERT)
				.put();
			}
			else
			{
				create("net.minecraft.util.text.TextComponentTranslation")
				.lName("ensureInitialized|()V")
				.lPosition(60, 5)
				.lNode(new InsnNode(ICONST_0),
						new TypeInsnNode(ANEWARRAY, "java/lang/Object"),
						new MethodInsnNode(INVOKESTATIC, "farcore/lib/util/LanguageManager", "translateToLocal", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraftforge.client.model.ModelLoader")
				.lName("onRegisterAllBlocks|(Lnet/minecraft/client/renderer/BlockModelShapes;)V")
				.lPosition(1079, 0)
				.lNode(new VarInsnNode(ALOAD, 0),
						new MethodInsnNode(INVOKESTATIC, "farcore/FarCoreSetup$ClientProxy", "onRegisterAllBlocks", "(Lnet/minecraft/client/renderer/BlockModelShapes;)V", false))
				.lLabel(OpType.INSERT)
				.lPut()
				.put();
				create("net.minecraft.world.chunk.Chunk")
				.lName("getBiome|(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;")
				.lPosition(1278, 3)
				.lNode(new VarInsnNode(ALOAD, 1),
						new VarInsnNode(ALOAD, 2),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "regetBiome", "(ILnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraft.client.renderer.EntityRenderer")
				.lName("updateRenderer|()V")
				.lPosition(325, 2)
				.lNode(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "random", "Ljava/util/Random;"),
						new VarInsnNode(ALOAD, 0),
						new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "rainSoundCounter", "I"),
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderDropOnGround", "(Ljava/util/Random;I)V", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraft.client.renderer.RenderItem")
				.lName("renderModel|(Lnet/minecraft/client/renderer/block/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V")
				.lPosition(133, 4)
				.lLength(2)
				.lLabel(OpType.REMOVE)
				.lPosition(133, 8)
				.lNode(new VarInsnNode(ALOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false))
				.lLabel(OpType.REPLACE)
				.lPosition(136, 5)
				.lLength(2)
				.lLabel(OpType.REMOVE)
				.lPosition(136, 10)
				.lNode(new VarInsnNode(ALOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraft.client.gui.inventory.GuiContainer")
				.lName("drawItemStack|(Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
				.lPosition(206, 19)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.lName("drawSlot|(Lnet/minecraft/inventory/Slot;)V")
				.lPosition(299, 9)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.put();
				create("net.minecraft.world.World")
				.lName("isRainingAt|(Lnet/minecraft/util/math/BlockPos;)Z")
				.lPosition(3882, 18)
				.lNode(new VarInsnNode(ALOAD, 0),
						new VarInsnNode(ALOAD, 1),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "isRainingAtBiome", "(Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.lName("checkLight|(Lnet/minecraft/util/math/BlockPos;)Z")
				.lPosition(2984, 5)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
				.lLabel(OpType.REPLACE)
				.lPosition(2987, 6)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.lName("markBlocksDirtyVertical|(IIII)V")
				.lPosition(500, 9)
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
				.lLabel(OpType.REPLACE)
				.lPut()
				.lName("tick|()V")
				.lPosition(2741, 2)
				.lNode(new VarInsnNode(ALOAD, 0))
				.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "tickLightUpdate", "(Lnet/minecraft/world/World;)V", false))
				.lLabel(OpType.INSERT)
				.lPut()
				.put();
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
			LOG.info("Far Core start to modify class {" + clazzName + "}.");
			LOG.debug("Checking targets are {" + information.modifies + "}");
			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(basicClass);
			reader.accept(node, 0);
			for(MethodNode node2 : node.methods)
			{
				String name = node2.name + "|" + node2.desc;
				if(information.modifies.containsKey(name))
				{
					LOG.debug("Injecting method  {" + name + "}.");
					logOutput(clazzName + "." + node2.name + "~source", node2.instructions);
					boolean success = modifyMethodNode(node2.instructions, information.modifies.remove(name));
					logOutput(clazzName + "." + node2.name + "~modified", node2.instructions);
					if(!success)
					{
						LOG.info("Injected method {" + name + "} failed.");
					}
					else
					{
						LOG.debug("Injected method {" + name + "} success.");
					}
					if(information.modifies.isEmpty())
					{
						break;
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
	
	/**
	 * I don't know what happen, this might is a bug, I can not replace some of nodes.
	 * So I use array list instead insnlist to cached insn nodes.
	 * @param methodInsn
	 * @param anchor
	 * @param input
	 */
	private void performAnchorOperation(InsnList methodInsn, int anchor, OpLabel input)
	{
		AbstractInsnNode current = methodInsn.get(anchor + input.off + numInsertions);
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
			numInsertions += input.nodes.size();
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
			numInsertions += input.nodes.size();
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
			numInsertions += input.nodes.size() - 1;
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
				current = methodInsn.get(anchor + input.off + numInsertions);
				methodInsn.remove(current);
				--i;
			}
			numInsertions -= input.len;
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
			mcpname = name;
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
		
		public OpInformation lNode(AbstractInsnNode...nodes)
		{
			if(cacheList == null)
			{
				cacheList = new ArrayList();
			}
			for(AbstractInsnNode node : nodes)
			{
				cacheList.add(node);
			}
			return this;
		}
		
		public OpInformation lLabel(OpType type)
		{
			if(label == null)
			{
				label = new ArrayList();
			}
			label.add(new OpLabel(line, off, length, type, cacheList));
			line = -1;
			off = -1;
			cacheList = null;
			return this;
		}

		public OpInformation lPut()
		{
			if(!modifies.containsKey(cacheName))
			{
				modifies.put(cacheName, label);
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
			return "label:" + type.name() + ":" + (nodes != null ? Arrays.toString(nodes.toArray()) : "");
		}
	}
}