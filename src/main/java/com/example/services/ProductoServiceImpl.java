package com.example.services;

import java.util.List;

import com.example.dao.PresentacionDao;
import com.example.dao.ProductoDao;
import com.example.entities.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoDao productoDao;

    @Autowired
    private PresentacionDao presentacionDao;

    @Override
    public List<Producto> findAll(Sort sort) {
        return productoDao.findAll(sort);
    }

    @Override
    public Page<Producto> findAll(Pageable pageable) {
        return productoDao.findAll(pageable);
    }

    @Override
    public Producto findById(long id) {
        return productoDao.findById(id);
    }

    @Override
    public void delete(long id) {
        productoDao.deleteById(id);
    }

    @Override
    public Producto save(Producto producto) {

        // Presentacion presentacion = presentacionDao.findById(producto.getPresentacion().getId()).get();
        // producto.setPresentacion(presentacion);
        return productoDao.save(producto);
    }
    
}
