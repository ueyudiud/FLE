package farcore.lib.crop;

import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;

public class CropManager
{
	private static final IRegister<CropCard> REGISTER = new Register();
	
	public static void register(CropCard card)
	{
		REGISTER.register(card.name(), card);
	}
	
	public static CropCard getCrop(String name)
	{
		return REGISTER.get(name);
	}
	
	public static IRegister<CropCard> getRegister()
	{
		return REGISTER;
	}
}