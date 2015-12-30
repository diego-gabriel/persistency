Persistency es, por ahora, un pequeño y simple ORM que permite dar persistencia
objetos de forma sencilla y rapida, e independientemente de la base de datos 
elegida para almacenar la informacion.

Requerimientos:

    - Java 1.8 (JDK 8) - aunque creo que puede funcionar incluso desde la version 1.6

ANTES DE COMENZAR

Antes de comenzar a utilizar Persistency, es necesario crear una Clase que implemente
la intefaz PersistentDatabase que define los siguientes metodos

    *- int insert(String table, HashMap<String, Object> values);
            inserta una nueva fila en la tabla 'table', los valores estan
            especificados en 'values' de la forma 'columna' -> 'valor'
            DEVUELVE el id del elemento insertado, o -1 si ocurrio algun error

    *- int update(String table, HashMap<String, Object> values, int rowID);
            actualiza la los valores especificados en 'values' en la fila 'rowID'
            de la tabla 'table'
            DEVUELVE la cantidad de elementos modificados, o -1 si ocurrio algun error

    *- HashMap<String, String> find(String table, String[] proyection, int rowID);
            Obtiene de la fila 'rowID' de la tabla 'table' los valores de las 
            columnas especificadas en 'proyection'
            DEVUELVE un HashMap <String, String>: columna -> valor , null si ocurrio algun error

    *- Collection<HashMap<String, String>> where(String table, String[] proyection, String selection, String[] sArgs);
            Obtiene todas las filas de la tabla 'table' con los valores de las
            columnas especificadas en 'proyection' que concuerden con las condiciones
            especificadas en 'selection' y 'sArgs'
            'selection' es una cadena que representa las condiciones de la parte del 
            WHERE en una consulta SQL, donde las variables estan reemplazadas por un '?'
            (sin las comillas), esta varibles estan definidas en 'sArgs'. Por ejemplo:
               selection = "id = ?"
               sArgs = {"5"}
               equivale a: "id = 5"
            DEVUELVE una coleccion de HashMap<String, String> como el mencionado en 'find'. 
            Si ocurre algun error o no se encuentran resultados, la coleccion debera ser vacia.

El usuario debera implementar la coneccion a la base de datos de su preferencia 
y todas las clases que considere necesarias para poder implementar los 4 metodos descritos
arriba.

COMO HACER UN OBJETO PERSISTENTE

Para crear una clase de objetos persistentes, solamente se debe heredar de la 
clase PersistentObject, implementar el metodo 'public String tableName()'
que debera devolver el nombre de la tabla de la base de datos donde se guardara 
el objeto (o su equivalente en la base de datos elegida) y llamar al constructor
de PersustentObject - super() - en cada constructor que la clase implemente. 
Ademas, esta tabla debera tambien existir en la base de datos y sus columnas 
deben tener el mismo nombre que los atributos de la clase. Ademas, cada tabla 
debe tener una columna 'id' de tipo entero que servira para identificar cada 
objeto (se recomienda ademas que sea autoincrementado) Por ejemplo:
    
//en Contacto.java
    
    public class Contacto extends PersistentObject{

        private String name;
        private int phone_number;
        public Contacto(){
            super();
        }
        public String tableName(){
            return "contacto";
        }
    }

//en la definicion de la base de datos, digamos database.sql

    CREATE TABLE contacto(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        phone_number INT
    );

Por el momento, Persistency soporta solo datos de tipo INT y STRING (o su 
su equivalente en la base de datos elegida, TEXT, VARCHAR, etc.)
Sin embargo, si se desea que, por ejemplo Contacto tenga un atributo de tipo Date,
esta clase (la clase Date) debera implementar ademas el metodo 'toString()' y un 
constructor que reciba unicamente un String y que permita reconstruir el objeto 
a partir del String generado por 'toString()', ademas, la columna para almacenar 
este dato en la base de datos, debera ser de tipo String. Por ejemplo:

//en Contacto.java
    
    public class Contacto extends PersistentObject{

        private String name;
        private int phone_number;
        private Date birthday; //<- AQUI ESTA EL OBJETO

        public Contacto(){
            super();
        }

        public String tableName(){
            return "contacto";
        }
    }

//en Date.java
    //nota que esta clase no necesita extender de PersistentObject
    public class Date{
        private int day;
        private int month;
        private int year;
        
        public Date(int a, int b, int c){
            day = a;
            month = b;
            year = c;
        }
        
        //este es el constructor que se debe implementar
        public Date(String aString){
            String[] data = aString.split("/");
            day = Integer.parseInt(data[0]);
            month = Integer.parseInt(data[1]);
            year = Integer.parseInt(data[2]);
        }

        public String toString(){
            return "" + day + "/" + month + "/" + year;
        }
    }

//en la definicion de la base de datos, digamos database.sql

    CREATE TABLE contacto(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        phone_number INT,
        birthday TEXT
    );

CONFIGURACION DEL ENTORNO DE PERSISTENCY

Antes de realizar cualquier interaccion con los objetos de Persistency, se debera 
llamar una unica vez (al iniciar el programa) al metodo 'setUp(PersistentDatabase database)' 
de la clase ObjectPersistency, pasandole como parametro una instancia de la clase
que implementa la interfaz PersistentDatabase. Ejemplo:

//al iniciar el programa, comunmente en el metodo main() de la clase Main
//Main.java
    public class Main(){
        publis static void main(String[] args){
            ObjectPersistency.setUp(new DatabaseConnection());
            //aca continua el programa
               .
               .
               .
        }
    }

//DatabaseConnection.java
    
    public class DatabaseConnection implements PersistentDatabase{
        //implementada aqui la clase
            .
            .
            .
    }

COMO RECUPERAR OBJETOS Y REALIZAR CONSULTAS - La clase ObjectRetriever

Persistency trae consigo una clase ObjectRetriever que sirve para traer objetos 
desde la base de datos. Tiene 2 metodos publicos:

    *- PersistenObject find(PersistenObject anExampleObject, int id)
        'anExampleObject es un objeto de ejemplo, cualquier instancia de la clase 
        del objeto que se quiere traer, 'id' es el id del objeto que se quiere traer.
        DEVUELVE un PersistentObject si se ha encontrado algun objeto de la clase de 
        'anExampleObject' con el id: 'id' en la base de datos, caso contrario 
        devuelve null

        Ejemplo:
        
            .
            . 
            .
        ObjectRetriever retriever = new ObjectRetriever();
        Contacto contactoPrueba = (Contacto)retriever.find(new Contacto(), 5);
            .
         //devuelve el contacto con id 5
            .

    *- Collection<PersistentObject> where(PersistenObject anExampleObject, String selection, String[] sArgs)
        'anExampleObject' cumple la misma funcion que en el metodo find. 'selection' y 'sArgs' 
        representan las condiciones de seleccion, al igual que en el metodo 'where' 
        de PersistentDatabase
        DEVUELVE una lista con todos los elementos encontrados que cumplan con las
        condiciones especificadas en 'selection' y 'sArgs'

            .
            . 
            .
        ObjectRetriever retriever = new ObjectRetriever();
        String[] sArgs = {"25/12/1994"}
        Collection<PersitentObject> contactos = retriever.where(new Contacto(), "birthday = ?", sArgs);
            .
         //devuelve todos los contactos cuya fecha de nacimiento es "25/12/1994"
            .
               
