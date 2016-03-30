package farcore;

public class FarCore
{
	public static final String ID = "farcore";
	public static final String OVERRIDE_ID = "fo";
	
	public static volatile int version = 300;

	public static String translateToLocal(String unlocalized, Object...arg)
	{
		return FarCoreSetup.lang.translateToLocal(unlocalized, arg);
	}
}