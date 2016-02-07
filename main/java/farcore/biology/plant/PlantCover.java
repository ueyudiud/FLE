package farcore.biology.plant;

import java.util.EnumMap;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import farcore.biology.DNA;
import farcore.biology.DNAPart;
import farcore.biology.DNAProperty;
import farcore.block.BlockFactory;
import farcore.block.EnumDirtState;
import farcore.block.IPropagatedableBlock;
import farcore.world.IObjectInWorld;

public class PlantCover extends ICoverablePlantSpecies
{
	private class Instance implements IPlant
	{
		DNA dna;
		int buf;
		int age;
		
		private Instance()
		{
			this(property.getDefaultDNA());
		}
		
		public Instance(DNA dna)
		{
			this.dna = dna;
		}
		
		@Override
		public DNA getDNA()
		{
			return dna;
		}
		
		@Override
		public int getDeformity()
		{
			return 0;
		}
		
		@Override
		public void update(IObjectInWorld object)
		{
			if (buf++ == 100)
			{
				buf = 0;
				++age;
				if (object.getWorld()
						.getBlockLightOpacity(object.getBlockPos().up()) < 10)
				{
					if (object.getWorld().getBlockState(object.getBlockPos())
							.getBlock() instanceof IPropagatedableBlock)
						((IPropagatedableBlock) object.getWorld()
								.getBlockState(object.getBlockPos()).getBlock())
										.setDead(object.getWorld(),
												object.getBlockPos());
				}
				else if (age > 20)
				{
					age = 10;
					BlockFactory.spreadSpecies(object.getWorld(),
							object.getBlockPos(), this);
				}
			}
		}
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound nbt)
		{
			nbt.setShort("age", (short) age);
			dna.writeToNBT(nbt);
			return nbt;
		}
		
		@Override
		public void readFromNBT(NBTTagCompound nbt)
		{
			age = nbt.getShort("age");
			dna = DNA.loadFromNBT(nbt);
		}
		
		@Override
		public IPlantSpecies getSpecie()
		{
			return PlantCover.this;
		}
	}
	
	static
	{
		new PlantCover(EnumDirtState.grass);
		new PlantCover(EnumDirtState.mycelium);
		new PlantCover(EnumDirtState.moss);
	}
	
	private static EnumMap<EnumDirtState, ICoverablePlantSpecies> map = new EnumMap(
			EnumDirtState.class);
			
	public static ICoverablePlantSpecies getSpecies(EnumDirtState state)
	{
		return map.get(state);
	}
	
	private EnumDirtState state;
	private final DNAProperty property;
	
	PlantCover(EnumDirtState state)
	{
		DNAPart part1 = DNAPart.part(fullName() + ".speed", "A", true, "a",
				false);
		DNAPart part2 = DNAPart.part(fullName() + "grow", "A", true, "a",
				false);
		property = new DNAProperty(part1, part2);
		this.state = state;
		map.put(state, this);
	}
	
	@Override
	public String name()
	{
		return "dirt." + state.name();
	}
	
	@Override
	public ImmutableList<DNAPart> dna()
	{
		return null;
	}
	
	@Override
	public boolean canGrow(World world, BlockPos pos)
	{
		return world.getLightFromNeighbors(pos.up()) > 8;
	}
	
	@Override
	public Instance instance()
	{
		return new Instance();
	}
	
	@Override
	public int getSpreadLoop(World world, BlockPos pos)
	{
		return 4;
	}
	
	@Override
	public int getSpreadRange(World world, BlockPos pos)
	{
		return 3;
	}
	
	public EnumDirtState getState()
	{
		return state;
	}
	
	@Override
	public boolean isBelongTo(World world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public boolean matchBlock(World world, BlockPos pos, IBlockState state)
	{
		if (world.getBlockState(pos).getBlock() instanceof IPropagatedableBlock)
		{
			((IPropagatedableBlock) world.getBlockState(pos).getBlock())
					.canPropagatedable(world, pos, this);
		}
		return false;
	}
	
	@Override
	public void onPlant(World world, BlockPos pos, IPlant plant)
	{
		if (plant instanceof Instance && world.getBlockState(pos)
				.getBlock() instanceof IPropagatedableBlock)
		{
			DNA dna = ((Instance) plant).dna
					.variation(plant.getDeformity() + 0.0625F);
			if (dna == null)
				return;
			((IPropagatedableBlock) world.getBlockState(pos).getBlock())
					.onPropagatedable(world, pos, new Instance(dna));
		}
	}
}