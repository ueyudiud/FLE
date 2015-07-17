package fla.api.item.tool;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.Cancelable;
import fla.api.world.BlockPos;

@Cancelable
public class ToolDamageEvent extends PlayerEvent
{
	public ItemStack stack;
	public final ItemDamageResource resource;

	public ToolDamageEvent(EntityPlayer player, ItemStack stack, ItemDamageResource resource)
	{
		super(player);
		this.resource = resource;
		this.stack = stack;
	}
	
	public static class BBDamageEvent extends ToolDamageEvent
	{
		final World world;
		Block target;
		BlockPos pos;
		int blockMeta;
		public BBDamageEvent(EntityPlayer player, Block block, ItemStack stack, int x, int y, int z, int meta) 
		{
			super(player, stack, ItemDamageResource.BreakBlock);
			world = player.worldObj;
			pos = new BlockPos(world, x, y, z);
			target = block;
			blockMeta = meta;
		}
	}
}
