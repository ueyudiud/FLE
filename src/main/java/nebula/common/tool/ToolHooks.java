package nebula.common.tool;

import static nebula.common.tool.EnumToolType.AXE;
import static nebula.common.tool.EnumToolType.PICKAXE;
import static nebula.common.tool.EnumToolType.SHOVEL;
import static nebula.common.tool.EnumToolType.SWORD;
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

import nebula.common.util.ItemStacks;
import nebula.common.util.L;
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
		L.put(breakableToolMap, material, toolTypes);
	}
	
	public static void addEfficiencyTool(Material material, EnumToolType...toolTypes)
	{
		L.put(breakableToolMap, material, toolTypes);//The efficiency tool should be able to break it.
		L.put(efficiencyToolMap, material, toolTypes);
	}
	
	public static void addHarvestableTool(Material material, boolean efficiency, EnumToolType...toolTypes)
	{
		L.put(breakableToolMap, material, toolTypes);//The harvestable tool should be able to break it.
		if (efficiency) L.put(efficiencyToolMap, material, toolTypes);
		L.put(harvestableToolMap, material, toolTypes);
	}
	
	public static boolean isToolBreakable(IBlockState state, ItemStack stack)
	{
		return mapMatch(breakableToolMap, state, stack);
	}
	
	public static boolean isToolEffciency(IBlockState state, ItemStack stack)
	{
		return mapMatch(efficiencyToolMap, state, stack);
	}
	
	public static boolean isToolHarvestable(Block block, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return ForgeHooks.canHarvestBlock(block, player, world, pos) || matchHarvestable(world.getBlockState(pos), player);
	}
	
	private static boolean matchHarvestable(IBlockState state, EntityPlayer player)
	{
		ItemStack stack = player.getHeldItemMainhand();
		if (!mapMatch(harvestableToolMap, state, stack)) return false;
		Set<String> set = stack.getItem().getToolClasses(stack);
		if (set.isEmpty()) return false;
		//Only get a default level, I don't think there is any tool have different harvest level for different tool type...
		int level = stack.getItem().getHarvestLevel(stack, set.iterator().next());
		return level >= state.getBlock().getHarvestLevel(state);
	}
	
	private static boolean mapMatch(Map<Material, List<EnumToolType>> map, IBlockState state, ItemStack stack)
	{
		Material material = state.getMaterial();
		if(material.isToolNotRequired()) return true;
		if(!map.containsKey(material)) return false;
		List<EnumToolType> toolTypes = map.get(material);
		List<EnumToolType> target = ItemStacks.getCurrentToolType(stack);
		return L.contain(toolTypes, type -> target.contains(type));
	}
}