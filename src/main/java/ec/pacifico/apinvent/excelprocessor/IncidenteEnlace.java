/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.pacifico.apinvent.excelprocessor;

import java.io.*;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ggcas
 */

public class IncidenteEnlace {
    private static final String ARCHIVO_ENTRADA = "C:\\Users\\ggcas\\Documents\\git\\eventos_orion\\files\\Report_Testing_orion_ggcas.xlsx";
    
    private static final List<String> PROVEEDORES_VALIDOS = Arrays.asList("telconet", "puntonet", "cnt", "movistar", "cirion", "claro", "newaccess");
    public static void main(String[] args){
        try{
            List<Map<String,String>> datos = cargarDatos(ARCHIVO_ENTRADA);
            List<Map<String,String>> procesados = preprocesarDatos(datos);
            List<Map<String, Object>> eventos = analizarEventos(procesados);
            List<Map<String, Object>> corregidos = corregirEstadosReboot(eventos, 2);
            //rangoReporteDia(corregidos);
            rangoReporteMadrugada(corregidos);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static List<Map<String, String>> cargarDatos(String rutaArchivo) throws IOException {
        List<Map<String, String>> registros = new ArrayList<>();
        try (InputStream fis = new FileInputStream(Paths.get(rutaArchivo).toFile());
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet hoja = workbook.getSheetAt(0);
            Iterator<Row> filaIterator = hoja.iterator();

            // Saltar las dos primeras filas
            if (filaIterator.hasNext()) filaIterator.next();
            if (filaIterator.hasNext()) filaIterator.next();

            // Leer encabezados
            List<String> nombresColumnas = Arrays.asList("EventTime", "EventTypeName", "Message");

            // Leer filas restantes
            while (filaIterator.hasNext()) {
                Row fila = filaIterator.next();
                Map<String, String> filaDatos = new LinkedHashMap<>();

                for (int i = 0; i < nombresColumnas.size(); i++) {
                    Cell celda = fila.getCell(i);
                    String valor = obtenerValorCelda(celda);
                    filaDatos.put(nombresColumnas.get(i), valor);
                }

                // Ajustar EventTime si existe
                if (filaDatos.containsKey("EventTime")) {
                    String valorOriginal = filaDatos.get("EventTime");
                    try {
                        LocalDateTime fecha = LocalDateTime.parse(valorOriginal.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        fecha = fecha.minusHours(5).withSecond(0).withNano(0);  // Ajuste horario -5h
                        filaDatos.put("EventTime", fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    } catch (Exception e) {
                        filaDatos.put("EventTime", "");  // Si no parsea, dejar en blanco
                    }
                }

                registros.add(filaDatos);
            }
        }
        return registros;
    }

    private static String obtenerValorCelda(Cell celda){
        if(celda == null) return "";
        switch(celda.getCellType()){
            case STRING:
                return celda.getStringCellValue();
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(celda)){
                    return celda.getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            case BOOLEAN:
                return Boolean.toString(celda.getBooleanCellValue());
            case FORMULA:
                return celda.getCellFormula();
            default:
                return "";
        }
    }
    public static String extraerProveedor(String enlace){
        if(enlace==null){
            return null;
        }
        String enlaceLower = enlace.toLowerCase();
        for(String prov: PROVEEDORES_VALIDOS){
            if(enlaceLower.contains(prov.toLowerCase())){
                return prov.substring(0,1).toUpperCase()+prov.substring(1).toLowerCase();
            }
        }
        return null;
    }
    public static String extraerAgenciaBase(String message){
        if(message == null) return null;
        List<String> frasesClave = Arrays.asList(
            "has stopped responding",
            "rebooted",
            "is responding again"
        );
        String mensajeLimpio = message;
        for(String frase: frasesClave){
            int idx = mensajeLimpio.toLowerCase().indexOf(frase.toLowerCase());
            if(idx != -1){
                mensajeLimpio = mensajeLimpio.substring(0, idx).trim();
                break;
            }
        }
        return mensajeLimpio.trim();
    }
    public static List<Map<String, String>> preprocesarDatos(List<Map<String, String>> datos) {
        List<Map<String, String>> filtrados = new ArrayList<>();

        for (Map<String, String> fila : datos) {
            String mensaje = fila.get("Message");
            String proveedor = extraerProveedor(mensaje);

            if (proveedor != null) {
                fila.put("Proveedor", proveedor);
                String agenciaBase = extraerAgenciaBase(mensaje);
                fila.put("Agencia_base", agenciaBase);
                filtrados.add(fila);
            }
        }
        return filtrados;
    }
    //Crear un diccionario con fechas de reboot por agencia, redondeadas al minuto.
    public static Map<String, Set<LocalDateTime>> construirDiccionarioReboots(List<Map<String, String>> datos) {
        Map<String, Set<LocalDateTime>> reboots = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Map<String, String> fila : datos) {
            String tipoEvento = fila.get("EventTypeName");
            if (tipoEvento != null && tipoEvento.toLowerCase().contains("reboot")) {
                String agencia = fila.get("Agencia_base");
                String fechaStr = fila.get("EventTime");
                if (agencia != null && fechaStr != null && !fechaStr.trim().isEmpty()) {
                    fechaStr = fechaStr.trim();  // eliminar espacios invisibles
                    try {
                        LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatter);
                        reboots.putIfAbsent(agencia, new HashSet<>());
                        reboots.get(agencia).add(fecha);
                    } catch (DateTimeParseException e) {
                        System.err.println("❌ No se pudo parsear la fecha: '" + fechaStr + "'");
                    }
                }
                
            }
        }
        return reboots;
    }
    //Verifica si hay un reboot dentro de ±2 minutos del evento UP.
    public static boolean hayRebootCercano(String agencia, LocalDateTime fechaUp,Map<String, Set<LocalDateTime>> rebootsPorAgencia){
        if (agencia ==null || fechaUp == null || !rebootsPorAgencia.containsKey(agencia)){
            return false;
        }
        //Redondear a minuto
        LocalDateTime fechaUpRed= fechaUp.withSecond(0).withNano(0);
        Set<LocalDateTime> fechasReboot = rebootsPorAgencia.get(agencia);
        //Revisar si hay un reboot entre -2 y +2 minutos
        for(int i = -2; i<=2;i++){
            LocalDateTime candidato = fechaUpRed.plusMinutes(i);
            if(fechasReboot.contains(candidato)){
                return true;
            }
        }
        return false;
    }
    public static List<Map<String, Object>> analizarEventos(List<Map<String, String>> datos) {
        List<Map<String, Object>> resultado = new ArrayList<>();
        Map<String, Set<LocalDateTime>> rebootsPorAgencia = construirDiccionarioReboots(datos);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String, List<Map<String, String>>> agrupadoPorAgencia = new HashMap<>();
        for (Map<String, String> fila : datos) {
            String agencia = fila.get("Agencia_base");
            agrupadoPorAgencia.computeIfAbsent(agencia, k -> new ArrayList<>()).add(fila);
        }

        for (Map.Entry<String, List<Map<String, String>>> entry : agrupadoPorAgencia.entrySet()) {
            String agencia = entry.getKey();
            List<Map<String, String>> eventos = entry.getValue();
            eventos.sort(Comparator.comparing(e -> LocalDateTime.parse(e.get("EventTime").trim(), formatter)));

            LocalDateTime fechaDown = null;

            for (Map<String, String> fila : eventos) {
                String evento = fila.get("EventTypeName").toLowerCase();
                LocalDateTime fechaEvento = LocalDateTime.parse(fila.get("EventTime").trim(), formatter);

                if (evento.contains("down")) {
                    if (fechaDown == null) {
                        fechaDown = fechaEvento;
                    } else {
                        Map<String, Object> incidente = new HashMap<>();
                        incidente.put("Enlace", agencia);
                        incidente.put("Fecha Down", fechaDown);
                        incidente.put("Fecha Up", null);
                        incidente.put("Tiempo", null);
                        incidente.put("Estado", "Caído");
                        incidente.put("Agencia_base", fila.get("Agencia_base"));
                        incidente.put("Proveedor", fila.get("Proveedor"));
                        resultado.add(incidente);
                        fechaDown = fechaEvento;
                    }
                } else if (evento.contains("up") && fechaDown != null) {
                    LocalDateTime fechaUp = fechaEvento;
                    boolean hayReboot = hayRebootCercano(agencia, fechaUp, rebootsPorAgencia);
                    String estado = hayReboot ? "Reboot" : "Caído y recuperado";
                    long minutos =  Duration.between(fechaDown, fechaUp).toMinutes();

                    Map<String, Object> incidente = new HashMap<>();
                    incidente.put("Enlace", agencia);
                    incidente.put("Fecha Down", fechaDown);
                    incidente.put("Fecha Up", fechaUp);
                    incidente.put("Tiempo", minutos);
                    incidente.put("Estado", estado);
                    incidente.put("Agencia_base", fila.get("Agencia_base"));
                    incidente.put("Proveedor", fila.get("Proveedor"));
                    resultado.add(incidente);

                    fechaDown = null;
                }
            }

            if (fechaDown != null) {
                Map<String, Object> incidente = new HashMap<>();
                incidente.put("Enlace", agencia);
                incidente.put("Fecha Down", fechaDown);
                incidente.put("Fecha Up", null);
                incidente.put("Tiempo", null);
                incidente.put("Estado", "Caído");
                incidente.put("Agencia_base", eventos.get(eventos.size() - 1).get("Agencia_base"));
                incidente.put("Proveedor", eventos.get(eventos.size() - 1).get("Proveedor"));
                resultado.add(incidente);
            }
        }
        return resultado;
    }
    public static List<Map<String, Object>> corregirEstadosReboot(List<Map<String, Object>> datos, int timeMarginMinutes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        List<Map<String, Object>> ajustado = new ArrayList<>(datos);
        Map<String, List<Map<String, Object>>> agrupado = new HashMap<>();

        // ⚙️ Limpiar "Agencia_base" y reagrupar
        for (Map<String, Object> fila : ajustado) {
            String agenciaBase = (String) fila.get("Agencia_base");
            if (agenciaBase == null) continue;

            // 🔁 Normalizar la agencia base: quitar proveedor y palabras extra
            String[] partes = agenciaBase.trim().split(" ");
            if (partes.length > 1) {
                agenciaBase = String.join(" ", Arrays.copyOf(partes, partes.length - 1)).trim();
            }
            agenciaBase = agenciaBase.replace("Principal", "").replace("Backup", "").trim();

            // Reasignar la agencia normalizada
            fila.put("Agencia_base", agenciaBase);
            agrupado.computeIfAbsent(agenciaBase, k -> new ArrayList<>()).add(fila);
        }

        for (String agencia : agrupado.keySet()) {
            List<Map<String, Object>> grupo = agrupado.get(agencia);

            // 🧠 Verificar enlaces principales con estado Reboot
            for (Map<String, Object> principal : grupo) {
                String enlace = (String) principal.get("Enlace");
                if ("Reboot".equals(principal.get("Estado")) && enlace.toLowerCase().contains("principal")) {
                    LocalDateTime downP = (LocalDateTime) principal.get("Fecha Down");
                    LocalDateTime upP = (LocalDateTime) principal.get("Fecha Up");

                    for (Map<String, Object> backup : grupo) {
                        String enlaceBackup = (String) backup.get("Enlace");
                        if (enlaceBackup != null && enlaceBackup.toLowerCase().contains("backup") &&
                                "Caído y recuperado".equals(backup.get("Estado"))) {

                            LocalDateTime downB = (LocalDateTime) backup.get("Fecha Down");
                            LocalDateTime upB = (LocalDateTime) backup.get("Fecha Up");

                            if (downB != null && upB != null && downP != null && upP != null) {
                                boolean matchDown = Math.abs(ChronoUnit.MINUTES.between(downP, downB)) <= timeMarginMinutes;
                                boolean matchUp = Math.abs(ChronoUnit.MINUTES.between(upP, upB)) <= timeMarginMinutes;

                                if (matchDown && matchUp) {
                                    backup.put("Estado", "Reboot");
                                }
                            }
                        }
                    }
                }
            }

            // 🧠 Comparar entre distintos proveedores
            Set<String> proveedores = new HashSet<>();
            for (Map<String, Object> fila : grupo) {
                proveedores.add((String) fila.get("Proveedor"));
            }

            if (proveedores.size() > 1) {
                for (Map<String, Object> fila : grupo) {
                    LocalDateTime fd = (LocalDateTime) fila.get("Fecha Down");
                    LocalDateTime fu = (LocalDateTime) fila.get("Fecha Up");
                    String proveedor = (String) fila.get("Proveedor");

                    for (Map<String, Object> otra : grupo) {
                        if (!proveedor.equals(otra.get("Proveedor")) && "Reboot".equals(otra.get("Estado"))) {
                            LocalDateTime fd2 = (LocalDateTime) otra.get("Fecha Down");
                            LocalDateTime fu2 = (LocalDateTime) otra.get("Fecha Up");

                            if (fd != null && fu != null && fd2 != null && fu2 != null) {
                                boolean matchDown = Math.abs(ChronoUnit.MINUTES.between(fd, fd2)) <= timeMarginMinutes;
                                boolean matchUp = Math.abs(ChronoUnit.MINUTES.between(fu, fu2)) <= timeMarginMinutes;

                                if (matchDown && matchUp && !"Reboot".equals(fila.get("Estado"))) {
                                    fila.put("Estado", "Reboot");
                                }
                            }
                        }
                    }
                }
            }
        }

        return ajustado;
    }

    public static void generarHojasDia(List<Map<String, Object>> datos, Workbook workbook, LocalDate fechaInicio, LocalDate fechaFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDate fechaActual = fechaInicio;
        while (!fechaActual.isAfter(fechaFin)) {
            LocalDateTime inicioDia = LocalDateTime.of(fechaActual, LocalTime.of(8, 0));
            LocalDateTime finDia = LocalDateTime.of(fechaActual, LocalTime.of(20, 0));

            List<Map<String, Object>> registrosDia = new ArrayList<>();
            for (Map<String, Object> fila : datos) {
                LocalDateTime fechaDown = (LocalDateTime) fila.get("Fecha Down");
                if (fechaDown != null && !fechaDown.isBefore(inicioDia) && fechaDown.isBefore(finDia)) {
                    registrosDia.add(fila);
                }
            }

            if (!registrosDia.isEmpty()) {
                registrosDia.sort(Comparator.comparing(f -> (LocalDateTime) f.get("Fecha Down")));
                

                String nombreHoja = String.format("%02d_dia", fechaActual.getDayOfMonth());
                Sheet sheet = workbook.createSheet(nombreHoja);

                // 👉 Crear estilo de cabecera
                CellStyle estiloCabecera = workbook.createCellStyle();
                Font fuenteNegrita = workbook.createFont();
                fuenteNegrita.setBold(true);
                estiloCabecera.setFont(fuenteNegrita);
                estiloCabecera.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                estiloCabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                estiloCabecera.setAlignment(HorizontalAlignment.CENTER);

                // 👉 Encabezados con estilo
                Row header = sheet.createRow(0);
                String[] columnas = {"Enlace", "Fecha Down", "Fecha Up", "Tiempo", "Estado", "Agencia_base", "Proveedor"};
                for (int i = 0; i < columnas.length; i++) {
                    Cell celda = header.createCell(i);
                    celda.setCellValue(columnas[i]);
                    celda.setCellStyle(estiloCabecera);  // aplicar estilo
                }
                CreationHelper creationHelper = workbook.getCreationHelper();
                CellStyle dateCellStyle = workbook.createCellStyle();
                short dateFormat = creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss");
                dateCellStyle.setDataFormat(dateFormat);


                // Datos
                int rowNum = 1;
                for (Map<String, Object> fila : registrosDia) {
                    Row row = sheet.createRow(rowNum++);
                    for (int i = 0; i < columnas.length; i++) {
                        Object valor = fila.get(columnas[i]);
                        if (valor instanceof LocalDateTime) {
                            //row.createCell(i).setCellValue(((LocalDateTime) valor).format(formatter));
                            LocalDateTime ldt = (LocalDateTime) valor;
                            Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                            Cell cell = row.createCell(i);
                            cell.setCellValue(date);
                            cell.setCellStyle(dateCellStyle);

                        }else if(valor instanceof Number){
                            row.createCell(i).setCellValue(((Number) valor).doubleValue());
                        } 
                        else if (valor != null) {
                            row.createCell(i).setCellValue(valor.toString());
                        } else {
                            row.createCell(i).setCellValue("");
                        }
                    }
                }
                for(int i=0; i<columnas.length; i++){
                    sheet.autoSizeColumn(i);
                }
            }
            fechaActual = fechaActual.plusDays(1);
        }
    }
    public static void generarHojasMadrugada(List<Map<String, Object>> datos, Workbook workbook, LocalDate fechaInicio, LocalDate fechaFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDate fechaActual = fechaInicio;
        while (!fechaActual.isAfter(fechaFin)) {
            LocalDateTime inicioMadrugada = LocalDateTime.of(fechaActual, LocalTime.of(20, 0));
            LocalDateTime finMadrugada = inicioMadrugada.plusHours(12); // hasta las 08h00 del siguiente día

            List<Map<String, Object>> registrosMadrugada = new ArrayList<>();
            for (Map<String, Object> fila : datos) {
                LocalDateTime fechaDown = (LocalDateTime) fila.get("Fecha Down");
                if (fechaDown != null && !fechaDown.isBefore(inicioMadrugada) && fechaDown.isBefore(finMadrugada)) {
                    registrosMadrugada.add(fila);
                }
            }

            if (!registrosMadrugada.isEmpty()) {
                registrosMadrugada.sort(Comparator.comparing(f -> (LocalDateTime) f.get("Fecha Down")));
                String nombreHoja = String.format("%02d-%02d_Madrugada", fechaActual.getDayOfMonth(), fechaActual.plusDays(1).getDayOfMonth());
                Sheet sheet = workbook.createSheet(nombreHoja);

                // 👉 Crear estilo de cabecera
                CellStyle estiloCabecera = workbook.createCellStyle();
                Font fuenteNegrita = workbook.createFont();
                fuenteNegrita.setBold(true);
                estiloCabecera.setFont(fuenteNegrita);
                estiloCabecera.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                estiloCabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                estiloCabecera.setAlignment(HorizontalAlignment.CENTER);

                // 👉 Encabezados con estilo
                Row header = sheet.createRow(0);
                String[] columnas = {"Enlace", "Fecha Down", "Fecha Up", "Tiempo", "Estado", "Agencia_base", "Proveedor"};
                for (int i = 0; i < columnas.length; i++) {
                    Cell celda = header.createCell(i);
                    celda.setCellValue(columnas[i]);
                    celda.setCellStyle(estiloCabecera);  // aplicar estilo
                }
                CreationHelper creationHelper = workbook.getCreationHelper();
                CellStyle dateCellStyle = workbook.createCellStyle();
                short dateFormat = creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss");
                dateCellStyle.setDataFormat(dateFormat);

                // Datos
                int rowNum = 1;
                for (Map<String, Object> fila : registrosMadrugada) {
                    Row row = sheet.createRow(rowNum++);
                    for (int i = 0; i < columnas.length; i++) {
                        Object valor = fila.get(columnas[i]);
                        if (valor instanceof LocalDateTime) {
                            LocalDateTime ldt = (LocalDateTime) valor;
                            Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                            Cell cell = row.createCell(i);
                            cell.setCellValue(date);
                            cell.setCellStyle(dateCellStyle);
                        }else if(valor instanceof Number){
                            row.createCell(i).setCellValue(((Number) valor).doubleValue());
                        } else if (valor != null) {
                            row.createCell(i).setCellValue(valor.toString());
                        } else {
                            row.createCell(i).setCellValue("");
                        }
                    }
                }
                for(int i=0; i<columnas.length; i++){
                    sheet.autoSizeColumn(i);
                }
            }
            fechaActual = fechaActual.plusDays(1);
        }
    }
    public static void rangoReporteDia(List<Map<String, Object>> datos) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\uD83D\uDCC5 Ingresa la fecha de inicio (dd/mm/yyyy): ");
        String fechaInicioStr = scanner.nextLine();
        System.out.print("\uD83D\uDCC5 Ingresa la fecha de fin (dd/mm/yyyy): ");
        String fechaFinStr = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
        LocalDate fechaFin = LocalDate.parse(fechaFinStr, formatter);

        String nombreArchivo = String.format("C:\\Users\\ggcas\\Documents\\git\\eventos_orion\\files\\%s_%s_Dia.xlsx", fechaInicio.format(DateTimeFormatter.ofPattern("yyyyMMdd")), fechaFin.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        try (FileOutputStream fos = new FileOutputStream(nombreArchivo); Workbook workbook = new XSSFWorkbook()) {
            Sheet hojaTotal = workbook.createSheet("Incidentes Total");
            String[] columnas = {"Enlace", "Fecha Down", "Fecha Up", "Tiempo", "Estado", "Agencia_base", "Proveedor"};
            Row header = hojaTotal.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            DateTimeFormatter fechaHoraFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            CreationHelper creationHelper = workbook.getCreationHelper();
            CellStyle dateCellStyle = workbook.createCellStyle();
            short dateFormat = creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss");
            dateCellStyle.setDataFormat(dateFormat);
            for (Map<String, Object> fila : datos) {
                Row row = hojaTotal.createRow(rowNum++);
                for (int i = 0; i < columnas.length; i++) {
                    Object valor = fila.get(columnas[i]);
                    if (valor instanceof LocalDateTime) {
                        //row.createCell(i).setCellValue(((LocalDateTime) valor).format(fechaHoraFormatter));
                        LocalDateTime ldt = (LocalDateTime) valor;
                        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                        Cell cell = row.createCell(i);
                        cell.setCellValue(date);
                        cell.setCellStyle(dateCellStyle);
                    }else if(valor instanceof Number){
                            row.createCell(i).setCellValue(((Number) valor).doubleValue());
                    } 
                    else if (valor != null) {
                        row.createCell(i).setCellValue(valor.toString());
                    } else {
                        row.createCell(i).setCellValue("");
                    }
                }
            }

            generarHojasDia(datos, workbook, fechaInicio, fechaFin);
            workbook.write(fos);
            System.out.println("✅ Archivo generado exitosamente: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("❌ Error al escribir el archivo: " + e.getMessage());
        }
    }
    public static void rangoReporteMadrugada(List<Map<String, Object>> datos) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterArchivo = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            System.out.print("📅 Ingresa la fecha de inicio (dd/mm/yyyy): ");
            String inicioStr = scanner.nextLine().trim();
            System.out.print("📅 Ingresa la fecha de fin (dd/mm/yyyy): ");
            String finStr = scanner.nextLine().trim();

            LocalDate fechaInicio = LocalDate.parse(inicioStr, formatterInput);
            LocalDate fechaFin = LocalDate.parse(finStr, formatterInput);

            String nombreArchivo = "C:\\Users\\ggcas\\Documents\\git\\eventos_orion\\files\\" + fechaInicio.format(formatterArchivo) + "_" + fechaFin.format(formatterArchivo) + "_Madrugada.xlsx";

            try (Workbook workbook = new XSSFWorkbook(); FileOutputStream out = new FileOutputStream(nombreArchivo)) {
                // Hoja total
                Sheet totalSheet = workbook.createSheet("Incidentes Total");

                String[] columnas = {"Enlace", "Fecha Down", "Fecha Up", "Tiempo", "Estado", "Agencia_base", "Proveedor"};
                Row header = totalSheet.createRow(0);
                for (int i = 0; i < columnas.length; i++) {
                    header.createCell(i).setCellValue(columnas[i]);
                }
                CreationHelper creationHelper = workbook.getCreationHelper();
                CellStyle dateCellStyle = workbook.createCellStyle();
                short dateFormat = creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss");
                dateCellStyle.setDataFormat(dateFormat);

                // Escribir datos
                int rowNum = 1;
                for (Map<String, Object> fila : datos) {
                    Row row = totalSheet.createRow(rowNum++);
                    for (int i = 0; i < columnas.length; i++) {
                        Object valor = fila.get(columnas[i]);
                        if (valor instanceof LocalDateTime) {
                            //row.createCell(i).setCellValue(((LocalDateTime) valor).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                            LocalDateTime ldt = (LocalDateTime) valor;
                            Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                            Cell cell = row.createCell(i);
                            cell.setCellValue(date);
                            cell.setCellStyle(dateCellStyle);
                        }else if(valor instanceof Number){
                            row.createCell(i).setCellValue(((Number) valor).doubleValue());
                        }  
                        else if (valor != null) {
                            row.createCell(i).setCellValue(valor.toString());
                        } else {
                            row.createCell(i).setCellValue("");
                        }
                    }
                }
                for(int i=0; i<columnas.length; i++){
                    totalSheet.autoSizeColumn(i);
                }
                // Hojas por madrugada
                generarHojasMadrugada(datos, workbook, fechaInicio, fechaFin);

                workbook.write(out);
                System.out.println("✅ Archivo generado exitosamente: " + nombreArchivo);

            } catch (IOException e) {
                System.err.println("❌ Error al escribir el archivo: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("❌ Formato de fecha incorrecto. Usa dd/mm/yyyy.");
        }
    }
    
}