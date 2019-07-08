package asignaturasYdocencia;

//CLASES IMPORTADAS DE JAVA

import java.util.Comparator;


public class Docencia implements Comparator<Object>{ 

	//ATRIBUTOS
	
    private int idGrupo;
    private String tipoGrupo;
    private int idAsignatura;
    public Grupos infoGrupo;
	
    
    //CONSTRUCTORES 
    
    /**
     *
     * Constructor vacío de la clase Docencia.
     */
    public Docencia(){
    	
    }
    
    /**
     * Constructor en el cual se le asignan al objeto los tres atributos principales de la clase Docencia.
     * @param idAsignatura					ID de la asignatura
     * @param tipoGrupo						Tipo de grupo del grupo (T o P)
     * @param idGrupo						ID del grupo
     */
    public Docencia(int idAsignatura, String tipoGrupo, int idGrupo){
    	this.idGrupo=idGrupo;
    	this.tipoGrupo=tipoGrupo;
    	this.idAsignatura=idAsignatura;
    }
    
    /**
     * Constructor que hace uso del constructor anterior y que además obtiene el calendario de una docencia determinada.
     * @param idAsignatura					ID de la asignatura
     * @param tipoGrupo						Tipo de grupo del grupo (T o P)
     * @param idGrupo						ID del grupo
     * @param asignatura					Informacion de la asignatura
     */
    public Docencia (int idAsignatura, String tipoGrupo, int idGrupo, Asignatura asignatura){ 	
    	this (idAsignatura, tipoGrupo, idGrupo);
    	String cadenaGrupos;
    	if (tipoGrupo.equals("T")) cadenaGrupos=asignatura.getGruposTeoria();    	    	
    	else cadenaGrupos=asignatura.getGruposPractica();
    	if (!(cadenaGrupos.isEmpty())){
    		String[]cadenaGruposTP=cadenaGrupos.split(";");
    		for (int i=0; i<cadenaGruposTP.length; i++){
    			String conjunto=cadenaGruposTP[i].trim();
    			String[] dividir=conjunto.split("\\s+");
    			Grupos grupoTP = new Grupos(Integer.parseInt(dividir[0]), dividir[1], Integer.parseInt(dividir[2]));
    			if (grupoTP.getIDgrupo()==idGrupo) {
    				infoGrupo=grupoTP; break;
    			}
    		}
    	}
	}
   
    
    //METODOS
    
    /**
     * Método toString() de la clase Object sobreescrito. Devuelve un String con los atributos principales del objeto de la clase Docencia.
     * @return String				String con los atributos del objeto de la clase Docencia
     */
    public String toString(){
    	return (idAsignatura+" "+tipoGrupo+" "+idGrupo);
    }
    
    /**
     * Método compare() de la interfaz Comparator implementado. Llama al método implementado compare() de la clase Grupos.
     * @return int					int que se corresponde -1, 0 ó 1 dependiendo si el primer objeto es menor, igual o mayor que el segundo
     */
	public int compare(Object docencia1, Object docencia2) {
		return ((new Grupos()).comapare((Object)((Docencia)docencia1).infoGrupo, (Object)((Docencia)docencia2).infoGrupo));
	}
    
	/**
	 * Método equals() de la clase Object sobreescrito. Compara si los atributos principales de dos docencias coinciden. 
	 * @param docencia				objeto de la clase Docencia
	 * @return boolean				true si los objetos son iguales y false si son diferentes
	 */
	public boolean equals(Docencia docencia){
		return (idGrupo==docencia.idGrupo && tipoGrupo.equals(docencia.tipoGrupo) && idAsignatura==docencia.idAsignatura);
	}
    
	
    //Getters
	
	/**
	 * Método get() que devuelve el ID de la asignatura.
	 * @return idAsignatura			int que se corresponde con el ID de la asignatura
	 */
    public int getIDasignatura(){
    	return idAsignatura;
    }
    
    /**
     * Método get() que devuelve el ID del grupo.
     * @return idGrupo				int con el ID del grupo
     */
    public int getIDgrupo(){
    	return idGrupo;
    }
    
    /**
     * Método get() que devuelve el horario del grupo, tipo y asignatura especificado en forma de objeto de la clase Grupos.
     * @return infoGrupo			valor de la clase Grupos que contiene información del horario del grupo y tipo de la asignatura
     */
    public Grupos getInfoGrupo(){
    	return infoGrupo;
    }
    
    /**
     * Método get() que devuelve el tipo de grupo.
     * @return tipoGrupo			String que devuelve el tipo de grupo (T o P)
     */
    public String getTipoGrupo(){
    	return tipoGrupo;
    }
   

}