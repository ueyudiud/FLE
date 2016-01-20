/*
 * Copyright (C) 2014 SimElectricity
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package simElectricity.Client.Gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import simElectricity.Common.Blocks.Container.ContainerSimpleGenerator;
import simElectricity.Common.Blocks.TileEntity.TileSimpleGenerator;

@SideOnly(Side.CLIENT)
public class GuiSimpleGenerator extends GuiContainer {
    protected TileSimpleGenerator tileentity;

    public GuiSimpleGenerator(InventoryPlayer inventoryPlayer, TileSimpleGenerator tileEntity) {
        super(new ContainerSimpleGenerator(inventoryPlayer, tileEntity));
        tileentity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        //draw text and stuff here
        //the parameters for drawString are: string, x, y, color

        fontRendererObj.drawString(StatCollector.translateToLocal("tile.sime:SimpleGenerator.name"), 8, 6, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal("sime.Output"), 115, 22, 4210752);
        fontRendererObj.drawString(String.valueOf(tileentity.getOutputVoltage()) + "V", 115, 32, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal("sime.Impedance"), 115, 42, 4210752);
        fontRendererObj.drawString(String.valueOf(tileentity.getResistance()) + "\u03a9", 115, 52, 4210752);

        //draws "Inventory" or your regional equivalent
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96, 4210752);
        fontRendererObj.drawString(String.valueOf(tileentity.progress) + "%", xSize - 90, ySize - 136, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float opacity, int par2, int par3) {
        //draw your Gui here, only thing you need to change is the path
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(new ResourceLocation("simElectricity:textures/gui/GUI_SimpleGenerator.png"));
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);


        this.drawTexturedModalRect(x + 83, y + 44, 176, 4, 24, 5);
        if (tileentity.progress > 0)
            this.drawTexturedModalRect(x + 84, y + 45, 176, 0, 21 * tileentity.progress / 100, 4);
    }
}