package net.sghill.rewrite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.marker.Markers;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.XmlVisitor;
import org.openrewrite.xml.tree.Content;
import org.openrewrite.xml.tree.Xml;

import java.util.ArrayList;
import java.util.List;

import static org.openrewrite.Tree.randomId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateParentPomVersion extends Recipe {

    @Option(displayName = "Parent's groupId",
            description = "groupId of the parent pom to update",
            example = "org.jenkins-ci.plugins")
    private String parentGroupId;
    
    @Option(displayName = "Parent's artifactId",
            description = "artifactId of the parent pom to update",
            example = "plugin")
    private String parentArtifactId;
    
    @Option(displayName = "Target version",
            description = "Version a matching parent pom will be updated to",
            example = "4.40")
    private String targetVersion;
    
    @Override
    public String getDisplayName() {
        return "Update a specified parent pom's version";
    }

    @Override
    public String getDescription() {
        return "If the pom has a matching parent, update its version";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new XmlVisitor<ExecutionContext>() {
            final XPathMatcher matcher = new XPathMatcher("/project/parent");

            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext ctx) {
                Xml.Tag t = (Xml.Tag) super.visitTag(tag, ctx);
                String commentText = "";
                if (matcher.matches(getCursor())) {
                    if (tag.getContent() != null) {
                        List<Content> contents = new ArrayList<>(tag.getContent());
                        boolean containsComment = contents.stream()
                                .anyMatch(c -> c instanceof Xml.Comment &&
                                        commentText.equals(((Xml.Comment) c).getText()));
                        if (!containsComment) {
                            int insertPos = 0;
                            Xml.Comment customComment = new Xml.Comment(randomId(),
                                    contents.get(insertPos).getPrefix(),
                                    Markers.EMPTY,
                                    commentText);
                            contents.add(insertPos, customComment);
                            t = t.withContent(contents);
                        }
                    }
                }
                return t;
            }
        };
    }
}
