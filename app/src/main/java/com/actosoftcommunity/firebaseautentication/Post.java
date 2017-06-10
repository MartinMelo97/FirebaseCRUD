package com.actosoftcommunity.firebaseautentication;

/**
 * Created by martinmelo on 6/10/17.
 */

public class Post {

    private String titulo;
    private String contenido;
    private String autor;
    private String imagen;

    public Post()
    {

    }

    public Post(String titulo_2, String contenido_2, String autor_2, String imagen_2){
        this.titulo = titulo_2;
        this.contenido = contenido_2;
        this.autor = autor_2;
        this.imagen = imagen_2;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String title)
    {
        this.titulo = title;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


}
