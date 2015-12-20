package flapi.material;

import flapi.enums.CompoundType;
import flapi.material.Matter.AtomStack;

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
	public String getChemName()
	{
		return i ? "e+" : "e";
	}
	
	public Matter asMatter()
	{
		return Matter.forMatter(getChemName(), CompoundType.Ionic, new AtomStack(this));
	}
}