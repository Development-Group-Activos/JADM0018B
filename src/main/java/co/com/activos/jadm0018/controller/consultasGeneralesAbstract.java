package co.com.activos.jadm0018.controller;

import co.com.activos.jadm0018.interfaces.consutasGeneralesInterface;
import co.com.activos.jadm0018.interfaces.consultasGenerales;
import java.util.Map;

/**
 *
 * @author kevrodriguez
 */
public abstract class consultasGeneralesAbstract {

    public static Map pb_rutas_credenciales_ldap() {
        consutasGeneralesInterface consultaGeneral = new consultasGenerales();
        Map key;
        key = consultaGeneral.pb_consulta_properties("C_LDAP");
        return key;
    }
}
