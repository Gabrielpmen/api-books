package br.com.gabs.mybooks_api.model;
import java.util.UUID;

// Esta é uma classe POJO (Plain Old Java Object).
// Sua única responsabilidade é representar os dados de um livro.
public class Livro {

    // Atributos privados da classe
    private UUID id;
    private String titulo;
    private String genero;
    private String midia;
    private String anoLancamento;
    private String statusLeitura;
    private String anoCompra;
    private double valorPago;
    private String autor;
    private String esaga;

    // Construtor padrão (sem argumentos).
    // É essencial para que bibliotecas como a Jackson possam criar instâncias vazias do objeto.
    public Livro() {
        // Garante que todo novo livro, mesmo que criado vazio, receba um ID único.
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    // --- Getters e Setters ---
    // Métodos públicos que permitem que outras classes (como o Jackson e o Spring)
    // leiam e escrevam nos atributos privados de forma controlada.
    // Esta é uma convenção fundamental em Java.

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getMidia() {
        return midia;
    }

    public void setMidia(String midia) {
        this.midia = midia;
    }

    public String getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(String anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getStatusLeitura() {
        return statusLeitura;
    }

    public void setStatusLeitura(String statusLeitura) {
        this.statusLeitura = statusLeitura;
    }

    public String getAnoCompra() {
        return anoCompra;
    }

    public void setAnoCompra(String anoCompra) {
        this.anoCompra = anoCompra;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEsaga() {
        return esaga;
    }

    public void setEsaga(String esaga) {
        this.esaga = esaga;
    }
}