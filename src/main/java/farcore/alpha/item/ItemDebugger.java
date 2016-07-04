package farcore.alpha.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.interfaces.block.IDebugableBlock;
import farcore.energy.thermal.ThermalNet;
import farcore.enums.Direction;
import farcore.enums.EnumItem;
import farcore.interfaces.energy.thermal.IThermalTile;
import farcore.interfaces.tile.IDebugableTile;
import farcore.util.FleLog;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemDebugger extends ItemBase
{
	public ItemDebugger()
	{
		super(FarCore.ID, "debugger", "Debugger", "Do not play this item~");
		setTextureName("farcore:debug");
		setMaxStackSize(1);
		EnumItem.debug.set(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(INamedIconRegister register)
	{
		super.registerIcons(register);
		V.voidItemIcon = register.registerIcon("void", "farcore:void");
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
				Block block = world.getBlock(x, y, z);
				TileEntity tile = world.getTileEntity(x, y, z);
				List<String> list = new ArrayList();
				list.add("Biome : " + world.getWorldChunkManager().getBiomeGenAt(null, x, z, 1, 1, false)[0].biomeName);
				list.add("Block Type : " + block.getClass().getSimpleName() + "|| Metadata : " + world.getBlockMetadata(x, y, z));
				if(block instanceof IDebugableBlock)
				{
					list.add("==========BLOCK INFO==========");
					((IDebugableBlock) block).addInformation(player, world, x, y, z, side, list);
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
				if(tile instanceof IThermalTile)
				{
					list.add("Tile Temperature : " + (int) ((IThermalTile) tile).getTemperature(Direction.Q) + "K");
					list.add("Tile IO Heat : " + (int) ((IThermalTile) tile).getDeltaHeat() + "J");
				}
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
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "System modifier", Double.MAX_VALUE, 0));
        return multimap;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		entity.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
		return true;
	}
}