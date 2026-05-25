# SWEN20003 Semester 1, 2026
# Project 2
# Shadow Aliens

## Running Instructions
#### -IntelliJ Run Configuration
* **Main class:** game.ShadowAliens
* **Working directory:** Set to the root folder of the project which containing the gameData.properties file.
* **Classpath:** Use the classpath of your project’s main module
#### -Command Line (Java 25)
* **Compile all Java files:** javac -d bin game/*.java
* **Run the game:** java -cp bin game.ShadowAliens

## Assumptions
* None

## AI Statement
I have not used any generative AI tools or technologies for this assignment.

## Code References
* No code was copied from Stack Overflow, GitHub, or other external sources.
* Comments and code formatting follow the Google Java style guide: https://google.github.io/styleguide/javaguide.html

## Design Report

### Extension:
#### Enemy System
* In Project 1, enemies were a single type with identical straight-down movement and no unique behaviours. Project 2 extended this into three distinct enemy types: regular, strafing, and shooting, each with unique movement or combat logic. I changed the original Enemy class into an abstract class in order to share common behaviours like downward movement and boundary checks, then added three concrete subclasses. Besides, the Wave class was updated to parse type-based enemy data and include a factory method to create the correct enemy type. BattleScreen was modified to handle enemy projectiles and type-specific scoring. Additionally, the wave counter became a scaled double so enemy spawn times follow game speed. Project 1’s flat structure limited extensibility, but using inheritance enabled clean code reuse and easy behaviour extension, following the open‑closed principle.

### Outcome:
* The final enemy system is modular, maintainable, and highly extensible. The abstract Enemy hierarchy reuses core behaviour across all types, eliminating duplicate code. Each subclass implements only unique movement or combat logic, keeping code clear and concise. A factory method in Wave centralises enemy creation, so adding a new enemy requires only a new subclass and a small factory update which means no changes to core classes like BattleScreen. Polymorphism simplifies enemy updates by managing all types uniformly. The scaled frame counter ensures spawn timing automatically follows game speed. The design follows the single responsibility principle, with each class having a clear, focused role. And this structure keeps future extensions simple: new enemy types or behaviours can be added with minimal, isolated changes, reducing complexity and bug risks.
## Design Report References
* None