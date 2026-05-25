package game;

import bagel.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Shooting enemy: moves straight down like a regular enemy, but fires projectiles
 * at a fixed interval. The first shot occurs at arrivalTime + firingRate frames,
 * with subsequent shots every firingRate frames thereafter.
 */
public class ShootingEnemy extends Enemy {

    private final int firingRate;
    private final Image projectileImage;
    private final double projectileSpeed;
    private final double screenHeight;

    private double framesSinceArrival;

    private final List<EnemyProjectile> pendingProjectiles;

    public ShootingEnemy(Image image, double x, double movementSpeed, double screenHeight,
                         int firingRate, Image projectileImage, double projectileSpeed) {
        super(image, x, movementSpeed, screenHeight);
        this.firingRate = firingRate;
        this.projectileImage = projectileImage;
        this.projectileSpeed = projectileSpeed;
        this.screenHeight = screenHeight;
        this.framesSinceArrival = 0;
        this.pendingProjectiles = new ArrayList<>();
    }

    @Override
    public void update(double timeScale) {
        if (!active) return;

        framesSinceArrival += timeScale;


        double prev = framesSinceArrival - timeScale;
        double curr = framesSinceArrival;


        long prevCount = (long) (prev / firingRate);
        long currCount = (long) (curr / firingRate);

        if (currCount > prevCount && curr >= firingRate) {
            pendingProjectiles.add(new EnemyProjectile(projectileImage, x, y, projectileSpeed, screenHeight)
            );
        }

        super.update(timeScale);
    }


    public List<EnemyProjectile> collectProjectiles() {
        List<EnemyProjectile> fired = new ArrayList<>(pendingProjectiles);
        pendingProjectiles.clear();
        return fired;
    }
}
