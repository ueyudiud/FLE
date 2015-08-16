package fle.api.gui;

import fle.api.FleValue;

public class GuiError extends GuiCondition
{
	public static GuiError DEFAULT;
	public static GuiError CAN_NOT_OUTPUT;
	public static GuiError CAN_NOT_INPUT;
	public static GuiError RAINING;
	
	public GuiError(String aName)
	{
		super(aName);
	}
	
	public GuiError setTextureName(String textureName)
	{
		this.textureName = textureName;
		return this;
	}
}