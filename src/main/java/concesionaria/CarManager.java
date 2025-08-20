package concesionaria;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Clase CarManager: Administra la colección de carros.
 * Se encarga de guardar y leer los datos del archivo.
 */
public class CarManager {
    // filePath: La dirección del archivo donde guardaremos los autos.
    private final Path filePath;
    // cars: Una lista especial para guardar los carros en memoria (mientras el programa se ejecuta).
    private final Map<String, Car> cars = new LinkedHashMap<>();

    /**
     * Constructor: Cuando creamos el CarManager, le decimos el nombre del archivo.
     * @param filename  El nombre del archivo de texto ("concesionaria.txt").
     */
    public CarManager(String filename) {
        this.filePath = Paths.get(filename);
        // Al crearse, el manager intenta cargar los autos que ya están guardados.
        loadFromFile();
    }

    /**
     * loadFromFile(): Lee el archivo de texto y carga los autos en la memoria.
     * Este método se llama al iniciar el programa.
     */
    private void loadFromFile() {
        // Paso 1: Verificamos si el archivo existe. Si no, lo creamos.
        if (Files.notExists(filePath)) {
            System.out.println(" Archivo de inventario de autos no encontrado. Se creará: " + filePath.toString());
            try {
                Files.createFile(filePath);
                System.out.println(" Archivo creado: " + filePath.toString());
            } catch (IOException | SecurityException e) {
                System.err.println(" No se pudo crear el archivo: " + e.getMessage());
            }
            return;
        }

        // Paso 2: Si el archivo existe, lo leemos línea por línea.
        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue; // Ignoramos las líneas vacías.
                try {
                    // Usamos el método de la clase Car para convertir la línea en un objeto Car.
                    Car c = Car.fromLine(line);
                    // Y lo guardamos en nuestra lista de autos.
                    cars.put(c.getVin(), c);
                } catch (IllegalArgumentException ex) {
                    // Si una línea tiene un error, mostramos un mensaje y la ignoramos.
                    System.err.printf("️ Línea %d corrupta ignorada: %s%n", lineNum, ex.getMessage());
                }
            }
            System.out.println("Carga de archivo completada. Autos cargados: " + cars.size());
        } catch (IOException e) {
            System.err.println(" Error al leer el archivo: " + e.getMessage());
        }
    }

    /**
     * saveToFile(): Guarda todos los autos de la memoria en el archivo de texto.
     * Este método se llama después de cada cambio (agregar, actualizar, eliminar).
     */
    private void saveToFile() {
        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING)) {
            // Recorremos la lista de autos.
            for (Car c : cars.values()) {
                // Para cada auto, usamos el método toString() para convertirlo en una línea de texto...
                bw.write(c.toString());
                // ...y lo escribimos en el archivo.
                bw.newLine();
            }
            bw.flush(); // Nos aseguramos de que todo se escriba en el archivo.
            System.out.println(" Cambios guardados en archivo.");
        } catch (IOException | SecurityException e) {
            System.err.println(" Error al escribir en el archivo: " + e.getMessage());
        }
    }

    // --- Métodos de gestión del inventario ---
    
    /**
     * addCar(): Agrega un nuevo auto al inventario.
     * @param c  El objeto Car que queremos agregar.
     */
    public void addCar(Car c) {
        // Verificamos si ya existe un auto con ese mismo VIN.
        if (cars.containsKey(c.getVin())) {
            System.out.println("️ Ya existe un auto con ese VIN.");
            return;
        }
        // Si no existe, lo agregamos a nuestra lista y guardamos el cambio.
        cars.put(c.getVin(), c);
        saveToFile();
        System.out.printf("✅ Auto '%s %s' agregado.%n", c.getMarca(), c.getModelo());
    }

    /**
     * updateCar(): Actualiza el año o el precio de un auto existente.
     * @param vin          El VIN del auto a actualizar.
     * @param nuevoAno     El nuevo año 
     * @param nuevoPrecio  El nuevo precio o null si no se va a cambiar.
     */
    public void updateCar(String vin, Integer nuevoAno, Double nuevoPrecio) {
        // Buscamos el auto por su VIN en nuestra lista.
        Car c = cars.get(vin);
        // Si no lo encontramos, mostramos un mensaje.
        if (c == null) {
            System.out.println("️ Auto no encontrado.");
            return;
        }
        // Si lo encontramos, actualizamos solo los datos que nos hayan dado.
        if (nuevoAno != null) c.setAno(nuevoAno);
        if (nuevoPrecio != null) c.setPrecio(nuevoPrecio);
        // Guardamos los cambios en el archivo.
        saveToFile();
        System.out.printf(" Auto '%s %s' actualizado.%n", c.getMarca(), c.getModelo());
    }

    /**
     * removeCar(): Elimina un auto del inventario.
     * @param vin  El VIN del auto a eliminar.
     */
    public void removeCar(String vin) {
        // Intentamos quitar el auto de la lista.
        Car c = cars.remove(vin);
        // Si el resultado es null, significa que no lo encontró.
        if (c == null) {
            System.out.println("️ Auto no encontrado.");
            return;
        }
        // Si lo quitó, guardamos el cambio.
        saveToFile();
        System.out.printf("️ Auto '%s %s' eliminado.%n", c.getMarca(), c.getModelo());
    }

    /**
     * listCars(): Muestra en pantalla todos los autos que están en el inventario.
     */
    public void listCars() {
        if (cars.isEmpty()) {
            System.out.println(" No hay autos registrados.");
            return;
        }
        System.out.println("\n Autos registrados:");
        // Recorremos la lista y mostramos los datos de cada auto.
        for (Car c : cars.values()) {
            System.out.printf("- VIN: %s | Marca: %s | Modelo: %s | Año: %d | Precio: %.2f%n",
                    c.getVin(), c.getMarca(), c.getModelo(), c.getAno(), c.getPrecio());
        }
    }
}