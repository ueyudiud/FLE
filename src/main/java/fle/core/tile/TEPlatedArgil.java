/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumBlock;
import farcore.data.EnumToolTypes;
import farcore.data.M;
import farcore.energy.thermal.IThermalHandler;
import farcore.energy.thermal.IThermalProvider;
import farcore.energy.thermal.instance.ThermalHandlerSimple;
import farcore.handler.FarCoreEnergyHandler;
import farcore.items.ItemOreChip;
import fle.api.recipes.IRecipeMap;
import fle.api.recipes.instance.RecipeMaps;
import fle.api.recipes.instance.SimpleReducingRecipeHandler;
import fle.api.recipes.instance.SimpleReducingRecipeHandler.Cache;
import fle.api.tile.TERecipe;
import fle.loader.IBFS;
import nebula.common.data.Misc;
import nebula.common.inventory.IBasicInventory;
import nebula.common.inventory.InventorySimple;
import nebula.common.stack.BaseStack;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockDestroyedByPlayer;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockExploded;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;

/**
 * @author ueyudiud
 */
public class TEPlatedArgil extends TERecipe<ItemStack, SimpleReducingRecipeHandler.Cache>
implements ITP_BoundingBox, IToolableTile, ITB_BlockDestroyedByPlayer, ITB_BlockExploded, IThermalProvider
{
	private static final List<AxisAlignedBB> BOUNDS_BOXES = ImmutableList.of(
			new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0),
			new AxisAlignedBB(0.0, 0.0, 0.0, 0.125, 1.0, 1.0),
			new AxisAlignedBB(0.875, 0.0, 0.0, 1.0, 1.0, 1.0),
			new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.125),
			new AxisAlignedBB(0.0, 0.0, 0.875, 1.0, 1.0, 1.0));
	
	private static final byte Burning = 3;
	
	private ThermalHandlerSimple handler = new ThermalHandlerSimple(this);
	private IBasicInventory stacks = new InventorySimple(1, 4);
	private boolean charcoal;
	
	{
		this.handler.material = M.argil;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.stacks.fromNBT(compound, "stacks");
		this.charcoal = compound.getBoolean("charcoal");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		this.stacks.toNBT(compound, "stacks");
		compound.setBoolean("charcoal", this.charcoal);
		return super.writeToNBT(compound);
	}
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		return Block.FULL_BLOCK_AABB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity)
	{
		collidingBoxes.addAll(BOUNDS_BOXES);
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (!is(Burning))
		{
			if (this.charcoal)
			{
				Worlds.spawnDropInWorld(this, IBFS.iResources.getSubItem("charcoal"));
			}
			else
			{
				Worlds.spawnDropInWorld(this, this.stacks.toArray());
				disable(Working);
			}
		}
		return super.onBlockActivated(player, hand, stack, side, hitX, hitY, hitZ);
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tool == EnumToolTypes.FIRESTARTER && is(Working) && !is(Burning) && this.charcoal)
		{
			if (isServer())
			{
				enable(Burning);
				syncToNearby();
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, 0.25F);
		}
		return new ActionResult<>(EnumActionResult.PASS, 0.0F);
	}
	
	@Override
	public boolean onBlockDestroyedByPlayer(IBlockState state)
	{
		if (is(WaitForOutput))
		{
			if (this.cache != null)
			{
				Worlds.spawnDropInWorld(this, this.cache.output);
				disable(WaitForOutput);
				disable(Working);
				this.recipeTick = 0;
				this.cache = null;
				this.stacks.removeAllStacks();
				this.charcoal = false;
			}
			return true;
		}
		else
		{
			Worlds.spawnDropInWorld(this, this.stacks.toArray());
			if (this.charcoal)
			{
				Worlds.spawnDropInWorld(this, IBFS.iResources.getSubItem("charcoal"));
			}
			return this.world.setBlockState(this.pos, Misc.AIR, isClient() ? 11 : 3);
		}
	}
	
	@Override
	public void onBlockExploded(Explosion explosion)
	{
		removeBlock();
	}
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		FarCoreEnergyHandler.onAddFromWorld(this);
	}
	
	@Override
	public void onRemoveFromLoadedWorld()
	{
		super.onRemoveFromLoadedWorld();
		FarCoreEnergyHandler.onRemoveFromWorld(this);
	}
	
	private boolean inputFlag = false;
	
	@Override
	protected void updateServer()
	{
		if (!is(Working))
		{
			boolean flag = getRecipeMap().findRecipe(this.stacks.getStack(0)) != null;
			for (EntityItem enitiy : getEntitiesWithinAABB(EntityItem.class))
			{
				ItemStack stack = enitiy.getEntityItem();
				int size = 0;
				if (stack.getItem() instanceof ItemOreChip && (size = this.stacks.incrItem(0, stack, true)) != 0)
				{
					stack.stackSize -= size;
				}
				else if (flag && new BaseStack(IBFS.iResources.getSubItem("charcoal")).similar(stack) && !this.charcoal)
				{
					this.charcoal = true;
					stack.stackSize --;
				}
				if (stack.stackSize == 0)
				{
					enitiy.setDead();
				}
			}
			this.inputFlag = this.charcoal;
		}
		super.updateServer();
		if (is(Burning))
		{
			if (isAirBlock(Direction.U))
			{
				setBlockState(Direction.U, EnumBlock.fire.apply(), 3);
			}
		}
	}
	
	@Override
	protected boolean isInputEnabled()
	{
		return this.inputFlag;
	}
	
	@Override
	protected ItemStack getRecipeInputHandler()
	{
		return this.stacks.getStack(0);
	}
	
	@Override
	protected IRecipeMap<?, Cache, ItemStack> getRecipeMap()
	{
		return RecipeMaps.SIMPLE_REDUCING;
	}
	
	@Override
	protected void onRecipeInput()
	{
		this.recipeMaxTick = this.cache.duration;
	}
	
	@Override
	protected boolean onRecipeOutput()
	{
		this.charcoal = false;
		disable(Burning);
		return false;//Always be false.
	}
	
	@Override
	protected int getPower()
	{
		return is(Burning) ? 1 : 0;
	}
	
	@Override
	public IThermalHandler getThermalHandler()
	{
		return this.handler;
	}
}
