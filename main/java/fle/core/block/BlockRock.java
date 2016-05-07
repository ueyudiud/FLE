package fle.core.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.energy.thermal.ThermalNet;
import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.substance.SubstanceRock;
import farcore.util.LanguageManager;
import farcore.util.U;
import fle.api.block.BlockSubstance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockRock extends BlockSubstance<SubstanceRock>
{
	public static void init()
	{
		Register<Block> registerRock = new Register();
		Register<Block> registerCobble = new Register();
		Register<Block> registerBrick = new Register();
		for(SubstanceRock rock : SubstanceRock.getRocks())
		{
			Block block1 = new BlockRock(rock, registerRock),
					block2 = new BlockCobble(rock, registerCobble),
					block3 = new BlockBrick(rock, registerBrick);
			registerRock.register(rock.getName(), block1);
			registerCobble.register(rock.getName(), block2);
			registerBrick.register(rock.getName(), block3);
			if(rock == SubstanceRock.VOID_ROCK)
			{
				EnumItem.rock_block.set(new ItemStack(block1));
				EnumBlock.rock.setBlock(block1, EnumItem.rock_block);	
				EnumItem.cobble_block.set(new ItemStack(block2));
				EnumBlock.cobble.setBlock(block2, EnumItem.cobble_block);
				EnumItem.brick_block.set(new ItemStack(block3));		
				EnumBlock.brick.setBlock(block3, EnumItem.brick_block);
			}
		}
	}
	
	public BlockRock(SubstanceRock rock, IRegister<Block> register)
	{
		super("rock", Material.rock, rock, register);
		setBlockTextureName("fle:rock");
		setCreativeTab(CreativeTabs.tabBlock);
		setTickRandomly(true);
	}
	
	@Override
	public Object[] getTranslateObject(ItemStack stack)
	{
		return new Object[]{substance.getLocalName()};
	}
	
	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		manager.registerLocal(getUnlocalizedName() + ".name", "%s Rock");
	}
	
	@Override
	public int tickRate(World world)
	{
		return 240;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		super.onNeighborBlockChange(world, x, y, z, block);
		world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if(world.isRemote) return;
		if(!U.Worlds.isBlockNearby(world, x, y, z, Blocks.air, -1, true))
		{
			return;
		}
		int meta = world.getBlockMetadata(x, y, z);
		if(meta < 3)
		{
			if(U.Worlds.isBlockNearby(world, x, y, z, EnumBlock.water.block(), -1, true))
			{
				return;
			}
			int deltaTemp = (int) ThermalNet.getTempDifference(world, x, y, z);
			if(deltaTemp < 30)
			{
				if(meta > 0)
				{
					world.setBlockMetadataWithNotify(x, y, z, meta - 1, 0);
				}
			}
			else if(deltaTemp > 149 || (deltaTemp >= 50 && rand.nextInt(150) < deltaTemp))
			{
				if(meta == 0)
				{	
					if(rand.nextBoolean())
					{
						world.setBlockMetadataWithNotify(x, y, z, 1, 0);
						world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
					}
				}
				else
				{
					if(rand.nextBoolean())
					{
						world.setBlockMetadataWithNotify(x, y, z, meta + 1, 0);
						 world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
					}
				}
			}
		}
		else if(meta == 3)
		{
			if(U.Worlds.isBlockNearby(world, x, y, z, EnumBlock.water.block(), -1, true))
			{
				EnumBlock.cobble.spawn(world, x, y, z, substance);
			}
			else
			{
				 if(ThermalNet.getTempDifference(world, x, y, z) < 30 && rand.nextBoolean())
				 {
					 world.setBlockMetadataWithNotify(x, y, z, meta - 1, 0);
				 }
				 else
				 {
					 world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
				 }
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 3)
		{
			double u1, v1, t1;
			for(int i = 0; i < 2; ++i)
			{
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", (double) x + u1, (double) y + v1, (double) z - 0.1, 0D, 0D, t1);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .1;
				world.spawnParticle("depthsuspend", (double) x + u1, (double) y, (double) z + v1, 0D, t1, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", (double) x - 0.1, (double) y + u1, (double) z + v1, t1, 0D, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", (double) x + u1, (double) y + v1, (double) z + 1.1, 0D, 0D, t1);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				world.spawnParticle("depthsuspend", (double) x + u1, (double) y + 1D, (double) z + v1, 0D, 0D, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", (double) x + 1.1, (double) y + u1, (double) z + v1, t1, 0D, 0D);
			}
		}
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = substance.icon = register.registerIcon(getTextureName() + "/" + substance.getName() + "/resource");
	}

	@Override
	public boolean spawn(World world, int x, int y, int z, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				return spawn(world, x, y, z, register.get((String) objects[0]));
			}
			else if(objects[0] instanceof SubstanceRock)
			{
				Block block = register.get(((SubstanceRock) objects[0]).getName());
				if(block != null)
				{
					return world.setBlock(x, y, z, block);
				}
			}
		}
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
			boolean silkTouching)
	{
		ArrayList<ItemStack> list = new ArrayList();
		if(silkTouching)
		{
			list.add(new ItemStack(this));
		}
		else
		{
			ItemStack stack = EnumItem.cobble_block.instance(1, substance);
			if(stack != null)
			{
				list.add(stack);
			}
		}
		return list;
	}
}