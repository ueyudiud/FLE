package farcore.lib.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farcore.data.EnumToolType;
import farcore.lib.block.instance.BlockLog;
import farcore.lib.block.instance.BlockOre;
import farcore.util.L;
import farcore.util.Players;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;

public class ToolHook
{
	private static final Map<Material, List<EnumToolType>> breakableToolMap = new HashMap();
	private static final Map<Material, List<EnumToolType>> efficiencyToolMap = new HashMap();
	@Deprecated
	private static final Map<Material, List<EnumToolType>> harvestableToolMap = new HashMap();

	static
	{
		addEfficiencyTool(Material.ROCK, EnumToolType.hammer_digable, EnumToolType.pickaxe, EnumToolType.explosive, EnumToolType.drill, EnumToolType.laser);
		addEfficiencyTool(Material.IRON, EnumToolType.hammer_digable, EnumToolType.pickaxe, EnumToolType.explosive, EnumToolType.drill, EnumToolType.laser);
		addEfficiencyTool(Material.ANVIL, EnumToolType.hammer_digable, EnumToolType.pickaxe, EnumToolType.explosive, EnumToolType.drill, EnumToolType.laser);
		addEfficiencyTool(BlockOre.ORE, EnumToolType.hammer_digable, EnumToolType.pickaxe, EnumToolType.explosive, EnumToolType.drill, EnumToolType.laser);
		addEfficiencyTool(BlockLog.LOG, EnumToolType.adz, EnumToolType.axe, EnumToolType.saw, EnumToolType.bow_saw);
	}
	
	public static void addBreakableTool(Material material, EnumToolType...toolTypes)
	{
		L.put(efficiencyToolMap, material, toolTypes);
	}
	
	public static void addEfficiencyTool(Material material, EnumToolType...toolTypes)
	{
		L.put(breakableToolMap, material, toolTypes);
		L.put(efficiencyToolMap, material, toolTypes);
	}

	public static boolean isToolBreakable(IBlockState state, EntityPlayer player)
	{
		return mapMatch(breakableToolMap, state, player);
	}

	public static boolean isToolEffciency(IBlockState state, EntityPlayer player)
	{
		return mapMatch(efficiencyToolMap, state, player);
	}
	
	private static boolean mapMatch(Map<Material, List<EnumToolType>> map, IBlockState state, EntityPlayer player)
	{
		List<EnumToolType> list = Players.getCurrentToolType(player);
		Material material = state.getMaterial();
		if(material.isToolNotRequired()) return true;
		if(!map.containsKey(material)) return false;
		List<EnumToolType> toolTypes = map.get(material);
		for(EnumToolType toolType : list)
			if(toolTypes.contains(toolType)) return true;
		return false;
	}
}