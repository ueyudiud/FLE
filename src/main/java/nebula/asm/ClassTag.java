/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.asm;

/**
 * @author ueyudiud
 */
abstract interface ClassTag
{
	abstract String getName();
	
	abstract String path();
	
	abstract String desc();
}

class SimpleClassTag implements ClassTag
{
	final String path;
	final String name;
	final String desc;
	
	SimpleClassTag(String path, String name)
	{
		this.path = path;
		this.name = name;
		this.desc = "L" + path.replace('.', '/') + ";";
	}
	
	@Override
	public String path()
	{
		return this.path;
	}
	
	@Override
	public String desc()
	{
		return this.desc;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
}

enum PrimitiveClassTag implements ClassTag
{
	VOID("void", "V"),
	BYTE("byte", "B"),
	SHORT("short", "S"),
	INT("int", "I"),
	LONG("long", "J"),
	FLOAT("float", "F"),
	DOUBLE("double", "D"),
	CHAR("char", "C"),
	BOOLEAN("boolean", "Z");
	
	static ClassTag getPrimitiveType(String name)
	{
		switch (name)
		{
		case "byte"   : return BYTE;
		case "short"  : return SHORT;
		case "int"    : return INT;
		case "long"   : return LONG;
		case "float"  : return FLOAT;
		case "double" : return DOUBLE;
		case "char"   : return CHAR;
		case "boolean": return BOOLEAN;
		case "void"   : return VOID;
		default       : return null;
		}
	}
	
	final String name;
	final String desc;
	
	PrimitiveClassTag(String name, String desc)
	{
		this.name = name;
		this.desc = desc;
	}
	
	@Override
	public String path()
	{
		return this.name;
	}
	
	@Override
	public String desc()
	{
		return this.desc;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
}
