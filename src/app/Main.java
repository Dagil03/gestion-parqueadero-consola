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
   static int leerEntero(String mensaje, int minimo, int maximo) {
      while (true) {
         System.out.print(mensaje);
         String texto = SCANNER.nextLine().trim();

         boolean soloDigitos = !texto.isEmpty() && texto.length() <= 9;
         for (int i = 0; i < texto.length() && soloDigitos; i++) {
            char c = texto.charAt(i);
            if (c < '0' || c > '9') {
               soloDigitos = false;
            }
         }

         if (soloDigitos) {
            int valor = Integer.parseInt(texto);
            if (valor >= minimo && valor <= maximo) {
               return valor;
            }
         }
         imprimir("Dato inválido. Ingrese un número entre " + minimo + " y " + maximo + ".");
      }
   }

   static String leerTipoVehiculo() {
      int opcion = leerEntero("Tipo de vehículo (1. Carro, 2. Moto): ", 1, 2);
      return opcion == 1 ? TIPO_CARRO : TIPO_MOTO;
   }

   static int leerCelda() {
      while (true) {
         int celda = leerEntero("Número de celda (1-" + CAPACIDAD_TOTAL + "): ", 1, CAPACIDAD_TOTAL) - 1;
         if (tipoVehiculo[celda] != null) {
            return celda;
         }
         imprimir("La celda " + (celda + 1) + " está libre. Indique una celda ocupada.");
      }
   }

   static LocalDateTime leerFechaHora(String mensaje) {
      while (true) {
         System.out.print(mensaje);
         String texto = SCANNER.nextLine().trim();
         if (!formatoFechaHoraValido(texto)) {
            imprimir("Fecha u hora inválidas. Use yyyy-MM-dd HH:mm y el horario 08:00-22:00.");
            continue;
         }

         LocalDateTime fecha = LocalDateTime.parse(texto, FORMATO_FECHA_HORA);
         if (!fecha.format(FORMATO_FECHA_HORA).equals(texto)) {
            imprimir("La fecha no existe en el calendario.");
            continue;
         }
         return fecha;
      }
   }

   static boolean formatoFechaHoraValido(String texto) {
      if (texto.length() != 16
              || texto.charAt(4) != '-'
              || texto.charAt(7) != '-'
              || texto.charAt(10) != ' '
              || texto.charAt(13) != ':') {
         return false;
      }

      int anio = leerDigitos(texto, 0, 4);
      int mes = leerDigitos(texto, 5, 2);
      int dia = leerDigitos(texto, 8, 2);
      int hora = leerDigitos(texto, 11, 2);
      int minuto = leerDigitos(texto, 14, 2);

      return anio >= 1 && mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31
              && hora >= HORA_APERTURA && hora <= HORA_CIERRE
              && (hora != HORA_CIERRE || minuto <= 0)
              && minuto >= 0 && minuto <= 59;
   }

   static int leerDigitos(String texto, int inicio, int cantidad) {
      int valor = 0;
      for (int i = inicio; i < inicio + cantidad; i++) {
         char caracter = texto.charAt(i);
         if (caracter < '0' || caracter > '9') {
            return -1;
         }
         valor = valor * 10 + caracter - '0';
      }
      return valor;
   }
   static void registrarSalida() {
      if (celdasOcupadas == 0) {
         imprimir("No hay vehículos en el parqueadero.");
         return;
      }

      int celda = leerCelda();
      LocalDateTime ingreso = fechaIngreso[celda];
      String tipo = tipoVehiculo[celda];

      LocalDateTime salida = leerFechaHora("Fecha y hora de salida (yyyy-MM-dd HH:mm): ");
      while (salida.isBefore(ingreso)) {
         imprimir("La salida no puede ser anterior al ingreso (" + ingreso.format(FORMATO_FECHA_HORA) + ").");
         salida = leerFechaHora("Fecha y hora de salida (yyyy-MM-dd HH:mm): ");
      }

      double total = liquidar(celda, salida);

      if (tipo.equals(TIPO_CARRO)) {
         recaudoCarros += total;
      } else {
         recaudoMotos += total;
      }

      tipoVehiculo[celda] = null;
      fechaIngreso[celda] = null;
      celdasOcupadas--;
      celdasLibres++;
   }

}