/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile.chest;

import fle.api.item.IIDKeyItem;
import fle.core.client.gui.GuiInvContainer;
import fle.core.common.gui.container.Containernxn;
import fle.core.common.gui.container.EnumSlotsSize;
import nebula.common.tile.IGuiTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TEChest1 extends TEChest
implements IGuiTile, ITP_BoundingBox
{
	public static enum ChestType
	{
		RATTAN	(3, 3, "rattan"),
		WOOD	(5, 3, "wooden"),
		LEATHER	(5, 3, "leather"),
		BAMBOO	(5, 3, "bamboo");
		
		final EnumSlotsSize size;
		public final String name;
		
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
	
	ChestType type = ChestType.WOOD;
	
	public TEChest1()
	{
		super(true, false);
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
		super.readFromNBT(compound);
		this.type = NBTs.getEnumOrDefault(compound, "type", ChestType.WOOD);
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
		NBTs.getEnumOrDefault(nbt, "ty", this.type);
	}
	
	@Override
	protected ItemStack[] stacks()
	{
		if (this.stacks == null)
		{
			this.stacks = new ItemStack[this.type.size.size];
		}
		
		return this.stacks;
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
		this.type = NBTs.getEnumOrDefault(compound, "material", ChestType.WOOD);
		super.readFromItemStackNBT(compound);
	}
	
	@Override
	public Container openContainer(int id, EntityPlayer player)
	{
		return new Containernxn<>(this, player, this.type.size);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(int id, EntityPlayer player)
	{
		return new GuiInvContainer(this, player, this.type.size);
	}
	
	private static final AxisAlignedBB AABB1 = new AxisAlignedBB(1.0F / 16.0F, 0.0F / 16.0F, 2.0F / 16.0F, 15.0F / 16.0F, 10.0F / 16.0F, 14.0F / 16.0F);
	private static final AxisAlignedBB AABB2 = new AxisAlignedBB(2.0F / 16.0F, 0.0F / 16.0F, 1.0F / 16.0F, 14.0F / 16.0F, 10.0F / 16.0F, 15.0F / 16.0F);
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		return this.facing.getAxis() == Axis.Z ? AABB1 : AABB2;//facing.getAxis() == Axis.X
	}
}