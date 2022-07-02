import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Node {
    private String value;
    private Node leftChild;
    private Node rightChild;
}
