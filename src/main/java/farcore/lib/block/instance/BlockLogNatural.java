package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.data.EnumItem;
import farcore.data.EnumToolType;
import farcore.data.V;
import farcore.lib.block.IBurnCustomBehaviorBlock;
import farcore.lib.block.IToolableBlock;
import farcore.lib.item.instance.ItemTreeLog;
import farcore.lib.material.Mat;
import farcore.lib.util.Direction;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLogNatural extends BlockLog
implements IToolableBlock, IBurnCustomBehaviorBlock
{
	public BlockLogNatural(Mat material)
	{
		super(material.tree, "log.natural." + material.name);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), material.localName + " Log");
		if(information.tickLogUpdate())
			setTickRandomly(true);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta)
	{
		information.breakLog(world, x, y, z, meta, false);
	}

	@Override
	public int tickRate(World world)
	{
		return 20;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		label:
			if(!world.isRemote)
			{
				if(world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP))
					break label;
			    for (int h = -1; h <= 1; h++)
					for (int g = -1; g <= 1; g++)
						for (int f = -1; f <= 1; f++)
							if ((h | g | f) != 0 && isLog(world, x + h, y + g, z + f))
								break label;
			    world.setBlockToAir(x, y, z);
			}
		information.updateLog(world, x, y, z, rand, false);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block)
	{
		if(world.isRemote) return;
		if(world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP))
			return;
	    for (int h = -1; h <= 1; h++)
			for (int g = -1; g <= 1; g++)
				for (int f = -1; f <= 1; f++)
					if ((h | g | f) != 0 && isLog(world, x + h, y + g, z + f))
						return;
	    world.setBlockToAir(x, y, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xPos,
			float yPos, float zPos)
	{
		return information.onLogRightClick(player, world, x, y, z, side, xPos, yPos, zPos, false);
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta,
			EntityPlayer player)
	{
		harvestBlock(world, player, x, y, z, world.getBlockMetadata(x, y, z));
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
	{
		ItemStack stack = player.getCurrentEquippedItem();
		boolean isAxe = EnumToolType.axe.match(stack);
		if(!world.isRemote)
			if(isAxe)
				breakTree(world, x, y, z);
			else
				world.setBlock(x, y, z, this, meta, 3);
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
	{
		try
		{
			breakTree(world, x, y, z);
		}
		catch(OutOfMemoryError error)
		{
			Log.warn("The out of memory prevent this tree destory.");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		information.registerLogIcon(register);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return information.getLogIcon(register, meta, side);
	}

	public boolean isLog(World world, int x, int y, int z)
	{
		return U.Worlds.isBlock(world, x, y, z, this, -1, false);
	}

	private void breakTree(World world, int x, int y, int z)
	{
		BreakTree runnable = new BreakTree(world, x, y, z);
		new Thread(runnable).start();
	}

	@Override
	public float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ)
	{
		return information.onToolClickLog(player, tool, stack, world, x, y, z, side, hitX, hitY, hitZ, false);
	}

	@Override
	public float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, int x,
			int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return information.onToolUseLog(player, tool, stack, world, useTick, x, y, z, side, hitX, hitY, hitZ, false);
	}

	private class BreakTree implements Runnable
	{
		public World world;
		public int x;
		public int y;
		public int z;
		private int cacheDrop = 0;

		public BreakTree(World world, int x, int y, int z)
		{
			this.world = world;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void run()
		{
			boolean[][][] cacheArray;
			scanLog(world, x, y, z, cacheArray = new boolean[2 * V.treeScanRange + 1][256][2 * V.treeScanRange + 1], (byte) 0, (byte) 0, (byte) 0);
			if(cacheDrop > 0)
			{
				ItemStack stack = new ItemStack(EnumItem.log.item, 1, information.material().id);
				ItemTreeLog.setLogSize(stack, cacheDrop);
				dropBlockAsItem(world, x, y, z, stack);
			}
		}

		public void scanLog(World world, int i, int j, int k, boolean[][][] array, byte x, byte y, byte z)
		{
			if(y >= 0 && j + y < 256)
			{
				int offsetX = 0,
						offsetY = 0,
						offsetZ = 0;
				array[x + V.treeScanRange][y][z + V.treeScanRange] = true;
				for (offsetX = -1; offsetX <= 1; offsetX++)
					for (offsetZ = -1; offsetZ <= 1; offsetZ++)
						for (offsetY = 0; offsetY <= 2; offsetY++)
							if((offsetX != 0 || offsetY != 0 || offsetZ != 0) &&
									Math.abs(x + offsetX) <= V.treeScanRange &&
									Math.abs(z + offsetZ) <= V.treeScanRange &&
									j + y + offsetY < 256)
								if(!array[x + offsetX + V.treeScanRange][y + offsetY][z + offsetZ + V.treeScanRange] &&
										isLog(world, i + x + offsetX, j + y + offsetY, k + z + offsetZ))
									scanLog(world, i, j, k, array, (byte) (x + offsetX), (byte) (y + offsetY), (byte) (z + offsetZ));
				++cacheDrop;
				world.setBlock(x + i, y + j, z + k, Blocks.air, 0, 2);

				world.getBlock(x + i + 1, y + j, z + k).beginLeavesDecay(world, x + i + 1, y + j, z + k);
				world.getBlock(x + i - 1, y + j, z + k).beginLeavesDecay(world, x + i - 1, y + j, z + k);
				world.getBlock(x + i, y + j + 1, z + k).beginLeavesDecay(world, x + i, y + j + 1, z + k);
				world.getBlock(x + i, y + j - 1, z + k).beginLeavesDecay(world, x + i, y + j - 1, z + k);
				world.getBlock(x + i, y + j, z + k + 1).beginLeavesDecay(world, x + i, y + j, z + k + 1);
				world.getBlock(x + i, y + j, z + k - 1).beginLeavesDecay(world, x + i, y + j, z + k - 1);
				U.Worlds.spawnDropsInWorld(world, i + x, j + y, k + z, information.getLogOtherDrop(world, i + x, j + y, k + z, new ArrayList()));
			}
		}
	}

	@Override
	public boolean onBurn(World world, int x, int y, int z, float burnHardness, Direction direction)
	{
		return false;
	}

	@Override
	public boolean onBurningTick(World world, int x, int y, int z, Random rand, Direction fireSourceDir)
	{
		return false;
	}

	@Override
	public float getThermalConduct(World world, int x, int y, int z)
	{
		return information.material().thermalConduct;
	}

	@Override
	public int getFireEncouragement(World world, int x, int y, int z)
	{
		return 0;
	}

	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return true;
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return 24;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return 25;
	}
}