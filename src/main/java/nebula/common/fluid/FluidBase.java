package nebula.common.fluid;

import nebula.common.LanguageManager;
import nebula.common.util.Game;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidBase extends Fluid
{
	public float fireAttackDamage;
	public float thermalConductivity = 2.4E-2F;
	
	public FluidBase(String fluidName, String localName)
	{
		this(fluidName, localName,
				new ResourceLocation(Game.getActiveModID(), "fluids/" + fluidName),
				new ResourceLocation(Game.getActiveModID(), "fluids/" + fluidName + "_flowing"));
	}
	public FluidBase(String fluidName, String localName, ResourceLocation still, ResourceLocation flowing)
	{
		super(fluidName, still, flowing);
		FluidRegistry.registerFluid(this);
		if(localName != null)
		{
			LanguageManager.registerLocal(getTranslateName(new FluidStack(this, 1)), localName);
		}
	}
	
	protected String getTranslateName(FluidStack stack)
	{
		return getUnlocalizedName(stack) + ".name";
	}
	
	@Override
	public String getLocalizedName(FluidStack stack)
	{
		return LanguageManager.translateToLocal(getTranslateName(stack));
	}
	
	public FluidBase setThermalConductivity(float thermalConductivity)
	{
		this.thermalConductivity = thermalConductivity;
		return this;
	}
	
	public FluidBase setFireAttackDamage(float fireAttackDamage)
	{
		this.fireAttackDamage = fireAttackDamage;
		return this;
	}
}