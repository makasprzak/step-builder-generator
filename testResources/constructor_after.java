public class PojoWithConstructor {
    private final String name, lastName;
    private final int age;

    public PojoWithConstructor(String name, String lastName, int age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public static interface NameStep {
        LastNameStep withName(String name);
    }

    public static interface LastNameStep {
        AgeStep withLastName(String lastName);
    }

    public static interface AgeStep {
        BuildStep withAge(int age);
    }

    public static interface BuildStep {
        PojoWithConstructor build();
    }

    public static class Builder implements NameStep, LastNameStep, AgeStep, BuildStep {
        private String name;
        private String lastName;
        private int age;

        private Builder() {
        }

        public static NameStep pojoWithConstructor() {
            return new Builder();
        }

        @Override
        public LastNameStep withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public AgeStep withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        @Override
        public BuildStep withAge(int age) {
            this.age = age;
            return this;
        }

        @Override
        public PojoWithConstructor build() {
            PojoWithConstructor pojoWithConstructor = new PojoWithConstructor(
                    this.name,
                    this.lastName,
                    this.age
            );
            return pojoWithConstructor;
        }
    }
}