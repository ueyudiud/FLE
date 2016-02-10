package fle.resource.fluid;

import java.util.Random;

import farcore.FarCore;
import farcore.fluid.BlockFluidBase;
import farcore.fluid.FluidBase;
import farcore.fluid.IFlashableFluid;
import farcore.world.BlockPos;
import farcore.world.Direction;
import fle.core.FLE;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockFlashableFluid extends BlockFluidBase
{
	public IFlashableFluid fluid;
	
	public BlockFlashableFluid(IFlashableFluid fluid, Material material)
	{
		super((FluidBase) fluid, material);
		this.fluid = fluid;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		super.updateTick(world, x, y, z, rand);
		if(!world.isRemote)
		{
			int flashPoint = fluid.getFlashPoint(world, x, y, z);
			BlockPos pos = new BlockPos(world, x, y, z);
			if(FarCore.getEnviormentTemperature(world, new BlockPos(world, x, y, z)) > flashPoint)
			{
				explosion(world, x, y, z);
			}
			else if(FLE.fle.getWorldManager().isSourceOfFire(pos.offset(Direction.UP)) ||
					FLE.fle.getWorldManager().isSourceOfFire(pos.offset(Direction.DOWN)) ||
					FLE.fle.getWorldManager().isSourceOfFire(pos.offset(Direction.NORTH)) ||
					FLE.fle.getWorldManager().isSourceOfFire(pos.offset(Direction.SOUTH)) ||
					FLE.fle.getWorldManager().isSourceOfFire(pos.offset(Direction.EAST)) ||
					FLE.fle.getWorldManager().isSourceOfFire(pos.offset(Direction.WEST)))
			{
				explosion(world, x, y, z);
			}
		}
	}
	
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
	{
		if((int) (explosion.explosionX - x) == 0 &&
				(int) (explosion.explosionY - y) == 0 &&
				(int) (explosion.explosionZ - z) == 0)
		{
			world.setBlockToAir(x, y, z);
			return;
		}
		if(explosion.isFlaming)
		{
			explosion(world, x, y, z);
		}
	}
	
	protected void explosion(World world, int x, int y, int z)
	{
		if(world.isRemote) return;
		world.newExplosion(null, x + 0.5, y + 0.5, z + 0.5, (float) fluid.getExplosionLevel(world, x, y, z) * getQuantaValue(world, x, y, z) / 100F, true, true);
	}
}