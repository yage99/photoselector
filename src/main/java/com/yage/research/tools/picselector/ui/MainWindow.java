/**
 * 
 */
package com.yage.research.tools.picselector.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.yage.research.tools.picselector.file.SelectableFile;
import com.yage.research.tools.picselector.file.WorkingDir;

/**
 * @author zhangya
 *
 */
public class MainWindow {

	protected Shell shell;
	private File folder;
	private Canvas canvas;
	private SelectableFile currentFile;

	private int currentIndex = 0;
	private WorkingDir workingDir;

	/**
	 * Open the window.
	 * 
	 * @param path
	 */
	public void open(String path) {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		changePath(path);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * @param path
	 */
	public void changePath(String path) {
		this.folder = new File(path);

		String[] pics = folder.list(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.endsWith("tiff") || name.endsWith("png") || name.endsWith("jpg")) {
					return true;
				}
				return false;
			}
		});

		try {
			this.workingDir = new WorkingDir(this.folder, pics);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setPicIndex(0);
	}

	/**
	 * @param indexOfPic
	 */
	public void setPicIndex(int indexOfPic) {
		if (indexOfPic < 0 || workingDir == null || indexOfPic >= workingDir.getFileLength())
			return;

		workingDir.setCurrentIndex(indexOfPic);
		this.currentFile = workingDir.getFile(indexOfPic);

		canvas.redraw();

		currentIndex = indexOfPic;
	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(569, 441);
		shell.setText("SWT Application");
		FillLayout fl_shell = new FillLayout(SWT.VERTICAL);
		fl_shell.spacing = 5;
		fl_shell.marginWidth = 10;
		fl_shell.marginHeight = 10;
		shell.setLayout(fl_shell);

		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);

		MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
		fileItem.setText("&File");

		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(submenu);
		MenuItem item = new MenuItem(submenu, SWT.PUSH);
		item.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				DirectoryDialog fd = new DirectoryDialog(shell);
				fd.setText("Open");
				String selected = fd.open();
				changePath(selected);
			}
		});
		item.setText("&Open \tCtrl+O");
		item.setAccelerator(SWT.MOD1 + 'O');

		MenuItem actionItem = new MenuItem(menuBar, SWT.CASCADE);
		actionItem.setText("&Action");

		Menu actionsMenu = new Menu(shell, SWT.DROP_DOWN);
		actionItem.setMenu(actionsMenu);

		MenuItem nextAction = new MenuItem(actionsMenu, SWT.PUSH);
		nextAction.setText("&Next");
		// nextAction.setAccelerator(SWT.ARROW_RIGHT);
		nextAction.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				nextPic();
			}

		});

		MenuItem prevAction = new MenuItem(actionsMenu, SWT.PUSH);
		prevAction.setText("&Prev");
		// prevAction.setAccelerator(SWT.ARROW_LEFT);
		prevAction.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				prevPic();
			}

		});

		MenuItem selectAction = new MenuItem(actionsMenu, SWT.PUSH);
		selectAction.setText("&Select");
		selectAction.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				selectCurrentPic();
			}

		});

		this.canvas = new Canvas(shell, SWT.NONE);
		canvas.addKeyListener(new KeyListener() {

			private int indexBuffer = 0;

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			public void keyPressed(KeyEvent e) {
				// System.out.println(e.keyCode);
				if (e.keyCode == 32) {
					selectCurrentPic();
				} else if (e.keyCode == 'd') {
					delCurrentPic();
				} else if (e.keyCode == 'k') {
					prevPic();
				} else if (e.keyCode == 'j') {
					nextPic();
				} else if (e.keyCode >= '0' && e.keyCode <= '9') {
					this.indexBuffer *= 10;
					this.indexBuffer += e.keyCode - '0';
				} else if (e.keyCode == 'g') {
					setPicIndex(this.indexBuffer);
					this.indexBuffer = 0;
				}
			}
		});
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (currentFile == null)
					return;

				if (e.x != 10 || e.y != 10 || e.width != 10 || e.height != 10) {
					Image image = new Image(Display.getDefault(), currentFile.getAbsolutePath());
					float srcWidth = image.getBounds().width;
					float srcHeight = image.getBounds().height;
					int targetWidth = canvas.getBounds().width;
					int targetHeight = canvas.getBounds().height;
					float widthRatio = targetWidth / srcWidth;
					float heightRatio = targetHeight / srcHeight;
					if (widthRatio > heightRatio) {
						targetWidth = (int) (heightRatio * srcWidth);
					} else {
						targetHeight = (int) (widthRatio * srcHeight);
					}

					int thumbX = targetWidth + 10;
					int thumbY = 0;
					int thumbWidth = canvas.getBounds().width - thumbX;
					if (thumbWidth > 200) {
						thumbWidth = 200;
					}
					int thumbHeight = (int) (thumbWidth / srcWidth * srcHeight);
					e.gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, targetWidth,
							targetHeight);
					e.gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, thumbX, thumbY,
							thumbWidth, thumbHeight);
					e.gc.setForeground(new Color(Display.getDefault(), 0, 0, 0));
					e.gc.drawText(String.format("%d / %d\n%s", currentIndex, workingDir.getFileLength(),
							currentFile.getName()), thumbX, thumbHeight + 10);
				}
				if (currentFile.isSelected()) {
					e.gc.setBackground(new Color(Display.getDefault(), 0, 255, 0));
					e.gc.fillRoundRectangle(10, 10, 10, 10, 10, 10);
				}
				if (currentFile.isDeleted()) {
					e.gc.setBackground(new Color(Display.getDefault(), 255, 0, 0));
					e.gc.fillRoundRectangle(10, 10, 10, 10, 10, 10);
				}
			}
		});
	}

	protected void nextPic() {
		setPicIndex(currentIndex + 1);
	}

	protected void prevPic() {
		setPicIndex(currentIndex - 1);
	}

	protected void selectCurrentPic() {
		try {
			workingDir.setSelected(currentFile, !currentFile.isSelected());
			if (currentFile.isSelected()) {
				workingDir.setDeleted(currentFile, false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		canvas.redraw(10, 10, 10, 10, false);
	}

	protected void delCurrentPic() {
		try {
			workingDir.setDeleted(currentFile, !currentFile.isDeleted());
			if (currentFile.isDeleted()) {
				workingDir.setSelected(currentFile, false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		canvas.redraw(10, 10, 10, 10, false);
	}
}