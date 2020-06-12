package com.qrreader.QRReader.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.opencsv.CSVWriter;
import com.qrreader.QRReader.service.QrReaderService;
import com.qrreader.QRReader.utils.Constants;
import com.qrreader.QRReader.utils.WriteCsv;

@Controller
@EnableCaching
@EnableScheduling
@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST })
public class QrReaderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QrReaderController.class);

	@Autowired
	QrReaderService qrReaderService;

	@RequestMapping(value = Constants.QRCODE_ENDPOINT, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<Object> getQrCode(@RequestParam("fileName") String fileName,
//			@RequestParam("file") MultipartFile file,
			HttpServletResponse response) throws Exception {
//		System.out.println("Hello");

//		String filename = file.getOriginalFilename();
//		if (filename != null) {
//			filename = URLEncoder.encode(filename);
//		}
//		InputStream inputStream = new FileInputStream(filename);
//		byte[] bytes=file.getBytes();
		String path = Constants.PATH + fileName;
		BufferedImage[] si = splitImage(path);

		try {
			StringBuffer res = new StringBuffer();
			for (int i = 0; i < si.length; i++) {
				res.append(qrReaderService.saveQRCodes(si[i], i).get());
				res.append(",");
			}
			res.setLength(res.length() - 1);
//			res.replace(res.length()-1, res.length(), "");
			CSVWriter writer = WriteCsv.getInstance().getCSVWriter();
			writer.writeNext(new String[] { res.toString() });
			writer.flush();
//			System.out.println("result: "+res.toString());
			LOGGER.info("result: " + res.toString());
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (final Exception e) {
			LOGGER.error(this.getClass().getName() + ":getQrCode");
//			System.out.println("Error");
//			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

//		InputStream inputStream = new FileInputStream(path);
//		System.out.println("Hello fileLocation: "+path);
//		byte[] bytes = IOUtils.toByteArray(inputStream);
//		org.apache.commons.io.IOUtils.copy(
//				new ByteArrayInputStream(bytes),
//				response.getOutputStream());
////		org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
//		inputStream.close();
//		response.flushBuffer();
//		response.setContentType("APPLICATION/OCTET-STREAM");
//		String disHeader = "Attachment";
//		response.setHeader("Content-Disposition", disHeader);
//		response.setContentLengthLong(1000l);
	}

	public BufferedImage[] splitImage(String path) throws IOException {
//		Image[] si = new Image[4];
		BufferedImage[] si = new BufferedImage[4];
		BufferedImage bimg = ImageIO.read(new File(path));
		int w = bimg.getWidth();
		int h = bimg.getHeight();
		for (int i = 0; i < 4; i++) {
			BufferedImage wim = bimg.getSubimage(i * w / 4, 0, w / 4, h);
//			Image sc = wim.getScaledInstance(w / 4, h, Image.SCALE_AREA_AVERAGING);
			si[i] = wim;
		}
		return si;
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
		return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
	}

	
	/*private ArrayList<Bitmap> divideImages(String path) { ArrayList<Bitmap> bs =
	  new ArrayList<Bitmap>(); Bitmap b =
	  BitmapFactory.decodeResource(getResources(), R.drawable.photo1); final int
	  width = b.getWidth(); final int height = b.getHeight(); final int pixelByCol
	  = width / 4; final int pixelByRow = height; // List<Bitmap> bs = new
	  ArrayList<Bitmap>(); for (int i = 0; i < 4; i++) {
	  System.out.println("Column no." + j); int startx = pixelByCol * i; int starty
	  = 0; Bitmap b1 = Bitmap.createBitmap(b, startx, starty, pixelByCol,
	  pixelByRow); bs.add(b1); b1 = null; } b = null; return bs; }

	public Bitmap[] splitBitmap(Bitmap picture) {
		int w = picture.getWidth();
		int h = picture.getHeight();
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, w, h, true);
		Bitmap[] imgs = new Bitmap[4];
		imgs[0] = Bitmap.createBitmap(scaledBitmap, 0, 0, w / 4, h);
		imgs[1] = Bitmap.createBitmap(scaledBitmap, w / 4, 0, w / 4, h);
		imgs[2] = Bitmap.createBitmap(scaledBitmap, 2 * w / 4, 0, w / 4, h);
		imgs[3] = Bitmap.createBitmap(scaledBitmap, 3 * w / 4, 0, w / 4, h);
		return imgs;
	}*/
	 
}
