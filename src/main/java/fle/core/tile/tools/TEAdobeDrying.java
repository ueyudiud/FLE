/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.tools;

import java.util.List;

import fle.loader.IBFS;
import nebula.base.ObjArrayParseHelper;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.TEAged;
import nebula.common.util.Direction;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author ueyudiud
 */
public class TEAdobeDrying extends TEAged implements ITP_BoundingBox, ITB_BlockPlacedBy, ITP_Drops, ITB_BlockActived
{
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25F, 0.0F, 0.125F, 0.75F, 0.375F, 0.875F);
	
	public int	duration;
	int			wetness;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setByte("duration", (byte) this.duration);
		nbt.setByte("wetness", (byte) this.wetness);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.duration = nbt.getByte("duration");
		this.wetness = nbt.getByte("wetness");
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setByte("d", (byte) this.duration);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.duration = nbt.getByte("d");
	}
	
	@Override
	protected void updateServer1()
	{
		if (isCatchingRain(true))
		{
			if (this.wetness < 100) this.wetness++;
		}
		else if (this.wetness > 0)
		{
			this.wetness--;
		}
		else if (this.duration < 10)
		{
			this.duration++;
			syncToNearby();
		}
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		NBTTagCompound compound = ItemStacks.getOrSetupNBT(stack, false);
		this.duration = compound.getInteger("duration");
		this.wetness = compound.getInteger("wetness");
		syncToNearby();
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (isServer())
		{
			if (this.wetness > 0)
			{
				player.sendMessage(new TextComponentTranslation("info.adobe.drying.wet"));
			}
			else if (this.duration < 5)
			{
				player.sendMessage(new TextComponentTranslation("info.adobe.drying.notdried"));
			}
			else if (this.duration < 10)
			{
				player.sendMessage(new TextComponentTranslation("info.adobe.drying.quitedried"));
			}
			else
			{
				player.sendMessage(new TextComponentTranslation("info.adobe.drying.dried"));
			}
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		if (this.duration == 10)
		{
			return ObjArrayParseHelper.newArrayList(IBFS.iResources.getSubItem("dried_filled_wooden_brick_mold"));
		}
		ItemStack stack = IBFS.iResources.getSubItem("filled_wooden_brick_mold");
		if (this.duration > 0 || this.wetness > 0)
		{
			NBTTagCompound compound = ItemStacks.getOrSetupNBT(stack, true);
			NBTs.setRemovableNumber(compound, "duration", this.duration);
			NBTs.setRemovableNumber(compound, "wetness", this.wetness);
		}
		return ObjArrayParseHelper.newArrayList(stack);
	}
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		return AABB;
	}
}
