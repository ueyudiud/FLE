package farcore;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.energy.electric.ElectricNet;
import farcore.energy.kinetic.KineticNet;
import farcore.energy.thermal.ThermalNet;
import farcore.item.ItemBase;
import farcore.lib.render.RenderHandler;
import farcore.util.FleTextureMap;
import net.minecraft.item.Item;

public class FarCore
{
	public static final String ID = "farcore";
	public static final String OVERRIDE_ID = "fo";
	
	public static volatile int version = 300;

	public static ThermalNet thermalNet;
	public static KineticNet kineticNet;
	public static ElectricNet electricNet;

	@SideOnly(Side.CLIENT)
	public static RenderHandler handlerA;
	@SideOnly(Side.CLIENT)
	public static RenderHandler handlerB;
	@SideOnly(Side.CLIENT)
	public static FleTextureMap bottonTextureMap;
	@SideOnly(Side.CLIENT)
	public static FleTextureMap conditionTextureMap;
	
	public static String translateToLocal(String unlocalized, Object...arg)
	{
		return FarCoreSetup.lang.translateToLocal(unlocalized, arg);
	}
}