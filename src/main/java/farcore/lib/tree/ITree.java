package farcore.lib.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.data.EnumToolType;
import farcore.lib.bio.IDNADecoder;
import farcore.lib.block.instance.BlockCoreLeaves;
import farcore.lib.block.instance.BlockLeaves;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.material.Mat;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.IRegisteredNameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public interface ITree extends IRegisteredNameable, IDNADecoder<TreeInfo>
{
	ITree VOID = new TreeVoid();
	
	Mat material();
	
	void initInfo(BlockLogNatural logNatural, BlockLogArtificial logArtificial, BlockLeaves leaves, BlockCoreLeaves leavesCore);
	
	boolean tickLogUpdate();
	
	@SideOnly(Side.CLIENT)
	void registerLogIcon(INamedIconRegister register);

	@SideOnly(Side.CLIENT)
	void registerLeavesIcon(INamedIconRegister register);

	@SideOnly(Side.CLIENT)
	IIcon getLogIcon(INamedIconRegister register, int meta, int side);

	@SideOnly(Side.CLIENT)
	IIcon getLeavesIcon(INamedIconRegister register, int meta, int side);

	void updateLog(World world, int x, int y, int z, Random rand, boolean isArt);

	void updateLeaves(World world, int x, int y, int z, Random rand);

	void breakLog(World world, int x, int y, int z, int meta, boolean isArt);

	void breakLeaves(World world, int x, int y, int z, int meta);

	void beginLeavesDency(World world, int x, int y, int z);

	boolean onLogRightClick(EntityPlayer player, World world, int x, int y, int z, int side, float xPos, float yPos,
			float zPos, boolean isArt);

	float onToolClickLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ, boolean isArt);

	float onToolClickLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ);
	
	float onToolUseLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, int x, int y,
			int z, int side, float hitX, float hitY, float hitZ, boolean isArt);

	float onToolUseLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, int x,
			int y, int z, int side, float hitX, float hitY, float hitZ);
	
	List<ItemStack> getLogOtherDrop(World world, int x, int y, int z, ArrayList list);

	ArrayList<ItemStack> getLeavesDrops(World world, int x, int y, int z, int metadata, int fortune,
			boolean silkTouching, ArrayList arrayList);
	
	boolean canGenerateTreeAt(World world, int x, int y, int z, TreeInfo info);
	
	void generateTreeAt(World world, int x, int y, int z, TreeInfo info);

	int onSaplingUpdate(ISaplingAccess access);

	int getGrowAge();
}