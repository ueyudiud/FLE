package fle.load;

import static farcore.util.U.OreDict.registerValid;

import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.lib.substance.SubstanceRock;
import farcore.util.U;

public class OreDicts
{
	public static void init()
	{
		for(SubstanceRock rock : SubstanceRock.getRocks())
		{
			String ore = U.Lang.validateOre(true, rock.getName());
			registerValid("stone" + ore, EnumItem.rock_block.instance(1, rock));
			registerValid("cobble" + ore, EnumItem.cobble_block.instance(1, rock));
			registerValid("chip" + ore, EnumItem.stone_chip.instance(1, rock.getName()));
			registerValid("fragment" + ore, EnumItem.stone_fragment.instance(1, rock.getName()));
		}
		registerValid("chipSharpFlint", EnumItem.stone_chip.instance(1, "flintSharp"));
		registerValid("chipObsidian", EnumItem.stone_chip.instance(1, "obsidian"));
		registerValid(EnumToolType.adz.ore(), EnumItem.tool.instance(1, "rough_stone_adz"));
		registerValid(EnumToolType.axe.ore(), EnumItem.tool.instance(1, "rough_stone_adz"));
		registerValid("craftingToolStoneAxe", EnumItem.tool.instance(1, "stone_axe"));
		registerValid(EnumToolType.axe.ore(), EnumItem.tool.instance(1, "stone_axe"));
		registerValid(EnumToolType.firestarter.ore(), EnumItem.tool.instance(1, "firestarter"));
		registerValid(EnumToolType.hammer_digable_basic.ore(), EnumItem.tool.instance(1, "flint_hammer"));
		registerValid(EnumToolType.hammer_digable_basic.ore(), EnumItem.tool.instance(1, "wood_hammer"));
		registerValid(EnumToolType.hammer_digable.ore(), EnumItem.tool.instance(1, "stone_hammer"));
		registerValid(EnumToolType.shovel.ore(), EnumItem.tool.instance(1, "stone_shovel"));
		registerValid(EnumToolType.bar_grizzly.ore(), EnumItem.tool.instance(1, "bar_grizzly"));
		registerValid("pileGravel", EnumItem.pile.instance(1, "gravel"));
		registerValid("treeSapling", BlockItems.sapling);
		registerValid("branchWood", EnumItem.plant.instance(1, "branch_oak"));
		registerValid("branchWood", EnumItem.plant.instance(1, "branch_spruce"));
		registerValid("branchWood", EnumItem.plant.instance(1, "branch_birch"));
		registerValid("branchWood", EnumItem.plant.instance(1, "branch_ceiba"));
		registerValid("branchWood", EnumItem.plant.instance(1, "branch_acacia"));
		registerValid("branchWood", EnumItem.plant.instance(1, "branch_oak-black"));
		registerValid("branchWood", EnumItem.plant.instance(1, "branch_bush"));
		registerValid("vine", EnumItem.plant.instance(1, "vine"));
		registerValid("ropeVine", EnumItem.plant.instance(1, "vine_rope"));
	}
}