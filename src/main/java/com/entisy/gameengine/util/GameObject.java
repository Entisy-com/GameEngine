package com.entisy.gameengine.util;

import com.entisy.gameengine.component.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> components;
    public Transform transform;

    public GameObject(String name) {
        this(name, new Transform());
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public <T extends Component> T getComponent(Class<T> clazz) {
        for (Component component : components) {
            if (clazz.isAssignableFrom(component.getClass())) {
                try {
                    return clazz.cast(component);
                }
                catch (ClassCastException exception) {
                    exception.printStackTrace();
                    assert false : "Error: casting component";
                }
            }
        };
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> clazz) {
        for (var i = 0; i < components.size(); i++) {
            if (clazz.isAssignableFrom(components.get(i).getClass())) {
                components.remove(i);
                return;
            }
        };
    }

    public void addComponent(Component component) {
        this.components.add(component);
        component.gameObject = this;
    }

    public void update(float dt) {
        for (Component component : components) {
            component.update(dt);
        }
    }

    public void start() {
        for (var component : components) {
            component.start();
        }
    }
}
