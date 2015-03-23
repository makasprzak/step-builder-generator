package makasprzak.idea.plugins.model;

import com.google.common.base.Optional;
import org.junit.Test;

import static java.util.Arrays.asList;
import static makasprzak.idea.plugins.model.Pojo.Builder.pojo;
import static makasprzak.idea.plugins.model.Property.Builder.property;
import static org.fest.assertions.Assertions.assertThat;

public class PojoTest {

    private final Property firstProperty = property().withName("first").withType("String").build();
    private final Property secondProperty = property().withName("second").withType("String").build();
    private final Property thirdProperty = property().withName("third").withType("String").build();
    private final Pojo pojo = pojo()
            .withName("SomePojo")
            .withProperties(asList(
                    firstProperty,
                    secondProperty,
                    thirdProperty
            ))
            .withConstructorInjection(false)
            .build();
    private final Property notExistingProperty = property().withName("notExisting").withType("String").build();

    @Test
    public void shouldReturnSecondPropertyGivenFirst() throws Exception {
        assertThat(pojo.nextProperty(firstProperty).get()).isEqualTo(secondProperty);
    }

    @Test
    public void shouldReturnThirdPropertyGivenSecond() throws Exception {
        assertThat(pojo.nextProperty(secondProperty).get()).isEqualTo(thirdProperty);
    }

    @Test
    public void shouldReturnAbsentGivenLastProperty() throws Exception {
        assertThat(pojo.nextProperty(thirdProperty)).isEqualTo(Optional.absent());
    }

    @Test(expected = RuntimeException.class)
    public void shouldReportErrorGivenNotExistingProperty() throws Exception {
        pojo.nextProperty(notExistingProperty);
    }
}