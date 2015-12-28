package fle.core.recipe.matter;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import flapi.chem.MatterDictionary;
import flapi.chem.MeltingRecipe;
import flapi.chem.base.Matter;
import flapi.chem.base.ScaleInput;
import flapi.chem.particle.Atoms;
import flapi.collection.abs.Stack;
import fle.core.init.IB;
import fle.core.recipe.FLECrucibleRecipe;
import fle.core.util.TempChemRequire;

public class MatterMeltingRecipe extends MeltingRecipe
{
	public static void init()
	{
		add(new MatterMeltingRecipe("Melting Copper" , new ScaleInput(new TempChemRequire(917 , Atoms.Cu.meltingPoint), new Stack(Atoms.Cu.asMatter())), 3200, 100, new FluidStack(IB.copper , 1)));
		add(new MatterMeltingRecipe("Melting Lead"   , new ScaleInput(new TempChemRequire(540 , Atoms.Pb.meltingPoint), new Stack(Atoms.Pb.asMatter())), 2700, 130, new FluidStack(IB.lead   , 1)));
		add(new MatterMeltingRecipe("Melting Tin"    , new ScaleInput(new TempChemRequire(497 , Atoms.Sn.meltingPoint), new Stack(Atoms.Sn.asMatter())), 1900, 130, new FluidStack(IB.tin    , 1)));
		add(new MatterMeltingRecipe("Melting Arsenic", new ScaleInput(new TempChemRequire(768 , Atoms.As.meltingPoint), new Stack(Atoms.As.asMatter())), 1900, 130, new FluidStack(IB.arsenic, 1)));
		
		add(new MatterMeltingRecipe("Melting Bronze Sn   ", new ScaleInput(new TempChemRequire(816, 934), new Stack(Atoms.Cu.asMatter(),  4), new Stack(Atoms.Sn.asMatter())), 24000, 400, new FluidStack(IB.cu_sn_1,  5)));
		add(new MatterMeltingRecipe("Melting Bronze Sn II", new ScaleInput(new TempChemRequire(847, 934), new Stack(Atoms.Cu.asMatter(),  9), new Stack(Atoms.Sn.asMatter())), 4800,  140, new FluidStack(IB.cu_sn_0, 10)));
		add(new MatterMeltingRecipe("Melting Bronze Pb   ", new ScaleInput(new TempChemRequire(819, 973), new Stack(Atoms.Cu.asMatter(),  4), new Stack(Atoms.Pb.asMatter())), 24000, 400, new FluidStack(IB.cu_pb_1,  5)));
		add(new MatterMeltingRecipe("Melting Bronze Pb II", new ScaleInput(new TempChemRequire(896, 984), new Stack(Atoms.Cu.asMatter(),  9), new Stack(Atoms.Pb.asMatter())), 3600,  134, new FluidStack(IB.cu_pb_0, 10)));
		add(new MatterMeltingRecipe("Melting Bronze As   ", new ScaleInput(new TempChemRequire(819, 973), new Stack(Atoms.Cu.asMatter(),  4), new Stack(Atoms.Sn.asMatter())), 24000, 400, new FluidStack(IB.cu_sn_1,  5)));
		add(new MatterMeltingRecipe("Melting Bronze As II", new ScaleInput(new TempChemRequire(896, 984), new Stack(Atoms.Cu.asMatter(),  9), new Stack(Atoms.Sn.asMatter())), 3600,  134, new FluidStack(IB.cu_sn_0, 10)));
		add(new MatterMeltingRecipe("Melting Bronze Pb Sn", new ScaleInput(new TempChemRequire(847, 934), new Stack(Atoms.Cu.asMatter(), 8), new Stack(Atoms.Sn.asMatter()), new Stack(Atoms.Sn.asMatter())), 1900, 180, new FluidStack(IB.cu_pb_sn, 10)));

		add(new MatterMeltingRecipe("Deoxidize Enargite"    , new ScaleInput(new TempChemRequire(671, 938 ), new Stack(Matter.mCu3AsS4, 8),     new Stack(Atoms.C.asMatter()))   , 2300, 200, new FluidStack(IB.cu_as_1, 4)));
		add(new MatterMeltingRecipe("Deoxidize Cuprite"     , new ScaleInput(new TempChemRequire(728, 1028), new Stack(Matter.mCu2O, 6),        new Stack(Atoms.C.asMatter()))   , 1740, 200, new FluidStack(IB.copper, 4)));
		add(new MatterMeltingRecipe("Deoxidize Tenorite"    , new ScaleInput(new TempChemRequire(740, 1098), new Stack(Matter.mCuO, 4),         new Stack(Atoms.C.asMatter()))   , 1960, 200, new FluidStack(IB.copper, 2)));
		add(new MatterMeltingRecipe("Deoxidize Covellite"   , new ScaleInput(new TempChemRequire(674, 998 ), new Stack(Matter.mCuS, 4),         new Stack(Atoms.C.asMatter()))   , 1910, 200, new FluidStack(IB.copper, 2)));
		add(new MatterMeltingRecipe("Deoxidize Chalcocite"  , new ScaleInput(new TempChemRequire(648, 956 ), new Stack(Matter.mCu2S, 6),        new Stack(Atoms.C.asMatter()))   , 1970, 200, new FluidStack(IB.copper, 4)));
		add(new MatterMeltingRecipe("Deoxidize Malachite"   , new ScaleInput(new TempChemRequire(569, 729 ), new Stack(Matter.mCu_OH2_CO3, 18), new Stack(Atoms.C.asMatter()))   , 1232, 225, new FluidStack(IB.copper, 2)));
		add(new MatterMeltingRecipe("Deoxidize Azurite"     , new ScaleInput(new TempChemRequire(569, 729 ), new Stack(Matter.mCu_OH2_2CO3, 26),new Stack(Atoms.C.asMatter()))   , 1471, 225, new FluidStack(IB.copper, 2)));
		//Chalcopyrite
		//Bornite
		add(new MatterMeltingRecipe("Deoxidize Orpiment"    , new ScaleInput(new TempChemRequire(618, 719 ), new Stack(Matter.mAs2S3, 10),      new Stack(Atoms.C.asMatter(), 3)),  948, 174, new FluidStack(IB.arsenic, 4)));
		add(new MatterMeltingRecipe("Deoxidize Realgar"     , new ScaleInput(new TempChemRequire(609, 711 ), new Stack(Matter.mAs4S4, 8),       new Stack(Atoms.C.asMatter(), 2)),  892, 174, new FluidStack(IB.arsenic, 4)));
		add(new MatterMeltingRecipe("Deoxidize Arsenolite"  , new ScaleInput(new TempChemRequire(637, 758 ), new Stack(Matter.mAs4S6, 10),      new Stack(Atoms.C.asMatter(), 3)), 1028, 174, new FluidStack(IB.arsenic, 4)));
		add(new MatterMeltingRecipe("Deoxidize Gelenite"    , new ScaleInput(new TempChemRequire(821, 1028), new Stack(Matter.mPbS,   4),       new Stack(Atoms.C.asMatter())),    2228, 235, new FluidStack(IB.lead, 2)));
		add(new MatterMeltingRecipe("Deoxidize Sphalerite"  , new ScaleInput(new TempChemRequire(819, 983 ), new Stack(Matter.mZnS,   4),       new Stack(Atoms.C.asMatter())),    2193, 235, new FluidStack(IB.zinc, 2)));
		add(new MatterMeltingRecipe("Deoxidize Cassiterite" , new ScaleInput(new TempChemRequire(728, 817 ), new Stack(Matter.mSnO2,  3),       new Stack(Atoms.C.asMatter())),    2749, 235, new FluidStack(IB.tin, 1)));		
	}
	
	@SuppressWarnings("unused")
	private static Matter get(String name)
	{
		return Matter.getMatterFromName(name);
	}
	
	public MatterMeltingRecipe(String name, ScaleInput input, int energy,
			float speed, FluidStack output)
	{
		super(name, input, energy, speed, output);
	}
	public MatterMeltingRecipe(String name, ScaleInput input, int energy,
			float speed, Fluid output, int size)
	{
		this(name, input, energy, speed, new FluidStack(output, size));
	}
	public MatterMeltingRecipe(String name, ScaleInput input, int energy,
			float speed, Matter matter, int size)
	{
		this(name, input, energy, speed, MatterDictionary.toFluid(matter), size);
	}

	private static void add(MeltingRecipe recipe)
	{
		MatterDictionary.addRecipe(recipe);
	}
	@SuppressWarnings("unused")
	private static void add(FLECrucibleRecipe recipe)
	{
		FLECrucibleRecipe.addRecipe(recipe);
	}
}