## PaymentRole

Aplicación de escritorio desarrollada en Java y JavaFX para **gestionar el rol de pagos de empleados**.  
Permite cargar datos de empleados, realizar cálculos de salario, ordenar y filtrar información, y exportar resultados.

---

### Captura de pantalla

![PaymentRole - Vista principal](<img width="599" height="427" alt="Screenshot 2026-03-04 at 9 25 16 AM" src="https://github.com/user-attachments/assets/af9074c9-df49-4ee5-9ff4-be24ffda3f5d" />)

---

### Estructura del proyecto

Desde la carpeta `PaymentRole`:

- **`src/`**: Código fuente de la aplicación.
  - **`src/PaymentRole.java`**: Clase principal que inicia la aplicación JavaFX.
  - **`src/controller/`**: Controladores y lógica de la interfaz.
    - `Controller.java`: Controlador principal de la vista.
    - `CalcManager.java`: Manejo de cálculos de salarios.
    - `SortManager.java`: Lógica de ordenamiento de empleados.
    - `ExportManager.java`: Exportación de datos.
    - `ConsoleManager.java`: Manejo de la salida en consola.
    - `AlertManager.java`: Manejo de alertas y diálogos.
    - `GUIObservers.java`: Observadores para sincronizar la interfaz.
  - **`src/model/`**: Clases de dominio / modelo.
    - `Employee.java`: Representa a un empleado.
    - `Manager.java`: Representa a un gerente (extiende o compone `Employee`).
    - `EmployeeList.java`: Colección y manejo de empleados.
    - `EmployeeComparator.java`: Comparadores para ordenar empleados.
  - (Opcional) **archivos `.fxml`** para las vistas, por ejemplo `PaymentRole.fxml`, ubicados en el mismo paquete que `PaymentRole.java` o en la carpeta de recursos correspondiente.
- **`bin/`**: Clases compiladas (`.class`) generadas por `javac`.
- **`lib/`**: Bibliotecas externas necesarias para correr la aplicación.
  - JavaFX (`javafx.base.jar`, `javafx.controls.jar`, `javafx.fxml.jar`, `javafx.graphics.jar`, etc.)
  - JAXB (`jaxb-api.jar`, `jaxb-runtime.jar`, etc.)
  - JSON Simple (`json-simple-2.3.0.jar`)
- **`employeesData.csv` / `EmployeeData.csv`**: Archivos CSV con los datos de empleados.
- **`README.md`**: Este archivo de documentación.

---

### Requisitos previos

- **Java JDK 17+** (probado con `javac 23.0.1`).
- **JavaFX SDK 23.0.2** (o compatible).
- Bibliotecas adicionales (si no usas las incluidas en `lib/`):
  - JAXB RI
  - json-simple

> Nota: El proyecto ya incluye las bibliotecas necesarias en la carpeta `lib/`, por lo que no es obligatorio instalarlas por separado.

---

### Cómo compilar y ejecutar la aplicación

Todas las instrucciones asumen que estás en la carpeta:

```bash
/Users/raymondportilla/Desktop/College/4th_Semester/Apps Programming/Final Project/PaymentRole
```

#### 1. Compilar

```bash
cd "/Users/raymondportilla/Desktop/College/4th_Semester/Apps Programming/Final Project/PaymentRole"

javac \
  -d bin \
  --module-path lib \
  --add-modules javafx.controls,javafx.fxml \
  -cp "lib/*" \
  $(find src -name "*.java")
```

- **`-d bin`**: coloca los `.class` en la carpeta `bin`.
- **`--module-path lib` y `--add-modules javafx.controls,javafx.fxml`**: activan JavaFX usando los JAR de `lib`.
- **`-cp "lib/*"`**: agrega todas las dependencias adicionales (JAXB, json-simple, etc.) al classpath.
- **`$(find src -name "*.java")`**: compila todos los archivos `.java` del proyecto.

#### 2. Ejecutar

Desde la misma carpeta `PaymentRole`:

```bash
java \
  --module-path lib \
  --add-modules javafx.controls,javafx.fxml \
  -cp "bin:lib/*" \
  PaymentRole
```

- `PaymentRole` es la clase principal (`public class PaymentRole extends Application`).
- Si más adelante mueves la clase a un paquete (por ejemplo `app.PaymentRole`), deberás usar el nombre completamente calificado.

---

### Notas adicionales

- Asegúrate de que `PaymentRole.fxml` esté accesible desde `PaymentRole.java`:
  - El código usa:  
    ```java
    FXMLLoader.load(getClass().getResource("PaymentRole.fxml"));
    ```
  - Esto implica que `PaymentRole.fxml` debe estar en el **mismo paquete** que `PaymentRole.java` o correctamente configurado en el classpath de recursos.
- Si tienes problemas con JavaFX en macOS (error de runtime o ventana que no se abre), revisa que:
  - Estés usando el JDK correcto.
  - La ruta de `--module-path` apunte realmente a la carpeta donde están los JAR de JavaFX.
- Puedes adaptar los comandos de compilación/ejecución a un IDE (IntelliJ, VS Code, Eclipse) configurando:
  - El SDK de Java.
  - El módulo de JavaFX con `module-path` y `add-modules`.
