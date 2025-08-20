package concesionaria;

import java.io.Serializable;

/**
 * Clase Car: Es el molde para crear objetos de tipo Carro.
 * Cada objeto de esta clase representa un solo auto en nuestro inventario.
 * La interfaz Serializable permite que los objetos de esta clase
 * puedan ser guardados en un archivo de texto.
 */
public class Car implements Serializable {
    // Estas son las características (los "atributos") de cada carro.
    // Usamos 'private' para que solo esta clase pueda cambiarlos directamente.
    private String vin;
    private String marca;
    private String modelo;
    private int ano;
    private double precio;

    /**
     * Constructor: Este método se llama cuando creamos un nuevo objeto Car.
     * @param vin     El VIN del auto.
     * @param marca   La marca del auto.
     * @param modelo  El modelo del auto.
     * @param ano     El año del auto.
     * @param precio  El precio del auto.
     */
    public Car(String vin, String marca, String modelo, int ano, double precio) {
        this.vin = vin;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.precio = precio;
    }

    // Nos permiten leer los datos privados de cada carro.
    public String getVin() { return vin; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAno() { return ano; }
    public double getPrecio() { return precio; }

    // Nos permiten modificar los datos privados.
    public void setAno(int ano) { this.ano = ano; }
    public void setPrecio(double precio) { this.precio = precio; }

    /**
     * toString(): Este método convierte los datos del carro en un solo texto.
     * Es útil para guardar la información de manera ordenada en el archivo de texto.
     * El formato es: VIN,Marca,Modelo,Año,Precio
     */
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d,%.2f",
                vin, marca.replace(",", " "), modelo.replace(",", " "), ano, precio);
    }

    /**
     * fromLine(): Este es un método especial (estático) que hace lo contrario de toString().
     * Toma una línea de texto del archivo y la convierte de nuevo en un objeto Car.
     * @param line  Una línea de texto del archivo (ej. "VIN123,Ford,Focus,2020,15000.00").
     * @return      Un nuevo objeto Car con los datos de esa línea.
     */
    public static Car fromLine(String line) {
        // Divide la línea en partes usando la coma como separador.
        String[] parts = line.split(",");
        // Si no tiene el número correcto de partes, lanza un error.
        if (parts.length != 5) {
            throw new IllegalArgumentException("Línea con número incorrecto de campos: " + line);
        }
        
        // Asignamos cada parte a su variable correspondiente, quitando los espacios extra.
        String vin = parts[0].trim();
        String marca = parts[1].trim();
        String modelo = parts[2].trim();
        
        int ano;
        double precio;
        
        // Convertimos las partes de texto a números. Si no son números, lanza un error.
        try {
            ano = Integer.parseInt(parts[3].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Año inválido en línea: " + line);
        }
        try {
            precio = Double.parseDouble(parts[4].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Precio inválido en línea: " + line);
        }
        
        // Finalmente, creamos y devolvemos el objeto Car con los datos.
        return new Car(vin, marca, modelo, ano, precio);
    }
}