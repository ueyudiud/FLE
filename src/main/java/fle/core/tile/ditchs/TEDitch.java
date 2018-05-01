/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.ditchs;

import farcore.lib.material.Mat;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.util.Direction;
import nebula.common.util.FluidStacks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class TEDitch extends TEDitchBase implements ITB_BlockPlacedBy, ITP_BlockHardness
{
	private Mat material;
	
	public TEDitch()
	{
		
	}
	
	public TEDitch(Mat material)
	{
		super(null);
		setMaterial(material);
	}
	
	@Override
	public void setMaterial(Mat material)
	{
		this.material = material;
		super.setMaterial(material);
	}
	
	@Override
	public Mat getMaterial()
	{
		return this.material;
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		setMaterial(Mat.material(stack.getItemDamage()));
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return 0.5F;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player)
	{
		return player.getDigSpeed(state, this.pos) / getBlockHardness(state) / 30F;
	}
	
	public int fill(Direction direction, FluidStack resource, boolean process)
	{
		if (isLinked(direction))
		{
			int amount = this.tank.fill(resource, process);
			if (process && direction.horizontal)
			{
				this.flowAmount[direction.horizontalOrdinal] -= amount;
			}
			return amount;
		}
		else
		{
			return 0;
		}
	}
	
	public FluidStack drain(Direction direction, FluidStack required, boolean process)
	{
		if (isLinked(direction))
		{
			FluidStack stack = this.tank.drain(required, process);
			if (process && direction.horizontal)
			{
				this.flowAmount[direction.horizontalOrdinal] += FluidStacks.getAmount(stack);
			}
			return stack;
		}
		else
		{
			return null;
		}
	}
	
	public FluidStack drain(Direction direction, int maxAmount, boolean process)
	{
		if (isLinked(direction))
		{
			FluidStack stack = this.tank.drain(maxAmount, process);
			if (process && direction.horizontal)
			{
				this.flowAmount[direction.horizontalOrdinal] += FluidStacks.getAmount(stack);
			}
			return stack;
		}
		else
		{
			return null;
		}
	}
}
