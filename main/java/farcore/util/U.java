package farcore.util;

import flapi.FleAPI;
import flapi.enums.EnumDamageResource;
import flapi.enums.EnumWorldNBT;
import flapi.item.ItemFle;
import flapi.recipe.stack.AbstractStack;
import flapi.util.Compact;
import flapi.world.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.oredict.OreDictionary;

public class U
{
	public static class F
	{
		private static Fluid water;
		private static Fluid lava;
		private static Fluid steam;
		
		public static int id(Fluid fluid)
		{
			return fluid.getID();
		}
		
		public static int id(FluidStack stack)
		{
			return stack == null ? -1 : id(stack.getFluid());
		}
		
		public static boolean equal(FluidStack stack1, FluidStack stack2)
		{
			return equal(stack1, stack2, false);
		}
		
		public static boolean equal(FluidStack stack1, FluidStack stack2, boolean checkNBT)
		{
			return stack1 == stack2 ? true :
				stack1 == null ^ stack2 == null ? false :
					stack1.getFluid() == stack2.getFluid() && (!checkNBT || 
							FluidStack.areFluidStackTagsEqual(stack1, stack2));
		}
		
		public static FluidStack water(int amount)
		{
			if(water == null)
			{
				water = FluidRegistry.getFluid("water");
			}
			return amount == 0 ? null : new FluidStack(water, amount);
		}
		
		public static boolean isWater(Fluid fluid)
		{
			if(water == null)
			{
				water = FluidRegistry.getFluid("water");
			}
			return water == fluid;
		}
		
		public static boolean isWater(FluidStack stack)
		{
			return stack == null ? false : isWater(stack.getFluid());
		}
		
		public static FluidStack lava(int amount)
		{
			if(lava == null)
			{
				lava = FluidRegistry.getFluid("lava");
			}
			return amount == 0 ? null : new FluidStack(lava, amount);
		}
		
		public static boolean isLava(Fluid fluid)
		{
			if(lava == null)
			{
				lava = FluidRegistry.getFluid("lava");
			}
			return lava == fluid;
		}
		
		public static boolean isLava(FluidStack stack)
		{
			return stack == null ? false : isLava(stack.getFluid());
		}
		
		public static FluidStack steam(int amount)
		{
			if(steam == null)
			{
				steam = FluidRegistry.getFluid("steam");
			}
			return amount == 0 ? null : new FluidStack(steam, amount);
		}
		
		public static boolean isSteam(Fluid fluid)
		{
			if(steam == null)
			{
				steam = FluidRegistry.getFluid("steam");
			}
			return steam == fluid;
		}
		
		public static boolean isSteam(FluidStack stack)
		{
			return stack == null ? false : isSteam(stack.getFluid());
		}
		
		public static String name(FluidStack stack)
		{
			return stack == null ? "" : stack.getFluid().getName();
		}
		
		public static FluidStack stack(String fluid, int amount)
		{
			if(fluid == null || fluid.length() == 0) return null;
			Fluid f = FluidRegistry.getFluid(fluid);
			return f == null ? null : new FluidStack(f, amount);
		}
		
		public static boolean contain(FluidStack resource, FluidStack target)
		{
			return target == null ? resource == null :
				resource == null || resource.containsFluid(target);
		}
		
		public static boolean contain(FluidStack resource, Fluid target)
		{
			return resource == null ? false : resource.getFluid() == target;
		}
		
		public static FluidStack copy(FluidStack stack)
		{
			return stack == null ? null : stack.copy();
		}
		
		public static int amount(FluidStack stack)
		{
			return stack == null ? 0 : stack.amount;
		}
		
		public static int temperature(FluidStack stack)
		{
			return stack == null ? 298 : stack.getFluid().getTemperature(stack);
		}
		
		public static Block toBlock(Fluid fluid)
		{
			return fluid.getBlock();
		}
		
		public static FluidStack getContain(ItemStack stack)
		{
			if(stack == null) return null;
			if(stack.getItem() instanceof IFluidContainerItem)
			{
				return ((IFluidContainerItem) stack.getItem()).getFluid(stack);
			}
			return FluidContainerRegistry.getFluidForFilledItem(stack);
		}
		
		public static int getCapacity(ItemStack stack)
		{
			if(stack == null) return 0;
			if(stack.getItem() instanceof IFluidContainerItem)
			{
				return ((IFluidContainerItem) stack.getItem()).getCapacity(stack);
			}
			return FluidContainerRegistry.getContainerCapacity(stack);
		}
		
		public static int getRemainderCapacity(ItemStack stack)
		{
			if(stack == null) return 0;
			if(stack.getItem() instanceof IFluidContainerItem)
			{
				return ((IFluidContainerItem) stack.getItem()).getCapacity(stack) -
						amount(((IFluidContainerItem) stack.getItem()).getFluid(stack));
			}
			return FluidContainerRegistry.getContainerCapacity(stack);
		}
		
		public static FluidStack multiply(FluidStack stack, double scale)
		{
			if(stack != null)
			{
				stack = stack.copy();
				stack.amount *= scale;
			}
			return stack;
		}
	}
	
	public static class I
	{
		public static boolean equal(ItemStack stack1, ItemStack stack2)
		{
			return equal(stack1, stack2, true);
		}
		
		public static boolean equal(ItemStack stack1, ItemStack stack2, boolean checkNBT)
		{
			return equal(stack1, stack2, checkNBT, false);
		}
		
		public static boolean equal(ItemStack stack1, ItemStack stack2, boolean checkNBT, boolean useGeneralMeta)
		{
			return stack1 == stack2 ? true :
				stack1 == null ^ stack2 == null ? false :
					(useGeneralMeta ? OreDictionary.itemMatches(stack1, stack2, false) : stack1.isItemEqual(stack2)) && 
					(!checkNBT || ItemStack.areItemStackTagsEqual(stack1, stack2));
		}
		
		public static ItemStack copy(ItemStack stack)
		{
			return stack == null ? null : stack.copy();
		}
		
		public static ItemStack copyAndValidate(ItemStack stack, boolean oreDictCheck)
		{
			if(stack == null) return null;
			ItemStack ret = stack.copy();
			if(oreDictCheck && ret.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			{
				ret.setItemDamage(0);
			}
			return ret;
		}

		public static ItemStack stack(String name, int size)
		{
			if(OreDictionary.getOres(name).isEmpty()) return null;
			ItemStack stack = OreDictionary.getOres(name).get(0).copy();
			stack.stackSize = size;
			return stack;
		}

		
		/**
		 * Damage item when use it (throwing, using, crafting, etc).
		 * Which compact with some mods (FLE, GT, etc.).
		 * @see {@link net.minecraft.item.ItemStack}
		 * @param player the user of this tool, null means no user.
		 * @param stack the tool which will be damage.
		 * @param resource the damage type of tool.
		 * @param damage the value of damage level.
		 */
		public static void damageItem(EntityLivingBase player, ItemStack stack, EnumDamageResource resource, float damage)
		{
			if(stack == null) return;
			else if(Compact.isFLETool(stack.getItem()))
			{
				((ItemFle) stack.getItem()).damageItem(stack, player, resource, damage);
				return;
			}
			else if(Compact.isGTTool(stack.getItem()))
			{
				Compact.damageGTTool(stack, damage);
				return;
			}
			else
			{
				stack.damageItem((int) Math.ceil(damage), player);
				return;
			}
		}
	}
	
	public static class S
	{
		
	}
	
	public static class B
	{
		
	}
	
	public static class N
	{
		/**
		 * Get direction ordinal, without UNKNOWN and null.
		 * @see net.minecraftforge.common.util.ForgeDirection
		 * @param direction
		 * @return The index of direction.
		 */
		public static int side(ForgeDirection direction)
		{
			if(direction == ForgeDirection.UNKNOWN || direction == null)
				return 3;
			return direction.ordinal();
		}		
	}
	
	public static class R
	{
		
	}
	
	public static class NBT
	{
		
	}
	
	public static class Inventory
	{
		
	}
	
	public static class P
	{
		/**
		 * Check does player has a stack equals to target. 
		 * @param aPlayer
		 * @param aStack target to check.
		 * @return slot ID found, -1 means don't have stack.
		 */
		public static int playerHas(EntityPlayer player, AbstractStack aStack)
		{
			if(player == null) return -1;
			for(int i = 0; i < 36; ++i)
			{
				if(player.inventory.getStackInSlot(i) != null)
				{
					if(aStack.contain(player.inventory.getStackInSlot(i)))
					{
						return i;
					}
				}
			}
			return -1;
		}		
	}
	
	public static class W
	{
		/**
		 * Get entity facing from world.
		 * @param world
		 * @param x
		 * @param y
		 * @param z
		 * @param entity
		 * @return
		 */
		public static ForgeDirection initFacing(EntityLivingBase entity)
		{
			int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
	
			switch(l)
			{
			case 0 : return ForgeDirection.SOUTH;
			case 1 : return ForgeDirection.WEST;
			case 2 : return ForgeDirection.NORTH;
			case 3 : return ForgeDirection.EAST;
			default : return ForgeDirection.UNKNOWN;
			}
		}
		
		/**
		 * Get facing from active position.
		 * @param world
		 * @param x
		 * @param y
		 * @param z
		 * @param xPos
		 * @param yPos
		 * @param zPos
		 * @return
		 */
		public static ForgeDirection initFacing(double xPos, double yPos, double zPos)
		{
			double a = xPos;
			double b = yPos;
			double c = zPos;
			
			ForgeDirection dir = ForgeDirection.UNKNOWN;
			
			if(b == 0.00D) dir = ForgeDirection.DOWN;
			if(a == 0.00D) dir = ForgeDirection.WEST;
			if(c == 0.00D) dir = ForgeDirection.SOUTH;
			if(b == 1.00D) dir = ForgeDirection.UP;
			if(a == 1.00D) dir = ForgeDirection.EAST;
			if(c == 1.00D) dir = ForgeDirection.NORTH;
			return dir;
		}

		public static int fwmMeta(BlockPos pos)
		{
			return FleAPI.mod.getWorldManager().getData(pos, EnumWorldNBT.Metadata);
		}
		
		public static int fwmMeta(IBlockAccess world, int x, int y, int z)
		{
			return fwmMeta(new BlockPos(world, x, y, z));
		}
	}
}