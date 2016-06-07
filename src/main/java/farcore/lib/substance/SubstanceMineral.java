package farcore.lib.substance;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumToolType;
import farcore.lib.collection.Register;
import farcore.util.SubTag;
import net.minecraft.util.IIcon;

public class SubstanceMineral extends SubstanceBlockAbstract
{
	private static final Register<SubstanceMineral> register = new Register();

	public static final SubstanceMineral VOID_MINERAL = (SubstanceMineral) new SubstanceMineral(0, "void");
	
	public static SubstanceMineral getSubstance(String tag)
	{
		return register.get(tag, VOID_MINERAL);
	}
	
	public static SubstanceMineral getSubstance(int id)
	{
		return register.get(id, VOID_MINERAL);
	}
	
	public static Register<SubstanceMineral> getMinerals()
	{
		return register;
	}
	
	public String localized;
	
	public SubstanceMineral(String name)
	{
		super(name);
	}

	public SubstanceMineral(int id, String name)
	{
		super(id, name);
	}
	
	public SubstanceMineral(int id, String name, String local)
	{
		this(id, name);
		registerLocalName(localized = local);
	}
	
	@Override
	public SubstanceMineral setDensity(int density)
	{
		super.setDensity(density);
		return this;
	}
	
	@Override
	public SubstanceMineral setHardness(float hardness)
	{
		super.setHardness(hardness);
		return this;
	}

	public SubstanceMineral setHarvestLevel(int level)
	{
		return setHarvestLevel(EnumToolType.pickaxe.name(), level);
	}
	
	@Override
	public SubstanceMineral setHarvestLevel(String tool, int level)
	{
		super.setHarvestLevel(tool, level);
		return this;
	}

	public final String getType(){return "mineral";}
	public Register getRegister() {return register;}
}