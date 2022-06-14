# Proyecto3 | Estructura de Datos | Simulador de Apuestas

---
## Ricardo Emiliano Apodaca Cardiel <span style="font-family:monospace">422029455
   Laura Itzel Rodríguez Dimayuga <span style="font-family:monospace">422013628
---
Este proyecto fue hecho en Java 11 y [Kotlin](https://kotlinlang.org/ "Kotlin" ) con
[Gradle](https://gradle.org/ "Gradle").
---

### Construcción y ejecución 

_Nota : La primera vez tarda ya que tiene que descargar varias cosas_
   
Se recomienda [instalar Gradle](https://gradle.org/install/ "Instalar Gradle") (si no se tiene instalado gradle los comandos pueden ser sustituidos con ./gradlew):

Para construir (es importante tener java 11): 

```bash
gradle build
```
```bash
./gradlew build
```
También es posible construir el proyecto y ejecutarlo a la vez con(Recomendado):

```bash
gradle run
```
```bash
./gradlew run
```
```bash
gradle runDistributable
```
```bash
./gradlew runDistributable
```

O se puede formar el ejecutable a partir de los siguientes comandas dependiendo de su 
sistema operativo

```bash
gradle package
```
```bash
gradle packageDeb
```
```bash
gradle packageDmg
```
```bash
gradle packageMsi
```

```bash
gradle packageRpm
```
### Uso de la aplicación

Al correr el programa se abrirá una ventana para iniciar sesión, crear usuario o entrar como modo 
invitado. Al entrar como modo invitado se generan automáticamente $1000 de saldo inicial genera un historial 
vacío. 

Al crear un usuario la contraseña es hasheada son SHA 512 utilizando la librería de [Guava](https://guava.dev/) 
por lo que la contraseña del usuario nunca se guarda directamente. EL historial y la información del usuario se guardan
en un objeto serializable. 

***Aclaración***: La información del usuario se guarda en la carpeta con el mismo nombre. Pero si se corre como 
una aplicación nativa es uuy probable que no se cree una carpeta, genere un error y cierre el programa. 

Al entrar a la aplicación se tienen dos filas. En la parte superior podemos apostar para las carreras, mientras 
que en la parte inferior podemos apostar por los torneos. 

### Carrera 

Para apostar a alguna carrera escribimos la cantidad que queremos apostar y un número del 1 al 6 
representado al corredor al que apostamos. Con el botón de consulta podemos consultar el historial 
de todas las carreras pasadas. El tiempo entre cada carrera es de 10 segundos. 

### Torneo

En el torneo tenemos 16 o 32 candidatos de manera aleatoria cada que se inicializa un nuevo torneo. 
Para apostar por un candidato podemos consultar su habilidad y cuota en la parte inferior izquierda.
También podemos consultar la última apuesta y el último ganador. Cada partida se inizializa cada 
15 segundos.

### Probabilidades 
La probabilidad de que gane o no un candidato si respeta las probabilidades de ganar de lso candidatos. 
Se genera un float random entre (0 .. 1) y si la probabilidad es menor que la probabilidad del
ganar del candidato con menor probabilidad este gana, si no gana el candidato con mayor probabilidad.
Por ejemplo si un candidato A tiene 30% de probabilidades de ganar, si el float random es menor a 0.30 gana 
el candidato A y si es mayor gana el candidato B. Notemos que las probabilidades de los dos candidatos suman
100 por lo que el candidato con menor probabilidad siempre es menor o igual a 50, respetando las probabilidades de
ganar de cada candidato. 

### Usuario 

Podemos consultar nuestro saldo haciendo clic en el icono de la barra lateral, también 
podemos consultar en el historial de cada movimiento, el monto realizado, el tipo de apuesta, y si se ganó o perdió.
También podemos agregar saldo. Para guardar la información del usuario, es necesario 
salir de la ventana de apuestas dando clic en la **x**. 

### Hilos 

En vez de utilizar hilos, utilizamos algo muy parecido que son las corrutinas esas son las funcionas 
que empiezan con suspend y estas son lanzadas en launchEffect aunque hay muchas otras maneras de
utilizarlas. Estas las usamos en el Torneo y en la Carrera. 

### Interfaz grafica

Para la interfaz gráfica ocupamos la librería de [Compose](https://www.jetbrains.com/lp/compose-desktop/)
