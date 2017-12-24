/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFGE;
import static org.objectweb.asm.Opcodes.IFGT;
import static org.objectweb.asm.Opcodes.IFLE;
import static org.objectweb.asm.Opcodes.IFLT;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.IF_ACMPEQ;
import static org.objectweb.asm.Opcodes.IF_ACMPNE;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.IF_ICMPGE;
import static org.objectweb.asm.Opcodes.IF_ICMPGT;
import static org.objectweb.asm.Opcodes.IF_ICMPLE;
import static org.objectweb.asm.Opcodes.IF_ICMPLT;
import static org.objectweb.asm.Opcodes.IF_ICMPNE;
import static org.objectweb.asm.Opcodes.IINC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INSTANCEOF;
import static org.objectweb.asm.Opcodes.INVOKEDYNAMIC;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.JSR;
import static org.objectweb.asm.Opcodes.LDC;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LOOKUPSWITCH;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.MULTIANEWARRAY;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RET;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.TABLESWITCH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * @author ueyudiud
 */
class Parser
{
	private Lexer lexer;
	
	Parser(Lexer lexer)
	{
		this.lexer = lexer;
	}
	
	private Map<String, ClassTag> defines = new HashMap<>();
	
	void joinin()
	{
		try
		{
			LinkedList<OpInfo> list = new LinkedList<>();
			label: for (;;)
			{
				switch (this.lexer.scan(null, ImmutableMap.of()))
				{
				case DEF :
					ClassTag tag = this.lexer.scanType(this.defines);
					this.defines.put(tag.getName(), tag);
					break;
				case MODIFICATION :
					tag = this.lexer.scanType(this.defines);
					this.lexer.scanLeftBracket();
					list.add(parseInfo(tag));
					this.lexer.scanRightBracket();
				case END :
					break label;
				default:
					this.lexer.err("Unexpected token.");
					break;
				}
			}
			if (this.lexer.errored)
				throw new RuntimeException();
			list.forEach(OpInfo::put);
			list.forEach(information -> NebulaASMLogHelper.LOG.info("Loaded {} modifications.", information.mcpname));
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
		finally
		{
			for (String cause : this.lexer.exceptions)
			{
				NebulaASMLogHelper.LOG.error(cause);
			}
		}
	}
	
	private OpInfo parseInfo(ClassTag predicated)
	{
		if (this.lexer.scan(predicated, this.defines) != TokenType.LBRACKET)
			this.lexer.err("'{' expected.");
		OpInfo info = new OpInfo(predicated.getName().replace('.', '/'));
		{
			label: while (true)
			{
				switch (this.lexer.scan(predicated, this.defines))
				{
				case MODIFY :
					this.lexer.skipWS();
					MethodTag tag = this.lexer.scanMethod(predicated, this.defines);
					this.lexer.scanLeftBracket();
					info.modifies.put(tag.name + "|" + tag.desc(), parseLabel(predicated));
					this.lexer.scanRightBracket();
					break;
				case REMOVE :
					this.lexer.skipWS();
					tag = this.lexer.scanMethod(predicated, this.defines);
					info.modifies.put(tag.name + "|" + tag.desc(), ImmutableList.of());
					break;
				default:
					this.lexer.err("Unexpected token.");
				case RBRACKET :
					break label;
				}
			}
		}
		return info;
	}
	
	private List<OpLabel> parseLabel(ClassTag predicated)
	{
		List<OpLabel> list = new ArrayList<>(3);
		{
			label: while (true)
			{
				boolean parseNode = true;
				OpType type;
				switch (this.lexer.scan(predicated, this.defines))
				{
				case INSERT :
					type = OpType.INSERT;
					break;
				case INSERT_BEFORE :
					type = OpType.INSERT_BEFORE;
					break;
				case REPLACE :
					type = OpType.REPLACE;
					break;
				case REMOVE :
					type = OpType.REMOVE;
					parseNode = false;
					break;
				default:
					this.lexer.err("Unexpected token.");
				case RBRACKET :
					break label;
				}
				switch (this.lexer.scan(predicated, this.defines))
				{
				case NUMBER :
				{
					int start = this.lexer.currentNumber;
					this.lexer.skipWS();
					int off = this.lexer.scanNumber();
					this.lexer.skipWS();
					int len = this.lexer.scanNumber();
					this.lexer.skipWS();
					if (!parseNode)
					{
						list.add(start >= 0 ?
								new OpLabel.OpLabelLineNumber(start, off, len, type, ImmutableList.of()) :
									new OpLabel.OpLabelBegining(off, len, type, ImmutableList.of()));
					}
					else
					{
						this.lexer.skipWS();
						List<AbstractInsnNode> nodes = parseNodes(predicated);
						list.add(start >= 0 ?
								new OpLabel.OpLabelLineNumber(start, off, len, type, nodes) :
									new OpLabel.OpLabelBegining(off, len, type, nodes));
					}
				}
				case METHOD :
				{
					MethodTag tag = this.lexer.currentMethod;
					this.lexer.skipWS();
					int count = this.lexer.scanNumber();
					this.lexer.skipWS();
					int off = this.lexer.scanNumber();
					this.lexer.skipWS();
					int len = this.lexer.scanNumber();
					this.lexer.skipWS();
					if (!parseNode)
					{
						list.add(new OpLabel.OpLabelMethodAsTag(count, tag.owner.path().replace('.', '/'), tag.name, tag.desc(), off, len, type, ImmutableList.of()));
					}
					else
					{
						this.lexer.skipWS();
						List<AbstractInsnNode> nodes = parseNodes(predicated);
						list.add(new OpLabel.OpLabelMethodAsTag(count, tag.owner.path().replace('.', '/'), tag.name, tag.desc(), off, len, type, nodes));
					}
				}
				break;
				default:
					this.lexer.err("Unexpected token.");
					break;
				}
			}
		}
		return list;
	}
	
	private List<AbstractInsnNode> parseNodes(ClassTag predicated)
	{
		this.lexer.scanLeftBracket();
		List<AbstractInsnNode> nodes = new ArrayList<>();
		label: while (true)
		{
			switch (this.lexer.scan(predicated, this.defines))
			{
			case NUMBER :
			{
				this.lexer.enableNewline = false;
				int opcode;
				switch (opcode = this.lexer.currentNumber)
				{
				case BIPUSH :
				case SIPUSH :
				case NEWARRAY :
				{
					this.lexer.skipWS();
					int operand = this.lexer.scanNumber();
					nodes.add(new IntInsnNode(opcode, operand));
					break;
				}
				case ILOAD :
				case LLOAD :
				case FLOAD :
				case DLOAD :
				case ALOAD :
				case ISTORE:
				case LSTORE:
				case FSTORE:
				case ASTORE:
				case RET :
				{
					this.lexer.skipWS();
					int var = this.lexer.scanNumber();
					nodes.add(new VarInsnNode(opcode, var));
					break;
				}
				case IINC :
				{
					this.lexer.skipWS();
					int incr = this.lexer.scanNumber();
					nodes.add(new IincInsnNode(opcode, incr));
					break;
				}
				case GETSTATIC:
				case PUTSTATIC:
				case GETFIELD:
				case PUTFIELD:
				{
					this.lexer.skipWS();
					MethodTag field = this.lexer.scanMethod(predicated, this.defines);
					if (field.parameters.length != 0)
						this.lexer.err("Field paramter should be 0.");
					nodes.add(new FieldInsnNode(opcode, field.owner.path().replace('.', '/'), field.name, field.result.desc()));
					break;
				}
				case INVOKEVIRTUAL :
				case INVOKESPECIAL :
				case INVOKESTATIC :
				{
					this.lexer.skipWS();
					MethodTag method = this.lexer.scanMethod(predicated, this.defines);
					nodes.add(new MethodInsnNode(opcode, method.owner.path().replace('.', '/'), method.name, method.desc(), false));
					break;
				}
				case INVOKEINTERFACE:
				{
					this.lexer.skipWS();
					MethodTag method = this.lexer.scanMethod(predicated, this.defines);
					nodes.add(new MethodInsnNode(opcode, method.owner.path().replace('.', '/'), method.name, method.desc(), true));
					break;
				}
				case NEW :
				case ANEWARRAY :
				case CHECKCAST :
				case INSTANCEOF :
				{
					this.lexer.skipWS();
					ClassTag type = this.lexer.scanType(this.defines);
					nodes.add(new TypeInsnNode(opcode, type.path().replace('.', '/')));
					break;
				}
				case MULTIANEWARRAY :
				{
					this.lexer.skipWS();
					ClassTag type = this.lexer.scanType(this.defines);
					this.lexer.skipWS();
					int dims;
					switch (this.lexer.scan(predicated, this.defines))
					{
					case NUMBER :
						dims = this.lexer.currentNumber;
						break;
					case COMMA :
						dims = this.lexer.scanNumber();
						break;
					default:
						this.lexer.err("Missing parameter for MULTIANEWARRAY node.");
						break label;
					}
					nodes.add(new MultiANewArrayInsnNode(type.desc(), dims));
					break;
				}
				case IFEQ:
				case IFNE:
				case IFLT:
				case IFGE:
				case IFGT:
				case IFLE:
				case IF_ICMPEQ:
				case IF_ICMPNE:
				case IF_ICMPLT:
				case IF_ICMPGE:
				case IF_ICMPGT:
				case IF_ICMPLE:
				case IF_ACMPEQ:
				case IF_ACMPNE:
				case GOTO:
				case JSR:
				case IFNULL:
				case IFNONNULL:
				case TABLESWITCH:
				case LOOKUPSWITCH:
				case INVOKEDYNAMIC :
				case LDC :
				{
					this.lexer.err("Opcode: " + opcode + " can not used yet, sorry.");
					break;
				}
				default:
				{
					nodes.add(new InsnNode(opcode));
					break;
				}
				}
				this.lexer.scanNewline();
				this.lexer.enableNewline = true;
				break;
			}
			default:
				this.lexer.err("Unexpected token.");
			case RBRACKET :
				break label;
			}
		}
		return nodes;
	}
}
