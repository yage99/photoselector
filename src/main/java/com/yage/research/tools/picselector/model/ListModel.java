/**
 * 
 */
package com.yage.research.tools.picselector.model;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangya
 *
 */
public class ListModel {

	private TreeMap<String, TreeSet<SelectableFile>> selectedList = new TreeMap<>();
	private Pattern pattern = null;

	/**
	 * @param pattern
	 * @param selectedFiles
	 */
	public ListModel(String pattern) {
		this.pattern = Pattern.compile(pattern);

	}

	/**
	 * @param file
	 */
	public void add(SelectableFile file) {
		String id = getId(file.getName());

		TreeSet<SelectableFile> fileList = selectedList.get(id);
		if (fileList == null) {
			fileList = new TreeSet<>();
			fileList.add(file);
			selectedList.put(id, fileList);
		} else {
			if (fileList.contains(file))
				return;
			fileList.add(file);
		}
	}

	/**
	 * @param file
	 */
	public void remove(SelectableFile file) {
		String id = getId(file.getName());
		TreeSet<SelectableFile> fileList = selectedList.get(id);
		if (fileList == null) {
			return;
		} else {
			fileList.remove(file);
			if (fileList.size() == 0) {
				selectedList.remove(id);
			}
		}
	}

	/**
	 * @param item
	 * @return
	 */
	public int getPositionByList(String item) {
		String id = getId(item);
		if (id == null || "".equals(id)) {
			return -1;
		}

		TreeSet<SelectableFile> fileList = selectedList.get(id);
		return fileList.first().getIndex();
	}

	/**
	 * @param file
	 * @return
	 */
	public int getListByFile(SelectableFile file) {
		if (file == null)
			return -1;
		String id = getId(file.getName());
		Iterator<String> iterator = selectedList.keySet().iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			if (id.equals(iterator.next())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param name
	 * @return
	 */
	public String getId(String name) {
		if (pattern == null) {
			return name;
		}

		Matcher matcher = pattern.matcher(name);
		if (!matcher.find()) {
			return "";
		}
		return matcher.group();
	}

	/**
	 * @return
	 */
	public String[] getListItems() {
		String[] result = new String[selectedList.size()];
		Iterator<Entry<String, TreeSet<SelectableFile>>> keyiter = selectedList.entrySet().iterator();
		for (int i = 0; keyiter.hasNext(); i++) {
			Entry<String, TreeSet<SelectableFile>> entry = keyiter.next();
			result[i] = entry.getKey() + " (" + entry.getValue().size() + ")";
		}

		return result;
	}
}
