package org.example.Util;

import java.util.List;

public class Serie extends Conteudo{
    private int episodios;

    public Serie(String titulo, int lancamento, String diretor, int classificacao, int episodios) {
        super(titulo, lancamento, diretor, classificacao);
        this.episodios = episodios;
    }

    public Serie(int id, String titulo, int lancamento, List<Genero> generos, String diretor, int classificacao, int episodios) {
        super(id, titulo, lancamento, generos, diretor, classificacao);
        this.episodios = episodios;
    }

    public Serie() {

    }

    public int getEpisodios() {
        return episodios;
    }

    public void setEpisodios(int episodios) {
        this.episodios = episodios;
    }



    @Override
    public String getTitulo() {
        return super.getTitulo();
    }

    @Override
    public int getLancamento() {
        return super.getLancamento();
    }

    @Override
    public void exibirInfo() {
        System.out.println("Serie: " + titulo + "\n" + "Lançamento: " + lancamento + "\n" + "Diretor: " + diretor + "\n" + "Classificação indicativa: " + classificacao + "\n" + "Duração: " + episodios + "\n");
    }
}
