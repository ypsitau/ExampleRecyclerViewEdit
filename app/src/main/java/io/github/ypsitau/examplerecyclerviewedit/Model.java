package io.github.ypsitau.examplerecyclerviewedit;

import java.util.ArrayList;
import java.util.List;

public class Model {
	static Model modelInst;

	public static class Item {
		public String label;

		public Item(String label) {
			this.label = label;
		}
	}

	private List<Item> items = new ArrayList<Item>();

	public static Model getInstance() {
		if (modelInst == null) modelInst = new Model();
		return modelInst;
	}

	public void initContent() {
		for (int i = 0; i < 5; i++) {
			items.add(new Item(String.format("item #%d", i)));
		}
	}

	public Item addItem(String label) {
		Item item = new Item(label);
		items.add(item);
		return item;
	}

	public void moveItem(int toPos, int fromPos) {
		items.add(toPos, items.remove(fromPos));
	}

	public void removeItem(int pos) { items.remove(pos); }

	public Item getItem(int pos) { return items.get(pos); }

	public List<Item> getItems() { return items; }

	public int getItemCount() { return items.size(); }
}
