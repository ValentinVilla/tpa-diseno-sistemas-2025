package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import io.javalin.http.Context;

public class UsuariosController {
    public void crearUsuario(Context ctx) {
        String nombre = ctx.formParam("nombre");
        String apellido = ctx.formParam("apellido");
        Integer telefono = Integer.parseInt(ctx.formParam("telefono"));
        String mail = ctx.formParam("mail");
        Integer edad = Integer.parseInt(ctx.formParam("edad"));

        Contribuyente usuarioCreado = new Contribuyente(nombre, apellido, telefono, mail, edad);

        // Aquí va la lógica para crear el usuario con esos datos

        ctx.redirect("/usuario-creado");
    }
}

