package farcore.debug;

import farcore.lib.material.Mat;
import farcore.lib.util.SubTag;

@Deprecated
public class Debug
{
	public static void main(String[] args)
	{
		DebugMaterial.init();
		String sourceLocate = "";
		//		String srcDirName = "";
		//		String destDirName = "";
		//		String formatName = "chiseled.png";
		//		TextureCopier.copyTarget(srcDirName, destDirName, formatName);
		for (Mat material : Mat.register)
		{
			if (material.contain(SubTag.WOOD))
			{
				ModelFileCreator.provideLogAndLeavesInfo(sourceLocate, material);
				//				ModelFileCreator.provideRockInfo(sourceLocate, material);
				//				ModelFileCreator.provideRockSlabInfo(sourceLocate, material);
			}
		}
	}
}