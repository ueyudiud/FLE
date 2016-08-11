package farcore.debug;

@Deprecated
public class Debug
{
	public static void main(String[] args)
	{
		for(int i = 0; i < 9999; ++i)
		{
			int j = i & (~0x2);
			System.out.println(i + " & ~2 = " + j);
		}
		//		MC.init();
		//		DebugMaterial.init();
		//		String sourceLocate = "";
		//		//		String srcDirName = "";
		//		//		String destDirName = "";
		//		//		String formatName = "chiseled.png";
		//		//		TextureCopier.copyTarget(srcDirName, destDirName, formatName);
		//		ModelFileCreator.provideGroupItemInfo(sourceLocate, MC.seed);
	}
}