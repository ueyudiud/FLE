/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.rocky;

import static fle.api.recipes.instance.RecipeMaps.CERAMICPOT_ADD_TO_MIX;
import static fle.api.recipes.instance.RecipeMaps.CERAMICPOT_BASE;
import static fle.api.recipes.instance.RecipeMaps.TAG_CERAMICPOT_BASE_INPUT1;
import static fle.api.recipes.instance.RecipeMaps.TAG_CERAMICPOT_BASE_INPUT2;
import static fle.api.recipes.instance.RecipeMaps.TAG_CERAMICPOT_BASE_INPUT3;

import farcore.data.M;
import farcore.energy.thermal.IThermalHandler;
import farcore.energy.thermal.IThermalProvider;
import farcore.energy.thermal.ThermalNet;
import farcore.energy.thermal.instance.ThermalHandlerSimple;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.container.Container04Solid;
import farcore.lib.material.Mat;
import farcore.lib.solid.container.SolidContainerHelper;
import fle.api.recipes.SingleInputMatch;
import fle.api.recipes.TemplateRecipeMap;
import fle.api.recipes.instance.interfaces.IRecipeInput;
import fle.api.tile.TE08Recipe;
import nebula.base.Ety;
import nebula.base.function.F;
import nebula.base.function.Judgable;
import nebula.client.gui.GuiBackground;
import nebula.client.gui.GuiContainer02TE;
import nebula.common.fluid.container.FluidContainerHandler;
import nebula.common.gui.FluidSlot;
import nebula.common.gui.ISlotInitalizer;
import nebula.common.gui.ItemSlot;
import nebula.common.gui.ItemSlotOutput;
import nebula.common.inventory.FluidContainersArray;
import nebula.common.inventory.IContainer;
import nebula.common.inventory.InventoryHelper;
import nebula.common.inventory.ItemContainersArray;
import nebula.common.inventory.task.Task;
import nebula.common.inventory.task.TaskBuilder;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@GuiBackground("fle:textures/gui/boiling_heater.png")
public class TECeramicPot extends TE08Recipe<IRecipeInput, TemplateRecipeMap.TemplateRecipeCache<IRecipeInput>> implements IThermalProvider, IRecipeInput, ITB_BlockActived
{
	public static final Mat material = M.argil;
	
	private ThermalHandlerSimple handler = new ThermalHandlerSimple(this);
	
	{
		this.handler.material = material;
		this.items = new ItemContainersArray(6, 64);
		this.fluids = new FluidContainersArray(1, 4000);
	}
	
	@Override
	protected void initServer()
	{
		super.initServer();
		FarCoreEnergyHandler.onAddFromWorld(this);
	}
	
	@Override
	public void onRemoveFromLoadedWorld()
	{
		super.onRemoveFromLoadedWorld();
		FarCoreEnergyHandler.onRemoveFromWorld(this);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		this.handler.writeToNBT(compound);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.handler.readFromNBT(compound);
	}
	
	@Override
	public String getName()
	{
		return "inventory.ceramic.pot";
	}
	
	@Override
	protected TemplateRecipeMap<IRecipeInput> getRecipeMap()
	{
		return CERAMICPOT_BASE;
	}
	
	@Override
	protected IRecipeInput getRecipeInputHandler()
	{
		return this;
	}
	
	@Override
	protected int getPower()
	{
		return this.cache.<Integer> get(0) <= ThermalNet.getTemperature(this.world, this.pos, false) ? 1 : 0;
	}
	
	@Override
	public <T> T getRecipeInput(String name)
	{
		switch (name)
		{
		case TAG_CERAMICPOT_BASE_INPUT1:
			return (T) this.items.getStackInContainer(0);
		case TAG_CERAMICPOT_BASE_INPUT2:
			return (T) this.fluids.getContainer(0).getStackInContainer();
		case TAG_CERAMICPOT_BASE_INPUT3:
			return (T) this.items.getStackInContainer(5);
		default:
			return null;
		}
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tileGUICheck(hand))
		{
			openGUI(player, 0);
			return EnumActionResult.SUCCESS;
		}
		return super.onBlockActivated(player, hand, stack, side, hitX, hitY, hitZ);
	}
	
	private final Task.TaskBTB taskFillOrDrain = InventoryHelper.taskFillOrDrain(((ItemContainersArray) this.items).stacks, 2, 3, ((FluidContainersArray) this.fluids).stacks, 0, 4000, true, true, false);
	
	@Override
	protected void updateServer()
	{
		if (!this.taskFillOrDrain.invoke())
		{
			TemplateRecipeMap.TemplateRecipeCache<?> cache = CERAMICPOT_ADD_TO_MIX.findRecipe(
					new Ety(SolidContainerHelper.getSolidFromItemStack(this.items.getStackInContainer(0)), this.fluids.getContainer(0).getStackInContainer()));
			if (cache != null)
			{
				if (SolidContainerHelper.drainFromItem(this.items.getContainer(2), this.items.getContainer(3), cache.get(0)))
				{
					this.fluids.getContainer(0).setStackInContainer(cache.get(1));
				}
			}
		}
		super.updateServer();
	}
	
	@Override
	protected void onRecipeInput()
	{
		decrStackSize(0, this.cache.get1(0));
		this.fluids.getContainer(0).decrStack(this.cache.get1(1), IContainer.PROCESS);
		this.recipeMaxTick = this.cache.get(1);
		setInventorySlotContents(4, this.cache.<SingleInputMatch> get1(2).getRemain(getStackInSlot(4)));
	}
	
	@Override
	protected boolean onRecipeOutput()
	{
		ItemStack[] stacks = ((ItemContainersArray) this.items).stacks;
		return TaskBuilder.builder()
				.add(InventoryHelper.taskInsertAll(stacks, 0, this.cache.<ItemStack> get(2), true))
				.add(InventoryHelper.taskInsertAll(stacks, 4, this.cache.<ItemStack> get(3), true))
				.build().run();
	}
	
	@Override
	public IThermalHandler getThermalHandler()
	{
		return this.handler;
	}
	
	@Override
	public void initalizeContainer(Container04Solid container, ISlotInitalizer initalizer)
	{
		initalizer.addSlots("fluidin",
				new ItemSlot(this.items.getContainer(0), this, 0, 76, 32).setPredicate(((Judgable<FluidStack>) F.P_ANY).from(FluidContainerHandler::getContain)))
		.addLocation("player", false);
		initalizer.addSlots("fluidout",
				new ItemSlotOutput(this.items.getContainer(1), this, 1, 94, 32))
		.addLocation("player", false);
		initalizer.addSlots("boilin",
				new ItemSlot(this.items.getContainer(2), this, 2, 38, 19))
		.addLocation("player", false);
		initalizer.addSlots("boilout",
				new ItemSlotOutput(this.items.getContainer(3), this, 3, 38, 44))
		.addLocation("player", false);
		initalizer.addSlots("toolin",
				new ItemSlot(this.items.getContainer(4), this, 4, 177, 19))
		.addLocation("player", false);
		initalizer.addSlots("toolout",
				new ItemSlotOutput(this.items.getContainer(5), this, 5, 177, 44))
		.addLocation("player", false);
		initalizer.straegyPlayerBag().addLocation("fluidin", false).addLocation("boilin", false).addLocation("toolin", false).addLocation("hand", false);
		initalizer.straegyPlayerHand().addLocation("fluidin", false).addLocation("boilin", false).addLocation("toolin", false).addLocation("bag", false);
		
		initalizer.addSlot(new FluidSlot(this.fluids.getContainer(0), 66, 28, 8, 20));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackgroundFirstLayer(GuiContainer02TE<?> gui, int x, int y, float partialTicks, int mouseX, int mouseY)
	{
		gui.drawTexturedModalRect(x + 66, y + 28, 176, 14, 8, 20);
	}
}
