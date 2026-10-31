package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {


   static final int UMBRAL_HORAS_DIA = 8;
   static final int HORA_APERTURA = 8;
   static final int HORA_CIERRE = 22;
   static final int CAPACIDAD_TOTAL = 10;
   static final double TARIFA_HORA_CARRO = 4000;
   static final double TARIFA_DIA_CARRO = 30000;
   static final double TARIFA_HORA_MOTO = 2000;
   static final double TARIFA_DIA_MOTO = 15000;

   static final String TIPO_CARRO = "Carro";
   static final String TIPO_MOTO = "Moto";

   static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


   static final Scanner SCANNER = new Scanner(System.in);


   static String[] tipoVehiculo = new String[CAPACIDAD_TOTAL];
   static LocalDateTime[] fechaIngreso = new LocalDateTime[CAPACIDAD_TOTAL];


   static int celdasOcupadas;
   static int celdasLibres;
   static double recaudoCarros;
   static double recaudoMotos;

   static void inicializarArreglos() {
      for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
         tipoVehiculo[i] = null;
         fechaIngreso[i] = null;
      }
      celdasOcupadas = 0;
      celdasLibres = CAPACIDAD_TOTAL;
      recaudoCarros = 0;
      recaudoMotos = 0;
   }

   static void imprimir(String mensaje) {
      System.out.println(mensaje);
   }

   public static void main(String[] args) {
      inicializarArreglos();
   }
   static void registrarEntrada() {
      int celda = celdaDisponible();
      if (celda == -1) {
         imprimir("El parqueadero está lleno. No es posible registrar el ingreso.");
         return;
      }

      String tipo = leerTipoVehiculo();
      LocalDateTime ingreso = leerFechaHora("Fecha y hora de ingreso (yyyy-MM-dd HH:mm): ");

      tipoVehiculo[celda] = tipo;
      fechaIngreso[celda] = ingreso;
      celdasOcupadas++;
      celdasLibres--;

      imprimir("Vehículo (" + tipo + ") registrado en la celda " + (celda + 1) + ".");
   }
   static int celdaDisponible() {
      for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
         if (tipoVehiculo[i] == null) {
            return i;
         }
      }
      return -1;
   }
}