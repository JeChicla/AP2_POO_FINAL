package org.example.Util;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Usuario {
    private int id;
    private String username;
    private List<Conteudo> conteudoAdicionado;
    private Collection<Review> reviews = new ArrayList<>();
    private List<Conteudo> favoritos;
    private List<Usuario> seguidores;
    private List<Usuario> seguindo;

    public Usuario(String username) {
        this.username = username;
        this.conteudoAdicionado = new ArrayList<>();
        this.favoritos = new ArrayList<>();
        this.seguidores = new ArrayList<>();
        this.seguindo = new ArrayList<>();
    }

    public Usuario(int id, String username, List<Conteudo> conteudoAdicionado, Collection<Review> reviews, List<Conteudo> favoritos, List<Usuario> seguidores, List<Usuario> seguindo) {
        this.id = id;
        this.username = username;
        this.conteudoAdicionado = conteudoAdicionado;
        this.reviews = reviews;
        this.favoritos = favoritos;
        this.seguidores = seguidores;
        this.seguindo = seguindo;
    }

    public Usuario() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Conteudo> getConteudoAdicionado() {
        return conteudoAdicionado;
    }

    public void setConteudoAdicionado(List<Conteudo> conteudoAdicionado) {
        this.conteudoAdicionado = conteudoAdicionado;
    }

    public Collection<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Collection<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Conteudo> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Conteudo> favoritos) {
        this.favoritos = favoritos;
    }

    public List<Usuario> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<Usuario> seguidores) {
        this.seguidores = seguidores;
    }

    public List<Usuario> getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(List<Usuario> seguindo) {
        this.seguindo = seguindo;
    }

    public void exibirReviewsUsuario() {
        System.out.println("reviews:");
        for (Review r : reviews){
            System.out.println("Titulo: "+ r.getTitulo());
            System.out.println("Nota: " + r.getNota());
            System.out.println("Comentario: " + r.getComentario());
        }
    }

    public void adicionarFavorito(Conteudo conteudo) {
        if (!favoritos.contains(conteudo)) {
            favoritos.add(conteudo);
        }
    }
    public void listarFavoritos() {
        System.out.println("Favoritos");
        for (Conteudo c : favoritos) {
            c.exibirInfo();
            System.out.println(c.getTitulo());
        }
    }

    public void seguirUsuario(Usuario u) {
        if (!seguindo.contains(u)) {
            seguindo.add(u);
            u.seguidores.add(this);
            System.out.print("seguindo: " + u.getUsername() + "\n");
        }
        else {
            System.out.println("Operação invalida");
        }
    }

    public void listarSeguidores() {
        System.out.print("Seguidores: ");
        for (Usuario u : seguidores) {
            System.out.print(u.getUsername() + "\n");
            }
    }

    public void listarSeguindo() {
        System.out.println("Seguindo:");
        for (Usuario u : seguindo) {
            System.out.println(u.getUsername() + "\n");
        }
    }

    public void adicionarReview(Review r){
        if (!reviews.contains(r)){
            reviews.add(r);
            System.out.println("Review adicionada.");
        }

    }


}
