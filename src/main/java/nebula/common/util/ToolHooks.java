package nebula.common.util;

import static nebula.common.data.EnumToolType.AXE;
import static nebula.common.data.EnumToolType.PICKAXE;
import static nebula.common.data.EnumToolType.SHOVEL;
import static nebula.common.data.EnumToolType.SWORD;
import static net.minecraft.block.material.Material.CLAY;
import static net.minecraft.block.material.Material.GRASS;
import static net.minecraft.block.material.Material.GROUND;
import static net.minecraft.block.material.Material.ICE;
import static net.minecraft.block.material.Material.IRON;
import static net.minecraft.block.material.Material.LEAVES;
import static net.minecraft.block.material.Material.PLANTS;
import static net.minecraft.block.material.Material.ROCK;
import static net.minecraft.block.material.Material.SAND;
import static net.minecraft.block.material.Material.SNOW;
import static net.minecraft.block.material.Material.VINE;
import static net.minecraft.block.material.Material.WEB;
import static net.minecraft.block.material.Material.WOOD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nebula.common.data.EnumToolType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeHooks;

public class ToolHooks
{
	private static final Map<Material, List<EnumToolType>> breakableToolMap = new HashMap();
	private static final Map<Material, List<EnumToolType>> efficiencyToolMap = new HashMap();
	private static final Map<Material, List<EnumToolType>> harvestableToolMap = new HashMap();
	
	static
	{
		addEfficiencyTool(WOOD, AXE);
		addEfficiencyTool(LEAVES, AXE);
		addEfficiencyTool(GROUND, SHOVEL);
		addEfficiencyTool(GRASS, SHOVEL);
		addEfficiencyTool(SAND, SHOVEL);
		addEfficiencyTool(SNOW, SHOVEL);
		addEfficiencyTool(CLAY, SHOVEL);
		addEfficiencyTool(ROCK, PICKAXE);
		addEfficiencyTool(IRON, PICKAXE);
		addEfficiencyTool(ICE, PICKAXE);
		addEfficiencyTool(PLANTS, SWORD);
		addEfficiencyTool(WEB, SWORD);
		addEfficiencyTool(VINE, SWORD);
	}
	
	public static void addBreakableTool(Material material, EnumToolType...toolTypes)
	{
		L.put(efficiencyToolMap, material, toolTypes);
	}
	
	public static void addEfficiencyTool(Material material, EnumToolType...toolTypes)
	{
		L.put(breakableToolMap, material, toolTypes);//The efficiency tool should be able to break it.
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
	
	public static boolean isToolHarvestable(Block block, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return ForgeHooks.canHarvestBlock(block, player, world, pos) || matchHarvestable(world.getBlockState(pos), player);
	}
	
	private static boolean matchHarvestable(IBlockState state, EntityPlayer player)
	{
		if (!mapMatch(harvestableToolMap, state, player)) return false;
		ItemStack stack = player.getHeldItemMainhand();
		Set<String> set = stack.getItem().getToolClasses(stack);
		if (set.isEmpty()) return false;
		//Only get a default level, I don't think there is any tool have different harvest level for different tool type...
		int level = stack.getItem().getHarvestLevel(stack, set.iterator().next(), player, state);
		return level >= state.getBlock().getHarvestLevel(state);
	}
	
	private static boolean mapMatch(Map<Material, List<EnumToolType>> map, IBlockState state, EntityPlayer player)
	{
		Material material = state.getMaterial();
		if(material.isToolNotRequired()) return true;
		if(!map.containsKey(material)) return false;
		List<EnumToolType> toolTypes = map.get(material);
		return Players.matchCurrentToolType(player, L.cast(toolTypes, EnumToolType.class));
	}
}