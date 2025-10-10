package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.RepositorioUsuarios;
import io.javalin.http.Context;

public class UsuariosController {

    //RepositorioUsuarios repositorioUsuarios;

    public void crearUsuario(Context ctx) {
        String nombre = ctx.formParam("nombre");
        String apellido = ctx.formParam("apellido");
        Integer telefono = Integer.parseInt(ctx.formParam("telefono"));
        String mail = ctx.formParam("mail");
        Integer edad = Integer.parseInt(ctx.formParam("edad"));
        String password = ctx.formParam("password");

        Contribuyente usuarioCreado = new Contribuyente(nombre, apellido, telefono, mail, edad, password);


        // Aquí va la lógica para crear el usuario con esos datos

        ctx.redirect("/home");
    }

    public UsuariosController(){
        //this.repositorioUsuarios = RepositorioUsuarios.getInstancia();
    }
} //TODO: ROMPE CUANDO PONGO EL REPO DE USUARIOS, falta que guarde la entidad que acabo de crear en la base de datos

