import static java.util.Arrays.asList;

public class PojoWithConstructor {
    private final String name, lastName;
    private final Integer age;
    private final List<String> friends;

    public PojoWithConstructor(String name, String lastName, int age, String ... friends) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.friends = asList(friends);
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

    public List<String> getFriends() {
        return friends;
    }

    public static interface NameStep {
        LastNameStep withName(String name);
    }

    public static interface LastNameStep {
        AgeStep withLastName(String lastName);
    }

    public static interface AgeStep {
        FriendsStep withAge(int age);
    }

    public static interface FriendsStep {
        BuildStep withFriends(String ... friends);
    }

    public static interface BuildStep {
        PojoWithConstructor build();
    }


    public static class Builder implements NameStep, LastNameStep, AgeStep, FriendsStep, BuildStep {
        private String name;
        private String lastName;
        private int age;
        private String[] friends;

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
        public FriendsStep withAge(int age) {
            this.age = age;
            return this;
        }

        @Override
        public BuildStep withFriends(String ... friends) {
            this.friends = friends;
            return this;
        }

        @Override
        public PojoWithConstructor build() {
            return new PojoWithConstructor(
                    this.name,
                    this.lastName,
                    this.age,
                    this.friends
            );
        }
    }
}