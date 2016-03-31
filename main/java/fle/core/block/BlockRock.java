package fle.core.block;

import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.lib.substance.SubstanceRock;
import fle.api.block.BlockSubstance;
import fle.api.tile.TileEntitySubstance;
import fle.core.tile.statics.TileEntityRock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRock extends BlockSubstance<SubstanceRock>
{
	public BlockRock()
	{
		super("rock", Material.rock);
		EnumItem.rock_block.set(new ItemStack(this));
		EnumBlock.rock.setBlock(this, EnumItem.rock_block);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		for(SubstanceRock rock : this.register)
		{
			icons.put(rock, rock.icon = register.registerIcon(getTextureName() + "/" + rock.getName() + "/resource"));
		}
	}

	@Override
	public void spawn(World world, int x, int y, int z, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				spawn(world, x, y, z, register.get((String) objects[0]));
			}
			else if(objects[0] instanceof SubstanceRock)
			{
				world.setBlock(x, y, z, this);
				if(world.getTileEntity(x, y, z) instanceof TileEntityRock)
				{
					((TileEntityRock) world.getTileEntity(x, y, z)).substance = (SubstanceRock) objects[0];
				}
			}
		}
	}

	@Override
	public TileEntityRock createNewTileEntity(World world, int meta)
	{
		return new TileEntityRock();
	}
}