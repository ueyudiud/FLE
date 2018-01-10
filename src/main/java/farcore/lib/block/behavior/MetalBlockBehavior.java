/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block.behavior;

import farcore.blocks.BlockMetal;
import farcore.data.EnumToolTypes;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import nebula.common.block.IBlockBehavior;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class MetalBlockBehavior<B extends BlockMetal> extends PropertyBlockable<B> implements IBlockBehavior<B>
{
	public MetalBlockBehavior(Mat material, int harvestLevel, float hardness, float explosionResistance)
	{
		super(material, harvestLevel, hardness, explosionResistance);
	}
	
	@Override
	public float getBlockHardness(B block, IBlockState state, World world, BlockPos pos)
	{
		return this.hardness;
	}
	
	@Override
	public float getExplosionResistance(B block, IBlockState state, World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return this.explosionResistance;
	}
	
	public int getHarvestLevel(B block, IBlockState state)
	{
		return this.harvestLevel;
	}
	
	public String getHarvestTool(B block, IBlockState state)
	{
		return EnumToolTypes.PICKAXE.name;
	}
}
