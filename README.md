# decision-tree-java — Árbol de decisión con entropía e information gain

Como parte del curso de inteligencia artificial en SENATI, desarrollé un árbol de decisión que determina si recomendar o no una película en base a distintos atributos. Calculando la entropía y la ganancia de información de cada variable, se construye un árbol recursivo que mide qué tan útil es cada atributo para clasificar los datos — implementado desde cero en Java, sin depender de librerías externas de ML.

El caso de uso está basado en **StreamFlix**, una plataforma ficticia que busca recomendar películas según características como popularidad del director, género y presupuesto.

---

## ¿qué hace el proyecto?

La aplicación web muestra paso a paso el proceso completo del algoritmo ID3:

1. **Entropía del conjunto total** — calcula la incertidumbre inicial del dataset (`peliculaR.arff`, 30 películas).
2. **Ganancia de información por variable** — evalúa qué tan útil es cada atributo (`director_popularity`, `budget`, `genre`) para separar los datos.
3. **Explicación de entropía = 0** — interpreta los subconjuntos puros encontrados durante el análisis.
4. **Recomendación por árbol de decisión recursivo** — recorre el árbol construido y devuelve una predicción final: ¿recomendar la película o no?

---

## tecnologías utilizadas

- **Java 23**
- **Vaadin** — interfaz web
- **Maven** — gestión de dependencias y build
- **Spring Boot** — servidor embebido
- **Formato ARFF** — estructura de datos compatible con Weka

---

## cómo correrlo

### 1. clona el repositorio

```bash
git clone https://github.com/1643083/decision-tree-java.git
cd decision-tree-java
```

### 2. asegúrate de tener Java 23 instalado

```bash
java -version
```

### 3. corre el proyecto

```bash
./mvnw spring-boot:run
```

En Windows:
```bash
mvnw spring-boot:run
```

### 4. abre en el navegador

```
http://localhost:8080
```

---

## estructura del proyecto

```
decision-tree-java/
├── src/
│   └── main/
│       ├── java/
│       │   ├── Application.java          # Punto de entrada
│       │   ├── EntropyCalculator.java    # Lógica de entropía e information gain
│       │   └── MainView.java             # Interfaz Vaadin
│       └── resources/
│           └── peliculaR.arff            # Dataset de películas
├── pom.xml
├── mvnw
├── .gitignore
└── README.md
```

---

## conceptos aplicados

- **Entropía (H):** mide la impureza o incertidumbre de un conjunto de datos. Un valor de 0 indica un subconjunto completamente puro (todas las instancias pertenecen a la misma clase).
- **Ganancia de información (GI):** mide cuánto reduce la entropía dividir el dataset según un atributo. El atributo con mayor ganancia se elige como raíz del árbol.
- **Algoritmo ID3:** construye el árbol de decisión de forma recursiva, eligiendo en cada nodo el atributo de mayor ganancia hasta que todos los subconjuntos sean puros o no queden atributos.

---

## contexto académico

Proyecto desarrollado para el curso de **Inteligencia Artificial** en SENATI, como parte de la carrera de Ingeniería de Software con IA.
