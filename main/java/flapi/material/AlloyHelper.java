package flapi.material;

import farcore.collection.abs.IStackList;
import farcore.collection.abs.Stack;
import flapi.chem.base.IMolecular;

public interface AlloyHelper
{
	public boolean matchProportion(IStackList<Stack<IMolecular>, IMolecular> aHelper);
}