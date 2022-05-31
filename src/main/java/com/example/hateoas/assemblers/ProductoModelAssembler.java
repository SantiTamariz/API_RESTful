package com.example.hateoas.assemblers;

import com.example.controllers.MainController;
import com.example.entities.Producto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

//Responsable de meter los enlaces a donde se indica

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto, // 
                        linkTo(methodOn(MainController.class).findById(producto.getId())).withSelfRel(),
                        linkTo(methodOn(MainController.class).getProductos(1,3)).withRel("productos"));
    }
    
}

