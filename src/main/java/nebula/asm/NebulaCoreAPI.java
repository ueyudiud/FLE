/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.asm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ueyudiud
 */
public final class NebulaCoreAPI
{
	static final List<String> ASM_SEARCHING_DIRECTION = new ArrayList<>();
	
	public static void addASMSearchingDirection(String file)
	{
		ASM_SEARCHING_DIRECTION.add(file);
	}
	
	private NebulaCoreAPI()
	{
	}
	
	static
	{
		addASMSearchingDirection("nebula");
		// Far & Nebula group mods...
		addASMSearchingDirection("ed");
		// Extra Data mod, uses to override vanilla world data save method.
	}
}
