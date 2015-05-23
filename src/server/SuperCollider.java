package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import common.Entity;

public class SuperCollider {
    private static final int MAGIC_NUMBER = 12;

    public static void collide(List<Entity> objects) {
        HashMap<Entity, List<Entity>> collisions = new HashMap<>();
        recursiveCollide(collisions, objects);
    }

    private static void recursiveCollide(HashMap<Entity, List<Entity>> collisions, List<Entity> objects) {
        if (objects.size() < MAGIC_NUMBER) {
            dumbCollide(collisions, objects);
            return;
        }
        Position middle = findMiddle(objects);
        List<Entity> topLeft = new ArrayList<Entity>(objects.size() / 2);
        List<Entity> topRight = new ArrayList<Entity>(objects.size() / 2);
        List<Entity> bottomLeft = new ArrayList<Entity>(objects.size() / 2);
        List<Entity> bottomRight = new ArrayList<Entity>(objects.size() / 2);
        int doubled = 0;
        int numAdded = 0;
        for (Entity obj : objects) {
            numAdded = 0;
            Position pos = obj.getLocation();
            if (pos.x + obj.getr() > middle.x) {
                if (pos.y + obj.getr() > middle.y) {
                    numAdded++;
                    topLeft.add(obj);
                }
                if (pos.y - obj.getr() < middle.y) {
                    numAdded++;
                    topRight.add(obj);
                }
            }
            if (pos.x - obj.getr() < middle.x) {
                if (pos.y + obj.getr() > middle.y) {
                    numAdded++;
                    bottomLeft.add(obj);
                }
                if (pos.y - obj.getr() < middle.y) {
                    numAdded++;
                    bottomRight.add(obj);
                }
            }
            if (numAdded > 1) {
                doubled += numAdded - 1;
            }
            if (doubled > objects.size()) {
                break;
            }
        }
        if (doubled > objects.size()) {
            dumbCollide(collisions, objects);
        } else {
            recursiveCollide(collisions, topLeft);
            recursiveCollide(collisions, topRight);
            recursiveCollide(collisions, bottomLeft);
            recursiveCollide(collisions, bottomRight);
        }
    }

    public static void dumbCollide(HashMap<Entity, List<Entity>> collisions, List<Entity> objects) {
        for (Entity a : objects) {
            for (Entity b : objects) {
                if (a != b && Position.distance(a.getLocation(), b.getLocation()) < a.getr() + b.getr()) {
                    List<Entity> alreadyCollided = collisions.get(a);
                    if (alreadyCollided == null || !alreadyCollided.contains(b)) {
                        a.collide(b);
                        if (alreadyCollided == null) {
                            alreadyCollided = new LinkedList<Entity>();
                            collisions.put(a, alreadyCollided);
                        }
                        alreadyCollided.add(b);
                    }
                }
            }
        }
    }

    private static Position findMiddle(List<Entity> objects) {
        double x = 0.0;
        double y = 0.0;
        int count = 0;
        for (Entity obj : objects) {
            x += obj.getLocation().x;
            y += obj.getLocation().y;
            count++;
        }
        return new Position(x / count, y / count);
    }
}
