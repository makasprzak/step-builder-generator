package makasprzak.idea.plugins.model;

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
