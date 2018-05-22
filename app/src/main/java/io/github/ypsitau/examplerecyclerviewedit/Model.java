package io.github.ypsitau.examplerecyclerviewedit;

import java.util.ArrayList;
import java.util.List;

public class Model {
	private static Model modelInst;
	public static final int FIELD_NONE = -1;
	public static final int FIELD_LABEL = 0;
	public static final int FIELD_PRICE = 1;

	public static class Item {
		private String label;
		private int price;

		public Item(String label, int price) {
			this.label = label;
			this.price = price;
		}
		public String getLabel() { return label; }
		public int getPrice() { return price; }
		public void setValues(String label, int price) { this.label = label; this.price = price; }
	}

	private List<Item> items = new ArrayList<Item>();

	public static Model getInstance() {
		if (modelInst == null) modelInst = new Model();
		return modelInst;
	}

	public void initContent() {
		for (int i = 0; i < 5; i++) {
			addItem(String.format("item #%d", i), 100);
		}
	}

	public void addItem(String label, int price) {
		items.add(new Item(label, price));
	}

	public void moveItem(int toPos, int fromPos) {
		items.add(toPos, items.remove(fromPos));
	}

	public void removeItem(int pos) { items.remove(pos); }

	public Item getItem(int pos) { return items.get(pos); }

	public List<Item> getItems() { return items; }

	public int getItemCount() { return items.size(); }

	public static String formatPrice(int price) { return String.format("%d", price); }
}
