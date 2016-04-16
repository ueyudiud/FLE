package fle.load;

import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.lib.substance.SubstanceRock;
import farcore.util.U;
import farcore.util.U.OreDict;

public class OreDicts
{
	public static void init()
	{
		for(SubstanceRock rock : SubstanceRock.getRocks())
		{
			String ore = U.Lang.validateOre(true, rock.getName());
			OreDict.registerValid("stone" + ore, EnumItem.rock_block.instance(1, rock));
			OreDict.registerValid("cobble" + ore, EnumItem.cobble_block.instance(1, rock));
			OreDict.registerValid("chip" + ore, EnumItem.stone_chip.instance(1, rock.getName()));
			OreDict.registerValid("fragment" + ore, EnumItem.stone_fragment.instance(1, rock.getName()));
		}
		OreDict.registerValid(EnumToolType.adz.ore(), EnumItem.tool.instance(1, "rough_stone_adz"));
		OreDict.registerValid(EnumToolType.axe.ore(), EnumItem.tool.instance(1, "rough_stone_adz"));
		OreDict.registerValid("craftingToolStoneAxe", EnumItem.tool.instance(1, "stone_axe"));
		OreDict.registerValid(EnumToolType.axe.ore(), EnumItem.tool.instance(1, "stone_axe"));
		OreDict.registerValid(EnumToolType.hammer_digable_basic.ore(), EnumItem.tool.instance(1, "flint_hammer"));
		OreDict.registerValid(EnumToolType.hammer_digable_basic.ore(), EnumItem.tool.instance(1, "stone_hammer"));
		OreDict.registerValid(EnumToolType.hammer_digable_basic.ore(), EnumItem.tool.instance(1, "wood_hammer"));
		OreDict.registerValid(EnumToolType.shovel.ore(), EnumItem.tool.instance(1, "stone_shovel"));
		OreDict.registerValid("treeSapling", BlockItems.sapling);
	}
}