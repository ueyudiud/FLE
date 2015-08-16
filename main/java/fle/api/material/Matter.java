package fle.api.material;

import static fle.api.enums.CompoundType.*;
import static fle.api.enums.EnumAtoms.*;
import static fle.api.enums.EnumIons.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fle.api.enums.CompoundType;
import fle.api.enums.EnumAtoms;
import fle.api.enums.EnumIons;
import fle.api.util.WeightHelper;

public class Matter implements IAtoms
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

	public static final Matter mC = new Matter(Molecular, new AtomStack(EnumAtoms.C, 1));

	public static final Matter mSiO2 = new Matter(Molecular, new AtomStack(Si4, 1), new AtomStack(O_2, 2));
	
	public static final Matter mNH3 = new Matter(Molecular, new AtomStack(N_3, 1), new AtomStack(H1, 3));
	public static final Matter mH2O = new Matter(Molecular, new AtomStack(H1, 2), new AtomStack(O_2));
	public static final Matter mCO = new Matter(Molecular, new AtomStack(C2), new AtomStack(O_2));
	public static final Matter mCO2 = new Matter(Molecular, new AtomStack(C4), new AtomStack(O_2, 2));
	public static final Matter mSO2 = new Matter(Molecular, new AtomStack(S4), new AtomStack(O_2, 2));
	public static final Matter mH2S = new Matter(Molecular, new AtomStack(H1, 2), new AtomStack(S_2));
	public static final Matter mHF = new Matter(Molecular, new AtomStack(H1), new AtomStack(F_1));
	public static final Matter mHCl = new Matter(Molecular, new AtomStack(H1), new AtomStack(Cl_1));
	public static final Matter mHBr = new Matter(Molecular, new AtomStack(H1), new AtomStack(Br_1));
	public static final Matter mHI = new Matter(Molecular, new AtomStack(H1), new AtomStack(I_1));

	public static final Matter mH2CO3 = new Matter(Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Carbonate));
	public static final Matter mH3PO4 = new Matter(Molecular, new AtomStack(H1), new AtomStack(Dihydrogen_Phosphate));
	public static final Matter mH2SO3 = new Matter(Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Sulfite));
	public static final Matter mH2SO4 = new Matter(Molecular, new AtomStack(H1, 2), new AtomStack(Sulfate));

	public static final Matter mN2 = new Matter(Molecular, new AtomStack(N, 2));
	public static final Matter mO2 = new Matter(Molecular, new AtomStack(O, 2));
	public static final Matter mAir = new Matter(Mix, new AtomStack(mN2, 4), new AtomStack(mO2));
	
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
		if(ions.length == 1)
		{
			return ions.toString();
		}
		String s = "";
		for(int i = 0; i < ions.length; ++i) 
		{
			if(ions[i].ion.isRadical() && (ions.length > 1 || ions[i].size != 1))
			{
				s += "(" + ions[i].toString() + ")";
				if(ions[i].size != 1)
					s += ions[i].size;
			}
			else
			{
				s += ions[i].toString();
			}
		}
		return s;
	}
	
	public static class AtomStack
	{
		private IAtoms ion;
		private int size;

		public AtomStack(IAtoms aIon) 
		{
			this(aIon, 1);
		}
		public AtomStack(IAtoms aIon, int aSize) 
		{
			ion = aIon;
			size = aSize;
		}
		
		public IAtoms get()
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

	@Override
	public String getChemicalFormulaName() 
	{
		return toString();
	}

	@Override
	public IAtoms[] getIonContain() 
	{
		List<IAtoms> ret = new ArrayList();
		for(int i = 0; i < ions.length; ++i)
		{
			ret.addAll(Arrays.asList(ions[i].ion.getIonContain()));
		}
		return ret.toArray(new IAtoms[ret.size()]);
	}

	@Override
	public boolean isRadical() 
	{
		return ions.length == 1 ? ions[0].ion.isRadical() : true;
	}
	
	private WeightHelper<EnumAtoms> wh;
	private WeightHelper<IAtoms> iwh;
	
	private void initCaculater()
	{
		int size = 0;
		int i;
		for(i = 0; i < ions.length; ++i)
		{
			size += ions[i].size;
		}
		EnumAtoms[][] ea = new EnumAtoms[size][];
		IAtoms[][] ea1 = new IAtoms[size][];
		int r = 0;
		for(i = 0; i < ions.length; ++i)
		{
			for(int j = 0; j < ions[i].size; ++j)
			{
				ea[r] = ions[i].get().getElementAtoms();
				ea1[r] = ions[i].get().getIonContain();
				++r;
			}
		}
		wh = new WeightHelper<EnumAtoms>(ea);
		iwh = new WeightHelper<IAtoms>(ea1);
	}

	public WeightHelper<IAtoms> getIonHelper()
	{
		if(iwh == null) initCaculater();
		return iwh;
	}

	public WeightHelper<EnumAtoms> getHelper()
	{
		if(wh == null) initCaculater();
		return wh;
	}
	
	public double getIconContain(IAtoms e) 
	{
		if(iwh == null) initCaculater();
		return iwh.getContain(e);
	}
	
	@Override
	public double getElementContain(EnumAtoms e) 
	{
		if(wh == null) initCaculater();
		return wh.getContain(e);
	}

	@Override
	public EnumAtoms[] getElementAtoms() 
	{
		if(wh == null) initCaculater();
		Object[] obj = wh.getList();
		EnumAtoms[] ret = new EnumAtoms[obj.length];
		for(int i = 0; i < obj.length; ++i) ret[i] = (EnumAtoms) obj[i];
		return ret;
	}
}