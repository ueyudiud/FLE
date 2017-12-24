/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.asm;

import static nebula.asm.PrimitiveClassTag.getPrimitiveType;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

/**
 * @author ueyudiud
 */
class Lexer
{
	private static final char EOF = '\0';
	private static final int IBUFLENGTH = 256;
	
	private char[] buffer;
	private int bufIdx;
	
	private char[] ibuf = new char[IBUFLENGTH];
	private int iBufIdx;
	
	String source;
	boolean errored = false;
	LinkedList<String> exceptions = new LinkedList<>();
	boolean enableNewline = true;
	
	int currentNumber;
	ClassTag currentClass;
	MethodTag currentMethod;
	
	Lexer(Reader reader) throws IOException
	{
		this.buffer = IOUtils.toCharArray(reader);
	}
	
	ClassTag scanType(Map<String, ClassTag> map)
	{
		char chr;
		label: while ((chr = next()) != EOF)
		{
			switch (chr)
			{
			case '.' :
				store('.');
				return scanComplexType();
			case ' ' :
			case '\t':
			case '\r':
			case '\n':
			case '#' : case ',' :
			case '{' : case '}' :
			case '[' : case ']' :
			case '(' : case ')' :
				break label;
			case '$' :
			{
				String str = pop();
				ClassTag tag = getPrimitiveType(str);
				if (tag == null)
				{
					tag = map.get(str);
				}
				if (tag == null)
				{
					tag = new SimpleClassTag(str, str);
				}
				return scanChildType(tag);
			}
			default:
				store(chr);
				break;
			}
		}
		this.bufIdx --;
		String str = pop();
		ClassTag tag = getPrimitiveType(str);
		if (tag == null)
		{
			tag = map.get(str);
		}
		if (tag == null)
		{
			tag = new SimpleClassTag(str, str);
		}
		return tag;
	}
	
	private ClassTag scanComplexType()
	{
		char chr;
		int lastDotId = this.iBufIdx;
		label: while ((chr = next()) != EOF)
		{
			switch (chr)
			{
			case ' ' :
			case '\t':
			case '\r':
			case '\n':
			case '#' : case ',' :
			case '{' : case '}' :
			case '[' : case ']' :
			case '(' : case ')' :
				break label;
			case '$' :
			case '.' :
				lastDotId = this.iBufIdx;
			default :
				store(chr);
				break;
			}
		}
		this.bufIdx --;
		String name = new String(this.ibuf, lastDotId, this.iBufIdx - lastDotId);
		String path = pop();
		return new SimpleClassTag(path, name);
	}
	
	private ClassTag scanChildType(ClassTag parent)
	{
		char chr;
		int lastDotId = this.iBufIdx;
		label: while ((chr = next()) != EOF)
		{
			switch (chr)
			{
			case ' ' :
			case '\t':
			case '\r':
			case '\n':
			case '#' : case ',' :
			case '{' : case '}' :
			case '[' : case ']' :
			case '(' : case ')' :
				break label;
			case '.' :
				warn("The internal type should not use '.' to link child type.");
			case '$' :
				lastDotId = this.iBufIdx;
				store('$');
				break;
			default :
				store(chr);
				break;
			}
		}
		String name = new String(this.ibuf, lastDotId, this.iBufIdx - lastDotId);
		String path = parent.path() + "$" + pop();
		return new SimpleClassTag(path, name);
	}
	
	void scanLeftBracket()
	{
		while (true)
		{
			switch (next())
			{
			case ' ' :
			case '\t':
			case '\r':
			case '\n':
				break;
			case '{' :
				return;
			default:
				err("The '{' is expected.");
				return;
			}
		}
	}
	
	void scanRightBracket()
	{
		while (true)
		{
			switch (next())
			{
			case ' ' :
			case '\t':
			case '\r':
			case '\n':
				break;
			case '}' :
				return;
			default:
				err("The '}' is expected.");
				return;
			}
		}
	}
	
	TokenType scan(ClassTag predicated, Map<String, ClassTag> map)
	{
		{
			label: while (true)
			{
				char chr1 = peek();
				switch (chr1)
				{
				case '\r':
				case '\n':
					if (!this.enableNewline)
						err("Can not enter here.");
				case ' ' :
				case '\t':
					break;
				case EOF :
					return TokenType.END;
				case ',' :
					return TokenType.COMMA;
				case '+' : case '-' : case '0' :
				case '1' : case '2' : case '3' :
				case '4' : case '5' : case '6' :
				case '7' : case '8' : case '9' :
					this.currentNumber = scanNumber();
					return TokenType.NUMBER;
				case '{' :
					return TokenType.LBRACKET;
				case '}' :
					return TokenType.RBRACKET;
				default :
					break label;
				}
			}
		}
		int mark = this.bufIdx;
		TokenType type = scanTypeOrIdentifier(map);
		if (type != TokenType.CLASS)
			return type;
		ClassTag owner = this.currentClass;
		if (peek() != '#')//No class load type, use predicated instead.
		{
			if (peek() == '(')
			{
				this.bufIdx = mark;
				if (predicated == null)
					err("No owner for method.");
				owner = predicated;
			}
			else
			{
				this.currentClass = owner;
				return TokenType.CLASS;
			}
		}
		char chr;
		label: while ((chr = next()) != EOF)
		{
			switch (chr)
			{
			case ' ' :
			case '\t':
			case '\r':
			case '\n':
			case '#' : case ',' :
			case '{' : case '}' :
			case '[' : case ']' :
			case ')' :
				err("No method parameter found.");
				break label;
			case '(' :
				break label;
			default :
				store(chr);
				break;
			}
		}
		String name = pop();
		List<ClassTag> tags = new ArrayList<>(4);
		boolean flag = true;
		label: while ((chr = next()) != EOF)
		{
			skipWS();
			switch (chr)
			{
			case '\r':
			case '\n':
			case '#' :
			case '(' :
			case '{' : case '}' :
			case '[' : case ']' :
				err("Illegal method parameter found.");
			case ',' :
				if (flag)
					err("Missing an identifier.");
				flag = true;
				break;
			case ')' :
				break label;
			default :
				tags.add(scanType(map));
				skipWS();
				flag = false;
				break;
			}
		}
		ClassTag result = scanType(map);
		this.currentMethod = new MethodTag(owner, name, tags.toArray(new ClassTag[tags.size()]), result);
		return TokenType.METHOD;
	}
	
	private TokenType scanTypeOrIdentifier(Map<String, ClassTag> map)
	{
		char chr;
		label: while ((chr = next()) != EOF)
		{
			switch (chr)
			{
			case '.' :
				store('.');
				this.currentClass = scanComplexType();
				return TokenType.CLASS;
			case ' ' :
			case '\t':
			case '\r':
			case '\n':
			case '#' : case ',' :
			case '{' : case '}' :
			case '[' : case ']' :
			case '(' : case ')' :
				break label;
			case '$' :
			{
				String str = pop();
				ClassTag tag = getPrimitiveType(str);
				if (tag == null)
				{
					tag = map.get(str);
				}
				if (tag == null)
				{
					tag = new SimpleClassTag(str, str);
				}
				this.currentClass = scanChildType(tag);
				return TokenType.CLASS;
			}
			default:
				store(chr);
				break;
			}
		}
		this.bufIdx --;
		String str = pop();
		switch (str)
		{
		case "modification" :
			return TokenType.MODIFICATION;
		case "modify" :
			return TokenType.MODIFY;
		case "insert" :
			return TokenType.INSERT;
		case "insert_before" :
			return TokenType.INSERT_BEFORE;
		case "replace" :
			return TokenType.REPLACE;
		case "remove" :
			return TokenType.REMOVE;
		case "def" :
			return TokenType.DEF;
		}
		ClassTag tag = getPrimitiveType(str);
		if (tag == null)
		{
			tag = map.get(str);
		}
		if (tag == null)
		{
			tag = new SimpleClassTag(str, str);
		}
		this.currentClass = tag;
		return TokenType.CLASS;
	}
	
	MethodTag scanMethod(ClassTag predicated, Map<String, ClassTag> map)
	{
		int mark = this.bufIdx;
		ClassTag owner = scanType(map);
		if (next() != '#')//No class load type, use predicated instead.
		{
			this.bufIdx = mark;
			if (predicated == null)
				err("No owner for method.");
			owner = predicated;
		}
		char chr;
		label: while ((chr = next()) != EOF)
		{
			switch (chr)
			{
			case ' ' :
			case '\t':
			case '\r':
			case '\n':
			case '#' : case ',' :
			case '{' : case '}' :
			case '[' : case ']' :
			case ')' :
				err("No method parameter found.");
				break label;
			case '(' :
				break label;
			default :
				store(chr);
				break;
			}
		}
		String name = pop();
		List<ClassTag> tags = new ArrayList<>(4);
		boolean flag = true;
		label: while ((chr = next()) != EOF)
		{
			skipWS();
			switch (chr)
			{
			case '\r':
			case '\n':
			case '#' :
			case '(' :
			case '{' : case '}' :
			case '[' : case ']' :
				err("Illegal method parameter found.");
			case ',' :
				if (flag)
					err("Missing an identifier.");
				flag = true;
				break;
			case ')' :
				break label;
			default :
				tags.add(scanType(map));
				skipWS();
				flag = false;
				break;
			}
		}
		ClassTag result = scanType(map);
		return new MethodTag(owner, name, tags.toArray(new ClassTag[tags.size()]), result);
	}
	
	int scanNumber()
	{
		char chr = next();
		boolean neg = false;
		if (chr == '-')
		{
			neg = true;
			chr = next();
		}
		else if (chr != '+')
			err("Number predicated.");
		int i = 0;
		while (chr >= '0' && chr <= '9')
		{
			i = 10 * i + (chr - '0');
			if (i < 0)
				err("Number is too large.");
			chr = next();
		}
		this.bufIdx --;
		return neg ? -i : i;
	}
	
	void scanNewline()
	{
		skipWS();
		switch (next())
		{
		case '\r' :
			if (peek() == '\n')
				this.bufIdx++;
		case '\n' :
			break;
		default   :
			err("Newline expected.");
			break;
		}
	}
	
	private char next()
	{
		return this.bufIdx >= this.buffer.length ? EOF : this.buffer[this.bufIdx ++];
	}
	
	private char peek()
	{
		return this.bufIdx >= this.buffer.length ? EOF : this.buffer[this.bufIdx];
	}
	
	private void store(char chr)
	{
		if (this.iBufIdx >= IBUFLENGTH)
			err("Identifier is too long.");
		this.ibuf[this.iBufIdx ++] = chr;
	}
	
	private String pop()
	{
		String result = new String(this.ibuf, 0, this.iBufIdx);
		this.iBufIdx = 0;
		return result;
	}
	
	void skipWS()
	{
		do
		{
			switch (this.buffer[this.bufIdx])
			{
			case ' ' :
			case '\t':
				this.bufIdx++;
				break;
			default:
				return;
			}
		}
		while (this.bufIdx < this.buffer.length);
	}
	
	void err(String cause)
	{
		this.errored = true;
		this.exceptions.addLast(cause);
		if (this.exceptions.size() > 10)
			throw new RuntimeException();
	}
	
	void warn(String cause)
	{
		this.exceptions.addLast(cause);
	}
}
