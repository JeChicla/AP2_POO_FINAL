package org.example.Util;

import java.util.ArrayList;
import java.util.List;

public class Genero {
    private int id;
    private String nomeGenero;
    private List<Conteudo> conteudos;

    public Genero(String nomeGenero) {
        this.nomeGenero = nomeGenero;
        this.conteudos = new ArrayList<>();
    }

    public Genero(int id, String nomeGenero, List<Conteudo> conteudos) {
        this.id = id;
        this.nomeGenero = nomeGenero;
        this.conteudos = conteudos;
    }

    public Genero() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeGenero() {
        return nomeGenero;
    }

    public void setNomeGenero(String nomeGenero) {
        this.nomeGenero = nomeGenero;
    }

    public List<Conteudo> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<Conteudo> conteudos) {
        this.conteudos = conteudos;
    }

    public void adicionarConteudo(Conteudo conteudo){
        if (!conteudos.contains(conteudo)){
            conteudos.add(conteudo);
            System.out.println(conteudo.getTitulo() + " adicionado a: " + nomeGenero);
        }
    }

    public void listarConteudo() {
        System.out.print(nomeGenero + ":");
        for (Conteudo c : conteudos) {
            System.out.print(c.getTitulo() + "\n");
        }
    }
}