import java.util.ArrayList;
import java.util.List;


public class HashMapImplementation {

	public static void main(String[] args) {
		HashMapParticular h = new HashMapParticular();
		h.put(1, 100);
	}
	static class MapEntry {
		int key;
		int value;

		MapEntry(int key, int value) {
			this.key = key;
			this.value = value;
		}

		MapEntry() {
		}
	}

	// Implementing increasing size hashmap
	static class HashMapParticular {
		int key;
		int value;
		int BUCKET_SIZE = 1;
		static final double THRESHOLD = 0.75; // 0.75 * 16 = 12th element
		int totalInserts;
		ArrayList<MapEntry>[] entryList;

		HashMapParticular() {
			totalInserts = 0;
			entryList = new ArrayList[BUCKET_SIZE];
			for (int i = 0; i < BUCKET_SIZE; i++) {
				entryList[i] = new ArrayList<>();
			}
		}

		int put(int key, int value) {
			int hashCode = key % BUCKET_SIZE;
			double allowedElements = Math.floor(THRESHOLD * BUCKET_SIZE);
			if (entryList[hashCode].size() == 0) {
				System.out.println("allowedElements = " + allowedElements);
				System.out.println("totalInserts = " + totalInserts);
				System.out.println("BUCKET_SIZE = " + BUCKET_SIZE);
				
				if (allowedElements <= totalInserts) {// resize
					System.out.println("resizing");
					return resize(key,value);
				}
				totalInserts++;
				entryList[hashCode].add(new MapEntry(key, value));
			} else {
				boolean done = false;
				for (MapEntry m : entryList[hashCode]) {
					if (m.key == key) {
						m.value = value;
						done = true;
					}
				}
				if (!done) { // not replaced
					if (allowedElements <= totalInserts) {
						return resize(key,value);
					}
					totalInserts++;
					entryList[hashCode].add(new MapEntry(key, value));
				}
			}
			return 1; // operation done;
		}

		private int resize(int key,int value) { // key ,value to be inserted

			BUCKET_SIZE = BUCKET_SIZE == 0 ? 1 : BUCKET_SIZE << 1; // power of 2
			ArrayList<MapEntry>[] newEntryList = new ArrayList[BUCKET_SIZE];
			ArrayList<MapEntry>[] oldEntryList = new ArrayList[entryList.length];
			
			for (int i = 0; i < entryList.length; i++) {
				oldEntryList[i] = new ArrayList<>();
				ArrayList<MapEntry> list = entryList[i];
				if (list != null && list.size() > 0) {
					for (MapEntry entry : list) {
						System.out.print(entry.key + " : " + entry.value + ",");
						oldEntryList[i].add(entry);
					}
				}
			}

			entryList = newEntryList;
			totalInserts = 0;
			for (int i = 0; i < BUCKET_SIZE; i++) {
				newEntryList[i] = new ArrayList<>();
			}

			for (List<MapEntry> list : oldEntryList) {
				if (list != null && list.size() > 0) {
					for (MapEntry entry : list) {
						put(entry.key, entry.value); // rehash and put value
					}
				}
			}
			
			put(key,value); // newOnes
			// TotalInserts after resizing =
			System.out.println("TotalInserts after resizing = "+ totalInserts);
			return -1;
		}

		int get(int key) {
			int hashCode = key % BUCKET_SIZE;
			if (entryList[hashCode].size() == 0) {
				return -1; // no key found
			} else {
				boolean done = false;
				for (MapEntry m : entryList[hashCode]) {
					if (m.key == key) {
						return m.value; // value is there
					}
				}
				return -1;
			}
		}

		int remove(int key) {
			int hashCode = key % BUCKET_SIZE;
			if (entryList[hashCode].size() == 0) {
				return -1; // nothing to remove;
			} else {
				int indexToRemove = -1;
				for (int i = 0; i < entryList[hashCode].size(); i++) {
					if (entryList[hashCode].get(i).key == key) {
						indexToRemove = i;
						break;
					}
				}
				if (indexToRemove != -1) { // not replcced
					totalInserts--;
					int val = entryList[hashCode].get(indexToRemove).value;
					entryList[hashCode].remove(indexToRemove);
					return val; // index val
				}
			}
			return -1; // operation done;
		}
	}
}
