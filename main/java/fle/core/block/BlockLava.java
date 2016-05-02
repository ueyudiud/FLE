package fle.core.block;

import farcore.block.BlockStandardFluid;
import farcore.enums.EnumBlock;
import farcore.interfaces.energy.thermal.IThermalProviderBlock;
import farcore.util.Values;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockLava extends BlockStandardFluid implements IThermalProviderBlock
{
	public BlockLava()
	{
		super(FluidRegistry.LAVA, Material.lava);
		setQuantaPerBlock(16);
		EnumBlock.lava.setBlock(this);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		entity.setFire(50);
	}
	
	@Override
	public int tickRate(World world)
	{
		return world.provider.isHellWorld ? tickRate / 2 : tickRate;
	}

	@Override
	public float getBlockTemperature(World world, int x, int y, int z)
	{
		return getFluid().getTemperature(world, x, y, z);
	}

	@Override
	public float getThermalConductivity(World world, int x, int y, int z)
	{
		return Values.lavaThermalConductivity;
	}

	@Override
	public void onHeatChanged(World world, int x, int y, int z, float input)
	{
		if(input < 0)
		{
			if(world.rand.nextDouble() * 100000D < -input)
			{
				freezeBlock(world, x, y, z);
			}
		}
	}
	
	private void freezeBlock(World world, int x, int y, int z)
	{
		if(world.getBlockMetadata(x, y, z) == quantaPerBlock - 1)
		{
			world.setBlock(x, y, z, Blocks.obsidian);
		}
		else
		{
			EnumBlock.cobble.spawn(world, x, y, z, "stone");
		}
	}
}