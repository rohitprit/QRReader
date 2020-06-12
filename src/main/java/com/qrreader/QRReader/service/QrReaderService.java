package com.qrreader.QRReader.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.qrreader.QRReader.utils.Constants;

@Service
public class QrReaderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QrReaderService.class);
	@Async
    public CompletableFuture<String> saveQRCodes(final BufferedImage im,final int thCount) throws Exception {
        final long start = System.currentTimeMillis();
        
        //url="number of pixels,Decoded QRCode"
        String url=decodeQRCode(im);//"http://www.iitk.ac.in/doaa/component/finder/search?q=cse&t%5B%5D=&Itemid=835";
        
        LOGGER.info("Thread: "+Thread.currentThread().getName()+"Elapsed time: {}", (System.currentTimeMillis() - start));
//        System.out.println("Thread: "+Thread.currentThread().getName()+"Elapsed time: "+(System.currentTimeMillis() - start));
        return CompletableFuture.completedFuture(url);
    }
	
  /*  public String saveQRCodes(final BufferedImage im,final int thCount) throws Exception {
        final long start = System.currentTimeMillis();
        //url="number of pixels,Decoded QRCode"
        String url=decodeQRCode(im);//"http://www.iitk.ac.in/doaa/component/finder/search?q=cse&t%5B%5D=&Itemid=835";
        LOGGER.info("Thread: "+Thread.currentThread().getName()+"Elapsed time: {}", (System.currentTimeMillis() - start));
//        System.out.println("Thread: "+Thread.currentThread().getName()+"Elapsed time: "+(System.currentTimeMillis() - start));
        return url;
    }*/
	
	// We are looking for black/white/black/white/black modules in
    // 1:1:3:1:1 ratio; this tracks the number of such modules seen so far
	private static String decodeQRCode(BufferedImage bufferedImage) throws NotFoundException {
		int numOfBPixels=0;
	    try {
		LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	   
	    BitMatrix image= bitmap.getBlackMatrix();
	    numOfBPixels=CountSetBit(image);
	    
	    Result result = new MultiFormatReader().decode(bitmap);
//	    int numOfBPixels1=CountSetBit(extractPureBits(image)); //For counting exact black pixels in the QRCode
	    return numOfBPixels+","+result.getText();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	LOGGER.error(":decodeQRCode: "+e.getMessage());
	    	return numOfBPixels+","+Constants.ErrorInDecode;
			// TODO: handle exception
		}
	}
	
	private static int CountSetBit(BitMatrix image) {
		int blackBits=0;
		int maxI = image.getHeight();
	    int maxJ = image.getWidth();
	    for (int i = 0; i < maxI; i ++) {
	        for (int j = 0; j < maxJ; j++) {
	          if (image.get(j, i)) {
	        	  blackBits++;
	          }}}
		return blackBits;
	}
	
	/*private static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {

	    int[] leftTopBlack = image.getTopLeftOnBit();
	    int[] rightBottomBlack = image.getBottomRightOnBit();
	    if (leftTopBlack == null || rightBottomBlack == null) {
	      throw NotFoundException.getNotFoundInstance();
	    }

	    float moduleSize = moduleSize(leftTopBlack, image);

	    int top = leftTopBlack[1];
	    int bottom = rightBottomBlack[1];
	    int left = leftTopBlack[0];
	    int right = rightBottomBlack[0];
	    // Sanity check!
	    if (left >= right || top >= bottom) {
	      throw NotFoundException.getNotFoundInstance();
	    }

	    if (bottom - top != right - left) {
	      // Special case, where bottom-right module wasn't black so we found something else in the last row
	      // Assume it's a square, so use height as the width
	      right = left + (bottom - top);
	      if (right >= image.getWidth()) {
	        // Abort if that would not make sense -- off image
	        throw NotFoundException.getNotFoundInstance();
	      }
	    }

	    int matrixWidth = Math.round((right - left + 1) / moduleSize);
	    int matrixHeight = Math.round((bottom - top + 1) / moduleSize);
	    if (matrixWidth <= 0 || matrixHeight <= 0) {
	      throw NotFoundException.getNotFoundInstance();
	    }
	    if (matrixHeight != matrixWidth) {
	      // Only possibly decode square regions
	      throw NotFoundException.getNotFoundInstance();
	    }

	    // Push in the "border" by half the module width so that we start
	    // sampling in the middle of the module. Just in case the image is a
	    // little off, this will help recover.
	    int nudge = (int) (moduleSize / 2.0f);
	    top += nudge;
	    left += nudge;
	    
	    // But careful that this does not sample off the edge
	    // "right" is the farthest-right valid pixel location -- right+1 is not necessarily
	    // This is positive by how much the inner x loop below would be too large
	    int nudgedTooFarRight = left + (int) ((matrixWidth - 1) * moduleSize) - right;
	    if (nudgedTooFarRight > 0) {
	      if (nudgedTooFarRight > nudge) {
	        // Neither way fits; abort
	        throw NotFoundException.getNotFoundInstance();
	      }
	      left -= nudgedTooFarRight;
	    }
	    // See logic above
	    int nudgedTooFarDown = top + (int) ((matrixHeight - 1) * moduleSize) - bottom;
	    if (nudgedTooFarDown > 0) {
	      if (nudgedTooFarDown > nudge) {
	        // Neither way fits; abort
	        throw NotFoundException.getNotFoundInstance();
	      }
	      top -= nudgedTooFarDown;
	    }

	    // Now just read off the bits
	    BitMatrix bits = new BitMatrix(matrixWidth, matrixHeight);
	    for (int y = 0; y < matrixHeight; y++) {
	      int iOffset = top + (int) (y * moduleSize);
	      for (int x = 0; x < matrixWidth; x++) {
	        if (image.get(left + (int) (x * moduleSize), iOffset)) {
	          bits.set(x, y);
	        }
	      }
	    }
	    return bits;
	  }

	  private static float moduleSize(int[] leftTopBlack, BitMatrix image) throws NotFoundException {
	    int height = image.getHeight();
	    int width = image.getWidth();
	    int x = leftTopBlack[0];
	    int y = leftTopBlack[1];
	    boolean inBlack = true;
	    int transitions = 0;
	    while (x < width && y < height) {
	      if (inBlack != image.get(x, y)) {
	        if (++transitions == 5) {
	          break;
	        }
	        inBlack = !inBlack;
	      }
	      x++;
	      y++;
	    }
	    if (x == width || y == height) {
	      throw NotFoundException.getNotFoundInstance();
	    }
	    return (x - leftTopBlack[0]) / 7.0f;
	  }*/
	
}
