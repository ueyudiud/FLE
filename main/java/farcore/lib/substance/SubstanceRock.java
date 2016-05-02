package farcore.lib.substance;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumToolType;
import farcore.lib.collection.Register;
import farcore.util.SubTag;
import net.minecraft.util.IIcon;

public class SubstanceRock extends SubstanceBlockAbstract
{
	private static final Register<SubstanceRock> register = new Register();

	public static final SubstanceRock VOID_ROCK = (SubstanceRock) new SubstanceRock(0, "void").setHardness(1.0F).setDensity(1000);
	
	public static SubstanceRock getSubstance(String tag)
	{
		return register.get(tag, VOID_ROCK);
	}
	
	public static SubstanceRock getSubstance(int id)
	{
		return register.get(id, VOID_ROCK);
	}
	
	public static Register<SubstanceRock> getRocks()
	{
		return register;
	}
	
	public String localized;
	public SubstanceTool toolBelong;
	
	public SubstanceRock(String name)
	{
		super(name);
	}

	public SubstanceRock(int id, String name)
	{
		super(id, name);
	}
	
	public SubstanceRock(int id, String name, String local)
	{
		this(id, name);
		registerLocalName(localized = local);
	}
	
	@Override
	public SubstanceRock setDensity(int density)
	{
		super.setDensity(density);
		return this;
	}
	
	@Override
	public SubstanceRock setHardness(float hardness)
	{
		super.setHardness(hardness);
		return this;
	}

	public SubstanceRock setHarvestLevel(int level)
	{
		return setHarvestLevel(EnumToolType.pickaxe.name(), level);
	}
	
	@Override
	public SubstanceRock setHarvestLevel(String tool, int level)
	{
		super.setHarvestLevel(tool, level);
		return this;
	}
	
	public boolean isToolMaterial()
	{
		return toolBelong != null;
	}
	
	public SubstanceRock setToolBelong(SubstanceTool tool)
	{
		this.toolBelong = tool;
		toolBelong.add(SubTag.TOOL_stone, SubTag.TOOL_stone_real);
		toolBelong.registerLocalName(localized);
		return this;
	}

	public final String getType(){return "rock";}
	public Register getRegister() {return register;}
}