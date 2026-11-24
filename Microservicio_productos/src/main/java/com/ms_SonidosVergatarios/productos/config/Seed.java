package com.ms_SonidosVergatarios.productos.config;

import com.ms_SonidosVergatarios.productos.model.Producto;
import com.ms_SonidosVergatarios.productos.repository.ProductoRepository;
import java.text.Normalizer;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Catálogo inicial (el mismo que traía el frontend). Solo se carga si la tabla está vacía. */
@Configuration
public class Seed {

    @Bean
    ApplicationRunner catalogoInicial(ProductoRepository repo, @Value("${app.seed.catalogo}") boolean activo) {
        return args -> {
            if (!activo || repo.count() > 0) {
                return;
            }
        crear(repo, "GA001", "Guitarra Acústica Folk", "Guitarras Acústicas", "Yamaha", "F310", "Tapa de abeto, aros y fondo de meranti. Ideal para iniciantes.", 129990, 8, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy_QHpxYeKChSb4Ks7rpgofVM8D1KLiHd4quCedYthYf_NMxU2xpVQC9Vy&s=10Guitarra%20Ac%C3%BAstica%20Folk");
        crear(repo, "GA002", "Guitarra Acústica Dreadnought", "Guitarras Acústicas", "Fender", "CD-60S", "Tapa de abeto macizo, brazo de caoba. Sonido cálido y proyectado.", 189990, 5, "https://placehold.co/400x400?text=Guitarra%20Ac%C3%BAstica%20Dreadnought");
        crear(repo, "GA003", "Guitarra Acústica Clásica 4/4", "Guitarras Acústicas", "Yamaha", "C40", "Nailon, tapa de abeto. Ideal para estudio y flamenco.", 89990, 10, "https://placehold.co/400x400?text=Guitarra%20Ac%C3%BAstica%20Cl%C3%A1sica%204%2F4");
        crear(repo, "GA004", "Guitarra Electroacústica", "Guitarras Acústicas", "Takamine", "GN20CE", "Pickup integrado, afinador incorporado.", 349990, 3, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdnqU_ltO-RqPw2ebHUZPfPAiVQaTQjqyM3W9roN22ku2mG_oPMYZhF1I&s=10Guitarra%20Electroac%C3%BAstica");
        crear(repo, "GA005", "Guitarra 3/4 Niños", "Guitarras Acústicas", "Yamaha", "JR1", "Tamaño reducido para niños de 6 a 10 años.", 79990, 6, "https://placehold.co/400x400?text=Guitarra%203%2F4%20Ni%C3%B1os");
        crear(repo, "GE001", "Guitarra Eléctrica Stratocaster", "Guitarras Eléctricas", "Squier", "Affinity Strat", "Cuerpo de álamo, mástil de arce, pastillas SSS.", 249990, 5, "https://placehold.co/400x400?text=Guitarra%20El%C3%A9ctrica%20Stratocaster");
        crear(repo, "GE002", "Guitarra Eléctrica Les Paul", "Guitarras Eléctricas", "Epiphone", "Les Paul Std", "Cuerpo caoba, tapa arce, pastillas humbucker.", 329990, 4, "https://placehold.co/400x400?text=Guitarra%20El%C3%A9ctrica%20Les%20Paul");
        crear(repo, "GE003", "Guitarra Eléctrica SG", "Guitarras Eléctricas", "Epiphone", "SG Standard", "Cuerpo caoba, mástil caoba, 2 humbuckers.", 319990, 3, "https://placehold.co/400x400?text=Guitarra%20El%C3%A9ctrica%20SG");
        crear(repo, "GE004", "Guitarra Eléctrica Telecaster", "Guitarras Eléctricas", "Squier", "Affinity Tele", "Cuerpo álamo, clavijero vintage, 2 pastillas single.", 239990, 4, "https://placehold.co/400x400?text=Guitarra%20El%C3%A9ctrica%20Telecaster");
        crear(repo, "GE005", "Guitarra Eléctrica Semi-hollow", "Guitarras Eléctricas", "Epiphone", "ES-335", "Semi-hueca, 2 humbuckers, ideal para jazz y blues.", 549990, 2, "https://placehold.co/400x400?text=Guitarra%20El%C3%A9ctrica%20Semi-hollow");
        crear(repo, "BA001", "Bajo Eléctrico 4 Cuerdas", "Bajos Eléctricos", "Squier", "Affinity PJ", "Pickup PJ, cuerpo álamo, mástil arce.", 299990, 5, "https://placehold.co/400x400?text=Bajo%20El%C3%A9ctrico%204%20Cuerdas");
        crear(repo, "BA002", "Bajo Eléctrico Jazz Bass", "Bajos Eléctricos", "Fender", "Player Jazz", "Alder body, 2 Alnico V Jazz single-coil.", 699990, 2, "https://placehold.co/400x400?text=Bajo%20El%C3%A9ctrico%20Jazz%20Bass");
        crear(repo, "BA003", "Bajo Acústico 4 Cuerdas", "Bajos Eléctricos", "Yamaha", "APX700II", "Electroacústico, afinador incorporado.", 429990, 2, "https://placehold.co/400x400?text=Bajo%20Ac%C3%BAstico%204%20Cuerdas");
        crear(repo, "BT001", "Batería Acústica 5 piezas", "Baterías", "Pearl", "Roadshow", "Incluye stands, platillos y pedal de bombo.", 599990, 2, "https://placehold.co/400x400?text=Bater%C3%ADa%20Ac%C3%BAstica%205%20piezas");
        crear(repo, "BT002", "Batería Electrónica 8 pads", "Baterías", "Roland", "TD-02KV", "Módulo TD-02, 8 pads de goma, pedal hi-hat.", 799990, 2, "https://placehold.co/400x400?text=Bater%C3%ADa%20Electr%C3%B3nica%208%20pads");
        crear(repo, "BT003", "Caja Snare 14\"", "Baterías", "Pearl", "STE1450", "Acero, 14x5\", 10 tensores.", 89990, 4, "https://placehold.co/400x400?text=Caja%20Snare%2014%22");
        crear(repo, "BT004", "Platillo Hi-Hat 14\"", "Baterías", "Zildjian", "A Series", "Latón B20, sonido brillante y claro.", 149990, 3, "https://placehold.co/400x400?text=Platillo%20Hi-Hat%2014%22");
        crear(repo, "BT005", "Platillo Crash 16\"", "Baterías", "Zildjian", "A Series", "Latón B20, ataque rápido.", 129990, 3, "https://placehold.co/400x400?text=Platillo%20Crash%2016%22");
        crear(repo, "TC001", "Teclado Digital 61 teclas", "Teclados y Pianos", "Yamaha", "PSR-E373", "61 teclas sensibles al tacto, 622 voces.", 249990, 4, "https://placehold.co/400x400?text=Teclado%20Digital%2061%20teclas");
        crear(repo, "TC002", "Piano Digital 88 teclas", "Teclados y Pianos", "Yamaha", "P-45", "88 teclas pesadas, 10 voces, pedal sustain incluido.", 499990, 2, "https://placehold.co/400x400?text=Piano%20Digital%2088%20teclas");
        };
    }

    private static void crear(ProductoRepository repo, String codigo, String nombre, String categoria,
                              String marca, String modelo, String descripcion, int precio, int stock,
                              String imagen) {
        Producto p = new Producto();
        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setNombreClave(Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("\\p{M}+", "")
                .trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT));
        p.setCategoria(categoria);
        p.setMarca(marca);
        p.setModelo(modelo);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setImagen(imagen);
        repo.save(p);
    }
}
