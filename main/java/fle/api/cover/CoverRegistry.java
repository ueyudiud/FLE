package fle.api.cover;

import fle.api.util.Register;

public class CoverRegistry
{
	private static final Register<Cover> coverRegister = new Register();

	public static void registerCover(Cover cover, String id)
	{
		if(coverRegister.contain(id)) throw new RuntimeException("FLE API: Some mod register cover with same id " + id + ".");
		coverRegister.register(cover, id);
	}
	public static void registerCover(int idx, Cover cover, String id)
	{
		if(coverRegister.contain(id) || coverRegister.contain(idx)) throw new RuntimeException("FLE API: Some mod register cover with same id");
		coverRegister.register(idx, cover, id);
	}
	
	public static Register<Cover> getCoverRegister()
	{
		return coverRegister;
	}
}