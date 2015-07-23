package fla.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.recipe.ItemState;
import fla.api.util.FlaValue;
import fla.core.Fla;
import fla.core.gui.base.GuiBase;
import fla.core.gui.base.GuiIconButton;
import fla.core.gui.base.GuiIconButton.ButtonSize;
import fla.core.tileentity.TileEntityPolishTable;

@SideOnly(Side.CLIENT)
public class GuiPolishTable extends GuiBase
{
	protected static final ResourceLocation locate = new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/polish.png");
	private EntityPlayer player;
	private TileEntityPolishTable tile;
	
	public GuiPolishTable(EntityPlayer player, TileEntityPolishTable table) 
	{
		super(new ContainerPolishTable(player.inventory, table));
		tile = table;
		this.player = player;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) 
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		//drawAreaTooltip(par1, par2, "Show the state of item.", xoffset + 45, yoffset + 18, 17 * 3, 17 * 3);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) 
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		
		ItemState[] states = ItemState.StateManager.getStates(tile.getStates());
		drawStates(45, 18, states, 3, 3);
		int select = tile.getSelectID();
		int i = select % 3;
		int j = (select - i) / 3;
		drawState(45 + 17 * i, 18 + 17 * j, ItemState.Select);
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 25, yoffset + 17, ButtonSize.Standard, new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/button.png"), 16, 0));
		buttonList.add(new GuiIconButton(1, xoffset + 25, yoffset + 51, ButtonSize.Standard, new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/button.png"), 32, 0));
		buttonList.add(new GuiIconButton(2, xoffset + 103, yoffset + 48, ButtonSize.Standard, new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/button.png"), 48, 0));
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		Fla.fla.nwm.get().initiateGuiButtonPress(this, container.player.player, tile.xCoord, tile.yCoord, tile.zCoord, guibutton.id);
		switch(guibutton.id)
		{
		case (short) 0 : ((TileEntityPolishTable) tile).craftedOnce(player);
		break;
		case (short) 1 : ((TileEntityPolishTable) tile).changeSelect();
		break;
		case (short) 2 : ((TileEntityPolishTable) tile).clearMap();
		break;
		}
		super.actionPerformed(guibutton);
	}

	@Override
	public String getName() 
	{
		return tile.getInventoryName();
	}

	@Override
	public ResourceLocation getResourceLocation() 
	{
		return locate;
	}
}