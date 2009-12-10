package org.villane.shttpc;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IterableNodeList implements Iterable<Node>, NodeList {

	public final NodeList nodeList;

	public IterableNodeList(NodeList nodeList) {
		this.nodeList = nodeList;
	}

	@Override
	public Iterator<Node> iterator() {
		return new NodeListIterator();
	}

	public int getLength() {
		return nodeList.getLength();
	}

	public Node item(int index) {
		return nodeList.item(index);
	}

	public class NodeListIterator implements Iterator<Node> {

		private int idx = 0;

		@Override
		public boolean hasNext() {
			return idx < nodeList.getLength();
		}

		@Override
		public Node next() {
			idx += 1;
			return nodeList.item(idx - 1);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
