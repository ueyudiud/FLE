/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block;

import java.util.Random;

import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import nebula.common.block.BlockBase;
import nebula.common.util.Direction;
import nebula.common.world.IModifiableCoord;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMaterial<P extends PropertyBlockable> extends BlockBase implements IThermalCustomBehaviorBlock
{
	public final Mat				material;
	protected final PropertyBasic	basic;
	public final P					property;
	
	public BlockMaterial(String modid, String name, Material materialIn, Mat mat, P property)
	{
		super(modid, name, materialIn);
		this.material = mat;
		this.basic = this.material.getProperty(MP.property_basic);
		this.property = property;
	}
	
	public BlockMaterial(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn, Mat mat, P property)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
		this.material = mat;
		this.basic = this.material.getProperty(MP.property_basic);
		this.property = property;
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return this.property.hardness;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder)
	{
		return this.property.explosionResistance;
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return getExplosionResistance(exploder);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return this.material.getProperty(MP.fire_spread_speed);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return this.material.getProperty(MP.flammability);
	}
	
	@Override
	public int getLightOpacity(IBlockState state)
	{
		return this.material.RGBa[3];
	}
	
	@Override
	public int getLightValue(IBlockState state)
	{
		return this.material.getProperty(MP.light_value);
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return getFlammability(world, pos, face) != 0;
	}
	
	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side)
	{
		return this.material.contain(SubTags.FIRE_SOURCE);
	}
	
	@Override
	public boolean onBurn(IModifiableCoord coord, float burnHardness, Direction direction)
	{
		return false;
	}
	
	@Override
	public boolean onBurningTick(IModifiableCoord coord, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		return false;
	}
	
	@Override
	public double getThermalConduct(World world, BlockPos pos, IBlockState state)
	{
		return this.basic.thermalConduct;
	}
	
	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return this.material.getProperty(MP.fire_encouragement);
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		entityIn.fall(fallDistance, 1F - this.material.getProperty(MP.fallen_damage_deduction) / 10000F);
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return this.property.harvestLevel;
	}
}
