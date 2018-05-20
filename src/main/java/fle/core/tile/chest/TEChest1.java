/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.chest;

import fle.api.item.IIDKeyItem;
import fle.core.common.gui.EnumSlotsSize;
import nebula.common.gui.Container03TileEntity;
import nebula.common.gui.ISlotInitalizer;
import nebula.common.gui.ItemSlot;
import nebula.common.inventory.ItemContainersArray;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TEChest1 extends TEChest implements ITP_BoundingBox
{
	public static enum ChestType
	{
		RATTAN(3, 3, "rattan"), WOOD(5, 3, "wooden"), LEATHER(5, 3, "leather"), BAMBOO(5, 3, "bamboo");
		
		final EnumSlotsSize	size;
		public final String	name;
		
		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite icon;
		
		ChestType(int x, int y, String name)
		{
			this.size = EnumSlotsSize.getSize(x, y);
			this.name = name;
		}
	}
	
	public static ChestType getChestType(ItemStack stack)
	{
		return NBTs.getEnumOrDefault(ItemStacks.getSubOrSetupNBT(stack, "chest", false), "material", TEChest1.ChestType.WOOD);
	}
	
	ChestType type;
	
	public TEChest1()
	{
		super(true, false);
		setChestType(ChestType.WOOD);
	}
	
	private void setChestType(ChestType type)
	{
		this.type = type;
		this.items = new ItemContainersArray(new ItemStack[type.size.size], 64);
	}
	
	public ChestType getChestType()
	{
		return this.type;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTs.setEnum(compound, "type", this.type);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.type = NBTs.getEnumOrDefault(compound, "type", ChestType.WOOD);
		this.items = new ItemContainersArray(new ItemStack[this.type.size.size], 64);
		super.readFromNBT(compound);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		NBTs.setEnum(nbt, "ty", this.type);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		ChestType t1;
		if (this.type != (t1 = NBTs.getEnumOrDefault(nbt, "ty", this.type)))
		{
			setChestType(t1);
		}
	}
	
	@Override
	public boolean canOpenTileEntity(ItemStack stack, IIDKeyItem item)
	{
		return true;
	}
	
	@Override
	public String getName()
	{
		return "inventory.fle.chest";
	}
	
	@Override
	protected void writeToItemStackNBT(NBTTagCompound compound)
	{
		super.writeToItemStackNBT(compound);
		NBTs.setEnum(compound, "material", this.type);
	}
	
	@Override
	protected void readFromItemStackNBT(NBTTagCompound compound)
	{
		setChestType(NBTs.getEnumOrDefault(compound, "material", ChestType.WOOD));
		super.readFromItemStackNBT(compound);
	}
	
	@Override
	public void initalizeContainer(Container03TileEntity container, ISlotInitalizer initalizer)
	{
		EnumSlotsSize size = this.type.size;
		
		ItemSlot[] slots = new ItemSlot[size.size];
		int index = 0;
		for (int i = 0; i < size.column; ++i)
		{
			for (int j = 0; j < size.row; ++j)
			{
				slots[index] = new ItemSlot(this.items.getContainer(index), this, index, size.x + 18 * j, size.y + 18 * i);
				index ++;
			}
		}
		initalizer.addSlots("store", slots).addLocation("player", false);
		initalizer.straegyPlayerBag().addLocation("store", false).addLocation("hand", false);
		initalizer.straegyPlayerHand().addLocation("store", false).addLocation("bag", false);
		
		openInventory(container.player);
	}
	
	private static final AxisAlignedBB	AABB1	= new AxisAlignedBB(1.0F / 16.0F, 0.0F / 16.0F, 2.0F / 16.0F, 15.0F / 16.0F, 10.0F / 16.0F, 14.0F / 16.0F);
	private static final AxisAlignedBB	AABB2	= new AxisAlignedBB(2.0F / 16.0F, 0.0F / 16.0F, 1.0F / 16.0F, 14.0F / 16.0F, 10.0F / 16.0F, 15.0F / 16.0F);
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		return this.facing.getAxis() == Axis.Z ? AABB1 : AABB2;
		// facing.getAxis() == Axis.X
	}
}
