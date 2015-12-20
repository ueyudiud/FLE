package fle.core.recipe.matter;

import static fle.core.recipe.matter.ReactionRecipe1.addRecipe;
import flapi.collection.abs.Stack;
import flapi.enums.EnumAtoms;
import flapi.enums.EnumIons;
import flapi.material.IChemCondition.EnumOxide;
import flapi.material.Matter;

public class ReactionRecipe2
{
	public static void addRecipes()
	{
		addFeGroupRecipe();
		addCuGroupRecipe();
		addGeGroupRecipe();
	}
	
	//Ge Sn Pb
	public static void addGeGroupRecipe()
	{
		//SnO2 + C = Sn + CO2
		addRecipe(EnumAtoms.C, Matter.mSnO2, 792, EnumOxide.Lowest, EnumOxide.Br2, 0.000174, 0.002481, 0.00947591, 
				new Stack(EnumAtoms.Sn.asMatter()), new Stack(Matter.mCO2));
		
		//PbS + O2 = PbO + SO
		addRecipe(Matter.mPbS, Matter.mO2, 892, EnumOxide.C, EnumOxide.O2, 0.0003141, -0.05175, 0.01247591, 
				new Stack(Matter.mPbO), new Stack(Matter.mSO));
		//PbO + C = Pb + CO
		addRecipe(EnumAtoms.C, Matter.mPbO, 592, EnumOxide.Lowest, EnumOxide.Br2, 0.0001291, 0.005175, 0.01247591, 
				new Stack(EnumAtoms.Pb.asMatter()), new Stack(Matter.mCO));
		//PbO + CO = Pb + CO2
		addRecipe(Matter.mCO, Matter.mPbO, 592, EnumOxide.Lowest, EnumOxide.Br2, 0.0001141, 0.004175, 0.01247591, 
				new Stack(EnumAtoms.Pb.asMatter()), new Stack(Matter.mCO2));
	}
	
	public static void addFeGroupRecipe()
	{
		//Fe2O3 + C = 2FeO + CO
		addRecipe(EnumAtoms.C, Matter.mFe2O3, 1048, EnumOxide.Lowest, EnumOxide.Br2, 0.0001341, 0.001175, 0.00126581, 
				new Stack(Matter.mFeO, 2), new Stack(Matter.mCO));
		//Fe3O4 + C = 3FeO + CO
		addRecipe(EnumAtoms.C, Matter.mFe3O4, 1019, EnumOxide.Lowest, EnumOxide.Br2, 0.0001541, 0.001175, 0.00129481, 
				new Stack(Matter.mFeO, 3), new Stack(Matter.mCO));
		//FeO + O2 = FeO+ + O- + O
		addRecipe(Matter.mFeO, Matter.mO2, 719, EnumOxide.Default, EnumOxide.Highest, 0.0000741, 0.00175, 0.00099481, 
				new Stack(EnumIons.Ferricoxide.asMatter()), new Stack(EnumAtoms.O.asMatter()), new Stack(EnumIons.O_1.asMatter()));
		//FeO + O- = FeO+ + O2-
		addRecipe(Matter.mFeO, EnumIons.O_1.asMatter(), 719, EnumOxide.Default, EnumOxide.Highest, 0.0000741, 0.00175, 0.010481, 
				new Stack(EnumIons.Ferricoxide.asMatter()), new Stack(EnumIons.O_2.asMatter()));
		//2FeO+ + O2- = Fe2O3
		addRecipe(Matter.mFeO, 2, EnumIons.O_1.asMatter(), 1, 719, EnumOxide.Default, EnumOxide.Highest, 0.0001279, 0.00175, 0.007481, 
				new Stack(Matter.mFe2O3));
		//FeO + CO = Fe + CO2
		addRecipe(Matter.mFeO, Matter.mCO, 1017, EnumOxide.Mg, EnumOxide.C, 0.0002279, 0.00391, 0.0001481, 
				new Stack(EnumAtoms.Fe), new Stack(Matter.mCO2));
		//FeO + C = Fe + CO
		addRecipe(EnumAtoms.C, Matter.mFeO, 1047, EnumOxide.Mg, EnumOxide.C, 0.0002038, 0.00391, 0.0001385, 
				new Stack(EnumAtoms.Fe), new Stack(Matter.mCO));
		//FeS + O2 = FeO + SO
		addRecipe(Matter.mFeS, Matter.mO2, 719, EnumOxide.Highest, EnumOxide.Default, 0.00348, -0.00791, 0.01385, 
				new Stack(Matter.mFeO), new Stack(Matter.mSO));
	}

	//Cu Ag Au
	public static void addCuGroupRecipe()
	{
		//CuO + C = Cu + CO
		addRecipe(EnumAtoms.C, Matter.mCuO, 771, EnumOxide.S, EnumOxide.Mg, 0.0000018, 0.00181, 0.010491, 
				new Stack(EnumAtoms.Cu.asMatter()), new Stack(Matter.mCO));
		//CuO + CO = Cu + CO2
		addRecipe(Matter.mCO, Matter.mCuO, 771, EnumOxide.S, EnumOxide.Mg, 0.0000024, 0.00172, 0.010491, 
				new Stack(EnumAtoms.Cu.asMatter()), new Stack(Matter.mCO2));
		//Cu2O + O = 2CuO
		addRecipe(EnumAtoms.O, Matter.mCu2O, 791, EnumOxide.Highest, EnumOxide.C, 0.000196, 0.0022, 0.000987, 
				new Stack(Matter.mCuO, 2));
		//Cu2O + C = 2Cu + CO
		addRecipe(EnumAtoms.C, Matter.mCu2O, 771, EnumOxide.S, EnumOxide.Mg, 0.0000024, 0.00185, 0.011841, 
				new Stack(EnumAtoms.Cu.asMatter(), 2), new Stack(Matter.mCO));
		//Cu2O + CO = 2Cu + CO2
		addRecipe(Matter.mCO, Matter.mCu2O, 771, EnumOxide.S, EnumOxide.Mg, 0.0000028, 0.00187, 0.011849, 
				new Stack(EnumAtoms.Cu.asMatter(), 2), new Stack(Matter.mCO2));

		//CuS + O2 = CuO + SO
		addRecipe(Matter.mCuS, Matter.mO2, 671, EnumOxide.O2, EnumOxide.C, 0.0001928, -0.000918, 0.009418, 
				new Stack(Matter.mCuO), new Stack(Matter.mSO));
		//Cu2S + O2 = Cu2O + SO
		addRecipe(Matter.mCu2S, Matter.mO2, 671, EnumOxide.O2, EnumOxide.C, 0.0001928, -0.000918, 0.009418, 
				new Stack(Matter.mCu2O), new Stack(Matter.mSO));
		//CuS + O = CuO + S
		addRecipe(EnumAtoms.O, Matter.mCuS, 641, EnumOxide.O2, EnumOxide.Default, 0.0001349, 0.001018, 0.010438, 
				new Stack(Matter.mCuO), new Stack(EnumAtoms.S.asMatter()));
		//Cu2S + O = Cu2O + S
		addRecipe(EnumAtoms.O, Matter.mCu2S, 641, EnumOxide.O2, EnumOxide.Default, 0.0001349, 0.001018, 0.010438, 
				new Stack(Matter.mCu2O), new Stack(EnumAtoms.S.asMatter()));
		//CuCO3 = CuO + CO2
		addRecipe(Matter.mCuCO3, 781, EnumOxide.Mg, 0.0001849, 0, 0.002338,
				new Stack(Matter.mCuO), new Stack(Matter.mCO2));
		//Cu(OH)2 = CuO + H2O
		addRecipe(Matter.mCu_OH2, 781, EnumOxide.Mg, 0.0001851, 0, 0.002338, 
				new Stack(Matter.mCuO), new Stack(Matter.mH2O));
		//Cu2(OH)2CO3 = Cu(OH)2 + CuCO3
		addRecipe(Matter.mCu_OH2_CO3, false, 500, 0.05F, 0.01F,
				new Matter[]{Matter.mCu_OH2, Matter.mCuCO3});
		//Cu3(OH)2(CO3)2 = Cu(OH)2 + 2CuCO3
		addRecipe(Matter.mCu_OH2_2CO3, false, 500, 0.05F, 0.01F,
				new Matter[]{Matter.mCu_OH2, Matter.mCuCO3, Matter.mCuCO3});
		//CuFeS2 = CuS + FeS
		addRecipe(Matter.mCuFeS2, false, 500, 0.05F, 0.01F,
				new Matter[]{Matter.mCuS, Matter.mFeS});
	}
}