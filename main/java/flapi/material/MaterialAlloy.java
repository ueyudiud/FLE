package flapi.material;

import flapi.chem.base.IMolecular;
import flapi.chem.base.Matter;
import flapi.collection.Register;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.util.SubTag;

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
	
	public boolean matchProportion(IStackList<Stack<IMolecular>, IMolecular> aHelper)
	{
		if(helper == null || aHelper == null) return false;
		return helper.matchProportion(aHelper);
	}

	public static MaterialAbstract findAlloy(IStackList<Stack<IMolecular>, IMolecular> wh)
	{
		for(MaterialAlloy m : register)
		{
			if(m.matchProportion(wh)) return m;
		}
		for(MaterialAbstract m : MaterialAbstract.pureMaterials)
		{
			if(m.matter != null)
				if(wh.scale(m.matter.getElementAtoms().keySet().iterator().next()) > 0.925F)
					return m;
		}
		return null;
	}
}