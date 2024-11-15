package com.example.tpf_paii_android.utilidades;

public class TipoUsuarioUtils {
    public static String getTipoUsuario(int tipoUsuario) {
        switch (tipoUsuario) {
            case 1:
                return "Estudiante";
            case 2:
                return "Empresa";
            case 3:
                return "Tutor";
            case 4:
                return "Admin";
            default:
                return "";
        }
    }
}
