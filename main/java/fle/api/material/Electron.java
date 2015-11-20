package fle.api.material;

import java.util.Map;

import fle.api.enums.CompoundType;
import fle.api.enums.EnumAtoms;
import fle.api.material.Matter.AtomStack;
import fle.api.util.FleEntry;

public class Electron extends ElementaryParticle
{
	public static final Electron e = new Electron(false);
	public static final Electron e_neg = new Electron(true);
	
	boolean i;
	
	private Electron(boolean isNegtive)
	{
		i = isNegtive;
	}
	
	@Override
	public String getChemicalFormulaName()
	{
		return i ? "e+" : "e";
	}
	
	public Matter asMatter()
	{
		return Matter.forMatter(getChemicalFormulaName(), CompoundType.Ionic, new AtomStack(this));
	}
}