package makasprzak.idea.plugins;

import makasprzak.idea.plugins.model.Pojo;
import makasprzak.idea.plugins.model.StepBuilderPattern;

public interface BuilderPatternComposer {
    StepBuilderPattern build(Pojo pojo);
}
