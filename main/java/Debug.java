import java.util.Random;

public class Debug
{
	public static void main(String[] args)
	{
		int[] s = new int[1024];
		Random rand = new Random();
		for(int i = 0; i < s.length; ++i)
		{
			s[i] = rand.nextInt(10);
		}
		for(int loop = 0; loop < 1024; ++loop)
		{
			int j = 0;
			for(int i = 0; i < s.length * 10; ++i)
				if(randInt(rand, s) == randInt(rand, s))
					++j;
			System.out.print(j + "\r");
		}
	}
	
	private static int randInt(Random rand, int...is)
	{
		return is[rand.nextInt(is.length)];
	}
}