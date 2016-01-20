package farcore.substance;

public interface PhaseDiagram<K, T>
{
	T getPhase(K condition);
	
	public enum Phase
	{
		SOLID,
		LIQUID,
		GAS,
		PLASMA,
		SUPERFLUID,//Used in such like CO2, etc.
		METALLIC,//Used in like metal H, etc.
		MESOMORPHIC;//Liquid crystal.
	}
}