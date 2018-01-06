/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

/**
 * @author ueyudiud
 */
enum TokenType
{
	END,
	LIT,
	ID,
	
	DOT,
	COMMA,
	SEMI,
	
	LPANC,
	LBRACE,
	LBRACKET,
	RPANC,
	RBRACE,
	RBRACKET,
	
	INCR,
	DESC,
	NOT,
	MUL(14),
	DIV(14),
	REM(14),
	ADD(13),
	SUB(13),
	SHL(12),
	SHR(12),
	USHR(12),
	IAND(11),
	IOR(10),
	IXOR(9),
	RGE(8),
	OEQ(7),
	ONE(7),
	GT(6),
	GE(6),
	LT(6),
	LE(6),
	EQ(5),
	NE(5),
	AND(4),
	OR(3),
	XOR(2),
	TRI(1),
	DEF(0),
	MULDEF(0),
	DIVDEF(0),
	REMDEF(0),
	ADDDEF(0),
	SUBDEF(0),
	SHLDEF(0),
	SHRDEF(0),
	USHRDEF(0),
	ANDDEF(0),
	ORDEF(0),
	XORDEF(0);
	
	final int level;
	final boolean left;
	
	TokenType()
	{
		this(-1, false);
	}
	TokenType(int level)
	{
		this(level, false);
	}
	TokenType(int level, boolean left)
	{
		this.level = level;
		this.left = left;
	}
}
