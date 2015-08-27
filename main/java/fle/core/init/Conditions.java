package fle.core.init;

import fle.api.FleValue;
import fle.api.gui.GuiError;
import fle.api.recipe.CraftingState;

public class Conditions
{
	public static void init()
	{
		CraftingState.DEFAULT = new CraftingState(' ', "default_crafting", 0xFFFFFF).setTextureName(FleValue.TEXTURE_FILE + ":default");
		CraftingState.CRUSH = new CraftingState('c', "crush_crafting", 0x000088).setTextureName(FleValue.TEXTURE_FILE + ":crush_state");
		CraftingState.POLISH = new CraftingState('p', "polish_crafting", 0x0000FF).setTextureName(FleValue.TEXTURE_FILE + ":polish_state");
		GuiError.DEFAULT = new GuiError("default").setTextureName(FleValue.TEXTURE_FILE + ":default");
		GuiError.CAN_NOT_OUTPUT = new GuiError("cant_output").setTextureName(FleValue.TEXTURE_FILE + ":output_error");
		GuiError.CAN_NOT_INPUT = new GuiError("cant_input").setTextureName(FleValue.TEXTURE_FILE + ":input_error");
		GuiError.RAINING = new GuiError("raining").setTextureName(FleValue.TEXTURE_FILE + ":rain_error");
	}
}