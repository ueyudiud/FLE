package fle.core.recipe.matter;

import flapi.chem.base.IChemCondition.EnumOxide;
import flapi.chem.base.IChemCondition.EnumPH;
import flapi.chem.base.IMolecular;
import flapi.chem.base.Matter;
import flapi.chem.particle.Atoms;
import flapi.chem.particle.Electron;
import flapi.chem.particle.Ions;
import flapi.collection.abs.Stack;
import flapi.material.MatterReactionRegister;
import fle.core.recipe.MatterPhaseChangeRecipe;
import fle.core.recipe.MatterPhaseChangeRecipe.MatterInfo;
import fle.core.recipe.MatterReactionRecipe;
import fle.core.recipe.MatterSingleRecipe;

@Deprecated
public class ReactionRecipe1
{
	public static void addRecipes()
	{
		addHRecipe();
		addBRecipe();
		addCRecipe();
		addNRecipe();
		addORecipe();
		addFRecipe();
		addSiRecipe();
		addPRecipe();
		addSRecipe();
		addClRecipe();
		addAsRecipe();
		addSeRecipe();
		addBrRecipe();
	}
	
	public static void addHRecipe()
	{
		//H2(g)
		addRecipe(Matter.mH2, true, 31, 0.0000028F, 0.0001031F, new Matter[0]);
		//H2O(g)
		addRecipe(Matter.mH2O, true, 384, 0.0048F, 0.0058F, new Matter[0]);
		//H- = H + e-
		addRecipe(Ions.H_1, 172, EnumOxide.H2, 0.00038, -0.00481, 0.0000001841, 
				new Stack(Atoms.H.asMatter()), new Stack(Electron.e.asMatter()));
		//H = H+ + e-
		addRecipe(Atoms.H, 209, EnumOxide.S, 0.00018, -0.00381, 0.0000002841, 
				new Stack(Ions.H1.asMatter()), new Stack(Electron.e.asMatter()));
		//H+ + e- = H
		addRecipe(Ions.H1, Electron.e.asMatter(), 209, EnumPH.Water, EnumPH.MaxPH, 0.00018, -0.000181, 0.000001841, 
				new Stack(Atoms.H.asMatter()));
		//H+ + H- = H2
		addRecipe(Ions.H1, Ions.H_1.asMatter(), 0, EnumPH.MinPH, EnumPH.MaxPH, -0.00000031, -0.00000181, 0.95, 
				new Stack(Matter.mH2));
		//H+ + O2- = OH-
		addRecipe(Ions.H1, Ions.O_2.asMatter(), 0, EnumPH.Super_Alkali, EnumPH.MinPH, -0.00000031, -0.00000181, 0.0785, 
				new Stack(Ions.Hydroxide.asMatter()));
		//H+ + OH- = H2O
		addRecipe(Ions.H1, Ions.Hydroxide.asMatter(), 18, EnumPH.Weak_Alkali, EnumPH.MinPH, -0.00000089, -0.00000381, 0.0615, 
				new Stack(Matter.mH2O));
		//H+ + H2O = H3O+
		addRecipe(Ions.H1, Matter.mH2O, 18, EnumPH.Weak_Acid, EnumPH.MinPH, 0.00000019, -0.000381, 0.0615, 
				new Stack(Ions.Hydronium.asMatter()));
		//H- + H2O = H2 + OH-
		addRecipe(Ions.H_1, Matter.mH2O, 182, EnumPH.Weak_Acid, EnumPH.MinPH, 0.019, 0.000388, 0.00835, 
				new Stack(Ions.Hydroxide.asMatter()), new Stack(Matter.mH2));
	}
	
	public static void addBRecipe()
	{
		
	}
	
	public static void addCRecipe()
	{
		//CO2(g)
		addRecipe(Matter.mCO2, true, 201, 0.0000048F, 0.00118F, new Matter[0]);
		//CO(g)
		addRecipe(Matter.mCO2, true, 201, 0.00027F, 0.00358F, new Matter[0]);
		//H2CO3 = H2O + CO2(g)
		addRecipe(Matter.mH2CO3, true, 311, 0.000048F, 0.00102F, new Matter[]{Matter.mH2O, Matter.mCO2});
		//CO32- = O2- + CO2(g)
		addRecipe(Ions.Carbonate, true, 511, 0.0000248F, 0.000131F, new Matter[]{Ions.O_2.asMatter(), Matter.mCO2});
		//C = C2+ + 2e-
		addRecipe(Atoms.C, 298, EnumOxide.Br2, 0.00000018, -0.000381, 0.000001964, 
				new Stack(Ions.C2.asMatter()), new Stack(Electron.e.asMatter(), 2));
		//C + O2 = CO2
		addRecipe(Atoms.C, Matter.mO2, 581, EnumPH.MaxPH, EnumPH.MinPH, 0.000003109, 0, 0.000835,
				new Stack(Matter.mCO2));
		//C + O = CO
		addRecipe(Atoms.C, Atoms.O.asMatter(), 574, EnumPH.MaxPH, EnumPH.MinPH, 0.000003119, 0, 0.000945,
				new Stack(Matter.mCO));
		//C4+ + O2- = CO
		addRecipe(Ions.C4, Ions.O_2.asMatter(), 374, EnumPH.MaxPH, EnumPH.MinPH, 0.000003119, 0, 0.001248,
				new Stack(Matter.mCO));
		//CO + O2 = CO2 + O
		addRecipe(Matter.mCO, Matter.mO2, 574, EnumPH.MaxPH, EnumPH.MinPH, 0.0003119, 0, 0.000945,
				new Stack(Matter.mCO2), new Stack(Atoms.O.asMatter()));
		//CO + O = CO2
		addRecipe(Atoms.O, Matter.mCO, 574, EnumPH.MaxPH, EnumPH.MinPH, 0.0004119, 0, 0.00128,
				new Stack(Matter.mCO2));
		//CO2 + C = 2CO
		addRecipe(Atoms.C, Matter.mCO2, 719, EnumPH.MaxPH, EnumPH.MinPH, 0.0002603, 0, 0,
				new Stack(Matter.mCO2));
		
	}
	
	public static void addNRecipe()
	{
		//N2(g)
		addRecipe(Matter.mN2, true, 178, 0.0048F, 0.000058F, new Matter[0]);
		//2O = O2
		addRecipe(Atoms.N, Atoms.N.asMatter(), 48, EnumPH.MaxPH, EnumPH.MinPH, 0.000941, 0, 0.08311, new Stack(Matter.mN2));
	}
	
	public static void addORecipe()
	{
		//O2(g)
		addRecipe(Matter.mO2, true, 194, 0.0038F, 0.000058F, new Matter[0]);
		//2O = O2
		addRecipe(Atoms.O, Atoms.O.asMatter(), 48, EnumPH.MaxPH, EnumPH.MinPH, 0.000741, 0, 0.08311, new Stack(Matter.mO2));
	}
	
	public static void addFRecipe()
	{
		
	}
	
	public static void addSiRecipe()
	{
		
	}
	
	public static void addPRecipe()
	{
		
	}
	
	public static void addSRecipe()
	{
		//SO2(g)
		addRecipe(Matter.mSO2, true, 374, 0.00148F, 0.0098F, new Matter[0]);
		//SO3(g)
		addRecipe(Matter.mSO3, true, 384, 0.00148F, 0.0088F, new Matter[0]);
		//S + O2 = SO2
		addRecipe(Atoms.S, Matter.mO2, 491, EnumPH.MaxPH, EnumPH.MinPH, 0.0003109, 0, 0.001635,
				new Stack(Matter.mSO2));
		//SO + O2 = SO2 + O
		addRecipe(Matter.mSO, Matter.mO2, 491, EnumPH.MaxPH, EnumPH.MinPH, 0.0015471, 0, 0.01635,
				new Stack(Matter.mSO2), new Stack(Atoms.O.asMatter()));
		//SO + O = SO2
		addRecipe(Matter.mSO, Atoms.O.asMatter(), 491, EnumPH.MaxPH, EnumPH.MinPH, 0.0015471, 0, 0.01835,
				new Stack(Matter.mSO2));
		//SO + H2S = H2O + 2S
		addRecipe(Matter.mSO, Matter.mH2S, 198, EnumPH.MaxPH, EnumPH.Weak_Alkali, 0.0000809, 0.00000673, 0.00935,
				new Stack(Matter.mH2O), new Stack(Atoms.S.asMatter(), 2));
		//S2- + O = SO + 2e-
		addRecipe(Ions.S_2, Atoms.O.asMatter(), 203, EnumPH.MaxPH, EnumPH.MinPH, 0.0003109, 0, 0.008635,
				new Stack(Matter.mSO), new Stack(Electron.e.asMatter()));
		//S2- + H+ = HS-
		addRecipe(Ions.S_2, Ions.H1.asMatter(), 193, EnumPH.Strong_Alkali, EnumPH.MinPH, 0.0004109, 0.00000131, 0.006635,
				new Stack(Ions.Hydrogen_Sulfide.asMatter()));
		//HS- + H+ = H2S
		addRecipe(Ions.Hydrogen_Sulfide, Ions.H1.asMatter(), 198, EnumPH.Weak_Acid, EnumPH.MinPH, 0.0001109, 0.00000173, 0.001635,
				new Stack(Matter.mH2S));
		//H2S + O2 = H2O + SO
		addRecipe(Matter.mO2, Matter.mH2S, 351, EnumPH.MaxPH, EnumPH.MinPH, 0.001109, 0, 0.00000513,
				new Stack(Matter.mH2O), new Stack(Matter.mSO));
	}
	
	public static void addClRecipe()
	{
		
	}
	
	public static void addAsRecipe()
	{
		//As(g)
		addRecipe(Atoms.As.asMatter(), true, 1029, 0.0000041F, 0.0000192F, new Matter[0]);
		//As4S6 = 2As2S3
		addRecipe(Matter.mAs4S6, 460, EnumOxide.Lowest, 0.00214, 0, 0.01041, new Stack(Matter.mAs2S3, 2));
		//As2S3 + O2 = As3+ + 2SO + S2-
		addRecipe(Matter.mAs2S3, Matter.mO2, 819, EnumPH.MaxPH, EnumPH.MinPH, 0.00015191, 0, 0.000415, 
				new Stack(Ions.As3, 2), new Stack(Matter.mSO, 2), new Stack(Ions.S_2));
		//As3+ + O2 = AsO23+
		addRecipe(Ions.As3, Matter.mO2, 601, EnumOxide.Highest, EnumOxide.C, 0.00041841, -0.00518451, 0.004812, 
				new Stack(Ions.Arsenitonium.asMatter()));
		//2AsO23+ = AsO43+ + As3+
		addRecipe(Ions.Arsenitonium, Ions.Arsenitonium.asMatter(), 601, EnumOxide.Highest, EnumOxide.C, 0.00041841, -0.00518451, 0.0812, 
				new Stack(Ions.Arsenate.asMatter()), new Stack(Ions.As3.asMatter()));
		//As3+ + C = C4+ + As + e-
		addRecipe(Ions.As3, Atoms.C.asMatter(), 681, EnumOxide.Highest, EnumOxide.C, 0.000002741, 0.00118451, 0.0000812, 
				new Stack(Atoms.As.asMatter()), new Stack(Ions.C4.asMatter()), new Stack(Electron.e.asMatter()));
		
	}
	
	public static void addSeRecipe()
	{
		
	}
	
	public static void addBrRecipe()
	{
		
	}

	static void addRecipe(IMolecular atom, boolean gasPhease, int temp, float bE, float tE, Matter...output)
	{
		MatterPhaseChangeRecipe.register(new MatterInfo(gasPhease, atom, temp, bE, tE, output));
	}
	static void addRecipe(Atoms atom, Matter matter, int tempreture, EnumPH ph1, EnumPH ph2, 
			double temE, double phE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(atom.asMatter(), matter, tempreture, ph1, ph2, phE, temE, baseE, stacks));
	}
	static void addRecipe(Atoms atom, Matter matter, int tempreture, EnumOxide oxi1, EnumOxide oxi2,
			double temE, double oxiE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(atom.asMatter(), matter, tempreture, oxi1, oxi2, 0, oxiE, temE, baseE, stacks));
	}
	static void addRecipe(Ions ion, Matter matter, int tempreture, EnumOxide oxi1, EnumOxide oxi2,
			double temE, double oxiE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(ion.asMatter(), matter, tempreture, oxi1, oxi2, 0, oxiE, temE, baseE, stacks));
	}
	static void addRecipe(Matter matter1, Matter matter2, int tempreture, EnumOxide oxi1, EnumOxide oxi2,
			double temE, double oxiE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(matter1, matter2, tempreture, oxi1, oxi2, 0, oxiE, temE, baseE, stacks));
	}
	static void addRecipe(Ions ion, Matter matter, int tempreture, EnumPH ph1, EnumPH ph2, 
			double temE, double phE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(ion.asMatter(), matter, tempreture, ph1, ph2, phE, temE, baseE, stacks));
	}
	static void addRecipe(Matter matter, Matter matter2, int tempreture, EnumPH ph1, EnumPH ph2, 
			double temE, double phE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(matter, matter2, tempreture, ph1, ph2, phE, temE, baseE, stacks));
	}
	static void addRecipe(Atoms atom, int tempreture, EnumOxide oxideLevel, 
			double temE, double oxiE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterSingleRecipe(atom.asMatter(), tempreture, oxideLevel, oxiE, temE, baseE, stacks));
	}
	static void addRecipe(Ions ion, int tempreture, EnumOxide oxideLevel, 
			double temE, double oxiE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterSingleRecipe(ion.asMatter(), tempreture, oxideLevel, 0, oxiE, temE, stacks));
	}
	static void addRecipe(Matter matter, int tempreture, EnumOxide oxideLevel, 
			double temE, double oxiE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterSingleRecipe(matter, tempreture, oxideLevel, 0, oxiE, temE, stacks));
	}
	

	static void addRecipe(Atoms atom, int size, Matter matter, int size1, int tempreture, EnumPH ph1, EnumPH ph2, 
			double temE, double phE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(atom.asMatter(), matter, tempreture, ph1, ph2, phE, temE, baseE, stacks).setIonScale(size, size1));
	}
	static void addRecipe(Atoms atom, int size, Matter matter, int size1, int tempreture, EnumOxide oxi1, EnumOxide oxi2,
			double temE, double oxiE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(atom.asMatter(), matter, tempreture, oxi1, oxi2, 0, oxiE, temE, baseE, stacks));
	}
	static void addRecipe(Ions ion, int size, Matter matter, int size1, int tempreture, EnumOxide oxi1, EnumOxide oxi2,
			double temE, double oxiE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(ion.asMatter(), matter, tempreture, oxi1, oxi2, 0, oxiE, temE, baseE, stacks).setIonScale(size, size1));
	}
	static void addRecipe(Matter matter1, int size, Matter matter2, int size1, int tempreture, EnumOxide oxi1, EnumOxide oxi2,
			double temE, double oxiE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(matter1, matter2, tempreture, oxi1, oxi2, 0, oxiE, temE, baseE, stacks).setIonScale(size, size1));
	}
	static void addRecipe(Ions ion, int size, Matter matter, int size1, int tempreture, EnumPH ph1, EnumPH ph2, 
			double temE, double phE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(ion.asMatter(), matter, tempreture, ph1, ph2, phE, temE, baseE, stacks).setIonScale(size, size1));
	}
	static void addRecipe(Matter matter, int size, Matter matter2, int size1, int tempreture, EnumPH ph1, EnumPH ph2, 
			double temE, double phE, double baseE, Stack...stacks)
	{
		MatterReactionRegister.registerReactionHandler(new MatterReactionRecipe(matter, matter2, tempreture, ph1, ph2, phE, temE, baseE, stacks).setIonScale(size, size1));
	}
}