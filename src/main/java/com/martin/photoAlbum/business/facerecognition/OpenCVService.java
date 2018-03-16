package com.martin.photoAlbum.business.facerecognition;

import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_core.CV_8UC3;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvLoad;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.BasicFaceRecognizer;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;
import java.net.URLClassLoader;

public class OpenCVService {
	private BasicFaceRecognizer faceRecognizer;
	private static final String FACE_DETECTION_XML = "./haarcascade_frontalface_default.xml";
	private CvHaarClassifierCascade faceDetectionCascade;
	private static volatile OpenCVService instance;

	private OpenCVService() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
        	System.out.println(url.getFile());
        }

		faceRecognizer = createFisherFaceRecognizer();
		initFaceDetection();
	}

	private void initFaceDetection() {
		faceDetectionCascade = new 
				CvHaarClassifierCascade(cvLoad(FACE_DETECTION_XML));
	}

	public void add(int id, Mat img) {
		Mat labels = new Mat(1, 1, CV_32SC1);
		IntBuffer labelsBuf = labels.createBuffer();
		labelsBuf.put(0, id);

		MatVector images = new MatVector(1);
		images.put(0, img);

		faceRecognizer.train(images, labels);
	}

	public Integer predict(BufferedImage bImg) {
		if (faceRecognizer.empty() || faceRecognizer.getLabels().empty()) {
			return null;
		}

		Mat mat = bufferedImageToMat(bImg);

		return predict(mat);
	}

	public int predict(Mat img) {
		IntPointer label = new IntPointer(1);
		DoublePointer confidence = new DoublePointer(1);
		faceRecognizer.predict(img, label, confidence);

		return label.get(0);
	}

	public static Mat bufferedImageToMat(BufferedImage bi) {
	    Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CV_8UC3);

	    int r, g, b;
	    UByteRawIndexer indexer = mat.createIndexer();
	    for (int y = 0; y < bi.getHeight(); y++) {
	        for (int x = 0; x < bi.getWidth(); x++) {
	            int rgb = bi.getRGB(x, y);

	            r = (byte) ((rgb >> 0) & 0xFF);
	            g = (byte) ((rgb >> 8) & 0xFF);
	            b = (byte) ((rgb >> 16) & 0xFF);

	            indexer.put(y, x, 0, r);
	            indexer.put(y, x, 1, g);
	            indexer.put(y, x, 2, b);
	        }
	    }
	    indexer.release();
	    return mat;
    }

	private IplImage toIplImage(BufferedImage bufImage) {
	    ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
	    Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
	    IplImage iplImage = iplConverter.convert(java2dConverter.convert(bufImage));
	    return iplImage;
	}

	public List<Rectangle> getFaces(BufferedImage bufImage) {
		IplImage iplImage = toIplImage(bufImage);

		return getFaces(iplImage);
	}

	public List<Rectangle> getFaces(IplImage src) {
		CvMemStorage storage = CvMemStorage.create();
		CvSeq sign = cvHaarDetectObjects(
				src,
				faceDetectionCascade,
				storage,
				1.5,
				3,
				CV_HAAR_DO_CANNY_PRUNING);

		cvClearMemStorage(storage);

		int totalFaces = sign.total();		

		List<Rectangle> faces = new ArrayList<>(totalFaces);

		for(int i = 0; i < totalFaces; i++){
			CvRect r = new CvRect(cvGetSeqElem(sign, i));
			Point pointA = new Point(r.x(), r.y());
			Point pointB = new Point(r.width() + r.x(), r.height() + r.y());
			Rectangle face = new Rectangle(pointA, pointB);

			faces.add(face);
			r.close();
		}

		return faces;
	}

	public static OpenCVService getInstance() {
		if (instance == null) {
			synchronized(OpenCVService.class) {
				if (instance == null) {
					instance = new OpenCVService();
				}
			}
		}

		return instance;
	}
}