package fle.api.item.behavior;

public class BehaviorWhetstone extends BehaviorDigableTool
{
	public BehaviorWhetstone()
	{
		destroyBlockDamageBase = 2.5F;
		destroyBlockDamageHardnessMul = 1.4F;
		hitEntityDamage = 1;
	}
}