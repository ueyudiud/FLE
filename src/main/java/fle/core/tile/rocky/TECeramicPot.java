/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile.rocky;

import static fle.api.recipes.instance.RecipeMaps.CERAMICPOT_ADD_TO_MIX;
import static fle.api.recipes.instance.RecipeMaps.CERAMICPOT_BASE;
import static fle.api.recipes.instance.RecipeMaps.TAG_CERAMICPOT_BASE_INPUT1;
import static fle.api.recipes.instance.RecipeMaps.TAG_CERAMICPOT_BASE_INPUT2;
import static fle.api.recipes.instance.RecipeMaps.TAG_CERAMICPOT_BASE_INPUT3;

import farcore.data.M;
import farcore.energy.thermal.IThermalHandler;
import farcore.energy.thermal.ThermalNet;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.solid.container.SolidContainerHelper;
import fle.api.energy.thermal.ThermalEnergyHelper;
import fle.api.recipes.SingleInputMatch;
import fle.api.recipes.TemplateRecipeMap;
import fle.api.recipes.instance.interfaces.IRecipeInput;
import fle.api.tile.TEITSRecipe;
import fle.core.client.gui.rocky.GuiCeramicPot;
import fle.core.common.gui.rocky.ContainerCeramicPot;
import nebula.base.Ety;
import nebula.common.fluid.FluidTankN;
import nebula.common.inventory.InventoryHelper;
import nebula.common.tile.IGuiTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.util.Direction;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TECeramicPot extends TEITSRecipe<IRecipeInput, TemplateRecipeMap.TemplateRecipeCache<IRecipeInput>> implements IThermalHandler, IRecipeInput, IGuiTile, ITB_BlockActived
{
	private FluidTankN tank = new FluidTankN(2000).enableTemperature();
	
	private ThermalEnergyHelper helper = new ThermalEnergyHelper(0, M.argil.heatCapacity, 100F, 4.8E-3F);
	
	public TECeramicPot()
	{
		super(6);
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
		this.helper.writeToNBT(compound, "energy");
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.helper.readFromNBT(compound, "energy");
	}
	
	@Override
	public FluidTankN tank()
	{
		return this.tank;
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
		return this.cache.<Integer> get(0) <= ThermalNet.getTemperature(this) ? 1 : 0;
	}
	
	@Override
	public <T> T getRecipeInput(String name)
	{
		switch (name)
		{
		case TAG_CERAMICPOT_BASE_INPUT1:
			return (T) this.stacks[0];
		case TAG_CERAMICPOT_BASE_INPUT2:
			return (T) this.tank.getFluid();
		case TAG_CERAMICPOT_BASE_INPUT3:
			return (T) this.stacks[5];
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
	
	@Override
	protected void updateServer()
	{
		if (!InventoryHelper.drainOrFillTank(this, this, 2, 3, InventoryHelper.FD_FILL_ANY_DRAIN))
		{
			TemplateRecipeMap.TemplateRecipeCache<?> cache = CERAMICPOT_ADD_TO_MIX.findRecipe(new Ety(SolidContainerHelper.getSolidFromItemStack(this.stacks[0]), this.tank.getFluid()));
			if (cache != null)
			{
				if (SolidContainerHelper.drainFromItem(this, cache.get(0), 2, 3))
				{
					this.tank.setFluid(cache.get(1));
				}
			}
		}
		super.updateServer();
	}
	
	@Override
	protected void onRecipeInput()
	{
		decrStackSize(0, this.cache.get1(0));
		this.tank.drain(this.cache.get1(1), true);
		this.recipeMaxTick = this.cache.get(1);
		this.stacks[5] = this.cache.<SingleInputMatch> get1(2).getRemain(this.stacks[5]);
	}
	
	@Override
	protected boolean onRecipeOutput()
	{
		if (instItem(1, this.cache.get(2), false) && instItem(5, this.cache.get(3), true))
		{
			return instItem(1, this.cache.get(2), true);// always true.
		}
		return false;
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return this.helper.getTemperature();
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		int multiplier = getTE(direction) instanceof IThermalHandler ? 4 : 1;
		return M.argil.thermalConductivity * multiplier;
	}
	
	@Override
	public void onHeatChange(Direction direction, long value)
	{
		this.helper.addInternalEnergy(value);
	}
	
	@Override
	public Container openContainer(int id, EntityPlayer player)
	{
		return new ContainerCeramicPot(this, player);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(int id, EntityPlayer player)
	{
		return new GuiCeramicPot(this, player);
	}
}
