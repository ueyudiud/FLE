package farcore.lib.item.instance;

import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;

public class ItemStoneFragment extends ItemMulti
{
	public ItemStoneFragment()
	{
		super(FarCore.ID, MC.fragment);
		enableChemicalFormula = false;
		EnumItem.stone_fragment.set(this);
	}
}