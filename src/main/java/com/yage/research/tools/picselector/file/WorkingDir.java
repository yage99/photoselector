/**
 * 
 */
package com.yage.research.tools.picselector.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhangya
 *
 */
public class WorkingDir extends File implements Comparator<SelectableFile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7306774053601124455L;
	List<SelectableFile> fileList = new LinkedList<>();
	private int currentIndex;
	private File metaFile;

	/**
	 * @param parent
	 * @param child
	 * @param pics
	 * @throws IOException
	 */
	public WorkingDir(File parent, String[] pics) throws IOException {
		super(parent, ".picselect");

		if (!this.exists() || this.isDirectory()) {
			this.createNewFile();

			for (int i = 0; i < pics.length; i++) {
				SelectableFile file = new SelectableFile(parent, pics[i]);
				fileList.add(file);
			}

		} else {
			BufferedReader reader = new BufferedReader(new FileReader(this));

			String line = null;
			List<SelectableFile> piclist = new LinkedList<>();
			piclist.sort(this);
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				SelectableFile file = new SelectableFile(parent, values[0], "1".equals(values[1]),
						"1".equals(values[2]));
				piclist.add(file);
			}

			reader.close();

			for (String pic : pics) {
				Iterator<SelectableFile> iterator = piclist.iterator();

				boolean inList = false;
				while (iterator.hasNext()) {
					SelectableFile file = iterator.next();
					if (pic.equals(file.getName())) {
						fileList.add(file);
						piclist.remove(file);
						inList = true;

						break;
					}
				}

				if (!inList) {
					SelectableFile file = new SelectableFile(parent, pic);
					fileList.add(file);
				}
			}

			fileList.sort(this);
			construct();
		}

		this.metaFile = new File(parent, ".picmeta");
		if (this.metaFile.exists() && !this.metaFile.isDirectory()) {
			BufferedReader reader = new BufferedReader(new FileReader(this.metaFile));
			try {
				this.currentIndex = Integer.valueOf(reader.readLine());
			} catch (Exception e) {
				this.currentIndex = 0;
			}
			reader.close();

			if (this.currentIndex < 0) {
				setCurrentIndex(0);
			} else if (this.currentIndex >= fileList.size()) {
				setCurrentIndex(fileList.size() - 1);
			} else {
				setCurrentIndex(this.currentIndex);
			}
		} else {
			this.metaFile.createNewFile();
			setCurrentIndex(0);
		}
	}

	private void construct() throws IOException {
		int pos = 0;

		FileWriter writer = new FileWriter(this);
		for (int i = 0; i < fileList.size(); i++) {
			SelectableFile file = fileList.get(i);
			String name = file.getName();
			char se = file.isSelected() ? '1' : '0';
			char de = file.isDeleted() ? '1' : '0';
			writer.write(name + ",");
			pos += (name.length() + 1);
			file.setSeekPosition(pos);

			writer.write(se);
			writer.write(",");
			writer.write(de);
			writer.write("\n");
			pos += 4;
		}

		writer.flush();
		writer.close();
	}

	private void updateFile(SelectableFile file) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(this, "rw");
		raf.seek(file.getSeekPosition());
		char se = file.isSelected() ? '1' : '0';
		char de = file.isDeleted() ? '1' : '0';
		raf.write(se);
		raf.write(',');
		raf.write(de);
		raf.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(SelectableFile o1, SelectableFile o2) {
		return o1.getName().compareTo(o2.getName());
	}

	/**
	 * @return
	 */
	public int getFileLength() {
		return fileList.size();
	}

	/**
	 * @param indexOfFile
	 * @return
	 */
	public SelectableFile getFile(int indexOfFile) {
		return fileList.get(indexOfFile);
	}

	/**
	 * @param file
	 * @param isSelected
	 * @throws IOException
	 */
	public void setSelected(SelectableFile file, boolean isSelected) throws IOException {
		file.setSelected(isSelected);
		updateFile(file);
	}

	/**
	 * @param file
	 * @param isDeleted
	 * @throws IOException
	 */
	public void setDeleted(SelectableFile file, boolean isDeleted) throws IOException {
		file.setDeleted(isDeleted);
		updateFile(file);
	}

	/**
	 * @param indexOfPic
	 * @throws IOException
	 */
	public void setCurrentIndex(int indexOfPic) throws IOException {
		if (indexOfPic < 0 || indexOfPic >= fileList.size())
			return;

		this.currentIndex = indexOfPic;
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.metaFile));
		writer.write(this.currentIndex + "\n");
		writer.flush();
		writer.close();
	}

	/**
	 * @return the current working file
	 */
	public int getCurrentIndex() {
		return this.currentIndex;
	}

	/**
	 * @return
	 */
	public SelectableFile getCurrentFile() {
		if (fileList == null || getCurrentIndex() >= fileList.size())
			return null;
		return fileList.get(getCurrentIndex());
	}

}
