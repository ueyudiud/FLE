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

package simElectricity.Common.Blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import simElectricity.API.Common.Blocks.BlockStandardGenerator;
import simElectricity.Common.Blocks.TileEntity.TileBatteryBox;

public class BlockBatteryBox extends BlockStandardGenerator {

    public BlockBatteryBox() {
        super();
        setUnlocalizedName("battery_box");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileBatteryBox();
    }


}
