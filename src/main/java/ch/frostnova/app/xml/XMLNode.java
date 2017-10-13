package ch.frostnova.app.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author pwalser
 * @since 13.10.2017.
 */
public class XMLNode {

    private final Node node;

    public XMLNode(Node node) {
        this.node = node;
    }

    public String getName() {
        return node.getNodeName();
    }

    public String getText() {
        return node.getTextContent();
    }

    public String getTrimmedText() {
        String text = getText();
        return text != null ? text.trim() : null;
    }

    public String getAttribute(String name) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) {
            return null;
        }
        Node attribute = attributes.getNamedItem(name);
        return attribute == null ? null : attribute.getTextContent();
    }

    public XMLNode getChild(String childNodeName) {
        for (XMLNode node : getChildren(n -> childNodeName.equals(n.getNodeName()))) {
            return node;
        }
        return null;
    }

    public Iterable<XMLNode> getChildren() {
        return getChildren(n -> true);
    }

    public Iterable<XMLNode> getChildren(String name) {
        return getChildren(n -> name.equals(n.getNodeName()));
    }

    public Iterable<XMLNode> getChildren(Predicate<Node> filter) {
        List<XMLNode> result = new LinkedList<>();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node item = children.item(i);
            if (filter == null || filter.test(item)) {
                result.add(new XMLNode(item));
            }
        }
        return result;
    }
}
