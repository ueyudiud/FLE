package fle.api.material;

import fle.api.enums.EnumAtoms;
import fle.api.util.Register;
import fle.api.util.SubTag;
import fle.api.util.WeightHelper;

public class MaterialAlloy extends MaterialAbstract
{
	private static Register<MaterialAlloy> register = new Register();
	
	public MaterialAlloy getAlloyFromAtomsProportion(EnumAtoms...aAtoms)
	{
		if(aAtoms == null) return null;
		WeightHelper<EnumAtoms> tHelper = new WeightHelper<EnumAtoms>(aAtoms);
		for(MaterialAlloy tMaterial : register)
		{
			if(tMaterial.matchProportion(tHelper)) return tMaterial;
		}
		return null;
	}
	
	private AlloyHelper helper;
	
	public MaterialAlloy(Matter aMatter, PropertyInfo aInfo, AlloyHelper aHelper, SubTag...aTag) 
	{
		super(aMatter, aInfo, aTag);
		helper = aHelper;
	}
	
	public boolean matchProportion(WeightHelper<EnumAtoms> aHelper)
	{
		if(helper == null || aHelper == null) return false;
		return helper.matchProportion(aHelper);
	}
}