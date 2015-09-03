package fle.api.te;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.block.IFacingBlock;
import fle.api.enums.EnumWorldNBT;
import fle.api.net.FleAbstractPacket;
import fle.api.world.BlockPos;

public class TEBase extends TileEntity implements ITEInWorld, IFacingBlock
{
	protected final Random rand = new Random();
	protected ForgeDirection dir;
	
	public void setDirction(ForgeDirection aDirection)
	{
		dir = aDirection;
	}
	
	public TEBase() 
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		dir = ForgeDirection.values()[nbt.getByte("BlockFacing")];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("BlockFacing", (byte) (dir == null ? ForgeDirection.UNKNOWN.ordinal() : dir.ordinal()));
	}
	
	@Override
	public TileEntity getTileEntity() 
	{
		return this;
	}

	@Override
	public int getLightValue() 
	{
		return (int) (16 * worldObj.getLightBrightness(xCoord, yCoord, zCoord));
	}

	@Override
	public boolean isRedStoneEmmit() 
	{
		return worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) != 0;
	}

	@Override
	public World getWorldObj() 
	{
		return worldObj;
	}

	@Override
	public BlockPos getBlockPos() 
	{
		return new BlockPos(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public ForgeDirection getDirction(BlockPos pos) 
	{
		if(dir == ForgeDirection.UNKNOWN || dir == null)
			dir = ForgeDirection.VALID_DIRECTIONS[FLE.fle.getWorldManager().getData(pos, EnumWorldNBT.Facing)];
		return dir;
	}

	@Override
	public boolean canSetDirection(BlockPos pos, ItemStack tool, float xPos,
			float yPos, float zPos) 
	{
		return false;
	}
	

	@Override
	public void setDirection(World world, BlockPos pos, ItemStack tool,
			float xPos, float yPos, float zPos) 
	{
		
	}

	@Override
	public boolean isCatchRain() 
	{
		if(worldObj.isRaining())
		{
			for(int i = yCoord + 1; i < 256; ++i)
			{
				if(worldObj.getBlock(xCoord, yCoord, zCoord).isLeaves(worldObj, xCoord, i, zCoord) || worldObj.getBlock(xCoord, i, zCoord).isSideSolid(worldObj, xCoord, i, zCoord, ForgeDirection.UP))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public void sendToNearBy(FleAbstractPacket packet, float range)
	{
		FleAPI.mod.getNetworkHandler().sendToNearBy(packet, new TargetPoint(worldObj.provider.dimensionId, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, range));
	}
}