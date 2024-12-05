package com.example.tpf_paii_android.repositorios;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.EstadoNivelEducativo;
import com.example.tpf_paii_android.modelos.Estudiante;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Modalidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.OfertaDetalle;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.modelos.PostulacionItem;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.modelos.TipoEmpleo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OfertaRepository {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public interface DataCallback<T> {
        void onSuccess(T result);

        void onFailure(Exception e);
    }

    public void getAllOfertas(DataCallback<ArrayList<OfertaEmpleo>> callback) {
        executor.execute(() -> {
            List<OfertaEmpleo> ofertas = new ArrayList<>();
            String query = "SELECT oe.*, c.id_categoria FROM ofertas_empleos oe JOIN curso c ON oe.id_curso = c.id_curso WHERE oe.estado = 1";

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
                            resultSet.getInt("id_localidad"),
                            resultSet.getInt("id_categoria")
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

    //Filtros de las ofertas
    // obtener modalidades
    public void obtenerModalidades(DataCallback<List<Modalidad>> callback) {
        executor.execute(() -> {
            List<Modalidad> modalidades = new ArrayList<>();
            String query = "SELECT * FROM modalidad";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Modalidad modalidad = new Modalidad();
                    modalidad.setId_modalidad(resultSet.getInt("id_modalidad"));
                    modalidad.setDescripcion(resultSet.getString("descripcion"));
                    modalidades.add(modalidad);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(modalidades));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    //  obtener tipos de empleo
    public void obtenerTiposEmpleo(DataCallback<List<TipoEmpleo>> callback) {
        executor.execute(() -> {
            List<TipoEmpleo> tiposEmpleo = new ArrayList<>();
            String query = "SELECT * FROM tipo_empleo";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    TipoEmpleo tipoEmpleo = new TipoEmpleo();
                    tipoEmpleo.setId_tipoEmpleo(resultSet.getInt("id_tipo_empleo"));
                    tipoEmpleo.setDescripcion(resultSet.getString("descripcion"));
                    tiposEmpleo.add(tipoEmpleo);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(tiposEmpleo));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    // obtener cursos
    public void obtenerCursos(DataCallback<List<Curso>> callback) {
        executor.execute(() -> {
            List<Curso> cursos = new ArrayList<>();
            String query = "SELECT * FROM curso";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Curso curso = new Curso(
                            resultSet.getInt("id_curso"),
                            resultSet.getString("nombre_curso"),
                            resultSet.getString("descripcion"),
                            resultSet.getInt("id_categoria"),
                            resultSet.getString("respuestas_correctas"),
                            resultSet.getInt("estado")
                    );
                    cursos.add(curso);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(cursos));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    public void obtenerOfertasConFiltros(List<Integer> modalidadesSeleccionadas,
                                         List<Integer> tiposEmpleoSeleccionados,
                                         List<Integer> cursosSeleccionados,
                                         DataCallback<List<OfertaEmpleo>> callback) {
        executor.execute(() -> {
            List<OfertaEmpleo> ofertas = new ArrayList<>();

            StringBuilder queryBuilder = new StringBuilder(
                    "SELECT oe.*, c.id_categoria " +
                            "FROM ofertas_empleos oe " +
                            "LEFT JOIN curso c ON oe.id_curso = c.id_curso " +
                            "WHERE 1=1"
            );

            // Filtros dinámicos
            if (!modalidadesSeleccionadas.isEmpty()) {
                queryBuilder.append(" AND oe.id_tipo_modalidad IN (")
                        .append(TextUtils.join(",", modalidadesSeleccionadas))
                        .append(")");
            }

            if (!tiposEmpleoSeleccionados.isEmpty()) {
                queryBuilder.append(" AND oe.id_tipo_empleo IN (")
                        .append(TextUtils.join(",", tiposEmpleoSeleccionados))
                        .append(")");
            }

            if (!cursosSeleccionados.isEmpty()) {
                queryBuilder.append(" AND oe.id_curso IN (")
                        .append(TextUtils.join(",", cursosSeleccionados))
                        .append(")");
            }

            String query = queryBuilder.toString();

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL,
                    DatabaseConnection.user,
                    DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    OfertaEmpleo oferta = new OfertaEmpleo();
                    oferta.setId_ofertaEmpleo(resultSet.getInt("id_oferta_empleo"));
                    oferta.setId_empresa(resultSet.getInt("id_empresa"));
                    oferta.setTitulo(resultSet.getString("titulo"));
                    oferta.setDescripcion(resultSet.getString("descripcion"));
                    oferta.setId_tipoEmpleo(resultSet.getInt("id_tipo_empleo"));
                    oferta.setId_tipoModalidad(resultSet.getInt("id_tipo_modalidad"));
                    oferta.setId_nivelEducativo(resultSet.getInt("id_nivel_educativo"));
                    oferta.setId_curso(resultSet.getInt("id_curso"));
                    oferta.setOtrosRequisitos(resultSet.getString("otros_requisitos"));
                    oferta.setDireccion(resultSet.getString("direccion"));
                    oferta.setId_localidad(resultSet.getInt("id_localidad"));

                    oferta.setIdCategoria(resultSet.getInt("id_categoria"));

                    ofertas.add(oferta);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(ofertas));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    //fin filtros

    //Comienzo de detalle Oferta
    public void obtenerDetallesOferta(int idOfertaEmpleo, DataCallback<OfertaDetalle> callback) {
        executor.execute(() -> {
            OfertaDetalle detalle = null;
            String query = "SELECT o.titulo, o.descripcion, o.direccion, o.otros_requisitos, " +
                    "c.nombre_curso AS nombre_curso, " +
                    "l.nombre AS nombre_localidad, p.nombre AS nombre_provincia, " +
                    "m.descripcion AS descripcion_modalidad, " +
                    "ne.descripcion AS descipcion_nivelEducativo, " +
                    "te.descripcion AS descripcion_tipo_empleo, " +
                    "emp.nombre AS nombre_empresa " +
                    "FROM ofertas_empleos o " +
                    "JOIN empresa emp ON o.id_empresa = emp.id_empresa " +
                    "JOIN curso c ON o.id_curso = c.id_curso " +
                    "JOIN localidad l ON o.id_localidad = l.id_localidad " +
                    "JOIN provincia p ON l.id_provincia = p.id_provincia " +
                    "JOIN modalidad m ON o.id_tipo_modalidad = m.id_modalidad " +
                    "JOIN nivel_educativo ne ON o.id_nivel_educativo = ne.id_nivel_educativo " +
                    "JOIN tipo_empleo te ON o.id_tipo_empleo = te.id_tipo_empleo " +
                    "WHERE o.id_oferta_empleo = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement stmt = con.prepareStatement(query)) {

                stmt.setInt(1, idOfertaEmpleo);  // Establece el id de la oferta a buscar
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        // Crea un objeto OfertaDetalle con los resultados obtenidos
                        detalle = new OfertaDetalle(
                                idOfertaEmpleo,  // Asigna el ID de la oferta
                                resultSet.getString("nombre_empresa"),
                                resultSet.getString("titulo"),
                                resultSet.getString("descripcion"),
                                resultSet.getString("direccion"),
                                resultSet.getString("nombre_curso"),  // Esto es el nombre del curso, pasamos aquí
                                resultSet.getString("nombre_localidad") + ", " + resultSet.getString("nombre_provincia"),
                                resultSet.getString("nombre_provincia"),  // O el valor que consideres para provincia
                                resultSet.getString("descripcion_modalidad"),
                                resultSet.getString("descripcion_tipo_empleo"),
                                resultSet.getString("descipcion_nivelEducativo"),
                                resultSet.getString("otros_requisitos")
                        );
                    }
                }

                // Enviar los detalles obtenidos en el hilo principal
                OfertaDetalle finalDetalle = detalle;
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (finalDetalle != null) {
                        callback.onSuccess(finalDetalle);
                    } else {
                        callback.onFailure(new Exception("No se encontraron detalles para la oferta."));
                    }
                });

            } catch (Exception e) {
                // Manejo de excepciones y error en la ejecución
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

////fin detalle oferta
//postulaciones
public void registrarPostulacion(int idOfertaEmpleo, int idUsuario, DataCallback<String> callback) {
    executor.execute(() -> {
        String mensaje;
        String consultaVerificarPostulacion = "SELECT * FROM postulaciones WHERE id_oferta_empleo = ? AND id_usuario = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
             PreparedStatement stmtVerificarPostulacion = con.prepareStatement(consultaVerificarPostulacion)) {

            stmtVerificarPostulacion.setInt(1, idOfertaEmpleo);
            stmtVerificarPostulacion.setInt(2, idUsuario);
            ResultSet rsVerificacion = stmtVerificarPostulacion.executeQuery();

            if (rsVerificacion.next()) {
                mensaje = "Ya se ha postulado a esta oferta.";
            } else {
                String consultaElegibilidad = "SELECT * FROM inscripciones i " +
                        "JOIN evaluaciones e ON i.id_inscripcion = e.id_inscripcion " +
                        "WHERE i.id_usuario = ? AND i.id_curso = " +
                        "(SELECT id_curso FROM ofertas_empleos WHERE id_oferta_empleo = ?) " +
                        "AND i.estado_inscripcion = 'finalizado' AND e.nota_obtenida >= 6";

                try (PreparedStatement stmtElegibilidad = con.prepareStatement(consultaElegibilidad)) {

                    stmtElegibilidad.setInt(1, idUsuario);
                    stmtElegibilidad.setInt(2, idOfertaEmpleo);
                    ResultSet rsElegibilidad = stmtElegibilidad.executeQuery();

                    if (rsElegibilidad.next()) {
                        // Insertar la postulación si cumple con los requisitos
                        String consultaPostulacion = "INSERT INTO postulaciones (id_oferta_empleo, id_usuario, estado_postulacion, fecha_postulacion) VALUES (?, ?, 'pendiente', ?)";

                        try (PreparedStatement stmtPostulacion = con.prepareStatement(consultaPostulacion)) {
                            stmtPostulacion.setInt(1, idOfertaEmpleo);
                            stmtPostulacion.setInt(2, idUsuario);
                            stmtPostulacion.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Fecha actual
                            stmtPostulacion.executeUpdate();
                            mensaje = "Postulación realizada con éxito.";
                        }
                    } else {
                        mensaje = "El usuario no cumple con los requisitos para postularse.";
                    }

                    rsElegibilidad.close();
                }
            }

            rsVerificacion.close();

            String finalMensaje = mensaje;
            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(finalMensaje));

        } catch (Exception e) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
        }
    });
}
//empresa alta
public void obtenerNivelesEducativos(DataCallback<List<NivelEducativo>> callback) {
    executor.execute(() -> {
        List<NivelEducativo> nivelesEducativos = new ArrayList<>();
        String query = "SELECT * FROM nivel_educativo";

        try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                NivelEducativo nivel = new NivelEducativo();
                nivel.setId_nivelEducativo(resultSet.getInt("id_nivel_educativo"));
                nivel.setDescripcion(resultSet.getString("descripcion"));
                nivelesEducativos.add(nivel);
            }

            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(nivelesEducativos));

        } catch (Exception e) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
        }
    });
}
    public void obtenerLocalidades(DataCallback<List<Localidad>> callback) {
        executor.execute(() -> {
            List<Localidad> localidades = new ArrayList<>();
            String query = "SELECT * FROM localidad";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Localidad localidad = new Localidad();
                    localidad.setId_localidad(resultSet.getInt("id_localidad"));
                    localidad.setNombre(resultSet.getString("nombre"));
                    localidad.setId_provincia(resultSet.getInt("id_provincia"));
                    localidades.add(localidad);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(localidades));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    public void obtenerProvincias(DataCallback<List<Provincia>> callback) {
        executor.execute(() -> {
            List<Provincia> provincias = new ArrayList<>();
            String query = "SELECT * FROM provincia";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Provincia provincia = new Provincia();
                    provincia.setId_provincia(resultSet.getInt("id_provincia"));
                    provincia.setNombre(resultSet.getString("nombre"));
                    provincias.add(provincia);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(provincias));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }
    public void obtenerLocalidadesPorProvincia(int idProvincia, DataCallback<List<Localidad>> callback) {
        executor.execute(() -> {
            List<Localidad> localidades = new ArrayList<>();
            String query = "SELECT * FROM localidad WHERE id_provincia = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement preparedStatement = con.prepareStatement(query)) {

                preparedStatement.setInt(1, idProvincia);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Localidad localidad = new Localidad();
                        localidad.setId_localidad(resultSet.getInt("id_localidad"));
                        localidad.setNombre(resultSet.getString("nombre"));
                        localidad.setId_provincia(resultSet.getInt("id_provincia"));
                        localidades.add(localidad);
                    }

                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(localidades));

                }

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    public void guardarOferta(OfertaEmpleo oferta, DataCallback<String> callback) {
        executor.execute(() -> {
            String query = "INSERT INTO ofertas_empleos (id_empresa, titulo, descripcion, id_tipo_empleo, id_tipo_modalidad, id_nivel_educativo, id_curso, otros_requisitos, direccion, id_localidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement stmt = con.prepareStatement(query)) {

                stmt.setInt(1, oferta.getIdEmpresa());  // Cambié de setString a setInt para los IDs
                stmt.setString(2, oferta.getTitulo());
                stmt.setString(3, oferta.getDescripcion());
                stmt.setInt(4, oferta.getIdTipoEmpleo());
                stmt.setInt(5, oferta.getIdModalidad());
                stmt.setInt(6, oferta.getIdNivelEducativo());
                stmt.setInt(7, oferta.getIdCurso());
                stmt.setString(8, oferta.getOtrosRequisitos());
                stmt.setString(9, oferta.getDireccion());
                stmt.setInt(10, oferta.getIdLocalidad());

                int result = stmt.executeUpdate();
                if (result > 0) {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess("Oferta guardada exitosamente"));
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(new Exception("Error al guardar la oferta")));
                }
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    // Método para obtener el ID de la empresa por el ID de usuario
    public void obtenerIdEmpresaPorUsuario(int idUsuario, DataCallback<Integer> callback) {
        executor.execute(() -> {
            String query = "SELECT id_empresa FROM empresa WHERE id_usuario = ?";
            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement stmt = con.prepareStatement(query)) {

                stmt.setInt(1, idUsuario);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        int idEmpresa = resultSet.getInt("id_empresa");
                        new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(idEmpresa));
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(new Exception("No se encontró la empresa para el usuario")));
                    }
                }
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    public void actualizarOferta(OfertaEmpleo oferta, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try (Connection connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass)) {
                // Código de conexión y actualización a la base de datos
                String updateQuery = "UPDATE ofertas_empleos SET titulo = ?, descripcion = ? WHERE id_oferta_empleo = ?";
                PreparedStatement statement = connection.prepareStatement(updateQuery);
                statement.setString(1, oferta.getTitulo());
                statement.setString(2, oferta.getDescripcion());
                statement.setInt(3, oferta.getId_ofertaEmpleo());

                int rowsAffected = statement.executeUpdate();
                statement.close();

                // Llama al callback en el hilo principal si se actualizó correctamente
                boolean exito = rowsAffected > 0;
                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(exito));

            } catch (Exception e) {
                // En caso de error, envía la excepción al callback de falla
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    //baja
    public void actualizarEstadoOferta(int idOferta, int nuevoEstado, OfertaRepository.DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Class.forName(DatabaseConnection.driver);
                Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);

                String queryOferta = "UPDATE ofertas_empleos SET estado = ? WHERE id_oferta_empleo = ?";
                PreparedStatement statementOferta = con.prepareStatement(queryOferta);
                statementOferta.setInt(1, nuevoEstado);
                statementOferta.setInt(2, idOferta);

                int rowsAffectedOferta = statementOferta.executeUpdate();
                statementOferta.close();

                String queryPostulaciones = "UPDATE postulaciones SET estado_postulacion = 'Finalizado' WHERE id_oferta_empleo = ? AND LOWER(estado_postulacion) = 'pendiente'";
                PreparedStatement statementPostulaciones = con.prepareStatement(queryPostulaciones);
                statementPostulaciones.setInt(1, idOferta);

                int rowsAffectedPostulaciones = statementPostulaciones.executeUpdate();
                statementPostulaciones.close();

                con.close();

                boolean success = rowsAffectedOferta > 0 || rowsAffectedPostulaciones > 0;
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onSuccess(success);
                });
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }
//    public void actualizarEstadoOferta(int idOferta, int nuevoEstado, OfertaRepository.DataCallback<Boolean> callback) {
//        executor.execute(() -> {
//            try {
//                Class.forName(DatabaseConnection.driver);
//                Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
//
//                // estado del curso a 0 (inactivo)
//                String query = "UPDATE ofertas_empleos SET estado = ? WHERE id_oferta_empleo = ?";
//                PreparedStatement statement = con.prepareStatement(query);
//                statement.setInt(1, nuevoEstado);
//                statement.setInt(2, idOferta);
//
//                int rowsAffected = statement.executeUpdate();
//                statement.close();
//                con.close();
//
//                new Handler(Looper.getMainLooper()).post(() -> {
//                    callback.onSuccess(rowsAffected > 0);
//                });
//            } catch (Exception e) {
//                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
//            }
//        });
//    }

    //Seccion postulaciones
    public void obtenerPostulacionesEstudiante(int idUsuario, OfertaRepository.DataCallback<List<PostulacionItem>> callback) {
        executor.execute(() -> {
            List<PostulacionItem> postulaciones = new ArrayList<>();
            String query = "SELECT oe.titulo, p.fecha_postulacion, p.estado_postulacion " +
                    "FROM postulaciones p " +
                    "INNER JOIN ofertas_empleos oe ON p.id_oferta_empleo = oe.id_oferta_empleo " +
                    "WHERE p.id_usuario = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    PostulacionItem item = new PostulacionItem();
                    item.setTitulo(rs.getString("titulo"));
                    item.setFechaPostulacion(rs.getDate("fecha_postulacion"));
                    item.setEstadoPostulacion(rs.getString("estado_postulacion"));
                    postulaciones.add(item);
                }
                callback.onSuccess(postulaciones);
            } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }

//    public void obtenerPostulacionesEmpresa(int idEmpresa, OfertaRepository.DataCallback<List<PostulacionItem>> callback) {
//        executor.execute(() -> {
//            List<PostulacionItem> postulaciones = new ArrayList<>();
//            String query = "SELECT oe.titulo, u.nombre_usuario, p.fecha_postulacion " +
//                    "FROM postulaciones p " +
//                    "INNER JOIN ofertas_empleos oe ON p.id_oferta_empleo = oe.id_oferta_empleo " +
//                    "INNER JOIN usuario u ON p.id_usuario = u.id_usuario " +
//                    "WHERE oe.id_empresa = ?";
//
//            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
//                 PreparedStatement ps = con.prepareStatement(query)) {
//                ps.setInt(1, idEmpresa);
//                ResultSet rs = ps.executeQuery();
//
//                while (rs.next()) {
//                    PostulacionItem item = new PostulacionItem();
//                    item.setTitulo(rs.getString("titulo"));
//                    item.setNombreUsuario(rs.getString("nombre_usuario"));
//                    item.setFechaPostulacion(rs.getDate("fecha_postulacion"));
//                    postulaciones.add(item);
//                }
//                callback.onSuccess(postulaciones);
//            } catch (Exception e) {
//                callback.onFailure(e);
//            }
//        });
//    }

    public void obtenerPostulacionesEmpresa(int idEmpresa, OfertaRepository.DataCallback<List<PostulacionItem>> callback) {
        executor.execute(() -> {
            List<PostulacionItem> postulaciones = new ArrayList<>();
            String query = "SELECT p.id_postulacion, oe.titulo, u.nombre_usuario, p.fecha_postulacion, p.estado_postulacion, p.id_usuario " +
                    "FROM postulaciones p " +
                    "INNER JOIN ofertas_empleos oe ON p.id_oferta_empleo = oe.id_oferta_empleo " +
                    "INNER JOIN usuario u ON p.id_usuario = u.id_usuario " +
                    "WHERE oe.id_empresa = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, idEmpresa);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    PostulacionItem item = new PostulacionItem();
                    item.setIdPostulacion(rs.getInt("id_postulacion"));
                    item.setTitulo(rs.getString("titulo"));
                    item.setNombreUsuario(rs.getString("nombre_usuario"));
                    item.setFechaPostulacion(rs.getDate("fecha_postulacion"));
                    item.setEstadoPostulacion(rs.getString("estado_postulacion"));
                    item.setIdUsuario(rs.getInt("id_usuario"));
                    postulaciones.add(item);
                }
                callback.onSuccess(postulaciones);
            } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void obtenerDetalleEstudiante(int idUsuario, DataCallback<Estudiante> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String query = "SELECT e.*, n.id_nivel_educativo, n.descripcion AS nivelEducativo, en.id_estado_nivel, en.descripcion AS estadoNivel, " +
                    "l.id_localidad, l.nombre AS localidad, p.id_provincia, p.nombre AS provincia " +
                    "FROM estudiante e " +
                    "JOIN nivel_educativo n ON e.id_nivel_educativo = n.id_nivel_educativo " +
                    "JOIN estado_nivel en ON e.id_estado_nivel = en.id_estado_nivel " +
                    "JOIN localidad l ON e.id_localidad = l.id_localidad " +
                    "JOIN provincia p ON l.id_provincia = p.id_provincia " +
                    "WHERE e.id_usuario = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setNombre(rs.getString("nombre"));
                    estudiante.setApellido(rs.getString("apellido"));
                    estudiante.setDni(rs.getString("dni"));
                    estudiante.setEmail(rs.getString("email"));
                    estudiante.setTelefono(rs.getString("telefono"));
                    estudiante.setDireccion(rs.getString("direccion"));
                    //obejo nivel educativo
                    int idNivelEducativo = rs.getInt("id_nivel_educativo");
                    String descripcionNivelEducativo = rs.getString("nivelEducativo");
                    NivelEducativo nivelEducativo = new NivelEducativo(idNivelEducativo, descripcionNivelEducativo);
                    estudiante.setNivelEducativo(nivelEducativo);

                    // objeto estadonivel
                    int idEstadoNivel = rs.getInt("id_estado_nivel");
                    String descripcionEstadoNivel = rs.getString("estadoNivel");
                    EstadoNivelEducativo estadoNivelEducativo = new EstadoNivelEducativo(idEstadoNivel, descripcionEstadoNivel);
                    estudiante.setEstadoNivelEducativo(estadoNivelEducativo);

                    // obj local
                    int idLocalidad = rs.getInt("id_localidad");
                    String nombreLocalidad = rs.getString("localidad");
                    Localidad localidad = new Localidad(idLocalidad, nombreLocalidad);

                    // obj prov
                    int idProvincia = rs.getInt("id_provincia");
                    String nombreProvincia = rs.getString("provincia");
                    Provincia provincia = new Provincia(idProvincia, nombreProvincia);
                    localidad.setId_provincia(provincia);

                    estudiante.setLocalidad(localidad);

                    callback.onSuccess(estudiante);
                } else {
                    callback.onSuccess(null);
                }
            } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void actualizarEstadoPostulacion(int idPostulacion, String nuevoEstado, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            String query = "UPDATE postulaciones SET estado_postulacion = ? WHERE id_postulacion = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, nuevoEstado);
                ps.setInt(2, idPostulacion);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    callback.onSuccess(true);
                } else {
                    callback.onSuccess(false);
                }
            } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }
    public void shutdownExecutor() {
        executor.shutdown();
    }

}












