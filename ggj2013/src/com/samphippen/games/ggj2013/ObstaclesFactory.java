package com.samphippen.games.ggj2013;

import com.badlogic.gdx.math.Vector2;
import com.samphippen.games.ggj2013.pathfind.ContinuousPathFinder;
import com.samphippen.games.ggj2013.quadtree.Quadtree;

public class ObstaclesFactory {

    private final Quadtree<GameObject> mWorldQuad;
    private final ContinuousPathFinder mCpf;

    private final float OBSTACLE_FIELD_SIZE = 12000.0f;
    private final float safeZoneSize = Constants.getFloat("safe_zone_size");

    public ObstaclesFactory(Quadtree<GameObject> worldQuad,
            ContinuousPathFinder cpf) {
        mWorldQuad = worldQuad;
        mCpf = cpf;
    }

    public void makeObstacles() {
        float density = Constants.getFloat("obstacle_density");
        for (int i = 0; i < (int) (density * OBSTACLE_FIELD_SIZE * OBSTACLE_FIELD_SIZE); i++) {
            Vector2 obstaclePosition = new Vector2();
            // trees don't spawn in safezone
            while (obstaclePosition.len() < safeZoneSize + 100) {
                obstaclePosition.set(GameServices.sRng.nextFloat()
                        * OBSTACLE_FIELD_SIZE - 0.5f * OBSTACLE_FIELD_SIZE,
                        GameServices.sRng.nextFloat() * OBSTACLE_FIELD_SIZE
                                - 0.5f * OBSTACLE_FIELD_SIZE);
            }
            ObstacleObject obstacle = new ObstacleObject(obstaclePosition);

            mWorldQuad.insert(obstaclePosition, obstacle);
            mCpf.addObstacle(GameServices.toPathFinder(obstaclePosition),
                    Constants.getFloat("obstacle_width") * 2);
        }
    }

    public void makeTreeRing(float firstCampfireAngle) {
        float spriteWidth = GameServices.loadSprite("tree.png").getWidth();
        float spriteHeight = GameServices.loadSprite("tree.png").getHeight();
        int treesInRing = (int) Constants
                .getFloat("number_of_trees_in_safezone_ring");
        for (int i = 0; i < treesInRing; i++) {
            firstCampfireAngle += 360 / (treesInRing + 1);
            double radAngle = Math.toRadians(firstCampfireAngle % 360);
            Vector2 obstaclePosition = new Vector2();
            float x = safeZoneSize * (float) Math.cos(radAngle);
            float y = safeZoneSize * (float) Math.sin(radAngle);
            obstaclePosition.set(x - spriteWidth / 2, y - spriteHeight / 2);
            ObstacleObject obstacle = new ObstacleObject(obstaclePosition);
            mWorldQuad.insert(obstaclePosition, obstacle);
        }
    }

}
