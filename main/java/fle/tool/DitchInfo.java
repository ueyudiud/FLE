package fle.tool;

import farcore.collection.Register;
import farcore.util.U;
import flapi.material.MaterialAbstract;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.IFluidTank;

public class DitchInfo
{
	public static Register<DitchInfo> register = new Register();
	
	private MaterialAbstract material;
	private ItemStack block;
	private int viscosity;
	private float speed;
	private int maxTemp;

	public DitchInfo(String name, MaterialAbstract aMaterial, Block aBlock, float aSpeed, int aViscosity, int aTemp)
	{
		this(name, aMaterial, new ItemStack(aBlock), aViscosity, aSpeed, aTemp);
	}
	public DitchInfo(String name, MaterialAbstract aMaterial, Block aBlock, int meta, float aSpeed, int aViscosity, int aTemp)
	{
		this(name, aMaterial, new ItemStack(aBlock, 1, meta), aViscosity, aSpeed, aTemp);
	}
	public DitchInfo(String name, MaterialAbstract aMaterial, ItemStack aBlock, int aViscosity, float aSpeed, int aTemp)
	{
		block = aBlock;
		material = aMaterial;
		viscosity = aViscosity;
		speed = aSpeed;
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
	
	public boolean canStay(IFluidTank tank)
	{
		return U.F.temperature(tank.getFluid()) <= maxTemp;
	}
}