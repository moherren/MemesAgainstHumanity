package Graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SpriteHolder extends ArrayList<BufferedImage> {
	static ArrayList<File> Files = new ArrayList<File>();
	static ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	ArrayList<File> localFiles = new ArrayList<File>();

	public SpriteHolder(File image) throws IOException {
		loadImage(image);
	}

	public SpriteHolder(File[] list) throws IOException {
		for (File image : list) {
			loadImage(image);
		}
	}

	public SpriteHolder(String[] list) throws IOException {
		for (String str : list) {
			File image = new File(str);
			loadImage(image);
		}
	}
	
	public synchronized void loadImage(File image) throws IOException{
		int fileLoc = -1;
		for (File file : Files) {
			if (image.equals(file)) {
				fileLoc = Files.indexOf(file);
				break;
			}
		}
		if (fileLoc == -1) {
			BufferedImage BI = new BufferedImage(60, 60, 1);
			BI = ImageIO.read(image);
			add(BI);
			images.add(BI);
			Files.add(image);
		} else {
			add(images.get(fileLoc));
		}
	}

	public ArrayList<File> getLocalFiles() {
		return localFiles;
	}

	public  ArrayList<File> getFiles() {
		return Files;
	}

	private synchronized void setFiles(File[] list) throws IOException {
		clear();
		Files.clear();
		for (File file : list) {
			BufferedImage BI = new BufferedImage(60, 60, 1);
			BI = ImageIO.read(file);
			add(BI);
			Files.add(file);
		}
	}

	public void draw(int imageNum, Point2D point, double angle, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (imageNum > this.size() - 1)
			imageNum = this.size() - 1;
		g2.rotate(angle, point.getX(), point.getY());
		g2.drawImage(this.get(imageNum), (int) point.getX(),
				(int) point.getY(), null);
		g2.rotate(-angle, point.getX(), point.getY());
	}

	public void drawCentered(int imageNum, Point2D point, double angle,
			Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (imageNum > this.size() - 1)
			imageNum = this.size() - 1;
		angle = angle / 180 * Math.PI;
		g2.rotate(angle, point.getX(), point.getY());
		g2.drawImage(this.get(imageNum), (int) point.getX()
				- this.get(imageNum).getWidth() / 2, (int) point.getY()
				- this.get(imageNum).getHeight() / 2, null);
		g2.rotate(-angle, point.getX(), point.getY());
	}

}
