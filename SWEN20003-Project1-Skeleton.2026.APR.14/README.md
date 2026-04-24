# SWEN20003 Semester 1, 2026
# Project 1
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

### OOP
#### 1.Inheritance
* File: game/ScreenState.java (lines 8-18) and game/BattleScreen.java (lines 8-210)BattleScreen and PauseScreen inherit from the abstract base class ScreenState. This reuse the gameProps field and enforces implementation of update() and draw() methods, avoiding redundant property initialization code across screen classes.
#### 2.Advanced: Polymorphism
* File: game/ShadowAliens.java (lines 47-50, 58-64) currentScreenState uses polymorphism by referencing ScreenState, but holding either BattleScreen or PauseScreen. At runtime, update(input) is dynamically dispatched to the correct overridden method, allowing different behaviours without changing the call site.
### Design choice
The main design choice in this project is using delegation together with a simple inheritance structure to keep the code organised. Instead of putting everything into one large class, different components handle their own responsibilities: BattleScreen manages gameplay, UserInterface draws UI settings, and each game object updates itself. This makes the code easier to read and modify. I also used protected fields so subclasses can access shared data without needing many getters. An alternative would be putting all logic in a single class or using only private fields with heavy getter usage, but that would make the code more complex and harder to maintain. Overall, this design keeps the structure clear and easier to extend.
## Design Report References
* None