package concesionaria;

import java.util.Scanner;

public class Main {
    /**
     * El método main() es el punto de inicio del programa.
     */
    public static void main(String[] args) {
  
        CarManager manager = new CarManager("concesionaria.txt");    
        Scanner sc = new Scanner(System.in);

        // El menú se mostrará una y otra vez hasta que el usuario elija "Salir".
        while (true) {
            // Mostramos las opciones del menú.
            System.out.println("\n--- SISTEMA DE INVENTARIO DE LA CONCESIONARIA ---");
            System.out.println("1. Agregar auto");
            System.out.println("2. Actualizar auto");
            System.out.println("3. Eliminar auto");
            System.out.println("4. Mostrar inventario");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            
            // Leemos la opción que el usuario escribió.
            String opcion = sc.nextLine().trim();

            try {
                // El 'switch' nos ayuda a ejecutar un bloque de código diferente dependiendo del número que elija el usuario.
                switch (opcion) {
                    case "1":
                        // Opción 1: Agregar un auto. Le pedimos todos los datos.
                        System.out.print("VIN (Número de Identificación del Vehículo): ");
                        String vin = sc.nextLine().trim();
                        System.out.print("Marca: ");
                        String marca = sc.nextLine().trim();
                        System.out.print("Modelo: ");
                        String modelo = sc.nextLine().trim();
                        System.out.print("Año: ");
                        int ano = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Precio: ");
                        double precio = Double.parseDouble(sc.nextLine().trim());
                        // Creamos un nuevo objeto Car con los datos y se lo pasamos al manager.
                        Car c = new Car(vin, marca, modelo, ano, precio);
                        manager.addCar(c);
                        break;

                    case "2":
                        // Opción 2: Actualizar un auto.
                        System.out.print("VIN a actualizar: ");
                        String vAct = sc.nextLine().trim();
                        System.out.print("Nuevo año (enter para omitir): ");
                        String anoStr = sc.nextLine().trim();
                        // Verificamos si el usuario escribió algo, si no, lo dejamos nulo.
                        Integer nuevoAno = anoStr.isEmpty() ? null : Integer.parseInt(anoStr);
                        System.out.print("Nuevo precio (enter para omitir): ");
                        String precStr = sc.nextLine().trim();
                        Double nuevoPrecio = precStr.isEmpty() ? null : Double.parseDouble(precStr);
                        // Llamamos al método del manager para actualizar el auto.
                        manager.updateCar(vAct, nuevoAno, nuevoPrecio);
                        break;

                    case "3":
                        // Opción 3: Eliminar un auto.
                        System.out.print("VIN a eliminar: ");
                        String vDel = sc.nextLine().trim();
                        // Llamamos al método del manager para eliminar el auto.
                        manager.removeCar(vDel);
                        break;

                    case "4":
                        // Opción 4: Mostrar el inventario.
                        manager.listCars();
                        break;

                    case "5":
                        // Opción 5: Salir del programa.
                        System.out.println(" Saliendo. ¡Hasta pronto!");
                        sc.close(); // Cerramos el scanner para liberar recursos.
                        return; // Terminamos la ejecución del programa.

                    default:
                        // Si el usuario mete un número o texto que no está en las opciones.
                        System.out.println("️ Opción no válida. Intenta otra vez.");
                }
            } catch (NumberFormatException e) {
                // Atrapamos el error si el usuario no ingresa un número donde se espera.
                System.err.println(" Error: Entrada numérica inválida. Asegúrate de ingresar números correctos para año/precio.");
            } catch (Exception e) {
                // Este es un metodo para atrapar cualquier otro error inesperado.
                System.err.println(" Ocurrió un error inesperado: " + e.getMessage());
            }
        }
    }
}