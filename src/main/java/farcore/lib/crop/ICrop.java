package farcore.lib.crop;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.bio.IDNADecoder;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.IRegisteredNameable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface ICrop extends IRegisteredNameable, IDNADecoder<ICropAccess>
{
	@SideOnly(Side.CLIENT)
	public static enum EnumRenderType
	{
		cross,
		lattice;
	}
	
	ICrop VOID = new CropVoid();

	String getLocalName(String dna);

	int getMaxStage();

	long tickUpdate(ICropAccess access);

	void onUpdate(ICropAccess access);

	int getGrowReq(ICropAccess access);

	void addInformation(ICropAccess access, List<String> list);

	boolean canPlantAt(ICropAccess access);

	void getDrops(ICropAccess access, ArrayList<ItemStack> list);

	@SideOnly(Side.CLIENT)
	void registerIcon(INamedIconRegister register);

	@SideOnly(Side.CLIENT)
	IIcon getIcon(ICropAccess access, INamedIconRegister register);

	@SideOnly(Side.CLIENT)
	EnumRenderType getRenderType();
}