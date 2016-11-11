package farcore.lib.plant;

import java.util.List;
import java.util.Random;

import farcore.lib.block.IMetaHandler;
import farcore.lib.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public interface IPlant extends IMetaHandler
{
	EnumPlantType type();

	void tickUpdate(Block block, World worldIn, BlockPos pos, IBlockState state, Random rand);
	
	List<ItemStack> getDrops(Block block, IBlockAccess world, BlockPos pos, IBlockState state, Random rand);

	boolean onBlockActive(Block block, World worldIn, BlockPos pos, IBlockState state, Direction direction, EntityPlayer player, ItemStack stack, EnumHand hand, float hitX, float hitY, float hitZ);

	void onEntityWalk(Block block, World worldIn, BlockPos pos, Entity entityIn);
}