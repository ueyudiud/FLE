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

package simElectricity.API.EnergyTile;

import java.util.List;

/**
 * This interface can represent a manual junction, should be implemented by a tileEntity
 * (A little bit hard to explain clearly in few words, so see
 * <a href="https://github.com/RoyalAliceAcademyOfSciences/SimElectricity/wiki">SimElectricity wiki</a>
 * for more information)
 */
public interface IManualJunction extends IBaseComponent {
    /**
     * Return the neighbors of this junction
     * Do not have to be geographically next to this tileEntity!
     */
    void addNeighbors(List<IBaseComponent> list);


    /**
     * A advanced version of {@link simElectricity.API.EnergyTile.IBaseComponent#getResistance() getResistance()} in {@link simElectricity.API.EnergyTile.IBaseComponent}
     * <p/>
     * Return 0 in {@link simElectricity.API.EnergyTile.IBaseComponent#getResistance() getResistance()} in {@link simElectricity.API.EnergyTile.IBaseComponent} to make this function valid
     * <p/>
     * Should return the resistance between this tileEntity and the neighbor
     */
    double getResistance(IBaseComponent neighbor);
}