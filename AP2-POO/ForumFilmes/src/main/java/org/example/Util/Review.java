package org.example.Util;

import java.util.ArrayList;
import java.util.Collection;

public class Review {
    private int id;
    private Usuario autor;
    private Conteudo conteudo;
    private float nota;
    private String titulo;
    private String comentario;
    private static Collection<Review> colecaoReviews = new ArrayList<>();

    public Review(Usuario autor, Conteudo conteudo,
                  float nota, String titulo, String comentario) {
        this.autor = autor;
        this.conteudo = conteudo;
        this.nota = nota;
        this.titulo = titulo;
        this.comentario = comentario;
        colecaoReviews.add(this);

    }

    public Review(int id, Usuario autor, Conteudo conteudo, float nota, String titulo, String comentario) {
        this.id = id;
        this.autor = autor;
        this.conteudo = conteudo;
        this.nota = nota;
        this.titulo = titulo;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public void setConteudo(Conteudo conteudo) {
        this.conteudo = conteudo;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void exibirReviews() {
        System.out.println("Autor: " + autor.getUsername());
        System.out.println("Título: " + titulo);
        System.out.println("Nota: " + nota);
        System.out.println("Comentário: " + comentario);
        System.out.println("Conteúdo: " + conteudo.getTitulo());
    }

    public static void exibirTodasReviews() {
        for (Review r : colecaoReviews) {
            r.exibirReviews();
        }
    }
}
