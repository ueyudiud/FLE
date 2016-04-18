package farcore.lib.substance;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.interfaces.ITreeGenerator;
import farcore.lib.collection.Register;
import farcore.lib.recipe.DropHandler;
import farcore.lib.world.gen.tree.TreeGenEmpty;
import net.minecraft.block.Block;

public class SubstanceWood implements ISubstance
{
	private static final Register<SubstanceWood> register = new Register();

	private static final TreeGenEmpty VOID_GEN = new TreeGenEmpty();
	public static final SubstanceWood WOOD_VOID = new SubstanceWood(0, "void").setMaxUses(1, 1);
	
	public static SubstanceWood getSubstance(String tag)
	{
		return register.get(tag, WOOD_VOID);
	}
	
	public static SubstanceWood getSubstance(int id)
	{
		return register.get(id, WOOD_VOID);
	}
	
	public static Register<SubstanceWood> getWoods()
	{
		return register;
	}
	
	protected final String name;
	/**
	 * Unit : kN
	 */
	public float hardness = 1.0F;
	/**
	 * Unit : kg/m^3
	 */
	public int density = 100;
	/**
	 * Unit : %
	 */
	public float ashcontent = 25.0F;
	/**
	 * Unit : %
	 */
	public float watercontent = 10.0F;
	/**
	 * Unit : J / mol
	 */
	public int burnEnergyPerUnit = 1000;
	
	public boolean canMakeSoftTool = false;
	public boolean canMakeHardTool = false;

	public int maxSoftUses = -1;
	public int maxHardUses = -1;
		
	public SubstanceTool tool;
	public boolean isTree = false;
	/**
	 * The generator that the root of tree is on generation coordinate.
	 */
	public ITreeGenerator generator = VOID_GEN;
	/**
	 * This two block is for tree generation.
	 */
	public Block log;
	public Block leaves;

	public DropHandler leafDrop = DropHandler.EMPTY;
	
	public SubstanceWood(int id, String name)
	{
		this.name = name;
		register.register(id, name, this);
	}
	public SubstanceWood(String name)
	{
		this.name = name;
		register.register(name, this);
	}
	
	public SubstanceWood setHardness(float hardness)
	{
		this.hardness = hardness;
		return this;
	}
	
	public SubstanceWood setDensity(int density)
	{
		this.density = density;
		return this;
	}
	
	public SubstanceWood setAshcontent(float ashcontent)
	{
		this.ashcontent = ashcontent;
		return this;
	}
	
	public SubstanceWood setWatercontent(float watercontent)
	{
		this.watercontent = watercontent;
		return this;
	}
	
	public SubstanceWood setMaxUses(int maxUses)
	{
		this.maxSoftUses = maxUses;
		this.canMakeSoftTool = true;
		return this;
	}
	
	public SubstanceWood setMaxUses(int maxSoftUses, int maxHardUses)
	{
		this.maxSoftUses = maxSoftUses;
		this.maxHardUses = maxHardUses;
		if(maxSoftUses > 0)
		{
			this.canMakeSoftTool = true;
		}
		this.canMakeHardTool = true;
		return this;
	}
	
	public SubstanceWood setTreeGen(ITreeGenerator generator)
	{
		this.isTree = true;
		this.generator = generator;
		return this;
	}
	
	public SubstanceWood setBurnEnergy(int energy)
	{
		this.burnEnergyPerUnit = energy;
		return this;
	}
	
	public SubstanceWood setLeafDrop(DropHandler handler)
	{
		this.leafDrop = handler;
		return this;
	}
	
	public SubstanceTool provide()
	{
		if(canMakeHardTool)
		{
			tool = new SubstanceTool(getID() + 4000, "wood_hard-" + name);
			tool.setDigSpeed((hardness * 0.2F) + 1.0F);
			tool.setHarvestLevel(5);
			tool.setMaxUses(maxHardUses);
			return tool;
		}
		else if(canMakeSoftTool)
		{
			tool = new SubstanceTool(getID() + 5000, "wood_soft-" + name);
			tool.setDigSpeed(hardness + 1.0F);
			tool.setHarvestLevel(3);
			tool.setMaxUses(maxSoftUses);
			return tool;
		}
		return null;
	}
	
	//Override method
	public final String getName(){return name;}
	public final int getID(){return register.id(this);}
	public final Register<SubstanceWood> getRegister(){return register;}

	public void registerLocalName(String name)
	{
		FarCoreSetup.lang.registerLocal("substance.wood." + name, name);
	}
	
	public String getLocalName()
	{
		return FarCore.translateToLocal("substance.wood." + name);
	}
}