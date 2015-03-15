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

    <caret>
}