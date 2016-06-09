package farcore.enums;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public enum EnumBlock 
{
	/**
	 * Far Land Era fire, registered by fle.<br>
	 * <code>spawn(world, x, y, z)</code><br>
	 * Spawn a new fire block which will not extinguished by update.
	 * <code>spawn(world, x, y, z, level)</code><br>
	 * Spawn a new fire block with select level.
	 * @param level The level of fire, higher level will cause fire spread in a larger range.
	 * The value is from 1 to 15.
	 */
	fire
	{
		@Override
		public void spawn(World world, int x, int y, int z, int meta, int type)
		{
			if(!init)
			{
				world.setBlock(x, y, z, Blocks.lava);
			}
			else
			{
				super.spawn(world, x, y, z, meta, type);
			}
		}
	},
	/**
	 * Far Land Era water, registered by fle.<br>
	 * The method spawn use way see fluid block type.
	 * @see farcore.block.BlockStandardFluid
	 */
	water
	{
		@Override
		public void spawn(World world, int x, int y, int z, int meta, int type)
		{
			if(!init)
			{
				world.setBlock(x, y, z, Blocks.water);
			}
			else
			{
				super.spawn(world, x, y, z, meta, type);
			}
		}
	},
	/**
	 * Far Land Era ore, registered by fle.<br>
	 * This block provide ore or custom ore block.
	 */
	ore,
	lava, 
	ice
	{
		@Override
		public void spawn(World world, int x, int y, int z, int meta, int type)
		{
			if(!init)
			{
				world.setBlock(x, y, z, Blocks.ice);
			}
			else
			{
				super.spawn(world, x, y, z, meta, type);
			}
		}
	},
	rock
	{
		@Override
		public void spawn(World world, int x, int y, int z, int meta, int type)
		{
			if(!init)
			{
				world.setBlock(x, y, z, Blocks.stone);
			}
			else
			{
				super.spawn(world, x, y, z, meta, type);
			}
		}
	},
	cobble
	{
		@Override
		public void spawn(World world, int x, int y, int z, int meta, int type)
		{
			if(!init)
			{
				world.setBlock(x, y, z, Blocks.cobblestone);
			}
			else
			{
				super.spawn(world, x, y, z, meta, type);
			}
		}
	}, 
	sand
	{
		@Override
		public void spawn(World world, int x, int y, int z, int meta, int type)
		{
			if(!init)
			{
				world.setBlock(x, y, z, Blocks.sand);
			}
			else
			{
				super.spawn(world, x, y, z, meta, type);
			}
		}
	}, 
	bush
	{
		@Override
		public void spawn(World world, int x, int y, int z, int meta, int type)
		{
			if(!init)
			{
				world.setBlock(x, y, z, Blocks.deadbush);
			}
			else
			{
				super.spawn(world, x, y, z, meta, type);
			}
		}
	}, 
	vine
	{
		@Override
		public void spawn(World world, int x, int y, int z, int meta, int type)
		{
			if(!init)
			{
				world.setBlock(x, y, z, Blocks.vine);
			}
			else
			{
				super.spawn(world, x, y, z, meta, type);
			}
		}
	}, 
	snow
	{
		@Override
		public void spawn(World world, int x, int y, int z, int meta, int type)
		{
			if(!init)
			{
				world.setBlock(x, y, z, Blocks.snow_layer);
			}
			else
			{
				super.spawn(world, x, y, z, meta, type);
			}
		}
	},
	brick,
	crop;
	
	boolean init = false;
	Block block;
	EnumItem item;

	public void setBlock(Block block)
	{
		setBlock(block, null);
	}
	public void setBlock(Block block, EnumItem item)
	{
		if(init)
		{
			throw new RuntimeException("The block " + name() + " has already init!");
		}
		this.item = item;
		this.block = block;
		this.init = true;
	}
	
	public Block block()
	{
		return block;
	}
	
	public EnumItem item()
	{
		return item;
	}
	
	public void spawn(World world, int x, int y, int z)
	{
		if(init)
		{
			spawn(world, x, y, z, 0);
		}
	}
	
	public void spawn(World world, int x, int y, int z, int meta)
	{
		if(init)
		{
			spawn(world, x, y, z, meta, 3);
		}
	}
	
	public void spawn(World world, int x, int y, int z, int meta, int type)
	{
		if(init)
		{
			world.setBlock(x, y, z, block, meta, type);
		}
	}
	
	public void spawn(World world, int x, int y, int z, Object...objects)
	{
		if(init)
		{
			if(block instanceof IInfoSpawnable)
			{
				((IInfoSpawnable) block).spawn(world, x, y, z, objects);
			}
			else if(objects.length == 0)
			{
				spawn(world, x, y, z);
			}
			else if(objects.length == 1 && objects[0] instanceof Integer)
			{
				spawn(world, x, y, z, ((Integer) objects[0]).intValue());
			}
		}
	}
	
	public static interface IInfoSpawnable
	{
		/**
		 * Spawn a block with custom data in world.
		 * @param world
		 * @param x
		 * @param y
		 * @param z
		 * @param objects Custom data.
		 * @return Whether spawn is succeed.
		 */
		boolean spawn(World world, int x, int y, int z, Object...objects);
	}
}