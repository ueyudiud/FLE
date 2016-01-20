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

package simElectricity.Common.Blocks.TileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import simElectricity.API.Common.TileEntitySE;
import simElectricity.API.EnergyTile.IConductor;
import simElectricity.API.INetworkEventHandler;
import simElectricity.API.Network;
import simElectricity.API.Util;
import simElectricity.Common.Blocks.BlockWire;

import java.util.List;

public class TileWire extends TileEntitySE implements IConductor, INetworkEventHandler {
    public boolean[] renderSides = new boolean[6];
    public int color = 0;
    public float resistance = 100;
    public float width = 0.1F;
    public String textureString;
    public boolean needsUpdate = false;
    protected boolean isAddedToEnergyNet = false;
    private int tick = 0;

    public TileWire() {
    }

    public TileWire(int meta) {
        super();
        resistance = BlockWire.resistanceList[meta];
        width = BlockWire.renderingWidthList[meta];
        textureString = BlockWire.subNames[meta];
    }

    public void updateSides() {
        EnumFacing[] dirs = EnumFacing.values();
        for (int i = 0; i < 6; i++) {
            renderSides[i] = Util.possibleConnection(this, dirs[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        AxisAlignedBB bb = INFINITE_EXTENT_AABB;
        bb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        return bb;
    }

    @Override
    public void update() {
        super.update();

        if (!worldObj.isRemote && needsUpdate) {
            tick++;
            if (tick > 2) {
                needsUpdate = false;
                tick = 0;
                Network.updateNetworkFields(this);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        resistance = tagCompound.getFloat("resistance");
        color = tagCompound.getInteger("color");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setFloat("resistance", resistance);
        tagCompound.setInteger("color", color);
    }

    @Override
    public boolean attachToEnergyNet() {
        return true;
    }

    @Override
    public double getResistance() {
        return resistance;
    }

    @Override
    public int getColor() {
        return color;
    }

    public boolean isConnected(EnumFacing direction) {
        return direction.ordinal() < 6 && direction.ordinal() >= 0 && renderSides[direction.ordinal()];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addNetworkFields(List fields) {
        updateSides();
        fields.add("renderSides");
    }

    @Override
    public void onFieldUpdate(String[] fields, Object[] values) {

    }
}
