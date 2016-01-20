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

package simElectricity.Common.Blocks.WindMill;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import simElectricity.API.Common.TileSidedFacingMachine;

import java.util.Random;

public class TileWindMillTop extends TileSidedFacingMachine {
    public int randAngle = (new Random()).nextInt(180);
    public boolean settled, initialized;

    @Override
    public void update() {
        super.update();
        if (!worldObj.isRemote && !initialized) {
            this.initialized = true;
        }
    }

    @Override
    public boolean canSetFacing(EnumFacing newFacing) {
        return newFacing != EnumFacing.UP && newFacing != EnumFacing.DOWN;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        settled = tagCompound.getBoolean("settled");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("settled", settled);
    }

    @Override
    public int getInventorySize() {
        return 0;
    }

    @Override
    public boolean attachToEnergyNet() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public double getMaxRenderDistanceSquared() {
        return 100000;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

	@Override
	public String getName()
	{
		return "Wind Mill Top";
	}
}