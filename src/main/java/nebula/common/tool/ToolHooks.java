/*
 * copyright© 2016-2018 ueyudiud
 */
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

import java.util.Collection;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import nebula.base.A;
import nebula.common.block.BlockBase;
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

/**
 * Tool using checking hooks.
 * 
 * @author ueyudiud
 */
public class ToolHooks
{
	private static final Multimap<Material, EnumToolType>	breakableToolMap	= HashMultimap.create();
	private static final Multimap<Material, EnumToolType>	efficiencyToolMap	= HashMultimap.create();
	private static final Multimap<Material, EnumToolType>	harvestableToolMap	= HashMultimap.create();
	
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
		breakableToolMap.putAll(material, A.argument(toolTypes));
	}
	
	public static void addEfficiencyTool(Material material, EnumToolType...toolTypes)
	{
		List<EnumToolType> list = A.argument(toolTypes);
		breakableToolMap.putAll(material, list);// The efficiency tool should be able to break it.
		efficiencyToolMap.putAll(material, list);
	}
	
	public static void addHarvestableTool(Material material, boolean efficiency, EnumToolType...toolTypes)
	{
		List<EnumToolType> list = A.argument(toolTypes);
		breakableToolMap.putAll(material, list);// The harvestable tool should be able to break it.
		if (efficiency) efficiencyToolMap.putAll(material, list);
		harvestableToolMap.putAll(material, list);
	}
	
	public static boolean isToolBreakable(IBlockState state, ItemStack stack)
	{
		Material material = state.getMaterial();
		Block block = state.getBlock();
		if (block instanceof BlockBase)
		{
			if (!((BlockBase) block).isToolRequired())
			{
				return true;
			}
		}
		else if (material.isToolNotRequired())//Might not accuracy, but it still needed a compatible option.
		{
			return true;
		}
		return mapMatch(breakableToolMap, material, stack);
	}
	
	public static boolean isToolEffciency(IBlockState state, ItemStack stack)
	{
		return mapMatch(efficiencyToolMap, state.getMaterial(), stack);
	}
	
	public static boolean isToolHarvestable(Block block, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return ForgeHooks.canHarvestBlock(block, player, world, pos) || matchHarvestable(world.getBlockState(pos), player);
	}
	
	private static boolean matchHarvestable(IBlockState state, EntityPlayer player)
	{
		ItemStack stack = player.getHeldItemMainhand();
		Material material = state.getMaterial();
		Collection<EnumToolType> toolTypes = harvestableToolMap.get(material);
		if (toolTypes.isEmpty()) return false;
		List<EnumToolType> target = ItemStacks.getCurrentToolType(stack);
		EnumToolType type = L.get(toolTypes, target::contains);
		if (type == null)
			return false;
		return stack.getItem().getHarvestLevel(stack, type.name, player, state) >= state.getBlock().getHarvestLevel(state);
	}
	
	private static boolean mapMatch(Multimap<Material, EnumToolType> map, Material material, ItemStack stack)
	{
		Collection<EnumToolType> toolTypes = map.get(material);
		if (toolTypes.isEmpty()) return false;
		List<EnumToolType> target = ItemStacks.getCurrentToolType(stack);
		return L.contain(toolTypes, target::contains);
	}
}
