package com.projectomega.main.plugin.loader.dependency;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a runtime-downloaded dependency
 */
@EqualsAndHashCode @ToString
public class Dependency {

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s";

    private final String groupId, artifactId, version;

    private final String mavenPath, name;

    public Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        mavenPath = String.format(MAVEN_FORMAT,
                groupId.replace('.', '/'), artifactId, version, artifactId, version);
        name = artifactId + "-" + version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getMavenPath() {
        return mavenPath;
    }

    public String getName() {
        return name;
    }

    public List<Dependency> getTransitiveDependencies(Repository repository) {
        List<Dependency> dependencies = new ArrayList<>();
        try (InputStream stream = repository.getPom(this).openStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(stream);
            NodeList list = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeName().equals("dependencies")) {
                    NodeList deps = node.getChildNodes();
                    for (int j = 0; j < deps.getLength(); j++) {
                        Node n = deps.item(j);
                        if (!(n instanceof Element)) continue;
                        Element dependency = (Element) n;
                        String groupId = dependency.getElementsByTagName("groupId").item(0).getTextContent();
                        String artifactId = dependency.getElementsByTagName("artifactId").item(0).getTextContent();
                        String version = "";
                        String scope = "";
                        try {
                            version = dependency.getElementsByTagName("version").item(0).getTextContent();
                            scope = dependency.getElementsByTagName("scope").item(0).getTextContent();
                        } catch (NullPointerException ignored) {
                        }
                        if (groupId.equals("${project.groupId}")) groupId = this.groupId;
                        if (version.equals("${project.version}")) version = this.version;
                        if (scope.equals("compile") && !version.isEmpty()) {
                            dependencies.add(new Dependency(groupId, artifactId, version));
                        }
                    }
                    break;
                }
            }
        } catch (Throwable e) {
            if (!(e instanceof FileNotFoundException)) e.printStackTrace();
        }
        return dependencies;
    }
}
