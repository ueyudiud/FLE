/*
 * copyright© 2016-2018 ueyudiud
 */

package fle.core.tile.wooden.workbench;

import static fle.api.recipes.instance.RecipeMaps.POLISHING;

import java.util.Arrays;

import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.api.recipes.instance.PolishRecipe;
import fle.api.recipes.instance.interfaces.IPolishRecipeHandler;
import fle.api.recipes.instance.interfaces.IPolishableItem;
import fle.core.client.gui.wooden.workbench.GuiPolishTable;
import fle.core.common.gui.wooden.workbench.ContainerPolishTable;
import nebula.common.tile.IGuiTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.TEStaticInventory;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TEWoodenPolishTable extends TEStaticInventory implements IPolishRecipeHandler, ITP_BlockHardness, ITP_ExplosionResistance, ITB_BlockActived, IGuiTile
{
	ItemStack	input;
	char[]		map	= "         ".toCharArray();
	
	public TEWoodenPolishTable()
	{
		super(2);
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return 1.5F;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return 2.0F;
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (isServer())
		{
			openGUI(player, 0);
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTs.setCharArray(compound, "map", this.map);
		NBTs.setItemStack(compound, "input", this.input, false);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.map = NBTs.getCharArrayOrDefault(compound, "map", this.map);
		this.input = NBTs.getItemStackOrDefault(compound, "input", null);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		NBTs.setCharArray(nbt, "m", this.map);
		NBTs.setItemStack(nbt, "i", this.input, true);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.map = NBTs.getCharArrayOrDefault(nbt, "m", this.map);
		this.input = NBTs.getItemStackOrDefault(nbt, "i", this.input);
	}
	
	public void onPolish(EntityPlayer player, ItemStack useTool, int clickIndex)
	{
		if (useTool == null || !(useTool.getItem() instanceof IPolishableItem)) return;
		int level = ((IPolishableItem) useTool.getItem()).getPolishLevel(useTool);
		if (level < 0) return;
		if (this.input == null)
		{
			if (!PolishRecipe.isPolishable(this.stacks[0])) return;
			this.input = decrStackSize(0, 1);
		}
		if (PolishRecipe.getPolishLevel(this.input) <= level)
		{
			this.map[clickIndex] = ((IPolishableItem) useTool.getItem()).getPolishResult(useTool, this.map[clickIndex]);
			TemplateRecipeCache<IPolishRecipeHandler> cache = POLISHING.findRecipe(this);
			if (cache != null)
			{
				this.stacks[1] = cache.<ItemStack> get(0).copy();
			}
			else
			{
				this.stacks[1] = null;
			}
			markDirty();
		}
		((IPolishableItem) useTool.getItem()).onPolished(player, useTool);
		syncToNearby();
	}
	
	public void clearPolishMap()
	{
		Arrays.fill(this.map, ' ');
		this.input = null;
		syncToNearby();
	}
	
	@Override
	public ItemStack decrItem(int index, int count, boolean process)
	{
		if (index == 1)
		{
			ItemStack stack = process ? ItemStackHelper.getAndRemove(this.stacks, index) : this.stacks[index];
			if (process)
			{
				clearPolishMap();
				markDirty();
			}
			return stack;
		}
		return super.decrItem(index, count, process);
	}
	
	@Override
	public String getName()
	{
		return "inventory.polishing.table";
	}
	
	@Override
	public ItemStack getPolishingInput()
	{
		return this.input;
	}
	
	@Override
	public char[] getPolishingMatrix()
	{
		return this.map;
	}
	
	@Override
	public Container openContainer(int id, EntityPlayer player)
	{
		return new ContainerPolishTable(this, player);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(int id, EntityPlayer player)
	{
		return new GuiPolishTable(this, player);
	}
}
