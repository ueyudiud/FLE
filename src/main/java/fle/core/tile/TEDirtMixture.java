/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile;

import java.util.List;

import farcore.data.M;
import farcore.data.MC;
import farcore.data.SubTags;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import fle.api.mat.StackContainer;
import fle.api.recipes.instance.RecipeMaps;
import nebula.base.ArrayListAddWithCheck;
import nebula.base.Stack;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.IUpdatableTile;
import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TEDirtMixture extends TESynchronization implements ITP_Drops, ITB_BlockActived, ITP_BlockHardness, ITP_ExplosionResistance, IUpdatableTile
{
	//	private long				duration;
	//	private long				mixture;
	private StackContainer<Mat>	stacks	= new StackContainer<>(5184);
	
	public StackContainer<Mat> getStacks()
	{
		return this.stacks;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		this.stacks.writeToNBT(nbt, "stacks", Mat.WITH_NULL_RW);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.stacks.readFromNBT(nbt, "stacks", Mat.WITH_NULL_RW);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		this.stacks.writeToNBT(nbt, "st", Mat.WITH_NULL_RW);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.stacks.readFromNBT(nbt, "st", Mat.WITH_NULL_RW);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		List<ItemStack> result = ArrayListAddWithCheck.requireNonnull();
		ItemStack stack = RecipeMaps.DIRT_MIXTURE_OUTPUT.findRecipe(this.stacks);
		if (stack != null)
		{
			result.add(stack);
		}
		else
		{
			for (Stack<Mat> s : this.stacks.getList())
			{
				if (s.element.contain(SubTags.DIRT))
				{
					if (s.size > MC.pile.size)
					{
						result.add(ItemMulti.createStack(s.element, MC.pile, (int) (s.size / MC.pile.size)));
					}
				}
				else if (s.element.contain(SubTags.CROP) || s.element.contain(SubTags.PLANT) || s.element.contain(SubTags.VINES))
				{
					// TODO Drop mixed plant ball.
				}
			}
		}
		return result;
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		//		int lv = ItemStacks.getToolLevel(stack, EnumToolTypes.SHOVEL);
		return EnumActionResult.PASS;
	}
	
	@Override
	public void onBlockBreak(IBlockState state)
	{
		// Stack<Mat> stack = stacks.drain(M.water);
		// TODO
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return 0.5F;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return 0.25F;
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		if (!tileUpdate)
		{
			checkStay();
		}
	}
	
	private boolean checkStay()
	{
		if (Worlds.isAir(this.world, this.pos.east()) || Worlds.isAir(this.world, this.pos.west()) || Worlds.isAir(this.world, this.pos.north()) || Worlds.isAir(this.world, this.pos.south()) || !Worlds.isSideSolid(this.world, this.pos, EnumFacing.UP, false))
		{
			removeBlock();
			return false;
		}
		return true;
	}
	
	@Override
	protected void updateServer()
	{
		if (!checkStay()) return;
		if (!this.stacks.isFull())
		{
			List<EntityItem> list = getEntitiesWithinAABB(EntityItem.class);
			for (EntityItem item : list)
			{
				ItemStack stack = item.getEntityItem();
				if (stack != null)
				{
					List<Stack<Mat>> l1 = RecipeMaps.DIRT_MIXTURE_INPUT.findRecipe(stack);
					if (l1 != null)
					{
						for (Stack<Mat> s : l1)
						{
							this.stacks.fill(s.element, s.size * stack.stackSize);
						}
						item.setDead();
						syncToNearby();
					}
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public int getGroundRGB()
	{
		long total = 0;
		long r = 0, g = 0, b = 0;
		for (Stack<Mat> stack : this.stacks.getList())
		{
			if (stack.element.contain(SubTags.DIRT))
			{
				r += stack.size * stack.element.RGBa[0];
				g += stack.size * stack.element.RGBa[1];
				b += stack.size * stack.element.RGBa[2];
				total += stack.size;
			}
		}
		if (total == 0) return M.latrosol.RGB;
		return L.index24i((int) (r / total), (int) (g / total), (int) (b / total));
	}
	
	@SideOnly(Side.CLIENT)
	public double getGroundPercentage()
	{
		long total = 0;
		for (Stack<Mat> stack : this.stacks.getList())
		{
			if (stack.element.contain(SubTags.DIRT) || stack.element.contain(SubTags.PLANT) || stack.element.contain(SubTags.CROP) || stack.element.contain(SubTags.VINES))
			{
				total += stack.size;
			}
		}
		return (double) total / (double) this.stacks.getCapacity();
	}
}
