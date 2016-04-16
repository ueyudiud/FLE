package farcore.item;

import java.util.ArrayList;
import java.util.List;

import farcore.energy.thermal.ThermalNet;
import farcore.enums.Direction;
import farcore.enums.EnumItem;
import farcore.interfaces.IDebugableBlock;
import farcore.interfaces.tile.IDebugableTile;
import farcore.lib.substance.SubstanceWood;
import farcore.util.FleLog;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemDebugger extends ItemBase
{
	public ItemDebugger()
	{
		super("debug", "debug");
		EnumItem.debug.set(new ItemStack(this));
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		super.registerIcons(register);
		V.voidItemIcon = register.registerIcon("fle:void");
	}
		
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, 
			int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
		{
			if(player.isSneaking())
			{
				Direction direction = Direction.directions[side];
				x += direction.x;
				y += direction.y;
				z += direction.z;
			}
			try
			{
				SubstanceWood.getSubstance("oak").generator.generate(world, world.rand, x, y, z, false);
				Block block = world.getBlock(x, y, z);
				TileEntity tile = world.getTileEntity(x, y, z);
				List<String> list = new ArrayList();
				list.add("Block Type : " + block.getClass().getName() + "|| Metadata : " + world.getBlockMetadata(x, y, z));
				if(block instanceof IDebugableBlock)
				{
					list.add("==========BLOCK INFO==========");
					((IDebugableBlock) block).addInformation(world, x, y, z, list);
					list.add("==============================");
				}
				if(tile != null)
				{
					list.add("TE Type : " + tile.getClass().getName());
					if(tile instanceof IDebugableTile)
					{
						list.add("===========TE INFO============");
						((IDebugableTile) tile).addDebugInformation(list);
						list.add("==============================");
					}
				}
				list.add("===========FTN INFO===========");
				list.add("Temperature : " + (int) ThermalNet.getTemp(world, x, y, z, true) + "K");
				list.add("Delta Temperature : " + (int) ThermalNet.getTempDifference(world, x, y, z) + "K");
				list.add("==============================");
				for(String string : list)
				{
					player.addChatComponentMessage(new ChatComponentText(string));
				}
			}
			catch(Throwable throwable)
			{
				player.addChatComponentMessage(new ChatComponentText("Fail to debug."));
				if(V.debug)
				{
					FleLog.getCoreLogger().catching(throwable);
				}
			}
			return true;
		}
		return false;
	}
}