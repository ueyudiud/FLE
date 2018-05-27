/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.compat.jei;

import java.util.ArrayList;
import java.util.List;

import nebula.common.stack.AbstractStack;

/**
 * @author ueyudiud
 */
public class ToolDisplayRecipeMap
{
	public static class ToolDisplayRecipe
	{
		public AbstractStack input;
		public AbstractStack[] tools;
		public AbstractStack[] outputs;
		public int[][] chances;
		
		ToolDisplayRecipe(AbstractStack input, AbstractStack[] tools, AbstractStack[] outputs, int[][] chances)
		{
			this.input = input;
			this.tools = tools;
			this.outputs = outputs;
			this.chances = chances;
		}
	}
	
	private static final List<ToolDisplayRecipe> LIST = new ArrayList<>();
	
	public static void addToolDisplayRecipe(AbstractStack input, AbstractStack[] tools, AbstractStack[] outputs)
	{
		addToolDisplayRecipe(input, tools, outputs, null);
	}
	
	public static void addToolDisplayRecipe(AbstractStack input, AbstractStack[] tools, AbstractStack[] outputs, int[][] chances)
	{
		LIST.add(new ToolDisplayRecipe(input, tools, outputs, chances));
	}
	
	public static List<ToolDisplayRecipe> getList()
	{
		return LIST;
	}
}
