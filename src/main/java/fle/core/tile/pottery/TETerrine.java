/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.tile.pottery;

import static nebula.common.inventory.InventoryHelper.FD_DRAIN;
import static nebula.common.inventory.InventoryHelper.FD_FILL_ONLYFULL;

import fle.core.client.gui.pottery.GuiTerrine;
import fle.core.common.gui.pottery.ContainerTerrine;
import nebula.common.fluid.FluidTankN;
import nebula.common.inventory.InventoryHelper;
import nebula.common.tile.IGuiTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import nebula.common.tile.TEInventoryTankSingleAbstract;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.Explosion;

/**
 * @author ueyudiud
 */
public class TETerrine extends TEInventoryTankSingleAbstract
implements ITP_BlockHardness, ITP_HarvestCheck, ITP_ExplosionResistance, ITB_BlockActived, IGuiTile
{
	private FluidTankN tank = new FluidTankN(1500).enableTemperature();
	
	public TETerrine()
	{
		super(2);
	}
	
	@Override
	public String getName()
	{
		return "inventory.terrine";
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return 2.0F;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return 7.5F;
	}
	
	@Override
	public boolean canHarvestBlock(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side,
			float hitX, float hitY, float hitZ)
	{
		if (hand == EnumHand.MAIN_HAND && InventoryHelper.drainOrFillTank(this.tank, player, hand, side, stack, (byte) (FD_FILL_ONLYFULL | FD_DRAIN)))
		{
			return EnumActionResult.SUCCESS;
		}
		else
		{
			if (tileGUICheck(hand))
			{
				openGUI(player, 0);
			}
			return EnumActionResult.SUCCESS;
		}
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		InventoryHelper.drainOrFillTank(this, this.tank, 0, 1, (byte) (FD_FILL_ONLYFULL | FD_DRAIN));
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	protected FluidTankN tank()
	{
		return this.tank;
	}
	
	public FluidTankN getTank()
	{
		return this.tank;
	}
	
	@Override
	public Container openContainer(int id, EntityPlayer player)
	{
		return new ContainerTerrine(this, player);
	}
	
	@Override
	public GuiContainer openGui(int id, EntityPlayer player)
	{
		return new GuiTerrine(this, player);
	}
}