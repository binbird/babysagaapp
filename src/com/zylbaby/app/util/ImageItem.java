package com.zylbaby.app.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;


public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	private Bitmap bitmap;
	public boolean isSelected = false;
	public File file;
	
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public Bitmap getBitmap() {		
		if(bitmap == null){
			try {
				bitmap = Bimp.revitionImageSize(imagePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	
	
	
	
}
