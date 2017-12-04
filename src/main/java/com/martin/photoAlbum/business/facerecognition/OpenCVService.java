package com.martin.photoAlbum.business.facerecognition;

import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_core.CV_32SC1;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.BasicFaceRecognizer;

public class OpenCVService {
	private BasicFaceRecognizer faceRecognizer = createFisherFaceRecognizer();

	private OpenCVService() {}

	public void add(int id, Mat img) {
		Mat labels = new Mat(1, 1, CV_32SC1);
		IntBuffer labelsBuf = labels.createBuffer();
		labelsBuf.put(0, id);

		MatVector images = new MatVector(1);
		images.put(0, img);

		faceRecognizer.train(images, labels);
	}

	public int predict(BufferedImage bImg) {
		byte[] pixels = ((DataBufferByte) bImg.getRaster().getDataBuffer()).getData();
	}

	public int predict(Mat img) {
		IntPointer label = new IntPointer(1);
		DoublePointer confidence = new DoublePointer(1);
		faceRecognizer.predict(img, label, confidence);

		return label.get(0);
	}
}