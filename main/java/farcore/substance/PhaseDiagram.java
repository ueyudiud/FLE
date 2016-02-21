package farcore.substance;

public interface PhaseDiagram<K, T>
{
	T getPhase(K condition);

	public enum EnumReactionSystemType
	{
		OPEN,
		CLOSE;
	}
	
	public enum Phase
	{		
		SOLID(1.0F, 0.0F),
		LIQUID(2.4E3F, 18.0F),
		GAS(6.4E7F, 240F),
		PLASMA(8E10F, 12800F),
		SUPERFLUID(8E7F, 500F),//Used in such like CO2, etc.
		METALLIC(0.8F, -8.0F),//Used in like metal H, etc.
		MESOMORPHIC(2.56E2F, 10.0F);//Liquid crystal.
		
		/** Use in part count and chemical reaction. */
		public final float area;
		public final float energy;
		
		private Phase(float speed, float energy)
		{
			this.area = speed;
			this.energy = energy;
		}
		
		public int isStaybleInSystem(EnumReactionSystemType system)
		{
			return system == EnumReactionSystemType.OPEN ? 
					(this == GAS ? 10 : this == PLASMA ? 4 : this == SUPERFLUID ? 6 : 0) : 0;
		}
	}
}