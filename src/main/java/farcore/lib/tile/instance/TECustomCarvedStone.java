package farcore.lib.tile.instance;

import farcore.data.M;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.material.Mat;
import farcore.lib.net.tile.PacketTEAsk;
import farcore.lib.tile.TEStatic;
import farcore.lib.util.Direction;
import farcore.util.U.L;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECustomCarvedStone extends TEStatic
{
	private static final long EMPTY = ~0L;
	private static final float BLOCK_SCALE = 0.25F;
	private static final long[] X_LAYER = {0x1111111111111111L, 0x2222222222222222L, 0x4444444444444444L, 0x8888888888888888L};
	private static final long[] Y_LAYER = {0x000F000F000F000FL, 0x00F000F000F000F0L, 0x0F000F000F000F00L, 0xF000F000F000F000L};
	private static final long[] Z_LAYER = {0x000000000000FFFFL, 0x00000000FFFF0000L, 0x00FFFF0000000000L, 0xFFFF000000000000L};
	private Mat rock = M.stone;
	public RockType type = RockType.resource;
	private boolean modified = true;
	private AxisAlignedBB box = null;

	private long carvedState= 0x0;
	
	public Mat rock()
	{
		if(!initialized && worldObj.isRemote)
		{
			sendToServer(new PacketTEAsk(worldObj, pos));
		}
		return rock;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("rock", rock.name);
		compound.setString("type", type.name());
		compound.setLong("carve", carvedState);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		rock = Mat.register.get(compound.getString("rock"), M.stone);
		type = RockType.valueOf(compound.getString("type"));
		carvedState = compound.getLong("carve");
	}

	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setString("r", rock.name);
		nbt.setString("t", type.name());
		nbt.setLong("c", carvedState);
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		rock = Mat.register.get(nbt.getString("r"), M.stone);
		type = RockType.valueOf(nbt.getString("t"));
		long state = nbt.getLong("c");
		if(state != carvedState)
		{
			carvedState = state;
			modified = true;
		}
	}

	private int index(int x, int y, int z)
	{
		return z << 4 | y << 2 | + x;
	}
	
	public boolean isCarved(int x, int y, int z)
	{
		return (carvedState & (1L << index(x, y, z))) != 0;
	}
	
	public float carveRock(EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		double vx = hitX - (player.posX - pos.getX());
		double vy = hitY - (player.posY + player.eyeHeight - pos.getY());
		double vz = hitZ - (player.posZ - pos.getZ());
		double l = Math.sqrt(vx * vx + vy * vy + vz * vz) * 64;
		vx /= l;
		vy /= l;
		vz /= l;
		double vx1 = hitX == 1.0 ? 0.9999 : hitX;
		double vy1 = hitY == 1.0 ? 0.9999 : hitY;
		double vz1 = hitZ == 1.0 ? 0.9999 : hitZ;
		while(L.inRange(1.0, 0.0, vx1) && L.inRange(1.0, 0.0, vy1) && L.inRange(1.0, 0.0, vz1))
		{
			int x = (int) (vx1 * 4);
			int y = (int) (vy1 * 4);
			int z = (int) (vz1 * 4);
			if(!isCarved(x, y, z))
			{
				carveRockUnmark(x, y, z);
				if(isEmpty())
				{
					removeBlock();
				}
				else
				{
					markBlockUpdate();
					syncToNearby();
				}
				return rock.blockHardness / 64F;
			}
			vx1 += vx;
			vy1 += vy;
			vz1 += vz;
		}
		return 0F;
	}
	
	protected void carveRockUnmark(int x, int y, int z)
	{
		carvedState |= (1L << index(x, y, z));
		box = null;
		modified = true;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideRender(int x, int y, int z, Direction facing)
	{
		if(isCarved(x, y, z))
			return false;
		if(x == 3 && facing == Direction.E)
			return !worldObj.isSideSolid(pos.east(), EnumFacing.WEST);
		if(y == 3 && facing == Direction.U)
			return !worldObj.isSideSolid(pos.up(), EnumFacing.DOWN);
		if(z == 3 && facing == Direction.S)
			return !worldObj.isSideSolid(pos.south(), EnumFacing.NORTH);
		if(x == 0 && facing == Direction.W)
			return !worldObj.isSideSolid(pos.west(), EnumFacing.EAST);
		if(y == 0 && facing == Direction.D)
			return !worldObj.isSideSolid(pos.down(), EnumFacing.UP);
		if(z == 0 && facing == Direction.N)
			return !worldObj.isSideSolid(pos.north(), EnumFacing.SOUTH);
		return isCarved(x + facing.x, y + facing.y, z + facing.z);
	}
	
	public boolean isFullCube()
	{
		return carvedState == 0;
	}
	
	public boolean isEmpty()
	{
		return carvedState == EMPTY;
	}

	public boolean isSideSolid(EnumFacing facing)
	{
		int i, j, k;
		switch (facing)
		{
		case UP : return (~carvedState & Y_LAYER[3]) == 0;
		case DOWN : return (~carvedState & Y_LAYER[0]) == 0;
		case SOUTH : return (~carvedState & Z_LAYER[3]) == 0;
		case NORTH : return (~carvedState & Z_LAYER[0]) == 0;
		case EAST : return (~carvedState & X_LAYER[3]) == 0;
		case WEST : return (~carvedState & X_LAYER[0]) == 0;
		}
		return true;
	}
	
	public AxisAlignedBB getCollisionBoundingBox()
	{
		if(modified)
		{
			long negS = ~carvedState;
			int minX = 0;
			int minY = 0;
			int minZ = 0;
			int maxX = 4;
			int maxY = 4;
			int maxZ = 4;
			for(int i = 0; i < 4; ++i)
			{
				if((negS & X_LAYER[i]) != 0L)
				{
					minX = i;
					break;
				}
			}
			for(int i = 4; i > 0; --i)
			{
				if((negS & X_LAYER[i - 1]) != 0L)
				{
					maxX = i;
					break;
				}
			}
			for(int i = 0; i < 4; ++i)
			{
				if((negS & Y_LAYER[i]) != 0L)
				{
					minY = i;
					break;
				}
			}
			for(int i = 4; i > 0; --i)
			{
				if((negS & Y_LAYER[i - 1]) != 0L)
				{
					maxY = i;
					break;
				}
			}
			for(int i = 0; i < 4; ++i)
			{
				if((negS & Z_LAYER[i]) != 0L)
				{
					minZ = i;
					break;
				}
			}
			for(int i = 4; i > 0; --i)
			{
				if((negS & Z_LAYER[i - 1]) != 0L)
				{
					maxZ = i;
					break;
				}
			}
			modified = false;
			box = new AxisAlignedBB(minX * BLOCK_SCALE, minY * BLOCK_SCALE, minZ * BLOCK_SCALE, maxX * BLOCK_SCALE, maxY * BLOCK_SCALE, maxZ * BLOCK_SCALE);
		}
		return box;
	}
}