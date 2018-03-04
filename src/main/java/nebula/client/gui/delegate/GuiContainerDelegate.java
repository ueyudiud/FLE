/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.gui.delegate;

import java.io.IOException;

import nebula.client.gui.GuiContainerTileInventory;
import nebula.common.block.delegated.ITileDelegateTE;
import nebula.common.gui.ContainerTileInventory;
import nebula.common.tile.delegate.TEDelegate;
import nebula.common.util.L;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public class GuiContainerDelegate<T extends TEDelegate & IInventory> extends GuiContainerTileInventory<T>
{
	protected ITileDelegateTE.ITDTE_HasGui<?, T, ?, ?> delegate;
	
	public GuiContainerDelegate(T tile, EntityPlayer player, ITileDelegateTE.ITDTE_HasGui<?, T, ?, ?> delegate, int id, ResourceLocation location)
	{
		super((ContainerTileInventory) delegate.openContainer(tile, player, id), location);
		this.delegate = delegate;
	}
	
	public GuiContainerDelegate(T tile, EntityPlayer player, ITileDelegateTE.ITDTE_HasGui<?, T, ?, ?> delegate, int id, ResourceLocation location, int width, int height)
	{
		super((ContainerTileInventory) delegate.openContainer(tile, player, id), location, width, height);
		this.delegate = delegate;
	}
	
	@Override
	protected void drawOther(int mouseX, int mouseY)
	{
		this.delegate.drawBackgroundLayer(this.tile, L.castAny(this), mouseX, mouseY, this.guiLeft, this.guiTop);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.delegate.drawForegroundLayer(this.tile, L.castAny(this), mouseX, mouseY, this.guiLeft, this.guiTop);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		this.delegate.onGuiButtonClicked(this.tile, L.castAny(this), button.id);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		this.delegate.addGuiButton(this.tile, L.castAny(this), this.guiLeft, this.guiTop);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		this.delegate.onKeyTyped(this.tile, L.castAny(this), typedChar, keyCode);
	}
	
	@Override
	public void sendGuiData(int type, long code, boolean processOnClient)
	{
		super.sendGuiData(type, code, processOnClient);
	}
	
	@Override
	public void drawProgressScaleDTU(int x, int y, int u, int v, int w, int h, int p, int mp)
	{
		super.drawProgressScaleDTU(x, y, u, v, w, h, p, mp);
	}
	
	@Override
	public void drawProgressScaleLTR(int x, int y, int u, int v, int w, int h, int p, int mp)
	{
		super.drawProgressScaleLTR(x, y, u, v, w, h, p, mp);
	}
	
	@Override
	public void drawProgressScaleRTL(int x, int y, int u, int v, int w, int h, int p, int mp)
	{
		super.drawProgressScaleRTL(x, y, u, v, w, h, p, mp);
	}
	
	@Override
	public void drawProgressScaleUTD(int x, int y, int u, int v, int w, int h, int p, int mp)
	{
		super.drawProgressScaleUTD(x, y, u, v, w, h, p, mp);
	}
	
	@Override
	public void drawAreaTooltip(int mouseX, int mouseY, String tooltip, int x, int y, int u, int v)
	{
		super.drawAreaTooltip(mouseX, mouseY, tooltip, x, y, u, v);
	}
}
