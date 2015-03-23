package makasprzak.idea.plugins.model;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import java.util.List;

public class Pojo {
    private final String name;
    private final List<Property> properties;
    private final boolean constructorInjection;

    private Pojo(String name,
                 List<Property> properties, 
                 boolean constructorInjection) {
        this.name = name;
        this.properties = properties;
        this.constructorInjection = constructorInjection;
    }

    public String getName() {
        return name;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public boolean isConstructorInjection() {
        return constructorInjection;
    }

    public Optional<Property> nextProperty(Property property) {
        if (!properties.contains(property)) throw new RuntimeException(property + " not found in " + this);
        int nextPropertyIndex = properties.indexOf(property) + 1;
        if (nextPropertyIndex == properties.size()) {
            return Optional.absent();
        } else {
            return Optional.of(properties.get(nextPropertyIndex));
        }
    }

    public static interface NameStep {
        PropertiesStep withName(String name);
    }

    public static interface PropertiesStep {
        ConstructorInjectionStep withProperties(List<Property> properties);
    }

    public static interface ConstructorInjectionStep {
        BuildStep withConstructorInjection(boolean constructorInjection);
    }

    public static interface BuildStep {
        Pojo build();
    }


    public static class Builder implements NameStep, PropertiesStep, ConstructorInjectionStep, BuildStep {
        private String name;
        private List<Property> properties;
        private boolean constructorInjection;

        private Builder() {
        }

        public static NameStep pojo() {
            return new Builder();
        }

        @Override
        public PropertiesStep withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public ConstructorInjectionStep withProperties(List<Property> properties) {
            this.properties = properties;
            return this;
        }

        @Override
        public BuildStep withConstructorInjection(boolean constructorInjection) {
            this.constructorInjection = constructorInjection;
            return this;
        }

        @Override
        public Pojo build() {
            return new Pojo(
                    this.name,
                    this.properties,
                    this.constructorInjection
            );
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("name", name)
                    .add("properties", properties)
                    .add("constructorInjection", constructorInjection)
                    .toString();
        }
    }
}
