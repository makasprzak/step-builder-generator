public class PojoWithSetters {
    private String name, lastName;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
        PojoWithSetters build();
    }


    public static class Builder implements NameStep, LastNameStep, AgeStep, BuildStep {
        private String name;
        private String lastName;
        private int age;

        private Builder() {
        }

        public static NameStep pojoWithSetters() {
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
        public PojoWithSetters build() {
            PojoWithSetters pojoWithSetters = new PojoWithSetters();
            pojoWithSetters.setName(this.name);
            pojoWithSetters.setLastName(this.lastName);
            pojoWithSetters.setAge(this.age);
            return pojoWithSetters;
        }
    }
}