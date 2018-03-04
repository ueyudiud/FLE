/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.world;

import nebula.common.util.Direction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

class WorldCoord0 implements IModifiableCoord
{
	World world;
	BlockPos pos;
	TileEntity tile;
	
	public WorldCoord0(World world, BlockPos pos, TileEntity tile)
	{
		this.world = world;
		this.pos = pos;
		this.tile = tile;
	}
	
	@Override
	public World world()
	{
		return this.world;
	}
	
	@Override
	public BlockPos pos()
	{
		return this.pos;
	}
	
	@Override
	public TileEntity getTE()
	{
		return this.tile.isInvalid() ? null : this.tile;
	}
}

/**
 * @author ueyudiud
 */
class WorldCoord1 implements IModifiableCoord
{
	IModifiableCoord parent;
	
	World world;
	BlockPos pos;
	
	WorldCoord1(IModifiableCoord parent, BlockPos target)
	{
		this.parent = parent;
		this.world = parent.world();
		this.pos = target;
	}
	
	@Override
	public World world()
	{
		return this.world;
	}
	
	@Override
	public BlockPos pos()
	{
		return this.pos;
	}
	
	@Override
	public IModifiableCoord offset(Direction offset)
	{
		BlockPos pos1 = offset.offset(this.pos);
		return pos1.equals(this.parent.pos()) ? this.parent : new WorldCoord1(this.parent, pos1);
	}
	
	@Override
	public IModifiableCoord offset(int x, int y, int z)
	{
		BlockPos pos1 = this.pos.add(x, y, z);
		return pos1.equals(this.parent.pos()) ? this.parent : new WorldCoord1(this.parent, pos1);
	}
}

class WorldCoord2 implements ICoord
{
	ICoord parent;
	
	World world;
	BlockPos pos;
	
	WorldCoord2(ICoord parent, BlockPos target)
	{
		this.parent = parent;
		this.world = parent.world();
		this.pos = target;
	}
	
	@Override
	public World world()
	{
		return this.world;
	}
	
	@Override
	public BlockPos pos()
	{
		return this.pos;
	}
	
	@Override
	public ICoord offset(Direction offset)
	{
		BlockPos pos1 = offset.offset(this.pos);
		return pos1.equals(this.parent.pos()) ? this.parent : new WorldCoord2(this.parent, pos1);
	}
	
	@Override
	public ICoord offset(int x, int y, int z)
	{
		BlockPos pos1 = this.pos.add(x, y, z);
		return pos1.equals(this.parent.pos()) ? this.parent : new WorldCoord2(this.parent, pos1);
	}
}