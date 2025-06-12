package org.example;
import org.example.Util.*;
import org.example.dao.ConteudoRepository;
import org.example.dao.*;
import org.example.bd.ConnectionFactory;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Usuario joao = new Usuario("joao12345");
        Usuario maria = new Usuario("maria02");
        Usuario valen = new Usuario("valen1");
        Usuario homura = new Usuario("homura_akemi");
        Usuario ana = new Usuario("ana_costa");
        Usuario lucas = new Usuario("lucas_martins");
        Usuario julia = new Usuario("julia_ferreira");
        Usuario bruno = new Usuario("bruno_almeida");
        Usuario carla = new Usuario("carla_pereira");
        Usuario rafael = new Usuario("rafael_rodrigues");
        Usuario laura = new Usuario("laura_barbosa");

        Filme matrix = new Filme("Matrix", 1999, "Diretor", 10, 120);
        Filme interestelar = new Filme("Interestelar", 2014, "Christopher Nolan", 12, 169);
        Filme origem = new Filme("A Origem", 2010, "Christopher Nolan", 14, 148);
        Filme avatar = new Filme("Avatar", 2009, "James Cameron", 12, 162);
        Filme gladiador = new Filme("Gladiador", 2000, "Ridley Scott", 16, 155);
        Filme parasita = new Filme("Parasita", 2019, "Bong Joon-ho", 16, 132);
        Filme corra = new Filme("Corra!", 2017, "Jordan Peele", 16, 104);
        Filme bastardos = new Filme("Bastardos Inglórios", 2009, "Quentin Tarantino", 18, 153);
        Filme dunas = new Filme("Duna", 2021, "Denis Villeneuve", 14, 155);
        Filme clubeDaLuta = new Filme("Clube da Luta", 1999, "David Fincher", 18, 139);


        Serie madoka = new Serie("Madoka", 2011, "Diretor2", 14, 12);
        Serie breakingBad = new Serie("Breaking Bad", 2008, "Vince Gilligan", 18, 62);
        Serie strangerThings = new Serie("Stranger Things", 2016, "Duffer Brothers", 14, 34);
        Serie theOffice = new Serie("The Office", 2005, "Greg Daniels", 12, 201);
        Serie dark = new Serie("Dark", 2017, "Baran bo Odar", 16, 26);
        Serie theBoys = new Serie("The Boys", 2019, "Eric Kripke", 18, 24);


        Genero ficcao = new Genero("Ficção Científica");
        Genero acao = new Genero("Ação");
        Genero fantasia = new Genero("Fantasia");
        Genero drama = new Genero("Drama");
        Genero comedia = new Genero("Comédia");
        Genero suspense = new Genero("Suspense");
        Genero terror = new Genero("Terror");
        Genero romance = new Genero("Romance");
        Genero documentario = new Genero("Documentário");
        Genero aventura = new Genero("Aventura");
        Genero animacao = new Genero("Animação");
        Genero musical = new Genero("Musical");
        Genero policial = new Genero("Policial");
        Genero guerra = new Genero("Guerra");


        homura.seguirUsuario(valen);
        ana.seguirUsuario(lucas);
        ana.seguirUsuario(julia);

        lucas.seguirUsuario(bruno);
        lucas.seguirUsuario(rafael);

        julia.seguirUsuario(carla);
        julia.seguirUsuario(rafael);
        julia.seguirUsuario(laura);
        bruno.seguirUsuario(laura);
        bruno.seguirUsuario(ana);
        carla.seguirUsuario(lucas);
        rafael.seguirUsuario(laura);

        laura.seguirUsuario(ana);
        valen.listarSeguidores();
        laura.listarSeguidores();

        homura.exibirReviewsUsuario();
        fantasia.adicionarConteudo(madoka);
        fantasia.listarConteudo();

        madoka.exibirInfo();
        acao.adicionarConteudo(matrix);
        fantasia.adicionarConteudo(madoka);


        matrix.adicionarGenero(acao);
        madoka.adicionarGenero(fantasia);


        joao.adicionarFavorito(matrix);
        joao.adicionarFavorito(madoka);


        System.out.println("Favoritos de João:");
        joao.listarFavoritos();


        joao.seguirUsuario(maria);


        System.out.println("Seguidores de Maria:");
        maria.listarSeguidores();


        Review review1 = new Review(joao, matrix, 8.5f, "Muito bom", "Gostei bastante do filme.");
        Review review2 = new Review(ana, matrix, 9.0f, "Excelente!", "Visual e roteiro incríveis.");
        Review review3 = new Review(lucas, madoka, 7.5f, "Interessante", "A série tem bons momentos, mas podia ser melhor.");
        Review review4 = new Review(julia, madoka, 8.0f, "Gostei", "Boa série, ótima para maratonar.");
        Review review5 = new Review(bruno, matrix, 6.0f, "Regular", "Esperava mais ação.");
        Review review6 = new Review(carla, madoka, 9.5f, "Fantástica!", "Personagens cativantes e enredo envolvente.");
        Review review7 = new Review(rafael, matrix, 7.0f, "Bom", "Vale a pena assistir, mas com ressalvas.");
        Review review8 = new Review(laura, madoka, 8.8f, "Recomendo", "Muito bem produzida, gostei bastante.");
        Review review9 = new Review(joao, madoka, 8.5f, "Muito bom", "Gostei bastante do filme.");
        Review review10 = new Review(homura, madoka, 8.5f, "Muito bom", "Gostei bastante do filme.");

        ana.adicionarReview(review2);
        lucas.adicionarReview(review3);
        julia.adicionarReview(review4);
        bruno.adicionarReview(review5);
        carla.adicionarReview(review6);
        rafael.adicionarReview(review7);
        laura.adicionarReview(review8);
        joao.adicionarReview(review1);
        joao.adicionarReview(review2);
        joao.adicionarReview(review9);
        joao.exibirReviewsUsuario();
        ana.adicionarReview(review2);
        ana.exibirReviewsUsuario();


        matrix.listarGeneros();

        fantasia.listarConteudo();
        ConnectionFactory fabricaDeConexao = new ConnectionFactory();
        Connection connection = fabricaDeConexao.recuperaConexao();

        UsuarioDAO udao =new UsuarioDAO(connection);
        GeneroDAO gdao = new GeneroDAO(connection);
        ReviewDAO rdao = new ReviewDAO(connection);
        SerieDAO sdao = new SerieDAO(connection);
        FilmeDAO fdao = new FilmeDAO(connection);


        Usuario pedro = new Usuario("pedro_silva");
        Usuario camila = new Usuario("camila_oliveira");
        Usuario daniel = new Usuario("daniel_souza");
        Usuario isabela = new Usuario("isabela_mendes");
        Usuario thiago = new Usuario("thiago_ramos");
        Usuario sofia = new Usuario("sofia_gomes");
        Usuario felipe = new Usuario("felipe_castro");
        Usuario leticia = new Usuario("leticia_moraes");
        Usuario gustavo = new Usuario("gustavo_lima");
        Usuario beatriz = new Usuario("beatriz_santana");

        udao.salvar(pedro);
        System.out.println("ID gerado: " + pedro.getId());

        udao.salvar(camila);
        System.out.println("ID gerado: " + camila.getId());

        udao.salvar(daniel);
        System.out.println("ID gerado: " + daniel.getId());

        udao.salvar(isabela);
        System.out.println("ID gerado: " + isabela.getId());

        udao.salvar(thiago);
        System.out.println("ID gerado: " + thiago.getId());

        udao.salvar(sofia);
        System.out.println("ID gerado: " + sofia.getId());

        udao.salvar(felipe);
        System.out.println("ID gerado: " + felipe.getId());

        udao.salvar(leticia);
        System.out.println("ID gerado: " + leticia.getId());

        udao.salvar(gustavo);
        System.out.println("ID gerado: " + gustavo.getId());

        udao.salvar(beatriz);
        System.out.println("ID gerado: " + beatriz.getId());

        Filme theGodfather = new Filme("The Godfather", 1972, "Francis Ford Coppola", 18, 175);
        Filme theDarkKnight = new Filme("The Dark Knight", 2008, "Christopher Nolan", 14, 152);
        Filme pulpFiction = new Filme("Pulp Fiction", 1994, "Quentin Tarantino", 18, 154);
        Filme theShawshank = new Filme("The Shawshank Redemption", 1994, "Frank Darabont", 14, 142);
        Filme fightClub = new Filme("Fight Club", 1999, "David Fincher", 18, 139);
        Filme inception = new Filme("Inception", 2010, "Christopher Nolan", 14, 148);
        Filme bladeRunner = new Filme("Blade Runner 2049", 2017, "Denis Villeneuve", 14, 164);
        Filme madMax = new Filme("Mad Max: Fury Road", 2015, "George Miller", 16, 120);

        fdao.salvar(inception);
        System.out.println("ID gerado: " + inception.getId());

        fdao.salvar(theGodfather);
        System.out.println("ID gerado: " + theGodfather.getId());

        fdao.salvar(theDarkKnight);
        System.out.println("ID gerado: " + theDarkKnight.getId());

        fdao.salvar(pulpFiction);
        System.out.println("ID gerado: " + pulpFiction.getId());

        fdao.salvar(theShawshank);
        System.out.println("ID gerado: " + theShawshank.getId());

        fdao.salvar(fightClub);
        System.out.println("ID gerado: " + fightClub.getId());

        fdao.salvar(bladeRunner);
        System.out.println("ID gerado: " + bladeRunner.getId());

        fdao.salvar(madMax);
        System.out.println("ID gerado: " + madMax.getId());

        acao.setId(1);
        fantasia.setId(2);
        drama.setId(3);
        comedia.setId(4);
        suspense.setId(5);
        terror.setId(6);
        romance.setId(7);
        documentario.setId(8);
        aventura.setId(9);
        animacao.setId(10);
        musical.setId(11);
        policial.setId(12);
        guerra.setId(13);


        Serie gameOfThrones = new Serie("Game of Thrones", 2011, "David Benioff & D.B. Weiss", 18, 73);
        Serie theCrown = new Serie("The Crown", 2016, "Peter Morgan", 14, 40);
        sdao.salvar(gameOfThrones);
        System.out.println("ID gerado: " + gameOfThrones.getId());
        sdao.salvar(theCrown);
        System.out.println("ID gerado: " + theCrown.getId());

        ConteudoRepository cdao = new ConteudoRepository(connection);

        cdao.adicionarGeneroAoConteudo(gameOfThrones.getId(), fantasia.getId());
        cdao.adicionarGeneroAoConteudo(gameOfThrones.getId(), guerra.getId());




        Review r20 = new Review(pedro, gameOfThrones, 5f, "Muito ruim", "Gostei nada");
        Review r21 = new Review(pedro, theGodfather, 8.5f, "Muito bom", "Gostei bastante do filme.");

        udao.seguirUsuario(pedro.getId(), camila.getId());
        udao.seguirUsuario(camila.getId(), pedro.getId());
        udao.adicionarConteudoAoUsuario(pedro.getId(), gameOfThrones.getId());
        udao.adicionarFavoritoAoUsuario(pedro.getId(), gameOfThrones.getId());
        rdao.buscarPorId(3);
        udao.listarUsuarios2seguidores();
        udao.listarUsuariosCom1Conteudo();
        rdao.salvar(r20);
        rdao.salvar(r21);
    }

}





