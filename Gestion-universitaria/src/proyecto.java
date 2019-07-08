//CLASES IMPORTADAS DE JAVA
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
//Clases para lectura/escritura de ficheros
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//Clases para controlar excepciones
import java.io.IOException;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.lang.NumberFormatException;
import java.lang.IndexOutOfBoundsException;
//Clases creadas en el proyecto
import asignaturasYdocencia.Asignatura;
import asignaturasYdocencia.Docencia;
import asignaturasYdocencia.Grupos;
import personas.Alumno;
import personas.Persona;
import personas.Profesor;
import interfaces.Comparator;


/**
 * 
 * @author Andrea Cabezas López
 * @author Sara Berezo Loza
 *
 */


public class proyecto {

	//Variable para asignar el identificador de cada persona
	static Integer idPersonas=1;
	
	//Mapas usados en todo el main
	static Map<Integer,Persona>lhmPersonas=new LinkedHashMap<Integer, Persona>();
	static Map<Integer,Asignatura>lhmAsignaturas=new LinkedHashMap<Integer, Asignatura>(); 

	/**
	 * Método main desde el cual se comprueba la existencia y se hace llamada a las funciones que leen, modifican y escriben los ficheros "asignaturas.txt" y "personas.txt". En él se genera error si el fichero "ejecucion.txt" no existe (mediante el aviso "Fichero de ejecucion no existente") y en caso contrario se lee y se llama a la función oportuna. Asimismo, se comprueba si los comando están completos y contienen toda la nformación necesaria para ejecutar las instrucciones introducidas por el fichero "ejecuciones.txt". Además, también generará error en el caso de que se inserten caracteres en donde debiera haber un dato numérico. Esto generará el aviso "Comando incorrecto: nombre del comando".
	 * @param args						Entrada de parámetros inutilizada
	 * @throws IOException				Main puede larzar una excepción de Entrada/Salida
	 */
	
	public static void main(String[] args) throws IOException {
			
		List<String> ListaAvisos = new ArrayList<String>();
		try{
			//Leer el ficheo avisos y alamcenarlo en la lista
			Scanner leerAvisos = new Scanner (new FileInputStream("avisos.txt"));
			while (leerAvisos.hasNextLine()) ListaAvisos.add(leerAvisos.nextLine());
			leerAvisos.close();
		}catch(FileNotFoundException exception){ }
		
		//Escribir fichero de avisos lo que había (puede estar vacio)
		PrintWriter escribirAvisos = new PrintWriter(new FileOutputStream("avisos.txt"));
		Iterator<String> iterator = ListaAvisos.iterator();
		while (iterator.hasNext()) escribirAvisos.println(iterator.next());
		
		//Comprobar si existen los ficheros "ejecuciones.txt", "personas.txt" y "asignaturas.txt" y leerlos si existen
		Scanner ejecuciones = null; Scanner leerPersonas = null; Scanner leerAsignaturas = null;
		//Si no existe el fichero "ejecucion.txt" se escribira un aviso y se saldra del programa
		try{ 
			ejecuciones = new Scanner (new FileInputStream("ejecucion.txt"));
		}catch (FileNotFoundException exception){
			escribirAvisos.println("Fichero de ejecucion no existente"); 
		    escribirAvisos.close(); System.exit(-1);
		}
		//Si no existen los ficheros "personas.txt" o "asignaturas.txt" o ambos se saldra del programa
		try{
			leerPersonas = new Scanner (new FileInputStream("personas.txt"));
			leerAsignaturas= new Scanner (new FileInputStream("asignaturas.txt"));
		}catch (FileNotFoundException exception){
			System.exit(-1);
		}

		//Leer los ficheros "asignaturas.txt" y "personas.txt" y volcarlos en los mapas que corresponda
		leerFichero(leerPersonas);
		leerFichero(leerAsignaturas);
	  
		//Leer el fichero ejecuciones
		String linea=null;
		while (ejecuciones.hasNextLine()){
			linea=ejecuciones.nextLine();
			while(linea.isEmpty()) linea = ejecuciones.nextLine();
			String[]data=linea.split("\\s+");
			String orden=data[0];
          
			try{
				switch(orden.toUpperCase()){
					case "INSERTAPERSONA":
						InsertaPersona(linea, escribirAvisos);
						break;
					case "ASIGNACOORDINADOR":
						AsignaCoordinador(linea, escribirAvisos);
						break;
					case "ASIGNACARGADOCENTE":
						AsignaCargaDocente(linea, escribirAvisos);
						break;
					case "MATRICULA":
						MatriculaAlumno(linea, escribirAvisos);
						break;
					case "ASIGNAGRUPO":
						AsignaGrupo(linea, escribirAvisos);
						break;
					case "EVALUAR":
						EvaluarAsignatura(linea, escribirAvisos);
						break;
					case "OBTENERCALENDARIOCLASES":
						ObtenerCalendarioClases(linea, escribirAvisos);
						break;
					case "ORDENARPROFESORESPORCARGADOCENTE":
						OrdenarProfesoresPorCargaDocente(linea, escribirAvisos);
						break;
					default: 
						if (!(orden.startsWith("*"))) escribirAvisos.println("Comando incorrecto: "+orden);
				}
			//Comprobar que no hay errores en los datos introducidos 
			//Se comprueba que esten todos los argumentos necesarios para la ejecucion de la instruccion
			}catch(IndexOutOfBoundsException exception){
				escribirAvisos.println("Comando incorrecto: "+orden);
			}
			//Se comprueba que los datos numericos que haya que introducir sean numeros y no caracteres
			catch(NumberFormatException exception){
				escribirAvisos.println("Comando incorrecto: "+orden);
			}
		}
  	  
		//Escribir el los ficheros
		PrintWriter escribirPersonas = new PrintWriter(new FileOutputStream("personas.txt"));
		PrintWriter escribirAsignaturas = new PrintWriter(new FileOutputStream("asignaturas.txt"));
		escribirFichero(lhmPersonas, escribirPersonas);
		escribirFichero(lhmAsignaturas, escribirAsignaturas);
	      
		//Cerrar los ficheros
		ejecuciones.close();     
		escribirAvisos.close();
	      
		//Si el fichero avisos está vacío se elimina
		if (new File("avisos.txt").exists() && new File("avisos.txt").length()==0) 
			(new File("avisos.txt")).delete();
		
	
}



	
	
/**
 * Se recogen los datos de la persona (profesor o alumno) y se procesan e incluyen al fichero "personas.txt" si no hay algún error en los datos.
 * En caso de error se notifica mediante el fichero "avisos.txt" con la abreviatura IP y no se realizará la operación.
 * En caso de que el perfil no sea "alumno" o "profesor" o en el caso de que la categoría del profesor no sea "titular" o "interino" no se generará ningún error ni se guardarán el resto de datos correspondientes a la instrucción.
 * Los posibles errores que se pueden dar en esta función son: "Fecha incorrecta", "Fecha de ingreso incorrecta" (sólo alumnos), "Nota media incorrecta" (sólo alumnos) y "Numero de horas incorrecto" (sólo profesores).
 * @param entrada			String con los datos de entrada del fichero "ejecuciones.txt"
 * @param avisos			Objeto para imprimir en el fichero "avisos.txt"
 * Función que se corresponde con el comardo "InsertaPersona" del fichero "ejecuciones.txt".
 */
	
//FUNCIÓN INSERTAR PERSONA
public static void InsertaPersona (String entrada,  PrintWriter avisos) {
	
	boolean anular = false;
	int indice = 0;
	
	//COMPROBAR SI EL ID QUE VAMOS A ASIGNAR YA ESTÁ ASIGNADO
	while(lhmPersonas.get(idPersonas)!=null) idPersonas++;
	
	//ESTABLECER DATOS COMUNES A PROFESORES Y ALUMNOS
  String[]data=entrada.split("\\s+");
  String perfil=data[++indice];
	String nombre=data[++indice];
	//Comprobar si tiene mas de un nombre
	if (nombre.startsWith("\"") && !(nombre.endsWith("\""))){
		for(++indice; ;indice++){
			nombre+=" "+data[indice];
			if (data[indice].endsWith("\"")) break;
		}
	}
	nombre=nombre.replace("\"", "");
	 
	String apellidos=data[++indice];
	//Comprobar si tiene mas de un apellido
	if (apellidos.startsWith("\"") && !(apellidos.endsWith("\""))){
		for(++indice; ;indice++){
			apellidos+=" "+data[indice];
			if (data[indice].endsWith("\"")) break;
		}
	}
	apellidos=apellidos.replace("\"", "");
	
	String[]fecha1=data[++indice].split("/");
		int dia1 = Integer.parseInt(fecha1[0]);
		int mes1 = Integer.parseInt(fecha1[1]);
		int anho1 = Integer.parseInt(fecha1[2]);

	
	//SI ES PROFESOR
	if (perfil.equalsIgnoreCase("Profesor")){
		String categoria=data[++indice].replace("\"", "");
		String departamento=data[++indice];
		//comprobar si el nombre del departamento consta de mas de una palabra
		if (departamento.startsWith("\"") && !(departamento.endsWith("\""))){
			for(++indice; ;indice++){
				departamento+=" "+data[indice];
				if (data[indice].endsWith("\"")) break;
			}
		}
		departamento=departamento.replace("\"", "");
	
		int horasasignables = Integer.parseInt(data[++indice]);
		String docenciaImpartida = "";
			try{
				LocalDate nacimiento = LocalDate.of(anho1,mes1,dia1);
				
				//Comprobar si la fecha de nacmiento es correcta
				if (nacimiento.getYear()<1950 || 
					((nacimiento.getYear()>2020) && (nacimiento.getMonthValue()>1) && (nacimiento.getDayOfMonth()>1))){
					avisos.println("IP -- Fecha incorrecta"); anular=true;
				}
			
				//Comprobar que la categoria es titular o interino
				if (!(categoria.equalsIgnoreCase("Interino")) && !(categoria.equalsIgnoreCase("Titular"))) anular=true;
		
				//Comprobar si las horas asignables son correctas
				if ( (horasasignables<0)  ||  (categoria.equalsIgnoreCase("Titular") && horasasignables>20) ||
					(categoria.equalsIgnoreCase("Interino") && horasasignables>15) ){
					avisos.println("IP -- Numero de horas incorrecto"); anular=true;
				}

				//AÑADIR EL NUEVO PROFESOR AL MAPA lhmPersonas SI NO HA HABIDO NINGUN ERROR
				if (!anular){
					Persona profesor = new Profesor(idPersonas, perfil,nombre,apellidos,nacimiento,departamento,categoria,horasasignables,docenciaImpartida);
					lhmPersonas.put(idPersonas, profesor);
				}
				//Comprobar si las fechas son correctas
			}catch(DateTimeException exception){
				avisos.println("IP -- Fecha incorrecta");
				//Comprobar si las horas asignables son correctas
				if ( (horasasignables<0)  ||  (categoria.equalsIgnoreCase("Titular") && horasasignables>20) ||
					(categoria.equalsIgnoreCase("Interino") && horasasignables>15) )
					avisos.println("IP -- Numero de horas incorrecto");
			}
		}
	
		//SI ES ALUMNO
		if (perfil.equalsIgnoreCase("Alumno")){  
			String[]fecha2=data[++indice].split("/");
				int dia2 = Integer.parseInt(fecha2[0]);
				int mes2 = Integer.parseInt(fecha2[1]);
				int anho2 = Integer.parseInt(fecha2[2]);
				LocalDate fechaIngreso = LocalDate.of(anho2, mes2, dia2);
			String asignaturasSuperadas = "";
			double notaMedia =Double.parseDouble(data[++indice]);
			String docenciaRecibida = "";
		
			try{
				LocalDate nacimiento = LocalDate.of(anho1,mes1,dia1);
				
				//Comprobar si la fecha de nacmiento es correcta
				if (nacimiento.getYear()<1950 || 
					((nacimiento.getYear()>2020) && (nacimiento.getMonthValue()>1) && (nacimiento.getDayOfMonth()>1))){
					avisos.println("IP -- Fecha incorrecta"); anular=true;
				}
			
				//Comprobar si la fecha de ingreso es correcta
				if (fechaIngreso.getYear()<1950 || 
					((fechaIngreso.getYear()>2020) && (fechaIngreso.getMonthValue()>1) && (fechaIngreso.getDayOfMonth()>1))){
					avisos.println("IP -- Fecha incorrecta"); anular=true;
				}
		
				//Comprobar si las fechas son incorrectas entre sí
				if ( ((Period.between(nacimiento, fechaIngreso)).getYears())<15 ||
					((Period.between(nacimiento, fechaIngreso)).getYears())>65){
					avisos.println("IP -- Fecha de ingreso incorrecta"); anular=true;
				}
		
				//Comprobar si la nota media es correcta
				if (notaMedia<0.00 || notaMedia>10.00){
					avisos.println("IP -- Nota media incorrecta"); anular=true;
				}
		
				//AÑADIR EL NUEVO ALUMNO AL MAPA lhmPersonas SI NO HA HABIDO NINGUN ERROR
				if (!anular){
					Persona alumno = new Alumno(idPersonas, perfil,nombre,apellidos,nacimiento,fechaIngreso,asignaturasSuperadas,notaMedia,docenciaRecibida);     
					lhmPersonas.put(idPersonas, alumno);
				}
				//Comprobar si las fechas son correctas
			}catch(DateTimeException exception){
				avisos.println("IP -- Fecha incorrecta");	
				if (notaMedia<0.00 || notaMedia>10.00) avisos.println("IP -- Nota media incorrecta");
		}
	}
		
}





/**
 * Función que se corresponde con el comardo "AsignaCoordinador" del fichero "ejecuciones.txt".
 * En caso de que no se produzcan errores, asigna un coordinador (mediande el ID del profesor) a una asignatura determinada.
 * En caso de error se notifica mediante el fichero "avisos.txt" con la abreviatura ACOORD y no se realizará la operación.
 * Si se da el caso de que esa asignatura ya tiene un coordinador, éste será sustituido por el nuevo.
 * Los posibles errores que se pueden dar en esta función son: "Profesor inexistente", "Profesor no titular", "Asignatura inexistente" y "Profesor ya es coordinador de dos materias".

 * @param entrada			String con los datos de entrada del fichero "ejecuciones.txt"
 * @param avisos			Objeto para imprimir en el fichero "avisos.txt"
 */

//FUNCION ASIGNAR COORDINADOR
public static void AsignaCoordinador (String entrada,  PrintWriter avisos){
	
	boolean anular = false;
	int asignaturasCoordinadas=0;
	//Leer los datos introducidos por el fichero "ejecuciones.txt"
	String[]data=entrada.split("\\s+");
	String IDprofesor = data[1];
	int IDasignatura = Integer.parseInt(data[2]);
		
	Persona persona = lhmPersonas.get(Integer.parseInt(IDprofesor));
	Asignatura asignatura = lhmAsignaturas.get(IDasignatura);
	
	if (!(persona instanceof Profesor)) {
		avisos.println("ACOORD -- Profesor inexistente"); anular=true;
	}else{
		if(((Profesor)persona).getCategoriaLaboral().equalsIgnoreCase("Interino")){
		avisos.println("ACOORD -- Profesor no titular"); anular=true;
		}
	}
	if (asignatura == null){
		avisos.println("ACOORD -- Asignatura inexistente"); anular=true;
	}
	
	if (anular) return;
	else {
		//Comprobar si el profesor ya está coordinando dos asignaturas
		Iterator<Asignatura> iterator = lhmAsignaturas.values().iterator();
		while (iterator.hasNext()){
		  Asignatura asignaturaIterator = iterator.next();
		  if (asignaturaIterator.getCoordinador().equals(IDprofesor)) asignaturasCoordinadas++;
		}
			if (asignaturasCoordinadas>=2) {
			avisos.println("ACOORD -- Profesor ya es coordinador de 2 materias"); anular=true;
		}
	}
	
	//Si no hay errores se le asigna la coordinacion de la materia
	if (!anular) ((lhmAsignaturas.get(IDasignatura))).setCoordinador(IDprofesor);
	
}





/**
 * Función que se corresponde con el comardo "AsignaCargaDocente" del fichero "ejecuciones.txt".
 * Se le asignará al profesor indicado el grupo (teórico o práctico) indicado de la asignatura indicada (mediante sus IDs). Además se modificarán sus horas asignables añadiendo la duración de la nueva docencia impartida.
 * En caso de error se notifica mediante el fichero "avisos.txt" con la abreviatura ACD y no se realizará la operación.
 * Los posibles errores que se pueden dar en esta función son: "Profesor inexistente", "Asignatura inexistente", "Tipo de grupo incorrecto", "Grupo inexistente", "Grupo ya asignado", "Horas asignables superior al maximo" y "Se genera solape".
 * @param entrada			String con los datos de entrada del fichero "ejecuciones.txt"
 * @param avisos			Objeto para imprimir en el fichero "avisos.txt"
 */

//FUNCION ASIGNAR CARGA DOCENTE
public static void AsignaCargaDocente(String entrada, PrintWriter avisos){
	
	boolean anular = false; boolean anularHoras = false;
	//Leer los datos introducidos por el fichero "ejecuciones.txt"
	String[]data=entrada.split("\\s+");
	int IDprofesor=Integer.parseInt(data[1]);
	int IDasignatura=Integer.parseInt(data[2]);
	String tipoGrupo=data[3];
	int IDgrupo=Integer.parseInt(data[4]);
	
	Persona persona = lhmPersonas.get(IDprofesor);
	Asignatura asignatura = lhmAsignaturas.get(IDasignatura);
	
	if (!(persona instanceof Profesor)) { 
		avisos.println("ACD -- Profesor inexistente"); anular=true;
	}
	if (asignatura == null){ 
		avisos.println("ACD -- Asignatura inexistente"); anular=true;
	}
	if (!(tipoGrupo.equals("T")) && (!(tipoGrupo.equals("P")))){ 
		avisos.println("ACD -- Tipo de grupo incorrecto"); anular=true;
	}
	
	if (anular) return;
	else{
	    Docencia grupoAsignar = new Docencia (IDasignatura, tipoGrupo, IDgrupo, asignatura);

		//Comprobar si existe el grupo en esa asignatura
		if (grupoAsignar.getInfoGrupo()==null)	{
			avisos.println("ACD -- Grupo inexistente"); anular=true;
		}
				
	    if (!anular){
	    	//Comprobar si el grupo ya esta asignado a un profesor
	    	Iterator<Persona> iterator = lhmPersonas.values().iterator();
	    	while (iterator.hasNext()){
	    		Persona personaIterator = iterator.next();
	    		if (personaIterator instanceof Profesor){
	    			if (!((((Profesor)personaIterator).getDocenciaImpartida()).isEmpty())){
	    				String[]cadenaDocenciaImpartida=(((Profesor)personaIterator).getDocenciaImpartida()).split(";");
	    				for (int i=0; i<cadenaDocenciaImpartida.length; i++){
	    					String conjunto=cadenaDocenciaImpartida[i].trim();
	    					String[] dividir=conjunto.split("\\s+");
	    					Docencia docenciaImpartidaProfesor = new Docencia (Integer.parseInt(dividir[0]), dividir[1], Integer.parseInt(dividir[2]));
	    					if (docenciaImpartidaProfesor.equals(grupoAsignar)){
	    						avisos.println("ACD -- Grupo ya asignado"); anular=true;
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
		
		//Comprobar si el profesor va a superar sus horas asignables maximas
		int duracion;
		if (tipoGrupo.equals("T")) duracion=asignatura.getDuracionTeoria();    	    	
		else duracion=asignatura.getDuracionPractica();
		if (duracion!=0){
			if ( ((Profesor)persona).superarHorasAsignables(duracion) ){
				avisos.println("ACD -- Horas asignables superior al maximo"); anularHoras=true;
			}
		}
		
		if (!anular){
			//Comprobar si se genera solape con las asignaciones previas del profesor
			if (!((((Profesor)persona).getDocenciaImpartida()).isEmpty())){
				String[]cadenaDocencia=(((Profesor)persona).getDocenciaImpartida()).split(";");
				for (int i=0; i<cadenaDocencia.length; i++){
					String conjunto=cadenaDocencia[i].trim();
					String[] dividir=conjunto.split("\\s+");
					int IDasignaturasProfesor = Integer.parseInt(dividir[0]);
					String tipoGrupoProfesor = dividir[1];
					Asignatura asignaturaDocenciaProfesor = lhmAsignaturas.get(IDasignaturasProfesor);
					Docencia docenciaImpartida = new Docencia (IDasignaturasProfesor, tipoGrupoProfesor, Integer.parseInt(dividir[2]), asignaturaDocenciaProfesor);
					if (docenciaImpartida.getInfoGrupo().getDia().equals(grupoAsignar.getInfoGrupo().getDia())){
						int duracionDocencia;
						if (tipoGrupoProfesor.equals("T")) duracionDocencia=asignaturaDocenciaProfesor.getDuracionTeoria();    	    	
						else duracionDocencia=asignaturaDocenciaProfesor.getDuracionPractica();
						if (docenciaImpartida.getInfoGrupo().generaSolape(duracionDocencia, grupoAsignar.getInfoGrupo(), duracion)){
							avisos.println("ACD -- Se genera solape"); anular=true;	
						}
					}
				}
			}
		}
		//Si no hay errores se le asigna al profesor la carga docente 
	    if (!anular && !anularHoras){
	    	if (((Profesor)persona).getDocenciaImpartida().isEmpty()) ((Profesor)persona).setDocenciaImpartida(grupoAsignar.toString());
	    	else ((Profesor)persona).setDocenciaImpartida((((Profesor)persona).getDocenciaImpartida().concat("; ").concat(grupoAsignar.toString())));
	    	((Profesor)persona).setHorasAsignables(duracion);
	    }
	}
	
}





/**
 * Función que se corresponde con el comardo "Matricula" del fichero "ejecuciones.txt".
 * Matriculará al alumno indicado en la asignatura indicada (mediantes sus IDs).
 * En caso de error se notifica mediante el fichero "avisos.txt" con la abreviatura MAT y no se realizará la operación.
 * En el caso de que un alumno se intente matricular en una asignatura que ya ha aprobado y superado no se generará ningún error pero no se podrá matricular en ella (no se realizará la operación).
 * Los posibles errores que se pueden dar en esta función son: "Alumno inexistente", "Asignatura inexistente", "Ya es alumno de la asignatura indicada" y "No cumple requisitos".
 * @param entrada			String con los datos de entrada del fichero "ejecuciones.txt"
 * @param avisos			Objeto para imprimir en el fichero "avisos.txt"
 */

//FUNCION MATRICULAR ALUMNO
public static void MatriculaAlumno (String entrada, PrintWriter avisos){
	
	boolean anular = false;
	//Leer los datos introducidos por el fichero "ejecuciones.txt"
	String[]data=entrada.split("\\s+");
	int IDalumno = Integer.parseInt(data[1]);
	int IDasignatura = Integer.parseInt(data[2]);
		
	Persona persona = lhmPersonas.get(IDalumno);
	Asignatura asignatura = lhmAsignaturas.get(IDasignatura);
		
	if (!(persona instanceof Alumno)) {
		avisos.println("MAT -- Alumno inexistente"); anular=true;
	}
	if (asignatura == null){
		avisos.println("MAT -- Asignatura inexistente"); anular=true;
	}
		
	if (anular) return;
	else {
		//Comprobar si el alumno ya esta matriculado en esa asignatura
		String[]docenciaAlumno=((Alumno)persona).getDocenciaRecibida().split(";");
		for (int i=0; i<docenciaAlumno.length; i++){
			String conjunto=docenciaAlumno[i].trim();
			String[] dividir=conjunto.split("\\s+");
			int IDasignaturaDocencia=Integer.parseInt(dividir[0]);
			if (IDasignaturaDocencia==IDasignatura){
				avisos.println("MAT -- Ya es alumno de la asignatura indicada"); anular=true;
			}
		}	
		//Comprobar si el alumno cumple los requisitos
		if (!(asignatura.getPrerequisitos().isEmpty())){
			if (((Alumno)persona).getAsignaturasSuperadas().isEmpty()){
				avisos.println("MAT -- No cumple requisitos"); anular=true;
			}else{
				String[]superadasAlumno=((Alumno)persona).getAsignaturasSuperadas().split(",");
				String[]requisitos=asignatura.getPrerequisitos().split(",");
				for (int i=0; i<requisitos.length; i++){
					anular=true;
					int prerequisito=Integer.parseInt(requisitos[i].trim());
					for (int j=0; j<superadasAlumno.length; j++){
						int superada=Integer.parseInt(superadasAlumno[j].trim());
						if (superada==prerequisito) anular=false;
					}
					if (anular==true){
					avisos.println("MAT -- No cumple requisitos"); break;
					}
				}
			}
		}	
		//Comprobar si el alumno ya ha aprobado la asignatura, en cuyo caso no se le volverá a matricular
		if (!(((Alumno)persona).getAsignaturasSuperadas().isEmpty())){
			String[]superadasAlumno=((Alumno)persona).getAsignaturasSuperadas().split(",");
			for (int i=0; i<superadasAlumno.length; i++){
				int asignaturaSuperada=Integer.parseInt(superadasAlumno[i].trim());
				if (asignaturaSuperada==IDasignatura) anular=true;		
			}
		}
		//Si no hay errores se matricula al alumno en esa asignatura
		if (!anular) ((Alumno)persona).setDocenciaRecibida(String.valueOf(IDasignatura));
	}
}
	




/**
 * Función que se corresponde con el comardo "AsignaGrupo" del fichero "ejecuciones.txt".
 * Se le asignará al alumno indicado el grupo indicado (teórico o práctico) de la asignatura indicada (mediante sus IDs).
 * En caso de error se notifica mediante el fichero "avisos.txt" con la abreviatura AGRUPO y no se realizará la operación.
 * En el caso de que al alumno se le intente asignar un grupo de una asignatura de la cual ya tiene un grupo asignado no se generará ningún error pero no se le podrá asignar ese grupo (no se realizará la operación).
 * Los posibles errores que se pueden dar en esta función son: "Alumno inexistente", "Asignatura inexistente", "Alumno no matriculado", "Tipo de grupo incorrecto", "Grupo inexistente" y "Se genera solape".
 * @param entrada			String con los datos de entrada del fichero "ejecuciones.txt"
 * @param avisos			Objeto para imprimir en el fichero "avisos.txt"
 */

//FUNCION ASIGNAR GRUPO
public static void AsignaGrupo (String entrada, PrintWriter avisos){
	
	boolean anular = false; boolean grupoAsignado=false;
	//Leer los datos introducidos por el fichero "ejecuciones.txt"
	String[]data=entrada.split("\\s+");
	int IDalumno=Integer.parseInt(data[1]);
	int IDasignatura=Integer.parseInt(data[2]);
	String tipoGrupo=data[3];
	int IDgrupo=Integer.parseInt(data[4]);
	
	Persona persona = lhmPersonas.get(IDalumno);
	Asignatura asignatura = lhmAsignaturas.get(IDasignatura);
		
	if (!(persona instanceof Alumno)) {
		avisos.println("AGRUPO -- Alumno inexistente"); anular=true;
	}
	if (asignatura == null){
		avisos.println("AGRUPO -- Asignatura inexistente"); anular=true;
	}
	
	if (anular) return;
	
	//Comprobar si el alumno esta matriculado en esa asignatura
	//Y comprobra si el alumno ya tiene asignado un grupo para esa asignatura y ese tipo de grupo (en cuyo caso no se hara nada)
	String[]docenciaAlumno=((Alumno)persona).getDocenciaRecibida().split(";");
	for (int i=0; i<docenciaAlumno.length; i++){
		String conjunto=docenciaAlumno[i].trim();
		try{
			int IDasignaturasAlumno=Integer.parseInt(conjunto);
			if (IDasignaturasAlumno==IDasignatura) {
				anular=false; break;
			} else anular=true;
		}catch (NumberFormatException exception){
			String[] dividir=conjunto.split("\\s+");
	    	int IDasignaturasAlumno=Integer.parseInt(dividir[0]);
			String tipoGrupoAlumno=dividir[1];
			if (IDasignaturasAlumno==IDasignatura) {
				if (tipoGrupoAlumno.equals(tipoGrupo)) {
					grupoAsignado=true; anular=false; break;
				}
				else {
					anular=false; break;
				}
			} else anular=true;
		}
	}
	if (anular) avisos.println("AGRUPO -- Alumno no matriculado");
	
	if (!(tipoGrupo.equals("T")) && (!(tipoGrupo.equals("P")))){ 
		avisos.println("AGRUPO -- Tipo de grupo incorrecto"); anular=true;
	}
	else{
		Docencia grupoAsignar = new Docencia (IDasignatura, tipoGrupo, IDgrupo, asignatura);
		
		//Comprobar si existe el grupo en esa asignatura
		if (grupoAsignar.getInfoGrupo()==null)	{
			avisos.println("AGRUPO -- Grupo inexistente"); anular=true;
		}
		
		if(!anular && !grupoAsignado){
			//Comprobar si se genera solape con las asignaciones previas del alumno
			int duracion;
			if (tipoGrupo.equals("T")) duracion=asignatura.getDuracionTeoria();    	    	
			else duracion=asignatura.getDuracionPractica();
			if (!((((Alumno)persona).getDocenciaRecibida()).isEmpty())){
				String[]cadenaDocencia=(((Alumno)persona).getDocenciaRecibida()).split(";");
				for (int i=0; i<cadenaDocencia.length; i++){
					String conjunto=cadenaDocencia[i].trim();
					try{
		    			Integer.parseInt(conjunto);	
		    	    }catch(NumberFormatException exception){
		    	    	String[] dividir=conjunto.split("\\s+");
		    	    	int IDasignaturasAlumno=Integer.parseInt(dividir[0]);
		    			String tipoGrupoAlumno=dividir[1];
						Asignatura asignaturaDocenciaAlumno = lhmAsignaturas.get(IDasignaturasAlumno);
						Docencia docenciaRecibida = new Docencia (IDasignaturasAlumno, tipoGrupoAlumno, Integer.parseInt(dividir[2]), asignaturaDocenciaAlumno);
						if (docenciaRecibida.getInfoGrupo().getDia().equals(grupoAsignar.getInfoGrupo().getDia())){
							int duracionDocencia;
							if (tipoGrupoAlumno.equals("T")) duracionDocencia=asignaturaDocenciaAlumno.getDuracionTeoria();    	    	
							else duracionDocencia=asignaturaDocenciaAlumno.getDuracionPractica();
							if (docenciaRecibida.getInfoGrupo().generaSolape(duracionDocencia, grupoAsignar.getInfoGrupo(), duracion)){
								avisos.println("AGRUPO -- Se genera solape"); anular=true;
							}
						}
		    	    }
				}	
			}
		}
		//Si no hay errores se le asigna el grupo al alumno (si ya tiene un grupo asignado con anterioridad no se le asignara el pedido)
		if (!anular && !grupoAsignado) {
			((Alumno)persona).setDocenciaRecibida(grupoAsignar.toString());
			((Alumno)persona).setDocenciaRecibida("; "+String.valueOf(IDasignatura), ";");
		}
	}
}



/**
 * Función que se corresponde con el comardo "Evaluar" del fichero "ejecuciones.txt".
 * Se evaluará de la asignatura indicada (mediante su ID) a todos los alumnos indicados (mediante su ID) en el fichero de notas creado previamente por el usuario.
 * En caso de que la nota sea mayor que cinco, se modificará la nota del expediente del alumno, se borrarán de su docencia recibida la asignatura y los grupos de la misma que tuviera asignados, agregando esta asignatura a las superadas por el alumno.
 * En caso de que la nota sea menor que cinco, no se modificará la nota del expediante del alumno pero se borrarán los grupos que tuviera asignados de esa asignatura aunque quedará matriculado en dicha asignatura.
 * En caso de error se notifica mediante el fichero "avisos.txt" con la abreviatura EVALUA y no se realizará la operación.
 * Los posibles errores que se pueden dar en esta función son: "Asignatura inexistente", "Fichero de notas inexistente", "Alumno inexistente: Id Alumno", "Alumno no matriculado: Id Alumno" y "Nota incorrecta". Estos tres últimos errores irán precedidos por el texto "Error en línea: nº línea:" indicando así el número de la línea del fichero que contiene las notas en la cual se ha producido el error.

 * @param entrada			String con los datos de entrada del fichero "ejecuciones.txt"
 * @param avisos			Objeto para imprimir en el fichero "avisos.txt"
 */

//FUNCION EVALUAR ASIGNATURA
public static void EvaluarAsignatura (String entrada, PrintWriter avisos){
	
	boolean anular = false; int numeroLinea=0;
	//Leer los datos introducidos por el fichero "ejecuciones.txt"
	String[]data=entrada.split("\\s+");
	int IDasignatura=Integer.parseInt(data[1]);
	String nombreFichero=data[2];
	
	Asignatura asignatura = lhmAsignaturas.get(IDasignatura);
	
	if (asignatura == null){
		avisos.println("EVALUA -- Asignatura inexistente"); anular=true;
	}
	try {
		//Leer el fichero con las notas de los alumnos
		Scanner leerNotas = new Scanner (new FileInputStream(nombreFichero));
		while (leerNotas.hasNextLine()){
			boolean anularFichero=false;
			//Variables para enumerar el numero de la linea y el error que produce
			numeroLinea++;
			String errorLinea = "Error en linea ".concat(String.valueOf(numeroLinea)).concat(": ");
			
			//Leer los datos introducidos por el fichero que contiene las notas
			String lineaFichero=leerNotas.nextLine();
			String[]linea=lineaFichero.split("\\s+");
			int IDalumno=Integer.parseInt(linea[0]);
			double nota=Double.parseDouble(linea[1]);
			
			Persona persona = lhmPersonas.get(IDalumno);
				
			if (!(persona instanceof Alumno)){
				avisos.println(errorLinea+"EVALUA -- Alumno inexistente: "+IDalumno); anularFichero=true;
			}
			if (!anularFichero && !anular){
				//Comprobar si el alumno esta matriculado en esa asignatura
				if (((Alumno)persona).getDocenciaRecibida().isEmpty()){
					avisos.println(errorLinea+"EVALUA -- Alumno no matriculado: "+IDalumno); anularFichero=true;
				}else{
					String[]docenciaAlumno=((Alumno)persona).getDocenciaRecibida().split(";");
					for (int i=0; i<docenciaAlumno.length; i++){
						String conjunto=docenciaAlumno[i].trim();
						String[] dividir=conjunto.split("\\s+");
						int IDasignaturaDocencia=Integer.parseInt(dividir[0]);
						if (IDasignaturaDocencia==IDasignatura) {
							anularFichero=false; break;
						} else anularFichero=true;
					}
					if (anularFichero && !anular)
						avisos.println(errorLinea+"EVALUA -- Alumno no matriculado: "+IDalumno);
				}
			}
			//Comprobar si la nota es correcta
			if (nota<0.00 || nota>10.00){
				avisos.println(errorLinea+"EVALUA -- Nota incorrecta"); anularFichero=true;
			}
			//Si no hay ningun error se hacen las operaciones pertinentes dependiendo de la nota obtenida
			if (!anularFichero && !anular){
				if (nota>=5){	
					//Modificamos su nota media
					((Alumno)persona).setNotaMediaExpediente(nota);
					
					//Incluímos la asignatura en asignaturasSuperadas
					((Alumno)persona).setAsignaturasSuperadas(IDasignatura);
					
					//Eliminamos la asignaturas y los grupos de la misma de docenciaRecibida
					String[]docenciaAlumno=((Alumno)persona).getDocenciaRecibida().split(";");
					for (int i=0; i<docenciaAlumno.length; i++){
						String conjunto=docenciaAlumno[i].trim();						
						try{
			    			int IDasignaturasAlumno=Integer.parseInt(conjunto);
			    			if (IDasignaturasAlumno==IDasignatura)
								((Alumno)persona).setDocenciaRecibida(String.valueOf(IDasignaturasAlumno), "");
			    	    }catch(NumberFormatException exception){
			    	    	String[] dividir=conjunto.split("\\s+");
			    			Docencia docenciaRecibidaAlumno = new Docencia(Integer.parseInt(dividir[0]), dividir[1], Integer.parseInt(dividir[2]));
			    			if (docenciaRecibidaAlumno.getIDasignatura()==IDasignatura)
								((Alumno)persona).setDocenciaRecibida(docenciaRecibidaAlumno.toString(), "");
			    	    }
					}	
				}else{
					/*Si la nota es menor que 5 eliminaos los grupos en los que estuviera matriculado de esa asignatura 
					(dejando unicamente el identificador de la asignatura en docenciaRecibida). No se modifica la nota*/
					String[]docenciaAlumno=((Alumno)persona).getDocenciaRecibida().split(";");
					for (int i=0; i<docenciaAlumno.length; i++){
						String conjunto=docenciaAlumno[i].trim();						
						try{
							Integer.parseInt(conjunto);
			    	    }catch(NumberFormatException exception){
			    	    	String[] dividir=conjunto.split("\\s+");
			    			Docencia docenciaRecibidaAlumno = new Docencia(Integer.parseInt(dividir[0]), dividir[1], Integer.parseInt(dividir[2]));
			    			if (docenciaRecibidaAlumno.getIDasignatura()==IDasignatura){
								((Alumno)persona).setDocenciaRecibida(conjunto.trim(), String.valueOf(IDasignatura)+";");
			    			}
			    	    }
					}	
				}
			}		
		}
		//Cierre del fichero donde estan almacenadas las notas de los alumnos
		leerNotas.close();
	}catch (FileNotFoundException exception){
		avisos.println("EVALUA -- Fichero de notas inexistente");
	}
}





/**
 * Función que se corresponde con el comardo "ObtenerCaldarioClases" del fichero "ejecuciones.txt".
 * Se generará un archivo con extensión .txt con el nombre indicado por el usuario en el que se ordenarán de forma cronológica (día y hora) los grupos asignados al alumno indicado (mediante su ID). En este fichero figurarán, separados por puntos y comas(;), el día, la hora, el tipo de grupo, el ID del grupo, el nombre de la asignatura y el profesor que la imparte.
 * En caso de error se notifica mediante el fichero "avisos.txt" con la abreviatura OCALEN y no se realizará la operación.
 * Para esta función se hará uso de la interfaz Comparator y su implementación en la clase Docencia.
 * Los posibles errores que se pueden dar en esta función son: "Alumno inexistente" y "Alumno sin asignaciones".
 * @param entrada			String con los datos de entrada del fichero "ejecuciones.txt"
 * @param avisos			Objeto para imprimir en el fichero "avisos.txt"
 * @throws IOException		La funcion puede larzar una excepción de Entrada/Salida
 */

//FUNCION OBTENER CALENDARIO CLASES
public static void ObtenerCalendarioClases(String entrada, PrintWriter avisos) throws IOException{
	
	boolean anular=true;
	List<Docencia>Horario = new ArrayList<Docencia>();
	//Leer los datos introducidos por el fichero "ejecuciones.txt"
	String[]data=entrada.split("\\s+");
	int IDalumno=Integer.parseInt(data[1]);
	String nombreFichero=data[2];
	
	Persona persona = lhmPersonas.get(IDalumno);
	
	if (!(persona instanceof Alumno)) {
		avisos.println("OCALEN -- Alumno inexistente"); return;
	}
	if (((Alumno)persona).getDocenciaRecibida().isEmpty()){
		avisos.println("OCALEN -- Alumno sin asignaciones"); return;
	}
	
	//Comprobar si el alumno tiene algún grupo asignado y de ser asi se almacena su docencia en una lista
	String[]docenciaAlumno=((Alumno)persona).getDocenciaRecibida().split(";");
	for (int i=0; i<docenciaAlumno.length; i++){
		String conjunto=docenciaAlumno[i].trim();						
		try{
			Integer.parseInt(conjunto);
	    }catch(NumberFormatException exception){
	    	anular=false;
	    	String[] dividir=conjunto.split("\\s+");
	    	int IDasignaturasAlumno=Integer.parseInt(dividir[0]);
	    	String tipoGrupoAlumno=dividir[1];
	    	int IDgrupoAlumno=Integer.parseInt(dividir[2]);
	    	Asignatura asignaturaDocenciaAlumno = lhmAsignaturas.get(IDasignaturasAlumno);
			Docencia infoDocencia = new Docencia (IDasignaturasAlumno, tipoGrupoAlumno, IDgrupoAlumno, asignaturaDocenciaAlumno);
			Horario.add(infoDocencia);	
	    }
	}	
	if (anular) avisos.println("OCALEN -- Alumno sin asignaciones");
	//Si no hay ningun error se ordena e imprime el horario en el fichero con el nombre seleccionado
	else{
		PrintWriter escribirHorario = new PrintWriter(new FileOutputStream(nombreFichero));
		escribirHorario.println("dia; hora; tipo grupo; id grupo; asignatura; docente");
		
		//Ordenar la lista según el criterio de CompararHorario (ver en clase Docencia)
		Collections.sort(Horario, new Docencia());

		//Imprimir la lista en el fichero
		Iterator<Docencia> iteratorDocencia = Horario.iterator();
		while (iteratorDocencia.hasNext()) {
			Iterator<Persona> iteratorPersona = lhmPersonas.values().iterator();
			String nombreYapellidos="";
			Docencia imprimirDocencia = iteratorDocencia.next();
			while(iteratorPersona.hasNext()){
		    	Persona personaIterator = iteratorPersona.next();
		    		if (personaIterator instanceof Profesor){
		    			if (!((((Profesor)personaIterator).getDocenciaImpartida()).isEmpty())){
		    				String[]cadenaDocenciaImpartida=(((Profesor)personaIterator).getDocenciaImpartida()).split(";");
		    				for (int i=0; i<cadenaDocenciaImpartida.length; i++){
		    					String conjunto=cadenaDocenciaImpartida[i].trim();
		    					String[] dividir=conjunto.split("\\s+");
		    					Docencia docenciaImpartidaProfesor = new Docencia (Integer.parseInt(dividir[0]), dividir[1], Integer.parseInt(dividir[2]));
		    					if (docenciaImpartidaProfesor.equals(imprimirDocencia)){
		    						nombreYapellidos = ((Profesor)personaIterator).getNombre()+" "+((Profesor)personaIterator).getApellidos();
		    						break;
		    					}
		    				}
		    			}
		    		}
			}

			escribirHorario.println(imprimirDocencia.getInfoGrupo().getDia()+"; "
					+imprimirDocencia.getInfoGrupo().getHora()+"; "+imprimirDocencia.getTipoGrupo()+"; "
					+imprimirDocencia.getIDgrupo()+"; "+lhmAsignaturas.get(imprimirDocencia.getIDasignatura()).getNombre()+"; "+nombreYapellidos);
		}
		//Cierre del fichero donde queda impreso el horario del alumno
		escribirHorario.close();
	}
}



/**
 * Función que se corresponde con el comardo "OrdenarProfesoresPorCargaDocente" del fichero "ejecuciones.txt".
 * Se generará un archivo con extensión .txt con el nombre indicado por el usuario en el cual se almacenarán los profesores titulares del fichero "personas.txt" en función de la carga docente que ostenten en orden ascendente. En caso de coincidir dos o más profesores en su carga docente se ordenarán según su ID en orden ascendente.
 * En caso de error se notifica mediante el fichero "avisos.txt" con la abreviatura OTIT y no se realizará la operación.
 * Para esta función se hará uso de la interfaz Comparator y su implementación en la clase Profesor.
 * El posible error que se puede dar en esta función es: "No hay profesores titulares".
 * @param entrada			String con los datos de entrada del fichero "ejecuciones.txt"
 * @param avisos			Objeto para imprimir en el fichero "avisos.txt"
 * @throws IOException		La funcion puede larzar una excepción de Entrada/Salida
 */

//FUNCION ORDENAR PROFESORES POR CARGA DOCENTE
public static void OrdenarProfesoresPorCargaDocente (String entrada, PrintWriter avisos) throws IOException{
	
	boolean anular=true; int i=0;
	List<Profesor>ALordenarProfesores = new ArrayList<Profesor>();
	//Leer los datos introducidos por el fichero "ejecuciones.txt"
	String[]data=entrada.split("\\s+");
	String nombreFichero=data[1];
	
	//Comprobar los elementos del mapa lhmPersonas y añadirlos a la lista ALordenaProfesores aquellos que sean de la clase Profesor y que su catecoria es titular
	Iterator<Persona> iterator = lhmPersonas.values().iterator();
	while (iterator.hasNext()){
	  Persona personaIterator = iterator.next();
	  if (personaIterator instanceof Profesor){
		  if (((Profesor)personaIterator).getCategoriaLaboral().equalsIgnoreCase("Titular")) {
			  anular=false;
			  ((Profesor)personaIterator).setCargaDocente(lhmAsignaturas);
			  ALordenarProfesores.add((Profesor)personaIterator);
		  }
	  }
	}
	if (anular)	avisos.println("OTIT -- No hay profesores titulares");
	else{
		//Si no hay ningun error se ordena e imprime los profesores en el fichero con el nombre seleccionado
		PrintWriter ordenarProfesores = new PrintWriter(new FileOutputStream(nombreFichero));
	
		//Ordenar la lista según el criterio de CompararProfesores (ver en clase Profesor)
		Collections.sort(ALordenarProfesores, new Profesor());
		
		//Imprimir la lista en el fichero
		Iterator<Profesor> iteratorProfesor = ALordenarProfesores.iterator();
		while (iteratorProfesor.hasNext()) {
			i++;
			ordenarProfesores.println(iteratorProfesor.next());
			if (i!=ALordenarProfesores.size()) ordenarProfesores.println("*");
		}
		//Cierre del fichero donde quedan impresos los profesores titulares ordenados
		ordenarProfesores.close();
	}
}



/**
 * Función utilizada para leer los ficheros "personas.txt" y "asignaturas.txt" y volcarlos en los mapas correspondientes (lhmPersonas o lhmAsignaturas respectivamente).
 * Esta función no detectará error alguno si los ficheros son correctos.
 * @param leerFichero		Objeto para leer los ficheros "personas.txt" o "asignaturas.txt"
 */

//FUNCION PARA LEER LOS FICHEROS PERRSONAS Y ASIGNATURAS
public static void leerFichero(Scanner leerFichero){

	  while (leerFichero.hasNextLine()){
		  //La primera linea de cada elemento ya sea persona o asignatura  siempre sera el ID
		  int ID=Integer.parseInt(leerFichero.nextLine());
		  String linea=leerFichero.nextLine();
		  
		  //SI ES PERSONA
		  if( (linea.equalsIgnoreCase("Profesor")) || (linea.equalsIgnoreCase("Alumno"))  ){
			  String perfil=linea;
			  String nombre=leerFichero.nextLine();
			  String apellidos=leerFichero.nextLine();
			  String[] fecha1=(leerFichero.nextLine()).split("/");
			  	int dia1 = Integer.parseInt(fecha1[0]);
			  	int mes1 = Integer.parseInt(fecha1[1]);
			  	int anho1 = Integer.parseInt(fecha1[2]);
			  	LocalDate nacimiento = LocalDate.of(anho1,mes1,dia1);
			  //Profesor
			  if ( perfil.equalsIgnoreCase("Profesor") ){
			  String categoria=leerFichero.nextLine();
			  String departamento=leerFichero.nextLine();
			  int horasasignables; String stringHorasasignables=leerFichero.nextLine();
			  if (!(stringHorasasignables.isEmpty())) horasasignables=Integer.parseInt(stringHorasasignables);
			  else horasasignables=0;
			  String docenciaImpartida;
			  if(leerFichero.hasNextLine()) docenciaImpartida=leerFichero.nextLine();
			  else docenciaImpartida="";
			  Persona persona = new Profesor(ID, perfil,nombre,apellidos,nacimiento,departamento,categoria,horasasignables,docenciaImpartida);
			  lhmPersonas.put(ID, persona);
			  }
			  //Alumno
			  if ( perfil.equalsIgnoreCase("Alumno") ){
			  String[]fecha2=leerFichero.nextLine().split("/");
			  	int dia2 = Integer.parseInt(fecha2[0]);
			  	int mes2 = Integer.parseInt(fecha2[1]);
			  	int anho2 = Integer.parseInt(fecha2[2]);
			  	LocalDate fechaIngreso = LocalDate.of(anho2, mes2, dia2);
			  String asignaturasSuperadas = leerFichero.nextLine();
			  double notaMedia; String stringNotaMedia=leerFichero.nextLine();
			  if (!(stringNotaMedia.isEmpty())) notaMedia=Double.parseDouble(stringNotaMedia);
			  else notaMedia=0;
			  String docenciaRecibida;
			  if(leerFichero.hasNextLine()) docenciaRecibida=leerFichero.nextLine();
			  else docenciaRecibida="";
			  Persona persona = new Alumno(ID, perfil,nombre,apellidos,nacimiento,fechaIngreso,asignaturasSuperadas,notaMedia,docenciaRecibida);     
			  lhmPersonas.put(ID, persona);			  
			  }
			  
		  //SI ES ASIGNATURA
		  }else{
			  String nombre=linea;
			  String siglas=leerFichero.nextLine();
			  String coordinador=leerFichero.nextLine();
			  String prerequisitos=leerFichero.nextLine();
			  String duracionTeoria=leerFichero.nextLine();
			  String duracionPractica=leerFichero.nextLine();
			  String gruposTeoricos=leerFichero.nextLine();
			  String gruposPracticos;
			  if(leerFichero.hasNextLine()) gruposPracticos=leerFichero.nextLine();
			  else gruposPracticos="";
			  Asignatura asignatura = new Asignatura(ID,nombre,siglas,coordinador,prerequisitos,duracionTeoria,duracionPractica,gruposTeoricos,gruposPracticos);
			  lhmAsignaturas.put(ID, asignatura);			
		  }
		  
		if(leerFichero.hasNextLine()) leerFichero.nextLine();
		else break;
	  }
	  //Cierre del fichero que estemos leyendo ("personas.txt" o "asignaturas.txt")
	  leerFichero.close();
}



/**
 * Función utilizada para escribir en los ficheros "personas.txt" y "asignaturas.txt" el contenido de los mapas correspondientes (lhmPersonas y lhmAsignaturas respectivamente).
 * Además, es la encargada de separar cada elemento de los ficheros mediante un asterisco, excepto en el último elemento del mapa, a continuación del cual no se insertará ningún elemento.
 * Esta función no detectará error alguno.
 * @param mapa					Mapa que la función va a imprimir (lhmPersonas o lhmAsignaturas)
 * @param escribirFichero		Objeto para imprimir en los ficheros "personas.txt" o "asignaturas.txt"
 */

//FUNCION PARA ESCRIBIR LOS MAPAS EN LOS FICHEROS
public static void escribirFichero(Map mapa, PrintWriter escribirFichero){	
	
	int i=0;
	Iterator iterator = mapa.entrySet().iterator();
	while (iterator.hasNext()) {
		i++;
		Map.Entry elemento = (Map.Entry)iterator.next();
		escribirFichero.print(elemento.getValue().toString());
		//Mientras no sea el ultimo elemento del mapa, se salta de liea y se imprime un asterisco al final de dicho elemento
		if (i!=mapa.size()) {
			escribirFichero.println(); escribirFichero.println("*");
		}
	}
	//Cierre del fichero que se acaba de escribir ("personas.txt" o "asignaturas.txt")
	escribirFichero.close();
}




}