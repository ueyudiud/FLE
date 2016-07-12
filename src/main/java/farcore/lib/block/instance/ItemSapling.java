package farcore.lib.block.instance;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.block.ItemBlockBase;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemSapling extends ItemBlockBase
{
	public ItemSapling(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int metadata)
	{
		return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
	}
	
	public static ItemStack setTree(ItemStack stack, String dna, int generation)
	{
		NBTTagCompound nbt = U.ItemStacks.setupNBT(stack, true);
		nbt.setString("dna", dna);
		nbt.setInteger("generation", generation);
		return stack;
	}
	
	public static String getDNA(ItemStack stack)
	{
		return U.ItemStacks.setupNBT(stack, false).getString("dna");
	}
}