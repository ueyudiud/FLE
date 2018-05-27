/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.tile.wooden;

import java.io.IOException;

import farcore.lib.container.Container04Solid;
import farcore.lib.inventory.SolidContainerSingle;
import farcore.lib.inventory.SolidContainers;
import farcore.lib.solid.SolidSlot;
import farcore.lib.solid.container.SolidContainerHelper;
import fle.api.recipes.IRecipeMap;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.api.recipes.instance.RecipeMaps;
import fle.api.tile.TE08Recipe;
import nebula.base.function.F;
import nebula.base.function.Judgable;
import nebula.client.gui.GuiBackground;
import nebula.client.gui.GuiContainer02TE;
import nebula.common.gui.FluidSlot;
import nebula.common.gui.ISlotInitalizer;
import nebula.common.gui.ItemSlot;
import nebula.common.gui.ItemSlotOutput;
import nebula.common.inventory.FluidContainerSingle;
import nebula.common.inventory.FluidContainers;
import nebula.common.inventory.IContainer;
import nebula.common.inventory.ItemContainersArray;
import nebula.common.inventory.task.Task;
import nebula.common.inventory.task.TaskBuilder;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
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
@GuiBackground("fle:textures/gui/stone_mill.png")
public class TEStoneMill extends TE08Recipe<ItemStack, TemplateRecipeCache<ItemStack>>
implements ITB_BlockActived, INetworkedSyncTile, ITP_BlockHardness, ITP_ExplosionResistance
{
	private int			buffer;
	@SideOnly(Side.CLIENT)
	private int			angle;
	
	public TEStoneMill()
	{
		this.items = new ItemContainersArray(3, 64);
		this.fluids = new FluidContainers<>(new FluidContainerSingle(4000));
		this.solids = new SolidContainers<>(new SolidContainerSingle(1000));
	}
	
	@Override
	public String getName()
	{
		return "inventory.stone.mill";
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return 2.0F;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return 4.0F;
	}
	
	private final Task.TaskBTB taskFillOrDrain = provideSolidFillOrDrainTask(1, 2, 0, 1000, false, true, true);
	
	@Override
	protected void updateServer()
	{
		this.taskFillOrDrain.invoke();
		super.updateServer();
		if (this.buffer > 0)
		{
			this.buffer--;
		}
	}
	
	@Override
	protected void updateClient()
	{
		super.updateClient();
		if (this.buffer > 0)
		{
			this.buffer--;
			this.angle++;
			if (this.angle >= 80)
			{
				this.angle = 0;
			}
		}
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tileGUICheck(hand))
		{
			if (side.horizontal)
			{
				onRotateMill(player);
				markTileUpdate(0);
			}
			else
			{
				openGUI(player, 0);
				syncToPlayer(player);
			}
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public void writeNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		buf.writeInt(this.buffer);
	}
	
	@Override
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		this.buffer = buf.readInt();
	}
	
	public void onRotateMill(EntityPlayer player)
	{
		this.buffer = 10;
		if (isServer())
		{
			player.addExhaustion(0.0025F);
		}
	}
	
	@Override
	protected ItemStack getRecipeInputHandler()
	{
		return getStackInSlot(0);
	}
	
	@Override
	protected IRecipeMap<?, TemplateRecipeCache<ItemStack>, ItemStack> getRecipeMap()
	{
		return RecipeMaps.STONE_MILL;
	}
	
	@Override
	protected void onRecipeInput()
	{
		decrStackSize(0, this.cache.get1(0));
		this.recipeMaxTick = this.cache.get(0);
	}
	
	@Override
	protected boolean onRecipeOutput()
	{
		return TaskBuilder.builder()
				.add(this.solids.getContainer(0).taskIncr(this.cache.get(1), IContainer.PROCESS))
				.add(this.fluids.getContainer(0).taskIncr(this.cache.get(2), IContainer.PROCESS))
				.build().run();
	}
	
	@Override
	protected int getPower()
	{
		return this.buffer > 0 ? 1 : 0;
	}
	
	@SideOnly(Side.CLIENT)
	public float getRotationAngle()
	{
		return 360.0F * this.angle / 80.0F;
	}
	
	@Override
	public void initalizeContainer(Container04Solid container, ISlotInitalizer initalizer)
	{
		initalizer.addSlot("input", new ItemSlot(this.items.getContainer(0), this, 0, 65, 20).setPredicate(((Judgable<Object>) F.P_ANY).from(RecipeMaps.STONE_MILL::findRecipe)))
		.addLocation("player", false);
		initalizer.addSlot("fillin", new ItemSlot(this.items.getContainer(1), this, 1, 55, 52).setPredicate(((Judgable<Object>) F.P_ANY).from(SolidContainerHelper::getSolidFromItemStack)))
		.addLocation("player", false);
		initalizer.addSlot("fillout", new ItemSlotOutput(this.items.getContainer(2), this, 2, 91, 52))
		.addLocation("player", false);
		initalizer.straegyPlayerBag().addLocation("fillin", false).addLocation("input", false).addLocation("hand", false);
		initalizer.straegyPlayerHand().addLocation("fillin", false).addLocation("input", false).addLocation("bag", false);
		initalizer.addSlot(new FluidSlot(this.fluids.getContainer(0), 117, 48, 8, 20).setRenderHorizontal());
		initalizer.addSlot(new SolidSlot(this.solids.getContainer(0), 73, 52, 16, 16));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackgroundSecondLayer(GuiContainer02TE<?> gui, int x, int y, float partialTicks, int mouseX, int mouseY)
	{
		if (this.buffer > 0)
		{
			gui.drawProgressScaleUTD(x + 82, y + 17, 176, 0, 19, 21, this.buffer, 20);
		}
		if (isWorking())
		{
			gui.drawProgressScaleLTR(x + 48, y + 39, 0, 166, 66, 9, getRecipeTick(), getMaxRecipeTick());
		}
	}
}
