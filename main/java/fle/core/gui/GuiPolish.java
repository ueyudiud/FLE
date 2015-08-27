package fle.core.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
import fle.api.gui.GuiIconButton;
import fle.api.gui.GuiIconButton.ButtonSize;
import fle.api.net.FlePackets.CoderGuiUpdate;
import fle.api.recipe.CraftingState;
import fle.core.te.TileEntityPolish;

@SideOnly(Side.CLIENT)
public class GuiPolish extends GuiContainerBase
{
	protected static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/polish.png");
	private EntityPlayer player;
	private TileEntityPolish tile;
	
	public GuiPolish(EntityPlayer player, TileEntityPolish table) 
	{
		super(new ContainerPolish(player.inventory, table));
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
	public void initGui() 
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				buttonList.add(new GuiIconButton(i + j * 3, xoffset + 45 + 17 * i, yoffset + 18 + 17 * j, ButtonSize.Slot));
		buttonList.add(new GuiIconButton(9, xoffset + 103, yoffset + 48, ButtonSize.Standard, new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/button.png"), 48, 0));
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		FLE.fle.getNetworkHandler().sendToServer(new CoderGuiUpdate((byte) 1, guibutton.id));
		if(guibutton.id < 9)
			tile.craftedOnce(player, guibutton.id);
		if(guibutton.id == 9)
			tile.clearMap();
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

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition) 
	{		
		CraftingState[] states = CraftingState.getStates(tile.getStates());
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				drawCondition(45 + i * 17, 18 + j  * 17, states[i + j * 3]);
		drawCondition(20, 54, tile.getCondition());
		if(tile.getRecipeInput() != null)
			itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, tile.getRecipeInput(), aXOffset + 20,  aYOffset + 35);
		mc.renderEngine.bindTexture(getResourceLocation());
	}

	@Override
	public boolean hasCustomName() 
	{
		return false;
	}
}