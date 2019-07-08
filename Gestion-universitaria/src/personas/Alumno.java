package personas;

//CLASES IMPORTADAS DE JAVA

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;


public class Alumno extends Persona {

	//ATRIBUTOS
	
	private LocalDate fechaIngreso;
	private double notaMediaExpediente;
	private String asignaturasSuperadas;
	private String docenciaRecibida;
	public DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
	
	
	//CONSTRUCTORES	
	
	/**
	 * Único constructor de la clase Alumno en el cual se le asignan al objeto los atributos con el valor especificado. Llama al contructor de su clase padre Persona.
	 * @param id							ID del alumno
	 * @param perfil						Pefil de la persona (alumno)
	 * @param nombre						Nombre o nombres del alumno
	 * @param apellidos						Apellido o apellidos del alumno
	 * @param nacimiento					Fecha de nacimiento del alumno
	 * @param fechaIngreso					Fecha de ingreso del alumno
	 * @param asignaturasSuperadas			Asignaturas superadas por el alumno
	 * @param notaMediaExpediente			Nota media del expediente del alumno
	 * @param docenciaRecibida				Docencia recibida por el alumno
	 */
	public Alumno (int id, String perfil, String nombre, String apellidos,LocalDate nacimiento,LocalDate fechaIngreso, String asignaturasSuperadas, double notaMediaExpediente, String docenciaRecibida){
		super(id, perfil, nombre, apellidos, nacimiento);
		this.fechaIngreso=fechaIngreso;
		this.asignaturasSuperadas=asignaturasSuperadas;
		this.docenciaRecibida=docenciaRecibida;
		this.notaMediaExpediente=notaMediaExpediente;
	}
	
	
	//METODOS
	
	/**
	 * Método toString() de la clase Object sobreescrito. Devuelve un String con los atributos del objeto de la clase Alumno separados por un salto de linea. La nota media que devuelve está escrita con dos decimales y la fecha de ingreso con el formato dd/mm/aaaa. Llama al método toString() de su clase padre Persona.
	 * @return String					String con los atributos del objeto de la clase Alumno
	 */
    public String toString(){
    	//Definir el formato de decimales para la nota media
    	simbolo.setDecimalSeparator('.');
    	DecimalFormat formatoDecimales = new DecimalFormat("#0.00",simbolo);	
    	if (notaMediaExpediente==0) return (super.toString()+fechaIngreso.format(formatoFechas)+"\r\n"
    										+asignaturasSuperadas+"\r\n"+"\r\n"+docenciaRecibida);
    	else return (super.toString()+fechaIngreso.format(formatoFechas)+"\r\n"+asignaturasSuperadas+"\r\n"
    				 +formatoDecimales.format (notaMediaExpediente)+"\r\n"+docenciaRecibida);
    }
	
    //Getters
    
    /**
     * Método get() que devuelve un String con la docencia recibida por el alumno.
     * @return docenciaRecibida			String con la docencia recibida por el alumno
     */
    public String getDocenciaRecibida(){
    	return docenciaRecibida;
    }

    /**
     * Método get() que devuelve un String con las asignatras superadas por el alumno.
     * @return asignaturasSuperadas		String con las asignaturas superadas por el alumno
     */
    public String getAsignaturasSuperadas(){
    	return asignaturasSuperadas;
    }
    
    //Setters
 
    //Metodo setDocenciaRecibida sobrecargado
    
    /**
     * Método set() sobrecargado que añade la docencia introducida a la docencia que el alumno tenía (sea nula o no).
     * @param anhadirDocendia			String con la docencia que se va a añadir a la que recbe el alumno
     */
    public void setDocenciaRecibida(String anhadirDocendia){
    	if (docenciaRecibida.isEmpty()) docenciaRecibida=anhadirDocendia;
    	else docenciaRecibida=docenciaRecibida.concat("; ").concat(anhadirDocendia);
    }
    
    /**
     * Método set() sobrecargado que elimina la docencia introducida a la docencia que el alumno tenía (no puede ser nula).
     * @param docenciaEliminar			String con la docencia a eliminar de la docencia recibida por el alumno
     * @param valorSustituir			String con el valor con el cual se va a sustituir la docencia que se va a eliminar
     */
    public void setDocenciaRecibida(String docenciaEliminar, String valorSustituir){ 
    	String cadena=("; "+docenciaRecibida+";").replaceAll(String.valueOf(docenciaEliminar)+";", valorSustituir).replace("  ", " ").trim();
    	if (cadena.endsWith(";")) cadena = cadena.substring(0, cadena.length()-1).trim();
    	if (cadena.startsWith("; ")) cadena = cadena.substring(1, cadena.length()).trim();
    	docenciaRecibida=cadena;
    }

    /**
     * Método set() que modifica las asignaturas superadas añadiendo la nueva asignatura introducida.
     * @param nuevaAsignatura			int con el ID de la nueva asignatura a añadir a las asignaturas superadas
     */
    public void setAsignaturasSuperadas(int nuevaAsignatura){
    	if (asignaturasSuperadas.isEmpty()) asignaturasSuperadas=(String.valueOf(nuevaAsignatura));
		else asignaturasSuperadas=asignaturasSuperadas.concat(", ").concat(String.valueOf(nuevaAsignatura));
    }
    
    /**
     * Método set() que modifica la nota media del expediente del alumno incluyendo la nueva nota.
     * @param notaAsignatura			double con la nueva nota a promediar con la nota media del expediente				
     */
    public void setNotaMediaExpediente(double notaAsignatura){
    	int numeroSuperadas=0;
    	if (!(asignaturasSuperadas.isEmpty())) numeroSuperadas=asignaturasSuperadas.split(",").length;
    	notaMediaExpediente=(numeroSuperadas*notaMediaExpediente+notaAsignatura)/(numeroSuperadas+1);
    }
    
}
