package asignaturasYdocencia;

//CLASES IMPORTADAS DE JAVA

import interfaces.Comparator;


public class Grupos implements Comparator{

	//ATRIBUTOS
	
    private int idGrupo;
    private String dia;
    private int hora;

    
    //CONSTRUCTORES
    
    /**
     * 
     * Constructor vac�o de la clase Grupos.
     */
    Grupos (){
    	
    }
    
    /**
     * Constructor en el cual se le asignan al objeto los atributos de la clase Grupos.
     * @param idGrupo				ID del grupo
     * @param dia					D�a en el que se imparte
     * @param hora					Hora a la que se imparte
     */
    Grupos (int idGrupo, String dia, int hora){
    	this.idGrupo=idGrupo;
    	this.dia=dia;
    	this.hora=hora;
    }
    
    
    //METODOS
    
    /**
     * M�todo toString() de la clase Object sobreescrito. Devuelve un String con los atributos principales del objeto de la clase Grupos.
     * @return String				String con los atributos del objeto de la clase Grupos
     */
    public String toString(){
    	return (idGrupo+" "+dia+" "+hora);
    }
    
    //Implemetado de la interfaz Comparator
    
    /**
     * M�todo compare() de la interfaz Comparator implementado.
     * @return int					int que se corresponde -1, 0 � 1 dependiendo si el primer objeto es menor, igual o mayor que el segundo
     */
	public int comapare(Object grupo1, Object grupo2) {
		if (((Grupos) grupo1).getDiaSemana() == ((Grupos) grupo2).getDiaSemana())
			return (((Grupos) grupo1).hora - ((Grupos) grupo2).hora);
		else return (((Grupos) grupo1).getDiaSemana() - ((Grupos) grupo2).getDiaSemana());	
	}
    
	/**
	 * M�todo que comprueba si entre dos grupos se genera solape en sus horarios.
	 * @param duracionDocencia		int con la duracion de la docencia del objeto
	 * @param asignar				objeto de la clase Grupos que se quiere a�adir a la docencia
	 * @param duracionAsignar		duraci�n del objeto que se quiere a�adir a la docencia
	 * @return boolean				true si se solapan los horarios de las dos asignaturas y false si no se solapan
	 */
	public boolean generaSolape (int duracionDocencia, Grupos asignar, int duracionAsignar){
		if( ((hora+duracionDocencia)<=(asignar.hora+duracionAsignar)) && ((hora+duracionDocencia)>asignar.hora) ) return true;
		else{
			if( ((asignar.hora+duracionAsignar)<=(hora+duracionDocencia)) && ((asignar.hora+duracionAsignar)>(hora)) ) return true;
		}
		return false;
	}
	
    
    //Getters
	
	/**
	 * M�todo get() que devuelve el ID del grupo.
	 * @return idGrupo				int con el ID del grupo
	 */
    public int getIDgrupo(){
    	return idGrupo;
    }

    /**
     * M�todo get() que devuelve el d�a en el que se imparte el grupo.
     * @return dia					String con el dia en el que se imparte el grupo del tipo de la asignatura
     */
    public String getDia(){
    	return dia;
    }
    
    /**
     * M�todo get() que devuelve la hora del dia (formato 24 horas) en la cual se imparte el grupo.
     * @return hora					int con la hora a la que se imparte el grupo del tipo de la asignatura
     */
    public int getHora(){
    	return hora;
    }

    //Implemetado de la interfaz Comparator
    
    /**
     * M�todo getDiaSemana() implementado de la interfaz Comparator. Devuelve el n�mero del d�a de la semana en el cual se imaprte el grupo.
     * @return int					int con el n�mero del d�a de la semana en el que se imparte el grupo del tipo de la asignatura
     */
    public int getDiaSemana(){
    	if (dia.equals("L")) return L;
    	if (dia.equals("M")) return M;
    	if (dia.equals("X")) return X;
    	if (dia.equals("J")) return J;
    	else return V;
    }
    

}