package com.example.tpf_paii_android.repositorios;
import android.os.Handler;
import android.os.Looper;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OfertaRepository {

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    public void getAllOfertas(DataCallback<ArrayList<OfertaEmpleo>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<OfertaEmpleo> ofertas = new ArrayList<>();
            String query = "SELECT * FROM ofertas_empleos";

            try {
                Class.forName(DatabaseConnection.driver);
                Connection connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    OfertaEmpleo oferta = new OfertaEmpleo(
                            resultSet.getInt("id_oferta_empleo"),
                            resultSet.getInt("id_empresa"),
                            resultSet.getString("titulo"),
                            resultSet.getString("descripcion"),
                            resultSet.getInt("id_tipo_empleo"),
                            resultSet.getInt("id_tipo_modalidad"),
                            resultSet.getInt("id_nivel_educativo"),
                            resultSet.getInt("id_curso"),
                            resultSet.getString("otros_requisitos"),
                            resultSet.getString("direccion"),
                            resultSet.getInt("id_provincia"),
                            resultSet.getInt("id_localidad")
                    );
                    ofertas.add(oferta);
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
                return;
            }

            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess((ArrayList<OfertaEmpleo>) ofertas));
        });
    }
}
