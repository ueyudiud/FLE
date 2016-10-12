package farcore.lib.block;

import java.util.Random;

import farcore.data.M;
import farcore.lib.material.Mat;
import farcore.lib.util.Direction;
import farcore.lib.util.SubTag;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMaterial extends BlockBase implements IThermalCustomBehaviorBlock
{
	protected final Mat material;
	
	public BlockMaterial(String modid, String name, Material materialIn, Mat mat)
	{
		super(modid, name, materialIn);
		material = mat;
	}
	public BlockMaterial(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn, Mat mat)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
		material = mat;
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return material.blockHardness;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder)
	{
		return material.blockExplosionResistance;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return getExplosionResistance(exploder);
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return material.getProperty(M.fire_spread_speed);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return material.getProperty(M.flammability);
	}

	@Override
	public int getLightOpacity(IBlockState state)
	{
		return material.getProperty(M.light_opacity);
	}

	@Override
	public int getLightValue(IBlockState state)
	{
		return material.getProperty(M.light_value);
	}

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return getFlammability(world, pos, face) != 0;
	}
	
	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side)
	{
		return material.contain(SubTag.FIRE_SOURCE);
	}

	@Override
	public boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction)
	{
		return false;
	}

	@Override
	public boolean onBurningTick(World world, BlockPos pos, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		return false;
	}
	
	@Override
	public float getThermalConduct(World world, BlockPos pos)
	{
		return material.thermalConduct;
	}

	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return material.getProperty(M.fire_encouragement);
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		entityIn.fall(fallDistance, 1F - material.getProperty(M.fallen_damage_deduction) / 10000F);
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return material.blockHarvestLevel;
	}
}