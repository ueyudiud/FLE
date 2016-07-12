package farcore.lib.block.instance;

import java.util.Random;

import farcore.data.EnumToolType;
import farcore.lib.block.IToolableBlock;
import farcore.lib.block.ItemBlockBase;
import farcore.lib.material.Mat;
import farcore.lib.tree.ITree;
import farcore.lib.util.LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLogArtificial extends BlockLog implements IToolableBlock
{
	public BlockLogArtificial(Mat material)
	{
		super(material.tree, "log.artificial." + material.name);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), material.localName + " Log");
		if(information.tickLogUpdate())
		{
			setTickRandomly(true);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xPos,
			float yPos, float zPos)
	{
		return information.onLogRightClick(player, world, x, y, z, side, xPos, yPos, zPos, true);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta)
	{
		information.breakLog(world, x, y, z, meta, true);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		information.updateLog(world, x, y, z, rand, true);
	}
	
	private static final int[][] ROTATION_MATRIX = {
			{0, 0, 2, 1, 2, 1},
			{0, 0, 2, 1, 2, 1},
			{2, 0, 1, 1, 2, 0},
			{2, 0, 1, 1, 2, 0},
			{0, 1, 0, 1, 2, 2},
			{0, 1, 0, 1, 2, 2}};
		
	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
	{
		int meta = world.getBlockMetadata(x, y, z),
				meta1 = ROTATION_MATRIX[axis.ordinal()][meta];
		if(meta != meta1)
		{
			world.setBlockMetadataWithNotify(x, y, z, meta1, 3);
			return true;
		}
		return false;
	}

	@Override
	public float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ)
	{
		return information.onToolClickLog(player, tool, stack, world, x, y, z, side, hitX, hitY, hitZ, true);
	}
	
	@Override
	public float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, int x,
			int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return information.onToolUseLog(player, tool, stack, world, useTick, x, y, z, side, hitX, hitY, hitZ, true);
	}
}