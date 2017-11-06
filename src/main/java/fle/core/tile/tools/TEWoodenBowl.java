/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile.tools;

import java.util.List;

import farcore.data.EnumBlock;
import farcore.data.EnumFluid;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.capability.IFluidHandler;
import fle.api.tile.ILogProductionCollector;
import fle.api.util.DrinkableFluidHandler;
import fle.api.util.DrinkableFluidHandler.DrinkableFluidEntry;
import fle.core.items.ItemSimpleFluidContainer;
import fle.loader.IBFS;
import nebula.base.ObjArrayParseHelper;
import nebula.common.fluid.FluidTankN;
import nebula.common.foodstat.FoodStatExt;
import nebula.common.tile.IFluidHandlerIO;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.TESingleTank;
import nebula.common.util.Direction;
import nebula.common.util.Players;
import nebula.common.util.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class TEWoodenBowl extends TESingleTank
implements ITP_Drops, IFluidHandler, IFluidHandlerIO, ITB_BlockPlacedBy, ILogProductionCollector,
ITP_BoundingBox, ITB_BlockActived
{
	private static final AxisAlignedBB AABB_BOWL = new AxisAlignedBB(0.25F, 0F, 0.25F, 0.75F, 0.25F, 0.75F);
	
	private int damage;
	private FluidTankN tank = new FluidTankN(250).enableTemperature();
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		return AABB_BOWL;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("damage", (short) this.damage);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.damage = nbt.getShort("damage");
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if (Worlds.isCatchingRain(this.world, this.pos) && (this.tank.getFluid() == null || this.tank.getFluid().getFluid() == EnumFluid.water.fluid))
		{
			this.tank.fill(EnumFluid.water.stack(1, (int) ThermalNet.getTemperature(this)), true);
			syncToNearby();
		}
		if (this.tank.getTemperature() >= 473.0F)
		{
			setBlockState(EnumBlock.fire.apply(), 3);
		}
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side,
			float hitX, float hitY, float hitZ)
	{
		if (hand == EnumHand.MAIN_HAND && stack == null && this.tank.hasFluid())
		{
			if (isServer())
			{
				FoodStatExt stat = Players.getFoodStat(player);
				DrinkableFluidEntry entry = DrinkableFluidHandler.getEntry(this.tank.getFluid());
				if (stat.needWater() && entry != null && this.tank.getFluidAmount() >= entry.amount1)
				{
					stat.addWaterStats(entry.amount2);
					this.tank.drain(entry.amount1, true);
					syncToNearby();
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return super.onBlockActivated(player, hand, stack, side, hitX, hitY, hitZ);
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		this.tank.setFluid(ItemSimpleFluidContainer.getFluid(stack));
		this.damage = ItemSimpleFluidContainer.getCustomDamage(stack);
		syncToNearby();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		ItemStack stack = IBFS.iFluidContainer.getSubItem("bowl_wooden");
		ItemSimpleFluidContainer.setFluid(stack, this.tank.getFluid());
		ItemSimpleFluidContainer.setCustomDamage(stack, this.damage);
		return ObjArrayParseHelper.newArrayList(stack);
	}
	
	@Override
	protected boolean canAccessFluidHandlerFrom(EnumFacing facing)
	{
		return facing == EnumFacing.UP;
	}
	
	@Override
	public boolean canExtractFluid(Direction to)
	{
		return to == Direction.U;
	}
	
	@Override
	public boolean canInsertFluid(Direction from, FluidStack stack)
	{
		return from == Direction.U;
	}
	
	@Override
	public FluidTankN getTank()
	{
		return this.tank;
	}
	
	@Override
	public boolean collectLogProductFrom(Direction direction, FluidStack stack)
	{
		if (this.tank.canInsertFluid(direction, stack))
		{
			this.tank.fill(stack, true);
			syncToNearby();
			return true;
		}
		return false;
	}
}