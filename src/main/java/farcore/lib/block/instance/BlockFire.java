package farcore.lib.block.instance;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.lib.block.BlockBase;
import farcore.lib.block.IBurnCustomBehaviorBlock;
import farcore.lib.util.Direction;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.LanguageManager;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Added some custom handler for burnable block,
 * this type of fire can handle these blocks.
 * @author ueyudiud
 *
 */
public class BlockFire extends BlockBase
{
	public BlockFire()
	{
		super("fire", Material.fire);
		setTickRandomly(true);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Fire");
		EnumBlock.fire.set(this);
		setLightLevel(11);
	}

	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return FarCore.handlerA.getRenderId();
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return Blocks.fire.getIcon(side, meta);
	}

	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		if (rand.nextInt(24) == 0)
			world.playSound(x + 0.5F, y + 0.5F, z + 0.5F, "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
		int l;
		float f, f1, f2;
		if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !canCatchFire(world, x, y - 1, z, UP))
		{
			if (canCatchFire(world, x - 1, y, z, EAST))
				for (l = 0; l < 2; ++l)
				{
					f = x + rand.nextFloat() * 0.1F;
					f1 = y + rand.nextFloat();
					f2 = z + rand.nextFloat();
					world.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			if (canCatchFire(world, x + 1, y, z, WEST))
				for (l = 0; l < 2; ++l)
				{
					f = x + 1 - rand.nextFloat() * 0.1F;
					f1 = y + rand.nextFloat();
					f2 = z + rand.nextFloat();
					world.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			if (canCatchFire(world, x, y, z - 1, SOUTH))
				for (l = 0; l < 2; ++l)
				{
					f = x + rand.nextFloat();
					f1 = y + rand.nextFloat();
					f2 = z + rand.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			if (canCatchFire(world, x, y, z + 1, NORTH))
				for (l = 0; l < 2; ++l)
				{
					f = x + rand.nextFloat();
					f1 = y + rand.nextFloat();
					f2 = z + 1 - rand.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			if (canCatchFire(world, x, y + 1, z, DOWN))
				for (l = 0; l < 2; ++l)
				{
					f = x + rand.nextFloat();
					f1 = y + 1 - rand.nextFloat() * 0.1F;
					f2 = z + rand.nextFloat();
					world.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
		}
		else for (l = 0; l < 3; ++l)
		{
			f = x + rand.nextFloat();
			f1 = y + rand.nextFloat() * 0.5F + 0.5F;
			f2 = z + rand.nextFloat();
			world.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
		}
	}

	/**
	 * Returns if this block is collidable (only used by Fire). Args: x, y, z
	 */
	@Override
	public boolean isCollidable()
	{
		return false;
	}

	@Override
	public MapColor getMapColor(int meta)
	{
		return MapColor.tntColor;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return false;
	}

	@Override
	public int tickRate(World world)
	{
		return world.provider.isHellWorld ? 25 : 30;
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return world.isSideSolid(x, y - 1, z, UP) ||
				canNeighborBurn(world, x, y, z);
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor Block
	 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if (!canPlaceBlockAt(world, x, y, z))
			world.setBlockToAir(x, y, z);
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if (world.provider.dimensionId > 0 || !Blocks.portal.func_150000_e(world, x, y, z))
			if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !canNeighborBurn(world, x, y, z))
				world.setBlockToAir(x, y, z);
			else
				world.scheduleBlockUpdate(x, y, z, this, tickRate(world) + world.rand.nextInt(10));
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta)
	{
		world.scheduleBlockUpdate(x, y, z, this, 10);
		return meta;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if (!canPlaceBlockAt(world, x, y, z))
		{
			world.setBlockToAir(x, y, z);
			return;
		}
		if (world.getGameRules().getGameRuleBooleanValue("doFireTick"))
		{
			Block base = world.getBlock(x, y - 1, z);
			boolean isFireSource = base.isFireSource(world, x, y - 1, z, UP);

			if (!isFireSource && U.Worlds.isCatchingRain(world, x, y, z, true))
				world.setBlockToAir(x, y, z);
			else
			{
				int l = world.getBlockMetadata(x, y, z);
				int k = base instanceof IBurnCustomBehaviorBlock ? ((IBurnCustomBehaviorBlock) base).getFireEncouragement(world, x, y - 1, z) : 0;
				label:
					if(!isFireSource)
					{
						int m = U.L.range(0, 15, l + rand.nextInt(3) / 2 - k);
						if(m == 15)
						{
							if (!canCatchFire(world, x, y - 1, z, UP) && rand.nextInt(4) == 0)
							{
								world.setBlockToAir(x, y, z);
								return;
							}
							break label;
						}
						else
							world.setBlockMetadataWithNotify(x, y, z, m, 4);
						if(!canNeighborBurn(world, x, y, z))
							if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) || m > 5)
							{
								world.setBlockToAir(x, y, z);
								return;
							}
					}
				world.scheduleBlockUpdate(x, y, z, this, tickRate(world) + rand.nextInt(10));
				boolean highHumidity = world.isBlockHighHumidity(x, y, z);
				byte hard = 0;
				if(highHumidity)
					hard = -50;

				for(Direction direction : Direction.directions)
					tryCatchFire(world, x - direction.x, y - direction.y, z - direction.z, 300 + hard, rand, l, direction);
				if(l < 15)
				{
					int p = k;
					int range = l < 3 ? 3 : l < 9 ? 2 : 1;
					for(int i = -range; i <= range; ++i)
						for(k = -range; k <= range; ++k)
							for(int j = -1; j <= range + 1; ++j)
								if(i != 0 || j != 0 || k != 0)
								{
									hard = 100;
									if(j > 0)
										hard -= j * 100 / (range + 2);
									int chance = getChanceOfNeighborsEncouragingFire(world, x + i, y + j, z + k);
									if(chance > 0)
									{
										int ref = (int) ((float) (chance + 40) / ((3 + Math.min(-1, l - p)) * (i * i + j * j + k * k)));
										if(highHumidity)
											ref /= 2;
										if(ref > 0 && rand.nextInt(hard) < ref)
										{
											Block block = world.getBlock(x, y - 1, z);
											if(block instanceof IBurnCustomBehaviorBlock &&
													((IBurnCustomBehaviorBlock) block).onBurn(world, x, y - 1, z, ref / 32F, Direction.U))
												continue;
											int m1 = U.L.range(0, 15, l + rand.nextInt(4) / 2);
											world.setBlock(x + i, y + j, z + k, this, m1, 3);
										}
									}
								}
				}
			}
		}
	}

	private void tryCatchFire(World world, int x, int y, int z, int hardness, Random rand, int meta, Direction face)
	{
		Block block = world.getBlock(x, y, z);
		int j1 = block.getFlammability(world, x, y, z, face.of());

		if (rand.nextInt(hardness) < j1)
		{
			if(block instanceof IBurnCustomBehaviorBlock &&
					((IBurnCustomBehaviorBlock) block).onBurningTick(world, x, y, z, rand, face))
				return;
			if (rand.nextInt(meta + 10) < 5)
			{
				int k1 = meta + rand.nextInt(5) / 4;
				if(block instanceof IBurnCustomBehaviorBlock)
					k1 -= ((IBurnCustomBehaviorBlock) block).getFireEncouragement(world, x, y, z);

				k1 = U.L.range(0, 15, k1);

				world.setBlock(x, y, z, this, k1, 3);
			}
			else
			{
				block.onBlockDestroyedByPlayer(world, x, y, z, 1);
				world.setBlockToAir(x, y, z);
			}
		}
	}

	/**
	 * Returns true if at least one block next to this one can burn.
	 */
	private boolean canNeighborBurn(World world, int x, int y, int z)
	{
		return canCatchFire(world, x + 1, y, z, WEST ) ||
				canCatchFire(world, x - 1, y, z, EAST ) ||
				canCatchFire(world, x, y - 1, z, UP   ) ||
				canCatchFire(world, x, y + 1, z, DOWN ) ||
				canCatchFire(world, x, y, z - 1, SOUTH) ||
				canCatchFire(world, x, y, z + 1, NORTH);
	}

	private int getChanceOfNeighborsEncouragingFire(World world, int x, int y, int z)
	{
		if (!world.isAirBlock(x, y, z))
			return 0;
		else
		{
			int l = getChanceToEncourageFire(world, x + 1, y, z, 0, WEST );
			l = getChanceToEncourageFire(world, x - 1, y, z, l, EAST );
			l = getChanceToEncourageFire(world, x, y - 1, z, l, UP   );
			l = getChanceToEncourageFire(world, x, y + 1, z, l, DOWN );
			l = getChanceToEncourageFire(world, x, y, z - 1, l, SOUTH);
			l = getChanceToEncourageFire(world, x, y, z + 1, l, NORTH);
			return l;
		}
	}

	/**
	 * Side sensitive version that calls the block function.
	 * @param world The current world
	 * @param x X Position
	 * @param y Y Position
	 * @param z Z Position
	 * @param oldChance The previous maximum chance.
	 * @param face The side the fire is coming from
	 * @return The chance of the block catching fire, or oldChance if it is higher
	 */
	public int getChanceToEncourageFire(IBlockAccess world, int x, int y, int z, int oldChance, ForgeDirection face)
	{
		int newChance = world.getBlock(x, y, z).getFireSpreadSpeed(world, x, y, z, face);
		return (newChance > oldChance ? newChance : oldChance);
	}

	public static boolean canCatchFire(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return world.getBlock(x, y, z).isFlammable(world, x, y, z, face);
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}
}