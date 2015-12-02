package fle.core.te.tank;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.te.TileEntityAbstractTank;
import fle.core.inventory.tank.InventoryMultiTank;
import fle.core.net.FleTEPacket;
import fle.core.recipe.FLESoakRecipe;
import fle.core.recipe.FLESoakRecipe.SoakRecipe;
import fle.core.recipe.FLESoakRecipe.SoakRecipeKey;

public class TileEntityMultiTankSoak extends TileEntityMultiTank
{
	private final SoakRecipeKey[] keys = new SoakRecipeKey[4];
	private final int[] recipeTick = new int[4];
	private final int[] targetTick = new int[4];
	
	public TileEntityMultiTankSoak()
	{
		super("MultiTankSoak", 4);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		for(int i = 0; i < 4; ++i)
		{
			if(keys[i] != null)
			{
				nbt.setString("RecipeName_" + Integer.toString(i), keys[i].toString());
				nbt.setInteger("RecipeTick_" + Integer.toString(i), recipeTick[i]);
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		for(int i = 0; i < 4; ++i)
		{
			if(nbt.hasKey("RecipeName_" + Integer.toString(i)))
			{
				keys[i] = (SoakRecipeKey) FLESoakRecipe.getInstance().getRecipeKey(nbt.getString("RecipeName_" + Integer.toString(i)));
				if(keys[i] != null)
				{
					recipeTick[i] = nbt.getInteger("RecipeTick_" + Integer.toString(i));
					targetTick[i] = keys[i].getRecipeTick(getStackInSlot(i));
				}
			}
		}
	}
	
	@Override
	protected boolean canBeMainTile()
	{
		return false;
	}
	
	@Override
	protected boolean canConnectWith(TileEntity tile)
	{
		return tile instanceof TileEntityMultiTank;
	}
	
	@Override
	protected void updateInventory()
	{
		super.updateInventory();
		if(worldObj.isRemote)
		{
			for(int i = 0; i < 4; ++i)
			{
				if(targetTick[i] > 0)
					++recipeTick[i];
			}
			return;
		}
		if(mainTile != null)
		{
			for(int i = 0; i < 4; ++i)
			{
				if(keys[i] == null)
				{
					if(getStackInSlot(i) != null)
					{
						SoakRecipe recipe = FLESoakRecipe.getInstance().getRecipe(new SoakRecipeKey(getMainTank().getFluid(), getStackInSlot(i)));
						if(recipe != null)
						{
							keys[i] = recipe.getRecipeKey();
							targetTick[i] = keys[i].getRecipeTick(getStackInSlot(i));
							sendToNearBy(new FleTEPacket(this, (byte) 3), 64.0F);
						}
					}
				}
				else
				{
					if(!keys[i].match(getMainTank().getFluid(), getStackInSlot(i)))
					{
						keys[i] = null;
						recipeTick[i] = 0;
						targetTick[i] = 0;
						sendToNearBy(new FleTEPacket(this, (byte) 2), 64.0F);
						sendToNearBy(new FleTEPacket(this, (byte) 3), 64.0F);
						return;
					}
					++recipeTick[i];
					if(recipeTick[i] >= targetTick[i])
					{
						SoakRecipe recipe = FLESoakRecipe.getInstance().getRecipe(keys[i]);
						getMainTank().drain(keys[i].getFluidUse(getStackInSlot(i)), true);
						ItemStack output = recipe.getOutput(getStackInSlot(i));
						if(output != null)
							setInventorySlotContents(i, output.copy());
						recipeTick[i] = 0;
						targetTick[i] = 0;
						keys[i] = null;
						getTileInventory().syncSlot(this);
						syncFluidTank();
						sendToNearBy(new FleTEPacket(this, (byte) 2), 64.0F);
						sendToNearBy(new FleTEPacket(this, (byte) 3), 64.0F);
					}
				}
			}
		}
	}
	
	@Override
	public InventoryMultiTank getTileInventory()
	{
		return getThisInventory();
	}
	
	@Override
	public boolean canBeConnect(TileEntityAbstractTank main, int xPos,
			int yPos, int zPos, int width, int height)
	{
		return xPos == 0 ? zPos != 0 && zPos != width - 1 : 
				xPos == width - 1 ? zPos != 0 && zPos != width - 1 : true;
	}
	
	@SideOnly(Side.CLIENT)
	public int getProgress(int i, int length)
	{
		return targetTick[i] == 0 ? 0 : (int) ((float) (length * recipeTick[i]) / (float) targetTick[i]);
	}
	
	@Override
	public Object onEmit(byte aType)
	{
		return aType == 3 ? (long) targetTick[3] << 48 | (long) targetTick[2] << 32 | (long) targetTick[1] << 16 | (long) targetTick[0] :
				aType == 2 ? (long) recipeTick[3] << 48 | (long) recipeTick[2] << 32 | (long) recipeTick[1] << 16 | (long) recipeTick[0] : super.onEmit(aType);
	}
	
	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 2)
		{
			long c = (Long) contain;
			recipeTick[0] = (int) (c & 0xFFFF);
			recipeTick[1] = (int) ((c >> 16) & 0xFFFF);
			recipeTick[2] = (int) ((c >> 32) & 0xFFFF);
			recipeTick[3] = (int) ((c >> 48) & 0xFFFF);
		}
		if(type == 3)
		{
			long c = (Long) contain;
			targetTick[0] = (int) (c & 0xFFFF);
			targetTick[1] = (int) ((c >> 16) & 0xFFFF);
			targetTick[2] = (int) ((c >> 32) & 0xFFFF);
			targetTick[3] = (int) ((c >> 48) & 0xFFFF);
		}
		super.onReceive(type, contain);
	}
}