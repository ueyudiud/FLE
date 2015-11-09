package fle.api.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import fle.api.cg.IGuideType.IGuidePage.Type;
import fle.api.recipe.ItemAbstractStack;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;

/**
 * The type of guide. <br>
 * Show difference type from instance of this.
 * @author ueyudiud
 *
 */
public abstract class IGuideType
{
	/**
	 * Get localized name of type.
	 * @return
	 */
	public abstract String getTypeName();
	
	public void onGuideReload(){}
	
	final List<IGuidePage> a(GuideTransInfo info)
	{
		if(info.typeEqul(Type.ITEM.name()))
		{
			return getPage((ItemAbstractStack) info.contain[0]);
		}
		if(info.typeEqul(Type.FLUID.name()))
		{
			return getPage(((FluidStack) info.contain[0]).getFluid());
		}
		if(info.typeEqul(Type.SOLID.name()))
		{
			return getPage(((SolidStack) info.contain[0]).getObj());
		}
		return getPage(info);
	}

	final GuideTransInfo b(IGuidePage page, int slotX, int slotY)
	{
		for(Type type : Type.values())
		{
			for(int i = 0; i < page.getSize(type); ++i)
			{
				if(page.getRectangle(type, i).contains(slotX, slotY))
				{
					return createTransInfo(page, type, i);
				}
			}
		}
		return createTransInfo(page, slotX, slotY);
	}

	/**
	 * Get all pages this type correct.
	 * @return List of pages.
	 */
	public abstract List<IGuidePage> getAllPage();
	public abstract List<IGuidePage> getPage(GuideTransInfo infomation);

	protected List<IGuidePage> getPage(ItemAbstractStack contain)
	{
		return new ArrayList();
	}
	
	protected List<IGuidePage> getPage(Fluid fluid)
	{
		return new ArrayList();
	}

	protected List<IGuidePage> getPage(Solid solid)
	{
		return new ArrayList();
	}
	
	protected GuideTransInfo createTransInfo(IGuidePage page, Type aType, int index)
	{
		return new GuideTransInfo(aType.name(), page.getObject(aType, index));
	}
	
	public abstract GuideTransInfo createTransInfo(IGuidePage page, int slotX, int slotY);
	
	public static abstract class IGuidePage
	{
		public abstract ResourceLocation getLocation();
		
		public abstract int getSize(Type aType);
		public abstract Object getObject(Type aType, int index);
		public abstract Object getObjectForDisplay(Type aType, int index);
		public abstract Rectangle getRectangle(Type aType, int index);
		public abstract String getStackTip(Type aType, int index);
		public abstract List<String> getToolTip(Type aType, int index);

		public abstract void onUpdate(GuiBookBase guiBookBase);

		public abstract void drawBackground(GuiBookBase gui, int xOffset, int yOffset);
		public abstract void drawOther(GuiBookBase gui, int xOffset, int yOffset);
		
		protected Rectangle slotRect(int x, int y)
		{
			return new Rectangle(x, y, 16, 16);
		}
		
		public enum Type
		{
			ITEM,
			FLUID,
			SOLID;
		}
	}
	
	public static class GuideTransInfo
	{
		final String type;
		Object[] contain;

		public GuideTransInfo(String type)
		{
			this(type, new Object[0]);
		}
		public GuideTransInfo(String aType, Object...objects)
		{
			type = aType;
			contain = objects;
		}
		
		public boolean typeEqul(String aTarget)
		{
			return type.equals(aTarget);
		}
		
		@Override
		public int hashCode()
		{
			int i = 31 + type.hashCode();
			for(Object obj : contain) i = i * 31 + obj.hashCode();
			return i;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof GuideTransInfo ? ((GuideTransInfo) obj).type == type && ((GuideTransInfo) obj).contain.equals(contain) : false;
		}
	}
}