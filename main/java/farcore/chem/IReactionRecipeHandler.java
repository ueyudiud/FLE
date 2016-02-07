package farcore.chem;

public interface IReactionRecipeHandler
{
	boolean matchRecipe(IReactionSystem system, IReactionRecipe recipe);
	
	void handleRecipe(IReactionSystem system, IReactionRecipe recipe);
}