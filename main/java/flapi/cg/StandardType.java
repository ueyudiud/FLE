package flapi.cg;

import java.awt.Rectangle;
import java.util.List;

public abstract class StandardType extends IGuideType
{
	public abstract String getGuideName();

	@Override
	public GuideTransInfo createTransInfo(IGuidePage page, int slotX, int slotY)
	{
		return getRecipeRect() == null ? null : getRecipeRect().contains(slotX, slotY) ? new GuideTransInfo(getGuideName(), new Object[0]) : null;
	}
	
	public Rectangle getRecipeRect()
	{
		return null;
	}
	
	@Override
	public List<IGuidePage> getPage(GuideTransInfo infomation)
	{
		return getGuideName().equals(infomation.type) ? getAllPage() : null;
	}
}