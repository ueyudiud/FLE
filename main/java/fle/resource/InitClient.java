package fle.resource;

import fle.core.init.Renders;
import fle.resource.block.auto.RenderStoneChip;
import fle.resource.entity.RenderThrownStone;

public class InitClient extends Init
{
	@Override
	public void load()
	{
		super.load();

		Renders.RenderStoneChip = RenderStoneChip.class;
		Renders.RenderThrownStone = new RenderThrownStone();
	}
}