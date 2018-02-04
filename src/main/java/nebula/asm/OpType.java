/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.asm;

import com.google.common.collect.ImmutableMap;

/**
 * @author ueyudiud
 */
public enum OpType
{
	INSERT("insert"), INSERT_BEFORE("insert_before"), REPLACE("replace"), REMOVE("remove"), SWITCH("switch");
	
	static final ImmutableMap<String, OpType> MAP = ImmutableMap.of("insert", INSERT, "insert_before", INSERT_BEFORE, "replace", REPLACE, "remove", REMOVE, "switch", SWITCH);
	
	static OpType parseValue(String arg) throws IllegalArgumentException
	{
		if (!MAP.containsKey(arg)) throw new IllegalArgumentException("Can not parse " + arg + " as a OptionType.");
		return MAP.get(arg);
	}
	
	final String name;
	
	OpType(String name)
	{
		this.name = name;
	}
}
