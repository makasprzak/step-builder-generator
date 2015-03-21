package makasprzak.idea.plugins.model;

import com.google.common.base.Objects;

public class Property {
    private final String name;
    private final String type;

    private Property(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", name)
                .add("type", type)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property that = (Property) o;

        return Objects.equal(this.name, that.name) &&
                Objects.equal(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, type);
    }

    public static interface NameStep {
        TypeStep withName(String name);
    }

    public static interface TypeStep {
        BuildStep withType(String type);
    }

    public static interface BuildStep {
        Property build();
    }


    public static class Builder implements NameStep, TypeStep, BuildStep {
        private String name;
        private String type;

        private Builder() {
        }

        public static NameStep property() {
            return new Builder();
        }

        @Override
        public TypeStep withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public BuildStep withType(String type) {
            this.type = type;
            return this;
        }

        @Override
        public Property build() {
            return new Property(
                    this.name,
                    this.type
            );
        }
    }
}
