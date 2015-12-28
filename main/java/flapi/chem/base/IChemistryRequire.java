package flapi.chem.base;

public interface IChemistryRequire
{
	boolean match(IChemCondition condition);
	
	float speed(IChemCondition condition);
}