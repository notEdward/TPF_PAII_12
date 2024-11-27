package com.example.tpf_paii_android.repositorios;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Empresa;
import com.example.tpf_paii_android.modelos.EstadoNivelEducativo;
import com.example.tpf_paii_android.modelos.Estudiante;
import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.Provincia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EstudianteRepository {

    private final Context context;
    private final ExecutorService executor;

    public EstudianteRepository(Context context){
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
    }


    // Metodo obtenerGeneros() los generos de la bd ASYNC
    public LiveData<List<Genero>> obtenerGeneros() {
        MutableLiveData<List<Genero>> liveDataGeneros = new MutableLiveData<>();

        // Ejecuta la consulta en un hilo secundario
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Genero> generos = new ArrayList<>();
                generos.add(new Genero (0,"Seleccione género"));
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
                        e.printStackTrace();
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




    public LiveData<List<NivelEducativo>> obtenerNivelEducativo() {
        MutableLiveData<List<NivelEducativo>> liveDataNivelesEduc = new MutableLiveData<>();

        // Ejecuta la consulta en un hilo secundario
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<NivelEducativo> nivelesEducativo = new ArrayList<>();
                nivelesEducativo.add(new NivelEducativo (0,"Seleccione Nivel"));
                Connection connection = null;
                try {
                    // Conectar a la base de datos
                    connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);

                    if (connection != null){
                        String query = "SELECT * FROM nivel_educativo";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        // Recorrer los resultados y agregarlos a la lista
                        while(resultSet.next()) {
                            int id = resultSet.getInt("id_nivel_educativo");
                            String descripcion = resultSet.getString("descripcion");
                            nivelesEducativo.add(new NivelEducativo(id, descripcion));
                        }
                    }

                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Error al obtener los Niveles Educ", Toast.LENGTH_SHORT).show()
                    );
                } finally {
                    try {
                        // Cierra la conexión
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Publica los géneros obtenidos en el LiveData
                new Handler(Looper.getMainLooper()).post(() ->
                        liveDataNivelesEduc.setValue(nivelesEducativo)
                );
            }
        });
        return liveDataNivelesEduc;
    }



   public LiveData<List<EstadoNivelEducativo>> obtenerEstadoNivelEducativo() {
        MutableLiveData<List<EstadoNivelEducativo>> liveDataEstadoNivelesEduc = new MutableLiveData<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<EstadoNivelEducativo> estadoNivelesEducativo = new ArrayList<>();
                estadoNivelesEducativo.add(new EstadoNivelEducativo (0,"Seleccione estado"));
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);

                    if (connection != null){
                        String query = "SELECT * FROM estado_nivel";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        while(resultSet.next()) {
                            int id = resultSet.getInt("id_estado_nivel");
                            String descripcion = resultSet.getString("descripcion");
                            estadoNivelesEducativo.add(new EstadoNivelEducativo(id, descripcion));
                        }
                    }

                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Error al obtener los Estado educativo", Toast.LENGTH_SHORT).show()
                    );
                } finally {
                    try {
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                    new Handler(Looper.getMainLooper()).post(() ->
                        liveDataEstadoNivelesEduc.setValue(estadoNivelesEducativo)
                );
            }
        });
        return liveDataEstadoNivelesEduc;
    }

    public LiveData<List<Provincia>> obtenerProvincias() {
        MutableLiveData<List<Provincia>> liveDataProvincias = new MutableLiveData<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Provincia> provincias = new ArrayList<>();
                provincias.add(new Provincia (0,"Seleccione provincia"));
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);

                    if (connection != null){
                        String query = "SELECT * FROM provincia";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        while(resultSet.next()) {
                            Provincia prov = new Provincia();
                            prov.setId_provincia(resultSet.getInt("id_provincia"));
                            prov.setNombre(resultSet.getString("nombre"));
                            provincias.add(prov);
                        }
                    }
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Error al obtener los Provincias", Toast.LENGTH_SHORT).show()
                    );
                } finally {
                    try {
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                new Handler(Looper.getMainLooper()).post(() ->
                        liveDataProvincias.setValue(provincias)
                );
            }
        });
        return liveDataProvincias;
    }

    public LiveData<List<Localidad>> obtenerLocalidadesPorProvincia(int idProvincia) {
        MutableLiveData<List<Localidad>> liveDataLocalidades = new MutableLiveData<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Localidad> localidades = new ArrayList<>();
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);

                    if (connection != null){
                        String query = "SELECT * FROM localidad WHERE id_provincia = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, idProvincia);
                        ResultSet resultSet = statement.executeQuery();

                        while(resultSet.next()) {
                            Localidad loc = new Localidad();
                            loc.setId_localidad(resultSet.getInt("id_localidad"));
                            loc.setNombre(resultSet.getString("nombre"));
                            loc.setId_provincia(resultSet.getInt("id_provincia"));
                            localidades.add(loc);
                        }
                    }

                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Error al obtener las Localidades", Toast.LENGTH_SHORT).show()
                    );
                } finally {
                    try {
                        // Cierra la conexión
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                        }
                    } catch (Exception e) {
                        Log.e("LocalidadesRepository", "Error al cerrar la conexión", e);
                    }
                }

                // Publica los géneros obtenidos en el LiveData
                new Handler(Looper.getMainLooper()).post(() ->
                        liveDataLocalidades.setValue(localidades)
                );
            }
        });
        return liveDataLocalidades;
    }



    public LiveData<Boolean> registrarEstudiante(Estudiante estudiante, int idUsuario) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            boolean registrada = false;
            String query = "INSERT INTO estudiante (dni, id_usuario, nombre, apellido, " +
                    "id_genero, email, telefono, direccion, id_localidad, id_nivel_educativo, id_estado_nivel) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, estudiante.getDni());
                ps.setInt(2, idUsuario);
                ps.setString(3, estudiante.getNombre());
                ps.setString(4, estudiante.getApellido());
                ps.setInt(5, estudiante.getId_genero());
                ps.setString(6, estudiante.getEmail());
                ps.setString(7, estudiante.getTelefono());
                ps.setString(8, estudiante.getDireccion());
                ps.setInt(9, estudiante.getId_localidad());
                ps.setInt(10, estudiante.getId_nivelEducativo());
                ps.setInt(11, estudiante.getId_estadoNivelEducativo());

                // Ejecuta la consulta
                int filasAfectadas = ps.executeUpdate();

                if (filasAfectadas > 0) {
                    registrada = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            resultLiveData.postValue(registrada);
        });
        executor.shutdown();
        return resultLiveData;
    }
}


