/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.items;

import farcore.FarCore;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;

public class ItemStoneFragment extends ItemMulti
{
	public ItemStoneFragment()
	{
		super(FarCore.ID, MC.fragment);
		this.enableChemicalFormula = false;
	}
}
