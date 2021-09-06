package com.example.vasoactivas;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    //<editor-fold desc="VARIABLES">
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "drogas.db";
    public static final String TABLE_DROGA = "droga";
    public static final String TABLE_DOSIS = "dosis";
    public Context myContext;
    public SQLiteDatabase myDataBase;
    public static String DB_PATH;
    //</editor-fold>

    //<editor-fold desc="METODOS MANTENIMIENTO BASE DE DATOS">
    public DBHelper(@Nullable Context myContext) {
        super(myContext, DATABASE_NOMBRE, null, DATABASE_VERSION);
        this.myContext = myContext;
        this.DB_PATH = this.myContext.getDatabasePath(DATABASE_NOMBRE).getAbsolutePath();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_DROGA);
        onCreate(sqLiteDatabase);

    }

    /**
     * Crea una base de datos vacia en el sistema y la reescribe con nuestro fichero de base de datos.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //la base de datos existe y no hacemos nada.
        } else {
            //Llamando a este metodo se crea la base de datos vac�a en la ruta por defecto del sistema
            //de nuestra aplicaci�n por lo que podremos sobreescribirla con nuestra base de datos.
            this.getWritableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copiando Base de Datos");
            }
        }
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }


    /**
     * Comprueba si la base de datos existe para evitar copiar siempre el fichero cada vez que se abra la aplicacion.
     *
     * @return true si existe, false si no existe
     */
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DATABASE_NOMBRE);
        return dbFile.exists();
    }

    /**
     * Copia nuestra base de datos desde la carpeta assets a la recien creada
     * base de datos en la carpeta de sistema, desde donde podremos acceder a ella.
     * Esto se hace con bytestream.
     */
    private void copyDataBase() throws IOException {
        //Abrimos el fichero de base de datos como entrada
        InputStream myInput = myContext.getAssets().open(DATABASE_NOMBRE);

        //Ruta a la base de datos vacia recien creada
        String outFileName = DB_PATH + DATABASE_NOMBRE;

        //Abrimos la base de datos vacìa como salida
        OutputStream myOutput = new FileOutputStream(outFileName);

        //Transferimos los bytes desde el fichero de entrada al de salida
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Liberamos los streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDB() {
        try {
            createDataBase();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("DataBaseHelper", e.toString());
        }

        openDataBase();
        getWritableDatabase();
        getReadableDatabase();

    }

    public void openDataBase() throws android.database.SQLException {

        // Open the database
        String myPath = DB_PATH + DATABASE_NOMBRE;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE // this was readable only
                        | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }
    //</editor-fold>

    //OBTENER MEDICAMENTO
    public String[] medicina(String med) throws SQLException {
        openDB();
        String medici[] = new String[5];
        int contador = 0;
        String selection = Droga.CN_nombre + " = ? ";
        String selectionArgs[] = new String[]{med};
        Cursor c = myDataBase.query(
                "droga",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (c != null) {
            c.moveToFirst();
        }
        close();
        while (contador <= 3) {
            medici[contador] = c.getString(contador);
            contador++;
        }
        String interme = medici[2];
        interme = medici[2];
        return medici;

    }
    //OBTIENE LAS DOSIS DE LOS MEDICAMENTOS
    public String[] dosis(String med)throws SQLException{
        openDB();
        int contador = 0,tamano=0;
        String medici[] = new String[10];
        String selection = Droga.CN_nombre + " = ? ";
        String selectionArgs[] = new String[]{med};
        Cursor c=myDataBase.query(Dosis.Tabla_dosis,null,selection,selectionArgs,null,null,null);
        if (c != null) { c.moveToFirst(); }
        close();
        while (contador<=9) {
            if(c.getString(contador)!=null){
            medici[contador] = c.getString(contador);
            tamano++;}
            contador++;
        }
        String devol[] = new String[tamano];
        devol = medici;

        return devol;

    }
    //QUITAR BLANCOS


    //OBTENER SOLO LOS MEDICAMENTOS
    public String[] todas_medicina() throws SQLException {
        openDB();
        int contador = 0;
        String [] campos = {Droga.CN_nombre};
        Cursor c = myDataBase.query(
                Droga.Tabla_droga,
                campos, null, null, null, null, null
        );
        if (c != null) { c.moveToFirst(); }
        close();
        int tamcursor = c.getCount();
        ArrayList<String> sacados = new ArrayList<>();
        if (c.moveToFirst()){
            do{
                sacados.add(c.getString(0));
            } while (c.moveToNext());
        }
        int tamano = sacados.size();
        String medicamentos[] = new String[tamano];
        contador = 0;
        while (contador < (sacados.size())){
            medicamentos[contador] = sacados.get(contador);
            contador++;
        }
        return medicamentos;
    }




}
