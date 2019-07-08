package personas;


//CLASES IMPORTADAS DE JAVA

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Persona{
	
	//ATRIBUTOS
	
	protected int id;
	private String perfil;
	private String nombre;
    private String apellidos;
    private LocalDate nacimiento;
	public DateTimeFormatter formatoFechas = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
    
    //CONSTRUCTORES
	
	/**
	 * 
	 * Constructor vacío de la clase Persona.
	 */
    Persona(){
    	
    }
    
    /**
     * Constructor en el cual se le asignan al objeto los atributos de la clase Persona.
     * @param id							ID de la persona
     * @param perfil						Pefil de la persona (alumno o profesor)
	 * @param nombre						Nombre o nombres de la persona
	 * @param apellidos						Apellido o apellidos de la persona
	 * @param nacimiento					Fecha de nacimiento de la persona
     */
    Persona (int id, String perfil, String nombre, String apellidos, LocalDate nacimiento){
    	this.id=id;
    	this.perfil=perfil;
    	this.nombre=nombre;
    	this.apellidos=apellidos;
    	this.nacimiento=nacimiento;
    }
    
    
    //METODOS
    
    /**
     * Método toString() de la clase Object sobreescrito. Devuelve un String con los atributos del objeto de la clase Persona separados por un salto de linea. Le da el formato dd/mm/aaaa a la fecha de nacimiento.
     * @return String					String con los atributos del objeto de la clase Persona
     */
    public String toString(){
    	return (id+"\r\n"+perfil+"\r\n"+nombre+"\r\n"+apellidos+"\r\n"+nacimiento.format(formatoFechas)+"\r\n");
    }

    //Gettres
    
    /**
     * Método get() que devuelve el perfil de la persona (alumno o profesor).
     * @return perfil					String con el perfil de la persona
     */
    public String getPerfil(){
    	return perfil;
    }

    /**
     * Método get() que devuelve el nombre de la persona.
     * @return nombre					String con el nombre o los nombres de la persona
     */
    public String getNombre(){
    	return nombre;
    }
    
    /**
     * Método get() que devuelve el/los apellido/apellidos de la persona.
     * @return apellidos				String con el apellido o los apellidos de la persona
     */
    public String getApellidos(){
    	return apellidos;
    }

    
}
