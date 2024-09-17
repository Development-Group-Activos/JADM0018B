package co.com.activos.jadm0018.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import co.com.activos.jadm0018.model.Usuario;
import co.com.activos.jadm0018.model.DominioSAAS;
import co.com.activos.jadm0018.controller.Ldap;
import co.com.activos.jadm0018.controller.View;
import java.util.List;

/**
 *
 * @author kpaz
 */
public class ReloadPasswordService {


    // Guardar la solicitud de restablecimiento de contraseña
    public String createPassword(String userName) {
        return Ldap.saveRequest(userName);
    }

    // Validar si la solicitud de restablecimiento es válida
    public String validatePassword(String requestId) {
        return Ldap.reloadPass(requestId);
    }

    // Actualizar la contraseña del usuario
    public boolean updatePassword(String userName, String newPassword, String domain) {
        List<DominioSAAS> dominios = View.loadDominios(Long.parseLong("1"));
        DominioSAAS selectedDomain = null;

        for (DominioSAAS x : dominios) {
            if (domain.equals(x.getDsa_ramaldap())) {
                selectedDomain = x;
                break;
            }
        }
        if (selectedDomain != null) {
            return Ldap.updateState(userName).isEmpty();
        }
        return false;
    }
  
    
    // Método para capturar el email de usuario
    public String findPropertyByAccountName2(String userName, String property) {
        return Ldap.findPropertyByAccountName2(userName, property);
    }

    // Método para enviar notificación por correo
    public void sendPasswordResetEmail(String userEmail, String userName, String requestUrl) {//requestUrl es la url para que se envia dede el front para acceder a la ventana de ingresar nueva password
        //String template = "<html> ... </html>"; // Construcción de plantilla de email
        String template = loadEmailTemplate("templates/reset_password_template.html");

        if (template != null) {
            // Para reemplazar los marcadores de posición en la plantilla
            String emailContent = template.replace("{userName}", userName)
                    .replace("{requestUrl}", requestUrl);

            // Enviar correo
            Ldap.sendMail("notificacion@activos.com.co", userEmail, "Reestablecer Clave BMX ACTIVOS S.A.", emailContent);
        } else {
            System.out.println("Error: No se pudo cargar la plantilla de correo.");
        }
    }

    // Método para cargar la plantilla de correo desde el classpath
    private String loadEmailTemplate(String filePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(filePath), StandardCharsets.UTF_8))) {

            // Leer todo el contenido del archivo
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
