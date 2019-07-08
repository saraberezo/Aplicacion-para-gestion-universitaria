package interfaces;


public interface Comparator {

	int L=1, M=2, X=3, J=4, V=5; 
	
	
	/**
	 * M�todo que devuelve el n�mero del d�a de la semana;
	 * @return int					int con el n�mero del d�a de la semana en el que se imparte el grupo del tipo de la asignatura
	 */
	int getDiaSemana();
	
	
	/**
	 * M�todo que compara dos objetos y devuelve -1, 0 � 1 dependiendo si el primer objeto es m�s peque�o, igual o m�s grande que el segundo objeto.
	 * @param objeto1				objeto1 a comparar
	 * @param objeto2				objeto2 a comparar
	 * @return int					int que se corresponde -1, 0 � 1 dependiendo si el primer objeto es menor, igual o mayor que el segundo					
	 */
	int comapare(Object objeto1, Object objeto2);
	
	
}
