package flapi.material;

import flapi.chem.base.IMolecular;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;

public interface AlloyHelper
{
	public boolean matchProportion(IStackList<Stack<IMolecular>, IMolecular> aHelper);
}