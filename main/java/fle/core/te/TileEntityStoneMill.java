package fle.core.te;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.energy.IRotationTileEntity;
import flapi.energy.RotationNet.RotationPacket;
import flapi.gui.GuiCondition;
import flapi.gui.GuiError;
import flapi.recipe.IRecipeHandler.RecipeKey;
import flapi.solid.ISolidHandler;
import flapi.solid.Solid;
import flapi.solid.SolidStack;
import flapi.solid.SolidTank;
import flapi.te.TEIFS;
import flapi.world.BlockPos;
import fle.core.energy.RotationTileHelper;
import fle.core.init.Lang;
import fle.core.recipe.FLEStoneMillRecipe;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipe;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipeKey;
import fle.core.recipe.RecipeHelper;
import fle.core.recipe.RecipeHelper.FDType;

public class TileEntityStoneMill extends TEIFS implements IRotationTileEntity
{
	private RotationTileHelper rh = new RotationTileHelper(1024D, 16D);
	public GuiCondition type;
	private int recipeTick;
	private RecipeKey recipe;
	private int maxRecipeTick;

	public TileEntityStoneMill()
	{
		super(3, 1000, 2000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		rh.readFromNBT(nbt);
		recipe = FLEStoneMillRecipe.getInstance().getRecipeKey(nbt.getString("Recipe"));
		if(recipe != null)
		{
			maxRecipeTick = ((StoneMillRecipeKey) recipe).getRecipeTick();
			recipeTick = nbt.getInteger("Tick");
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		rh.writeToNBT(nbt);
		if(recipe != null)
		{
			nbt.setString("Recipe", recipe.toString());
			nbt.setInteger("Tick", recipeTick);
		}
	}
	
	private static ForgeDirection[] dirs = {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};

	@Override
	public void update() 
	{
		rh.update();
		double energy = rh.getRotationEnergy();
		if(energy > 200)
		{
			++tick;
			onWork((int) Math.log(energy / 200D + Math.E));
			rh.minusInnerEnergy(200.0D);
			markRenderForUpdate();
		}
		if(energy > 0)
		{
			rh.minusInnerEnergy(1.0D);
		}
		if(isClient()) return;
		RecipeHelper.fillOrDrainInventoryTank(this, sTank, 1, 2, FDType.F);
		BlockPos tPos = getBlockPos();
		for(ForgeDirection dir : dirs)
		{
			if(sTank.getStack() != null)
			{
				if(tPos.toPos(dir).getBlockTile() instanceof ISolidHandler)
				{
					ISolidHandler sh = (ISolidHandler) tPos.toPos(dir).getBlockTile();
					if(sh.canFillS(dir.getOpposite(), sTank.get()))
					{
						int i = sh.fillS(dir.getOpposite(), sTank.getStack(), false);
						SolidStack aStack = sTank.drain(i, true);
						sh.fillS(dir.getOpposite(), aStack, true);
					}
				}
				else if(tPos.toPos(dir).toPos(ForgeDirection.DOWN).getBlockTile() instanceof ISolidHandler)
				{
					ISolidHandler sh = (ISolidHandler) tPos.toPos(dir).toPos(ForgeDirection.DOWN).getBlockTile();
					if(sh.canFillS(ForgeDirection.UP, sTank.get()))
					{
						int i = sh.fillS(ForgeDirection.UP, sTank.getStack(), false);
						SolidStack aStack = sTank.drain(i, true);
						sh.fillS(ForgeDirection.UP, aStack, true);
					}
				}
			}
			else break;
		}
	}
	
	public void onWork(int speed)
	{
		if(recipe == null)
		{
			recipeTick = 0;
			StoneMillRecipe str = FLEStoneMillRecipe.getInstance().getRecipe(new StoneMillRecipeKey(stacks[0]));
			if(str != null)
			{
				RecipeHelper.onInputItemStack(this, 0);
				recipe = str.getRecipeKey();
				maxRecipeTick = ((StoneMillRecipeKey) recipe).getRecipeTick();
				type = GuiError.DEFAULT;
			}
			else
			{
				type = GuiError.CAN_NOT_INPUT;
			}
		}
		if(recipe != null && !isClient())
		{
			if(maxRecipeTick == -1)
			{
				recipe = null;
				return;
			}
			recipeTick += speed;
			if(recipeTick >= maxRecipeTick)
			{
				StoneMillRecipe r = FLEStoneMillRecipe.getInstance().getRecipe(recipe);
				if(r == null)
				{
					recipeTick = 0;
					recipe = null;
					return;
				}
				SolidStack output = r.getOutput();
				FluidStack output1 = r.getFluidOutput();
				if(RecipeHelper.matchOutSolidStack(sTank, output) && RecipeHelper.matchOutFluidStack(fTank, output1))
				{
					RecipeHelper.onOutputSolidStack(sTank, output);
					RecipeHelper.onOutputFluidStack(fTank, output1);
					recipe = null;
					recipeTick = 0;
					type = GuiError.DEFAULT;
				}
				else
				{
					recipeTick = ((StoneMillRecipeKey) recipe).getRecipeTick();
					type = GuiError.CAN_NOT_OUTPUT;
				}
			}
		}
	}
	
	public void onPower()
	{
		rh.reseaveRotation(new RotationPacket(2048, 0.25));
	}
	
	int tick;
	
	@SideOnly(Side.CLIENT)
	public double getRotation()
	{
		return (tick % 100) * Math.PI / 100D;
	}
	
	@SideOnly(Side.CLIENT)
	public double getEnergyContain()
	{
		return Math.min(rh.getRotationEnergy(), 1024D) / 1024D;
	}

	@SideOnly(Side.CLIENT)
	public double getProgressBar(int i)
	{
		return (int) (maxRecipeTick == 0 ? 0D : (double) recipeTick / (double) maxRecipeTick * i);
	}
	
	@Override
	protected SolidTank getTankFromSide(ForgeDirection side)
	{
		for(ForgeDirection dir : dirs) if(dir == side) return sTank;
		return null;
	}

	@Override
	public boolean canFillS(ForgeDirection from, Solid Solid)
	{
		return false;
	}

	@Override
	public double getEnergyCurrect()
	{
		return rh.getRotationEnergy();
	}

	@Override
	public int getPreEnergyEmit()
	{
		return (int) (rh.getShowSpeedConduct() * rh.getShowTouqueConduct());
	}

	@Override
	public boolean canReciveEnergy(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP;
	}

	@Override
	public boolean canEmitEnergy(ForgeDirection dir)
	{
		return false;
	}

	@Override
	public int getStuck(RotationPacket packet, ForgeDirection dir)
	{
		return rh.getStuck(packet);
	}

	@Override
	public void onRotationEmit(RotationPacket packet, ForgeDirection dir)
	{
		rh.emitRotation(packet);
	}

	@Override
	public void onRotationReceive(RotationPacket packet, ForgeDirection dir)
	{
		rh.reseaveRotation(packet);
	}

	@Override
	public void onRotationStuck(int stuck)
	{
		type = GuiError.ROTATION_STUCK;
	}
	
	public RotationTileHelper getRotationHelper()
	{
		return rh;
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_stoneMill;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return isInputSlot(aSlotID);
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return isOutputSlot(aSlotID);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP ? new int[]{0} : dir != ForgeDirection.DOWN ? new int[]{1, 2, 3} : null;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return isInputSlot(i);
	}

	protected boolean isInputSlot(int i)
	{
		return i == 0;
	}

	protected boolean isOutputSlot(int i)
	{
		return i != 0;
	}
	
	@Override
	public int getProgressSize()
	{
		return 2;
	}
	
	@Override
	public int getProgress(int id)
	{
		return id == 0 ? recipeTick : id == 1 ? maxRecipeTick : 0;
	}
	
	@Override
	public void setProgress(int id, int value)
	{
		if(id == 0) recipeTick = value;
		if(id == 1) maxRecipeTick = value;
	}
}