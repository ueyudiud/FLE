/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.blocks.terria;

import java.util.List;
import java.util.Random;

import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import nebula.base.ObjArrayParseHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class BlockClay extends BlockSoilLike
{
	public BlockClay(String modid, String name, Material materialIn, Mat mat, PropertyBlockable<?> property)
	{
		super(modid, name, materialIn, mat, property);
		this.uneffectiveSpeedMultiplier = 1F / 200F;
		this.effectiveSpeedMultiplier = 1F / 20F;
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		MC.clay.registerOre(this.material, this);
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		if (!worldIn.isRemote)
		{
			IBlockState state2 = updateBase(worldIn, pos, state, random, false);
			if (state != state2)
			{
				worldIn.setBlockState(pos, state2, 2);
			}
			spreadCoverPlant(worldIn, pos, state2, random);
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			updateBase(worldIn, pos, state, rand, true);
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		return ObjArrayParseHelper.newArrayList(ItemMulti.createStack(this.material, MC.clayball, 4 + RANDOM.nextInt(5)));
	}
}
