package fle.core.tech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.tech.ITechTag;
import fle.api.tech.PlayerTechInfo;
import fle.api.tech.Technology;

@Deprecated
@SideOnly(Side.CLIENT)
public class GuiTechnology extends GuiScreen
{
	private PlayerTechInfo info;
	private List<Technology> techList = new ArrayList();
	private ITechTag activeTag;
	private double screenXOffset;
	private double screenYOffset;
	
	public GuiTechnology(EntityPlayer aPlayer)
	{
		info = new FlePlayerTechInfo(aPlayer);
		techList = info.getPlayerTechList();
		if(techList.isEmpty())
		{
			activeTag = null;
		}
		else
		{
			activeTag = techList.get(0).getTechClassBelong();
		}
		screenXOffset = 0D;
		screenYOffset = 0D;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	}
}