# EdR - Academic Management & Fraud Detection System (UBA)



This repository contains the full development of the **EdR (Examen de Resiliencia)** project for the Algorithms and Data Structures course at the **University of Buenos Aires (UBA)**. 



The project is divided into two main phases: **Formal Specification** and **High-Performance Implementation**.



## 📖 Project Phases



### 1. Formal Specification (TP1)

Before coding, a rigorous mathematical model was designed using **First-Order Logic**.

- **Objective:** Define types, invariants, and contracts (pre/post conditions) to ensure system consistency.

- **Key Document:** `docs/Informe_Especificacion_Formal.pdf` (Logic and TAD design).



### 2. Implementation & Complexity Optimization (TP2)

The system was implemented in **Java**, focusing on meeting strict computational complexity requirements ($O$ notation).

- **Min-Heap with Handles:** Custom implementation to manage student rankings in $O(\log E)$ time.

- **Fraud Detection:** Optimized algorithms to detect cheating patterns based on classroom proximity and response similarity.

- **Key Code:** `src/main/java/aed/Edr.java` and `Heap.java`.



## 🛠️ Tech Stack

- **Language:** Java

- **Testing:** JUnit (Complete test suite included).

- **Documentation:** LaTeX.

- **Tools:** Maven-style project structure.



## 📂 Repository Structure

- `src/main/java/aed/`: Core logic and Data Structures.

- `src/test/java/aed/`: Unit tests and edge case validations.

- `docs/`: 

  - `Enunciado_TP1.pdf`: Project requirements (Phase 1).

  - `Enunciado_TP2.pdf`: Project requirements (Phase 2).

  - `RTP1.pdf`: My technical report on Formal Specification.



## 📊 Computational Complexity Highlights

- **Cheating Detection:** $O(E \times R)$

- **Score Updates:** $O(\log E)$

- **Ranking Access:** $O(1)$



## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
