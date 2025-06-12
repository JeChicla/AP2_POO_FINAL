package org.example.Util;


import java.util.List;

public class Filme extends Conteudo {
    private int duracao;

    public Filme(String titulo, int lancamento, String diretor, int classificacao, int duracao) {
        super(titulo, lancamento, diretor, classificacao);
        this.duracao = duracao;
    }

    public Filme(int id, String titulo, int lancamento, List<Genero> generos, String diretor, int classificacao, int duracao) {
        super(id, titulo, lancamento, generos, diretor, classificacao);
        this.duracao = duracao;
    }

    public Filme() {

    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    @Override
    public void exibirInfo() {
        System.out.print("Filme: " + titulo + "\n" + "Lançamento: " + lancamento + "\n" + "Diretor: " + diretor + "\n" + "Classificação indicativa: " + classificacao + "\n" + "Duração: " + duracao + "\n");
    }

    @Override
    public String getTitulo() {
        return super.getTitulo();
    }

    @Override
    public int getLancamento() {
        return super.getLancamento();
    }
}



