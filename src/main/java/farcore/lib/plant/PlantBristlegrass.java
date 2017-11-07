/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.EnumToolTypes;
import farcore.data.M;
import farcore.lib.block.instance.BlockPlant;
import farcore.lib.item.instance.ItemSeed;
import nebula.common.util.L;
import nebula.common.util.Players;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class PlantBristlegrass extends PlantStatic
{
	public PlantBristlegrass()
	{
		super(M.bristlegrass, true);
	}
	
	@Override
	public boolean canHarvestBlock(BlockPlant block, IBlockState state, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return Players.matchCurrentToolType(player, EnumToolTypes.SICKLE, EnumToolTypes.HOE);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockPlant block, IBlockState state, BlockPos pos, IBlockAccess world, TileEntity tile, int fortune, boolean silkTouch)
	{
		List<ItemStack> list = new ArrayList<>();
		Random rand = world instanceof World ? ((World) world).rand : L.random();
		if (rand.nextInt(12) == 0)
		{
			list.add(ItemSeed.applyNativeSeed(1, M.bristlegrass));
		}
		return list;
	}
}
