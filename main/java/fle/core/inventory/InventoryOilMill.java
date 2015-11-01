package fle.core.inventory;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.gui.GuiError;
import fle.api.inventory.InventoryWithFluidTank;
import fle.api.recipe.IRecipeHandler.RecipeKey;
import fle.core.net.FleTEPacket;
import fle.core.recipe.FLEOilMillRecipe;
import fle.core.recipe.FLEOilMillRecipe.OilMillKey;
import fle.core.recipe.FLEOilMillRecipe.OilMillRecipe;
import fle.core.recipe.RecipeHelper;
import fle.core.te.TileEntityOilMill;

public class InventoryOilMill extends InventoryWithFluidTank<TileEntityOilMill>
{
	private static final int[] a = {0};
	
	private int cache;
	private int recipeTime;
	private RecipeKey key;
	
	public InventoryOilMill()
	{
		super("inventory.oil.mill", 2, 2000);
		maxHeat = 600;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		recipeTime = nbt.getShort("RecipeTime");
		key = FLEOilMillRecipe.getInstance().getRecipeKey(nbt.getString("Recipe"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("RecipeTime", (short) recipeTime);
		if(key != null)
			nbt.setString("Recipe", key.toString());
	}
	
	@Override
	public void updateEntity(TileEntityOilMill tile)
	{
		super.updateEntity(tile);
		if(cache > 0)
		{
			--cache;
			if(key == null)
			{
				OilMillRecipe recipe = FLEOilMillRecipe.getInstance().getRecipe(new OilMillKey(stacks[0]));
				if(recipe != null)
				{
					RecipeHelper.onInputItemStack(this, 0);
					key = recipe.getRecipeKey();
					tile.type = GuiError.DEFAULT;
				}
				else
				{
					tile.type = GuiError.CAN_NOT_INPUT;
				}
			}
			if(key != null)
			{
				++recipeTime;
				if(recipeTime > 200)
				{
					OilMillRecipe recipe = FLEOilMillRecipe.getInstance().getRecipe(key);
					if(recipe != null)
					{
						if(RecipeHelper.matchOutput(this, 1, recipe.output1) && RecipeHelper.matchOutFluidStack(tank, recipe.output2))
						{
							RecipeHelper.onOutputItemStack(this, 1, recipe.getRandOutput());
							RecipeHelper.onOutputFluidStack(tank, recipe.output2);
							tile.type = GuiError.DEFAULT;
							recipeTime = 0;
							key = null;
						}
						else
						{
							tile.type = GuiError.CAN_NOT_OUTPUT;
							recipeTime = 200;
						}
					}
					else
					{
						key = null;
						recipeTime = 0;
					}
				}
			}
			syncTank(tile);
			syncSlot(tile);
		}
		tile.sendToNearBy(new FleTEPacket(tile, (byte) 0), 16.0F);
	}
	
	public void onWork()
	{
		if(cache < 40)
			cache += 40;
	}

	@Override
	protected void onMelted(TileEntityOilMill tile)
	{
		tile.getWorldObj().removeTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
		tile.getWorldObj().setBlock(tile.xCoord, tile.yCoord, tile.zCoord, Blocks.fire);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP ? a : null;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aSlotID == 0;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aSlotID == 1;
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return from != ForgeDirection.UP && from != ForgeDirection.DOWN;
	}
	
	@SideOnly(Side.CLIENT)
	public int getRecipeProgress(int i)
	{
		return (int) ((float) (recipeTime * i) / 200F);
	}

	@SideOnly(Side.CLIENT)
	public int getCache(int i)
	{
		return (int) ((float) (cache * i) / 40F);
	}

	@SideOnly(Side.CLIENT)
	public void syncRecipeTime(int i)
	{
		recipeTime = i;
	}

	public int getRecipeTime()
	{
		return recipeTime;
	}
}