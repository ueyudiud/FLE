package farcore.debug;

import farcore.data.MC;

@Deprecated
public class Debug
{
	public static void main(String[] args)
	{
		MC.init();
		DebugMaterial.init();
		String sourceLocate = "";
		//		String srcDirName = "";
		//		String destDirName = "";
		//		String formatName = "chiseled.png";
		//		TextureCopier.copyTarget(srcDirName, destDirName, formatName);
		ModelFileCreator.provideGroupItemInfo(sourceLocate, MC.seed);
	}
}