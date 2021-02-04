package br.com.alura.ceep.model;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Nota implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long ordem;
    private String titulo;
    private String descricao;

    public Nota(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    @Ignore
    public Nota(String titulo, String descricao, Long id, Long ordem) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.id = id;
        this.ordem = ordem;
    }

    @Ignore
    public Nota(){}

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrdem() {
        return ordem;
    }

    public void setOrdem(Long ordem){
        this.ordem = ordem;
    }


    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", ordem=" + ordem +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}