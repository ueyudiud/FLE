/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.asm;

import static org.objectweb.asm.Opcodes.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;
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
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import nebula.NebulaLoadingPlugin;

/**
 * @author ueyudiud
 */
public class NebulaASMLogHelper
{
	private static final DecimalFormat FORMAT = new DecimalFormat("000");
	public static final Logger LOG = LogManager.getLogger("Nebula ASM");
	
	private static final boolean codeOutput = true;
	
	static File file;
	
	static void outputInit()
	{
		if (file == null)
		{
			file = new File(new File(NebulaLoadingPlugin.location, "asm"), "logs");
			if (!file.exists())
			{
				file.mkdirs();
			}
			else
			{
				try
				{
					FileUtils.cleanDirectory(file);
				}
				catch (IOException exception)
				{
					LOG.warn("Failed to setup log file, will override on old file.", exception);
				}
			}
		}
		if (keyOutputStream == null)
		{
			try
			{
				File file = new File(NebulaASMLogHelper.file, "keys.txt");
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
				NebulaASMLogHelper.keyOutputStream = System.out;
			}
		}
	}
	
	static PrintStream keyOutputStream;
	
	static void logMethods(String name, List<String> methods)
	{
		if(NebulaASMLogHelper.codeOutput)
		{
			BufferedWriter writer = null;
			try
			{
				File file1 = new File(file, name + "_methods.txt");
				if(!file1.exists())
				{
					file1.createNewFile();
				}
				writer = new BufferedWriter(new FileWriter(file1));
				for(String method : methods)
				{
					writer.write(method);
					writer.newLine();
				}
			}
			catch(IOException exception)
			{
				LOG.error("Fail to output methods list of type {}", exception, name);
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
	
	static void logOutput(String name, String postfix, String fullName, InsnList list)
	{
		if(NebulaASMLogHelper.codeOutput)
		{
			BufferedWriter writer = null;
			try
			{
				if(name.indexOf('<') != -1)//<init> method has invalid character for file name.
				{
					name = name.replace('<', '_');
					name = name.replace('>', '_');
				}
				File file1 = new File(file, name + "~" + postfix + ".txt");
				if(!file1.exists())
				{
					file1.createNewFile();
				}
				else
				{
					int i = 1;
					while (file1.exists())
					{
						file1 = new File(file, name + "$" + i + "~" + postfix + ".txt");
					}
					file1.createNewFile();
				}
				writer = new BufferedWriter(new FileWriter(file1));
				writer.write(fullName);
				writer.newLine();
				int off = 0;
				Iterator<AbstractInsnNode> itr = list.iterator();
				while(itr.hasNext())
				{
					AbstractInsnNode node = itr.next();
					writer.write((off ++) + " " + NebulaASMLogHelper.getOutput(node));
					writer.newLine();
				}
			}
			catch(IOException exception)
			{
				LOG.error("Fail to output asm code of type {}", exception, name);
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
		String opcode;
		switch (node.getOpcode())
		{
		case BIPUSH : opcode = "BIPUSH"; break;
		case SIPUSH : opcode = "SIPUSH"; break;
		case NEWARRAY : opcode = "NEWARRAY"; break;
		case LDC : opcode = "LDC"; break;
		case ILOAD : opcode = "ILOAD"; break;
		case LLOAD : opcode = "LLOAD"; break;
		case FLOAD : opcode = "FLOAD"; break;
		case DLOAD : opcode = "DLOAD"; break;
		case ALOAD : opcode = "ALOAD"; break;
		case ISTORE : opcode = "ISTORE"; break;
		case LSTORE : opcode = "LSTORE"; break;
		case FSTORE : opcode = "FSTORE"; break;
		case ASTORE : opcode = "ASTORE"; break;
		case RET : opcode = "RET"; break;
		case IINC : opcode = "IINC"; break;
		case IFEQ : opcode = "IFEQ"; break;
		case IFNE : opcode = "IFNE"; break;
		case IFLT : opcode = "IFLT"; break;
		case IFGE : opcode = "IFGE"; break;
		case IFGT : opcode = "IFGT"; break;
		case IFLE : opcode = "IFLE"; break;
		case IF_ICMPEQ : opcode = "IF_ICMPEQ"; break;
		case IF_ICMPNE : opcode = "IF_ICMPNE"; break;
		case IF_ICMPLT : opcode = "IF_ICMPLT"; break;
		case IF_ICMPGE : opcode = "IF_ICMPGE"; break;
		case IF_ICMPGT : opcode = "IF_ICMPGT"; break;
		case IF_ICMPLE : opcode = "IF_ICMPLE"; break;
		case IF_ACMPEQ : opcode = "IF_ACMPEQ"; break;
		case IF_ACMPNE : opcode = "IF_ACMPNE"; break;
		case GOTO : opcode = "GOTO"; break;
		case JSR : opcode = "JSR"; break;
		case IFNULL : opcode = "IFNULL"; break;
		case IFNONNULL : opcode = "IFNONNULL"; break;
		case TABLESWITCH : opcode = "TABLESWITCH"; break;
		case LOOKUPSWITCH : opcode = "LOOKUPSWITCH"; break;
		case GETSTATIC : opcode = "GETSTATIC"; break;
		case PUTSTATIC : opcode = "PUTSTATIC"; break;
		case GETFIELD : opcode = "GETFIELD"; break;
		case PUTFIELD : opcode = "PUTFIELD"; break;
		case INVOKEVIRTUAL : opcode = "INVOKEVIRTUAL"; break;
		case INVOKESPECIAL : opcode = "INVOKESPECIAL"; break;
		case INVOKESTATIC : opcode = "INVOKESTATIC"; break;
		case INVOKEINTERFACE : opcode = "INVOKEINSTANCE"; break;
		case INVOKEDYNAMIC : opcode = "INVOKEDYNAMIC"; break;
		case NEW : opcode = "NEW"; break;
		case ANEWARRAY : opcode = "NEWARRAY"; break;
		case CHECKCAST : opcode = "CHECKCAST"; break;
		case INSTANCEOF : opcode = "INSTANCEOF"; break;
		case MULTIANEWARRAY : opcode = "MULTIANEWARRAY"; break;
		case NOP : opcode = "NOP"; break;
		case ACONST_NULL : opcode = "ACONST_NULL"; break;
		case ICONST_M1 : opcode = "ICONST_M1"; break;
		case ICONST_0 : opcode = "ICONST_0"; break;
		case ICONST_1 : opcode = "ICONST_1"; break;
		case ICONST_2 : opcode = "ICONST_2"; break;
		case ICONST_3 : opcode = "ICONST_3"; break;
		case ICONST_4 : opcode = "ICONST_4"; break;
		case ICONST_5 : opcode = "ICONST_5"; break;
		case LCONST_0 : opcode = "LCONST_0"; break;
		case LCONST_1 : opcode = "LCONST_1"; break;
		case FCONST_0 : opcode = "FCONST_0"; break;
		case FCONST_1 : opcode = "FCONST_1"; break;
		case FCONST_2 : opcode = "FCONST_2"; break;
		case DCONST_0 : opcode = "DCONST_0"; break;
		case DCONST_1 : opcode = "DCONST_1"; break;
		case IALOAD : opcode = "IALOAD"; break;
		case LALOAD : opcode = "LALOAD"; break;
		case FALOAD : opcode = "FALOAD"; break;
		case DALOAD : opcode = "DALOAD"; break;
		case AALOAD : opcode = "AALOAD"; break;
		case BALOAD : opcode = "BALOAD"; break;
		case CALOAD : opcode = "CALOAD"; break;
		case SALOAD : opcode = "SALOAD"; break;
		case IASTORE : opcode = "IASTORE"; break;
		case LASTORE : opcode = "LASTORE"; break;
		case FASTORE : opcode = "FASTORE"; break;
		case DASTORE : opcode = "DASTORE"; break;
		case AASTORE : opcode = "AASTORE"; break;
		case CASTORE : opcode = "CASTORE"; break;
		case SASTORE : opcode = "SASTORE"; break;
		case POP : opcode = "POP"; break;
		case POP2 : opcode = "POP2"; break;
		case DUP : opcode = "DUP"; break;
		case DUP_X1 : opcode = "DUP_X1"; break;
		case DUP_X2 : opcode = "DUP_X2"; break;
		case DUP2 : opcode = "DUP2"; break;
		case DUP2_X1 : opcode = "DUP2_X1"; break;
		case DUP2_X2 : opcode = "DUP2_X2"; break;
		case SWAP : opcode = "SWAP"; break;
		case IADD : opcode = "IADD"; break;
		case LADD : opcode = "LADD"; break;
		case FADD : opcode = "FADD"; break;
		case DADD : opcode = "DADD"; break;
		case ISUB : opcode = "ISUB"; break;
		case LSUB : opcode = "LSUB"; break;
		case FSUB : opcode = "FSUB"; break;
		case DSUB : opcode = "DSUB"; break;
		case IMUL : opcode = "IMUL"; break;
		case LMUL : opcode = "LMUL"; break;
		case FMUL : opcode = "FMUL"; break;
		case DMUL : opcode = "DMUL"; break;
		case IDIV : opcode = "IDIV"; break;
		case LDIV : opcode = "LDIV"; break;
		case FDIV : opcode = "FDIV"; break;
		case DDIV : opcode = "DDIV"; break;
		case IREM : opcode = "IREM"; break;
		case FREM : opcode = "FREM"; break;
		case DREM : opcode = "DREM"; break;
		case INEG : opcode = "INEG"; break;
		case LNEG : opcode = "LNEG"; break;
		case FNEG : opcode = "FNEG"; break;
		case DNEG : opcode = "DNEG"; break;
		case ISHL : opcode = "ISHL"; break;
		case LSHL : opcode = "LSHL"; break;
		case ISHR : opcode = "ISHR"; break;
		case LSHR : opcode = "LSHR"; break;
		case IUSHR : opcode = "IUSHR"; break;
		case LUSHR : opcode = "LUSHR"; break;
		case IAND : opcode = "IAND"; break;
		case LAND : opcode = "LAND"; break;
		case IOR : opcode = "IOR"; break;
		case LOR : opcode = "LOR"; break;
		case IXOR : opcode = "IXOR"; break;
		case LXOR : opcode = "LXOR"; break;
		case I2L : opcode = "I2L"; break;
		case I2F : opcode = "I2F"; break;
		case I2D : opcode = "I2D"; break;
		case L2I : opcode = "L2I"; break;
		case L2F : opcode = "L2F"; break;
		case L2D : opcode = "L2D"; break;
		case F2I : opcode = "F2I"; break;
		case F2L : opcode = "F2L"; break;
		case F2D : opcode = "F2D"; break;
		case D2I : opcode = "D2I"; break;
		case D2L : opcode = "D2L"; break;
		case D2F : opcode = "D2F"; break;
		case I2B : opcode = "I2B"; break;
		case I2C : opcode = "I2C"; break;
		case I2S : opcode = "I2S"; break;
		case LCMP : opcode = "LCMP"; break;
		case FCMPL : opcode = "FCMPL"; break;
		case FCMPG : opcode = "FCMPG"; break;
		case DCMPL : opcode = "DCMPL"; break;
		case DCMPG : opcode = "DCMPG"; break;
		case IRETURN : opcode = "IRETURN"; break;
		case LRETURN : opcode = "LRETURN"; break;
		case FRETURN : opcode = "FRETURN"; break;
		case DRETURN : opcode = "DRETURN"; break;
		case ARETURN : opcode = "ARETURN"; break;
		case RETURN : opcode = "RETURN"; break;
		case ARRAYLENGTH : opcode = "ARRAYLENGTH"; break;
		case ATHROW : opcode = "ATHROW"; break;
		case MONITORENTER : opcode = "MONITORENTER"; break;
		case MONITOREXIT : opcode = "MONITOREXIT"; break;
		default : opcode = "UNKNOWN"; break;
		}
		opcode = FORMAT.format(node.getOpcode()) + " " + opcode;
		switch (node.getType())
		{
		case AbstractInsnNode.VAR_INSN :
			return opcode + " " + ((VarInsnNode) node).var;
		case AbstractInsnNode.TYPE_INSN :
			return opcode + " " + ((TypeInsnNode) node).desc;
		case AbstractInsnNode.TABLESWITCH_INSN :
			return opcode + " " + ((TableSwitchInsnNode) node).min + "," + ((TableSwitchInsnNode) node).max;
		case AbstractInsnNode.MULTIANEWARRAY_INSN :
			return opcode + " " + ((MultiANewArrayInsnNode) node).desc + "[x" + ((MultiANewArrayInsnNode) node).dims;
		case AbstractInsnNode.METHOD_INSN :
			return opcode + " " + ((MethodInsnNode) node).owner + "." + ((MethodInsnNode) node).name + " " + ((MethodInsnNode) node).desc;
		case AbstractInsnNode.LOOKUPSWITCH_INSN :
			return opcode + " " + ((LookupSwitchInsnNode) node).dflt.getLabel().toString();
		case AbstractInsnNode.LINE :
			return "line " + ((LineNumberNode) node).line;
		case AbstractInsnNode.LDC_INSN :
			return opcode + " " + ((LdcInsnNode) node).cst.toString();
		case AbstractInsnNode.LABEL :
			return "label " + ((LabelNode) node).getLabel().toString();
		case AbstractInsnNode.JUMP_INSN :
			return opcode + " " + ((JumpInsnNode) node).label.getLabel().toString();
		case AbstractInsnNode.INVOKE_DYNAMIC_INSN :
			return opcode + " " + ((InvokeDynamicInsnNode) node).name + " " + ((InvokeDynamicInsnNode) node).desc;
		case AbstractInsnNode.INT_INSN :
			return opcode + " " + ((IntInsnNode) node).operand;
		case AbstractInsnNode.INSN :
			return opcode;
		case AbstractInsnNode.IINC_INSN :
			return opcode + " " + ((IincInsnNode) node).var + ":" + ((IincInsnNode) node).incr;
		case AbstractInsnNode.FRAME :
			return opcode + " " + ((FrameNode) node).type;
		case AbstractInsnNode.FIELD_INSN :
			return opcode + " " + ((FieldInsnNode) node).owner + "." + ((FieldInsnNode) node).name + "." + ((FieldInsnNode) node).desc;
		default : return "";
		}
	}
}