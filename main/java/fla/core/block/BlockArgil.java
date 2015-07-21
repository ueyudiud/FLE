package fla.core.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos;
import fla.core.Fla;
import fla.core.FlaItems;
import fla.core.tileentity.argil.TileEntityTerrine;

public class BlockArgil extends BlockArgilBase
{
	public BlockArgil()
	{
		super(Material.rock);
		setItemTextureName(FlaValue.TEXT_FILE_NAME + ":clay/1001", 0);
	}

	@Override
	public boolean onBlockActivated(World world, int x,
			int y, int z, EntityPlayer player,
			int side, float xPos, float yPos,
			float zPos)
	{
		if(world.isRemote)
		{
			return true;
		}
		else
		{
			TileEntity tile = (TileEntity) world.getTileEntity(x, y, z);
			if(tile != null)
			{
				player.openGui(Fla.MODID, 3, world, x, y, z);
			}
			return true;
		}
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ForgeDirection.NORTH;
	}
	
	@Override
	public void getBlockInfomation(ItemStack itemstack, boolean shiftKey,
			boolean pKey, List list) 
	{
		switch(itemstack.getItemDamage())
		{
		case 0 : 
		{
			if(itemstack.hasTagCompound())
			{
				NBTTagCompound nbt = itemstack.getTagCompound();
				StringBuilder sb = new StringBuilder().append(StatCollector.translateToLocal("info.item.inventory.contain")).append(":");
				list.add(sb.toString());
				sb = new StringBuilder().append(StatCollector.translateToLocal("info.item.inventory.itemstack")).append(":");
				list.add(sb.toString());
				for(int i = 0; i < 2; ++i)
				{
					ItemStack stack1 = ItemStack.loadItemStackFromNBT(itemstack.getTagCompound().getCompoundTag("ItemStack_" + String.valueOf(i)));
					if(stack1 != null)
					{
						sb = new StringBuilder().append(EnumRarity.rare.rarityColor).append(stack1.getDisplayName()).append(EnumRarity.common.rarityColor).append("x").append(stack1.stackSize);
						list.add(sb.toString());
					}
				}
				sb = new StringBuilder().append(StatCollector.translateToLocal("info.item.inventory.fluidstack")).append(":");
				list.add(sb.toString());
				FluidStack stack = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("FluidStack"));
				if(stack != null)
				{
					sb = new StringBuilder().append(EnumRarity.rare.rarityColor)
							.append(stack.getLocalizedName()).append(EnumRarity.common.rarityColor)
							.append("x").append(stack.amount).append("L");
					list.add(sb.toString());
				}
			}
		}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		TileEntity tile1 = tile;
		tile = null;
		if(tile1 == null)
		{
			switch(meta)
			{
			case 0 : return new TileEntityTerrine();
			}
		}
		return tile1;
	}

	@Override
	public int getMaxDamage() 
	{
		return 1;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int level)
	{
		return FlaItems.argil_smelted;
	}
	
	public TileEntity tile;

	@Override
	public void addNBTToTileByItemStack(ItemStack stack) 
	{
		switch(stack.getItemDamage())
		{
		case 0 : 
		{
			if(!stack.hasTagCompound())
			{
				tile = null;
				return;
			}
			tile = new TileEntityTerrine();
			NBTTagCompound nbt1 = new NBTTagCompound();
			for(int i = 0; i < ((IInventory) tile).getSizeInventory(); ++i)
			{
				ItemStack stack1 = ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag("ItemStack_" + String.valueOf(i)));
				((IInventory) tile).setInventorySlotContents(i, stack1);
			}
			nbt1 = new NBTTagCompound();
			((IFluidTank) tile).fill(FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("FluidStack")), true);
		}
		}
	}


	@Override
	public void addNBTToItemStackByTile(ItemStack stack, TileEntity tile, int meta) 
	{
		switch(meta)
		{
		case 0 : 
		{
			stack.stackTagCompound = new NBTTagCompound();
			NBTTagCompound nbt1 = new NBTTagCompound();
			for(int i = 0; i < ((IInventory) tile).getSizeInventory(); ++i)
			{
				nbt1 = new NBTTagCompound();
				ItemStack stack1 = ((IInventory) tile).getStackInSlot(i);
				if(stack1 != null) stack1.writeToNBT(nbt1);
				stack.getTagCompound().setTag("ItemStack_" + String.valueOf(i), nbt1);
			}
			nbt1 = new NBTTagCompound();
			if(((IFluidTank) tile).getFluid() != null) ((IFluidTank) tile).getFluid().writeToNBT(nbt1);
			stack.getTagCompound().setTag("FluidStack", nbt1);
		}
		}
	}
}