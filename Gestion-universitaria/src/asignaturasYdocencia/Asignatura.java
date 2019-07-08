package asignaturasYdocencia;


public class Asignatura {

	//ATRIBUTOS
	
	private int id;
    private String nombre;
    private String siglas;
    private String coordinador;
    private String prerequisitos;
    private String duracionTeoria;
    private String duracionPractica;
    private String gruposTeoria;
    private String gruposPractica;
	
    
    //CONSTRUCTORES  
    
    /**
     * Único constructor de la clase Asignatura en el cual se le asignan al objeto los atributos con el valor especificado.
     * @param id					ID de la asignatura
     * @param nombre				Nombre de la asignatura
     * @param siglas				Siglas de la asignatura
     * @param coordinador			ID del coordinador de la asignatura
     * @param prerequisitos			Prerequisitos necesarios para matricularse en la asignatura
     * @param duracionTeoria		Duración de las clases teóricas de la asignatura
     * @param duracionPractica		Duración de las clases prácticas de la asignatura
     * @param gruposTeoria			Horario de los grupos de teoría de la asignatura
     * @param gruposPractica		Horario de los grupos de práctica de la asignatura
     */
    public Asignatura(int id, String nombre, String siglas, String coordinador, String prerequisitos, String duracionTeoria, String duracionPractica, String gruposTeoria, String gruposPractica){
    	this.id=id;
    	this.nombre=nombre;
    	this.siglas=siglas;
    	this.coordinador=coordinador;
    	this.prerequisitos=prerequisitos;
    	this.duracionTeoria=duracionTeoria;
    	this.duracionPractica=duracionPractica;
    	this.gruposTeoria=gruposTeoria;
    	this.gruposPractica=gruposPractica;
    }
    
    
    //METODOS
    
    /**
     * Método toString() de la clase Object sobreescrito.
     * @return String				String con los atributos del objeto de la clase Asignatura
     */
    public String toString(){
    	return (id+"\r\n"+nombre+"\r\n"+siglas+"\r\n"+coordinador+"\r\n"+prerequisitos+"\r\n"
    			+duracionTeoria+"\r\n"+duracionPractica+"\r\n"+gruposTeoria+"\r\n"+gruposPractica);
    }
    
    //Getters
    
    /**
     * Método get() que devuelve el ID del coordinador de una asignatura (en forma de String).
     * @return coordinador			String con el identificador del coordinador de la asignatura
     */
    public String getCoordinador(){
    	return coordinador;
    }
    
    /**
     * Método get() que devuelve los grupos teóricos y su horario de una asignatura (en forma de String).
     * @return gruposTeoria			String con el horario de sus grupos teóricos	
     */
    public String getGruposTeoria(){
    	return gruposTeoria;
    }

    /**
     * Método get() que devuelve los grupos prácticos y su horario de una asignatura (en forma de String).
     * @return gruposPractica			String con el horario de sus grupos prácticos
     */
    public String getGruposPractica(){
    	return gruposPractica;
    }
    
    /**
     * Método get() que devuelve la duración de los grupos teóricos de una asignatura. Si el campo está vacío, devolverá 0 (caso poco probable).
     * @return duracionTeoria			int con la duración de las clases teóricas de la asignatura
     */
    public int getDuracionTeoria(){
    	if (duracionTeoria.equals("")) return 0;
    	else return (Integer.parseInt(duracionTeoria));
    }

    /**
     * Método get() que devuelve la duración de los grupos prácticos de una asignatura. Si el campo está vacío, devolverá 0.
     * @return duracionPractica			int con la duración de las clases prácticas de la asignatura
     */
    public int getDuracionPractica(){
    	if (duracionPractica.equals("")) return 0;
    	else return (Integer.parseInt(duracionPractica));
    }
    
    /**
     * Método get() que devuelve los prerequisitos necesarios para matricularse en una asignatura.
     * @return prerequisitos			String con los prerequisitos necesarios para poder matricularse en la asignatura
     */
    public String getPrerequisitos(){
    	return prerequisitos;
    }
    
    /**
     * Método get() que devuelve el nombre de la asignatura.
     * @return nombre					String con el nombre de la asignatura
     */
    public String getNombre(){
    	return nombre;
    }
    
    //Setters
    
    /**
     * Método set() que modifica el coordinador de una asignaura por el que esté indicado por el parámetro de entrada.
     * @param coordinador				String con el ID del nuevo coordinador
     */
    public void setCoordinador(String coordinador){
    	this.coordinador=coordinador;
    }

}
