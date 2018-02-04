/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.world.gen;

import farcore.blocks.terria.BlockOre;
import farcore.data.EnumBlock;
import farcore.data.EnumOreAmount;
import farcore.data.EnumRockType;
import farcore.lib.material.Mat;
import net.minecraft.block.state.IBlockState;

public abstract class FarWorldGenOreBase extends FarWorldGenerator
{
	protected final IBlockState ore = EnumBlock.ore.block.getDefaultState();
	
	protected void generateOre(int xOffset, int yOffset, int zOffset, Mat ore, EnumOreAmount amount, Mat rock, EnumRockType type)
	{
		BlockOre.ORE_ELEMENT_THREAD.set(new Object[] { ore, amount, rock, type });
		setBlockAndNotifyAdequately(this.world, this.pos.add(xOffset, yOffset, zOffset), this.ore);
		BlockOre.ORE_ELEMENT_THREAD.set(null);
	}
}
