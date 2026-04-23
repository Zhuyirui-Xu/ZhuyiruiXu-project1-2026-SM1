# SWEN20003 Semester 1, 2026
# Project 1
# Shadow Aliens

## Running Instructions
IntelliJ Run Configuration:
- Main class: game.ShadowAliens
- Working directory: project root folder (where gameData.properties is located)
- Use classpath of your project module

Command line (Java 25):
Compile:
javac game/*.java

Run:
java game.ShadowAliens

## Assumptions

* None

## AI Statement
I have not used any generative AI tools for code implementations
## Code References
* No code was copied from Stack Overflow, GitHub, or other external sources

## Design Report

### OOP
1.Inheritance
File: game/GameObject.java and all entity classes (Player, Enemy, etc.)
Player, Enemy, Projectile, and Explosion all inherit from GameObject. This lets them reuse drawing, collision, and destroy logic instead of repeating code.
2.Advanced: Polymorphism
File: game/GameState.java, BattleScreen.java, PauseScreen.java
BattleScreen and PauseScreen both follow the GameState interface. The main game can call update() and draw() without knowing which screen is active, making the code clean and flexible.
### Design choice
The most important design choice I made combines using the protected access modifier and applying delegation to keep the code clean and organised.
I used protected for core fields in GameObject and GameState so subclasses can safely access position and game properties without excessive getters, while still keeping them encapsulated.
I also used delegation heavily by letting dedicated classes handle specific jobs: UserInterface handles all HUD rendering, BattleScreen manages gameplay, and each entity runs its own update logic. This means no single class becomes too big or hard to maintain.
The alternative would be putting all logic in one main class or using only private fields with large numbers of getters. This would make code longer, harder to debug, and messy to extend.
Combining protected for safe inheritance and delegation for clear responsibility separation makes the code simpler, more maintainable, and more aligned with proper object-oriented design. This structure also makes it easy to extend the game later for Project 2.

## Design Report References

* None