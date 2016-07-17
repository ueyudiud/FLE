import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

public class Debug
{
	private static String MESSAGE = "";

	/**文件重命名
	 * @param path 文件目录
	 * @param oldname  原来的文件名
	 * @param newname 新文件名
	 */
	public void renameFile(String path, String oldname, String newname)
	{
		if(!oldname.equals(newname))
		{
			//新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(path + "/" + oldname);
			File newfile = new File(path + "/" + newname);
			if(!oldfile.exists())
				return;
			if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				System.out.println(newname + "已经存在！");
			else
				oldfile.renameTo(newfile);
		}
		else
			System.out.println("新文件名和旧文件名相同...");
	}

	/**
	 * 复制单个文件
	 *
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @param overlay
	 *            如果目标文件存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String destFileName,
			boolean overlay)
	{
		File srcFile = new File(srcFileName);

		// 判断源文件是否存在
		if (!srcFile.exists())
		{
			MESSAGE = "源文件：" + srcFileName + "不存在！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}
		else if (!srcFile.isFile())
		{
			MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}

		// 判断目标文件是否存在
		File destFile = new File(destFileName);
		if (destFile.exists())
		{
			// 如果目标文件存在并允许覆盖
			if (overlay)
				// 删除已经存在的目标文件，无论目标文件是目录还是单个文件
				new File(destFileName).delete();
		}
		else // 如果目标文件所在目录不存在，则创建目录
			if (!destFile.getParentFile().exists())
				// 目标文件所在目录不存在
				if (!destFile.getParentFile().mkdirs())
					// 复制文件失败：创建目标文件所在目录失败
					return false;

		// 复制文件
		int byteread = 0; // 读取的字节数
		InputStream in = null;
		OutputStream out = null;

		try
		{
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];

			while ((byteread = in.read(buffer)) != -1)
				out.write(buffer, 0, byteread);
			return true;
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
		finally
		{
			try
			{
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 复制整个目录的内容
	 *
	 * @param srcDirName
	 *            待复制目录的目录名
	 * @param destDirName
	 *            目标目录名
	 * @param overlay
	 *            如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String destDirName,
			boolean overlay)
	{
		// 判断源目录是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists())
		{
			MESSAGE = "复制目录失败：源目录" + srcDirName + "不存在！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}
		else if (!srcDir.isDirectory())
		{
			MESSAGE = "复制目录失败：" + srcDirName + "不是目录！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}

		// 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
		if (!destDirName.endsWith(File.separator))
			destDirName = destDirName + File.separator;
		File destDir = new File(destDirName);
		// 如果目标文件夹存在
		if (destDir.exists())
		{
			// 如果允许覆盖则删除已存在的目标目录
			if (overlay)
				new File(destDirName).delete();
			else
			{
				MESSAGE = "复制目录失败：目的目录" + destDirName + "已存在！";
				JOptionPane.showMessageDialog(null, MESSAGE);
				return false;
			}
		}
		else
		{
			// 创建目的目录
			System.out.println("目的目录不存在，准备创建。。。");
			if (!destDir.mkdirs())
			{
				System.out.println("复制目录失败：创建目的目录失败！");
				return false;
			}
		}

		boolean flag = true;
		File[] files = srcDir.listFiles();
		for (File file : files)
			// 复制文件
			if (file.isFile())
			{
				flag = copyFile(file.getAbsolutePath(),
						destDirName + file.getName(), overlay);
				if (!flag)
					break;
			}
			else if (file.isDirectory())
			{
				flag = copyDirectory(file.getAbsolutePath(),
						destDirName + file.getName(), overlay);
				if (!flag)
					break;
			}
		if (!flag)
		{
			MESSAGE = "复制目录" + srcDirName + "至" + destDirName + "失败！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		} else
			return true;
	}

	public static boolean copyTarget(String srcDirName, String targetDirName, String formatName)
	{
		// 判断源目录是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists())
		{
			MESSAGE = "复制目录失败：源目录" + srcDirName + "不存在！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}
		else if (!srcDir.isDirectory())
		{
			MESSAGE = "复制目录失败：" + srcDirName + "不是目录！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}
		File targetDir = new File(targetDirName);
		File subDir;
		for(File file : srcDir.listFiles())
		{
			String name = file.getName();
			name = name.substring(0, name.indexOf('.'));
			subDir = new File(targetDir, name);
			if(!subDir.exists())
				subDir.mkdirs();
			copyFile(file.getAbsolutePath(), new File(subDir, formatName).getAbsolutePath(), true);
		}

		return true;
	}

	public static void main(String[] args)
	{
		String srcDirName = "";
		String destDirName = "";
		String formatName = "brick_crushed.png";
		copyTarget(srcDirName, destDirName, formatName);
	}
}