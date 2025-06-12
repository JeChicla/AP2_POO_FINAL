package org.example.Util;
import java.util.ArrayList;
import java.util.List;

public abstract class Conteudo {
    protected int id;
    protected String titulo;
    protected int lancamento;
    protected List<Genero> generos;
    protected String diretor;
    protected int classificacao;

    public Conteudo(String titulo, int lancamento, String diretor, int classificacao) {
        this.titulo = titulo;
        this.lancamento = lancamento;
        this.generos = new ArrayList<>();
        this.diretor = diretor;
        if (classificacao == 0 ||classificacao == 10 ||classificacao == 12 ||classificacao == 14 ||classificacao == 16||classificacao == 18) {
            this.classificacao = classificacao;}
    }

    public Conteudo(int id, String titulo, int lancamento, List<Genero> generos, String diretor, int classificacao) {
        this.id = id;
        this.titulo = titulo;
        this.lancamento = lancamento;
        this.generos = generos;
        this.diretor = diretor;
        this.classificacao = classificacao;
    }

    public Conteudo() {

    }

    //Metodos que serao obrigatorios para os Conteudos;

    public abstract void exibirInfo();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getLancamento() {
        return lancamento;
    }

    public void setLancamento(int lancamento) {
        this.lancamento = lancamento;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
    }

    public void adicionarGenero(Genero genero){
        if (!generos.contains(genero)){
            generos.add(genero);
            genero.adicionarConteudo(this);
        }
    }
    public void listarGeneros() {
        System.out.println(titulo + ":");
        for (Genero g : generos) {
            System.out.println(g.getNomeGenero());
        }
    }


}
