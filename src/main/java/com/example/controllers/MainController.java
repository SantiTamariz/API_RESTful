package com.example.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.example.entities.Producto;
import com.example.services.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController   // responsable de armar el JSON
@RequestMapping("/productos") /// LA URL REFIERE A LOS RECURSOS, en este caso se asume que manda productos
public class MainController {

    @Autowired
    private ProductoService productoService;

    // no devuelve el modelo
    // devuelve JSON para ser consumido desde el front

    // get mapping no lleva /nombre
    // en REST se define el metodo segun el verbo HTTP, GET POST DELETE
    @GetMapping                                        
    public ResponseEntity<List<Producto>> getProductos  // parametros OPCIONALES paginacion y cantidad
                            (@RequestParam(name = "page", required = false) Integer page,
                            @RequestParam(name = "size", required = false) Integer size) {
    // ResponsiveEntity permite devolver un obj con mas informacion, ej el Status
    // para que sea rest tiene que devolver esa informacion

        // Para que devuelva el listado de productos ordenado por nombre, tanto si es
        // con paginacion
        // como si no lo es
        Sort sortByName = Sort.by("nombre");  // crear variable que tenga el criterio de
                                                            // ORDENAMIENTO que quiero
                                                            // se podria pasar tb por param
        ResponseEntity<List<Producto>> responseEntity = null;

        List<Producto> productos = null;  // se podria hacer con VAR tb

        if (page != null && size != null) { // CON paginacion
            

            Pageable pageable = PageRequest.of(page, size, sortByName); 
                                // recibe param pag, cantidad y crit de ordenacion
            productos = productoService.findAll(pageable).getContent();
                                                        // extraer productos 
        } else {
            // Sin paginacion y devolvemos la lista completa de los productos
            productos = productoService.findAll(sortByName);

        }

        if (productos.size() > 0) {
            // si devuelve la lista == status OK
            responseEntity = new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
        } else {
            // si el listado esta vacio == otro status
            responseEntity = new ResponseEntity<List<Producto>>(HttpStatus.NO_CONTENT); //204
        }

        return responseEntity;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> findById(@PathVariable(name = "id") Long id){

        ResponseEntity<Producto> responseEntity = null;

        Producto producto = productoService.findById(id);

        if(producto != null){
            responseEntity = new ResponseEntity<Producto>(producto, HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return responseEntity;
    }

    //Persistir un método en la BD mediante el método post y un ResponseEntity
    //Post porque es contenido a persisitir
    //RequestBody, body porque viene en el body al ser POST
    //@Valid para poder validar el JSON con las @annotaciones del Producto
    //BindingResult como objeto para validar el JSON
    @PostMapping()
    public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Producto producto, BindingResult result){

        //Para la respuesta del objeto persistido
        ResponseEntity<Map<String, Object>> responseEntity = null;

        //para la respuesta de los errores
        Map<String, Object> responseAsMap = new HashMap<>();

        //Comprobar si el objeto tiene errores, validar cada campo de la clase prodcuto
        if(result.hasErrors()){

            List<String> errores = new ArrayList<>();

            //Recorrer la colección para sacar los errores 
            //La colleción de result tiene los errores y devuelve una lista de todos los errores
            //Sacar el mensaje/String de la lista de errores, el defaultMessage es message de la entidad prodcuto
            for(ObjectError error : result.getAllErrors()){
                errores.add(error.getDefaultMessage());

            }

            //Devuelve la lista de errores
            responseAsMap.put("errores", errores);

            //Declaras dentro de la variable el responseMap con los errores y el http de error
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);

        }

        try {

            Producto productoDB = productoService.save(producto);

            if(producto!=null){
                responseAsMap.put("mensaje", "El producto con id " + productoDB.getId() + " se ha creado exitosamente");
                responseAsMap.put("producto", productoDB);
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.CREATED);
            }
            else{
                responseAsMap.put("mensaje", "Error creando el producto");
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.BAD_GATEWAY);
            }

        
        } catch (DataAccessException e) {
            //Excepcion de los datos
            responseAsMap.put("mensaje", "No se ha podido crear el prodcuto" + e.getMostSpecificCause());
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    

    }

    //Modificar un producto es practicamente similar al anterior
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> modificar(@Valid @RequestBody Producto producto, BindingResult result, @PathVariable(name="id") Long id){

        //Para la respuesta del objeto persistido
        ResponseEntity<Map<String, Object>> responseEntity = null;

        //para la respuesta de los errores
        Map<String, Object> responseAsMap = new HashMap<>();

        //Comprobar si el objeto tiene errores, validar cada campo de la clase prodcuto
        if(result.hasErrors()){

            List<String> errores = new ArrayList<>();

            //Recorrer la colección para sacar los errores 
            //La colleción de result tiene los errores y devuelve una lista de todos los errores
            //Sacar el mensaje/String de la lista de errores, el defaultMessage es message de la entidad prodcuto
            for(ObjectError error : result.getAllErrors()){
                errores.add(error.getDefaultMessage());

            }

            //Devuelve la lista de errores
            responseAsMap.put("errores", errores);

            //Declaras dentro de la variable el responseMap con los errores y el http de error
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);

        }

        try {

            producto.setId(id);

            Producto productoDB = productoService.save(producto);

            if(productoDB!=null){
                responseAsMap.put("mensaje", "El producto con id " + productoDB.getId() + " se ha modificado exitosamente");
                responseAsMap.put("producto", productoDB);
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.CREATED);
            }
            else {
                responseAsMap.put("mensaje", "Error actualizando el producto");
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.BAD_GATEWAY);
            }

        
        } catch (DataAccessException e) {
            //Excepcion de los datos
            responseAsMap.put("mensaje", "No se ha podido actualizar el prodcuto" + e.getMostSpecificCause());
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name="id") Long id){

        ResponseEntity<String> responseEntity = null;


        productoService.delete(id);

        //Comprobar si se ha borrado
        Producto productoDB = productoService.findById(id);

        if(productoDB==null){
            //Se ha borrado correctamente
            responseEntity = new ResponseEntity<>("El producto se ha borrado correctamente", HttpStatus.OK);
            
        }
        else responseEntity = new ResponseEntity<>("No se ha podido eliminar", HttpStatus.BAD_REQUEST);


        return responseEntity;

    }
}

// POSTMAN
//ejemplo
// http://localhost:8080/productos?page=1&size=3