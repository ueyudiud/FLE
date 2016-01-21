package farcore.tileentity;

public interface INutritionTileEntity
{
	int getParticleContain(EnumNutrition nutrition);
	
	int useParticle(EnumNutrition nutrition, int max, boolean process);
	
	public static enum EnumNutrition
	{
		H2O, NH3, CH4, Ca, K, N, P, Fe, Cu, Cs;
	}
}
