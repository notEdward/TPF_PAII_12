package com.example.tpf_paii_android.repositorios;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UsuarioRepository {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private Context context;

    public UsuarioRepository(Context context){
        this.context = context;
    }

    public UsuarioRepository() {
    }

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    public boolean modificarContrasena(String nombreUsuario, String nuevaContrasena) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> futureResult = executor.submit(() -> {
            String query = "UPDATE usuario SET contrasena = ? WHERE nombre_usuario = ?";
            boolean actualizado = false;

            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = cn.prepareStatement(query)) {

                ps.setString(1, nuevaContrasena);
                ps.setString(2, nombreUsuario);

                int filasAfectadas = ps.executeUpdate();
                actualizado = filasAfectadas > 0; // Si se afectó al menos una fila, la contraseña se actualizó correctamente.

            } catch (SQLException e) {
                System.err.println("Error al modificar la contraseña: " + e.getMessage());
                e.printStackTrace();
            }

            return actualizado;
        });

        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false; // Error al intentar modificar la contraseña
        } finally {
            executor.shutdown();
        }
    }


    public boolean verificarContrasenaActual(String nombreUsuario, String contrasenaActual) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> futureResult = executor.submit(() -> {
            String query = "SELECT contrasena FROM usuario WHERE nombre_usuario = ?";
            boolean coincide = false;

            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = cn.prepareStatement(query)) {

                ps.setString(1, nombreUsuario);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String contrasenaAlmacenada = rs.getString("contrasena");
                        coincide = contrasenaAlmacenada.equals(contrasenaActual); // Comparación de contraseñas
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al verificar la contraseña actual: " + e.getMessage());
                e.printStackTrace();
            }

            return coincide;
        });

        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false; // En caso de error, asumimos que no coincide
        } finally {
            executor.shutdown();
        }
    }


    //Simulacion Asincronica
   public Integer registrarUsuario(Usuario user) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> futureResult = executor.submit(() -> {
            String query = "INSERT INTO usuario (nombre_usuario, contrasena, id_tipo_usuario) VALUES (?, ?, ?)";
            int idGenerado = -1; // en caso de error

            // ver si existe
            if (!existeUsuario(user.getNombreUsuario())) {
                try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                     PreparedStatement ps = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, user.getNombreUsuario());
                    ps.setString(2, user.getContrasenia());
                    ps.setInt(3, user.getIdTipoUsuario());

                    // vemos el id para dps ver el tema de las fk
                    int filasAfectadas = ps.executeUpdate();
                    if (filasAfectadas > 0) {
                        ResultSet generatedKeys = ps.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            idGenerado = generatedKeys.getInt(1); // el id q se creo
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error al registrar el usuario: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("El usuario ya existe en la base de datos.");
                return 0;
            }
            return idGenerado;
        });

        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1; //  -1  error
        } finally {
            executor.shutdown();
        }
    }


    public Boolean existeUsuario(String nombreUser) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> futureResult = executor.submit(() -> {
            String query = "SELECT * FROM usuario WHERE nombre_usuario = ?";
            boolean existe = false;

            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = cn.prepareStatement(query)) {

                ps.setString(1, nombreUser);
                ResultSet rs = ps.executeQuery();
                existe = rs.next();
                rs.close();

            } catch (SQLException e) {
                System.err.println("Error al verificar la existencia del usuario: " + e.getMessage());
                e.printStackTrace();
            }
            return existe;
        });

        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        } finally {
            executor.shutdown();
        }
    }

    public void registrarUsuario(Usuario usuario, int tipoUsuario, Connection con, DataCallback<Integer> callback) {
        executor.execute(() -> {
            String query = "INSERT INTO usuario (nombre_usuario, contrasena, id_tipo_usuario) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                // Verifica si el usuario ya existe
                if (existeUsuario(usuario.getNombreUsuario())) {
                    mainHandler.post(() -> {
                        callback.onFailure(new SQLException("El nombre de usuario ya existe."));
                        Toast.makeText(context, "El nombre de usuario ya existe.", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                // Parametros para insertar
                preparedStatement.setString(1, usuario.getNombreUsuario());
                preparedStatement.setString(2, usuario.getContrasenia());
                preparedStatement.setInt(3, tipoUsuario);

                // Ejecuto consulta de INSERT
                int filasAfectadas = preparedStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    // Obtener el id_usuario generado automaticamente
                    try (ResultSet generateKeys = preparedStatement.getGeneratedKeys()) {
                        if (generateKeys.next()) {
                            int id_usuario = generateKeys.getInt(1);
                            mainHandler.post(() -> callback.onSuccess(id_usuario));
                        }
                    }
                } else {
                    mainHandler.post(() -> callback.onFailure(new SQLException("Error al registrar usuario")));
                }
            } catch (SQLException e) {
                mainHandler.post(() -> callback.onFailure(e));
                e.printStackTrace();
            }
        });
    }

}
