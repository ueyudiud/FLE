package fle.core.tile.tools;

import java.util.ArrayList;
import java.util.List;

import farcore.data.EnumToolTypes;
import farcore.lib.material.Mat;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Light;
import nebula.common.tile.IToolableTile;
import nebula.common.tile.TE04Synchronization;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;

public class TEOilLamp extends TE04Synchronization implements ITP_Light, IToolableTile, ITP_BoundingBox, ITP_Drops
{
	public static final AxisAlignedBB AABB_OIL_LAMP = new AxisAlignedBB(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.25F, 0.6875F);
	
	public static final byte	HasWick		= 0x10;
	public static final byte	Burning		= 0x11;
	public static final byte	HasSmoke	= 0x12;
	
	public Mat	material;
	public long	fuelAmount;
	public byte	fuelLightValue;
	public long	fuelBurnTime;
	public long	fuelTotalBurnTime;
	
	public TEOilLamp()
	{
	}
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		return AABB_OIL_LAMP;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.material = Mat.material(nbt.getString("material"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("material", this.material.name);
		return super.writeToNBT(nbt);
	}
	
	@Override
	protected void updateClient()
	{
		super.updateClient();
		if (is(Burning) && is(HasSmoke))
		{
			if (this.random.nextInt(4) == 0)
			{
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX() + .5, this.pos.getY() + .8, this.pos.getZ() + .5, 0F, 0.01F, 0F);
			}
		}
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		burnFuel();
	}
	
	protected void burnFuel()
	{
		if (is(Burning))
		{
			if (!is(HasWick))
			{
				disable(Burning);
				return;
			}
			if (this.fuelBurnTime <= 0)
			{
				if (this.fuelAmount > 0)
				{
					this.fuelAmount--;
					this.fuelBurnTime = this.fuelTotalBurnTime;
				}
				else
				{
					disable(Burning);
					return;
				}
			}
			--this.fuelBurnTime;
		}
	}
	
	@Override
	public int getLightValue(IBlockState state)
	{
		return is(Burning) ? this.fuelLightValue : 0;
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tool == EnumToolTypes.FIRESTARTER)
		{
			if (is(HasWick) && !is(Burning))
			{
				change(Burning);
				return new ActionResult<>(EnumActionResult.SUCCESS, 0.2F);
			}
			else
				return new ActionResult<>(EnumActionResult.FAIL, 0F);
		}
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		return new ArrayList<>();
	}
}
