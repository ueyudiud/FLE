import java.io.File;

public class Debug
{
	public static void main(String[] args)
	{
		String[][] strs = {
				{"cuas", "arsenicbronze"}, {"cuas2", "higharsenicbronze"}, 
				{"cupb", "leadbronze"}, {"cupb2", "highleadbronze"}, 
				{"cusn", "tinbronze"}, {"cusn2", "hightinbronze"}, 
				{"cusnpb", "leadtinbronze"}};
		try
		{
			for(String[] str : strs)
				renameFile("./src/main/resources/assets/fle/textures/items/resource/plate", 
					str[0], 
					str[1]);
			for(String[] str : strs)
				renameFile("./src/main/resources/assets/fle/textures/items/resource/plateDouble", 
					str[0], 
					str[1]);
			for(String[] str : strs)
				renameFile("./src/main/resources/assets/fle/textures/items/resource/plateQuadruple", 
					str[0], 
					str[1]);
			for(String[] str : strs)
				renameFile("./src/main/resources/assets/fle/textures/items/resource/ingot", 
					str[0], 
					str[1]);
			for(String[] str : strs)
				renameFile("./src/main/resources/assets/fle/textures/items/resource/ingotDouble", 
					str[0], 
					str[1]);
			for(String[] str : strs)
				renameFile("./src/main/resources/assets/fle/textures/items/resource/ingotQuadruple",
					str[0], 
					str[1]);
			for(String[] str : strs)
				renameFile("./src/main/resources/assets/fle/textures/items/resource/stick", 
					str[0], 
					str[1]);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	public static void renameFile(String path, String oldname, String newname)
	{
		if(!oldname.equals(newname))
		{//新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(path + "/" + oldname + ".png"); 
			File newfile=new File(path + "/" + newname + ".png");
			if(!oldfile.exists())
			{
				return;//重命名文件不存在
			}
			if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名 
				System.out.println(newname + "已经存在！"); 
			else
				oldfile.renameTo(newfile);	 
		}
		else
		{
			System.out.println("新文件名和旧文件名相同...");
		}
	}
}