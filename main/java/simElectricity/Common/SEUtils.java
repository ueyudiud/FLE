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

import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import simElectricity.API.Util;

/**
 * Created by <Meow J> on 8/6/2014.
 *
 * @author Meow J
 */
public class SEUtils {
    public static void logInfo(Object object) {
        FMLLog.log(Util.NAME, Level.INFO, "[SimElectricity] " + String.valueOf(object));
    }

    public static void logWarn(Object object) {
        FMLLog.log(Util.NAME, Level.WARN, "[SimElectricity] " + String.valueOf(object));
    }

    public static void logError(Object object) {
        FMLLog.log(Util.NAME, Level.ERROR, "[SimElectricity] " + String.valueOf(object));
    }

    public static void logFatal(Object object) {
        FMLLog.log(Util.NAME, Level.FATAL, "[SimElectricity] " + String.valueOf(object));
    }
}