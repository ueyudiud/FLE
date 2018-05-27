/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.tile.pottery;

import nebula.client.gui.GuiContainer02TE;
import nebula.common.gui.Container03TileEntity;
import nebula.common.gui.FluidSlot;
import nebula.common.gui.ISlotInitalizer;
import nebula.common.gui.ItemSlot;
import nebula.common.inventory.FluidContainersArray;
import nebula.common.inventory.InventoryHelper;
import nebula.common.inventory.ItemContainersArray;
import nebula.common.inventory.task.Task;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import nebula.common.tile.TE06HasGui;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TETerrine extends TE06HasGui implements ITP_BlockHardness, ITP_HarvestCheck, ITP_ExplosionResistance, ITB_BlockActived
{
	private final Task.TaskBTB taskFillOrDrain;
	
	public TETerrine()
	{
		this.items = new ItemContainersArray(2, 1);
		this.fluids = new FluidContainersArray(1, 1500);
		this.taskFillOrDrain = InventoryHelper.taskFillOrDrain(((ItemContainersArray) this.items).stacks, 0, 1, ((FluidContainersArray) this.fluids).stacks, 0, 1500, true, true, true);
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
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		//		if (hand == EnumHand.MAIN_HAND && InventoryHelper.drainOrFillTank(this.tank, player, hand, side, stack, (byte) (FD_FILL_ONLYFULL | FD_DRAIN)))
		//		{
		//			return EnumActionResult.SUCCESS;
		//		}
		//		else
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
		this.taskFillOrDrain.invoke();
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public void initalizeContainer(Container03TileEntity container, ISlotInitalizer initalizer)
	{
		initalizer.addSlots("store",
				new ItemSlot(this.items.getContainer(0), this, 0, 89, 28),
				new ItemSlot(this.items.getContainer(1), this, 1, 89, 46))
		.addLocation("player", false);
		initalizer.straegyPlayerBag().addLocation("store", false).addLocation("hand", false);
		initalizer.straegyPlayerHand().addLocation("store", false).addLocation("bag", false);
		
		initalizer.addSlot(new FluidSlot(this.fluids.getContainer(0), 75, 32, 8, 30));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackgroundSecondLayer(GuiContainer02TE gui, int x, int y, float partialTicks, int mouseX, int mouseY)
	{
		gui.setZLevel(400.0F);
		gui.drawTexturedModalRect(x + 75, y + 32, 176, 0, 8, 30);
	}
}
