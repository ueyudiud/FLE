/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.items;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.container.SolidContainerManager;
import fle.core.FLE;
import fle.loader.IBFS;
import nebula.common.item.ItemSubBehavior;

/**
 * @author ueyudiud
 */
public class ItemSack extends ItemSubBehavior
{
	public ItemSack()
	{
		super(FLE.MODID, "sack");
		this.maxStackSize = 1;
		init();
	}
	
	protected void init()
	{
		addSubItem(0, "empty", "Empty Sack");
		addSubItem(1, "millet", "Millet Sack");
		addSubItem(2, "millet_w", "Wholemeal Millet Sack");
		addSubItem(3, "wheat", "Wheat Sack");
		addSubItem(4, "wheat_w", "Wholemeal Wheat Sack");
	}
	
	@Override
	public void postInitalizedItems()
	{
		super.postInitalizedItems();
		SolidContainerManager.addContainerItem(this, 0, 1, new SolidStack(IBFS.sMillet, 1000));
		SolidContainerManager.addContainerItem(this, 0, 2, new SolidStack(IBFS.sWholemealMillet, 1000));
		SolidContainerManager.addContainerItem(this, 0, 3, new SolidStack(IBFS.sWheat, 1000));
		SolidContainerManager.addContainerItem(this, 0, 4, new SolidStack(IBFS.sWholemealWheat, 1000));
	}
}