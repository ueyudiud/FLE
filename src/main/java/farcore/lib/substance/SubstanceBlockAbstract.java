package farcore.lib.substance;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.lib.collection.Register;
import net.minecraft.util.IIcon;

public abstract class SubstanceBlockAbstract implements ISubstance
{
	protected final String name;
	
	public String harvestTool;
	
	public int harvestLevel;
	/**
	 * Use in block.
	 */
	public float hardness = 1.0F;
	/**
	 * Unit : kg/m^3
	 */
	public int density = 100;
	
	@SideOnly(Side.CLIENT)
	public IIcon icon;

	public SubstanceBlockAbstract(int id, String name)
	{
		this.name = name;
		getRegister().register(id, name, this);
	}
	public SubstanceBlockAbstract(String name)
	{
		this.name = name;
		getRegister().register(name, this);
	}
	
	public SubstanceBlockAbstract setHardness(float hardness)
	{
		this.hardness = hardness;
		return this;
	}
	
	public SubstanceBlockAbstract setDensity(int density)
	{
		this.density = density;
		return this;
	}
	
	public SubstanceBlockAbstract setHarvestLevel(String tool, int level)
	{
		this.harvestTool = tool;
		this.harvestLevel = level;
		return this;
	}
	
	//Override method
	public final String getName(){return name;}
	public final int getID(){return getRegister().id(this);}
	public abstract Register getRegister();
}