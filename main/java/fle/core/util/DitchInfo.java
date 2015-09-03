package fle.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import fle.api.material.MaterialAbstract;
import fle.api.util.IChemCondition;
import fle.api.util.IChemCondition.EnumPH;
import fle.api.util.Register;

public class DitchInfo
{
	public static Register<DitchInfo> register = new Register();
	
	private MaterialAbstract material;
	private ItemStack block;
	private int viscosity;
	private float speed;
	private EnumPH minPH;
	private EnumPH maxPH;
	private int maxTemp;

	public DitchInfo(String name, MaterialAbstract aMaterial, Block aBlock, float aSpeed, int aViscosity, int aTemp)
	{
		this(name, aMaterial, new ItemStack(aBlock), aViscosity, aSpeed, EnumPH.Weak_Acid, EnumPH.Weak_Alkali, aTemp);
	}
	public DitchInfo(String name, MaterialAbstract aMaterial, Block aBlock, int meta, float aSpeed, int aViscosity, int aTemp)
	{
		this(name, aMaterial, new ItemStack(aBlock, 1, meta), aViscosity, aSpeed, EnumPH.Weak_Acid, EnumPH.Weak_Alkali, aTemp);
	}
	public DitchInfo(String name, MaterialAbstract aMaterial, ItemStack aBlock, float aSpeed, int aViscosity, int aTemp)
	{
		this(name, aMaterial, aBlock, aViscosity, aSpeed, EnumPH.Weak_Acid, EnumPH.Weak_Alkali, aTemp);
	}
	public DitchInfo(String name, MaterialAbstract aMaterial, ItemStack aBlock, int aViscosity, float aSpeed, EnumPH min, EnumPH max, int aTemp)
	{
		block = aBlock;
		material = aMaterial;
		viscosity = aViscosity;
		speed = aSpeed;
		minPH = min;
		maxPH = max;
		maxTemp = aTemp;
		register.register(this, name);
	}
	
	public String getName()
	{
		return register.name(this);
	}
	
	public MaterialAbstract getMaterial()
	{
		return material;
	}
	
	public ItemStack getBlock()
	{
		return block;
	}
	
	public int getViscosity()
	{
		return viscosity;
	}
	
	public float getDripSpeed()
	{
		return speed;
	}
	
	public boolean canStay(IChemCondition cc)
	{
		int ph = cc.getPHLevel().ordinal();
		return maxTemp >= cc.getTemperature() && ph >= minPH.ordinal() && ph <= maxPH.ordinal();
	}
}