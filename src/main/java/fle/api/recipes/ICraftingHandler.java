package fle.api.recipes;

public interface ICraftingHandler
{
	int getInputMatrixSize();

	ICraftingMatrix getInputMatrix(int index);
	
	int getOutputMatrixSize();

	ICraftingMatrix getOutputMatrix(int index);
}