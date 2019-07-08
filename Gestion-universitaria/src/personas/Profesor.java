package personas;

//CLASES IMPORTADAS DE JAVA

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;

//Importar clases de fuera del paquete
import asignaturasYdocencia.Asignatura;
import asignaturasYdocencia.Docencia;


public class Profesor extends Persona implements Comparator<Object> {

	//ATRIBUTOS
	
	private String departamento;
	private String categoriaLaboral;
	private int horasAsignables;
	private String docenciaImpartida;
	private int cargaDocente=0;

	
	//CONSTRUCTORES
	
	/**
	 * 
	 * Constructor vac�o de la clase Profesor.
	 */
	public Profesor(){
		
	}
	
	/**
	 * Constructor de la clase Profesor en el cual se le asignan al objeto los atributos con el valor especificado. Llama al contructor de su clase padre Persona.
	 * @param id							ID del profesor
	 * @param perfil						Pefil de la persona (profesor)
	 * @param nombre						Nombre o nombres del profesor
	 * @param apellidos						Apellido o apellidos del profesor
	 * @param nacimiento					Fecha de nacimiento del profesor
	 * @param departamento					Departamento al que pertenece el profesor
	 * @param categoriaLaboral				Categor�a laboral del profesor (titular o interino)
	 * @param horasAsignables				N�mero de horas asignadas al profesor (m�ximo 20 para titulares y 15 para interinos)
	 * @param docenciaImpartida				N�mero de horas que un profesor imparte docencia
	 */
    public Profesor (int id, String perfil, String nombre, String apellidos,LocalDate nacimiento, String departamento, String categoriaLaboral, int horasAsignables, String docenciaImpartida){
    	super(id, perfil, nombre, apellidos, nacimiento);
    	this.categoriaLaboral=categoriaLaboral;
    	this.departamento=departamento;
    	this.horasAsignables=horasAsignables;
    	this.docenciaImpartida=docenciaImpartida;
    }
    
    
    //METODOS
    
    /**
	 * M�todo toString() de la clase Object sobreescrito. Devuelve un String con los atributos del objeto de la clase Profesor separados por un salto de linea. Llama al m�todo toString() de su clase padre Persona.
	 * @return String					String con los atributos del objeto de la clase Profesor
	 */
    public String toString(){
    	if (horasAsignables==0) return (super.toString()+categoriaLaboral+"\r\n"+departamento+"\r\n"+"\r\n"+docenciaImpartida);
    	else return (super.toString()+categoriaLaboral+"\r\n"+departamento+"\r\n"+horasAsignables+"\r\n"+docenciaImpartida);
    }
    
    //Implemetado de la interfaz Comparator
    
    /**
     * M�todo compare() de la interfaz Comparator implementado.
     * @return int					int que se corresponde -1, 0 � 1 dependiendo si el primer objeto es menor, igual o mayor que el segundo
     */
	public int compare(Object profesor1, Object profesor2) {
		if (((Profesor)profesor1).cargaDocente == ((Profesor)profesor2).cargaDocente)
			return (((Profesor)profesor1).id - ((Profesor)profesor2).id);
		else return (((Profesor)profesor1).cargaDocente - ((Profesor)profesor2).cargaDocente);
	}	
	
	/**
	 * M�todo que comprueba si un profesor supera sus horas asignables (20 horas si es titular y 15 horas si es interino) con la nueva docencia que se le va a asignar.
	 * @param duracion				int con la duraci�n del grupo que se le va asignar al profesor (nuevas horas que se van a asignar al profesor)
	 * @return boolean				true si se pasa del m�ximo permitido seg�n su categor�a laboral y false en caso contrario
	 */
	public boolean superarHorasAsignables(int duracion){
		if ( categoriaLaboral.equalsIgnoreCase("Titular") && ((horasAsignables + duracion) > 20) )
				return true;
		if ( categoriaLaboral.equalsIgnoreCase("Interino") && ((horasAsignables + duracion) > 15) )
				return true;
		return false;
	}
    
    //Getters
	
	/**
	 * M�todo get() que devuelve la categor�a laboral del profesor (titular o nterino).
	 * @return categoriaLaboral		String con la categor�a laboral del profesor (titular o interino)
	 */
    public String getCategoriaLaboral(){
    	return categoriaLaboral;
    }
    
    /**
     * M�todo get() que devuelve un String con la docencia impartida por el profesor.
     * @return docenciaImpartida	String con la docencia impartida por el profesor
     */
    public String getDocenciaImpartida(){
    	return docenciaImpartida;
    }
 
    /**
     * M�todo get() que devuleve las horas asignables del profesor.
     * @return horasAsignables		int con las horas asignadas al profesor
     */
    public int getHorasAsignables(){
    	return horasAsignables;
    }

    /**
     * M�todo get() que llama al m�todo getNombre() de su clase padre Persona y devuleve el nombre del Profesor.
     * @return String				String con el nombre o los nombres del profesor
     */
    public String getNombre(){
    	return super.getNombre();
    }
    
    /**
     * M�todo get() que llama al m�todo getApellidos() de su clase padre Persona y devuleve el/los apellido/apellidos del Profesor.
     * @return String				String con el apellido o los apellidos del profesor
     */
    public String getApellidos(){
    	return super.getApellidos();
    }

    
    //Setters
    
    /**
     * M�todo set() que modifica la docencia impartida por el profesor.
     * @param docenciaImpartida		String con la docencia impartida por el profesor
     */
    public void setDocenciaImpartida(String docenciaImpartida){
    	this.docenciaImpartida=docenciaImpartida;
    }
    
    /**
     * M�todo set() que a�ade a las horas asignables del profesor las nuevas horas que se le han asignado al a�adirle una nueva carga docente.
     * @param anhadirHoras			int con las horas que se van a a�adir a las que ya tiene asignadas el profesor
     */
    public void setHorasAsignables(int anhadirHoras){
    	horasAsignables+=anhadirHoras;
    }
    
    /**
     * M�todo set() que cuenta las horas que el profesor da clase y las almacena en cargaDocente.
     * @param lhmAsignaturas		map con las asignaturas e informaci�n sobre las mismas que hay en el fichero "asignaturas.txt"
     */
    public void setCargaDocente(Map<Integer, Asignatura> lhmAsignaturas){
		if (!(docenciaImpartida.isEmpty())){
			String[]cadenaDocenciaImpartida=docenciaImpartida.split(";");
			for (int i=0; i<cadenaDocenciaImpartida.length; i++){
				String conjunto=cadenaDocenciaImpartida[i].trim();
				String[] dividir=conjunto.split("\\s+");
				Docencia docenciaImpartidaProfesor = new Docencia (Integer.parseInt(dividir[0]), dividir[1], Integer.parseInt(dividir[2]));
				if (docenciaImpartidaProfesor.getTipoGrupo().equals("T")) 
					cargaDocente+=((Asignatura) lhmAsignaturas.get(docenciaImpartidaProfesor.getIDasignatura())).getDuracionTeoria();
				else cargaDocente+=((Asignatura) lhmAsignaturas.get(docenciaImpartidaProfesor.getIDasignatura())).getDuracionPractica();
			}
		}	
    }
    
}
