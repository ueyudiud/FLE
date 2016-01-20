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
import net.minecraft.util.EnumFacing;
import simElectricity.API.Common.TileEntitySE;
import simElectricity.API.Energy;
import simElectricity.API.EnergyTile.ITransformer;
import simElectricity.API.INetworkEventHandler;

import java.util.List;

public class TileAdjustableTransformer extends TileEntitySE implements ITransformer, INetworkEventHandler {
    public Primary primary = new ITransformer.Primary(this);
    public Secondary secondary = new ITransformer.Secondary(this);

    public EnumFacing primarySide = EnumFacing.NORTH, secondarySide = EnumFacing.SOUTH;
    public float ratio = 10, outputResistance = 1;

    @Override
    public boolean attachToEnergyNet() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        ratio = tagCompound.getFloat("ratio");
        outputResistance = tagCompound.getFloat("outputResistance");
        primarySide = EnumFacing.getFront(tagCompound.getByte("primarySide"));
        secondarySide = EnumFacing.getFront(tagCompound.getByte("secondarySide"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setFloat("ratio", ratio);
        tagCompound.setFloat("outputResistance", outputResistance);
        tagCompound.setByte("primarySide", (byte) primarySide.ordinal());
        tagCompound.setByte("secondarySide", (byte) secondarySide.ordinal());
    }

    @Override
    public double getResistance() {
        return outputResistance;
    }

    @Override
    public double getRatio() {
        return ratio;
    }

    @Override
    public EnumFacing getPrimarySide() {
        return primarySide;
    }

    @Override
    public EnumFacing getSecondarySide() {
        return secondarySide;
    }

    @Override
    public ITransformerWinding getPrimary() {
        return primary;
    }

    @Override
    public ITransformerWinding getSecondary() {
        return secondary;
    }

    @Override
    public void onFieldUpdate(String[] fields, Object[] values) {
        //Handling on server side
        if (!worldObj.isRemote) {
            for (String s : fields) {
                if (s.contains("primarySide") || s.contains("secondarySide")) {
                    Energy.postTileRejoinEvent(this);
                    worldObj.notifyBlockOfStateChange(pos, blockType);
                } else if (s.contains("outputResistance") || s.contains("ratio")) {
                    Energy.postTileChangeEvent(this);
                }
            }

        }
    }

    @Override
    public void addNetworkFields(List fields) {

    }
}
