package fla.api.chem;

import static fla.api.chem.EnumAtom.*;
import static fla.api.chem.EnumIon.*;
import static fla.api.chem.Matter.CompoundType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matter implements IAtom
{	
	public static final Matter mFeS = new Matter(Ionic, new AtomStack(Fe2, 1), new AtomStack(S_2, 1));
	
	public static final Matter mCu = new Matter(Alloy, new AtomStack(Cu, 1));
	public static final Matter mCuO = new Matter(Ionic, new AtomStack(Cu2, 1), new AtomStack(O_2, 1));
	public static final Matter mCuS = new Matter(Ionic, new AtomStack(Cu2, 1), new AtomStack(S_2, 1));
	public static final Matter mCu2O = new Matter(Ionic, new AtomStack(Cu1, 2), new AtomStack(O_2, 1));
	public static final Matter mCu2S = new Matter(Ionic, new AtomStack(Cu1, 2), new AtomStack(S_2, 1));
	public static final Matter mCuCO3 = new Matter(Ionic, new AtomStack(Cu2, 1), new AtomStack(Carbonate, 1));
	public static final Matter mCu_OH2 = new Matter(Ionic, new AtomStack(Cu2, 1), new AtomStack(Hydroxide, 2));
	public static final Matter mCu_OH2_CO3 = new Matter(Ionic, new AtomStack(mCu_OH2), new AtomStack(mCuCO3));
	public static final Matter mCu_OH2_2CO3 = new Matter(Ionic, new AtomStack(mCu_OH2), new AtomStack(mCuCO3, 2));

	public static final Matter mAg = new Matter(Alloy, new AtomStack(Ag, 1));
	
	public static final Matter mAu = new Matter(Alloy, new AtomStack(Au, 1));

	public static final Matter mSb2S3 = new Matter(Ionic, new AtomStack(Sb3, 2), new AtomStack(S_2, 3));
	
	public static final Matter mCuFeS2 = new Matter(Ionic, new AtomStack(Cu2), new AtomStack(Fe2), new AtomStack(S_2, 2));
	public static final Matter mCu3AsS4 = new Matter(Ionic, new AtomStack(Cu1, 3), new AtomStack(As5), new AtomStack(S_2, 4));
	public static final Matter mCu5FeS4 = new Matter(Ionic, new AtomStack(Cu1, 5), new AtomStack(Fe3), new AtomStack(S_2, 4));
	public static final Matter mCu10Fe2Sb4S13 = new Matter(Ionic, new AtomStack(Cu1, 10), new AtomStack(Fe2, 2), new AtomStack(Sb3, 4), new AtomStack(S_2, 13));
	
	public static final Matter mNH3 = new Matter(Molecular, new AtomStack(N_3, 1), new AtomStack(H1, 3));
	public static final Matter mH2O = new Matter(Molecular, new AtomStack(H1, 2), new AtomStack(O_2));
	public static final Matter mH2S = new Matter(Molecular, new AtomStack(H1, 2), new AtomStack(S_2));
	public static final Matter mHF = new Matter(Molecular, new AtomStack(H1), new AtomStack(F_1));
	public static final Matter mHCl = new Matter(Molecular, new AtomStack(H1), new AtomStack(Cl_1));
	public static final Matter mHBr = new Matter(Molecular, new AtomStack(H1), new AtomStack(Br_1));
	public static final Matter mHI = new Matter(Molecular, new AtomStack(H1), new AtomStack(I_1));

	public static final Matter mH2CO3 = new Matter(Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Carbonate));
	public static final Matter mH3PO4 = new Matter(Molecular, new AtomStack(H1), new AtomStack(Dihydrogen_Phosphate));
	public static final Matter mH2SO3 = new Matter(Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Sulfite));
	public static final Matter mH2SO4 = new Matter(Molecular, new AtomStack(H1, 2), new AtomStack(Sulfate));
	
	protected CompoundType ct;
	protected AtomStack[] ions;
	
	public Matter(CompoundType aCt, AtomStack...aIons) 
	{
		ions = aIons;
		ct = aCt;
	}
	
	@Override
	public String toString() 
	{
		String s = "";
		s += ions[0].toString();
		for(int i = 1; i < ions.length; ++i) 
		{
			s += ions[i].toString();
		}
		return s;
	}
	
	public static class AtomStack
	{
		private IAtom ion;
		private int size;

		public AtomStack(IAtom aIon) 
		{
			this(aIon, 1);
		}
		public AtomStack(IAtom aIon, int aSize) 
		{
			ion = aIon;
			size = aSize;
		}
		
		public IAtom get()
		{
			return ion;
		}
		
		public int size()
		{
			return size;
		}
		
		@Override
		public String toString() 
		{
			return size == 1 ? ion.getChemicalFormulaName() : ion.isRadical() ? "(" + ion.getChemicalFormulaName() + ")" + String.valueOf(size) : ion.getChemicalFormulaName() + String.valueOf(size);
		}
	}

	public static enum CompoundType
	{
		Ionic,
		Molecular,
		Alloy;
	}

	
	@Override
	public String getChemicalFormulaName() 
	{
		return toString();
	}

	
	@Override
	public IAtom[] getIonContain() 
	{
		List<IAtom> ret = new ArrayList();
		for(int i = 0; i < ions.length; ++i)
		{
			ret.addAll(Arrays.asList(ions[i].ion.getIonContain()));
		}
		return ret.toArray(new IAtom[ret.size()]);
	}


	@Override
	public boolean isRadical() 
	{
		return ions.length == 1 ? ions[0].ion.isRadical() : true;
	}
}