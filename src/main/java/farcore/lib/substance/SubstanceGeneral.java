package farcore.lib.substance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import farcore.interfaces.ISubTagContainer;
import farcore.lib.collection.Register;
import farcore.util.IDataChecker;
import farcore.util.SubTag;

/**
 * The generalist substance of register.
 * @author ueyudiud
 *
 */
public class SubstanceGeneral implements ISubstance, ISubTagContainer
{
	private static final Register<SubstanceGeneral> register = new Register();

	public static final SubstanceGeneral VOID = (SubstanceGeneral) new SubstanceGeneral(0, "default", "void", "Void");
	
	public static Register<SubstanceGeneral> register()
	{
		return register;
	}
	
	public static List<SubstanceGeneral> split(IDataChecker<ISubTagContainer> checker)
	{
		List<SubstanceGeneral> ret = new ArrayList();
		for(SubstanceGeneral substance : register)
		{
			if(checker.isTrue(substance))
			{
				ret.add(substance);
			}
		}
		return ret;
	}
	
	//Allowed id range.        General id        Local id
	SubstanceDirt dirt;//      0-1023            0-1024
	SubstanceRock rock;//      1024-2047         0-1024
	SubstanceMineral mineral;//2048-3071         0-1024
	SubstanceSand sand;//      3072-4095         0-1024
//	SubstanceMetal metal;//    4096-5119         0-1024
	SubstanceWood wood;//      5120-6143         0-1024
//  SubstanceChemical chem;//  6144-7167         Allowed all general substance
//	SubstanceLiquid liquid;//  based on other substance type
//	SubstanceGas gas;//        based on other substance type
//	SubstancePlasma plasma;//  based on other substance type
//	SubstanceSuperfluid sf;//  based on other substance type
	SubstanceTool tool;//      based on other substance type
	SubstanceHandle handle;//  based on other substance type
	
	final int id;
	final String type;
	final String name;
	String oreName;
	
	public SubstanceGeneral(int id, String type, String name, String oreName)
	{
		register.register(id, name, this);
		this.type = type;
		this.name = name;
		this.id = id;
		this.oreName = oreName;
	}

	public String type()
	{
		return type;
	}
	
	public boolean isDirt()
	{
		return dirt != null;
	}
	
	public SubstanceDirt setDirt()
	{
		if(isDirt()) throw new RuntimeException("The dirt is already created.");
		this.dirt = new SubstanceDirt(id, name);
		return dirt;
	}
	
	public SubstanceDirt getDirt()
	{
		return dirt;
	}
	
	public boolean isRock()
	{
		return rock != null;
	}
	
	public SubstanceRock setRock()
	{
		if(isRock()) throw new RuntimeException("The rock is already created.");
		this.rock = new SubstanceRock(id - 1024, name);
		return rock;
	}
	
	public SubstanceRock getRock()
	{
		return rock;
	}
	
	public boolean isMineral()
	{
		return mineral != null;
	}
	
	public SubstanceMineral setMineral()
	{
		if(isMineral()) throw new RuntimeException("The mineral is already created.");
		this.mineral = new SubstanceMineral(id, oreName);
		return mineral;
	}
	
	public SubstanceMineral getMineral()
	{
		return mineral;
	}
	
	public boolean isHandle()
	{
		return handle != null;
	}
	
	public SubstanceHandle setHandleFromWood()
	{
		if(isHandle()) throw new RuntimeException("The wooden handle is already created.");
		if(isWood())
		{
			this.handle = new SubstanceHandle(id, name).setUseMultipler(0.8F + wood.hardness * 0.1F);
		}
		return handle;
	}
	
	public boolean isToolMaterial()
	{
		return tool != null;
	}
	
	public SubstanceTool setToolFromWood()
	{
		if(isToolMaterial()) throw new RuntimeException("The tool is already created.");
		if(isWood())
		{
			this.tool = wood.provide();
		}
		return this.tool;
	}
	
	public SubstanceTool setToolFromRock()
	{
		if(isToolMaterial()) throw new RuntimeException("The tool is already created.");
		if(isRock())
		{
			rock.setToolBelong(this.tool = new SubstanceTool(id, getName()));
		}
		return this.tool;
	}
	
	public boolean isSand()
	{
		return sand != null;
	}
	
	public boolean isWood()
	{
		return wood != null;
	}
	
	public String provideOreName(String prefix)
	{
		return prefix + oreName;
	}
	
	@Override
	public final String getType()
	{
		return "general";
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final int getID()
	{
		return id;
	}

	@Override
	public final Register<? extends ISubstance> getRegister() 
	{
		return register;
	}

	
	private List<SubTag> list = new ArrayList();
	

	@Override
	public void add(SubTag... tags)
	{
		if(tags.length == 1)
		{
			list.add(tags[0]);
		}
		else
		{
			list.addAll(Arrays.asList(tags));
		}
	}

	
	@Override
	public boolean contain(SubTag tag)
	{
		return list.contains(tag);
	}
}