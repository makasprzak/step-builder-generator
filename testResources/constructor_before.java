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

    <caret>
}