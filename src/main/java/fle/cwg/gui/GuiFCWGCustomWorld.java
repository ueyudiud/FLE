package fle.cwg.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.config.GuiSlider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.cwg.world.FCWGWorldInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiContainer;

@SideOnly(Side.CLIENT)
public class GuiFCWGCustomWorld extends GuiScreen
{
	private static final GuiButton BUTTON = new GuiButton(-1, -1, -1, -1, -1, "");
	
	protected final GuiCreateWorld guiCreateWorld;
	
	public GuiTextField guiTextField;
	
	protected FCWGWorldInfo info;
	
	protected int page;
	
	public GuiFCWGCustomWorld(GuiCreateWorld world)
	{
		this.guiCreateWorld = world;
		this.info = FCWGWorldInfo.createNewInfo(world.field_146334_a);
	}
	
	protected void setOptionToWorld()
	{
		guiCreateWorld.field_146334_a = info.toString();
	}
	
	public void changePage()
	{
		if(page == 0)
		{
			disableButton(1);
			enableButton(10);
			enableButton(11);
			enableButton(12);
			enableButton(13);
			enableButton(14);
			enableButton(15);
			enableButton(16);
			enableButton(17);
			enableButton(18);
			enableButton(19);
		}
		else
		{
			enableButton(1);
			disableButton(10);
			disableButton(11);
			disableButton(12);
			disableButton(13);
			disableButton(14);
			disableButton(15);
			disableButton(16);
			disableButton(17);
			disableButton(18);
			disableButton(19);
		}
		if(page == 1)
		{
			enableButton(20);
			enableButton(21);
			enableButton(22);
			enableButton(23);
			enableButton(24);
			enableButton(25);
			enableButton(26);
			enableButton(27);
			enableButton(28);
			enableButton(29);
		}
		else
		{
			disableButton(20);
			disableButton(21);
			disableButton(22);
			disableButton(23);
			disableButton(24);
			disableButton(25);
			disableButton(26);
			disableButton(27);
			disableButton(28);
			disableButton(29);
		}
		if(page == 2)
		{
			disableButton(2);
			enableButton(30);
			enableButton(31);
		}
		else
		{
			enableButton(2);
			disableButton(30);
			disableButton(31);
		}
	}

	private GuiButton button(int id)
	{
		for(Object buttonRaw : buttonList)
		{
			if(((GuiButton) buttonRaw).id == id)
			{
				return ((GuiButton) buttonRaw);
			}
		}
		return BUTTON;
	}
	private void enableButton(int id)
	{
		GuiButton button = button(id);
		button.enabled = true;
		button.visible = true;
	}
	private void disableButton(int id)
	{
		GuiButton button = button(id);
		button.enabled = false;
		button.visible = false;
	}
	
	@Override
	public void updateScreen()
	{
		guiTextField.updateCursorCounter();
		super.updateScreen();
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        int length = width / 3;
        int left = width / 2 - length - 20;
        int right = width / 2 + 20;
        int top = (height - 120) / 2;
        buttonList.add(new GuiButton(0, width / 2 - 100, top + 140, 200, 20, "save"));
		buttonList.add(new GuiButton(1, 100 - 60, top - 40, 60, 20, "<"));
		buttonList.add(new GuiButton(2, width - 100, top - 40, 60, 20, ">"));
		
		buttonList.add(new GuiSlider(10, left, top, length, 20, "coreEfficiency:", "", 0.1, 4.0, info.coreEfficiency, true, true));
        buttonList.add(new GuiSlider(11, left, top + 20, length, 20, "heightCheckRange:", "", 1, 8, info.heightCheckRange, false, true));
        buttonList.add(new GuiSlider(12, left, top + 40, length, 20, "baseHeight:", "", 0.0, 32.0, info.baseHeight, true, true));
        buttonList.add(new GuiSlider(13, left, top + 60, length, 20, "rangeHeight:", "", 0.0, 16.0, info.rangeHeight, true, true));
        buttonList.add(new GuiSlider(14, left, top + 80, length, 20, "rootHeightEfficiency:", "", 0.0, 20.0, info.rootHeightEfficiency, true, true));
        buttonList.add(new GuiSlider(15, right, top, length, 20, "randHeightEfficiency:", "", 0.0, 3.0, info.randHeightEfficiency, true, true));
        buttonList.add(new GuiSlider(16, right, top + 20, length, 20, "mainNoiseSize:", "", 5, 800, info.mainNoiseSize, true, true));
        buttonList.add(new GuiSlider(17, right, top + 40, length, 20, "rangeNoiseXZSize:", "", 5, 800, info.rangeNoiseXZSize, true, true));
        buttonList.add(new GuiSlider(18, right, top + 60, length, 20, "rangeNoiseYSize:", "", 5, 800, info.rangeNoiseYSize, true, true));
        buttonList.add(new GuiSlider(19, right, top + 80, length, 20, "slopeNoiseXZSize:", "", 0.1, 20, info.slopeNoiseXZSize, true, true));
        buttonList.add(new GuiSlider(20, left, top, length, 20, "slopeNoiseYSize:", "", 0.1, 20, info.slopeNoiseYSize, true, true));
        buttonList.add(new GuiSlider(21, left, top + 20, length, 20, "decorateNoiseSize:", "", 0, 2.0, info.decorateNoiseSize, true, true));
        buttonList.add(new GuiSlider(22, left, top + 40, length, 20, "seaLevel:", "", 0, 255, info.seaLevel, false, true));
        buttonList.add(new GuiSlider(23, left, top + 60, length, 20, "tempNoiseOctave:", "", 0, 10, info.tempNoiseOctave, false, true));
        buttonList.add(new GuiSlider(24, left, top + 80, length, 20, "tempNoiseSize:", "", 0.0, 5.0, info.tempNoiseSize, true, true));
        buttonList.add(new GuiSlider(25, right, top, length, 20, "rainfallNoiseOctave:", "", 0, 10, info.rainfallNoiseOctave, false, true));
        buttonList.add(new GuiSlider(26, right, top + 20, length, 20, "rainfallNoiseSize:", "", 0.0, 5.0, info.rainfallNoiseSize, true, true));
        buttonList.add(new GuiSlider(27, right, top + 40, length, 20, "startLandChance:", "", 1, 50, info.startLandChance, false, true));
        buttonList.add(new GuiSlider(28, right, top + 60, length, 20, "secondLandChance:", "", 1, 50, info.secondLandChance, false, true));
        buttonList.add(new GuiSlider(29, right, top + 80, length, 20, "terrainNoiseSize:", "", 0, 1, info.terrainNoiseSize, true, true));
        buttonList.add(new GuiSlider(30, left, top, length, 20, "terrainNoiseIteration:", "", 1, 8, info.terrainNoiseIteration, false, true));
        buttonList.add(new GuiButton(31, left, top + 20, length, 20, info.enableRiver ? "disableRiver" : "enableRiver"));
        
		guiTextField = new GuiTextField(fontRendererObj, 50, 170, this.width - 100, 20);
		guiTextField.setText(info.toString());
		changePage();
	}
	
	@Override
	protected void mouseClicked(int x, int y, int type)
	{
		guiTextField.mouseClicked(x, y, type);
		super.mouseClicked(x, y, type);
	}
		
	@Override
	protected void keyTyped(char chr, int type)
	{
		if(!guiTextField.textboxKeyTyped(chr, type))
		{
			super.keyTyped(chr, type);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		int id = button.id;
		switch (id)
		{
		case 0:
			setOptionToWorld();
			mc.displayGuiScreen(guiCreateWorld);
			break;
		case 1:
			--page;
			changePage();
			break;
		case 2:
			++page;
			changePage();
			break;
		case 10:
			info.coreEfficiency = (float) ((GuiSlider) button).getValue();
			break;
		case 11:
			info.heightCheckRange = ((GuiSlider) button).getValueInt();
			break;
		case 12:
			info.baseHeight = (float) ((GuiSlider) button).getValue();
			break;
		case 13:
			info.rangeHeight = (float) ((GuiSlider) button).getValue();
			break;
		case 14:
			info.rootHeightEfficiency = (float) ((GuiSlider) button).getValue();
			break;
		case 15:
			info.randHeightEfficiency = (float) ((GuiSlider) button).getValue();
			break;
		case 16:
			info.mainNoiseSize = (float) ((GuiSlider) button).getValue();
			break;
		case 17:
			info.rangeNoiseXZSize = (float) ((GuiSlider) button).getValue();
			break;
		case 18:
			info.rangeNoiseYSize = (float) ((GuiSlider) button).getValue();
			break;
		case 19:
			info.slopeNoiseXZSize = (float) ((GuiSlider) button).getValue();
			break;
		case 20:
			info.slopeNoiseYSize = (float) ((GuiSlider) button).getValue();
			break;
		case 21:
			info.decorateNoiseSize = (float) ((GuiSlider) button).getValue();
			break;
		case 22:
			info.seaLevel = ((GuiSlider) button).getValueInt();
			break;
		case 23:
			info.tempNoiseOctave = ((GuiSlider) button).getValueInt();
			break;
		case 24:
			info.tempNoiseSize = (float) ((GuiSlider) button).getValue();
			break;
		case 25:
			info.rainfallNoiseOctave = ((GuiSlider) button).getValueInt();
			break;
		case 26:
			info.rainfallNoiseSize = (float) ((GuiSlider) button).getValue();
			break;
		case 27:
			info.startLandChance = ((GuiSlider) button).getValueInt();
			break;
		case 28:
			info.secondLandChance = ((GuiSlider) button).getValueInt();
			break;
		case 29:
			info.terrainNoiseSize = (float) ((GuiSlider) button).getValue();
			break;
		case 30:
			info.terrainNoiseIteration = ((GuiSlider) button).getValueInt();
			break;
		case 31:
			info.enableRiver =! info.enableRiver;
			button.displayString = info.enableRiver ? "disableRiver" : "enableRiver";
			break;
		default:
			break;
		}
	}

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        setOptionToWorld();
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int u, int v, float z)
    {
        drawDefaultBackground();
        int k = this.width / 2 - 92 - 16;
        guiTextField.drawTextBox();
        super.drawScreen(u, v, z);
    }
}