Assignment 3: Optimization of a City Transportation Network

Course: Design and Analysis of Algorithms (DAA)
Student: Abdimutalip Aidana
Group: SE-2419
University: Astana IT University

1. Objective

The main objective of this assignment is to optimize a city’s transportation network by constructing a Minimum Spanning Tree (MST) using two classical algorithms — Prim’s Algorithm and Kruskal’s Algorithm.
The goal is to determine the smallest possible total cost required to connect all city districts by roads while maintaining full connectivity between them.

2. Description of the Task

In this project, the city’s districts are represented as vertices of a weighted undirected graph, and the possible roads between them are represented as edges with construction costs as weights.
The task is to implement both algorithms, compare their performance, and analyze the efficiency of each approach based on:

The total cost of the MST,

Execution time in milliseconds,

The number of operations performed (comparisons, unions, and insertions).

The algorithms are tested using multiple datasets with different numbers of vertices and edges to observe how performance changes depending on graph size and density.

3. Project Structure

The project is divided into logical components to maintain clarity and modularity:

Algorithms: Implementation of Prim’s and Kruskal’s algorithms for MST computation.

Metrics: A utility for measuring performance metrics such as comparisons, unions, inserts, and total execution time.

Utils: Handles reading input graphs from JSON files and generating CSV reports with performance data.

Main: The entry point of the program that reads graph data, runs both algorithms, and writes results to the output JSON file.

Tests: Automated unit and performance tests that verify algorithm correctness and consistency of results.

4. Implementation Overview

Both algorithms were implemented in Java.

Prim’s Algorithm builds the MST incrementally, starting from one vertex and repeatedly selecting the smallest edge that connects a visited vertex with an unvisited one.

Kruskal’s Algorithm sorts all edges by weight and adds them one by one to the MST, skipping any that would create a cycle.

Each algorithm was enhanced with operation counters and time measurement to analyze performance.
The input graph data was provided in a structured JSON file, and the program outputs a new JSON file containing MST results for both algorithms. Additionally, a CSV file was generated for performance comparison.

5. Input and Output Data

Input: JSON file containing multiple graphs with vertex and edge information, including weights.

Output:

output.json — contains MST edges, total cost, operation counts, and execution times for both algorithms.

performance_results.csv — records time and operation comparisons for several graph sizes.

Each dataset includes small (4–6 vertices), medium (10–15 vertices), and large (20–30+ vertices) graphs to evaluate scalability.

6. Performance Analysis

Both algorithms produce MSTs with identical total costs, confirming their correctness.
However, performance differs depending on graph characteristics:

Prim’s Algorithm performs better on dense graphs, where most vertices are connected.

Kruskal’s Algorithm performs better on sparse graphs, as it efficiently handles fewer edges using the union-find data structure.
The time complexity results correspond with theoretical expectations:

Prim’s Algorithm: O(E log V)

Kruskal’s Algorithm: O(E log E)

The measured execution times and operation counts demonstrate these trends clearly in the CSV results.

7. Testing

Comprehensive unit tests were written to validate:

MST correctness (same total cost for both algorithms),

Proper edge count (V − 1 edges),

Absence of cycles,

Graph connectivity,

Non-negative execution times and consistent operation counts.

All tests passed successfully, confirming the stability and reliability of both implementations.

8. Observations

Both algorithms always produce MSTs with equal total weights.

Kruskal’s algorithm is simpler conceptually but requires sorting all edges before execution.

Prim’s algorithm is more efficient when using a priority queue on dense graphs.

The number of operations grows with the number of edges, but both remain linear in relation to graph size.

Execution time increases gradually with input size, showing that both implementations scale predictably.

9.References
https://algs4.cs.princeton.edu/43mst/KruskalMST.java.html
https://algs4.cs.princeton.edu/43mst/PrimMST.java.html

10. Conclusion

This project successfully demonstrates the construction of Minimum Spanning Trees using Prim’s and Kruskal’s algorithms.
Both algorithms are efficient, accurate, and consistent with theoretical analysis.
Empirical results confirm that:

Prim’s Algorithm is generally faster on dense networks.

Kruskal’s Algorithm performs better on sparse networks.

The modular design of the project, including separate classes for algorithms, metrics, and utilities, ensures clarity, reusability, and maintainability.
The combination of algorithmic analysis, performance measurement, and automated testing fulfills all assignment requirements.