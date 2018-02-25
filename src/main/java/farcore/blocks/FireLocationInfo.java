/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.blocks;

import static nebula.common.util.Direction.D;
import static nebula.common.util.Direction.E;
import static nebula.common.util.Direction.N;
import static nebula.common.util.Direction.S;
import static nebula.common.util.Direction.U;
import static nebula.common.util.Direction.W;

import farcore.data.EnumBlock;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import nebula.common.data.Misc;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Worlds;
import nebula.common.world.IModifiableCoord;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
class FireLocationInfo implements IModifiableCoord
{
	int				range;
	World			world;
	BlockPos		pos;
	MutableBlockPos	pos1	= new MutableBlockPos();
	
	int x, y, z;
	
	/**
	 * The information for fire update. The elementary list length is 13. 0 for
	 * boolean type prop. 1-6 for spread speed 7-12 for flammability.
	 * <table BORDER CELLPADDING=8 CELLSPACING=1>
	 * <caption>Data format</caption>
	 *  <tr>
	 *    <td ALIGN=CENTER><b>Index</b></td>
	 *    <td ALIGN=CENTER COLSPAN=8><b>Name</b></td>
	 *  </tr>
	 *  <tr>
	 *    <td>0</td>
	 *    <td>isFire(0)</td>
	 *    <td>isBlock(1)</td>
	 *    <td>isCustomed(2)</td>
	 *    <td>&ltempty&gt(3-7)</td>
	 *    <td>isFlammable(8-13)</td>
	 *    <td>&ltempty&gt(14-15)</td>
	 *    <td>isFireSource(16-21)</td>
	 *    <td>&ltempty&gt(22-32)</td>
	 *   </tr>
	 *   <tr>
	 *     <td>1</td>
	 *     <td COLSPAN=8 ROWSPAN=6>Spread Speed(int for 6 dirs)</td>
	 *   </tr>
	 *   <tr>
	 *     <td>2</td>
	 *   </tr>
	 *   <tr>
	 *     <td>3</td>
	 *   </tr>
	 *   <tr>
	 *     <td>4</td>
	 *   </tr>
	 *   <tr>
	 *     <td>5</td>
	 *   </tr>
	 *   <tr>
	 *     <td>6</td>
	 *   </tr>
	 *   <tr>
	 *     <td>7</td>
	 *     <td COLSPAN=8 ROWSPAN=6>Flammability(int for 6 dirs)</td>
	 *   </tr>
	 *   <tr>
	 *     <td>8</td>
	 *   </tr>
	 *   <tr>
	 *     <td>9</td>
	 *   </tr>
	 *   <tr>
	 *     <td>10</td>
	 *   </tr>
	 *   <tr>
	 *     <td>11</td>
	 *   </tr>
	 *   <tr>
	 *     <td>12</td>
	 *   </tr>
	 * </table>
	 */
	int[][][][] values;
	
	FireLocationInfo(int range, World world, BlockPos pos)
	{
		this.range = range;
		this.world = world;
		this.pos = pos;
		final int r = 2 * range + 1;
		this.values = new int[r][r][r][];
		
		resetMainPos();
	}
	
	FireLocationInfo resetMainPos()
	{
		return setMainPos(this.pos);
	}
	
	FireLocationInfo setMainPos(BlockPos pos)
	{
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		return this;
	}
	
	@Override
	public World world()
	{
		return this.world;
	}
	
	@Override
	public BlockPos pos()
	{
		return this.pos1.setPos(this.x, this.y, this.z);
	}
	
	void setToAir(BlockPos pos)
	{
		setBlockState(pos, Misc.AIR, 3);
	}
	
	public boolean setBlockState(BlockPos pos, IBlockState state, int flag)
	{
		return setBlockState(pos.getX() - this.x, pos.getY() - this.y, pos.getZ() - this.z, state, flag);
	}
	
	public boolean setBlockState(IBlockState state, int flag)
	{
		return setBlockState(0, 0, 0, state, 3);
	}
	
	public boolean setBlockState(Direction offset, IBlockState state, int flag)
	{
		return setBlockState(offset.x, offset.y, offset.z, state, flag);
	}
	
	public boolean setBlockState(int x, int y, int z, IBlockState state, int flag)
	{
		if (this.world.setBlockState(this.pos1.setPos(this.x + x, this.y + y, this.z + z), state, flag))
		{
			x += this.x - this.pos.getX();
			y += this.y - this.pos.getY();
			z += this.z - this.pos.getZ();
			if (inRange((byte) x, (byte) y, (byte) z))
			{
				int[] list = this.values[x + this.range][y + this.range][z + this.range];
				if (list != null)
				{
					refresh(list, state);
				}
			}
			return true;
		}
		return false;
	}
	
	int getSpreadSpeed(BlockPos pos)
	{
		return getSpreadSpeed(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
	}
	
	int getSpreadSpeed(int ofX, int ofY, int ofZ)
	{
		int value = 0;
		value = Math.max(value, value((byte) ofX, (byte) (ofY + 1), (byte) ofZ, (byte) 0x1));
		value = Math.max(value, value((byte) ofX, (byte) (ofY - 1), (byte) ofZ, (byte) 0x2));
		value = Math.max(value, value((byte) ofX, (byte) ofY, (byte) (ofZ + 1), (byte) 0x3));
		value = Math.max(value, value((byte) ofX, (byte) ofY, (byte) (ofZ - 1), (byte) 0x4));
		value = Math.max(value, value((byte) (ofX + 1), (byte) ofY, (byte) ofZ, (byte) 0x5));
		value = Math.max(value, value((byte) (ofX - 1), (byte) ofY, (byte) ofZ, (byte) 0x6));
		return value;
	}
	
	int getFlammability(BlockPos pos)
	{
		return getFlammability(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
	}
	
	int getFlammability(int ofX, int ofY, int ofZ)
	{
		int value = 0;
		value = Math.max(value, getFlammability(ofX, ofY + 1, ofZ, D));
		value = Math.max(value, getFlammability(ofX, ofY - 1, ofZ, U));
		value = Math.max(value, getFlammability(ofX, ofY, ofZ + 1, N));
		value = Math.max(value, getFlammability(ofX, ofY, ofZ - 1, S));
		value = Math.max(value, getFlammability(ofX + 1, ofY, ofZ, W));
		value = Math.max(value, getFlammability(ofX - 1, ofY, ofZ, E));
		return value;
	}
	
	int getFlammability(int ofX, int ofY, int ofZ, Direction facing)
	{
		return value((byte) ofX, (byte) ofY, (byte) ofZ, (byte) (0x7 + facing.ordinal()));
	}
	
	boolean isAir(BlockPos pos)
	{
		return isAir(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
	}
	
	boolean isAir(int ofX, int ofY, int ofZ)
	{
		return value((byte) ofX, (byte) ofY, (byte) ofZ, (byte) 0x0) == 0;
	}
	
	boolean isFire(BlockPos pos)
	{
		return isFire(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
	}
	
	boolean isFire(int ofX, int ofY, int ofZ)
	{
		return value((byte) ofX, (byte) ofY, (byte) ofZ, (byte) 0x0) == 1;
	}
	
	boolean isFlammable(BlockPos pos, EnumFacing facing)
	{
		return isFlammable(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ(), facing.ordinal());
	}
	
	boolean isFlammable(int ofX, int ofY, int ofZ, int facing)
	{
		return (value((byte) ofX, (byte) ofY, (byte) ofZ, (byte) 0x0) & (1 << (facing + 8))) != 0;
	}
	
	boolean isCustomed(BlockPos pos)
	{
		return isCustomed(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
	}
	
	boolean isCustomed(int ofX, int ofY, int ofZ)
	{
		return (value((byte) ofX, (byte) ofY, (byte) ofZ, (byte) 0x0) & 0x4) != 0;
	}
	
	boolean isFireSource(BlockPos pos, EnumFacing facing)
	{
		return isFireSource(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ(), facing.ordinal());
	}
	
	boolean isFireSource(int ofX, int ofY, int ofZ, int facing)
	{
		return (value((byte) ofX, (byte) ofY, (byte) ofZ, (byte) 0x0) & (1 << (facing + 16))) != 0;
	}
	
	boolean canBlockStay(BlockPos pos)
	{
		return canBlockStay(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
	}
	
	boolean canBlockStay(int ofX, int ofY, int ofZ)
	{
		return (value((byte) (ofX + 1), (byte) ofY, (byte) ofZ, (byte) 0x0) & 0x2) == 0 || (value((byte) (ofX - 1), (byte) ofY, (byte) ofZ, (byte) 0x0) & 0x2) == 0 || (value((byte) ofX, (byte) (ofY + 1), (byte) ofZ, (byte) 0x0) & 0x2) == 0
				|| (value((byte) ofX, (byte) (ofY - 1), (byte) ofZ, (byte) 0x0) & 0x2) == 0 || (value((byte) ofX, (byte) ofY, (byte) (ofZ + 1), (byte) 0x0) & 0x2) == 0 || (value((byte) ofX, (byte) ofY, (byte) (ofZ - 1), (byte) 0x0) & 0x2) == 0;
	}
	
	private boolean inRange(byte x, byte y, byte z)
	{
		return L.inRange(this.range, -this.range, x) && L.inRange(this.range, -this.range, y) && L.inRange(this.range, -this.range, z);
	}
	
	private int value(byte i, byte j, byte k, byte type)
	{
		int[] list = this.values[i + this.range][j + this.range][k + this.range];
		if (list != null) return list[type];
		this.values[i + this.range][j + this.range][k + this.range] = list = new int[13];
		IBlockState state = this.world.getBlockState(this.pos1.setPos(this.pos.getX() + i, this.pos.getY() + j, this.pos.getZ() + k));
		refresh(list, state);
		return list[type];
	}
	
	private void refresh(int[] list, IBlockState state)
	{
		if (state.getBlock().isAir(state, this.world, this.pos1))
		{
			list[0] = 0;
			return;
		}
		else if (state.getBlock() == EnumBlock.fire.block)
		{
			list[0] = 1;
			return;
		}
		list[0] = state.getBlock() instanceof IThermalCustomBehaviorBlock ? 6 : 2;
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			boolean isCatchingRaining = Worlds.isCatchingRain(this.world, this.pos1, true);
			if (((list[0] & 4) != 0 && ((IThermalCustomBehaviorBlock) state.getBlock()).canFireBurnOn(this.world, this.pos1, facing, isCatchingRaining)) || state.getBlock().isFlammable(this.world, this.pos1, facing))
			{
				list[0] |= 1 << (8 + facing.ordinal());
			}
			else if (state.getBlock().isFireSource(this.world, this.pos1, facing))
			{
				list[0] |= 1 << (16 + facing.ordinal());
			}
			list[7 + facing.ordinal()] = state.getBlock().getFlammability(this.world, this.pos1, facing);
			list[1 + facing.ordinal()] = state.getBlock().getFireSpreadSpeed(this.world, this.pos1, facing);
		}
	}
}
