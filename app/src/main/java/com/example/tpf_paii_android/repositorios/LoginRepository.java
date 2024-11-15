package com.example.tpf_paii_android.repositorios;

import android.os.Handler;
import android.os.Looper;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.UsuarioLogin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LoginRepository {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    // Método para autenticar usuario y obtener sus datos
    public void authenticateUser(String username, String password, DataCallback<UsuarioLogin> callback) {
        executor.execute(() -> {
            final UsuarioLogin[] usuarioLogin = new UsuarioLogin[1];  // Usamos un array para que sea "final" (por la naturaleza de las lambdas)

            String query = "SELECT id_usuario, nombre_usuario, id_tipo_usuario FROM usuario WHERE nombre_usuario = ? AND contrasena = ?";

            try {
                // Conexión a la base de datos
                Class.forName(DatabaseConnection.driver);
                Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, username);  // Establece el valor del parámetro username
                preparedStatement.setString(2, password);  // Establece el valor del parámetro password

                ResultSet resultSet = preparedStatement.executeQuery();

                // Si el usuario existe y las credenciales son correctas
                if (resultSet.next()) {
                    usuarioLogin[0] = new UsuarioLogin(
                            resultSet.getInt("id_usuario"),
                            resultSet.getString("nombre_usuario"),
                            resultSet.getInt("id_tipo_usuario")
                    );
                }

                resultSet.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
                return;
            }

            // Devolver el resultado a través del callback
            if (usuarioLogin[0] != null) {
                mainHandler.post(() -> callback.onSuccess(usuarioLogin[0]));
            } else {
                mainHandler.post(() -> callback.onFailure(new Exception("Credenciales incorrectas")));
            }
        });
    }


}
