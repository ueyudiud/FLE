package fle.core.te;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import flapi.FleAPI;
import flapi.enums.EnumDamageResource;
import flapi.recipe.stack.OreStack;
import flapi.te.TEIFluidTank;
import flapi.util.FleValue;
import fle.FLE;
import fle.core.init.Lang;
import fle.core.recipe.BarrelDrinkMixRecipe;
import fle.core.recipe.BarrelDrinkRecipe;
import fle.core.recipe.RecipeHelper;

public class TileEntityBarrel extends TEIFluidTank
{
	public String drinkName;
	public int starchContent;//(C6H10O5)n
	public int glucanGlucohydrolaceLevel;//(C6H10O5)n -> C6H12O6
	public int sugarContent;//C6H12O6
	public int microzymeLevel;//C6H12O6 -> CH3-CH2-OH
	public int alcoholContent;//CH3-CH2-OH
	public int acetaldehydeContent;//CH3-CHO
	public int ethanicAcidContent;//CH3-COOH
	public int waterContent;//H2O
	
	public TileEntityBarrel()
	{
		super(0, 4000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		drinkName = nbt.getString("DrinkName");
		if(drinkName == "") drinkName = null;
		waterContent = nbt.getInteger("H2O");
		starchContent = nbt.getInteger("A");
		glucanGlucohydrolaceLevel = nbt.getInteger("B");
		sugarContent = nbt.getInteger("C");
		microzymeLevel = nbt.getInteger("D");
		alcoholContent = nbt.getInteger("E");
		acetaldehydeContent = nbt.getInteger("F");
		ethanicAcidContent = nbt.getInteger("G");
		if(should(OPEN))
			nbt.setBoolean("Open", true);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(drinkName != null)
		{
			nbt.setString("DrinkName", drinkName);
		}
		nbt.setInteger("H2O", waterContent);
		nbt.setInteger("A", starchContent);
		nbt.setInteger("B", glucanGlucohydrolaceLevel);
		nbt.setInteger("C", sugarContent);
		nbt.setInteger("D", microzymeLevel);
		nbt.setInteger("E", alcoholContent);
		nbt.setInteger("F", acetaldehydeContent);
		nbt.setInteger("G", ethanicAcidContent);
		if(nbt.getBoolean("Open"))
			enable(OPEN);
	}	
	
	int tempCache;
	int buf;
	
	@Override
	protected void update()
	{
		if(worldObj.isRemote) return;
		tempCache = FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
		if(buf++ < 50) return;
		if(canFermen())
		{
			buf = 0;
			int h = -(tempCache - (FleValue.WATER_FREEZE_POINT + 35)) * (tempCache - (FleValue.WATER_FREEZE_POINT + 15));
			int h1 = -(tempCache - (FleValue.WATER_FREEZE_POINT + 55)) * (tempCache - (FleValue.WATER_FREEZE_POINT - 5));
			int a = getAmount();
			if(h1 > 0)
			{
				int t0 = (int) (2 * (0.5F - (float) sugarContent / (float) a) * h * (starchContent + glucanGlucohydrolaceLevel) * (rand.nextFloat() + 1F) / 20000F);
				int t1 = Math.min(t0, starchContent);
				int t2 = (int) (4 * (0.25F - (float) alcoholContent / (float) a) * h * (sugarContent + microzymeLevel) * (rand.nextFloat() + 1F) / 20000F);
				int t3 = Math.min(t2, sugarContent);
				int t4 = (int) (h1 * starchContent * ((float) (starchContent - glucanGlucohydrolaceLevel) / (float) a) * rand.nextFloat() / (glucanGlucohydrolaceLevel * 100F));
				int t5 = (int) (h1 * sugarContent * ((float) (sugarContent - microzymeLevel) / (float) a) * rand.nextFloat() / (microzymeLevel * 100F));
				int t6 = (int) (h1 * glucanGlucohydrolaceLevel * ((float) sugarContent / (float) a) * (rand.nextFloat() + 1F) / 200000F);
				int t7 = (int) (h1 * microzymeLevel * ((float) alcoholContent / (float) a) * (rand.nextFloat() + 1F) / 200000F);
				if(h > 0)
				{
					if(starchContent > 0 && glucanGlucohydrolaceLevel > 0 && t1 > 0)
					{
						starchContent -= t1;
						sugarContent += t1;
					}
					if(sugarContent > 0 && microzymeLevel > 0)
					{
						sugarContent -= t3;
						alcoholContent += t3;
						if(rand.nextInt(10) == 0)
							acetaldehydeContent += rand.nextInt(Math.abs(t2) / 10 + 1);
					}
				}
				if(glucanGlucohydrolaceLevel > 0)
					if(t4 >= 0)
						glucanGlucohydrolaceLevel += (rand.nextInt(t4 + 1) - rand.nextInt(t6 + 1)) / 4;
					else
						glucanGlucohydrolaceLevel -= rand.nextInt(t6 + 1) / 4;
				if(microzymeLevel > 0)
					if(t5 >= 0)
						microzymeLevel += (rand.nextInt(t5 + 1) - rand.nextInt(t7 + 1)) / 4;
					else
						microzymeLevel -= rand.nextInt(t7 + 1) / 4;
			}
			else
			{
				if(glucanGlucohydrolaceLevel > 0) glucanGlucohydrolaceLevel -= rand.nextInt(21) / 20;
				else glucanGlucohydrolaceLevel = 0;
				if(microzymeLevel > 0) microzymeLevel -= rand.nextInt(21) / 20;
				else microzymeLevel = 0;
			}
		}
	}
	
	private static final OreStack softHammer = new OreStack("craftingToolSoftHammer");
	
	public void onUseItemStack(EntityPlayer player, ItemStack stack)
	{
		if(stack == null || softHammer.equal(stack))
		{
			FleAPI.damageItem(player, stack, EnumDamageResource.UseTool, 0.25F);
			setCondition(player);
			return;
		}
		else if(should(OPEN))
		{
			if(FluidContainerRegistry.isFilledContainer(stack))
			{
				FluidStack stack1 = FluidContainerRegistry.getFluidForFilledItem(stack);
				ItemStack stack2 = FluidContainerRegistry.drainFluidContainer(stack);
				if(fill(stack1, false) != 0)
				{
					fill(stack1, true);
					RecipeHelper.onInputItemStack(player);
					if(RecipeHelper.matchOutput(player.inventory, 0, 36, stack2))
					{
						RecipeHelper.onOutputShapelessStack(player.inventory, 0, 36, stack2);
					}
					else
					{
						player.dropPlayerItemWithRandomChoice(stack2, false);
					}
				}
			}
			else if(FluidContainerRegistry.isEmptyContainer(stack))
			{
				ItemStack stack2 = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), stack);
				if(stack2 != null)
				{
					tank.drain(FluidContainerRegistry.getContainerCapacity(stack2), true);
					RecipeHelper.onInputItemStack(player);
					if(RecipeHelper.matchOutput(player.inventory, 0, 36, stack2))
					{
						RecipeHelper.onOutputShapelessStack(player.inventory, 0, 36, stack2);
					}
					else
					{
						player.dropPlayerItemWithRandomChoice(stack2, false);
					}
					return;
				}
			}
			if(addItemStackIn(stack)) return;
		}
	}
	
	public boolean addItemStackIn(ItemStack stack)
	{
		BarrelDrinkRecipe recipe = BarrelDrinkRecipe.getRecipeInfo(stack);
		if(recipe == null) return false;
		drinkName = drinkName == "" ? recipe.getDrinkName() : 
			!recipe.getDrinkName().equals("unknown") && !recipe.getDrinkName().equals(drinkName) ? "unknown" : drinkName;
		acetaldehydeContent += recipe.getAcetaldehydeContent(stack);
		alcoholContent += recipe.getAlcoholContent(stack);
		glucanGlucohydrolaceLevel += recipe.getGlucanGlucohydrolaceLevel(stack);
		microzymeLevel += recipe.getMicrozymeLevel(stack);
		starchContent += recipe.getStarchContent(stack);
		sugarContent += recipe.getSugarContent(stack);
		stack.stackSize = 0;
		return true;
	}
	
	public void setCondition(EntityPlayer player)
	{
		if(should(OPEN))
		{
			if(tank.getFluidAmount() == 0)
				disable(OPEN);
		}
		else
		{
			enable(OPEN);
			BarrelDrinkMixRecipe recipe = BarrelDrinkMixRecipe.getRecipeInfo(this);
			if(recipe != null)
			{
				tank.fill(recipe.getOutput(getAmount()), true);
				drinkName = null;
				acetaldehydeContent = alcoholContent =
						ethanicAcidContent = glucanGlucohydrolaceLevel = 
						starchContent = sugarContent = 
						waterContent = 0;
			}
			syncFluidTank();
		}
	}
	
	public String getOutputInfo()
	{
		if(getAmount() < 100) return null;
		BarrelDrinkMixRecipe recipe = BarrelDrinkMixRecipe.getRecipeInfo(this);
		return recipe == null || recipe == BarrelDrinkMixRecipe.UNKNOWN ? null : recipe.getInfo();
	}
	
	private boolean canFermen()
	{
		if(should(OPEN)) return false;
		return true;
	}
	
	public int getAmount()
	{
		return waterContent + 
				alcoholContent + 
				acetaldehydeContent + 
				ethanicAcidContent + 
				starchContent + 
				sugarContent;
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_barrel;
	}
	
	@Override
	public int getSizeTank()
	{
		return 2;
	}
	
	@Override
	protected FluidTank getFluidTankFromSide(ForgeDirection side)
	{
		return side == ForgeDirection.UP ? tank : new FluidTank(0);
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return should(OPEN) && from == ForgeDirection.UP && BarrelDrinkRecipe.isFluidAddable(fluid);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if(should(OPEN) && BarrelDrinkRecipe.isFluidAddable(resource.getFluid()))
		{
			return fill(resource, doFill);
		}
		return 0;
	}
	
	private int fill(FluidStack resource, boolean flag)
	{
		BarrelDrinkRecipe recipe = BarrelDrinkRecipe.getRecipeInfo(resource);
		if(recipe == null) return 0;
		int amount = Math.min(32000 - getAmount(), recipe.getAmount(resource)) / recipe.getAmount();
		if(flag && amount > 0)
		{
			FluidStack stack = resource.copy();
			stack.amount = amount;
			drinkName = drinkName == null ? recipe.getDrinkName() : 
				recipe.getDrinkName() != null && !recipe.getDrinkName().equals(drinkName) ? "unknown" : drinkName;
			acetaldehydeContent += recipe.getAcetaldehydeContent(stack);
			alcoholContent += recipe.getAlcoholContent(stack);
			ethanicAcidContent += recipe.getEthanicAcidContent(stack);
			sugarContent += recipe.getSugarContent(stack);
			starchContent += recipe.getStarchContent(stack);
			waterContent += recipe.getWaterContent(stack);
			glucanGlucohydrolaceLevel += recipe.getGlucanGlucohydrolaceLevel(stack);
			microzymeLevel += recipe.getMicrozymeLevel(stack);
		}
		return amount;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return should(OPEN) && from != ForgeDirection.UP;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return should(OPEN);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return new int[]{0, 1, 2, 3};
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return direction == ForgeDirection.UP ? true : false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return false;
	}
	
	private static final int OPEN = 3;
	
	@Override
	public Object onEmit(byte aType)
	{
		if(aType == 2)
		{
			return new int[]
					{
					starchContent,//(C6H10O5)n
					glucanGlucohydrolaceLevel,//(C6H10O5)n -> C6H12O6
					sugarContent,//C6H12O6
					microzymeLevel,//C6H12O6 -> CH3-CH2-OH
					alcoholContent,//CH3-CH2-OH
					acetaldehydeContent,//CH3-CHO
					ethanicAcidContent,//CH3-COOH
					waterContent//H2O
					};
		}
		return super.onEmit(aType);
	}

	public void addDebugInfo(List<String> list)
	{
		list.add("Barrel Info:");
		list.add("Barrel State : " + (should(OPEN) ? "OPEN" : "CLOSED"));
		if(drinkName != null)
			list.add("Drink Type : " + drinkName);
		list.add(new StringBuilder()
		.append(EnumChatFormatting.AQUA).append("W:").append(waterContent)
		.append(EnumChatFormatting.WHITE).append(" |")
		.append(EnumChatFormatting.RED).append(" S:").append(starchContent)
		.append(EnumChatFormatting.WHITE).append(" |")
		.append(EnumChatFormatting.YELLOW).append(" A:").append(alcoholContent)
		.append(EnumChatFormatting.WHITE).append(" |")
		.append(EnumChatFormatting.WHITE).append(" S:").append(sugarContent)
		.append(EnumChatFormatting.WHITE).append(" |")
		.append(EnumChatFormatting.DARK_BLUE).append(" G:").append(glucanGlucohydrolaceLevel)
		.append(EnumChatFormatting.WHITE).append(" |")
		.append(EnumChatFormatting.GREEN).append(" M:").append(microzymeLevel).toString());
		list.add(new StringBuilder()
		.append("Alcohol Contain : ")
		.append(FleValue.format_progress.format_c((float) alcoholContent / (float) getAmount())).toString());
	}
}