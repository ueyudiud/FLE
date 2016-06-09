package farcore.lib.crop;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.enums.EnumBlock;
import farcore.lib.bio.IDNADecoder;
import farcore.util.LanguageManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public interface CropCard extends IDNADecoder<ICropAccess>
{
	public static enum EnumRenderType
	{
		cross,
		lattice;
	}

	public static final CropCard VOID = new CropVoid();
	
	default void setCrop(World world, int x, int y, int z, int generations, String dna)
	{
		EnumBlock.crop.spawn(world, x, y, z);
		TileEntity tileRaw = world.getTileEntity(x, y, z);
		if(tileRaw instanceof TileEntityCrop)
		{
			((TileEntityCrop) tileRaw).initCrop(generations, dna, this);
		}
	}

	default void setCrop(World world, int x, int y, int z)
	{
		EnumBlock.crop.spawn(world, x, y, z);
		TileEntity tileRaw = world.getTileEntity(x, y, z);
		if(tileRaw instanceof TileEntityCrop)
		{
			((TileEntityCrop) tileRaw).initCrop(this);
		}
	}
	
	String name();
	
	default String getName(String dna)
	{
		return name();
	}
	
	default String getTranslateName(String dna)
	{
		return getName(dna);
	}
	
	default String getLocalizedName(String dna)
	{
		return FarCore.translateToLocal("crop." + getTranslateName(dna) + ".name");
	}
	
	String instanceDNA(Random random);
	
	int getMaxStage();
	
	int getGrowReq(ICropAccess access);
	
	long tickUpdate(ICropAccess access);
	
	void getDrops(ICropAccess access, List<ItemStack> list);
	
	void onUpdate(ICropAccess access);
	
	void addInformation(ICropAccess access, List<String> infos);
	
	@SideOnly(Side.CLIENT)
	void registerIcon(IIconRegister register);

	@SideOnly(Side.CLIENT)
	void registerLocalizedName(LanguageManager manager);
	
	@SideOnly(Side.CLIENT)
	EnumRenderType getRenderType();
	
	@SideOnly(Side.CLIENT)
	IIcon getIcon(ItemStack stack);
	
	@SideOnly(Side.CLIENT)
	IIcon getIcon(ICropAccess access);

	boolean canPlantAt(World world, int x, int y, int z);
}