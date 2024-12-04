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
import com.example.tpf_paii_android.modelos.Localidad;
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

public class EmpresaRepository {

    private final Context context;
    private final ExecutorService executor;

    public EmpresaRepository(Context context){
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
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

public LiveData<Boolean> registrarEmpresa(Empresa empresa, int idUsuario) {
    MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(() -> {
        boolean registrada = false;
        String queryEmpresa = "INSERT INTO empresa (nombre, descripcion, sector, n_identificacion_fiscal, " +
                "direccion, id_localidad, email, telefono, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
             PreparedStatement ps = con.prepareStatement(queryEmpresa)) {

            ps.setString(1, empresa.getNombre());
            ps.setString(2, empresa.getDescripcion());
            ps.setString(3, empresa.getSector());
            ps.setString(4, empresa.getN_identificacionFiscal());
            ps.setString(5, empresa.getDireccion());
            ps.setInt(6, empresa.getId_localidad());
            ps.setString(7, empresa.getEmail());
            ps.setString(8, empresa.getTelefono());
            ps.setInt(9, idUsuario);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0)  registrada = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        resultLiveData.postValue(registrada);
    });
    executor.shutdown();
    return resultLiveData;
  }

    public Empresa obtenerEmpresa(int idEmpresa) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Empresa> futureResult = executor.submit(() -> {
            String query = "SELECT nombre, descripcion, sector, n_identificacion_fiscal, direccion, id_localidad, email, telefono FROM empresa WHERE id_empresa = ?";
            Empresa empresa = null;

            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = cn.prepareStatement(query)) {
                ps.setInt(1, idEmpresa);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        empresa = new Empresa(
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getString("sector"),
                                rs.getString("email"),
                                rs.getString("telefono"),
                                rs.getString("direccion"),
                                rs.getInt("id_localidad")
                        );
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al obtener la empresa: " + e.getMessage());
                e.printStackTrace();
            }
            return empresa;
        });
        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
        }
    }


    public boolean modificarEmpresa(Empresa empresa) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> futureResult = executor.submit(() -> {
            String query = "UPDATE empresa SET nombre = ?, descripcion = ?, sector = ?, email = ?, telefono = ?," +
                            " direccion = ?, id_localidad = ? WHERE id_empresa = ?";


            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = cn.prepareStatement(query)) {

                ps.setString(1, empresa.getNombre());
                ps.setString(2, empresa.getDescripcion());
                ps.setString(3, empresa.getSector());
                ps.setString(4, empresa.getEmail());
                ps.setString(5, empresa.getTelefono());
                ps.setString(6, empresa.getDireccion());
                ps.setInt(7, empresa.getId_localidad());
                ps.setInt(8, empresa.getId_empresa());

                // Ejecutar la actualización
                int filasAfectadas = ps.executeUpdate();
                return filasAfectadas > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
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













    public Integer obtenerIdProvinciaPorLocalidad(int idLocalidad) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> futureResult = executor.submit(() -> {
            String query = "SELECT prov.id_provincia FROM provincia prov " +
                            "INNER JOIN localidad loc ON loc.id_provincia = prov.id_provincia" +
                            "WHERE loc.id_localidad = ?";
            Integer idProvincia = null;

            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = cn.prepareStatement(query)) {
                ps.setInt(1, idLocalidad);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idProvincia = rs.getInt("id_provincia");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return idProvincia;
        });

        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
        }
    }







}






