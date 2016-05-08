package farcore.lib.matter;

import java.util.List;

import farcore.interfaces.IRegisterExceptionHandler;
import farcore.lib.collection.Register;

public class MatterRegistry implements IRegisterExceptionHandler
{
	private static final MatterRegistry REGISTRY = new MatterRegistry();
	private static final Register<IMatter> register = new Register(REGISTRY);
	
	public static List<IMatter> getMatters()
	{
		return register.targets();
	}
	
	public static void registerMatter(IMatter matter)
	{
		register.register(matter.name(), matter);
	}
	
	public static boolean isMatterRegistered(String name)
	{
		return register.contain(name);
	}
	
	public static boolean isMatterRegistered(IMatter matter)
	{
		return register.contain(matter);
	}
	
	public static int getMatterID(IMatter matter)
	{
		return register.id(matter);
	}
	
	public static IMatter getMatterFromID(int id)
	{
		return register.get(id);
	}
	
	public static IMatter getMatter(String name)
	{
		return register.get(name);
	}
	
	private MatterRegistry() {	}

	@Override
	public void onIDContain(int id, Object registered)
	{
		;
	}

	@Override
	public void onNameContain(int id, String registered)
	{
		throw new IllegalArgumentException("The matter named " + registered + " has already used! Please check your mod.");
	}
}