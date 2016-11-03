package farcore.lib.item.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.data.EnumToolType;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.block.IDebugableBlock;
import farcore.lib.block.IToolableBlock;
import farcore.lib.item.ItemBase;
import farcore.lib.tile.IDebugableTile;
import farcore.lib.tile.IToolableTile;
import farcore.lib.util.Direction;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDebugger extends ItemBase
{
	public ItemDebugger()
	{
		super(FarCore.ID, "debugger", "debugger", "Don't play this item~");
		LanguageManager.registerLocal(getTranslateName(new ItemStack(this)), "Debugger");
		setMaxStackSize(1);
		EnumItem.debug.set(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		U.Mod.registerItemModel(EnumItem.debug.item, 0, FarCore.ID, "debugger");
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if(!world.isRemote)
		{
			try
			{
				if(player.isSneaking())
				{
					pos = pos.offset(side);
				}
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if(block instanceof IToolableBlock)
				{
					((IToolableBlock) block).onToolClick(player, EnumToolType.chisel, stack, world, pos, Direction.of(side), hitX, hitY, hitZ);
				}
				TileEntity tile = world.getTileEntity(pos);
				if(tile instanceof IToolableTile)
				{
					((IToolableTile) tile).onToolClick(player, EnumToolType.chisel, stack, Direction.of(side), hitX, hitY, hitZ);
				}
				List<String> list = new ArrayList();
				//This information is added in F3 information, so should I remove these information display?
				list.add("================World Info==================");
				list.add("Block Type : " + block.getClass().getSimpleName());
				list.add("State : ");
				for(Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet())
				{
					list.add(entry.getKey().getName() + " : " + ((IProperty) entry.getKey()).getName(entry.getValue()));
				}
				list.add("Temperature : " + ChatFormatting.RED + ThermalNet.getTemperature(world, pos, false) + "K");
				if(block instanceof IDebugableBlock)
				{
					list.add("==========BLOCK INFO==========");
					((IDebugableBlock) block).addInformation(player, world, pos, Direction.of(side), list);
					list.add("==============================");
				}
				if(tile != null)
				{
					list.add("TE Type : " + tile.getClass().getName());
					if(tile instanceof IDebugableTile)
					{
						list.add("===========TE INFO============");
						((IDebugableTile) tile).addDebugInformation(player, Direction.of(side), list);
						list.add("==============================");
					}
				}
				for(String string : list)
				{
					player.addChatComponentMessage(new TextComponentString(string));
				}
			}
			catch(Exception exception)
			{
				player.addChatComponentMessage(new TextComponentString("Fail to debug."));
				if(FarCore.debug)
				{
					Log.warn("Fail to get information from coord.", exception);
				}
			}
		}
		return EnumActionResult.PASS;
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(player.isSneaking())
		{
			try
			{
				entity.worldObj.removeEntity(entity);
			}
			catch(Exception exception)
			{
				if(FarCore.debug)
				{
					Log.warn("Fail to remove %s from world.", exception, entity);
				}
			}
			return true;
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
}