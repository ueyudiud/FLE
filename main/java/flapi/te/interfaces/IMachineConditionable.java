package flapi.te.interfaces;

public interface IMachineConditionable
{
	Condition getCondition();
	
	public enum Condition
	{
		Waiting,
		Disabled,
		Working,
		Error;
	}
}