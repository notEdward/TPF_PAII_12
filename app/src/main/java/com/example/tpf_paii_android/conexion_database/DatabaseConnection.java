package com.example.tpf_paii_android.conexion_database;

public class DatabaseConnection {
    //datos conexion
    public static String host="sql10.freesqldatabase.com";
    public static String port="3306";
    public static String nameBD="sql10742357";
    public static String user="sql10742357";
    public static String pass="QxgHBp5xwg";

    public static String urlMySQL = "jdbc:mysql://" + host + ":" + port + "/"+nameBD;
    public static String driver = "com.mysql.jdbc.Driver";
}
