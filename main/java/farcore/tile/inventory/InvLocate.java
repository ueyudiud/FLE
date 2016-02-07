package farcore.tile.inventory;

public class InvLocate
{
	final char invChar;
	final int width;
	final int height;
	final boolean flag1;
	int locateID;
	int startID;
	int endID;
	/** The interval length in container. */
	int interval;

	public InvLocate(char chr, int size)
	{
		this(chr, size, 1);
	}
	public InvLocate(char chr, int size, boolean accessInput)
	{
		this(chr, size, 1, true);
	}
	public InvLocate(char chr, int width, int height)
	{
		this(chr, width, height, true);
	}
	public InvLocate(char chr, int width, int height, boolean accessInput)
	{
		this.invChar = chr;
		this.width = width;
		this.height = height;
		this.flag1 = accessInput;
	}
	
	public InvLocate setInterval(int interval)
	{
		this.interval = interval;
		return this;
	}
	
	public int size()
	{
		return width * height;
	}
}