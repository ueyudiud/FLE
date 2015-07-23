package fla.core.tech;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import fla.api.tech.IPageGui;
import fla.api.util.FlaValue;

public class FlaPageText extends FlaPage
{
	private IIcon icon;
	private ItemStack stack;
	private String str;

	public FlaPageText() 
	{
		setTextureName(FlaValue.TEXT_FILE_NAME, "gui/tech/base.png");
	}
	public FlaPageText(IIcon aIcon) 
	{
		setTextureName(FlaValue.TEXT_FILE_NAME, "gui/tech/base.png");
		icon = aIcon;
	}
	public FlaPageText(Block block) 
	{
		this(new ItemStack(block));
	}
	public FlaPageText(Item item) 
	{
		this(new ItemStack(item));
	}
	public FlaPageText(ItemStack aStack) 
	{
		setTextureName(FlaValue.TEXT_FILE_NAME, "gui/tech/base.png");
		stack = aStack;
	}
	
	public FlaPageText setText(String aString)
	{
		str = aString;
		return this;
	}
	
	@Override
	public void drawOther(IPageGui gui) 
	{
		if(stack != null)
		{
			drawItemStack(gui, 80, 23, stack);
		}
		else if(icon != null)
		{
			drawIcon(gui, 80, 23, icon);
		}
		gui.bindTexture(getTextureLocation());
		gui.drawString(19, 46, 0xFFFFFF, str);
	}
}