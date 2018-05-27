/*
 * copyright 2016-2018 ueyudiud
 */

package fle.core.tile.kinetic;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.M;
import farcore.lib.material.Mat;
import fle.api.tile.kinetic.TEGearBase;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class TEGear extends TEGearBase implements ITB_BlockPlacedBy, ITP_Drops
{
	public static enum GearSize
	{
		SMALL_4(0.25F, 4),
		SMALL_8(0.25F, 8),
		SMALL_16(0.25F, 16),
		SMALL_24(0.25F, 24),
		SMALL_32(0.25F, 32),
		MID_8(0.5F, 8),
		MID_16(0.5F, 16),
		MID_24(0.5F, 24),
		MID_32(0.5F, 32),
		MID_48(0.5F, 48),
		MID_64(0.5F, 64),
		MID_96(0.5F, 96),
		BIG_12(0.75F, 12),
		BIG_24(0.75F, 32),
		BIG_48(0.75F, 48),
		BIG_64(0.75F, 64),
		BIG_96(0.75F, 96),
		BIG_128(0.75F, 128);
		
		final float	gearSize;
		final float	gearTeethSize;
		final int	gearTeethCount;
		
		GearSize(float s, int c)
		{
			this.gearSize = s - 1E-5F;
			this.gearTeethSize = s + s / c;
			this.gearTeethCount = c;
		}
	}
	
	public GearSize	size		= GearSize.MID_8;
	public Mat		material	= M.oak;
	
	public TEGear()
	{
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTs.setEnum(nbt, "size", this.size);
		nbt.setString("material", this.material.name);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.size = NBTs.getEnumOrDefault(nbt, "size", this.size);
		this.material = Mat.getMaterialByNameOrDefault(nbt, "material", this.material);
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		this.size = GearSize.values()[stack.getItemDamage()];
		this.material = Mat.getMaterialFromStack(stack, "material", this.material);
		this.axis = facing.axis;
		syncToNearby();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, this.size.ordinal());
		Mat.setMaterialToStack(stack, "material", this.material);
		return ImmutableList.of(stack);
	}
	
	@Override
	public float getGearSize(Direction direction)
	{
		return this.size.gearSize;
	}
	
	@Override
	public float getGearTeethSize(Direction direction)
	{
		return this.size.gearTeethSize;
	}
	
	@Override
	public int getGearTeethCount(Direction direction)
	{
		return this.size.gearTeethCount;
	}
	
	@Override
	protected boolean isSideRotatable(Direction direction)
	{
		return true;
	}
}
