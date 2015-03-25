package makasprzak.idea.plugins.properties;

import makasprzak.idea.plugins.model.Property;

import java.util.List;

public interface PropertiesConsumer {
   void consume(List<Property> properties);
}
