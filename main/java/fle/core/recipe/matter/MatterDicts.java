package fle.core.recipe.matter;

import flapi.chem.MatterDictionary;
import flapi.chem.particle.Atoms;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.init.Parts;
import fle.core.item.ItemFleSub;
import fle.resource.OreCobbleMatterRecipe;

public class MatterDicts
{
	public static void init()
	{
		MatterDictionary.registerMatter(new OreCobbleMatterRecipe());
		for(Atoms atom : Atoms.values())
		{
		MatterDictionary.registerMatter(ItemFleSub.a("ingot_" + atom.name().toLowerCase()), atom.asMatter(), Parts.ingot);
		}
		MatterDictionary.registerMatter(ItemFleSub.a("ingot_cu_as_0"),  Materials.CuAs.getMatter(), Parts.ingot);
		MatterDictionary.registerMatter(ItemFleSub.a("ingot_cu_as_1"),  Materials.CuAs2.getMatter(), Parts.ingot);
		MatterDictionary.registerMatter(ItemFleSub.a("ingot_cu_pb_0"),  Materials.CuPb.getMatter(), Parts.ingot);
		MatterDictionary.registerMatter(ItemFleSub.a("ingot_cu_pb_1"),  Materials.CuPb2.getMatter(), Parts.ingot);
		MatterDictionary.registerMatter(ItemFleSub.a("ingot_cu_sn_0"),  Materials.CuSn.getMatter(), Parts.ingot);
		MatterDictionary.registerMatter(ItemFleSub.a("ingot_cu_sn_1"),  Materials.CuSn2.getMatter(), Parts.ingot);
		MatterDictionary.registerMatter(ItemFleSub.a("ingot_cu_pb_sn"), Materials.CuSnPb.getMatter(), Parts.ingot);
		MatterDictionary.registerMatter(ItemFleSub.a("charred_log"),    Atoms.C.asMatter(), Parts.cube);

		MatterDictionary.registerMatter(IB.arsenic,  Atoms.As.asMatter());
		
		MatterDictionary.registerMatter(IB.copper,   Atoms.Cu.asMatter());
		MatterDictionary.registerMatter(IB.lead,     Atoms.Pb.asMatter());
		MatterDictionary.registerMatter(IB.zinc,     Atoms.Zn.asMatter());
		MatterDictionary.registerMatter(IB.tin,      Atoms.Sn.asMatter());
		MatterDictionary.registerMatter(IB.cu_as_0,  Materials.CuAs.getMatter());
		MatterDictionary.registerMatter(IB.cu_as_1,  Materials.CuAs2.getMatter());
		MatterDictionary.registerMatter(IB.cu_pb_0,  Materials.CuPb.getMatter());
		MatterDictionary.registerMatter(IB.cu_pb_1,  Materials.CuPb2.getMatter());
		MatterDictionary.registerMatter(IB.cu_sn_0,  Materials.CuSn.getMatter());
		MatterDictionary.registerMatter(IB.cu_sn_1,  Materials.CuSn2.getMatter());
		MatterDictionary.registerMatter(IB.cu_pb_sn, Materials.CuSnPb.getMatter());
	}
}