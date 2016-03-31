package fle.load;

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
			U.OreDict.registerValid("stone" + ore, EnumItem.rock_block.instance(1, rock));
			U.OreDict.registerValid("cobble" + ore, EnumItem.cobble_block.instance(1, rock));
		}
		U.OreDict.registerValid(EnumToolType.adz.ore(), EnumItem.tool.instance(1, "rough_stone_adz"));
		U.OreDict.registerValid("craftingToolStoneAxe", EnumItem.tool.instance(1, "stone_axe"));
		U.OreDict.registerValid(EnumToolType.axe.ore(), EnumItem.tool.instance(1, "stone_axe"));
		U.OreDict.registerValid(EnumToolType.hammer_digable_basic.ore(), EnumItem.tool.instance(1, "flint_hammer"));
		U.OreDict.registerValid(EnumToolType.hammer_digable_basic.ore(), EnumItem.tool.instance(1, "stone_hammer"));
		U.OreDict.registerValid(EnumToolType.hammer_digable_basic.ore(), EnumItem.tool.instance(1, "wood_hammer"));
		U.OreDict.registerValid(EnumToolType.shovel.ore(), EnumItem.tool.instance(1, "stone_shovel"));
	}
}