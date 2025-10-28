package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;

public class AdminController {

  public void mostrarAdmin(Context ctx) {
    ctx.render("admin.hbs");
  }

  public void mostarCrearColeccion(Context ctx){
    ctx.render("admin_colecciones_crear.hbs");
  }
}