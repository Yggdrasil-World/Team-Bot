package de.overcraft;


// TODO: change into own subclasses
public class Sections {

    private final Section Builder;
    private final Section Developer;
    private final Section Storywriter;

    public Sections(Section builder, Section developer, Section storywriter) {
        Builder = builder;
        Developer = developer;
        Storywriter = storywriter;
    }

    public boolean isManager(long id) {
        return Builder.isManager(id) || Developer.isManager(id) || Storywriter.isManager(id);
    }

    public Section getSection(SectionEnum e) {
        switch (e) {
            case Builder -> {
                return getBuilder();
            }
            case Developer -> {
                return getDeveloper();
            }
            case Storywriter -> {
                return getStorywriter();
            }
            default -> {
                return null;
            }
        }
    }

    public Section getBuilder() {
        return Builder;
    }

    public Section getDeveloper() {
        return Developer;
    }

    public Section getStorywriter() {
        return Storywriter;
    }

    public enum SectionEnum {
        Builder,
        Developer,
        Storywriter,
    }


}
