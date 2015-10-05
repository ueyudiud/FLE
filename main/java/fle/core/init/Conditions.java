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
		new CraftingState('n', "hit_l_u", 0x010000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_u");
		new CraftingState('o', "hit_l_r", 0x020000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_r");
		new CraftingState('q', "hit_l_d", 0x030000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_d");
		new CraftingState('r', "hit_l_l", 0x040000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_l");
		new CraftingState('s', "hit_m_u", 0x050000).setTextureName(FleValue.TEXTURE_FILE + ":hit_m_u");
		new CraftingState('t', "hit_m_r", 0x060000).setTextureName(FleValue.TEXTURE_FILE + ":hit_m_r");
		new CraftingState('u', "hit_m_d", 0x070000).setTextureName(FleValue.TEXTURE_FILE + ":hit_m_d");
		new CraftingState('v', "hit_m_l", 0x080000).setTextureName(FleValue.TEXTURE_FILE + ":hit_m_l");
		new CraftingState('w', "hit_h_u", 0x090000).setTextureName(FleValue.TEXTURE_FILE + ":hit_h_u");
		new CraftingState('x', "hit_h_r", 0x0A0000).setTextureName(FleValue.TEXTURE_FILE + ":hit_h_r");
		new CraftingState('y', "hit_h_d", 0x0B0000).setTextureName(FleValue.TEXTURE_FILE + ":hit_h_d");
		new CraftingState('z', "hit_h_l", 0x0C0000).setTextureName(FleValue.TEXTURE_FILE + ":hit_h_l");
		
		new CraftingState('1', "hit_ur", 0x0D0000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_r", FleValue.TEXTURE_FILE + ":hit_l_u");
		new CraftingState('2', "hit_rd", 0x100000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_d", FleValue.TEXTURE_FILE + ":hit_l_r");
		new CraftingState('3', "hit_dl", 0x110000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_l", FleValue.TEXTURE_FILE + ":hit_l_d");
		new CraftingState('4', "hit_lu", 0x130000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_u", FleValue.TEXTURE_FILE + ":hit_l_l");
		new CraftingState('!', "hit_h_ur", 0x0D0000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_r", FleValue.TEXTURE_FILE + ":hit_m_u");
		new CraftingState('@', "hit_h_ul", 0x0E0000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_l", FleValue.TEXTURE_FILE + ":hit_m_u");
		new CraftingState('#', "hit_h_ru", 0x0F0000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_u", FleValue.TEXTURE_FILE + ":hit_m_r");
		new CraftingState('$', "hit_h_rd", 0x100000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_d", FleValue.TEXTURE_FILE + ":hit_m_r");
		new CraftingState('%', "hit_h_dl", 0x110000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_l", FleValue.TEXTURE_FILE + ":hit_m_d");
		new CraftingState('^', "hit_h_dr", 0x120000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_r", FleValue.TEXTURE_FILE + ":hit_m_d");
		new CraftingState('&', "hit_h_lu", 0x130000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_u", FleValue.TEXTURE_FILE + ":hit_m_l");
		new CraftingState('*', "hit_h_ld", 0x140000).setTextureName(FleValue.TEXTURE_FILE + ":hit_l_d", FleValue.TEXTURE_FILE + ":hit_m_l");
		GuiError.DEFAULT = new GuiError("default").setTextureName(FleValue.TEXTURE_FILE + ":default");
		GuiError.CAN_NOT_OUTPUT = new GuiError("cant_output").setTextureName(FleValue.TEXTURE_FILE + ":output_error");
		GuiError.CAN_NOT_INPUT = new GuiError("cant_input").setTextureName(FleValue.TEXTURE_FILE + ":input_error");
		GuiError.RAINING = new GuiError("raining").setTextureName(FleValue.TEXTURE_FILE + ":rain_error");
		GuiError.ROTATION_STUCK = new GuiError("stuck").setTextureName(FleValue.TEXTURE_FILE + ":stuck");
	}
}