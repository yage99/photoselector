/**
 * 
 */
package com.yage.research.tools.picselector.model;

import java.io.File;

/**
 * @author zhangya
 *
 */
public class SelectableFile extends File {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2001803580592490066L;

	private boolean selected = false;
	private boolean deleted = false;
	private long seekPosition = 0;

	private int index;

	/**
	 * @param parent
	 * @param file
	 * @param selected
	 * @param deleted
	 */
	public SelectableFile(File parent, String file, boolean selected, boolean deleted) {
		super(parent, file);
		this.selected = selected;
		this.deleted = deleted;
	}

	/**
	 * @param parent
	 * @param file
	 */
	public SelectableFile(File parent, String file) {
		super(parent, file);
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the seekPosition
	 */
	long getSeekPosition() {
		return seekPosition;
	}

	/**
	 * @param seekPosition
	 *            the seekPosition to set
	 */
	void setSeekPosition(long seekPosition) {
		this.seekPosition = seekPosition;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * @param i
	 */
	public void setIndex(int i) {
		this.index = i;
	}

	/**
	 * @return
	 */
	public int getIndex() {
		return this.index;
	}

}
