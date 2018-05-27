/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.world;

import nebula.common.block.ISmartFallableBlock;
import nebula.common.data.Misc;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.world.WorldTask;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;

/**
 * @author ueyudiud
 */
public class TaskFalling extends WorldTask
{
	private BlockPos source;
	private BlockPos target;
	private IBlockState state;
	private TileEntity tile;
	private boolean flammable;
	
	public TaskFalling(World world, BlockPos source, BlockPos target, IBlockState state, long delay)
	{
		super(world, delay);
		this.source = source;
		this.target = target;
		this.state = state;
		this.tile = world.getChunkFromBlockCoords(source).getTileEntity(source, EnumCreateEntityType.CHECK);
		this.flammable = state.getBlock().isFlammable(world, source, null);
		world.setBlockState(source, Misc.AIR, 4);
	}
	
	@Override
	public boolean handleTask()
	{
		if (!this.world.isBlockLoaded(this.target))
		{
			if (this.state.getBlock() instanceof ISmartFallableBlock)
			{
				((ISmartFallableBlock) this.state.getBlock()).onStartFalling(this.world, this.target);
			}
			int height = 0;
			BlockPos pos = this.target;
			while (!EntityFallingBlockExtended.canFallAt(this.world, pos, this.state))
			{
				pos = pos.down();
				++ height;
			}
			if (pos.getY() > 0)
			{
				EntityFallingBlockExtended.replaceFallingBlock(this.world, pos, this.state, height);
				NBTTagCompound nbt = new NBTTagCompound();
				if (this.tile != null)
				{
					this.tile.writeToNBT(nbt);
				}
				if (!(this.state.getBlock() instanceof ISmartFallableBlock && ((ISmartFallableBlock) this.state.getBlock()).onFallOnGround(this.world, pos, this.state, height, nbt)))
				{
					this.world.setBlockState(pos, this.state, 2);
					TileEntity tile1 = this.world.getTileEntity(pos);
					if (tile1 != null)
					{
						tile1.writeToNBT(nbt);
						tile1.setPos(pos);
					}
				}
			}
			return true;
		}
		if (super.handleTask())
		{
			this.world.notifyBlockUpdate(this.source, this.state, Misc.AIR, 2);
			this.world.spawnEntity(new EntityFallingBlockExtended(this.world, this.source, this.target, this.state, this.tile, this.flammable));
			return true;
		}
		return false;
	}
}
