package farcore.recipe.stack;

public class OS extends AS
{
	public OS(String string)
	{
		this(string, 1);
	}
	public OS(String string, int size)
	{
		this(new OM(string), size);
	}
	public OS(String string, boolean flag, int size)
	{
		this(new OM(string, flag), size);
	}
	public OS(OM matcher, int size)
	{
		super(matcher, size);
	}
}