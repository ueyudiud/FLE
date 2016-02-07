package farcore.util;

public interface ISubTagContainer
{
	public boolean contain(SubTag tag);
	
	public void add(SubTag...tag);

	public void remove(SubTag tag);
}
