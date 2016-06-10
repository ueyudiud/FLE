package farcore.lib.crop;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.util.LanguageManager;
import farcore.util.V;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class CropVoid implements CropCard
{
	CropVoid() {	}
	
	public void decodeDNA(ICropAccess biology, String dna){	}

	@Override
	public String makeNativeDNA()
	{
		return "";
	}

	@Override
	public String makeOffspringDNA(String par1, String par2)
	{
		return "";
	}

	@Override
	public String name()
	{
		return "void";
	}

	@Override
	public String instanceDNA(Random random)
	{
		return "";
	}

	@Override
	public int getMaxStage()
	{
		return 1;
	}

	@Override
	public int getGrowReq(ICropAccess access)
	{
		return 1000;
	}

	@Override
	public long tickUpdate(ICropAccess access)
	{
		return 1000L;
	}

	@Override
	public void getDrops(ICropAccess access, List<ItemStack> list) {	}

	@Override
	public void onUpdate(ICropAccess access) {	}

	@Override
	public void addInformation(ICropAccess access, List<String> infos) {	}

	@SideOnly(Side.CLIENT)
	public void registerIcon(IIconRegister register) {	}

	@SideOnly(Side.CLIENT)
	public EnumRenderType getRenderType()
	{
		return EnumRenderType.cross;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack)
	{
		return V.voidBlockIcon;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ICropAccess access)
	{
		return V.voidBlockIcon;
	}

	@Override
	public String makeChildDNA(int generation, String par)
	{
		return par;
	}

	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		
	}

	@Override
	public boolean canPlantAt(World world, int x, int y, int z)
	{
		return true;
	}
}