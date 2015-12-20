package fle.core.util;

public enum EnumGreek
{
	alpha('Α', 'α'), 
	beta('Β', 'β'), 
	gamma('Γ', 'γ'), 
	delte('Δ', 'δ'), 
	epsilon('Ε', 'ε'),
	zeta('Ζ', 'ζ'),
	eta('Ε', 'η'), 
	theta('Θ', 'θ'), 
	iota('Ι', 'ι'), 
	kappa('Κ', 'κ'), 
	lambda('∧', 'λ'), 
	mu('Μ', 'μ'),
	nu('Ν', 'ν'), 
	xi('Ξ', 'ξ'), 
	omicron('Ο', 'ο'),
	pi('∏', 'π'),
	rho('Ρ', 'ρ'),
	sigma('∑', 'σ'), 
	tau('Τ', 'τ'), 
	upsilon('Υ', 'υ'),
	phi('Φ', 'φ'), 
	chi('Χ', 'χ'), 
	psi('Ψ', 'ψ'), 
	omega('Ω', 'ω');
	final String name;
	final char chr;
	
	EnumGreek(char higherCase, char chr)
	{
		this.chr = chr;
		name = new String(new char[]{chr});
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}