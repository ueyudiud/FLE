package farcore.alpha.interfaces.block.tree;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.interfaces.block.IToolableBlock;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public interface IBlockLogProp extends IToolableBlock
{
	@SideOnly(Side.CLIENT)
	void registerIcon(INamedIconRegister register);
	
	@SideOnly(Side.CLIENT)
	IIcon getIcon(int side, int meta, INamedIconRegister register);
	
	boolean tickUpdate();
	
	void onUpdate(World world, int x, int y, int z, Random rand);
}