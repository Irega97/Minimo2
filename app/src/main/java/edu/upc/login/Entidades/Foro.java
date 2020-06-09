package edu.upc.login.Entidades;

public class Foro {

    public int idComment;
    public String nombre;
    public String comentario;
    public String fecha;

    public Foro(){}

    public Foro(int idComment, String nombre, String comentario, String fecha) {
        this();
        this.idComment = idComment;
        this.nombre = nombre;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Foro{" +
                "idComment=" + idComment +
                ", username='" + nombre + '\'' +
                ", comentario='" + comentario + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
