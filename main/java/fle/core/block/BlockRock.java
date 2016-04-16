package fle.core.block;

import java.util.ArrayList;
import java.util.Random;

import farcore.energy.thermal.ThermalNet;
import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.substance.SubstanceRock;
import fle.api.block.BlockSubstance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockRock extends BlockSubstance<SubstanceRock>
{
	public static void init()
	{
		Register<Block> registerRock = new Register();
		Register<Block> registerCobble = new Register();
		for(SubstanceRock rock : SubstanceRock.getRocks())
		{
			Block block1 = new BlockRock(rock, registerRock),
					block2 = new BlockCobble(rock, registerCobble);
			registerRock.register(rock.getName(), block1);
			registerCobble.register(rock.getName(), block2);
			if(rock == SubstanceRock.VOID_ROCK)
			{
				EnumItem.rock_block.set(new ItemStack(block1));
				EnumBlock.rock.setBlock(block1, EnumItem.rock_block);	
				EnumItem.cobble_block.set(new ItemStack(block2));
				EnumBlock.cobble.setBlock(block2, EnumItem.cobble_block);				
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
	public int tickRate(World world)
	{
		return 4;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if(world.isRemote) return;
		int deltaTemp = (int) ThermalNet.getTempDifference(world, x, y, z);
		if(deltaTemp < 50) return;
		if(deltaTemp > 149 || rand.nextInt(150) < deltaTemp)
		{
			int meta = world.getBlockMetadata(x, y, z);
			if(meta == 0)
			{	
				if(rand.nextInt(5) == 0)
				{
					world.setBlockMetadataWithNotify(x, y, z, 1, 3);
				}
			}
			else
			{
				if(meta == 3)
				{
					EnumBlock.cobble.spawn(world, x, y, z, substance);
				}
				else if(rand.nextBoolean())
				{
					world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
				}
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