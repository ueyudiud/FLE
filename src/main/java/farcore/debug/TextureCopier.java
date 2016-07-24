package farcore.debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

public class TextureCopier
{
	private static String MESSAGE = "";
	
	public void renameFile(String path, String oldname, String newname)
	{
		if(!oldname.equals(newname))
		{
			File oldfile = new File(path + "/" + oldname);
			File newfile = new File(path + "/" + newname);
			if(!oldfile.exists())
				return;
			if(newfile.exists())
			{
				System.out.println(newname + " already exists!");
			}
			else
			{
				oldfile.renameTo(newfile);
			}
		}
		else
		{
			System.out.println("The new file name is same to old name...");
		}
	}
	
	public static boolean copyFile(String srcFileName, String destFileName,
			boolean overlay)
	{
		File srcFile = new File(srcFileName);
		
		if (!srcFile.exists())
		{
			MESSAGE = "Source file:" + srcFileName + " does not exist£¡";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}
		else if (!srcFile.isFile())
		{
			MESSAGE = "Copy file failed, source file : " + srcFileName + " is not a file!";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}
		
		File destFile = new File(destFileName);
		if (destFile.exists())
		{
			if (overlay)
			{
				new File(destFileName).delete();
			}
		}
		else
			if (!destFile.getParentFile().exists())
				if (!destFile.getParentFile().mkdirs())
					return false;
		
		int byteread = 0;
		InputStream in = null;
		OutputStream out = null;
		
		try
		{
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			
			while ((byteread = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, byteread);
			}
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
				{
					out.close();
				}
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static boolean copyDirectory(String srcDirName, String destDirName,
			boolean overlay)
	{
		File srcDir = new File(srcDirName);
		if (!srcDir.exists())
		{
			MESSAGE = "Copy path failed: src file " + srcDirName + " does not exist!";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}
		else if (!srcDir.isDirectory())
		{
			MESSAGE = "Copy path failed: file " + srcDirName + " is not a path!";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}
		
		if (!destDirName.endsWith(File.separator))
		{
			destDirName = destDirName + File.separator;
		}
		File destDir = new File(destDirName);
		if (destDir.exists())
		{
			if (overlay)
			{
				new File(destDirName).delete();
			}
			else
			{
				MESSAGE = "Copy file failed:" + destDirName + " already exist!";
				JOptionPane.showMessageDialog(null, MESSAGE);
				return false;
			}
		}
		else
		{
			System.out.println("Creating target path...");
			if (!destDir.mkdirs())
			{
				System.out.println("Failed to create file!");
				return false;
			}
		}
		
		boolean flag = true;
		File[] files = srcDir.listFiles();
		for (File file : files)
			if (file.isFile())
			{
				flag = copyFile(file.getAbsolutePath(),
						destDirName + file.getName(), overlay);
				if (!flag)
				{
					break;
				}
			}
			else if (file.isDirectory())
			{
				flag = copyDirectory(file.getAbsolutePath(),
						destDirName + file.getName(), overlay);
				if (!flag)
				{
					break;
				}
			}
		if (!flag)
		{
			MESSAGE = "Copy " + srcDirName + " to" + destDirName + " fail!";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		} else
			return true;
	}
	
	public static boolean copyTarget(String srcDirName, String targetDirName, String formatName)
	{
		File srcDir = new File(srcDirName);
		if (!srcDir.exists())
		{
			MESSAGE = "File " + srcDirName + " does not exist!";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}
		else if (!srcDir.isDirectory())
		{
			MESSAGE = "Fail " + srcDirName + " is not file!";
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
			{
				subDir.mkdirs();
			}
			copyFile(file.getAbsolutePath(), new File(subDir, formatName).getAbsolutePath(), true);
		}
		
		return true;
	}
}