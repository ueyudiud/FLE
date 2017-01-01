package farcore.lib.world.gen;

import farcore.data.EnumBlock;
import farcore.data.EnumOreAmount;
import farcore.data.EnumRockType;
import farcore.lib.block.instance.BlockOre;
import farcore.lib.material.Mat;
import net.minecraft.block.state.IBlockState;

public abstract class FarWorldGenOreBase extends FarWorldGenerator
{
	protected final IBlockState ore = EnumBlock.ore.block.getDefaultState();

	protected void generateOre(int xOffset, int yOffset, int zOffset, Mat ore, EnumOreAmount amount, Mat rock, EnumRockType type)
	{
		BlockOre.ORE_ELEMENT_THREAD.set(new Object[]{ore, amount, rock, type});
		setBlockAndNotifyAdequately(world, pos.add(xOffset, yOffset, zOffset), this.ore);
		BlockOre.ORE_ELEMENT_THREAD.set(null);
	}
}