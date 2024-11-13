package com.example.tpf_paii_android.repositorios;

import android.os.Handler;
import android.os.Looper;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.Tutor;
import com.example.tpf_paii_android.modelos.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TutorRepository {
/*
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // Repositorios usados
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final GeneroRepository generoRepository = new GeneroRepository();

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    // Obtener todos los tutores async (hilo secundario)
    public void getAllTutores(DataCallback<List<Tutor>> callback){
        executor.execute(() -> {
            List<Tutor> tutores = new ArrayList<>();
            String query = "SELECT * FROM tutor";

            try{
                Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while(resultSet.next()){

                    // Obtengo datos del Tutor
                            int id_tutor = resultSet.getInt("id_tutor");
                            String dni = resultSet.getString("dni");
                            String nombre = resultSet.getString("nombre");
                            String apellido = resultSet.getString("apellido");
                            int edad = resultSet.getInt("edad");
                            int id_genero = resultSet.getInt("id_genero");
                            String ocupacion = resultSet.getString("ocupacion");
                            String pasatiempos = resultSet.getString("pasatiempos");
                            String info_adicional = resultSet.getString("info_adicional");

                    // Obtengo el Usuario relacionado usando el repositorio de Usuario
                            int id_usuario = resultSet.getInt("id_usuario");
                            Usuario usuario = usuarioRepository.getUsuarioPorId(id_usuario);

                    // Obtengo el Genero usando el repositorio de Genero
                            Genero genero = generoRepository.getGeneroPorId(id_genero);

                    // Crea objeto Tutor y asigna los valores
                            Tutor tutor = new Tutor(id_tutor, dni, nombre, apellido, edad, genero, ocupacion, pasatiempos, info_adicional, usuario);
                            tutores.add(tutor);
                }

                resultSet.close();
                statement.close();
                con.close();
            } catch (SQLException e){
                mainHandler.post(() -> callback.onFailure(e));
                return;
            }
            // Envia resultados a hilo principal
            mainHandler.post(() -> callback.onSuccess(tutores));
        });
    }

    // Registrar un nuevo Tutor
    public void registrarTutor(Tutor tutor, DataCallback<Boolean> callback){
        executor.execute(() -> {
            String query = "INSERT INTO tutor (dni, nombre, apellido, edad, id_genero, ocupacion, pasatiempos, info_adicional, id_usuario) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                // Setea parametros en la query
                preparedStatement.setString(1, tutor.getDni());
                preparedStatement.setString(2, tutor.getNombre());
                preparedStatement.setString(3, tutor.getApellido());
                preparedStatement.setInt(4, tutor.getEdad());
                preparedStatement.setInt(5, tutor.getIdGenero().getId_genero());
                preparedStatement.setString(6, tutor.getOcupacion());
                preparedStatement.setString(7, tutor.getPasatiempos());
                preparedStatement.setString(8, tutor.getInfoAdicional());
                preparedStatement.setInt(9, tutor.getId_usuario();

                // Ejecuta la consulta
                int rowsAffected = preparedStatement.executeUpdate();

                // Si la inserción es exitosa, obtenemos el ID generado del nuevo tutor
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            tutor.setIdTutor(generatedKeys.getInt(1)); // Seteamos el id del tutor recién creado
                        }
                    }
                }

                // Envia resultado a la interfaz de callback en el hilo principal
                mainHandler.post(() -> callback.onSuccess(true));

            } catch (SQLException e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }*/
}
