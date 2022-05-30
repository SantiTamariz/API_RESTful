package com.example.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacio")
    @Size(max = 50, min = 4, message = "Nombre entre 3 y 50")
    private String nombre;

    @Size(max = 250, message = "Descripcion hasta 250 caracteres")
    private String descripcion;

    @Min(value = 0, message = "el precio no puede ser negativo")
    private Double precio;

    @Min(value = 0, message = "el stock no puede estar negativo")
    private Long stock;

    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @NotNull(message = "el producto tiene que tener una presentacion")
    private Presentacion presentacion;

}
