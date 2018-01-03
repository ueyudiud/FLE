/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.util.LinkedList;

import javax.script.ScriptContext;
import javax.script.ScriptException;

/**
 * @author ueyudiud
 */
class Parser
{
	Scanner scanner;
	
	Parser(Scanner scanner)
	{
		this.scanner = scanner;
	}
	
	Object eval(ScriptContext context) throws ScriptException
	{
		IFSObject result = null;
		FSBindings bindings = new FSBindings();
		bindings.parent.putAll(context.getBindings(ScriptContext.ENGINE_SCOPE));
		while (this.scanner.type != TokenType.END)
		{
			result = scanStatement(bindings);
		}
		
		return FSObjects.unpack(result);
	}
	
	enum State
	{
		S, I, O, U;
	}
	
	private static void collectValues(Scanner scanner,
			FSBindings binding, LinkedList<IFSObject> lbuf, LinkedList<TokenType> rbuf) throws ScriptException
	{
		while (lbuf.size() != 0)
		{
			TokenType type = rbuf.getFirst();
			IFSObject second = lbuf.removeLast();
			IFSObject first = lbuf.removeLast();
			switch (type)
			{
			case MUL :
				lbuf.addLast(first.operator_mul(second));
				break;
			case DIV :
				lbuf.addLast(first.operator_div(second));
				break;
			case REM :
				lbuf.addLast(first.operator_rem(second));
				break;
			case ADD :
				lbuf.addLast(first.operator_add(second));
				break;
			case SUB :
				lbuf.addLast(first.operator_sub(second));
				break;
			case SHL :
				lbuf.addLast(first.operator_shl(second));
				break;
			case SHR :
				lbuf.addLast(first.operator_shr(second));
				break;
			case USHR :
				lbuf.addLast(first.operator_ushr(second));
				break;
			case RGE :
				lbuf.addLast(first.operator_range(second));
				break;
			case IAND :
				lbuf.addLast(first.operator_iand(second));
				break;
			case IOR :
				lbuf.addLast(first.operator_ior(second));
				break;
			case IXOR :
				lbuf.addLast(first.operator_ixor(second));
				break;
			case OEQ :
				lbuf.addLast(first.operator_objeq(second));
				break;
			case ONE :
				lbuf.addLast(first.operator_objne(second));
				break;
			case GT :
				lbuf.addLast(first.operator_gt(second));
				break;
			case GE :
				lbuf.addLast(first.operator_ge(second));
				break;
			case LT :
				lbuf.addLast(first.operator_lt(second));
				break;
			case LE :
				lbuf.addLast(first.operator_le(second));
				break;
			case EQ :
				lbuf.addLast(first.operator_equal(second));
				break;
			case NE :
				lbuf.addLast(first.operator_noneq(second));
				break;
			case AND :
				lbuf.addLast(FSObjects.valueOf(first.isTrue() && second.isTrue()));
				break;
			case OR :
				lbuf.addLast(FSObjects.valueOf(first.isTrue() || second.isTrue()));
				break;
			case XOR :
				lbuf.addLast(FSObjects.valueOf(first.isTrue() != second.isTrue()));
				break;
			case DEF :
				lbuf.addLast(first.operator_set(second.eval()));
				break;
			case MULDEF :
				lbuf.addLast(first.operator_mulset(second));
				break;
			case DIVDEF :
				lbuf.addLast(first.operator_divset(second));
				break;
			case REMDEF :
				lbuf.addLast(first.operator_remset(second));
				break;
			case ADDDEF :
				lbuf.addLast(first.operator_addset(second));
				break;
			case SUBDEF :
				lbuf.addLast(first.operator_subset(second));
				break;
			case SHLDEF :
				lbuf.addLast(first.operator_shlset(second));
				break;
			case SHRDEF :
				lbuf.addLast(first.operator_shrset(second));
				break;
			case USHRDEF :
				lbuf.addLast(first.operator_shrset(second));
				break;
			case ANDDEF :
				lbuf.addLast(first.operator_andset(second));
				break;
			case ORDEF :
				lbuf.addLast(first.operator_orset(second));
				break;
			case XORDEF :
				lbuf.addLast(first.operator_xorset(second));
				break;
			default:
				throw new InternalError();
			}
		}
	}
	
	private IFSObject scanStatement(FSBindings binding) throws ScriptException
	{
		LinkedList<IFSObject> result = new LinkedList<>();
		LinkedList<IFSObject> lbuf = new LinkedList<>();
		LinkedList<TokenType> rbuf = new LinkedList<>();
		State S = State.S;
		while (true)
		{
			this.scanner.scan();
			switch (this.scanner.type)
			{
			case DOT :
				lbuf.removeLast();
				this.scanner.scan(TokenType.ID);
				lbuf.addLast(lbuf.removeLast().child(this.scanner.identifier));
				break;
			case COMMA :
				if (lbuf.size() != 1)
					this.scanner.error("Unexpected token.");
				collectValues(this.scanner, binding, lbuf, rbuf);
				result.addLast(lbuf.removeLast());
				break;
			case RPANC :
			case RBRACE :
			case RBRACKET :
			case SEMI :
				switch (lbuf.size())
				{
				case 1 :
					collectValues(this.scanner, binding, lbuf, rbuf);
					result.addLast(lbuf.removeLast());
					return result.size() == 1 ? result.getFirst() : new FSTuple(result.toArray(new IFSObject[result.size()]));
				case 0 :
					if (result.size() == 0)
						return null;
				default:
					this.scanner.error("Unexpected token.");
					break;
				}
			case LIT :
				switch (S)
				{
				case I :
					this.scanner.error("Unexpected literal.");
					break;
				case U :
					switch (rbuf.removeFirst())
					{
					case INCR :
						lbuf.add(this.scanner.literal.operator_lincr());
						break;
					case DESC :
						lbuf.add(this.scanner.literal.operator_ldesc());
						break;
					case ADD :
						lbuf.add(this.scanner.literal.operator_positive());
						break;
					case SUB :
						lbuf.add(this.scanner.literal.operator_negetive());
						break;
					case MUL :
						lbuf.add(this.scanner.literal.operator_ptr());
						break;
					case RGE :
						lbuf.add(this.scanner.literal.operator_invert());
						break;
					case NOT :
						lbuf.add(this.scanner.literal.operator_not());
						break;
					default:
						throw new InternalError();
					}
					break;
				default:
					lbuf.add(this.scanner.literal);
					break;
				}
				S = State.I;
				break;
			case ID :
				switch (S)
				{
				case I :
					this.scanner.error("Unexpected identifier.");
				case U :
					IFSObject object = binding.getOrCreate(this.scanner.identifier);
					switch (rbuf.removeFirst())
					{
					case INCR :
						lbuf.add(object.operator_lincr());
						break;
					case DESC :
						lbuf.add(object.operator_ldesc());
						break;
					case ADD :
						lbuf.add(object.operator_positive());
						break;
					case SUB :
						lbuf.add(object.operator_negetive());
						break;
					case MUL :
						lbuf.add(object.operator_ptr());
						break;
					case RGE :
						lbuf.add(object.operator_invert());
						break;
					case NOT :
						lbuf.add(object.operator_not());
						break;
					default:
						throw new InternalError();
					}
					break;
				default :
					lbuf.add(binding.getOrCreate(this.scanner.identifier));
					break;
				}
				S = State.I;
				break;
			case INCR :
				if (S == State.I)
					lbuf.addLast(lbuf.removeFirst().operator_rincr());
				else
					rbuf.addFirst(TokenType.INCR);
				break;
			case DESC :
				if (S == State.I)
					lbuf.addLast(lbuf.removeFirst().operator_rdesc());
				else
					rbuf.addFirst(TokenType.DESC);
				break;
			default :
				switch (S)
				{
				case I :
					final int level = this.scanner.type.level;
					final boolean connect = !this.scanner.type.left;
					while (lbuf.size() != 0)
					{
						TokenType type = rbuf.getFirst();
						if (type.level < level || (connect && type.level == level))
						{
							IFSObject second = lbuf.removeLast();
							IFSObject first = lbuf.removeLast();
							switch (type)
							{
							case MUL :
								lbuf.addLast(first.operator_mul(second));
								break;
							case DIV :
								lbuf.addLast(first.operator_div(second));
								break;
							case REM :
								lbuf.addLast(first.operator_rem(second));
								break;
							case ADD :
								lbuf.addLast(first.operator_add(second));
								break;
							case SUB :
								lbuf.addLast(first.operator_sub(second));
								break;
							case SHL :
								lbuf.addLast(first.operator_shl(second));
								break;
							case SHR :
								lbuf.addLast(first.operator_shr(second));
								break;
							case USHR :
								lbuf.addLast(first.operator_ushr(second));
								break;
							case IAND :
								lbuf.addLast(first.operator_iand(second));
								break;
							case IOR :
								lbuf.addLast(first.operator_ior(second));
								break;
							case IXOR :
								lbuf.addLast(first.operator_ixor(second));
								break;
							case RGE :
								lbuf.addLast(first.operator_range(second));
								break;
							case OEQ :
								lbuf.addLast(first.operator_objeq(second));
								break;
							case ONE :
								lbuf.addLast(first.operator_objne(second));
								break;
							case GT :
								lbuf.addLast(first.operator_gt(second));
								break;
							case GE :
								lbuf.addLast(first.operator_ge(second));
								break;
							case LT :
								lbuf.addLast(first.operator_lt(second));
								break;
							case LE :
								lbuf.addLast(first.operator_le(second));
								break;
							case EQ :
								lbuf.addLast(first.operator_equal(second));
								break;
							case NE :
								lbuf.addLast(first.operator_noneq(second));
								break;
							case AND :
								lbuf.addLast(FSObjects.valueOf(first.isTrue() && second.isTrue()));
								break;
							case OR :
								lbuf.addLast(FSObjects.valueOf(first.isTrue() || second.isTrue()));
								break;
							case XOR :
								lbuf.addLast(FSObjects.valueOf(first.isTrue() != second.isTrue()));
								break;
							default:
								break;
							}
						}
						else break;
					}
					rbuf.addFirst(this.scanner.type);
					S = State.O;
					break;
				case U :
					this.scanner.error("Unexpected token.");
					break;
				default :
					switch (this.scanner.type)
					{
					case NOT : case RGE : case ADD : case SUB : case MUL :
						rbuf.addFirst(this.scanner.type);
						S = State.U;
						break;
					default :
						this.scanner.error("This operator can not refer as left unary operator.");
						break;
					}
				}
			}
		}
	}
}
