package fla.api.chem;

public interface IAtom
{
	public String getChemicalFormulaName();

	public IAtom[] getIonContain();
	
	public boolean isRadical();
}
