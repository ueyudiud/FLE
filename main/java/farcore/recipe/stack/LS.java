package farcore.recipe.stack;

public class LS extends AS
{
	public LS(IItemChecker...checkers)
	{
		super(new LM(checkers));
	}
	public LS(int size, IItemChecker...checkers)
	{
		super(new LM(checkers), size);
	}
	public LS(LM matcher, int size)
	{
		super(matcher, size);
	}
}