package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.RepositorioUsuarios;
import io.javalin.http.Context;

import java.util.Objects;

public class UsuariosController {
  RepositorioUsuarios repositorioUsuarios;
  public UsuariosController() {
    this.repositorioUsuarios = RepositorioUsuarios.getInstancia();
  }

  public void crearUsuario(Context ctx) {
    String nombre = ctx.formParam("nombre");
    String apellido = ctx.formParam("apellido");
    String telefono = ctx.formParam("telefono");
    String mail = ctx.formParam("email");
    Integer edad = Integer.parseInt(Objects.requireNonNull(ctx.formParam("edad")));
    String password = ctx.formParam("password");

    Contribuyente usuarioCreado = new Contribuyente(nombre, apellido, telefono, mail, edad, password);

    repositorioUsuarios.guardar(usuarioCreado);
    ctx.redirect("/logueo");
  }

  public void mostrarRegistro(Context ctx) {
    ctx.render("register.hbs");
  }
}

