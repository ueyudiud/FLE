package farcore.alpha.block.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.alpha.block.ItemBlockBase;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.interfaces.block.IToolableBlock;
import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.lib.substance.SubstanceWood;
import farcore.util.FleLog;
import farcore.util.U;
import farcore.util.V;
import farcore.util.Values;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import sun.net.www.content.audio.wav;

public class BlockLogNatural extends BlockLog
implements IToolableBlock
{
	private SubstanceWood wood;
	
	protected BlockLogNatural(String name, Material material, SubstanceWood wood, 
			String localName, float hardness, float resistance)
	{
		super(name, material, hardness, resistance);
		this.wood = wood;
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(0), localName + " Log");
		if(wood.logProp != null && wood.logProp.tickUpdate())
		{
			setTickRandomly(true);
		}
	}
	protected BlockLogNatural(String name, Class<? extends ItemBlockBase> clazz, 
			Material material, SubstanceWood wood, String localName, float hardness,
			float resistance, Object[] objects)
	{
		super(name, clazz, material, hardness, resistance, objects);
		this.wood = wood;
		if(wood.logProp != null && wood.logProp.tickUpdate())
		{
			setTickRandomly(true);
		}
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(0), localName + " Log");
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
			    {
			    	for (int g = -1; g <= 1; g++)
			    	{
			    		for (int f = -1; f <= 1; f++)
			    		{
			    			if ((h | g | f) != 0 && isLog(world, x + h, y + g, z + f))
			    			{
			    				break label;
			    			}
			    		}
			    	}
			    }
			    world.setBlockToAir(x, y, z);
			}
		if(wood.logProp != null)
			wood.logProp.onUpdate(world, x, y, z, rand);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block)
	{
		if(world.isRemote) return;
		if(world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP))
			return;
	    for (int h = -1; h <= 1; h++)
	    {
	    	for (int g = -1; g <= 1; g++)
	    	{
	    		for (int f = -1; f <= 1; f++)
	    		{
	    			if ((h | g | f) != 0 && isLog(world, x + h, y + g, z + f))
	    			{
	    				return;
	    			}
	    		}
	    	}
	    }
	    world.setBlockToAir(x, y, z);
	}
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int p_149681_5_,
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
		{
			if(isAxe)
			{
				breakTree(world, x, y, z);
			}
			else
			{
				world.setBlock(x, y, z, this, meta, 3);
			}
		}
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
			FleLog.getCoreLogger().warn("The out of memory prevent this tree destory.");
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		if(wood.logProp != null)
		{
			wood.logProp.registerIcon(register);
		}
		else
		{
			super.registerIcon(register);
		}
	}
	
	@Override
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return wood.logProp != null ? wood.logProp.getIcon(side, meta, register) :
			super.getIcon(side, meta, register);
	}
	
	public boolean isLog(World world, int x, int y, int z)
	{
		return U.Worlds.isBlock(world, x, y, z, this, -1, false);
	}
	
	private List<ItemStack> otherDrop()
	{
		return new ArrayList();
	}
	
	private void notifyLeaves(World world, int x, int y, int z)
	{
		world.notifyBlockChange(x + 1, y, z, Blocks.air);
		world.notifyBlockChange(x - 1, y, z, Blocks.air);
		world.notifyBlockChange(x, y + 1, z, Blocks.air);
		world.notifyBlockChange(x, y - 1, z, Blocks.air);
		world.notifyBlockChange(x, y, z + 1, Blocks.air);
		world.notifyBlockChange(x, y, z - 1, Blocks.air);
	}
	
	@Override
	public boolean onBurned(World world, int x, int y, int z)
	{
		breakTree(world, x, y, z);
		return true;
	}
	
	@Override
	public int getFlammability(World world, int x, int y, int z, ForgeDirection face, int hardness)
	{
		return 100;
	}

	@Override
	public int getBurningTemperature(World world, int x, int y, int z, int level)
	{
		return 500;
	}
	
	private void breakTree(World world, int x, int y, int z)
	{
		BreakTree runnable = new BreakTree(world, x, y, z);
		if(V.multiThread)
		{
			new Thread(runnable).start();
		}
		else
		{
			runnable.run();
		}
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
				dropBlockAsItem(world, x, y, z, EnumItem.log.instance(1, wood, cacheDrop));
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
							{
								if(!array[x + offsetX + V.treeScanRange][y + offsetY][z + offsetZ + V.treeScanRange] && 
										isLog(world, i + x + offsetX, j + y + offsetY, k + z + offsetZ))
								{
									scanLog(world, i, j, k, array, (byte) (x + offsetX), (byte) (y + offsetY), (byte) (z + offsetZ));
								}
							}
				++cacheDrop;
				world.setBlock(x + i, y + j, z + k, Blocks.air, 0, 2);
				U.Worlds.spawnDropsInWorld(world, i + x, j + y, k + z, otherDrop());
			}
		}
	}

	@Override
	public float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ)
	{
		return wood.logProp != null ? wood.logProp.onToolClick(player, tool, stack, world, x, y, z, side, hitX, hitY, hitZ) :
			0;
	}
	
	@Override
	public float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, int x,
			int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return wood.logProp != null ? wood.logProp.onToolUse(player, tool, stack, world, useTick, x, y, z, side, hitX, hitY, hitZ) :
			0;
	}
}