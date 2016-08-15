package farcore;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.world.IWorldGenerateReplacer;
import farcore.network.Network;
import net.minecraft.creativetab.CreativeTabs;

public class FarCore
{
	public static final String ID = "farcore";
	
	public static final String OVERRIDE_ID = "faroverride";
	
	public static final String INNER_RENDER = "farinner";
	
	public static Network network;
	
	public static CreativeTabs tabFluids;
	public static CreativeTabs tabResourceBlock;
	public static CreativeTabs tabResourceItem;
	public static CreativeTabs tabTool;
	public static CreativeTabs tabMachine;
	public static CreativeTabs tabMaterial;
	
	public static final List<IWorldGenerateReplacer> worldGenerateReplacers = new ArrayList();

	public static boolean debug = false;
}