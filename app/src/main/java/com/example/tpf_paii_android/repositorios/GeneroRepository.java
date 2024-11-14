package com.example.tpf_paii_android.repositorios;

import android.content.Context;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Genero;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GeneroRepository {

    private final Context context;
    private final ExecutorService executor;

    public GeneroRepository(Context context){
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
    }


    // Metodo obtenerTodod() los generos de la bd ASYNC
    public LiveData<List<Genero>> obtenerTodos() {
        MutableLiveData<List<Genero>> liveDataGeneros = new MutableLiveData<>();

        // Ejecuta la consulta en un hilo secundario
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Genero> generos = new ArrayList<>();
                Connection connection = null;
                try {
                    // Conectar a la base de datos
                    connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);

                    if (connection != null){
                        String query = "SELECT * FROM genero";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        // Recorrer los resultados y agregarlos a la lista
                        while(resultSet.next()) {
                            int id = resultSet.getInt("id_genero");
                            String descripcion = resultSet.getString("descripcion");
                            generos.add(new Genero(id, descripcion));
                        }
                    }

                } catch (Exception e) {
                    Log.e("GeneroRepository", "Error al obtener generos", e);
                    // Muestra un error en el Toast
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Error al obtener los géneros", Toast.LENGTH_SHORT).show()
                    );
                } finally {
                    try {
                        // Cierra la conexión
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                        }
                    } catch (Exception e) {
                        Log.e("GeneroRepository", "Error al cerrar la conexión", e);
                    }
                }

                // Publica los géneros obtenidos en el LiveData
                new Handler(Looper.getMainLooper()).post(() ->
                        liveDataGeneros.setValue(generos)
                );
            }
        });

        return liveDataGeneros;
    }
}