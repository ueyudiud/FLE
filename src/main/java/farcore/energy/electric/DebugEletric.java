/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.electric;

/**
 * @author ueyudiud
 */
public class DebugEletric
{
	public static void main(String[] args)
	{
		final int f = 5000;
		final double L = 1.0, C = 1.0, E = 1.0;
		
		Linker linker = new Linker(3);
		
		linker.bind(0, 1);
		linker.bind(1, 2);
		linker.bind(2, 0);
		
		Resolvor resolvor = new Resolvor();
		
		resolvor.initalize(linker);
		
		final double[] Rl = {0}, Cl = {L*f}, Re = {E}, Ce = null, Rc = {0}, Cc = {-1/(f*C)};
		resolvor.stampSimple(0, 1, Cl, Rl);
		resolvor.stampSimple(1, 2, Cc, Rc);
		resolvor.stampSimple(2, 0, Ce, Re);
		
		double U = 10.0, I = 0.0;
		for (int i = 0; i < 100; i ++)
		{
			for (int j = 0; j < f; j ++)
			{
				Rc[0] = U;
				Rl[0] = L*I*f;
				//			System.out.println(resolvor);
				resolvor.solve();
				//			System.out.println(Arrays.toString(resolvor.results()));
				U = resolvor.voltage(1, 2);
				I = resolvor.current(0, 1);
			}
			System.out.println(U + "," + I);
		}
		//		double[][] A = {
		//				{1.0, 1.0, 0.0},
		//				{1.0, 0.0, 1.0},
		//				{0.0, 1.0, 1.0}
		//		};
		//		double[] y = {
		//				2.0,
		//				3.0,
		//				4.0
		//		};
		//		double[] x = new double[3];
		//		EquationResolvor.gauss(3, A, x, y, null);
		//		System.out.println(Arrays.toString(x));
	}
}
