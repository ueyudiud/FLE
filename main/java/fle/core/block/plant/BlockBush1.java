package fle.core.block.plant;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.ItemBlockBase;
import farcore.block.plant.BlockClassicalBush;
import farcore.enums.Direction;
import farcore.enums.EnumBlock;
import farcore.enums.EnumBlock.IInfoSpawnable;
import farcore.enums.EnumItem;
import farcore.lib.collection.Ety;
import farcore.lib.recipe.DropHandler;
import farcore.lib.stack.BaseStack;
import farcore.util.LanguageManager;
import farcore.util.U;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBush1 extends BlockClassicalBush implements IInfoSpawnable
{
	private static final String[] names = {
			"bush_ivy",
			"bush_rattan"
		};
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	public BlockBush1(String name)
	{
		super("bush1", ItemBlockBase.class, true);
		EnumBlock.bush.setBlock(this);
		setHardness(0.3F);
		setResistance(0.05F);
	}

	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		manager.registerLocal(getUnlocalizedName() + "@" + names[0] + ".name", "Ivy Bush");
		manager.registerLocal(getUnlocalizedName() + "@" + names[1] + ".name", "Rattan Bush");
	}
	
	@Override
	public int getRenderType() 
	{
		return 0;
	}
	
	@Override
	public String getMetadataName(int meta)
	{
		return names[meta];
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		icons = new IIcon[names.length];
		for(int i = 0; i < icons.length; ++i)
			icons[i] = register.registerIcon(getTextureName() + "/" + names[i]);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return icons[meta % icons.length];
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if(!canBlockStay(world, x, y, z))
		{
			world.setBlockToAir(x, y, z);
		}
		else
		{
			if(world.getBlockLightValue(x, y + 1, z) > 7 && rand.nextInt(7) == 0)
			{
				Direction direction = U.Lang.randomSelect(Direction.directions_2D, rand);
				if(world.isAirBlock(x + direction.x, y + direction.y, z + direction.z))
				{
					world.setBlock(x + direction.x, y + direction.y, z + direction.z, EnumBlock.vine.block(), world.getBlockMetadata(x, y, z), 3);
				}
			}
		}
	}
	
	@Override
	protected DropHandler getDropHandler(int meta)
	{
		switch (meta)
		{
		case 0 : return new DropHandler(2, 
				new Ety(new BaseStack(EnumItem.plant.instance(1, "leaves_1")), 2),
				new Ety(new BaseStack(EnumItem.plant.instance(1, "vine")), 1));
		case 1 : return new DropHandler(2, 
				new Ety(new BaseStack(EnumItem.plant.instance(1, "leaves_1")), 2),
				new Ety(new BaseStack(EnumItem.plant.instance(1, "vine")), 1));
		default: return DropHandler.EMPTY;
		}
	}
	
	@Override
	public boolean spawn(World world, int x, int y, int z, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				String tag = (String) objects[0];
				int i = 0;
				for(; i < names.length && !names[i].equals(tag); 
						i++);
				if(i < names.length)
				{
					return world.setBlock(x, y, z, this, i, 3);
				}
			}
		}
		return false;
	}
}