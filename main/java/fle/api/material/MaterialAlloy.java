package fle.api.material;

import fle.api.enums.EnumAtoms;
import fle.api.util.Register;
import fle.api.util.SubTag;
import fle.api.util.WeightHelper;

public class MaterialAlloy extends MaterialAbstract
{
	private static Register<MaterialAlloy> register = new Register();
	
	private AlloyHelper helper;
	
	public MaterialAlloy(String aName, Matter aMatter, PropertyInfo aInfo, AlloyHelper aHelper, SubTag...aTag) 
	{
		super(aName, aMatter, aInfo, aTag);
		helper = aHelper;
		register.register(this, aName);
	}
	
	public boolean matchProportion(WeightHelper<IAtoms> aHelper)
	{
		if(helper == null || aHelper == null) return false;
		return helper.matchProportion(aHelper);
	}

	public static MaterialAbstract findAlloy(WeightHelper<IAtoms> wh)
	{
		for(MaterialAlloy m : register)
		{
			if(m.matchProportion(wh)) return m;
		}
		for(MaterialAbstract m : MaterialAbstract.pureMaterials)
		{
			if(m.matter != null)
				if(wh.getContain(m.matter.getElementAtoms().keySet().iterator().next()) > 0.9375F)
					return m;
		}
		return null;
	}
}