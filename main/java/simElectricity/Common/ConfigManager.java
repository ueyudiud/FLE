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

package simElectricity.Common;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import simElectricity.API.Util;

public class ConfigManager {

    public static Configuration config;

    /**
     * Enable Optimized Nodes
     */
    public static boolean showEnergyNetInfo;
    @SideOnly(Side.CLIENT)
    public static int parabolaRenderSteps;
    public static String matrixSolver;

    public static void init(FMLPreInitializationEvent event) {
        if (config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
            syncConfig();
        }
    }

    private static void syncConfig() {
        showEnergyNetInfo = config.get(Configuration.CATEGORY_GENERAL, "ShowEnergyNetInfo", false, "Display energy net information, such as joining/leaving/changing").getBoolean();
        parabolaRenderSteps = config.get(Configuration.CATEGORY_GENERAL, "ParabolaRenderSteps", 12, "Decides how smooth the parabola cable is(must be a even number!Client ONLY!)").getInt(12);
        // matrixSolver = config.get(Configuration.CATEGORY_GENERAL, "MatrixSolver", "QR", "Which algorithms is used for solving matrix(QR is much more effective than Gaussian, options: QR, Gaussian)").getString();
        matrixSolver = config.getString("MatrixSolver", Configuration.CATEGORY_GENERAL, "QR", "Which algorithms is used for solving matrix(QR is much more effective than Gaussian.).Options: QR, Gaussian", new String[]{"QR", "Gaussian"});

        if (config.hasChanged())
            config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Util.MODID))
            syncConfig();
    }
}
